package com.harunichi.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harunichi.member.dao.MemberDao;
import com.harunichi.member.vo.MemberVo;

@Service("memberService")
public class MemberServiceImpl implements MemberService{
	
	@Autowired
	private MemberDao memberDao;

	@Override
	public MemberVo selectMemberByKakaoId(String kakaoId) throws Exception {
		return memberDao.selectMemberByKakaoId(kakaoId);
	}
	
	

}
