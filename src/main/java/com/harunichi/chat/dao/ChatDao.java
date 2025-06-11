package com.harunichi.chat.dao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.harunichi.chat.vo.ChatVo;
import com.harunichi.member.vo.MemberVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ChatDao {

	@Autowired
	private SqlSession sqlSession;	
	
	private static final String NAMESPACE = "mapper.chat.";

	
	
	//채팅 저장
	public void saveMessage(ChatVo chatMsg) {		
		System.out.println("ChatDao의 saveMessage 메소드 실행 ===============");	
		sqlSession.insert(NAMESPACE + "saveMessage", chatMsg);			
	}

	
	
	//친구 추천 리스트 조회
	public List<MemberVo> selectMembers(String id) {
		System.out.println("ChatDao의 selectMembers 메소드 실행 ===============");		
		
		//사용자의 관심사 조회하여 변수에 저장
		String myLike = sqlSession.selectOne(NAMESPACE + "selectMyLike", id);		
//		System.out.println(myLike);
		
		//조회한 관심사를 ","로 구분하여 자른 후 List에 저장
		List<String> likeList = Arrays.stream(myLike.split(","))
								      .map(String::trim) 
								      .collect(Collectors.toList());
		
		//map에 관심사와 아이디를 저장하여 매퍼파일 매개변수로 전달
		Map<String, Object> MyLikeMap = new HashMap<String, Object>();
		MyLikeMap.put("likeList", likeList);
		MyLikeMap.put("id", id);
		
		//관심사와 연령대가 비슷한 사람 10명 조회하여 List에 저장
		List<MemberVo> resultList = sqlSession.selectList(NAMESPACE + "selectMembers", MyLikeMap);
		
		//List에 10명이 저장되지않았을 경우 (랜덤으로 추천하기 위함)
		if (resultList.size() < 10) {
		    int shortage = 10 - resultList.size();	//부족한 멤버수 계산하여 저장
		    
		    //조회된 멤버들 중복 조회 방지를 위해 조회된 멤버들 id를 List에 저장
		    List<String> alreadySelectedIds = resultList.stream()
									          .map(MemberVo::getId)
									          .collect(Collectors.toList());
		    
		    //사용자 아이디, 조회된 멤버 아이디, 부족한 멤버수를 Map에 저장하여 매퍼파일 매개변수로 전달
		    Map<String, Object> randomMap = new HashMap<>();
		    randomMap.put("id", id);
		    randomMap.put("alreadySelectedIds", alreadySelectedIds);
		    randomMap.put("shortage", shortage);

		    List<MemberVo> randomMembers = sqlSession.selectList(NAMESPACE + "selectRandomMember", randomMap);
		    resultList.addAll(randomMembers);
		}
		return resultList;		
	}


		
	//DB에서 채팅방 ID 조회
	public String selectRoomId(String senderId, String receiverId) {		
		System.out.println("ChatDao의 selectRoomId 메소드 실행 ===============");	
		
		Map<String, String> idMap = new HashMap<String, String>();
		idMap.put("senderId", senderId);
		idMap.put("receiverId", receiverId);
			
		return sqlSession.selectOne(NAMESPACE + "selectRoomId", idMap);
	}
	


	//DB에서 채팅 내역 조회
	public List<ChatVo> selectChatHistory(String roomId) {
		System.out.println("ChatDao의 selectChatHistory 메소드 실행 ===============");
		return sqlSession.selectList(NAMESPACE + "selectChatHistory", roomId);
	}



	//DB의 chatRoom테이블에 roomId 저장
	public void insertRoomId(Map<String, String> roomMap) {
		System.out.println("ChatDao의 insertRoomId 메소드 실행 ===============");
		sqlSession.insert(NAMESPACE + "insertRoomId", roomMap);				
	}


	//채팅방에 참여하고 있는 유저 조회
	public String selectUserCount(String roomId) {
		System.out.println("ChatDao의 selectUserCount 메소드 실행 ===============");
		return sqlSession.selectOne(NAMESPACE + "selectUserCount", roomId);
	}


	//채팅방 타이틀 조회(단체)
	public String selectTitle(String roomId) {		
		System.out.println("ChatDao의 selectTitle 메소드 실행 ===============");
		return sqlSession.selectOne(NAMESPACE + "selectTitle", roomId);
	}


	//채팅방 타이틀 조회(개인)
	public String selectNick(String receiverId) {
		System.out.println("ChatDao의 selectNick 메소드 실행 ===============");
		return sqlSession.selectOne(NAMESPACE + "selectNick", receiverId);
	}





	
	
}
