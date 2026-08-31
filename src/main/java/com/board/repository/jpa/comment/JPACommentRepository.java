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

    private final EntityManager em;

    @Autowired
    public JPACommentRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Long insertComment(Member member, CommentForm commentForm) {
//        Long parentId, Long postId, String memberId, String writer, String content
//        Comment comment = new Comment(commentForm.get);


        String sql = "INSERT INTO " + commentForm.getCategory()
                + "_COMMENT(MEMBER_ID, WRITER, CONTENT, POST_ID, PARENT_ID) VALUES(?,?,?,?,?)";
        String[] col = { "COMMENT_ID" };

        Long result = 0L;
//        Connection con = null;
//        PreparedStatement preparedStatement = null;
//        ResultSet rs = null;
//
//        try {
//            con = DataSourceUtils.getConnection(dataSource);
//            preparedStatement = con.prepareStatement(sql, col);
//            preparedStatement.setString(1, member.getId());
//            preparedStatement.setString(2, member.getNickname());
//            preparedStatement.setString(3, commentForm.getContent());
//            preparedStatement.setLong(4, commentForm.getPostId());
//            preparedStatement.setLong(5, commentForm.getParentId());
//
//            result = (long) preparedStatement.executeUpdate();
//            rs = preparedStatement.getGeneratedKeys();
//            if (rs != null && rs.next()) {
//                result = rs.getLong(1);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            result = 0L;
//        } finally {
//            close(rs, preparedStatement, con);
//        }

        return result;
    }

    @Override
    public Comment findOne(String category, Long commentId) {
        String sql = "SELECT c FROM comment c where c.commentId =:commentId";
        return em.createQuery(sql, Comment.class)
                .setParameter("commentId", commentId)
                .getSingleResult();
//        Connection con = null;
//        PreparedStatement preparedStatement = null;
//        ResultSet rs = null;
//        Comment comment = null;
//
//        try {
//            con = DataSourceUtils.getConnection(this.dataSource);
//            preparedStatement = con.prepareStatement(sql);
//            preparedStatement.setLong(1, commentId);
//
//            rs = preparedStatement.executeQuery();
//            if (rs.next()) {
//                Long parentId = rs.getLong("PARENT_ID");
//                Long postId = rs.getLong("POST_ID");
//                String memberId = rs.getString("MEMBER_ID");
//                String writer = rs.getString("WRITER");
//                String content = rs.getString("CONTENT");
//                Date regdate = rs.getDate("REGDATE");
//                int like = rs.getInt("LIKE");
//                int unlike = rs.getInt("LIKE");
//                int blind = rs.getInt("BLIND");
//
//                comment = new Comment(commentId, parentId, postId, memberId, writer, content, regdate, like, unlike, blind);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            close(rs, preparedStatement, con);
//        }
//
//        return comment;
    }

    @Override
    public List<CommentDTO> findComments(Long postId, int page) {
//        String sql = "SELECT * FROM( \r\n" + " SELECT ROWNUM NUM, C.* \r\n" + " FROM (\r\n" + "  SELECT * FROM "
//                + category + "_COMMENT \r\n" + "  WHERE POST_ID =: postId AND PARENT_ID = 0 \r\n" + "  ORDER BY REGDATE DESC \r\n"
//                + " ) C \r\n" + ")\r\n" + "WHERE NUM BETWEEN :start AND :end";
        String sql = "select c from comment c where c.postId =:postId and c.parentId = 0 order by c.regdate desc";
        List<Comment> list = em.createQuery(sql, Comment.class)
                .setParameter("postId", postId)
                .setFirstResult((int)((page - 1) * CommentConst.COUNT_PER_PAGE))
                .setMaxResults(CommentConst.COUNT_PER_PAGE)
                .getResultList();

        List<CommentDTO> result = new ArrayList<>();
        for (int i = 0; i < list.size(); ++i) {
            Comment c = list.get(i);
            CommentDTO dto = new CommentDTO(c.getId(), c.getMemberId(), c.getWriter(),
                    c.getContent(), c.getRegdate(), c.getLikes(),
                    c.getUnlike(), 0, c.getBlind());
            result.add(dto);
        }

        return result;

//        Connection con = null;
//        PreparedStatement preparedStatement = null;
//        ResultSet rs = null;
//        List<CommentDTO> list = new ArrayList<>();
//
//        try {
//            con = DataSourceUtils.getConnection(this.dataSource);
//            preparedStatement = con.prepareStatement(sql);
//            preparedStatement.setLong(1, postId);
//            preparedStatement.setInt(2, 1 + (page - 1) * CommentConst.COUNT_PER_PAGE);
//            preparedStatement.setInt(3, page * CommentConst.COUNT_PER_PAGE);
//
//            rs = preparedStatement.executeQuery();
//            while (rs.next()) {
//                // blind를 entity에 추가해야 함
//                Long id = rs.getLong("COMMENT_ID");
//                String memberId = rs.getString("MEMBER_ID");
//                String writer = rs.getString("WRITER");
//                String content = rs.getString("CONTENT");
//                Date regdate = rs.getTimestamp("REGDATE");
//                int like = rs.getInt("LIKE");
//                int unlike = rs.getInt("UNLIKE");
//                int blind = rs.getInt("BLIND");
//
//                CommentDTO comment = new CommentDTO(id, memberId, writer, content, regdate, like, unlike, 0, blind);
//                list.add(comment);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            close(rs, preparedStatement, con);
//        }
//
//        return list;
    }

    @Override
    public List<CommentDTO> findReplyComments(String category, Long postId, Long parentId) {
        String sql = "SELECT c.*, level as \"level\" \r\n" + "  FROM " + category + "_COMMENT c \r\n"
                + "  WHERE POST_ID =:postId \r\n" + "  START WITH PARENT_ID =:parentId \r\n"
                + "  CONNECT BY PARENT_ID = PRIOR COMMENT_ID" + "  ORDER BY REGDATE";

        return em.createQuery(sql, CommentDTO.class)
                .setParameter("postId", postId)
                .setParameter("parentId", parentId)
                .getResultList();

//        Connection con = null;
//        PreparedStatement preparedStatement = null;
//        ResultSet rs = null;
//        List<CommentDTO> list = new ArrayList<>();
//
//        try {
//            con = DataSourceUtils.getConnection(this.dataSource);
//            preparedStatement = con.prepareStatement(sql);
//            preparedStatement.setLong(1, postId);
//            preparedStatement.setLong(2, parentId);
//
//            rs = preparedStatement.executeQuery();
//            while (rs.next()) {
//                Long id = rs.getLong("COMMENT_ID");
//                String memberId = rs.getString("MEMBER_ID");
//                String writer = rs.getString("WRITER");
//                String content = rs.getString("CONTENT");
//                Date regdate = rs.getTimestamp("REGDATE");
//                int like = rs.getInt("LIKE");
//                int unlike = rs.getInt("UNLIKE");
//                int level = rs.getInt("LEVEL");
//                int blind = rs.getInt("BLIND");
//
//                CommentDTO comment = new CommentDTO(id, memberId, writer, content, regdate, like, unlike, level, blind);
//                list.add(comment);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            close(rs, preparedStatement, con);
//        }
//
//        return list;
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
    public Long findTotalCount(String category, Long postId) {
        String sql = "SELECT COUNT(COMMENT_ID) AS COUNT FROM " + category + "_COMMENT WHERE POST_ID = ?";

        long count = 0L;



        return count;
    }

    @Override
    public Long findTopTotalCount(String category, Long postId) {
        String sql = "SELECT COUNT(COMMENT_ID) AS COUNT FROM " + category
                + "_COMMENT WHERE POST_ID = ? AND PARENT_ID = 0";

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
