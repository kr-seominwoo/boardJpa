package com.board.repository.jpa.comment;

import java.util.Date;

public class CommentDTO {
	private Long id;
	private String memberId;
	private String writer;
	private String content;
	private Date regdate;
	private int like;
	private int unlike;
	private int level;
	private int blind;
	
	public CommentDTO(Long id, String memberId, String writer, String content, Date regdate, int like, int unlike,
                      int level, int blind) {
		this.id = id;
		this.memberId = memberId;
		this.writer = writer;
		this.content = content;
		this.regdate = regdate;
		this.like = like;
		this.unlike = unlike;
		this.level = level;
		this.blind = blind;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getMemberId() {
		return memberId;
	}
	
	public String getWriter() {
		return writer;
	}
	
	public String getContent() {
		return content;
	}
	
	public Date getRegdate() {
		return regdate;
	}
	
	public int getLike() {
		return like;
	}
	
	public int getUnlike() {
		return unlike;
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getBlind() {
		return blind;
	}
}
