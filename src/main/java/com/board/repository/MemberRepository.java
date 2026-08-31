package com.board.repository;

import com.board.entity.Member;
import com.board.entity.PersistenceLogin;

import java.util.Optional;

public interface MemberRepository {
	void insertMember(Member member);

	Member findMemberByRefreshToken(String refreshToken);
	Member findMemberById(String id);
	int insertRefreshToken(PersistenceLogin persistenceLogin);
//	Member findMemberByField(String field, String value);
	public Optional<Member> findMemberByField(String field, String value);
	int deleteRefreshToken(String refreshToken);
//	int updatePassword(String id, String encryptedPassword);
//	int updateEmail(String id, String email);
//	int updateNickname(String id, String nickname);
	int deleteRefreshTokenById(String id);
}
