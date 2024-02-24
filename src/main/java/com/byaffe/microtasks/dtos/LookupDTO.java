package com.byaffe.microtasks.dtos;

import com.byaffe.microtasks.shared.api.BaseDTO;
import lombok.Data;

@Data
public class LookupDTO extends BaseDTO {
    public long id;
    public String name;
    public String value;

    public LookupDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
