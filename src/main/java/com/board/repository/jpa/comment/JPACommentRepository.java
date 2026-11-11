package com.board.repository.jpa.comment;

import com.board.controller.comment.CommentConst;
import com.board.controller.comment.CommentForm;
import com.board.entity.Comment;
import com.board.entity.Member;
import com.board.entity.Post;
import com.board.repository.CommentRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JPACommentRepository implements CommentRepository {
    private final String DTO_MAPPING = "CommentDtoMapping";

    private final EntityManager em;

    @Autowired
    public JPACommentRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Long insertComment(Member member, CommentForm commentForm) {
        Comment comment = new Comment(commentForm.getParentId(), commentForm.getPostId(),
                member.getId(), member.getNickname(), commentForm.getContent());

        em.persist(comment);
        return comment.getId();
    }

    @Override
    public Comment findOne(Long commentId) {
        return em.find(Comment.class, commentId);
    }

    @Override
    public List<CommentDTO> findComments(Long postId, int page) {
        String sql = "select c from comment c where c.postId =:postId and c.parentId = 0 order by c.regdate desc";
        List<Comment> list = em.createQuery(sql, Comment.class)
                .setParameter("postId", postId)
                .setFirstResult((int)((page - 1) * CommentConst.COUNT_PER_PAGE))
                .setMaxResults(CommentConst.COUNT_PER_PAGE)
                .getResultList();

        List<CommentDTO> result = new ArrayList<>();
        for (Comment c : list) {
            result.add(new CommentDTO(c, 0));
        }

        return result;
    }

    @Override
    public List<CommentDTO> findReplyComments(Long postId, Long parentId) {
        String sql = "select c.*, temp.level from comment c , ( \n" +
                "with recursive temp(id, parentId, path, level) as ( \n" +
                " select c.id, c.parent_id, concat(LPAD(0, 10, '0'), '/', LPAD(c.id, 10, '0')) as path, 1 as level from comment c where c.parent_id=:parentId and c.post_id=:postId \n" +
                " union all \n" +
                " select c.id, c.parent_id, concat(prev.path, '/', LPAD(c.id, 10, '0')) as path, prev.level+1 from comment c \n" +
                " inner join temp as prev on c.parent_id = prev.id \n" +
                ") select temp.id, temp.level from temp order by temp.path asc) as temp where c.id = temp.id\n";
        List<?> list = em.createNativeQuery(sql, DTO_MAPPING)
                .setParameter("postId", postId)
                .setParameter("parentId", parentId)
                .getResultList();

        return list.stream()
                .map(CommentDTO.class::cast)
                .toList();
    }

    @Override
    public Map<Long, Integer> findCounts(List<Post> postList, String category) {
        StringBuilder builder = new StringBuilder();
        builder.append(postList.get(0).getPostId());
        for (int index = 1; index < postList.size(); ++index) {
            builder.append(", ");
            builder.append(postList.get(index).getPostId());

        }

        String numberStr = builder.toString();
        String sql = "SELECT p.post_id p, nvl(c.cnt, 0) cnt "
                + " FROM " + category + "_POST p LEFT JOIN (select post_id, count(*) as cnt from " + category + "_comment group by post_id) c ON p.post_id = c.post_id"
                + " WHERE p.post_id in (" + numberStr + ")";



        Map<Long, Integer> map = new HashMap<>();

        return map;
    }

    @Override
    public Long findTotalCount(Long postId) {
//        String sql = "SELECT COUNT(COMMENT_ID) AS COUNT FROM " + category + "_COMMENT WHERE POST_ID = ?";

        long count = 0L;



        return count;
    }

    @Override
    public Long findTopTotalCount(Long postId) {
//        String sql = "SELECT COUNT(COMMENT_ID) AS COUNT FROM " + category
//                + "_COMMENT WHERE POST_ID = ? AND PARENT_ID = 0";

        long count = 0L;


        return count;
    }

    @Override
    public int findCount(String category, String column, Long commentId) {
        String sql = "SELECT \"" + column + "\" FROM " + category + "_COMMENT WHERE COMMENT_ID = ?";

        int count = 0;


        return count;
    }

    @Override
    public int updateContent(String category, Long commentId, String content) {
        String sql = "UPDATE " + category + "_COMMENT SET CONTENT = ? WHERE COMMENT_ID = ?";

        int result = 0;

        return result;
    }

    @Override
    public int updateCount(String category, String column, Long commentId) {
        String sql = "UPDATE " + category + "_COMMENT SET \"" + column + "\" = \"" + column + "\" + 1 "
                + " WHERE COMMENT_ID = ?";


        int result = 0;


        return result;
    }

    @Override
    public int deleteComment(String category, Long commentId) {
        String sql = "UPDATE " + category + "_COMMENT SET BLIND = 1 WHERE COMMENT_ID = ?";

        int result = 0;


        return result;
    }
}
