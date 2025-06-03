package com.harunichi.board.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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
		List<BoardVo> boardVoList = boardService.selectBoardList();

		// 2. ModelAndView 객체를 생성하고 뷰 이름("/board/list")을 지정합니다.
		ModelAndView mav = new ModelAndView("/board/list");

		// 3. 게시글 목록(boardVoList)을 ModelAndView 객체에 담아 JSP로 전달합니다.
		// 이때, JSP에서 사용할 이름(key)을 "boardList"로 지정합니다.
		mav.addObject("boardList", boardVoList);

		// 4. 데이터가 담긴 ModelAndView 객체를 반환합니다.
		return mav;
	}

	private static final String FILE_UPLOAD_PATH = "C:/board/upload/"; // 파일 저장 경로 설정 (실제 환경에 맞게 변경 필요)
	private static final int MAX_IMAGE_COUNT = 4; // 최대 이미지 파일 개수

	private static final int IMG1 = 0, IMG2 = 1, IMG3 = 2, IMG4 = 3; // 이미지 상수

	@Override
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView boardEdit(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "boardId", defaultValue = "0") int boardId,
			@RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles) {

		ModelAndView mav = new ModelAndView();

		try {
			// 1. BoardVo 객체 생성 및 설정
			BoardVo boardVo = new BoardVo();
			if (boardId > 0) {
				boardVo.setBoardId(boardId);
			}

			String boardCont = request.getParameter("boardCont");
			if (boardCont != null) {
				boardVo.setBoardCont(boardCont);
			}

			String boardDateStr = request.getParameter("boardDate");
			if (boardDateStr != null) {
				try {
					boardVo.setBoardDate(Timestamp.valueOf(boardDateStr));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					mav.setViewName("/error/dateError");
					mav.addObject("msg", "잘못된 날짜 형식입니다.");
					return mav;
				}
			}

			// 2. 이미지 파일 업로드 처리 및 BoardVo에 파일 이름 설정
			String[] imgFiles = new String[MAX_IMAGE_COUNT]; // 이미지 파일 이름을 저장할 배열

			if (imageFiles != null && !imageFiles.isEmpty()) {
				File uploadDir = new File(FILE_UPLOAD_PATH);
				if (!uploadDir.exists()) {
					uploadDir.mkdirs();
				}

				int fileCount = 0;
				for (MultipartFile imageFile : imageFiles) {
					if (!imageFile.isEmpty() && fileCount < MAX_IMAGE_COUNT) {
						try {
							String originalFileName = imageFile.getOriginalFilename();
							String fileExtension = "";
							if (originalFileName != null && originalFileName.contains(".")) {
								fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
							}
							// 파일 확장자 제한 (예: jpg, png, gif만 허용)
							if (!isValidFileExtension(fileExtension)) {
								mav.setViewName("/error/fileUploadError");
								mav.addObject("msg", "허용되지 않는 파일 형식입니다.");
								return mav;
							}

							// --- 파일명 중복 방지 로직 (UUID 사용) ---
							String newFileName = UUID.randomUUID().toString() + fileExtension; // 고유한 파일명 생성
							// --- 파일명 중복 방지 로직 끝 ---

							File destFile = new File(FILE_UPLOAD_PATH + newFileName);
							imageFile.transferTo(destFile); // 파일 저장

							imgFiles[fileCount] = newFileName; // 배열에 파일 이름 저장

						} catch (IOException e) {
							e.printStackTrace();
							mav.setViewName("/error/fileUploadError");
							mav.addObject("msg", "파일 업로드 중 오류가 발생했습니다.");
							return mav;
						}
						fileCount++;
					}
				}
			}
			// BoardVo에 이미지 파일 이름 설정
			boardVo.setBoardImg1(imgFiles[IMG1]);
			boardVo.setBoardImg2(imgFiles[IMG2]);
			boardVo.setBoardImg3(imgFiles[IMG3]);
			boardVo.setBoardImg4(imgFiles[IMG4]);

			if (boardId == 0) { // 등록
				try {
					boardService.insertBoard(boardVo);
					int newBoardId = boardVo.getBoardId();
					mav.setViewName("redirect:/board/view?boardId=" + newBoardId);
				} catch (Exception e) {
					e.printStackTrace();
					mav.setViewName("/error/genericError");
					mav.addObject("msg", "게시글 등록 중 오류가 발생했습니다.");
				}
			} else { // 수정
				try {
					boardService.updateBoard(boardVo);
					mav.setViewName("redirect:/board/view?boardId=" + boardId);
				} catch (Exception e) {
					e.printStackTrace();
					mav.setViewName("/error/genericError");
					mav.addObject("msg", "게시글 수정 중 오류가 발생했습니다.");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName("/error/genericError");
			mav.addObject("msg", "게시글 처리 중 예상치 못한 오류가 발생했습니다.");
		}

		return mav;
	}

	// 허용된 파일 확장자인지 확인하는 메서드
	private boolean isValidFileExtension(String fileExtension) {
		String[] allowedExtensions = { ".jpg", ".jpeg", ".png", ".gif" };
		for (String ext : allowedExtensions) {
			if (fileExtension != null && fileExtension.equalsIgnoreCase(ext)) { // null 체크 추가
				return true;
			}
		}
		return false;
	}

}
