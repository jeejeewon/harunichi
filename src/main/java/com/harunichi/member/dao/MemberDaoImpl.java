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

	@Override // MemberDAO 인터페이스에 있는 메소드를 오버라이드 한다는 표시!
	public MemberVo selectMemberById(String id) throws DataAccessException {
		// sqlSession 객체한테 일을 시키자!
		// selectOne: 결과를 MemberVo 객체 하나로 받아올 거니까!
		// "mapper.member.selectMemberById": << 요게 바로 매퍼 XML 파일에서 찾을 쿼리의 이름표!
		// "mapper.member" 부분은 매퍼 XML 파일의 namespace="mapper.member" 이거고,
		// "selectMemberById" 부분은 매퍼 XML 파일의 <select id="selectMemberById"> 이거야!
		// id: 이 쿼리를 실행할 때 넘겨줄 데이터(파라미터)야. 여기서는 조회할 회원 아이디!
		MemberVo memberVo = sqlSession.selectOne("mapper.member.selectMemberById", id);

		return memberVo; // DB에서 가져온 MemberVo 객체 돌려주기!
	}

	@Override
	public int insertMember(MemberVo memberVO) throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MemberVo login(Map<String, String> loginMap) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String selectOverlappedID(String id) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String selectOverlappedNick(String nick) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateMember(MemberVo memberVO) throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updatePassword(String id, String newPass) throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteMember(String id) throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

}
