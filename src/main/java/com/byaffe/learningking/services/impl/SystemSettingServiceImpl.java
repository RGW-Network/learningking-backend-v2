package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.daos.CountryDao;
import com.byaffe.learningking.dtos.SettingsRequestDto;
import com.byaffe.learningking.models.SystemSetting;
import com.byaffe.learningking.services.SystemSettingService;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.models.Country;
import com.googlecode.genericdao.search.Search;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


@Service
@Transactional
public class SystemSettingServiceImpl extends GenericServiceImpl<SystemSetting> implements SystemSettingService {

    @Autowired
    CountryDao countryDao;

    @Autowired
    ModelMapper  modelMapper;
    @Override
    public SystemSetting saveInstance(SystemSetting appSetting) throws ValidationFailedException {
        return super.save(appSetting);
    }

    @Override
    public Country getCountryByName(String countryName) {
        return countryDao.searchUniqueByPropertyEqual("name", countryName);
    }

    @Override
    public SystemSetting save(SettingsRequestDto appSetting) throws ValidationFailedException {
        SystemSetting  systemSetting= getAppSetting();
        systemSetting.setBaseCurrency(appSetting.getBaseCurrency());
        systemSetting.setLogoUrl(appSetting.getLogoUrl());
        systemSetting.setSmtpAddress(appSetting.getSmtpAddress());
        systemSetting.setSmtpHost(appSetting.getSmtpHost());
        systemSetting.setSmtpPassword(appSetting.getSmtpPassword());
        systemSetting.setSmtpPort(appSetting.getSmtpPort());
        systemSetting.setDefaultTrainingMandate(appSetting.getDefaultTrainingMandate());
        systemSetting.setSmsApiPassword(appSetting.getSmsApiPassword());
        systemSetting.setSmsApiUsername(appSetting.getSmsApiUsername());
        return super.save(systemSetting);
    }

    @Override
    public SystemSetting getAppSetting() {
        SystemSetting systemSetting=super.searchUnique(new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE));
        if (systemSetting==null) {
            systemSetting= saveInstance(new SystemSetting());
        }
        return systemSetting;
    }


    @Override
    public boolean isDeletable(SystemSetting entity) throws OperationFailedException {
        return true;
    }

    @Override
    public List<String> getStringFilterFields() {
        return Collections.emptyList();
    }

}
