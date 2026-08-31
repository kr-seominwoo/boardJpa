package com.board.repository.jpa;

import com.board.controller.PostConst;
import com.board.entity.Post;
import com.board.repository.*;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JPAPostRepository implements PostRepository {

    private EntityManager em;

    @Autowired
    public JPAPostRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public int deletePost(String category, Long id) {
//		String sql = "UPDATE " + category + "_POST SET BLIND = 1 WHERE POST_ID = ?";
        String sql = "update post p set p.blind = 1 where p.post_id  = :post_id";
        return em.createQuery(sql)
                .setParameter("post_id", id)
                .executeUpdate();
    }

    @Override
    public List<Post> findPosts(PostAllSearch postAllSearch) {
//		String sql = "SELECT * FROM (SELECT ROWNUM NUM, P.* FROM (SELECT * FROM "
//				+ postAllSearch.getCategory() + "_POST" + " WHERE BLIND = 0 ORDER BY " + postAllSearch.getOrder() + " DESC) P WHERE P."
//				+ postAllSearch.getField() + " LIKE ?) WHERE NUM BETWEEN ? AND ?";
        String sql = "select p from post p where p.blind = 0 and p.category =:category and p."
                + postAllSearch.getField() + " like :query "
                + " order by p." + postAllSearch.getOrder() + " desc";

        return em.createQuery(sql, Post.class)
                .setParameter("category", postAllSearch.getCategory())
                .setParameter("query", "%" + postAllSearch.getQuery() + "%")
                .setFirstResult((postAllSearch.getPage() - 1) * PostConst.COUNT_PER_PAGE)
                .setMaxResults(PostConst.COUNT_PER_PAGE)
                .getResultList();
    }

    @Override
    public List<Post> findPosts(PostSearch postSearch) {
//		String sql = "SELECT * FROM (SELECT ROWNUM NUM, P.* FROM (SELECT * FROM "
//				+ postSearch.getCategory() + "_POST" + " WHERE BLIND = 0 AND CATEGORY = ? ORDER BY " + postSearch.getOrder() + " DESC) P WHERE P."
//				+ postSearch.getField() + " LIKE ?) WHERE NUM BETWEEN ? AND ?";
//        String sql = "select p from post p where p.blind = 0 and p.category =:category and p.subcategory =:subcategory and "
//                + postSearch.getField() + " LIKE concat('%', :field, '%')"
//                + " order by p." + postSearch.getOrder() + " desc";

        String sql = "select p from post p where p.blind = 0 and p.category =:category and p.subCategory =:subCategory and p."
                + postSearch.getField() + " like :query"
                + " order by p." + postSearch.getOrder() + " desc";
        return em.createQuery(sql, Post.class)
                .setParameter("category", postSearch.getCategory())
                .setParameter("subCategory", postSearch.getSubCategory())
                .setParameter("query", "%" + postSearch.getQuery() + "%")
                .setFirstResult((postSearch.getPage() - 1) * PostConst.COUNT_PER_PAGE)
                .setMaxResults(PostConst.COUNT_PER_PAGE)
                .getResultList();
    }

    @Override
    public Post findPost(String category, Long id) {
//		String sql = "SELECT * FROM " + category + "_POST WHERE POST_ID = ?";
        return em.find(Post.class, id);
    }

    @Override
    public String findMemberIdByPostId(String category, Long id) {
//		String sql = "SELECT MEMBER_ID FROM " + category + "_POST WHERE POST_ID = ?";
        String sql = "select p.memberId from post p where p.postId = :post_id";
        return (String) em.createQuery(sql)
                .setParameter("post_id", id)
                .getSingleResult();
    }

    @Override
    public Long insertPost(String category, PostForm postForm) {
//		String sql = "INSERT INTO " +  category + "_POST(MEMBER_ID, WRITER, TITLE, CONTENT, CATEGORY) VALUES(?,?,?,?,?)";
        // Long postId, String memberId, String writer, String title, String content, Date regdate, Integer like,
        //			Integer unlike, Integer hit, String category
        Post post = new Post(postForm.getMemberId(),
                postForm.getWriter(), postForm.getTitle(),
                postForm.getContent(),
                postForm.getCategory(), postForm.getSubCategory());

        em.persist(post);
        return post.getPostId();
    }

    @Override
    public int updatePost(String category, UpdatePostForm updatePostForm) {
//		String sql = "UPDATE " +  category + "_POST SET TITLE = ?, CONTENT = ? WHERE POST_ID = ?";
        String sql = "update post p set p.title = :title and p.content = :content where p.postId = :post_id";

        return em.createQuery(sql)
                .setParameter("title", updatePostForm.getTitle())
                .setParameter("content", updatePostForm.getContent())
                .setParameter("post_id", updatePostForm.getPostId())
                .executeUpdate();
    }

    @Override
    public int updateCount(String category, Long id, String column) {
//		String sql = "UPDATE " +  category + "_POST SET " + column + " = " + column + " + 1 WHERE POST_ID = ?";
        String sql = "update post p set p." + column + " = p." + column + " + 1 where p.postId = :post_id";

        return em.createQuery(sql)
                .setParameter("post_id", id)
                .executeUpdate();
    }

    //좋아요, 싫어요, 조회수 찾기
//	@Override
//	public int findCount(String category, Long id, String column) {
//		String sql = "SELECT " + column + " FROM " + category + "_POST WHERE POST_ID = ?";
//
//		Connection con = null;
//		PreparedStatement preparedStatement = null;
//		ResultSet rs = null;
//		int cnt = 0;
//
//		try {
//			con = DataSourceUtils.getConnection(this.dataSource);
//			preparedStatement = con.prepareStatement(sql);
//			preparedStatement.setLong(1, id);
//
//			rs = preparedStatement.executeQuery();
//			if(rs.next()) {
//				cnt = rs.getInt(column);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			close(rs, preparedStatement, con);
//		}
//
//		return cnt;
//	}
//
    @Override
    public Long findTotalCount(PostAllSearch postAllSearch) {
        String sql = "select count(p.postId) as count from post p where p." + postAllSearch.getField() + " like :field and p.category = :category";
        return em.createQuery(sql, Long.class)
                .setParameter("field", "%" + postAllSearch.getQuery() + "%")
                .setParameter("category", postAllSearch.getCategory())
                .getSingleResult();

//		String sql = "SELECT COUNT(POST_ID) AS COUNT FROM " + category + "_POST WHERE " + postAllSearch.getField() + " LIKE ?";
//		Connection con = null;
//		PreparedStatement preparedStatement = null;
//		ResultSet rs = null;
//		int cnt = 0;
//
//		try {
//			con = DataSourceUtils.getConnection(this.dataSource);
//			preparedStatement = con.prepareStatement(sql);
//			preparedStatement.setString(1, '%' + postAllSearch.getQuery() + '%');
//
//			rs = preparedStatement.executeQuery();
//			if(rs.next()) {
//				cnt = rs.getInt("COUNT");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			close(rs, preparedStatement, con);
//		}
//
//		return cnt;
    }

    @Override
    public Long findTotalCount(PostSearch postSearch) {
        String sql = "select count(p.postId) as count from post p where p.category =:category  and " +
                "p.subCategory =: subCategory and " +
                "p." + postSearch.getField() + " like :field";
        return em.createQuery(sql, Long.class)
                .setParameter("category", postSearch.getCategory())
                .setParameter("subCategory", postSearch.getSubCategory())
                .setParameter("field", "%" + postSearch.getQuery() + "%")
                .getSingleResult();

//		String sql = "SELECT COUNT(POST_ID) AS COUNT FROM " + postSearch.getCategory() + "_POST WHERE CATEGORY = ? AND " + postSearch.getField() + " LIKE ?";
//		Connection con = null;
//		PreparedStatement preparedStatement = null;
//		ResultSet rs = null;
//		int cnt = 0;
//
//		try {
//			con = DataSourceUtils.getConnection(this.dataSource);
//			preparedStatement = con.prepareStatement(sql);
//			preparedStatement.setString(1, postSearch.getCategory());
//			preparedStatement.setString(2, '%' + postSearch.getQuery() + '%');
//			rs = preparedStatement.executeQuery();
//			if(rs.next()) {
//				cnt = rs.getInt("COUNT");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			close(rs, preparedStatement, con);
//		}
//
//		return cnt;
    }
}
