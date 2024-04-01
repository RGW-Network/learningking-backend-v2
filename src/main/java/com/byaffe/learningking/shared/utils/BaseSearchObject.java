package com.byaffe.learningking.shared.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseSearchObject {
    private String searchTerm;
    private int offset,limit;
    private String sortField;
    private boolean sortAscending;

}
