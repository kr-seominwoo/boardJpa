package com.board.repository;

public class PostSearch {
	
	private String field;
	private String query;
	private String order;
	private String category;
	private String subCategory;
	private int page;
	
	public PostSearch(String field, String query, String order, String category, String subCategory, int page) {
		this.field = field;
		this.query = query;
		this.order = order;
		this.category = category;
		this.subCategory = subCategory;
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

	public String getSubCategory() {
		return subCategory;
	}

	public int getPage() {
		return page;
	}
}
