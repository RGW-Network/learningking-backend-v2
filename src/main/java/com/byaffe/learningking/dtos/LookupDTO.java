package com.byaffe.learningking.dtos;

import com.byaffe.learningking.shared.api.BaseDTO;
import lombok.Data;

@Data
public class LookupDTO extends BaseDTO {
    public String id;
    public String name;

    public LookupDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
