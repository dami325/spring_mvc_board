package com.itwillbs.mvc_board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.mvc_board.mapper.BoardMapper;
import com.itwillbs.mvc_board.vo.BoardVO;

@Service
public class BoardService {
	@Autowired
	private BoardMapper mapper;

	// 글 쓰기
	public int registBoard(BoardVO board) {
		return mapper.insertBoard(board);
	}

	// 전체 글 목록 갯수 조회
	// => 파라미터 : 검색타입, 검색어    리턴타입 : int(listCount)
	public int getBoardListCount(String searchType, String keyword) {
		return mapper.selectBoardListCount(searchType, keyword);
	}

	// 게시물 목록 조회
	// => 파라미터 : 시작행번호, 페이지 당 목록 갯수, 검색타입, 검색어
	//    리턴타입 : List<BoardVO>(boardList)
	public List<BoardVO> getBoardList(
			int startRow, int listLimit, String searchType, String keyword) {
		return mapper.selectBoardList(startRow, listLimit, searchType, keyword);
	}

	// 게시물 조회수 증가
	// => 파라미터 : 글번호, 리턴타입 : void
	public void increaseReadcount(int board_num) {
		mapper.updateReadcount(board_num);
	}

	// 게시물 상세 정보 조회
	// => 파라미터 : 글번호, 리턴타입 : BoardVO(board)
	public BoardVO getBoard(int board_num) {
		return mapper.selectBoard(board_num);
	}

	// 게시물 삭제
	// => 파라미터 : BoardVO 객체, 리턴타입 : int(deleteCount)
	public int removeBoard(BoardVO board) {
		return mapper.deleteBoard(board);
	}
	
}











