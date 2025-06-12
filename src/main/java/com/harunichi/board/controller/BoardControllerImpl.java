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
	// 스마트에디터 적용

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

	// 게시글 등록 폼
	@Override
	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public ModelAndView boardPost(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("imageFiles") List<MultipartFile> imageFiles) {

		ModelAndView mav = new ModelAndView();

		try {
			// 1. 파라미터 받기 (작성자, 내용 등)
			String boardWriter = request.getParameter("boardWriter");
			String boardCont = request.getParameter("boardCont");
			String boardCate = request.getParameter("boardCate");

			// 2. BoardVo 객체 생성 및 데이터 설정
			BoardVo boardVo = new BoardVo();
			boardVo.setBoardWriter(boardWriter);
			boardVo.setBoardCate(boardCate);
			boardVo.setBoardCont(boardCont);

			// boardDate에 현재 시간 설정
			boardVo.setBoardDate(new Timestamp(System.currentTimeMillis())); // 현재 시간 설정

			// 3. 파일 업로드 처리 및 파일명 설정
			// 주입받은 servletContext 객체를 uploadFiles 메소드로 전달
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

	// 게시글 상세 요청 URL: /board/view?boardId=숫자
	@Override
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView viewBoard(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("boardId") int boardId) throws Exception {

		ModelAndView mav = new ModelAndView();

		log.info(">> BoardControllerImpl - viewBoard() 호출 시작, boardId: {}", boardId); // logback 사용 예시

		try {
			// BoardService의 getBoardById 메소드를 호출
			// 이 서비스 메소드 안에서 boardId에 해당하는 게시글의 조회수를 1 증가시키는 로직
			BoardVo board = boardService.getBoardById(boardId);

			// 가져온 게시글 정보(BoardVo 객체)가 null이 아닌지 확인
			if (board != null) {
				log.info(">> 게시글 정보 가져오기 성공! boardId: {}", board.getBoardId());
				// 게시글 정보가 있다면, 이 정보를 ModelAndView 객체에 담아서 View(JSP)로 넘겨줌
				// board라는 이름으로 담았으니, View에서는 ${board}로 접근됨
				mav.addObject("board", board);

				// 게시글 상세 정보를 보여줄 View의 이름을 설정
				mav.setViewName("/board/view");

			} else {

				log.warn(">> 해당 boardId의 게시글이 없습니다: {}", boardId);

				mav.addObject("msg", "죄송합니다. 요청하신 게시글을 찾을 수 없습니다.");
				// mav.setViewName("/board/errorPage");
				mav.setViewName("redirect:/board/list");
			}

		} catch (Exception e) {

			log.error(">> 게시글 조회 중 예외 발생, boardId: {}", boardId, e);

			mav.addObject("msg", "게시글을 불러오는 중 시스템 오류가 발생했습니다.");
			// mav.setViewName("/board/errorPage");
			mav.setViewName("redirect:/board/list");
		}

		return mav;
	}

	// 게시글 수정 폼 요청
	@RequestMapping(value = "/editForm", method = RequestMethod.GET)
	public ModelAndView editBoardForm(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("boardId") int boardId) throws Exception {
		ModelAndView mav = new ModelAndView("/board/editForm"); // 수정 폼을 보여줄 JSP 경로

		log.info(">> BoardControllerImpl - editBoardForm() 호출 시작, boardId: {}", boardId);

		try {
			// 조회수 증가 로직이 없는 서비스 메소드
			BoardVo boardVo = boardService.getBoardByIdWithoutIncrement(boardId);

			if (boardVo != null) {
				log.info(">> 게시글 정보 가져오기 성공! boardId: {}", boardVo.getBoardId());
				mav.addObject("board", boardVo); // 모델에 게시글 정보 추가
			} else {
				log.warn(">> 해당 boardId의 게시글이 없습니다: {}", boardId);
				mav.addObject("msg", "게시글을 찾을 수 없습니다.");
				// mav.setViewName("/board/errorPage");
				mav.setViewName("redirect:/board/list");
			}

		} catch (Exception e) {
			log.error(">> 게시글 수정 폼 로딩 중 예외 발생, boardId: {}", boardId, e);
			mav.addObject("msg", "게시글 수정 폼을 불러오는 중 시스템 오류가 발생했습니다.");
			// mav.setViewName("/board/errorPage");
			mav.setViewName("redirect:/board/list");
		}
		log.info(">> BoardControllerImpl - editBoardForm() 호출 종료");
		return mav;
	}

	// 파일 삭제 헬퍼 메소드 (ServiceImpl에서 이동)
	private void deleteFile(String fileName, ServletContext context) {
		if (fileName != null && !fileName.isEmpty()) {
			String uploadPath = context.getRealPath("/resources/images/board"); // 실제 파일 저장 경로 가져오기
			File fileToDelete = new File(uploadPath, fileName);
			if (fileToDelete.exists()) {
				if (fileToDelete.delete()) {
					log.info(">>파일 삭제 성공:{}", fileName);
				} else {
					log.warn(">>파일 삭제 실패:{}", fileName);
				}
			} else {
				log.warn(">>삭제하려는 파일이 존재하지 않습니다:{}", fileName);
			}
		} else {
			log.warn(">>파일 삭제 시도 실패: 파일명이 유효하지 않습니다.");
		}
	}

	// 게시글 수정
	@Override
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView updateBoard(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles, // 새로 업로드할 파일 리스트
			@RequestParam(value = "deleteIndices", required = false) List<Integer> deleteIndices // 삭제할 기존 이미지의 순서(1, 2,
																									// 3, 4) 리스트
	) {
		ModelAndView mav = new ModelAndView();
		log.info(">>BoardControllerImpl-updateBoard() 호출 시작");
		// 삭제 요청된 파일들의 실제 파일명을 저장할 리스트
		List<String> filesToDeleteFromSystem = new ArrayList<>();
		// 업로드되었으나 최종 목록에 포함되지 않은 파일 목록
		List<String> uploadedButNotAdded = new ArrayList<>();

		try {
			// 1. 파라미터 받기
			int boardId = Integer.parseInt(request.getParameter("boardId"));
			String boardCont = request.getParameter("boardCont");
			String boardCate = request.getParameter("boardCate");
			log.info(">>수정을 위한 boardId:{}", boardId);
			log.info(">>수정 내용:{}, 카테고리:{}", boardCont, boardCate);
			log.info(">>삭제 요청된 이미지 순서:{}", deleteIndices);

			// 2. 기존 게시글 정보 조회 (파일 이름 확인용)
			BoardVo existingBoard = boardService.getBoardByIdWithoutIncrement(boardId);
			if (existingBoard == null) {
				log.warn(">>수정하려는 게시글(ID:{})을 찾을 수 없습니다.", boardId);
				mav.addObject("msg", "수정하려는 게시글을 찾을 수 없습니다.");
				mav.setViewName("redirect:/board/list");
				return mav;
			}
			log.info(">>기존 게시글 정보 조회 완료.");

			// 3. 삭제 요청된 기존 이미지 처리
			// 삭제 요청이 들어온 이미지 순서에 해당하는 필드를 null로 설정하고 실제 삭제할 파일 목록에 추가
			if (deleteIndices != null && !deleteIndices.isEmpty()) {
				log.info(">>삭제 요청된 이미지 처리 시작");
				// 기존 이미지 파일명 리스트를 임시로 만듬
				List<String> currentImageNames = new ArrayList<>();
				currentImageNames.add(existingBoard.getBoardImg1()); // 인덱스 0 (순서 1)
				currentImageNames.add(existingBoard.getBoardImg2()); // 인덱스 1 (순서 2)
				currentImageNames.add(existingBoard.getBoardImg3()); // 인덱스 2 (순서 3)
				currentImageNames.add(existingBoard.getBoardImg4()); // 인덱스 3 (순서 4)

				for (Integer index : deleteIndices) {
					// 순서는 1부터 시작하므로 인덱스는 순서-1
					int listIndex = index - 1;
					if (listIndex >= 0 && listIndex < currentImageNames.size()) {
						String fileNameToDelete = currentImageNames.get(listIndex);
						if (fileNameToDelete != null && !fileNameToDelete.isEmpty()) {
							log.info(">>기존 이미지 {}(파일명:{}) 삭제 요청 감지. DB 필드를 null로 설정.", index, fileNameToDelete);
							// 해당 순서의 이미지 필드를 null로 설정 (DB 업데이트 시 반영)
							switch (index) {
							case 1:
								existingBoard.setBoardImg1(null);
								break;
							case 2:
								existingBoard.setBoardImg2(null);
								break;
							case 3:
								existingBoard.setBoardImg3(null);
								break;
							case 4:
								existingBoard.setBoardImg4(null);
								break;
							}
							// 실제 파일 시스템에서 삭제할 파일 목록에 추가
							filesToDeleteFromSystem.add(fileNameToDelete);
						} else {
							log.warn(">>삭제 요청된 순서 {}에 해당하는 기존 이미지가 없습니다.", index);
						}
					} else {
						log.warn(">>유효하지 않은 삭제 요청 순서:{}", index);
					}
				}
			} else {
				log.info(">>삭제 요청된 이미지가 없습니다. 기존 이미지를 유지합니다.");
			}

			// 4. 새로운 이미지 파일 업로드 처리
			List<String> newFileNames = new ArrayList<>();
			boolean isNewFileUploaded = (imageFiles != null && !imageFiles.isEmpty()
					&& imageFiles.get(0).getSize() > 0);
			if (isNewFileUploaded) {
				log.info(">>새로운 이미지 파일이 감지되었습니다.");
				newFileNames = uploadFiles(servletContext, imageFiles); // 새로운 파일 업로드 로직 호출
				log.info(">>업로드된 새 파일명:{}", newFileNames);
			} else {
				log.info(">>새로운 이미지 파일이 첨부되지 않았습니다.");
			}

			// 5. 최종 이미지 목록 구성 (삭제되지 않고 남은 기존 이미지 + 새로 업로드된 이미지 추가, 최대 4개 제한)
			List<String> finalImageNames = new ArrayList<>();
			// 삭제되지 않고 남은 기존 이미지 추가 (existingBoard 객체는 이미 삭제 요청된 필드가 null로 설정되어 있음)
			if (existingBoard.getBoardImg1() != null)
				finalImageNames.add(existingBoard.getBoardImg1());
			if (existingBoard.getBoardImg2() != null)
				finalImageNames.add(existingBoard.getBoardImg2());
			if (existingBoard.getBoardImg3() != null)
				finalImageNames.add(existingBoard.getBoardImg3());
			if (existingBoard.getBoardImg4() != null)
				finalImageNames.add(existingBoard.getBoardImg4());
			log.info(">>삭제 처리 후 남은 기존 이미지:{}", finalImageNames);

			// 새로 업로드된 이미지 추가 (최대 4개까지)
			if (newFileNames != null) {
				for (String newName : newFileNames) {
					if (finalImageNames.size() < 4) { // 최대 개수(4개) 확인
						finalImageNames.add(newName);
						log.info(">>새 이미지 '{}'를 최종 목록에 추가합니다.", newName);
					} else {
						log.warn(">>이미지 최대 개수(4개) 초과로 새로운 파일 '{}'은 추가되지 않았습니다.", newName);
						uploadedButNotAdded.add(newName); // 추가되지 못한 파일 목록에 추가
					}
				}
			}
			log.info(">>최종 이미지 목록:{}", finalImageNames);
			log.info(">>업로드되었으나 추가되지 못한 파일 목록:{}", uploadedButNotAdded);

			// 6. BoardVo 객체에 최종 데이터 설정
			// existingBoard 객체에 수정 내용과 최종 이미지 목록을 설정
			existingBoard.setBoardCont(boardCont);
			existingBoard.setBoardCate(boardCate);
			existingBoard.setBoardModDate(new Timestamp(System.currentTimeMillis())); // 수정 시간 설정

			// 이미지 필드를 모두 초기화하고 최종 목록으로 다시 설정함
			existingBoard.setBoardImg1(null);
			existingBoard.setBoardImg2(null);
			existingBoard.setBoardImg3(null);
			existingBoard.setBoardImg4(null);

			if (finalImageNames.size() > 0)
				existingBoard.setBoardImg1(finalImageNames.get(0));
			if (finalImageNames.size() > 1)
				existingBoard.setBoardImg2(finalImageNames.get(1));
			if (finalImageNames.size() > 2)
				existingBoard.setBoardImg3(finalImageNames.get(2));
			if (finalImageNames.size() > 3)
				existingBoard.setBoardImg4(finalImageNames.get(3));

			// 7. BoardService를 통해 게시글 업데이트 (DB 저장)
			log.info(">>게시글 DB 업데이트를 위해 Service 호출");
			boardService.updateBoard(existingBoard);
			log.info(">>BoardService.updateBoard() 호출 완료");

			// 8. DB 업데이트 성공 시 실제 파일 시스템에서 파일 삭제
			log.info(">>DB 업데이트 성공. 실제 파일 시스템 삭제 시도.");
			// 삭제 요청된 기존 파일 삭제
			if (!filesToDeleteFromSystem.isEmpty()) {
				log.info(">>삭제 요청된 기존 파일 {}개 삭제 시도.", filesToDeleteFromSystem.size());
				for (String fileName : filesToDeleteFromSystem) {
					try {
						// deleteFile 헬퍼 메소드 호출 시 파라미터 순서 수정
						deleteFile(fileName, servletContext);
					} catch (Exception e) {
						log.error("기존 이미지 파일 삭제 중 오류 발생 (DB 업데이트는 완료됨): {}", fileName, e);
						// 파일 삭제 실패는 로그만 남기고 계속 진행
					}
				}
				log.info(">>삭제 요청된 기존 파일 삭제 시도 완료.");
			} else {
				log.info(">>삭제 요청된 기존 파일이 없습니다.");
			}

			// 업로드되었지만 최종 목록에 포함되지 않은 파일 삭제 (최대 개수 초과 등)
			if (!uploadedButNotAdded.isEmpty()) {
				log.info(">>업로드되었으나 사용되지 않은 파일 {}개 삭제 시도.", uploadedButNotAdded.size());
				for (String fileName : uploadedButNotAdded) {
					try {
						// deleteFile 헬퍼 메소드 호출 시 파라미터 순서 수정
						deleteFile(fileName, servletContext);
					} catch (Exception e) {
						log.error("업로드되었으나 사용되지 않은 파일 삭제 중 오류 발생: {}", fileName, e);
						// 파일 삭제 실패는 로그만 남기고 계속 진행
					}
				}
				log.info(">>업로드되었으나 사용되지 않은 파일 삭제 시도 완료.");
			} else {
				log.info(">>업로드되었으나 사용되지 않은 파일이 없습니다.");
			}

			log.info(">>파일 시스템 삭제 시도 완료.");

			// 9. 결과 설정 및 뷰 반환
			log.info(">>게시글 수정 성공, 상세 페이지로 리다이렉트:/board/view?boardId={}", boardId);
			mav.setViewName("redirect:/board/view?boardId=" + boardId); // 수정 성공 시 해당 게시글 상세 페이지로 리다이렉트

		} catch (NumberFormatException e) { // boardId 파라미터가 숫자가 아닐 경우 발생
			log.error(">>게시글 ID 파싱 오류 발생:{}", request.getParameter("boardId"), e);
			mav.addObject("msg", "잘못된 게시글 정보입니다.");
			// mav.setViewName("/board/errorPage"); // 400 Bad Request 에러 페이지
			mav.setViewName("redirect:/board/list"); // 목록으로 리다이렉트
		} catch (Exception e) {
			log.error(">>게시글 수정 처리 중 예외 발생, boardId:{}", request.getParameter("boardId"), e);
			mav.addObject("msg", "게시글 수정 중 오류가 발생했습니다.");
			// mav.setViewName("/board/errorPage"); // 500 Internal Server Error 에러 페이지
			mav.setViewName("redirect:/board/list"); // 목록으로 리다이렉트
		}
		log.info(">>BoardControllerImpl-updateBoard() 호출 종료");
		return mav;
	}

	// 파일 업로드 처리 메소드 (ServletContext 파라미터 추가)
	private List<String> uploadFiles(ServletContext context, List<MultipartFile> imageFiles) throws Exception {
		List<String> fileNames = new ArrayList<>();
		// 1. ServletContext 객체에서 웹 애플리케이션 루트의 실제 경로
		String realPath = context.getRealPath("/"); // 주입받은 context 사용
		// 2. 파일을 저장할 프로젝트 내부 폴더 경로를 정의 (예: webapp/resources/images/board)
		// File.separator를 사용하여 OS에 맞는 경로 구분자를 사용
		String uploadPath = realPath + "resources" + File.separator + "images" + File.separator + "board";
		// 3. 이미지 저장 폴더(uploadPath)에 해당하는 File 객체를 생성
		File repoDir = new File(uploadPath);

		// 4. 해당 경로에 폴더가 존재하는지 확인
		if (!repoDir.exists()) {
			log.info("이미지 저장 폴더가 존재하지 않습니다. 폴더를 생성합니다.");
			try {
				repoDir.mkdirs(); // 폴더 생성 시도
				log.info("이미지 저장 폴더 생성 성공: " + uploadPath);
			} catch (Exception e) {
				log.error("이미지 저장 폴더 생성 실패: " + uploadPath, e);
				return fileNames;
			}
		} else {
			log.info("이미지 저장 폴더가 이미 존재합니다: " + uploadPath);
		}

		// imageFiles가 null이거나 비어있으면 더 이상 진행하지 않고 빈 리스트를 반환
		if (imageFiles == null || imageFiles.isEmpty()) {
			log.info("업로드할 이미지가 없습니다.");
			return fileNames;
		}

		// 파일 업로드 로직
		for (MultipartFile imageFile : imageFiles) {
			if (imageFile.isEmpty()) {
				log.warn("첨부 파일이 비어있습니다. 파일 업로드를 건너뜁니다.");
				continue; // 다음 파일로 진행
			}

			// 파일 타입 검사 (이미지 파일만 허용)
			String contentType = imageFile.getContentType();
			if (contentType == null || !contentType.startsWith("image/")) {
				log.warn("유효하지 않은 파일 형식: {}", imageFile.getOriginalFilename());
				continue;
			}

			String originalFileName = imageFile.getOriginalFilename();
			String extension = "";
			int lastDotIndex = originalFileName.lastIndexOf(".");
			if (lastDotIndex > 0) {
				extension = originalFileName.substring(lastDotIndex);
			}
			// 파일명 중복 방지를 위해 UUID 사용
			String fileName = UUID.randomUUID().toString() + extension;
			File targetFile = new File(repoDir, fileName); // 저장할 파일 객체 생성

			try {
				// 파일 저장 시도
				imageFile.transferTo(targetFile); // 파일 저장
				fileNames.add(fileName); // 파일 이름 리스트에 추가
				log.info("파일 업로드 성공: {} -> {}", originalFileName, fileName);
			} catch (IOException e) {
				log.error("파일 업로드 실패: {}", originalFileName, e);
			} catch (Exception e) {
				log.error("예상치 못한 오류 발생: {}", originalFileName, e);
			}
		}
		// 최종적으로 업로드된 파일 이름 리스트를 반환
		return fileNames;
	}

	// 게시글 삭제 처리
	@Override
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ModelAndView deleteBoard(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("boardId") int boardId) throws Exception {

		log.info(">>BoardControllerImpl-deleteBoard() 호출 시작, boardId:{}", boardId);

		BoardVo boardToDelete = null;
		int dbDeleteResult = 0; // DB 삭제 결과
		ModelAndView mav = new ModelAndView(); // ModelAndView 객체 미리 생성

		try {
			// 1. Service를 통해 삭제할 게시글 정보 (파일 이름 포함) 조회
			// ServiceImpl의 getBoardByIdWithoutIncrement 메소드를 호출합니다.
			log.info(">>게시글 정보 조회를 위해 Service 호출 (getBoardByIdWithoutIncrement), boardId:{}", boardId);
			boardToDelete = boardService.getBoardByIdWithoutIncrement(boardId);

			if (boardToDelete == null) {
				log.warn(">>삭제할 게시글(ID:{})을 찾을 수 없습니다. 삭제를 중단합니다.", boardId);
				// 게시글이 없으면 목록 페이지로 리다이렉트 또는 에러 처리
				mav.setViewName("redirect:/board/list");
				mav.addObject("msg", "notfound");
				return mav;
			}

			// 2. Service를 통해 데이터베이스에서 게시글 및 댓글 데이터 삭제
			// ServiceImpl의 deleteBoardData 메소드를 호출합니다.
			log.info(">>데이터베이스 삭제를 위해 Service 호출 (deleteBoardData), boardId:{}", boardId);
			dbDeleteResult = boardService.deleteBoardData(boardId);

			// 3. 데이터베이스 삭제 성공 시 실제 파일 시스템에서 이미지 파일 삭제
			if (dbDeleteResult > 0) { // 게시글 레코드가 1개 이상 삭제되었다면 (DB 삭제 성공)
				log.info(">>데이터베이스 삭제 성공 ({} 행 삭제). 파일 삭제 시도.", dbDeleteResult);
				// 파일 삭제는 DB 트랜잭션과 별개로 Controller에서 처리
				try {
					deleteFile(boardToDelete.getBoardImg1(), servletContext);
					deleteFile(boardToDelete.getBoardImg2(), servletContext);
					deleteFile(boardToDelete.getBoardImg3(), servletContext);
					deleteFile(boardToDelete.getBoardImg4(), servletContext);
					log.info(">>파일 삭제 시도 완료.");
				} catch (Exception e) {
					log.error(">>이미지 파일 삭제 중 예외 발생 (DB 삭제는 완료됨), boardId:{}", boardId, e);
				}
			} else {
				log.warn(">>데이터베이스 삭제 실패 (삭제된 행 수 0), boardId:{}", boardId);
				// DB 삭제 실패 시 처리 (예: 에러 페이지 또는 메시지)
				mav.setViewName("redirect:/board/list");
				mav.addObject("msg", "db_delete_failed");
				return mav;
			}

		} catch (Exception e) {
			log.error(">>게시글 삭제 처리 중 예외 발생, boardId:{}", boardId, e);
			// Service에서 발생한 예외 (DB 관련)는 여기서 잡아서 처리
			// 예: 에러 페이지로 이동 또는 메시지 표시
			mav.setViewName("redirect:/board/list");
			mav.addObject("msg", "error");
			return mav;
		}

		log.info(">>BoardControllerImpl-deleteBoard() 호출 종료, DB 삭제된 행 수:{}", dbDeleteResult);

		// 삭제 성공 시 게시글 목록 페이지로 리다이렉트
		mav.setViewName("redirect:/board/list");
		mav.addObject("msg", "deleted");
		return mav;
	}

}
