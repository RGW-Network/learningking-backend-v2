package com.byaffe.learningking.controllers.models;

import lombok.Data;
@Data
public class BaseFilterDTO {
    public String searchTerm;
    public Integer offset = 0;
    public Integer limit = 0;
    public String sortBy = "dateCreated";
    public Boolean sortDescending = true;
}
