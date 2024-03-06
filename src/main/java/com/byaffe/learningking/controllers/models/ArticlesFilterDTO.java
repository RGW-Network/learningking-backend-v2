package com.byaffe.learningking.controllers.models;

import lombok.Data;

@Data
public class ArticlesFilterDTO extends BaseFilterDTO{
    public Long categoryId;
    public String academy;
    public String entityId;
    public String memberId;
    public String categoryName;
    public String authorId;
    public Long companyId;
    public Boolean popular = false;
    public Boolean featured = null;
    public Boolean special = false;
    public Boolean free = false;
    public Boolean mine = false;
    public String status = null;
    public String type;

}
