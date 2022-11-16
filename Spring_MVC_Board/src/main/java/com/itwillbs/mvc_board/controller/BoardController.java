package com.itwillbs.mvc_board.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.mvc_board.service.BoardService;
import com.itwillbs.mvc_board.vo.BoardVO;
import com.itwillbs.mvc_board.vo.PageInfo;

@Controller
public class BoardController {
	@Autowired
	private BoardService service;
	
	// 글 쓰기 폼 - GET
	@GetMapping(value = "/BoardWriteForm.bo")
	public String write() {
		return "board/qna_board_write";
	}
	
	// "/BoardWritePro.bo" 서블릿 요청에 대해 글쓰기 작업 수행할 writePro() - POST
//	@PostMapping(value = "/BoardWritePro.bo")
//	public String writePro(@ModelAttribute BoardVO board, Model model) {
//		int insertCount = service.registBoard(board);
//		
//		if(insertCount > 0) {
//			return "redirect:/BoardList.bo";
//		} else {
//			model.addAttribute("msg", "글 쓰기 실패!");
//			return "member/fail_back";
//		}
//		
//	}
	
	// "/BoardWritePro.bo" 서블릿 요청에 대해 글쓰기 작업 수행할 writePro() - POST
	// => 파일 업로드 기능 추가
	@PostMapping(value = "/BoardWritePro.bo")
	public String writePro(@ModelAttribute BoardVO board, Model model, HttpSession session) {
		// 주의! 파일 업로드 기능을 통해 전달받은 파일 객체를 다루기 위해서는
		// BoardVO 클래스 내에 MultipartFile 타입 변수와 Getter/Setter 정의 필수!
		// => input type="file" 태그의 name 속성과 동일한 변수명 사용해야함
//		System.out.println(board.getFile());
		
		// 가상 업로드 경로에 대한 실제 업로드 경로 알아내기
		// => 단, request 객체에 getServletContext() 메서드 대신, session 객체로 동일한 작업 수행
		//    (request 객체에 해당 메서드 없음)
		String uploadDir = "/resources/upload"; // 가상의 업로드 경로
		// => webapp/resources 폴더 내에 upload 폴더 생성 필요
		String saveDir = session.getServletContext().getRealPath(uploadDir);
		System.out.println("실제 업로드 경로 : " + saveDir);
		
		File f = new File(saveDir); // 실제 경로를 갖는 File 객체 생성
		// 만약, 해당 경로 상에 디렉토리(폴더)가 존재하지 않을 경우 생성
		if(!f.exists()) { // 해당 경로가 존재하지 않을 경우
			// 경로 상의 존재하지 않는 모든 경로 생성
			f.mkdirs();
		}
		
		// BoardVO 객체에 전달된 MultipartFile 객체 꺼내기
		MultipartFile mFile = board.getFile();
		
		String originalFileName = mFile.getOriginalFilename();
		long fileSize = mFile.getSize();
		System.out.println("파일명 : " + originalFileName);
		System.out.println("파일크기 : " + fileSize + " Byte");
		
		// 파일명 중복 방지를 위한 대책
		// 시스템에서 랜덤ID 값을 추출하여 파일명 앞에 붙여 "랜덤ID값_파일명" 형식으로 설정
		// 랜덤ID 는 UUID 클래스 활용(UUID : 범용 고유 식별자)
		String uuid = UUID.randomUUID().toString();
		System.out.println("업로드 될 파일명 : " + uuid + "_" + originalFileName);
		
		// BoardVO 객체에 원본 파일명과 업로드 될 파일명 저장
		// => 단, uuid 를 결합한 파일명을 사용할 경우 원본 파일명과 실제 파일명을 구분할 필요 없이
		//    하나의 컬럼에 저장해두고, 원본 파일명이 필요할 경우 "_" 를 구분자로 지정하여
		//    문자열을 분리하면 두번째 파라미터가 원본 파일명이 된다!
		board.setBoard_file(originalFileName); // 실제로는 불필요한 컬럼
		board.setBoard_real_file(uuid + "_" + originalFileName);
		
		int insertCount = service.registBoard(board);
		
		if(insertCount > 0) {
			// 파일 등록 작업 성공 시 실제 폴더 위치에 파일 업로드 수행
			// => MultipartFile 객체의 transferTo() 메서드를 호출하여 파일 업로드 작업 수행
			//    (파라미터 : new File(업로드 경로, 업로드 할 파일명))
			try {
				mFile.transferTo(new File(saveDir, board.getBoard_real_file()));
			} catch (IllegalStateException e) {
				System.out.println("IllegalStateException");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("IOException");
				e.printStackTrace();
			}
			
			return "redirect:/BoardList.bo";
		} else {
			model.addAttribute("msg", "글 쓰기 실패!");
			return "member/fail_back";
		}
		
	}

	// "/BoardList.bo" 서블릿 요청에 대해 글 목록 조회 list() - GET
	// => 파라미터 : 현재 페이지번호(pageNum) => 단, 기본값 1로 설정
	//               데이터 저장할 Model 객체(model)
	// => List<BoardVO> 객체 저장한 후 board/qna_board_list.jsp 페이지로 포워딩(Dispatch)
//	@GetMapping(value = "/BoardList.bo")
//	public String list(@RequestParam(defaultValue = "1") int pageNum, Model model) {
//		// -------------------------------------------------------------------
//		// 페이징 처리를 위한 계산 작업
//		int listLimit = 10; // 한 페이지 당 표시할 게시물 목록 갯수 
//		int pageListLimit = 10; // 한 페이지 당 표시할 페이지 목록 갯수
//		
//		// 조회 시작 게시물 번호(행 번호) 계산
//		int startRow = (pageNum - 1) * listLimit;
//
//		// Service 객체의 getBoardList() 메서드를 호출하여 게시물 목록 조회
//		// => 파라미터 : 시작행번호, 페이지 당 목록 갯수
//		// => 리턴타입 : List<BoardVO>(boardList)
//		List<BoardVO> boardList = service.getBoardList(startRow, listLimit);
//		// -------------------------------------------
//		// Service 객체의 getBoardListCount() 메서드를 호출하여 전체 게시물 목록 갯수 조회
//		// => 파라미터 : 없음, 리턴타입 : int(listCount)
//		int listCount = service.getBoardListCount();
//		
//		// 페이지 계산 작업 수행
//		// 전체 페이지 수 계산
//		// Math 클래스의 ceil() 메서드를 활용하여 소수점 올림 처리를 통해 전체 페이지 수 계산
//		// => listCount / listLimit 를 실수 연산으로 수행하여 소수점까지 계산하고
//		//    Math.ceil() 메서드를 통해 올림 처리 후 결과값을 정수로 변환
//		int maxPage = (int)Math.ceil((double)listCount / listLimit);
//		
//		// 시작 페이지 번호 계산
//		int startPage = (pageNum - 1) / pageListLimit * pageListLimit + 1;
//		
//		// 끝 페이지 번호 계산
//		int endPage = startPage + pageListLimit - 1;
//		
//		// 만약, 끝 페이지 번호(endPage)가 최대 페이지 번호(maxPage)보다 클 경우 
//		// 끝 페이지 번호를 최대 페이지 번호로 교체
//		if(endPage > maxPage) {
//			endPage = maxPage;
//		}
//		
//		// 페이징 처리 정보를 저장하는 PageInfo 클래스 인스턴스 생성 및 데이터 저장
//		PageInfo pageInfo = new PageInfo(
//				pageNum, listLimit, listCount, pageListLimit, maxPage, startPage, endPage);
////		System.out.println(pageInfo);
//		// --------------------------------------------------------------------------------
//		// 게시물 목록(boardList) 과 페이징 처리 정보(pageInfo)를 Model 객체에 저장
//		model.addAttribute("boardList", boardList);
//		model.addAttribute("pageInfo", pageInfo);
//		
//		return "board/qna_board_list";
//	}
	
	
	// "/BoardList.bo" 서블릿 요청에 대해 글 목록 조회 list() - GET
	// => 파라미터 : 검색타입(searchType) => 기본값 널스트링
	//				 검색어(keyword) => 기본값 널스트링
	//				 현재 페이지번호(pageNum) => 단, 기본값 1로 설정
	//               데이터 저장할 Model 객체(model)
	// => List<BoardVO> 객체 저장한 후 board/qna_board_list.jsp 페이지로 포워딩(Dispatch)
	@GetMapping(value = "/BoardList.bo")
	public String list(
			@RequestParam(defaultValue = "") String searchType, 
			@RequestParam(defaultValue = "") String keyword, 
			@RequestParam(defaultValue = "1") int pageNum, Model model) {
		System.out.println("searchType : " + searchType);
		System.out.println("keyword : " + keyword);
		// -------------------------------------------------------------------
		// 페이징 처리를 위한 계산 작업
		int listLimit = 10; // 한 페이지 당 표시할 게시물 목록 갯수 
		int pageListLimit = 10; // 한 페이지 당 표시할 페이지 목록 갯수
		
		// 조회 시작 게시물 번호(행 번호) 계산
		int startRow = (pageNum - 1) * listLimit;
		
		// Service 객체의 getBoardList() 메서드를 호출하여 게시물 목록 조회
		// => 파라미터 : 시작행번호, 페이지 당 목록 갯수
		// => 리턴타입 : List<BoardVO>(boardList)
		List<BoardVO> boardList = service.getBoardList(startRow, listLimit, searchType, keyword);
		// -------------------------------------------
		// Service 객체의 getBoardListCount() 메서드를 호출하여 전체 게시물 목록 갯수 조회
		// => 파라미터 : 없음, 리턴타입 : int(listCount)
		int listCount = service.getBoardListCount(searchType, keyword);
		
		// 페이지 계산 작업 수행
		// 전체 페이지 수 계산
		// Math 클래스의 ceil() 메서드를 활용하여 소수점 올림 처리를 통해 전체 페이지 수 계산
		// => listCount / listLimit 를 실수 연산으로 수행하여 소수점까지 계산하고
		//    Math.ceil() 메서드를 통해 올림 처리 후 결과값을 정수로 변환
		int maxPage = (int)Math.ceil((double)listCount / listLimit);
		
		// 시작 페이지 번호 계산
		int startPage = (pageNum - 1) / pageListLimit * pageListLimit + 1;
		
		// 끝 페이지 번호 계산
		int endPage = startPage + pageListLimit - 1;
		
		// 만약, 끝 페이지 번호(endPage)가 최대 페이지 번호(maxPage)보다 클 경우 
		// 끝 페이지 번호를 최대 페이지 번호로 교체
		if(endPage > maxPage) {
			endPage = maxPage;
		}
		
		// 페이징 처리 정보를 저장하는 PageInfo 클래스 인스턴스 생성 및 데이터 저장
		PageInfo pageInfo = new PageInfo(
				pageNum, listLimit, listCount, pageListLimit, maxPage, startPage, endPage);
//		System.out.println(pageInfo);
		// --------------------------------------------------------------------------------
		// 게시물 목록(boardList) 과 페이징 처리 정보(pageInfo)를 Model 객체에 저장
		model.addAttribute("boardList", boardList);
		model.addAttribute("pageInfo", pageInfo);
		
		return "board/qna_board_list";
	}
	
	// "BoardDetail.bo" 서블릿 요청에 대한 글 상세내용 조회 작업 수행 - GET
	@GetMapping(value = "/BoardDetail.bo")
	public String detail(@RequestParam int board_num, Model model) {
		// Service 객체의 increaseReadcount() 메서드 호출하여 게시물 조회 증가
		// => 파라미터 : 글번호, 리턴타입 : void
		service.increaseReadcount(board_num);
		
		// Service 객체의 getBoard() 메서드를 호출하여 게시물 상세 정보 조회
		// => 파라미터 : 글번호, 리턴타입 : BoardVO(board)
		BoardVO board = service.getBoard(board_num);
		
		// Model 객체에 BoardVO 객체 추가
		model.addAttribute("board", board);
		
		return "board/qna_board_view";
	}
	
	// "BoardDeleteForm.bo" 서블릿 요청에 대한 글 삭제 폼 - GET
	@GetMapping(value = "/BoardDeleteForm.bo")
	public String delete() {
		return "board/qna_board_delete";
	}
	
	// "BoardDeletePro.bo" 서블릿 요청에 대한 글 삭제 - POST
	@PostMapping(value = "/BoardDeletePro.bo")
	public String deletePro(@ModelAttribute BoardVO board, @RequestParam int pageNum, Model model, HttpSession session) {
		// Service - getRealFile() 메서드를 호출하여 삭제 전 실제 업로드 된 파일명 조회 작업 요청
		// => 파라미터 : 글번호, 리턴타입 : String(realFile)
		String realFile = service.getRealFile(board.getBoard_num());
//		System.out.println(realFile);
		
		// Service - removeBoard() 메서드 호출하여 삭제 작업 요청
		// => 파라미터 : BoardVO 객체, 리턴타입 : int(deleteCount)
		int deleteCount = service.removeBoard(board);
		
		// 삭제 실패 시 "패스워드 틀림!" 메세지 저장 후 fail_back.jsp 페이지로 포워딩
		// 아니면, BoardList.bo 서블릿 요청(페이지번호 전달)
		if(deleteCount == 0) {
			model.addAttribute("msg", "패스워드 틀림!");
			return "member/fail_back";
		} else { // 삭제 성공 시
			// File 객체의 delete() 메서드를 활용하여 실제 업로드 된 파일 삭제
			String uploadDir = "/resources/upload"; // 가상의 업로드 경로
			// => webapp/resources 폴더 내에 upload 폴더 생성 필요
			String saveDir = session.getServletContext().getRealPath(uploadDir);
			System.out.println("실제 업로드 경로 : " + saveDir);
			
			File f = new File(saveDir, realFile); // 실제 경로와 실제 파일명을 갖는 File 객체 생성
			// 만약, 해당 경로 상에 파일이 존재할 경우 삭제
			if(f.exists()) { // 해당 경로에 파일이 존재할 경우
				f.delete();
			}
			
			return "redirect:/BoardList.bo?pageNum=" + pageNum;
		}
		
	}

	// "BoardModifyForm.bo" 서블릿 요청에 대한 글 수정 폼 - GET
	@GetMapping(value = "/BoardModifyForm.bo")
	public String modify(@RequestParam int board_num, Model model) {
		BoardVO board = service.getBoard(board_num);
		
		model.addAttribute("board", board);
		
		return "board/qna_board_modify";
	}
	
	// "BoardModifyPro.bo" 서블릿 요청에 대한 글 수정 - POST
	@PostMapping(value = "/BoardModifyPro.bo")
	public String modifyPro(@ModelAttribute BoardVO board, @RequestParam int pageNum, Model model) {
		// Service - modifyBoard() 메서드 호출하여 수정 작업 요청
		// => 파라미터 : BoardVO 객체, 리턴타입 : int(updateCount)
		int updateCount = service.modifyBoard(board);
		
		// 수정 실패 시 "패스워드 틀림!" 메세지 저장 후 fail_back.jsp 페이지로 포워딩
		// 아니면, BoardDetail.bo 서블릿 요청(글번호, 페이지번호 전달)
		if(updateCount == 0) {
			model.addAttribute("msg", "패스워드 틀림!");
			return "member/fail_back";
		}
		
		return "redirect:/BoardDetail.bo?board_num=" + board.getBoard_num() + "&pageNum=" + pageNum;
	}
	
	// "/BoardReplyForm.bo" 서블릿 요청에 대한 답글 폼 - GET
	// Service - getBoard() 메서드 재사용
	@GetMapping(value = "/BoardReplyForm.bo")
	public String reply(@RequestParam int board_num, Model model) {
		BoardVO board = service.getBoard(board_num);
		
		if(board != null) {
			model.addAttribute("board", board);
			return "board/qna_board_reply";
		} else {
			model.addAttribute("msg", "조회 실패");
			return "member/fail_back";
		}
	}
	
	// "/BoardReplyPro.bo" 서블릿 요청에 대한 답글 작성 요청 - POST
	@PostMapping(value = "/BoardReplyPro.bo")
	public String replyPro(@ModelAttribute BoardVO board, int pageNum, Model model) {
		// Service - increaseBoardReSeq() 메서드 호출하여 순서번호(board_re_seq) 조정 요청
		// => 파라미터 : BoardVO 객체   리턴타입 : void
		service.increaseBoardReSeq(board);
		
		// Service - registReplyBoard() 메서드 호출하여 답글 등록 요청
		// => 파라미터 : BoardVO 객체   리턴타입 : int(insertCount)
		int insertCount = service.registReplyBoard(board);
		
		if(insertCount > 0) {
			return "redirect:/BoardList.bo";
		} else {
			model.addAttribute("msg", "답글 쓰기 실패!");
			return "member/fail_back";
		}
	}
	
}












