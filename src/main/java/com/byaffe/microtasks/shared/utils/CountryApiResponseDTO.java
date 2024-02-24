package com.byaffe.microtasks.shared.utils;

import java.util.List;

public class CountryApiResponseDTO {

    public boolean error;
    public String msg;
    public List<CountryDTO> data;

 public    static class CountryDTO {
        public String name;
        public String Iso2;
        public String Iso3;
    }
}
