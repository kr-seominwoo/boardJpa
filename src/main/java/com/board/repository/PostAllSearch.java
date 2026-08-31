package com.board.repository;

public class PostAllSearch {
	
	private String field;
	private String query;
	private String order;
	private String category;

	private int page;

	public PostAllSearch(String field, String query, String order, String category, int page) {
		this.field = field;
		this.query = query;
		this.order = order;
		this.category = category;
		this.page = page;
	}

	public String getField() {
		return field;
	}

	public String getQuery() {
		return query;
	}

	public String getOrder() {
		return order;
	}

	public String getCategory() {
		return category;
	}

	public int getPage() {
		return page;
	}
}
