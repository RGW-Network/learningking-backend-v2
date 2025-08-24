package com.byaffe.learningking.shared.models;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;


@Getter
public enum MessageTemplateType {
	NEW_STUDENT_SIGNUP_OTP("New Student Signup OTP"),
	NEW_STUDENT_SIGNUP_SUCCESS("New Student Signup Success"),
	RESET_PASSWORD_OTP("Reset Password OTP"),
	SUCCESS_PAYMENT("Success payment"),
	STUDENT_ORGANISATION_INVITATION("Student Organisation invitation"),
	NON_STUDENT_ORGANISATION_INVITATION("Non Student Organisation invitation"),
	VERIFY_ORGANISATION("Verify Organisation");

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
