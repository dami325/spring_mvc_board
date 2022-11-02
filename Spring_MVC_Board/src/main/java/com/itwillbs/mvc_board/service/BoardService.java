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
	
	
	public int registBoard(BoardVO board) {
		return mapper.insertBoard(board);
	}



	
	// 전체 글 목록 갯수 조회
	public int getBoardListCount() {
		return mapper.selectBoardListCount();
	}

	// 게시물 목록 조회
	public List<BoardVO> getBoardList(int startRow, int listLimit) {
		return mapper.selectBoardList(startRow,listLimit);
	}
	
}
