package com.itwillbs.mvc_board.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.mvc_board.service.BoardService;
import com.itwillbs.mvc_board.vo.BoardVO;
import com.itwillbs.mvc_board.vo.PageInfo;

@Controller
public class BoardController {
	
	
	@Autowired
	private BoardService service;
	
	@GetMapping(value = "/BoardWriteForm.bo")
	public String boardWrite() {
		
		return "board/qna_board_write";
	}
	
//	 "/BoardWritePro.bo" 서블릿 요청에 대해 글쓰기 작업 수행할 writePro()
//	 => Service - getMaxNum() => Mapper - selectMaxNum()
//	 => Service - registBoard()
//	 => Mapper - insertBoard()
//	 => XML - INSERT 작업을 사용하여 게시물 등록 후 리턴되는 int 값 처리
//	 => 최종적으로 Controller 에서 글쓰기 성공 시 qna_board_list.jsp 로 리다이렉트
	@PostMapping(value = "/BoardWritePro.bo")
	public String writePro(Model model,@ModelAttribute BoardVO board) {
		int insertCount = service.registBoard(board);
		
		if(insertCount > 0) {
			return "redirect:/BoardList.bo";
		} else {
			model.addAttribute("msg", "가입 실패!");
			return "member/fail_back";
		}
		
	}
	
//	//"/BoardList.bo" 서블릿 요청에 대해 글 목록 조회
//	// => List<BoardVO> 객체 저장한 후 board/qna_board_list.jsp 페이지로 포워딩(Dispatch)
//	@GetMapping(value = "/BoardList.bo")
//	public String list(@RequestParam(defaultValue = "1") int pageNum, Model model,HttpSession session) {
//		
//		// 페이징 처리를 위한 계산 작업
//		int listLimit = 10; // 한 페이지 당 표시할 게시물 목록 갯수 
//		int pageListLimit = 10; // 한 페이지 당 표시할 페이지 목록 갯수
//		
//		
//		// 조회 시작 게시물 번호(행 번호) 계산
//		int startRow = (pageNum - 1) * listLimit;
//		
//		// Service 객체의 getBoardList() 메서드를 호출하여 게시물 목록 조회
//		// => 파라미터 : 시작행번호, 페이지 당 목록 갯수
//		// => 리턴타입 : List<BoardVO>(boardList)
//		List<BoardVO> boardList = service.getBoardList(startRow,listLimit);
//		
//		
//		// Service 객체의 getBoardListCount() 메서드를 호출하여 전체 게시물 목록 갯수 조회
//		int listCount = service.getBoardListCount();
//		
//		
//		// 페이지 계산 작업 수행
//		// 전체 페이지 수 계산
////		int maxPage = listCount / listLimit;
////		
////		if(listCount % listLimit > 0) {
////			maxPage++;
////		}
//		
//		// 삼항연산자(조건연사자)를 활용하여 전체 페이지 수 계산
//		// => 총 게시물 수를 페이지 당 게시물 수로 나눈 결과값에 
//		//    남은 게시물 존재 시(나눈 나머지가 0보다 크면) 페이지 수 + 1, 아니면 + 0
////		int maxPage = listCount / listLimit + (listCount % listLimit > 0 ? 1 : 0);
//		
//		// Math 클래스의 ceil() 메서드를 활용하여 소수점 올림 처리를 통해 전체 페이지 수 계산
//		// =>listCount / listLimit 를 실수 연산으로 수행하여 소수점까지 계산하고
//		// Math.ceil() 메서드를 통해 올림 처리 후 결과값을 정수로 변환
//		int maxPage = (int)Math.ceil((double)listCount / listLimit);
//		
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
//		// --------------------------------------------------------------------------------
////		System.out.println(pageInfo.toString());
//		// 게시물 목록(boardList) 과 페이징 처리 정보(pageInfo) 를 Model 객체에 저장
//		model.addAttribute("boardList", boardList);
//		model.addAttribute("pageInfo", pageInfo);
//		
//			return "board/qna_board_list";
//	}
	
	// ===============> 검색 기능 추가 구현예정
	
	
	//"/BoardList.bo" 서블릿 요청에 대해 글 목록 조회
		// => List<BoardVO> 객체 저장한 후 board/qna_board_list.jsp 페이지로 포워딩(Dispatch)
		@GetMapping(value = "/BoardList.bo")
		public String list(@RequestParam(defaultValue = "1") int pageNum, Model model,HttpSession session) {
			
			// 페이징 처리를 위한 계산 작업
			int listLimit = 10; // 한 페이지 당 표시할 게시물 목록 갯수 
			int pageListLimit = 10; // 한 페이지 당 표시할 페이지 목록 갯수
			
			
			// 조회 시작 게시물 번호(행 번호) 계산
			int startRow = (pageNum - 1) * listLimit;
			
			// Service 객체의 getBoardList() 메서드를 호출하여 게시물 목록 조회
			// => 파라미터 : 시작행번호, 페이지 당 목록 갯수
			// => 리턴타입 : List<BoardVO>(boardList)
			List<BoardVO> boardList = service.getBoardList(startRow,listLimit);
			
			
			// Service 객체의 getBoardListCount() 메서드를 호출하여 전체 게시물 목록 갯수 조회
			int listCount = service.getBoardListCount();
			
			
			// 페이지 계산 작업 수행
			// 전체 페이지 수 계산
//			int maxPage = listCount / listLimit;
//			
//			if(listCount % listLimit > 0) {
//				maxPage++;
//			}
			
			// 삼항연산자(조건연사자)를 활용하여 전체 페이지 수 계산
			// => 총 게시물 수를 페이지 당 게시물 수로 나눈 결과값에 
			//    남은 게시물 존재 시(나눈 나머지가 0보다 크면) 페이지 수 + 1, 아니면 + 0
//			int maxPage = listCount / listLimit + (listCount % listLimit > 0 ? 1 : 0);
			
			// Math 클래스의 ceil() 메서드를 활용하여 소수점 올림 처리를 통해 전체 페이지 수 계산
			// =>listCount / listLimit 를 실수 연산으로 수행하여 소수점까지 계산하고
			// Math.ceil() 메서드를 통해 올림 처리 후 결과값을 정수로 변환
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
			// --------------------------------------------------------------------------------
//			System.out.println(pageInfo.toString());
			// 게시물 목록(boardList) 과 페이징 처리 정보(pageInfo) 를 Model 객체에 저장
			model.addAttribute("boardList", boardList);
			model.addAttribute("pageInfo", pageInfo);
			
				return "board/qna_board_list";
		}
	
	
	
}
