package com.byaffe.learningking.shared.models;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;


@Getter
public enum MessageTemplateType {

	RESET_PASSWORD("Reset Password"),
	
	EMAIL_VERIFICATION("Email verification"),
        
    LOGIN_CREDENTIALS("Login credentials"),
	
	SUCCESS_PAYMENT("Success payment"),
	
	SUBSCRIPTION_REMINDER("Subscription reminder"),
	
	TOKEN_RESEND("Token Resend"),
	SIGNUP_INVITATION("Signup invitation"),
	ORGANISATION_INVITATION("Organisation invitation"),
	USERACCOUNT_REGISTRATION("UserAccount Registration");

	private String displayName;

	MessageTemplateType(String name) {
		this.displayName = name;
	}

	public static final MessageTemplateType getEnumObject(String value) {
		if (StringUtils.isBlank(value))
			return null;
		for (MessageTemplateType object : MessageTemplateType.values()) {
			if (object.getDisplayName().equals(value))
				return object;
		}
		return null;
	}

    @Override
	public String toString() {
		return this.displayName;
	}
};
