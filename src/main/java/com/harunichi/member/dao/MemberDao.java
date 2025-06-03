package com.harunichi.member.dao;

import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.harunichi.member.vo.MemberVo;

//memeber 작업 dao 인터페이스
public interface MemberDao {
	
	//회원가입 기능 : 새로운 정보가 담긴 MemberVo객체를 받아서 DB에 저장
	public int insertMember(MemberVo memberVO) throws DataAccessException;
	
	//로그인 기능 : 로그인 정보가 담긴 Map을 받아서 회원정보 조회
	public MemberVo login(Map<String, String> loginMap) throws DataAccessException;
	
	//아이디 중복 확인 기능
	public String selectOverlappedID(String id) throws DataAccessException;
	
	//닉네임 중복 확인 기능
	public String selectOverlappedNick(String nick) throws DataAccessException;
	
	//회원 정보 조회 기능 : 특정아이디에 해당하는 회원 정보를 Membervo객체로 가져옴
	public MemberVo selectMemberById(String id) throws DataAccessException;
	
	//(카카오아이디) DB에 이미 가입된 회원인지 확인하는 메서드
	public MemberVo selectMemberByKakaoId(String kakaoId) throws DataAccessException;
	
	//회원 정보 수정 기능
	public int updateMember(MemberVo memberVO) throws DataAccessException;
	 
	//비밀번호 변경 기능
	public int updatePassword(String id, String newPass) throws DataAccessException;
	
	//회원 탈퇴 기능
	public int deleteMember(String id) throws DataAccessException;
	
	// MemberVO selectAPIUser(String apiId, String platform) throws DataAccessException; // API 로그인 관련
    // void insertKakaoUser(APILoginVO kakaoUser) throws DataAccessException; // API 회원 가입 관련
    // public String selectMemID(MemberVO memberVO) throws DataAccessException;
    // public MemberVO checkMemInfo(Map<String, String> checkMem) throws DataAccessException;
}
