package com.byaffe.microtasks.shared.api;

import lombok.Data;

import java.util.List;


@Data
public class ResponseList<T> extends BaseResponse {

	private long totalItems;
	private int offset;
	private int limit;
	private List<T> records;

	public ResponseList(List<T> records, long totalItems, int offset, int limit) {
		this.totalItems = totalItems;
		this.offset = offset;
		this.limit = limit;
		this.records = records;
	}


}
