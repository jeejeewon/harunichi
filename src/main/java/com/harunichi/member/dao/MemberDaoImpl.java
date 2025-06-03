package com.harunichi.member.dao;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.harunichi.member.vo.MemberVo;

@Repository("memberDao")
public class MemberDaoImpl implements MemberDao {

	@Autowired
	private SqlSession sqlSession;
	
	//회원가입 기능
	@Override
	public int insertMember(MemberVo memberVO) throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	//로그인 기능
	@Override
	public MemberVo login(Map<String, String> loginMap) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}
	
	//아이디 중복 확인 기능
	@Override
	public String selectOverlappedID(String id) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}
	
	//닉네임 중복 확인 기능
	@Override
	public String selectOverlappedNick(String nick) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}
	
	//회원 정보 조회 기능
	@Override
	public MemberVo selectMemberById(String id) throws DataAccessException {

		MemberVo memberVo = sqlSession.selectOne("mapper.member.selectMemberById", id);

		return memberVo; // DB에서 가져온 MemberVo 객체 돌려주기
	}
	
	//(카카오아이디) DB에 이미 가입된 회원인지 확인하는 메서드
	@Override
	public MemberVo selectMemberByKakaoId(String kakaoId) throws DataAccessException {
		 // sqlSession.selectOne() 메서드를 사용하여 DB에서 kakaoId로 회원 정보 조회
        MemberVo memberVo = sqlSession.selectOne("mapper.member.selectMemberByKakaoId", kakaoId);
        return memberVo;
	}

	//회원 정보 수정 기능
	@Override
	public int updateMember(MemberVo memberVO) throws DataAccessException {
		int result = sqlSession.update("mapper.member.updateMemberProfileAndLikes", memberVO);
		return result;
	}
	
	//비밀번호 변경 기능
	@Override
	public int updatePassword(String id, String newPass) throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	//회원 탈퇴 기능
	@Override
	public int deleteMember(String id) throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

}
