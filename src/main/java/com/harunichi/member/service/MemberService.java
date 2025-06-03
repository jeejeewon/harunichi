package com.harunichi.member.service;

import com.harunichi.member.vo.MemberVo;

public interface MemberService {
	
	public int updateMember(MemberVo memberVo) throws Exception;
	
	public MemberVo selectMemberByKakaoId(String kakaoId) throws Exception;
	
}
