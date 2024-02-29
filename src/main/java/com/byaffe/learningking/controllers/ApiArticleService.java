package com.byaffe.learningking.controllers;

import org.byaffe.systems.api.models.BaseQueryParamModel;
import com.googlecode.genericdao.search.Search;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang3.StringUtils;
import org.byaffe.systems.models.Member;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.byaffe.systems.api.constants.ApiUtils;
import org.byaffe.systems.api.models.ApiUserModel;
import org.byaffe.systems.core.services.ArticleService;
import org.byaffe.systems.core.services.CompanyService;
import org.byaffe.systems.core.services.CourseCategoryService;
import org.byaffe.systems.core.services.LookUpService;
import org.byaffe.systems.core.services.MemberService;
import org.byaffe.systems.core.services.impl.ArticleServiceImpl;
import org.byaffe.systems.models.Article;
import org.byaffe.systems.models.LookUpValue;
import org.byaffe.systems.models.LookupType;
import org.byaffe.systems.models.courses.ArticleType;
import org.byaffe.systems.models.courses.CategoryType;
import org.byaffe.systems.models.courses.Company;
import org.byaffe.systems.models.courses.CourseAcademyType;
import org.byaffe.systems.models.courses.CourseCategory;
import org.byaffe.systems.models.courses.PublicationStatus;
import org.sers.webutils.model.utils.SortField;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.exception.OperationFailedException;
com.byaffe.learningking.shared.exceptions.ValidationFailedException;

/**
 *
 * @author Ray Gdhrt
 */
@Path("/v1/articles")
public class ApiArticleService {

    @GET
    @Path("/")
    @Produces("application/json")
    @Consumes("application/json")
    public Response getArticles(@Context HttpServletRequest request, @Context UriInfo uriInfo) throws JSONException {
        JSONObject result = new JSONObject();

        try {
            JSONArray articles = new JSONArray();
            Member member = (Member) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);
            if (member == null) {
                return ApiUtils.composeFailureMessage("User not found", 401);
            }
            BaseQueryParamModel queryParamModel = new BaseQueryParamModel().buildFromQueryParams(uriInfo);
            Search search = ArticleServiceImpl.generateSearchTermsForArticles(queryParamModel.getSearchTerm(), null)
                    .addFilterEqual("publicationStatus", PublicationStatus.ACTIVE);

            if (StringUtils.isNotBlank(queryParamModel.getType())) {
                ArticleType articleType = ArticleType.valueOf(queryParamModel.getType());

                if (StringUtils.isBlank(queryParamModel.getCompanyId()) && articleType.equals(ArticleType.COOPORATE_JOURNEL)) {
                    return ApiUtils.composeFailureMessage("Company Id is required for coporate journels", 400);
                }
                search.addFilterEqual("type", articleType);

            }

            if (StringUtils.isNotBlank(queryParamModel.getAcademy())) {
                CourseAcademyType academyType = CourseAcademyType.valueOf(queryParamModel.getAcademy());
                if (academyType != null) {
                    search.addFilterEqual("category.academy", academyType);
                } else {
                    return ApiUtils.composeFailureMessage("Unknown academy type supplied", 400);
                }

            }
            if (StringUtils.isNotBlank(queryParamModel.getCategoryId())) {
                search.addFilterEqual("category.id", queryParamModel.getCategoryId());
            }

            if (StringUtils.isNotBlank(queryParamModel.getCompanyId())) {
                search.addFilterEqual("type", ArticleType.COOPORATE_JOURNEL);
                if (member.getInterestNames() != null && member.getInterestNames().isEmpty()) {
                    search.addFilterIn("category.name", member.getInterestNames());
                } else {
                    Company company = ApplicationContextProvider.getBean(CompanyService.class).getInstanceByID(queryParamModel.getCompanyId());
                    if (company != null) {
                        search.addFilterEqual("areaOfBusiness", company.getAreaOfBusiness());
                    }
                }
            }

            if (queryParamModel.getFeatured() != null) {
                search.addFilterEqual("isFeatured", queryParamModel.getFeatured());
            }

            if (StringUtils.isNotBlank(queryParamModel.getSortBy())) {
                search.addSort(queryParamModel.getSortBy(), queryParamModel.getSortDescending());
            }

            for (Article article : ApplicationContextProvider.getBean(ArticleService.class).getInstances(search, queryParamModel.getOffset(), queryParamModel.getLimit())) {
                JSONArray testimonialArray = new JSONArray();

                articles.put(new JSONObject(article)
                        .put("id", article.getId())
                        .put("category", new JSONObject()
                                .put("name", article.getCategory().getName())
                                .put("academy", article.getCategory().getAcademy())
                                .put("imageUrl", article.getCategory().getImageUrl())
                                .put("id", article.getCategory().getId())
                        )
                        .put("testimonials", testimonialArray)
                        .put("coverImageUrl", article.getCoverImageUrl())
                        .put("title", article.getTitle())
                        .put("description", article.getDescription())
                        .put("publicationStatus", article.getPublicationStatus())
                );
            }
            result.put("articles", articles);
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, ApiUtils.SUCCESSFUL_RESPONSE_VALUE);
            return ApiUtils.buidResponse(200, result);
        } catch (Exception e) {
            e.printStackTrace();
            result.put(ApiUtils.STATUS_PARAM, ApiUtils.FAILURE_TOKEN);
            result.put(ApiUtils.RESPONSE_PARAM, e.getMessage());
            return ApiUtils.buidResponse(500, result);
        }

    }

    @GET
    @Path("/by-categories")
    @Produces("application/json")
    @Consumes("application/json")
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
