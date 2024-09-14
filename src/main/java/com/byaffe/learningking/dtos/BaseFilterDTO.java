package com.byaffe.learningking.dtos;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class BaseFilterDTO {
    public String searchTerm;
    @Min(0)
    public Integer offset = 0;
    @Min(1)
    public Integer limit = 0;
    public String sortBy = "dateCreated";
    public Boolean sortDescending = true;

}
