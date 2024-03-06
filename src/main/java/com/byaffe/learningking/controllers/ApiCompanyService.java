package com.byaffe.learningking.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Ray Gdhrt
 */
@Slf4j
@RestController
@RequestMapping("/v1/companies")
public class ApiCompanyService {

//    @GET
//    @Path("/mycompanies")
//    @Produces("application/json")
//    @Consumes("application/json")
//    public Response getMyCompanies(@Context HttpServletRequest request, @Context UriInfo uriInfo) throws JSONException {
//        JSONObject result = new JSONObject();
//
//        try {
//            JSONArray companyJSONArray = new JSONArray();
//            Member member = (Member) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);
//            if (member == null) {
//                return ApiUtils.composeFailureMessage("User not found", 401);
//            }
//            BaseQueryParamModel queryParamModel = new BaseQueryParamModel().buildFromQueryParams(uriInfo);
//            Search search = CompanyServiceImpl.generateSearchTermsForCompanyMembers(queryParamModel.getSearchTerm(), null, null)
//                    .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
//                    .addFilterEqual("member", member);
//            if (queryParamModel.getCategoryId() != null) {
//                search.addFilterEqual("category.id", queryParamModel.getCategoryId());
//            }
//
//            if (queryParamModel.getSortBy() != null) {
//                search.addSort(queryParamModel.getSortBy(), queryParamModel.getSortDescending());
//            }
//
//            for (CompanyMember companyMember : ApplicationContextProvider.getBean(CompanyService.class).getCompanyMembers(search, queryParamModel.getOffset(), queryParamModel.getLimit())) {
//                companyJSONArray.put(new JSONObject()
//                        .put("id", companyMember.getCompany().getId())
//                        .put("companyName", companyMember.getCompany().getName())
//                         .put("coverImageUrl", companyMember.getCompany().getCoverImageUrl())
//                         .put("emailAddress", companyMember.getCompany().getEmailAddress())
//                         .put("areaOfBusiness", new JSONObject(companyMember.getCompany().getAreaOfBusiness()))
//                         .put("website", companyMember.getCompany().getWebsite())
//                        .put("dateAdded", companyMember.getDateCreated())
//                        .put("recordStatus", companyMember.getRecordStatus().name())
//                );
//
//            }
//            result.put("companies", companyJSONArray);
//            result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
//            result.put(ApiUtils.RESPONSE_PARAM, ApiUtils.SUCCESSFUL_RESPONSE_VALUE);
//            return ApiUtils.buidResponse(200, result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            result.put(ApiUtils.STATUS_PARAM, ApiUtils.FAILURE_TOKEN);
//            result.put(ApiUtils.RESPONSE_PARAM, e.getMessage());
//            return ApiUtils.buidResponse(500, result);
//        }
//
//    }
//
//    @GET
//    @Path("/{id}")
//    @Produces("application/json")
//    @Consumes("application/json")
//    public Response getCompanyDetails(@Context HttpServletRequest request, @PathParam("id") String id) throws JSONException {
//        JSONObject result = new JSONObject();
//        Member member = (Member) request.getAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE);
//        if (member == null) {
//            return ApiUtils.composeFailureMessage("User not found", 401);
//        } else {
//            try {
//
//                Company company = ApplicationContextProvider.getBean(CompanyService.class).getInstanceByID(id);
//                if (company == null) {
//                    return ApiUtils.composeFailureMessage("Company Not Found", ApiConstants.BAD_REQUEST_CODE);
//                }
//                JSONObject companyJSONObject = new JSONObject(company);
//
//                companyJSONObject.put("id", company.getId());
//
//                JSONArray companyCoursesJSONArray = new JSONArray();
//                List<CompanyCourse> companyCourses = ApplicationContextProvider.getBean(CompanyService.class).getCompanyCourses(company);
//
//                for (CompanyCourse companyCourse : companyCourses) {
//                    companyCoursesJSONArray.put(new JSONObject(companyCourse.getCourse())
//                            .put("id", companyCourse.getCourse().getId())
//                    );
//
//                }
//
//                 JSONArray companyArticlesJSONArray = new JSONArray();
//                 Search articleSearch= new Search()
//                         .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
//                          .addFilterEqual("publicationStatus", PublicationStatus.ACTIVE)
//                         .addFilterEqual("type", ArticleType.COOPORATE_JOURNEL)
//
//                         .addFilterEqual("areaOfBusiness", company.getAreaOfBusiness());
//
//                List<Article> articles = ApplicationContextProvider.getBean(ArticleService.class).getInstances(articleSearch,0,20);
//
//                for (Article companyArticle : articles) {
//                    companyArticlesJSONArray.put(new JSONObject(companyArticle)
//                            .put("id", companyArticle.getId())
//                    );
//
//                }
//
//                companyJSONObject.put("articles", companyArticlesJSONArray);
//                companyJSONObject.put("courses", companyCoursesJSONArray);
//                result.put("company", companyJSONObject);
//                result.put(ApiUtils.STATUS_PARAM, ApiUtils.SUCCESSFUL_TOKEN);
//                result.put(ApiUtils.RESPONSE_PARAM, ApiUtils.SUCCESSFUL_RESPONSE_VALUE);
//                return ApiUtils.buidResponse(200, result);
//            } catch (Exception e) {
//                return ApiUtils.composeFailureMessage(e.getMessage());
//            }
//
//        }
//
//    }

}
