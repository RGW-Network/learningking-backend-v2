package com.byaffe.microtasks.controllers;

import com.byaffe.microtasks.dtos.LookupDTO;
import com.byaffe.microtasks.dtos.LookupValueDTO;
import com.byaffe.microtasks.models.LookupType;
import com.byaffe.microtasks.models.LookupValue;
import com.byaffe.microtasks.services.LookupValueService;
import com.byaffe.microtasks.services.impl.LookupServiceImpl;
import com.byaffe.microtasks.shared.api.ResponseList;
import com.byaffe.microtasks.shared.constants.Gender;
import com.byaffe.microtasks.shared.models.Country;
import com.google.gson.Gson;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Endpoint to register a microservice
     *
     * @return
     */
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
    public ResponseEntity<ResponseList<LookupValueDTO>> getLookupValue(@RequestParam(required = false, value = "searchTerm") String searchTerm,
                                                                       @RequestParam("offset") int offset,
                                                                       @RequestParam("limit") int limit,
                                                                       @RequestParam(required = false, value = "commaSeparatedTypeIds") String commaSeparatedTypeIds){
        Search search = LookupServiceImpl.composeSearchObjectForLookupValues(searchTerm);
        if(commaSeparatedTypeIds!=null){
            String[] list = commaSeparatedTypeIds.split(",");
            List<LookupType> lookupTypes= Arrays.stream(list).map(r->LookupType.getById(Integer.parseInt(r))).collect(Collectors.toList());
            search.addFilterIn("type", lookupTypes);
        }
        List<LookupValue> lookupValues = lookupService.getList(search, offset, limit);
        System.err.println(new Gson().toJson(lookupValues));
        long totalRecords = lookupService.countLookupValues(search);
        return ResponseEntity.ok().body(new ResponseList<>(lookupValues.stream().map(LookupValueDTO::fromDBModel).collect(Collectors.toList()), totalRecords, 0, 0));

    }

    @PostMapping("/lookup-values")
    public ResponseEntity<LookupValueDTO> saveLookupValue(@RequestBody LookupValueDTO lookupValueDTO) throws ValidationException {
        LookupValue lookupValue = lookupService.save(lookupValueDTO);
        return ResponseEntity.ok().body(LookupValueDTO.fromDBModel(lookupValue));
    }

    @PutMapping("/lookup-values/{id}")
    public ResponseEntity<LookupValueDTO> updateLookupValue(@PathVariable(value = "id", required = true) long id, @RequestBody LookupValueDTO lookupValueDTO) throws ValidationException {
        lookupValueDTO.setId(id);
        LookupValue lookupValue = lookupService.save(lookupValueDTO);
        return ResponseEntity.ok().body(LookupValueDTO.fromDBModel(lookupValue));
    }


}
