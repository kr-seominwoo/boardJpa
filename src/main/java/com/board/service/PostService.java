package com.board.service;

import java.util.List;

import javax.sql.DataSource;

import com.board.entity.Post;
import com.board.repository.PostAllSearch;
import com.board.repository.PostForm;
import com.board.repository.PostSearch;
import jakarta.transaction.Transactional;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.board.repository.PostRepository;
import com.board.repository.UpdatePostForm;

@DynamicUpdate
@Service
public class PostService {
    private PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    public Long savePost(String category, PostForm postForm) {
        return postRepository.insertPost(category, postForm);
    }

    @Transactional
    public int updatePost(String category, UpdatePostForm updatePostForm) {
        if (isPossible(updatePostForm.getMemberId(), category, updatePostForm.getPostId())) {
            Post post = postRepository.findPost(category, updatePostForm.getPostId());
            post.updatePost(updatePostForm);
            return 0;
        }
        return -1;
    }

    public List<Post> findPosts(PostAllSearch postAllSearch) {
        return postRepository.findPosts(postAllSearch);
    }

    public List<Post> findPosts(PostSearch postSearch) {
        return postRepository.findPosts(postSearch);
    }

    @Transactional
    public Post findPost(String category, String subCategory, Long id) {
        Post post = null;
        post = postRepository.findPost(category, id);
        post.updateHit();

        return post;
    }

    @Transactional
    public int deletePost(String memberId, String category, Long id) {
        if (isPossible(memberId, category, id)) {
            Post post = postRepository.findPost(category, id);
            post.deletePost();
            return 0;
        }

        return -1;
    }

    //	public int updateCount(String column, String category, Long id) {
//		postRepository.updateCount(category, id, column);
//		return postRepository.findCount(category, id, column);
//	}
//
    public boolean isPossible(String memberId, String category, Long id) {
        return memberId.equals(postRepository.findMemberIdByPostId(category, id));
    }

    public Long findTotalCount(PostAllSearch postAllSearch) {
        return postRepository.findTotalCount(postAllSearch);
    }

    public Long findTotalCount(PostSearch postSearch) {
        return postRepository.findTotalCount(postSearch);
    }

}
