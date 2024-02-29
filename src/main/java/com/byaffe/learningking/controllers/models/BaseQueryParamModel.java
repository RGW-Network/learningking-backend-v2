package com.byaffe.learningking.controllers.models;

import javax.ws.rs.core.UriInfo;

public class BaseQueryParamModel {

    private String categoryId;
      private String academy;
    private String entityId;
    private String memberId;
    private String categoryName;
    private String authorId;
    private String companyId;
    private String searchTerm;
    private int offset = 0;
    private int limit = 0;
    private String sortBy = "dateCreated";
    private Boolean sortDescending = true;
    private Boolean popular = false;
    private Boolean featured = null;
    private Boolean special = false;
    private Boolean free = false;
    private Boolean mine = false;
        
    private String status = null;
    private String type;

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Boolean getSortDescending() {
        return sortDescending;
    }

    public void setSortDescending(Boolean sortDescending) {
        this.sortDescending = sortDescending;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

   

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Boolean getPopular() {
        return popular;
    }

    public void setPopular(Boolean popular) {
        this.popular = popular;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Boolean getMine() {
        return mine;
    }

    public void setMine(Boolean mine) {
        this.mine = mine;
    }

    

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAcademy() {
        return academy;
    }

    public void setAcademy(String academy) {
        this.academy = academy;
    }
    
    

    public BaseQueryParamModel buildFromQueryParams(UriInfo info) {
        BaseQueryParamModel apiRequestModel = new BaseQueryParamModel();
        if (info != null) {
            if (info.getQueryParameters().getFirst("offset") != null) {
                apiRequestModel.offset = Integer.valueOf(info.getQueryParameters().getFirst("offset"));
            }
            if (info.getQueryParameters().getFirst("limit") != null) {
                apiRequestModel.limit = Integer.valueOf(info.getQueryParameters().getFirst("limit"));
            }
            if (info.getQueryParameters().getFirst("sortBy") != null) {
                apiRequestModel.sortBy = info.getQueryParameters().getFirst("sortBy");
            }

            if (info.getQueryParameters().getFirst("sortDescending") != null) {
                apiRequestModel.sortDescending = Boolean.valueOf(info.getQueryParameters().getFirst("sortDescending"));

            }
             if (info.getQueryParameters().getFirst("popular") != null) {
                apiRequestModel.popular = Boolean.valueOf(info.getQueryParameters().getFirst("popular"));

            }
              if (info.getQueryParameters().getFirst("featured") != null) {
                apiRequestModel.featured = Boolean.valueOf(info.getQueryParameters().getFirst("featured"));

            }
               if (info.getQueryParameters().getFirst("mine") != null) {
                apiRequestModel.mine = Boolean.valueOf(info.getQueryParameters().getFirst("mine"));

            }
               
                
                if (info.getQueryParameters().getFirst("special") != null) {
                apiRequestModel.special = Boolean.valueOf(info.getQueryParameters().getFirst("special"));

            }
            if (info.getQueryParameters().getFirst("status") != null) {
                apiRequestModel.status = info.getQueryParameters().getFirst("status");

            }
            apiRequestModel.searchTerm = info.getQueryParameters().getFirst("searchTerm");
            apiRequestModel.authorId = info.getQueryParameters().getFirst("authorId");
               apiRequestModel.companyId = info.getQueryParameters().getFirst("companyId");
            apiRequestModel.categoryId = info.getQueryParameters().getFirst("categoryId");
            apiRequestModel.categoryName = info.getQueryParameters().getFirst("categoryName");
            apiRequestModel.entityId = info.getQueryParameters().getFirst("entityId");
            apiRequestModel.memberId = info.getQueryParameters().getFirst("memberId");
            apiRequestModel.type = info.getQueryParameters().getFirst("type");
              apiRequestModel.academy = info.getQueryParameters().getFirst("academy");

        }
        return apiRequestModel;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

  

}
