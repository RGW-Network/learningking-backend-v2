package com.byaffe.learningking.constants;

import org.apache.commons.lang3.StringUtils;


public enum TemplateType {
	/**
	 * 
	 */
	RESET_PASSWORD("Reset Password"),
	
	EMAIL_VERIFICATION("Email verification"),
        
        LOGIN_CREDENTIALS("Login credentials"),
	
	SUCCESS_PAYMENT("Success payment"),
	
	SUBSCRIPTION_REMINDER("Subscription reminder"),
	
	TOKEN_RESEND("Token Resend"),
	
	USERACCOUNT_REGISTRATION("UserAccount Registration");

	private String name;

	TemplateType(String name) {
		this.name = name;
	}

	public static final TemplateType getEnumObject(String value) {
		if (StringUtils.isBlank(value))
			return null;
		for (TemplateType object : TemplateType.values()) {
			if (object.getName().equals(value))
				return object;
		}
		return null;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.name;
	}
};
