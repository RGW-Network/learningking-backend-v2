package com.byaffe.learningking.shared.services;

import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.courses.Organisation;
import com.byaffe.learningking.shared.models.MessageTemplateParams;
import com.byaffe.learningking.shared.models.User;

import java.util.ArrayList;
import java.util.List;

public class MessageTemplateUtils {
    public static List<String> getDisplayNames() {
        List<String> displayNames = new ArrayList<>();
        for (MessageTemplateParams param : MessageTemplateParams.values()) {
            displayNames.add(param.getDisplayName());
        }
        return displayNames;
    }

    public static String resolveOrganisationInvitation(Organisation organisation, String templateString) {
       String result=templateString;
       result=result.replace(MessageTemplateParams.OTP.getDisplayName(),organisation.getLastVerificationCode());
        result=result.replace(MessageTemplateParams.COMPANY_NAME.getDisplayName(),organisation.getName());
       return result;

    }
    public static String resolveOTPMessageTemplate(Student user, String templateString) {
        String result=templateString;
        result=result.replace(MessageTemplateParams.OTP.getDisplayName(),user.getLastEmailVerificationCode());
        result=result.replace(MessageTemplateParams.USERNAME.getDisplayName(),user.getUsername());
        result=result.replace(MessageTemplateParams.FIRST_NAME.getDisplayName(),user.getFirstName());
        result=result.replace(MessageTemplateParams.LAST_NAME.getDisplayName(),user.getLastName());
        return result;

    }

    public static String resolveLkInvitationMessageTemplate(String  userEmail,User invitor,String companyName, String templateString) {
        String result=templateString;
        result=result.replace(MessageTemplateParams.USERNAME.getDisplayName(),invitor.getUsername());
        result=result.replace(MessageTemplateParams.FIRST_NAME.getDisplayName(),invitor.getFirstName());
        result=result.replace(MessageTemplateParams.EMAIL_ADDRESS.getDisplayName(),userEmail);
        result=result.replace(MessageTemplateParams.COMPANY_NAME.getDisplayName(),companyName);
        result=result.replace(MessageTemplateParams.LAST_NAME.getDisplayName(),invitor.getLastName());
        return result;

    }
}
