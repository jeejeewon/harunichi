package com.harunichi.board.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.harunichi.board.service.BoardService;
import com.harunichi.board.vo.BoardVo;

@Controller("boardController")
@RequestMapping("/board")
public class BoardControllerImpl implements BoardController {
	
	@Autowired
	private BoardService boardService;

	@Override
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView boardList(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		
				// 1. BoardService를 호출하여 게시글 목록 (List<BoardVo>)을 가져옵니다.
				List<BoardVo> boardVoList = boardService.selectBoardList(); // 변수 이름을 boardVoList로 변경하여 명확히 구분

				// 2. Map 형태로 변환된 게시글 목록을 담을 새로운 리스트를 생성합니다.
				List<Map<String, Object>> boardList = new ArrayList<>(); // JSP에 전달할 최종 리스트

				// 3. 가져온 BoardVo 리스트를 순회하며 각 BoardVo 객체를 Map으로 변환합니다.
				for (BoardVo boardVo : boardVoList) {
					Map<String, Object> map = new HashMap<>();
					// BoardVo 객체의 필드 값을 Map에 담습니다.
					// 키 값은 JSP에서 사용할 이름으로 지정합니다.
					map.put("boardId", boardVo.getBoardId());
					map.put("boardCont", boardVo.getBoardCont());
					map.put("boardDate", boardVo.getBoardDate());
					map.put("boardImg", boardVo.getBoardImg());
					map.put("boardLike", boardVo.getBoardLike());
					map.put("boardCount", boardVo.getBoardCount());
					map.put("boardRe", boardVo.getBoardRe());

					// Map을 결과 리스트에 추가합니다.
					boardList.add(map);
				}

				// 4. ModelAndView 객체를 생성하고 뷰 이름("/board/list")을 지정합니다.
				ModelAndView mav = new ModelAndView("/board/list");

				// 5. Map 형태로 변환된 게시글 목록(boardList)을 ModelAndView 객체에 담아 JSP로 전달합니다.
				//    이때, JSP에서 사용할 이름(key)을 "boardList"로 지정합니다.
				mav.addObject("boardList", boardList);

				// 6. 데이터가 담긴 ModelAndView 객체를 반환합니다.
				return mav;
	}

}
