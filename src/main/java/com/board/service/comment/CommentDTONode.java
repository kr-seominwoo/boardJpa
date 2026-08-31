package com.board.service.comment;

import com.board.repository.jpa.comment.CommentDTO;

import java.util.ArrayList;
import java.util.List;


public class CommentDTONode {
	CommentDTO commentDTO;
	List<CommentDTONode> children;
	
	public CommentDTONode(CommentDTO commentDTO) {
		this.commentDTO = commentDTO;
		this.children = new ArrayList<>();
	}

	public CommentDTO getCommentDTO() {
		return commentDTO;
	}

	public List<CommentDTONode> getChildren() {
		return children;
	}
	
	public void addChild(CommentDTONode child) {
		children.add(child);
	}
}
