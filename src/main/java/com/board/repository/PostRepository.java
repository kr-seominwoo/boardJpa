package com.board.repository;

import java.util.List;

import com.board.entity.Post;

public interface PostRepository {
    List<Post> findPosts(PostAllSearch postAllSearch);

    List<Post> findPosts(PostSearch postSearch);

    Post findPost(String category, Long id);

    Long insertPost(String category, PostForm postForm);

    int updatePost(String category, UpdatePostForm updatePostForm);

    int updateCount(String category, Long id, String column);

    int deletePost(String category, Long id);

    //	int findCount(String category, Long id, String column);
//	int findTotalCount(String category, PostAllSearch postAllSearch);
//	int findTotalCount(String category, PostSearch postSearch);
    Long findTotalCount(PostAllSearch postAllSearch);

    Long findTotalCount(PostSearch postSearch);

    String findMemberIdByPostId(String category, Long id);
}
