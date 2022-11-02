package com.itwillbs.mvc_board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.itwillbs.mvc_board.vo.BoardVO;

public interface BoardMapper {

	// 글 등록
	public int insertBoard(BoardVO board);

	// 전체 글 목록 수 조회
	public int selectBoardListCount();
	
	// 게시물 목록 조회(복수개의 파라미터는 @Param 어노테이션으로 이름 설정해야함)
	public List<BoardVO> selectBoardList(@Param("startRow") int startRow,@Param("listLimit") int listLimit);

	
}
