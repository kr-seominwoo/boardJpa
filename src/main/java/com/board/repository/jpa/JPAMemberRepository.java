package com.board.repository.jpa;

import com.board.entity.Member;
import com.board.entity.PersistenceLogin;
import com.board.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JPAMemberRepository implements MemberRepository {

    private EntityManager em;


    @Autowired
    public JPAMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public void insertMember(Member member) {
        em.persist(member);
//		String sql = "INSERT INTO MEMBER(NAME, ID, PASSWORD, NICKNAME, EMAIL, BIRTHDAY, ROLE) VALUES(?,?,?,?,?,?,?)";
    }

    @Override
    public Member findMemberByRefreshToken(String refreshToken) {
//        String sql = "SELECT MEMBER_ID FROM PERSISTENCE_LOGINS WHERE TOKEN = ?";

        PersistenceLogin persistenceLogin = em.find(PersistenceLogin.class, refreshToken);
        return persistenceLogin != null ? persistenceLogin.getMember() : null;
    }

    @Override
    public Optional<Member> findMemberByField(String field, String value) {
//        String sql = "SELECT * FROM MEMBER WHERE " + field + " = ?";
        String sql = "select m from Member m where m." + field + "= :value";
        TypedQuery<Member> query = em.createQuery(sql, Member.class);
        query.setParameter("value", value);
        return query.getResultList().stream().findAny();
    }


    @Override
    public Member findMemberById(String id) {
//        String sql = "SELECT * FROM MEMBER WHERE ID = ?";
        return em.find(Member.class, id);
    }

    @Override
    public int insertRefreshToken(PersistenceLogin persistenceLogin) {
//        String sql = "INSERT INTO PERSISTENCE_LOGINS(MEMBER_ID, TOKEN) VALUES(?,?)";
        int result = 0;
        em.persist(persistenceLogin);
        return result;
    }

    @Override
    public int deleteRefreshToken(String refreshToken) {
//        String sql = "DELETE FROM PERSISTENCE_LOGINS WHERE TOKEN = ?";
        String sql = "DELETE FROM PersistenceLogin p WHERE p.refreshToken = :token";
        return em.createQuery(sql)
                .setParameter("token", refreshToken)
                .executeUpdate();
    }

//    @Override

//    public int updatePassword(String id, String encryptedPassword) {
////        String sql = "UPDATE MEMBER SET PASSWORD = ? WHERE ID = ?";
//        String sql = "update member m set m.password = :password where id = :id";
//        return em.createQuery(sql, Member.class)
//                .setParameter("password", encryptedPassword)
//                .setParameter("id", id)
//                .executeUpdate();
//    }
//
//    @Override
//    public int updateEmail(String id, String email) {
////        String sql = "UPDATE MEMBER SET EMAIL = ? WHERE ID = ?";
//
//        String sql = "update member m set m.email = :email where id = :id";
//        return em.createQuery(sql, Member.class)
//                .setParameter("email", email)
//                .setParameter("id", id)
//                .executeUpdate();
//    }
//
//    @Override
//    public int updateNickname(String id, String nickname) {
////        String sql = "UPDATE MEMBER SET NICKNAME = ? WHERE ID = ?";
//        String sql = "update member m set m.nickname = :nickname where id = :id";
//        return em.createQuery(sql, Member.class)
//                .setParameter("email", nickname)
//                .setParameter("id", id)
//                .executeUpdate();
//    }

    @Override
    public int deleteRefreshTokenById(String id) {
        String sql = "DELETE FROM PERSISTENCE_LOGINS p WHERE p.id = :id";
        return em.createQuery(sql, PersistenceLogin.class)
                .setParameter("id", id)
                .executeUpdate();
    }
}