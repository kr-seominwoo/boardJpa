package com.board.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import com.board.entity.Member;
import com.board.entity.PersistenceLogin;
import com.board.error.MemberError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.board.repository.MemberRepository;
import com.board.security.EncryptiontSecurity;
import com.board.controller.customer.EmailForm;
import com.board.controller.customer.LoginResult;
import com.board.controller.customer.MemberForm;
import com.board.controller.customer.NicknameForm;
import com.board.controller.customer.PasswordForm;

@Transactional(readOnly = true)
@Service
public class MemberService {
    private final EncryptiontSecurity encryptiontSecurity;
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(EncryptiontSecurity encryptiontSecurity, MemberRepository memberRepository) {
        this.encryptiontSecurity = encryptiontSecurity;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberError registMember(MemberForm memberForm) {
        if (!validateBirthday(memberForm.getBirthday())) {
            return MemberError.INVALID_BIRTHDAY;
        }

        if (!validateId(memberForm.getId())) {
            return MemberError.INVALID_ID;
        }

        if (!validatePassword(memberForm.getPassword(), memberForm.getConfirmationPassword())) {
            return MemberError.INVALID_PASSWORD;
        }

        if (!validateNickname(memberForm.getNickname())) {
            return MemberError.INVALID_NICKNAME;
        }

        if (!validateEmail(memberForm.getEmail())) {
            return MemberError.INVALID_EMAIL;
        }

        if (existId(memberForm.getId())) {
            return MemberError.DUPLICATE_ID;
        }

        if (existNickname(memberForm.getNickname())) {
            return MemberError.DUPLICATE_NICKNAME;
        }

        String encryptedPassword = this.encryptiontSecurity.encryptPassword(memberForm.getPassword());
        Member member = new Member(memberForm.getName(), memberForm.getNickname(), memberForm.getId(), memberForm.getEmail(), memberForm.getBirthday(), encryptedPassword);
        memberRepository.insertMember(member);

        return MemberError.NO_ERROR;
    }

    public boolean validateBirthday(Date birthday) {
        Date now = java.sql.Date.valueOf(LocalDate.now());
        return birthday.compareTo(now) < 1;
    }

    public boolean validatePassword(String password, String confirmationPassword) {
        // 숫자
        String numberPattern = "(.*)[0-9](.*)$";
        // 영문자
        String alphabetPattern = "(.*)[a-zA-Z](.*)$";
        // 특수문자
        String specialPattern = "(.*)[!-/:-@\\[-`{-~](.*)$";
        // 포함 문자
        String allPattern = "^[!-~]{8,20}$";

        // 확인용 비밀번호와 일치하는지 확인
        if (password.compareTo(confirmationPassword) != 0) {
            return false;
        }
        // 숫자, 문자, 특수문자가 알맞게 들어가있는지 확인
        else if (!Pattern.matches(numberPattern, password) || !Pattern.matches(alphabetPattern, password)
                || !Pattern.matches(specialPattern, password) || !Pattern.matches(allPattern, password)) {
            return false;
        }

        return true;
    }

    public boolean validateEmail(String email) {
        String pattern = "\\w+@\\w+.\\w+(\\.\\w+)?{3,320}";
        return Pattern.matches(pattern, email);
    }

    public boolean validateId(String id) {
        String pattern = "^[a-zA-Z0-9_-]{5,20}$";
        return Pattern.matches(pattern, id);
    }

    public boolean validateNickname(String nickname) {
        String pattern = "^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣_-]{5,20}$";
        return Pattern.matches(pattern, nickname);
    }

    public boolean existId(String id) {
        Optional<Member> findMember = memberRepository.findMemberByField("id", id);
        return findMember.isPresent();
    }

    public boolean existNickname(String nickname) {
        Optional<Member> findMember = memberRepository.findMemberByField("nickname", nickname);
        return findMember.isPresent();
    }

    @Transactional
    public LoginResult login(String id, String password, String loginKeep) {
        Member member = null;
        String resfreshToken = null;

        member = memberRepository.findMemberById(id);
        if (encryptiontSecurity.matches(password, member.getEncryptedPassword())) {
            if (loginKeep != null) {
                resfreshToken = UUID.randomUUID().toString();
                PersistenceLogin persistenceLogin = new PersistenceLogin(resfreshToken, member);
                this.memberRepository.insertRefreshToken(persistenceLogin);
            }
        } else {
            member = null;
        }

        return new LoginResult(member, resfreshToken);
    }

    @Transactional
    public int logout(String refreshToken) {
        // 토큰 삭제
        return memberRepository.deleteRefreshToken(refreshToken);
    }

    @Transactional
    public boolean updatePassword(PasswordForm passwordForm) {
        Member member = null;
        int result = 0;

        member = memberRepository.findMemberById(passwordForm.getId());
        if (encryptiontSecurity.matches(passwordForm.getCurrentPassword(), member.getEncryptedPassword())
                && validatePassword(passwordForm.getNewPassword(), passwordForm.getConfirmationPassword())) {
            String encryptedPassword = this.encryptiontSecurity.encryptPassword(passwordForm.getNewPassword());
            member.updatePassword(encryptedPassword);
//				result = memberRepository.updatePassword(passwordForm.getId(), encryptedPassword);
            result = memberRepository.deleteRefreshTokenById(passwordForm.getId());
        }

        return result != -1;
    }

    @Transactional
    public MemberError updateEmail(EmailForm form) {
        if (!validateEmail(form.getEmail())) {
            return MemberError.INVALID_EMAIL;
        }

        Member member = null;
        MemberError result = MemberError.NO_ERROR;

        member = memberRepository.findMemberById(form.getId());
        if (member != null && encryptiontSecurity.matches(form.getPassword(), member.getEncryptedPassword())) {
            member.updateEmail(form.getEmail());
        } else {
            result = MemberError.WRONG_PASSWORD;
        }

        return result;
    }

    @Transactional
    public MemberError updateNickname(NicknameForm form) {
        if (!validateNickname(form.getNickname())) {
            return MemberError.INVALID_NICKNAME;
        }

        Member member = null;
        MemberError result = MemberError.NO_ERROR;

        if (existNickname(form.getNickname())) {
            result = MemberError.DUPLICATE_NICKNAME;
        } else {
            member = memberRepository.findMemberById(form.getId());
            if (member != null && encryptiontSecurity.matches(form.getPassword(), member.getEncryptedPassword())) {
                member.updateNickname(form.getNickname());
            } else {
                result = MemberError.WRONG_PASSWORD;
            }
        }

        return result;
    }
}
