/**
 *
 */
package com.byaffe.learningking.services;

import com.googlecode.genericdao.search.Filter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Contains the general search utility/helper functions used to query models
 * from the DB.
 *
 */
public class GeneralSearchUtils {

    private static final int MINIMUM_CHARACTERS_FOR_SEARCH_TERM = 1;

    public static boolean searchTermSatisfiesQueryCriteria(String query) {
        if (StringUtils.isBlank(query)) {
            return false;
        }
        return query.length() >= MINIMUM_CHARACTERS_FOR_SEARCH_TERM;
    }



    public static boolean generateSearchTerms(String commaSeparatedsearchFields, String query, List<Filter> filters) {
        if (StringUtils.isBlank(commaSeparatedsearchFields)) {
            return false;
        }

        List<String> searchFields = Arrays.asList(commaSeparatedsearchFields.split(","));
        if (searchFields != null && !searchFields.isEmpty()) {
            for (String token : query.replaceAll("  ", " ").split(" ")) {
                String searchTerm = "%" + StringEscapeUtils(token) + "%";
                Filter[] orFilters = new Filter[searchFields.size()];
                int counter = 0;
                for (String searchField : searchFields) {
                    orFilters[counter] = Filter.like(searchField, searchTerm);
                    counter++;
                }
                filters.add(Filter.or(orFilters));
            }
            return true;
        }
        return false;
    }



    private static String StringEscapeUtils(String token) {
        if (token == null) {
            return null;
        }
        return StringUtils.replace(token, "'", "''");
    }

}
