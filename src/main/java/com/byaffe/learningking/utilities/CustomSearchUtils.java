 package com.byaffe.learningking.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.utils.SortField;

import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.Sort;
import org.byaffe.systems.constants.AccountStatus;
import org.byaffe.systems.constants.MemberRegistrationType;
import org.byaffe.systems.constants.Region;
import org.byaffe.systems.constants.TransactionStatus;
import org.byaffe.systems.models.PaymentReasonType;
import org.byaffe.systems.models.ProfessionValue;

public class CustomSearchUtils {

    private static final int MINIMUM_CHARACTERS_FOR_SEARCH_TERM = 2;

    public static boolean searchTermSatisfiesQueryCriteria(String query) {
        if (StringUtils.isBlank(query)) {
            return false;
        }
        return query.length() >= MINIMUM_CHARACTERS_FOR_SEARCH_TERM;
    }

    private static Search generateSearchTerms(String query, List<String> searchFields) {
        Search search = new Search();
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        if (StringUtils.isNotBlank(query) && CustomSearchUtils.searchTermSatisfiesQueryCriteria(query)) {
            ArrayList<Filter> filters = new ArrayList<Filter>();
            CustomSearchUtils.generateSearchTerms(searchFields, query, filters);
            search.addFilterAnd(filters.toArray(new Filter[filters.size()]));
        }
        return search;
    }
 public static Search generateSearchTermsGlobalEntities(String query, List<String> searchFields) {
        Search search = new Search();
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        if (org.apache.commons.lang.StringUtils.isNotBlank(query) && searchTermSatisfiesQueryCriteria(query)) {
            ArrayList<Filter> filters = new ArrayList<Filter>();
            generateSearchTerms(searchFields, query, filters);
            search.addFilterAnd(filters.toArray(new Filter[filters.size()]));
        }
        return search;
    }
    private static boolean generateSearchTerms(List<String> searchFields, String query, List<Filter> filters) {
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

    public static Search genereateSearchObjectForUsers(String query, SortField sortField) {
        Search search = generateSearchTerms(query, Arrays.asList("username", "firstName", "lastName", "emailAddress"));

        if (sortField != null) {
            search.addSort(sortField.getSort());
        } else {
            search.addSort(new Sort("dateCreated", true));
        }

        return search;
    }

    public static Search genereateSearchObjectForMembers(String query, ProfessionValue profession, Region region, AccountStatus accountStatus,MemberRegistrationType registrationType, SortField sortField) {
        Search search = generateSearchTerms(query, Arrays.asList("firstName", "lastName", "emailAddress", "location", "gender", "region", "lastEmailVerificationCode"));

        if (sortField != null) {
            search.addSort(sortField.getSort());
        } else {
            search.addSort(new Sort("dateCreated", true));
        }

        if (profession != null) {
            search.addFilterEqual("professionValue", profession);
        }

        if (region != null) {
            search.addFilterEqual("region", region);
        }

        if (accountStatus != null) {
            search.addFilterEqual("accountStatus", accountStatus);
        }
        
          if (registrationType != null) {
            search.addFilterEqual("registrationType", registrationType);
        }

        return search;
    }

    public static Search genereateSearchObjectForPayments(String query, List<TransactionStatus> statuses,SortField sortField) {
        Search search = generateSearchTerms(query, Arrays.asList("phoneNumber", "title", "description", "subscriber.firstName", "subscriber.lastName", "raveId", "transactionId","failureReason", "subscriber.emailAddress"));

        if (sortField != null) {
            search.addSort(sortField.getSort());
        } else {
            search.addSort(new Sort("dateCreated", true));
        }

        if (statuses != null) {
            search.addFilterIn("status", statuses);
        }

       
        return search;
    }

    private static String StringEscapeUtils(String token) {
        if (token == null) {
            return null;
        }
        return StringUtils.replace(token, "'", "''");
    }
}
