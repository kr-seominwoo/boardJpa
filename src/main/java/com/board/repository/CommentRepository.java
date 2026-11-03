package com.board.repository;

import java.util.List;
import java.util.Map;

import com.board.controller.comment.CommentForm;
import com.board.entity.Comment;
import com.board.entity.Member;
import com.board.entity.Post;
import com.board.repository.jpa.comment.CommentDTO;

public interface CommentRepository {
	Long insertComment(Member member, CommentForm commentForm);
	Comment findOne(Long commentId);
	List<CommentDTO> findComments(Long postId, int page);
	List<CommentDTO> findReplyComments(Long postId, Long parentId);
	Map<Long, Integer> findCounts(List<Post> postList, String category);
	Long findTotalCount(Long postId);
	Long findTopTotalCount(Long postId);
	int findCount(String category, String column, Long commentId);
	int updateCount(String category, String column, Long commentId);
	int updateContent(String category, Long commentId, String content);
	int deleteComment(String category, Long commentId);
}
