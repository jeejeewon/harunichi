package com.harunichi.board.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
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

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

@Slf4j
@Controller("boardController")
@RequestMapping("/board")
public class BoardControllerImpl implements BoardController {

	// private static final String IMAGE_REPO = "c:\\spring\\image_repo";

	// 에러페이지 만들기

	@Autowired
	private BoardService boardService;

	@Autowired
	private ServletContext servletContext; // ServletContext 주입

	@Override
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView boardList(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// List<BoardVo>
		List<BoardVo> boardVoList = boardService.selectBoardList();

		// ModelAndView 객체를 생성 뷰 이름("/board/list")을 지정
		ModelAndView mav = new ModelAndView("/board/list");

		// boardVoList)을 ModelAndView 객체에 담아 JSP로 전달
		// 이때, JSP에서 사용할 이름(key)을 "boardList"로 지정
		mav.addObject("boardList", boardVoList);

		// ModelAndView 객체를 반환
		return mav;
	}

	@RequestMapping(value = "/postForm", method = RequestMethod.GET)
	public String boardForm() throws Exception {
		return "/board/postForm";
	}

	// 게시글 등록
	@Override
	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public ModelAndView boardPost(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("imageFiles") List<MultipartFile> imageFiles) {
		ModelAndView mav = new ModelAndView();
		try {
			// 1. 파라미터 받기 (작성자, 내용 등)
			String boardWriter = request.getParameter("boardWriter");
			String boardCont = request.getParameter("boardCont");

			// 2. BoardVo 객체 생성 및 데이터 설정
			BoardVo boardVo = new BoardVo();
			boardVo.setBoardWriter(boardWriter);
			boardVo.setBoardCont(boardCont);

			// 3. 파일 업로드 처리 및 파일명 설정
			// 주입받은 servletContext 객체를 uploadFiles 메소드로 전달합니다.
			List<String> fileNames = uploadFiles(servletContext, imageFiles); // 주입받은 servletContext 전달
			if (fileNames != null && !fileNames.isEmpty()) {
				if (fileNames.size() > 0)
					boardVo.setBoardImg1(fileNames.get(0));
				if (fileNames.size() > 1)
					boardVo.setBoardImg2(fileNames.get(1));
				if (fileNames.size() > 2)
					boardVo.setBoardImg3(fileNames.get(2));
				if (fileNames.size() > 3)
					boardVo.setBoardImg4(fileNames.get(3));
			}

			// 4. BoardService를 통해 게시글 등록
			boardService.insertBoard(boardVo);

			// 5. 결과 설정 및 뷰 반환
			mav.setViewName("redirect:/board/list"); // 성공 시 게시글 목록으로 리다이렉트
		} catch (Exception e) {
			log.error("게시글 등록 중 오류 발생", e);
			mav.addObject("msg", "게시글 등록 중 오류가 발생했습니다.");
			mav.setViewName("errorPage"); // 실패 시 에러 페이지로 이동
		}

		return mav;
	}

	// 파일 업로드 처리 메소드 (ServletContext 파라미터 추가)
	private List<String> uploadFiles(ServletContext context, List<MultipartFile> imageFiles) {
		List<String> fileNames = new ArrayList<>();

		// 1. ServletContext 객체에서 웹 애플리케이션 루트의 실제 경로
		String realPath = context.getRealPath("/"); // 주입받은 context 사용

		// 2. 파일을 저장할 프로젝트 내부 폴더 경로를 정의합니다. (예: webapp/uploads/boardImages)
		// File.separator를 사용하여 OS에 맞는 경로 구분자를 사용합니다.
		String uploadPath = realPath + "resources" + File.separator + "images" + File.separator + "board";

		// 3. 이미지 저장 폴더(uploadPath)에 해당하는 File 객체를 생성합니다.
		File repoDir = new File(uploadPath);

		// 4. 해당 경로에 폴더가 존재하는지 확인합니다.
		if (!repoDir.exists()) {
			// 폴더가 존재하지 않으면 폴더를 생성합니다.
			log.info("이미지 저장 폴더가 존재하지 않습니다. 폴더를 생성합니다.");
			try {
				repoDir.mkdirs(); // 폴더 생성 시도
				log.info("이미지 저장 폴더 생성 성공: " + uploadPath);
			} catch (Exception e) {
				log.error("이미지 저장 폴더 생성 실패: " + uploadPath, e);
				// 폴더 생성 실패 시, 파일 업로드를 진행할 수 없으므로 빈 리스트를 반환하고 종료합니다.
				return fileNames;
			}
		} else {
			log.info("이미지 저장 폴더가 이미 존재합니다: " + uploadPath);
		}

		// imageFiles가 null이거나 비어있으면, 더 이상 진행하지 않고 빈 리스트를 반환합니다.
		if (imageFiles == null || imageFiles.isEmpty()) {
			log.info("업로드할 이미지가 없습니다.");
			return fileNames;
		}

		// 파일 업로드 로직
		for (MultipartFile imageFile : imageFiles) {
			// 파일이 비어있으면, 해당 파일은 건너뛰고 다음 파일로 진행합니다.
			if (imageFile.isEmpty()) {
				log.warn("첨부 파일이 비어 있습니다. 파일 업로드를 건너뜁니다.");
				continue; // 다음 파일로 진행
			}

			String originalFileName = imageFile.getOriginalFilename();
			String extension = "";
			int lastDotIndex = originalFileName.lastIndexOf(".");
			if (lastDotIndex > 0) {
				extension = originalFileName.substring(lastDotIndex);
			}

			String fileName = UUID.randomUUID().toString() + extension;
			File targetFile = new File(repoDir, fileName); // 저장할 파일 객체 생성

			try {
				// 파일 저장 시도
				imageFile.transferTo(targetFile); // 파일 저장
				fileNames.add(fileName); // 파일 이름 리스트에 추가
				log.info("파일 업로드 성공: " + originalFileName + " -> " + fileName);

			} catch (IOException e) {
				log.error("파일 업로드 실패: " + originalFileName, e);
				// 파일 업로드 실패 시, 예외를 기록하고 다음 파일로 계속 진행합니다.
			} catch (Exception e) { // 혹시 모를 다른 예외 처리
				log.error("예상치 못한 오류 발생: " + originalFileName, e);
			}
		}

		// 최종적으로 업로드된 파일 이름 리스트를 반환
		return fileNames;
	}

	

}
