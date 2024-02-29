/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byaffe.learningking.utilities;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import java.util.Date;
import org.byaffe.systems.constants.AccountStatus;
import org.byaffe.systems.constants.Region;
import org.byaffe.systems.models.Member;
import org.byaffe.systems.models.ProfessionValue;
import org.sers.webutils.model.Gender;

/**
 *
 * @author RayGdhrt
 */
public class EnvironmentVariables {

    @CsvBindByName(column = "LastName", required = true)
    private String lastName;

    @CsvBindByName(column = "FirstName", required = true)
    private String firstName;

    @CsvBindByName(column = "PhoneNumber", required = true)
    private String phoneNumber;

    @CsvBindByName(column = "Email", required = true)
    private String email;

    @CsvBindByName(column = "Location", required = false)
    private String location;

    @CsvBindByName(column = "Region", required = true)
    private String region;

    @CsvBindByName(column = "Gender", required = true)
    private String gender;

    @CsvBindByName(column = "Profession", required = true)
    private String profession;

    @CsvBindByName(column = "AccountStatus", required = true)
    private String accountStatus;

    @CsvDate(value = "dd/MM/yyyy")
    @CsvBindByName(column = "RegistrationDate", required = true)
    private Date registrationDate;

    @CsvDate(value = "dd/MM/yyyy")
    @CsvBindByName(column = "SubscriptionStartDate", required = false)
    private Date subscriptionStartDate;

    public Member obtainMemberModel() throws Exception {

        Member member = new Member();

        member.setFirstName(firstName);
        member.setLastName(lastName);
        member.setLocation(location);
        member.setPhoneNumber(phoneNumber);
        member.setEmailAddress(email);
        member.setDateCreated(registrationDate);
        member.setGender(Gender.valueOf(gender));
        member.setProfessionValue(ProfessionValue.fromString(profession));
        member.setRegion(Region.fromString(region));
         member.setAccountStatus(AccountStatus.valueOf(accountStatus));
        member.setDateChanged(subscriptionStartDate);

        System.out.println("Added " + this.toString() + "...");

        return member;

    }

}
