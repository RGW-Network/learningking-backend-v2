package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.daos.CountryDao;
import com.byaffe.learningking.models.SystemSetting;
import com.byaffe.learningking.services.SystemSettingService;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.models.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class SystemSettingServiceImpl extends GenericServiceImpl<SystemSetting> implements SystemSettingService {

    @Autowired
    CountryDao countryDao;

    @Override
    public SystemSetting saveInstance(SystemSetting appSetting) throws ValidationFailedException {
        return super.save(appSetting);
    }

    @Override
    public Country getCountryByName(String countryName) {
        return countryDao.searchUniqueByPropertyEqual("name", countryName);
    }

    @Override
    public SystemSetting getAppSetting() {
        if (super.findAll().size() > 0) {
            return super.findAll().get(0);
        } else {
            return super.save(new SystemSetting());
        }
    }

    public int testUrls(String url) {
//        WebResource resource = Client.create(new DefaultClientConfig())
//                .resource(url);
//        final Builder webResource = resource.accept(MediaType.APPLICATION_JSON);
//        webResource.type(MediaType.APPLICATION_JSON);
//        ClientResponse clientResponse = webResource.post(ClientResponse.class, String.class.toString());
//        System.out.println("Status " + clientResponse.getStatus());
//        System.out.println("Response " + clientResponse.toString());
//        return clientResponse.getStatus();

        return 0;
    }

    @Override
    public boolean isDeletable(SystemSetting entity) throws OperationFailedException {
        return true;
    }

}
