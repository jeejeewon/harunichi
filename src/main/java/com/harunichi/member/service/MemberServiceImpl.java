package com.harunichi.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
	public int insertMember(MemberVo memberVO) throws DataAccessException {
		
		return memberDao.insertMember(memberVO);
	}

	@Override
	public int updateMember(MemberVo memberVo) throws Exception {
		return memberDao.updateMember(memberVo);
	}

	@Override
	public MemberVo selectMemberByKakaoId(String kakaoId) throws Exception {
		return memberDao.selectMemberByKakaoId(kakaoId);
	}
	//이메일 중복 체크 기능 (매개변수로 받은 이메일이 중복인지 아닌지를 판단)
	@Override
	public boolean isEmailDuplicate(String email) {
		return memberDao.selectMemberByEmail(email) != null;
	}

	
	

}
