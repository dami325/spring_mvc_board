package com.itwillbs.mvc_board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.itwillbs.mvc_board.vo.BoardVO;

public interface BoardMapper {

	// 글 등록
	int insertBoard(BoardVO board);

	// 전체 글 목록 갯수 조회
	int selectBoardListCount(
			@Param("searchType") String searchType, @Param("keyword") String keyword);

	// 게시물 목록 조회(복수개의 파라미터는 @Param 어노테이션으로 이름 설정)
	List<BoardVO> selectBoardList(
			@Param("startRow") int startRow, @Param("listLimit") int listLimit,
			@Param("searchType") String searchType, @Param("keyword") String keyword);

	// 게시물 조회수 증가
	void updateReadcount(int board_num);

	// 게시물 상세 정보 조회
	BoardVO selectBoard(int board_num);

	// 게시물 삭제
	int deleteBoard(BoardVO board);

	// 게시물 수정
	int updateBoard(BoardVO board);

	// 답글 순서번호 조정
	void updateBoardReSeq(BoardVO board);

	// 답글 등록
	int insertReplyBoard(BoardVO board);
	
}











