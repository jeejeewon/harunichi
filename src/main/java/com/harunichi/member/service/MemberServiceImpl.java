package com.harunichi.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.harunichi.member.dao.MemberDao;
import com.harunichi.member.vo.MemberVo;

@Service("memberService")
@Transactional
public class MemberServiceImpl implements MemberService{
	
	@Autowired
	private MemberDao memberDao;
	
	@Override
	public int updateMember(MemberVo memberVo) throws Exception {
		return memberDao.updateMember(memberVo);
	}

	@Override
	public MemberVo selectMemberByKakaoId(String kakaoId) throws Exception {
		return memberDao.selectMemberByKakaoId(kakaoId);
	}

	
	

}
