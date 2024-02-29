package com.byaffe.learningking.shared.api;

import lombok.Data;

@Data
public class BaseResponse {
	private String status;
	private String message;

	public static final String DEFAULT_SUCCESS_STATUS="Success";
	public static final String DEFAULT_SUCCESS_MESSAGE="Action successful";
	public static final String DEFAULT_FAILURE_MESSAGE="Action failed";
	public static final String DEFAULT_FAILURE_STATUS="Failure";


	public BaseResponse() {
	}

	public BaseResponse(String message, boolean success){
		this.message=message;
		this.status=success?DEFAULT_SUCCESS_STATUS:DEFAULT_FAILURE_STATUS;
	}

	public BaseResponse(boolean success){
		this.status=success?DEFAULT_SUCCESS_STATUS:DEFAULT_FAILURE_STATUS;
		this.message=success?DEFAULT_SUCCESS_MESSAGE:DEFAULT_FAILURE_MESSAGE;
	}
}
