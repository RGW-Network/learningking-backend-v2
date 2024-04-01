package com.byaffe.learningking.shared.api;

public class ResponseObject<T> extends BaseResponse {
	private T data;
	public ResponseObject(T object) {
		this.data=object;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
