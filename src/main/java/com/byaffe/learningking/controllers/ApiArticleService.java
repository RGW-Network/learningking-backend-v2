package com.byaffe.learningking.controllers;

import com.byaffe.learningking.models.Article;
import com.byaffe.learningking.models.Member;
import com.byaffe.learningking.models.courses.ArticleType;
import com.byaffe.learningking.models.courses.Company;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.services.ArticleService;
import com.byaffe.learningking.services.CompanyService;
import com.byaffe.learningking.services.impl.ArticleServiceImpl;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.byaffe.systems.api.models.ApiUserModel;
import org.byaffe.systems.api.models.BaseQueryParamModel;
import org.byaffe.systems.core.services.*;
import org.byaffe.systems.models.LookUpValue;
import org.byaffe.systems.models.LookupType;
import org.byaffe.systems.models.courses.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.utils.SortField;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Ray Gdhrt
 */

@Slf4j
@RestController
@RequestMapping("/v1/articles")
public class ApiArticleService {

    @PostMapping("/")
    public ResponseEntity<ResponseList<Article>> getArticles(
            @RequestParam String searchTerm,
            @RequestParam int offset, @RequestParam int limit,
            @RequestParam String sortBy, @RequestParam Boolean sortDescending,
            @RequestParam String type, @RequestParam Boolean featured,
            @RequestParam Long categoryId, @RequestParam String academy,
            @RequestParam Long companyId) throws ValidationException {
        Search search = ArticleServiceImpl.generateSearchTermsForArticles(searchTerm).addFilterEqual("publicationStatus", PublicationStatus.ACTIVE);

        if (StringUtils.isNotEmpty(type)) {
            search.addFilterEqual("type", ArticleType.valueOf(type));
        }
        if (StringUtils.isNotEmpty(type)) {
            search.addFilterEqual("category.academy", academy);
        }
        if (categoryId > 0) {
            search.addFilterEqual("category.id", categoryId);
        }
        if (featured != null) {
            search.addFilterEqual("isFeatured", featured);
        }
        if (StringUtils.isNotBlank(sortBy)) {
            search.addSort(sortBy, sortDescending);
        }
        if (companyId > 0) {
            Member member = new Member();
            search.addFilterEqual("type", ArticleType.COOPORATE_JOURNEL);
            if (member.getInterestNames() != null && member.getInterestNames().isEmpty()) {
                search.addFilterIn("category.name", member.getInterestNames());
            } else {
                Company company = ApplicationContextProvider.getBean(CompanyService.class).getInstanceByID(companyId);
                if (company != null) {
                    search.addFilterEqual("areaOfBusiness", company.getAreaOfBusiness());
                }
            }
        }

        List<Article> articles = ApplicationContextProvider.getBean(ArticleService.class).getInstances(search, offset, limit))
        return ResponseEntity.ok().body(new ResponseList<>(articles, articles.size(), offset, limit));
    }


    @GetMapping("/by-categories")
    public Response getArticlesByCategories(@Context HttpServletRequest request, @Context UriInfo uriInfo) throws JSONException {
        JSONObject result = new JSONObject();

        try {
            BaseQueryParamModel queryParamModel = new BaseQueryParamModel().buildFromQueryParams(uriInfo);
            Member member = (Member) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);

            if (member == null) {
                return ApiUtils.composeFailureMessage("User not found", 401);
            }
            JSONArray articlesArray = new JSONArray();
            JSONArray data = new JSONArray();
            SortField sortField = new SortField("dateCreated", "dateCreated", true);

            for (CourseCategory devTopic : ApplicationContextProvider.getBean(CourseCategoryService.class).getInstances(new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE).addFilterEqual("type", CategoryType.ARTICLE), 0, 0)) {
                List<Article> articles = ApplicationContextProvider.getBean(ArticleService.class).getInstances(new Search()
                        .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                        .addFilterEqual("publicationStatus", PublicationStatus.ACTIVE)
                        .addFilterEqual("category", devTopic), queryParamModel.getOffset(), queryParamModel.getLimit());

                for (Article article : articles) {

                    articlesArray.put(new JSONObject()
                            .put("id", article.getId())
                            .put("category", new JSONObject()
                                    .put("name", article.getCategory().getName())
                                    .put("academy", article.getCategory().getAcademy())
                                    .put("imageUrl", article.getCategory().getImageUrl())
                                    .put("id", article.getCategory().getId())
                            )
                            .put("title", article.getTitle())
                            .put("coverImageUrl", article.getCoverImageUrl())
                            .put("description", article.getDescription())
                            .put("publicationStatus", article.getPublicationStatus())
                    );

                }
                if (!articles.isEmpty()) {
                    data.put(new JSONObject()
                            .put("id", devTopic.getId())
                            .put("name", devTopic.getName())
                            .put("imageUrl", devTopic.getName())
                            .put("articles", articlesArray));
                    articlesArray = new JSONArray();
                }
            }

            result.put("topicData", data);
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, ApiUtils.SUCCESSFUL_RESPONSE_VALUE);
            return ApiUtils.buidResponse(200, result);
        } catch (JSONException e) {

            result.put(ApiUtils.STATUS_PARAM, ApiUtils.FAILURE_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, e.getMessage());
            return ApiUtils.buidResponse(500, result);

        }

    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response getArticleDetails(@Context HttpServletRequest request, @PathParam("id") String id) throws JSONException {
        JSONObject result = new JSONObject();
        Member member = (Member) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);
        if (member == null) {
            return ApiUtils.composeFailureMessage("User not found", 401);
        } else {
            try {
                JSONObject articleObj = new JSONObject();
                JSONObject subscriptionObject = new JSONObject();
                JSONArray lessonsArray = new JSONArray();

                Article article = ApplicationContextProvider.getBean(ArticleService.class).getInstanceByID(id);
                if (article == null) {
                    return ApiUtils.composeFailureMessage("Articles Not Found", ApiConstants.BAD_REQUEST_CODE);
                }
                articleObj = new JSONObject(article);
                articleObj.put("id", article.getId())
                        .put("category", new JSONObject()
                                .put("name", article.getCategory().getName())
                                .put("academy", article.getCategory().getAcademy())
                                .put("imageUrl", article.getCategory().getImageUrl())
                                .put("id", article.getCategory().getId())
                        )
                        .put("publicationStatus", article.getPublicationStatus());

                result.put("article", articleObj);
                result.put("lessons", lessonsArray);
                result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
                result.put(ApiUtils.RESPONSE_PARAM, ApiUtils.SUCCESSFUL_RESPONSE_VALUE);
                return ApiUtils.buidResponse(200, result);
            } catch (Exception e) {
                return ApiUtils.composeFailureMessage(e.getMessage());
            }

        }

    }

    @GET
    @Path("/categories")
    @Produces("application/json")
    @Consumes("application/json")
    public Response getTopics(@Context HttpServletRequest request, @Context UriInfo uriInfo) throws JSONException {
        JSONObject result = new JSONObject();
        try {
            BaseQueryParamModel queryParamModel = new BaseQueryParamModel().buildFromQueryParams(uriInfo);
            Search search = new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                    .addFilterEqual("type", CategoryType.ARTICLE);
            if (StringUtils.isNotBlank(queryParamModel.getSortBy())) {
                search.addSort(queryParamModel.getSortBy(), queryParamModel.getSortDescending());
            }

            if (StringUtils.isNotBlank(queryParamModel.getType())) {
                CourseAcademyType academyType = CourseAcademyType.valueOf(queryParamModel.getType());
                search.addFilterEqual("academy", academyType);
            }
            JSONArray topics = new JSONArray();
            for (CourseCategory topic : ApplicationContextProvider.getBean(CourseCategoryService.class).getInstances(search, queryParamModel.getOffset(), queryParamModel.getLimit())) {
                int count = ApplicationContextProvider.getBean(ArticleService.class).countInstances(new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE).addFilterEqual("category", topic));
                topics.put(
                        new JSONObject()
                                .put("id", topic.getId())
                                .put("academy", topic.getAcademy())
                                .put("name", topic.getName())
                                .put("colorCode", topic.getColorCode())
                                .put("imageUrl", topic.getImageUrl())
                                .put("articlesCount", count)
                );
            }
            //topics= ApiUtils.sortJsonArray(topics, "articlesCount",false);

            result.put("articleCategories", topics);
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, ApiUtils.SUCCESSFUL_RESPONSE_VALUE);
            return ApiUtils.buidResponse(200, result);
        } catch (JSONException e) {
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.FAILURE_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, e.getMessage());
            return ApiUtils.buidResponse(500, result);
        }

    }

    @GET
    @Path("/business-areas")
    @Produces("application/json")
    @Consumes("application/json")
    public Response getBusinesssAreas(@Context HttpServletRequest request, @Context UriInfo uriInfo) throws JSONException {
        JSONObject result = new JSONObject();
        try {

            JSONArray topics = new JSONArray();
            for (LookUpValue lookUpValue : ApplicationContextProvider.getBean(LookUpService.class).getLookUps(LookupType.AREA_OF_BUSINESS)) {
                topics.put(
                        new JSONObject(lookUpValue)
                                .put("id", lookUpValue.getId())
                );
            }
            result.put("areasOfBusiness", topics);
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, ApiUtils.SUCCESSFUL_RESPONSE_VALUE);
            return ApiUtils.buidResponse(200, result);
        } catch (JSONException e) {
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.FAILURE_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, e.getMessage());
            return ApiUtils.buidResponse(500, result);
        }

    }

    @POST
    @Path("/interests")
    @Produces("application/json")
    @Consumes("application/json")
    public Response updateInterests(@Context HttpServletRequest request, ApiUserModel apiSecurity) throws JSONException {
        JSONObject result = new JSONObject();
        try {
            Member member = (Member) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);
            if (member == null) {
                return ApiUtils.composeFailureMessage("User with that username does not exist", 401);
            }

            if (apiSecurity == null) {
                return ApiUtils.composeFailureMessage("No data specified");
            }
            member.setInterestNames(apiSecurity.getInterests());
            Member savedMember = ApplicationContextProvider.getBean(MemberService.class).saveInstance(member);

            result.put("interests", savedMember.getInterestNames());
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
            return Response.status(200).entity("" + result).build();
        } catch (JSONException | OperationFailedException | ValidationFailedException ex) {
            Logger.getLogger(ApiMemberService.class.getName()).log(Level.WARNING, null, ex);

            return ApiUtils.composeFailureMessage(ex.getMessage());

        }

    }
}
