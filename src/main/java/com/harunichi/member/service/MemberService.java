package com.harunichi.member.service;

import com.harunichi.member.vo.MemberVo;

public interface MemberService {
	
	public MemberVo selectMemberByKakaoId(String kakaoId) throws Exception;
	
}
