package com.board.entity;

import com.board.repository.UpdatePostForm;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity(name = "post")
@DynamicInsert
public class Post {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long postId;
	@Column(name="member_id")
	private String memberId;

	private String writer;
	private String title;
	private String content;
//	private Date regdate;
	private LocalDateTime regdate;

	private Integer likes;
	private Integer unlike;

	private Integer hit;
	private String category;

	@Column(name="subcategory")
	private String subCategory;

	private Integer blind;

	public Post() {

	}

	public Post(String memberId, String writer, String title, String content, String category, String subCategory) {
		this.memberId = memberId;
		this.writer = writer;
		this.title = title;
		this.content = content;
		this.category = category;
		this.subCategory = subCategory;
		this.blind = 0;
	}

	public Long getPostId() {
		return postId;
	}

	public String getMemberId() {
		return memberId;
	}

	public String getWriter() {
		return writer;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public LocalDateTime getRegdate() {
		return regdate;
	}

	public Integer getLikes() {
		return likes;
	}

	public Integer getUnlike() {
		return unlike;
	}

	public Integer getHit() {
		return hit;
	}

	public String getCategory() {
		return category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public Integer getBlind() {
		return blind;
	}

	public void updatePost(UpdatePostForm updatePostForm) {
		this.title = updatePostForm.getTitle();
		this.content = updatePostForm.getContent();
	}

	public void updateHit() {
		this.hit += 1;
	}

	public void deletePost() {
		this.blind = 1;
	}
}
