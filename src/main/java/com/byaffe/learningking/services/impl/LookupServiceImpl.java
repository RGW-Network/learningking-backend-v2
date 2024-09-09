package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.googlecode.genericdao.search.Search;
import com.byaffe.learningking.daos.CountryDao;
import com.byaffe.learningking.daos.LookupValueDao;
import com.byaffe.learningking.dtos.LookupValueDTO;
import com.byaffe.learningking.models.LookupType;
import com.byaffe.learningking.models.LookupValue;
import com.byaffe.learningking.services.LookupValueService;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.models.Country;
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
import com.byaffe.learningking.shared.utils.Validate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class LookupServiceImpl implements LookupValueService {
    @Autowired
    LookupValueDao LookupValueDao;

    @Autowired
    CountryDao countryDao;


    @Override
    public LookupValue save(LookupValueDTO dto) {
        if (StringUtils.isBlank(dto.getValue())) {
            throw new OperationFailedException("Missing value");
        }
        LookupType lookupType= dto.getType();
        Validate.notNull(lookupType,"Missing Type");

        LookupValue existsWithNameAndType = getLookupValueByTypeAndValue(lookupType, dto.getValue());
        if (existsWithNameAndType != null && existsWithNameAndType.getId() != dto.getId()) {
            throw new OperationFailedException("Lookup value with same name and type exists");
        }
        LookupValue LookupValue = new LookupValue();
        if(dto.getId()>0){
            LookupValue = getById(dto.getId());
        }
        LookupValue.setValue(dto.getValue() );
        LookupValue.setDescription(dto.getDescription() );
        LookupValue.setImageUrl(dto.getImageUrl() );
        LookupValue.setType(lookupType);
        return LookupValueDao.save(LookupValue);
    }

    LookupValue getLookupValueByTypeAndValue(LookupType type, String value) {
        return LookupValueDao.searchUnique(
                new Search().addFilterEqual("type", type)
                        .addFilterEqual("value", value));
    }

    @Override
    public Country save(Country country) {
        if (StringUtils.isBlank(country.getName())) {
            throw new OperationFailedException("Missing name");
        }

        if (StringUtils.isBlank(country.getPostalCode())) {
            throw new OperationFailedException("Missing code");
        }

        Country existsWithName = getByName(country.getName());
        if (existsWithName != null && existsWithName.getId() != country.getId()) {
            throw new OperationFailedException("Country value with same name and type exists");
        }

        return countryDao.save(country);
    }

    Country getByName(String name) {
        return countryDao.searchUnique(
                new Search().addFilterEqual("name", name));
    }

   public   Country getCountryById(long id) {
        return countryDao.findById(id).orElseThrow(()->new ValidationFailedException("Country with id not found"));
    }

    @Override
    public List<LookupValue> getList(Search search, int offset, int limit) {
        search.setFirstResult(offset);
        search.setMaxResults(limit);
        return LookupValueDao.search(search);
    }

    @Override
    public LookupValue getById(long id) {
        return LookupValueDao.findById(id).orElseThrow(() -> new OperationFailedException("Not found"));
    }

    @Override
    public List<LookupValue> getByType(LookupType type) {
        return LookupValueDao.findAll();
    }

    @Override
    public LookupValue getByType(LookupType lookupType, Long id) {

        if(lookupType==null||id==null){
            return null;

        }
        Search search= new Search();
        search.addFilterEqual("type",lookupType)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addFilterEqual("id",id);
        return LookupValueDao.searchUnique(search);
    }

    @Override
    public List<Country> getCountries(Search search, int offset, int limit) {
        search.setMaxResults(limit);
        search.setFirstResult(offset);
        return countryDao.search(search);
    }


    @Override 
    public long countCountries(Search search) {
        return countryDao.count(search);

    }

    @Override
    public long countLookupValues(Search search) {
        return LookupValueDao.count(search);

    }

    public static Search composeSearchObjectForCountry(String searchTerm) {
        com.googlecode.genericdao.search.Search search = CustomSearchUtils.generateSearchTerms(searchTerm,
                Arrays.asList("value",
                        "postalCode",
                        "currencyCode"));

        return search;
    }
    public LookupValue getInstanceByID(Long id) {
        return  LookupValueDao.findById (id).orElseThrow(() -> new ValidationFailedException(String.format("LookupValue with ID %d not found", id)));
    }
    public static Search composeSearchObjectForLookupValues(String searchTerm) {
        com.googlecode.genericdao.search.Search search = CustomSearchUtils.generateSearchTerms(searchTerm,
                Arrays.asList("value","description"));

        return search;
    }

}
