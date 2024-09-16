package com.byaffe.learningking.controllers;

import com.byaffe.learningking.dtos.LookupDTO;
import com.byaffe.learningking.models.LookupType;
import com.byaffe.learningking.models.LookupValue;
import com.byaffe.learningking.services.LookupValueService;
import com.byaffe.learningking.services.impl.LookupServiceImpl;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.constants.Gender;
import com.byaffe.learningking.shared.models.Country;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/lookups")
public class LookupsController {

    @Autowired
    LookupValueService lookupService;

    @GetMapping("/genders")
    public ResponseEntity<ResponseList<LookupDTO>> getGenders() {
        List<LookupDTO> genders = Arrays.stream(Gender.values()).map(r -> new LookupDTO(r.getId(), r.getUiName())).collect(Collectors.toList());
        return ResponseEntity.ok().body(new ResponseList<>(genders, genders.size(), 0, 0));
    }

    @GetMapping("/lookup-types")
    public ResponseEntity<ResponseList<LookupDTO>> getLookupType() {
        List<LookupDTO> lookupTypes = Arrays.stream(LookupType.values()).map(r -> new LookupDTO(r.getId(), r.getUiName())).collect(Collectors.toList());
        return ResponseEntity.ok().body(new ResponseList<>(lookupTypes, lookupTypes.size(), 0, 0));
    }


    @GetMapping("/countries")
    public ResponseEntity<ResponseList<Country>> getCountries(@RequestParam(required = false, value = "searchTerm") String searchTerm,
                                                              @RequestParam("offset") int limit,
                                                              @RequestParam("limit") int offset) {
        List<Country> countries = lookupService.getCountries(LookupServiceImpl.composeSearchObjectForCountry(searchTerm), offset, limit);
        return ResponseEntity.ok().body(new ResponseList<>(countries, countries.size(), 0, 0));

    }

    @GetMapping("/lookup-values")
    public ResponseEntity<ResponseList<LookupValue>> getLookupValue(@RequestParam(required = false, value = "searchTerm") String searchTerm,
                                                                       @RequestParam("offset") int offset,
                                                                       @RequestParam("limit") int limit,
                                                                       @RequestParam(required = false, value = "commaSeparatedTypes") String commaSeparatedTypes){
        Search search = LookupServiceImpl.composeSearchObjectForLookupValues(searchTerm);
        if(commaSeparatedTypes!=null){
            String[] list = commaSeparatedTypes.split(",");
            List<LookupType> lookupTypes= Arrays.stream(list).map(LookupType::valueOf).collect(Collectors.toList());
            search.addFilterIn("type", lookupTypes);
        }
        List<LookupValue> values = lookupService.getList(search, offset, limit);
        long totalRecords = lookupService.countLookupValues(search);
        return ResponseEntity.ok().body(new ResponseList<>(values, totalRecords, offset, limit));
    }

    @PostMapping("/lookup-values")
    public ResponseEntity<LookupValue> saveLookupValue(@RequestBody LookupValue lookupValue) throws ValidationException {
        return ResponseEntity.ok().body(lookupService.save(lookupValue));
    }



}
