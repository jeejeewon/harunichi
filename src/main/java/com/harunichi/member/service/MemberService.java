package com.harunichi.member.service;

import org.springframework.dao.DataAccessException;

import com.harunichi.member.vo.MemberVo;

public interface MemberService {
	
	//회원 가입 기능
	public int insertMember(MemberVo memberVO) throws DataAccessException;
	//회원 업데이트 기능
	public int updateMember(MemberVo memberVo) throws Exception;
	
	public MemberVo selectMemberByKakaoId(String kakaoId) throws Exception;
	//이메일 중복 체크 기능 (매개변수로 받은 이메일이 중복인지 아닌지를 판단)
	public boolean isEmailDuplicate(String email);
	
}
