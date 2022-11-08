package com.itwillbs.mvc_board.vo;

import java.sql.Date;

import org.springframework.web.multipart.MultipartFile;

/*
 * mvc_board3 데이터베이스 생성 및 board 테이블 정의
    CREATE DATABASE mvc_board3;
    use mvc_board3
    
	CREATE TABLE board (
		board_num INT PRIMARY KEY,
		board_name VARCHAR(20) NOT NULL,
		board_pass VARCHAR(16) NOT NULL,
		board_subject VARCHAR(50) NOT NULL,
		board_content VARCHAR(2000) NOT NULL,
		board_file VARCHAR(200) NOT NULL,
		board_real_file VARCHAR(200) NOT NULL,
		board_re_ref INT NOT NULL,
		board_re_lev INT NOT NULL,
		board_re_seq INT NOT NULL,
		board_readcount INT DEFAULT 0,
		board_date DATE
	);
 * 
 * mvc_board3 데이터베이스의 board 테이블(게시판) 1개 레코드(= 1개 게시물) 정보를 저장하는
 * Bean 클래스(DTO or VO) 정의
 * 
 */
public class BoardVO {
	// board 테이블 컬럼에 대응하는 멤버변수 선언
	private int board_num;
	private String board_name;
	private String board_pass;
	private String board_subject;
	private String board_content;
	private String board_file; // 원본 파일명
	private String board_real_file; // 실제 업로드 된 파일명(중복처리 된 파일명)
	private int board_re_ref;
	private int board_re_lev;
	private int board_re_seq;
	private int board_readcount;
	private Date board_date; // java.sql.Date
	private MultipartFile file; // 파일 처리(input type="file")를 위한 MultipartFile 타입 선언
	
	public int getBoard_num() {
		return board_num;
	}
	public void setBoard_num(int board_num) {
		this.board_num = board_num;
	}
	public String getBoard_name() {
		return board_name;
	}
	public void setBoard_name(String board_name) {
		this.board_name = board_name;
	}
	public String getBoard_pass() {
		return board_pass;
	}
	public void setBoard_pass(String board_pass) {
		this.board_pass = board_pass;
	}
	public String getBoard_subject() {
		return board_subject;
	}
	public void setBoard_subject(String board_subject) {
		this.board_subject = board_subject;
	}
	public String getBoard_content() {
		return board_content;
	}
	public void setBoard_content(String board_content) {
		this.board_content = board_content;
	}
	public String getBoard_file() {
		return board_file;
	}
	public void setBoard_file(String board_file) {
		this.board_file = board_file;
	}
	public String getBoard_real_file() {
		return board_real_file;
	}
	public void setBoard_real_file(String board_real_file) {
		this.board_real_file = board_real_file;
	}
	public int getBoard_re_ref() {
		return board_re_ref;
	}
	public void setBoard_re_ref(int board_re_ref) {
		this.board_re_ref = board_re_ref;
	}
	public int getBoard_re_lev() {
		return board_re_lev;
	}
	public void setBoard_re_lev(int board_re_lev) {
		this.board_re_lev = board_re_lev;
	}
	public int getBoard_re_seq() {
		return board_re_seq;
	}
	public void setBoard_re_seq(int board_re_seq) {
		this.board_re_seq = board_re_seq;
	}
	public int getBoard_readcount() {
		return board_readcount;
	}
	public void setBoard_readcount(int board_readcount) {
		this.board_readcount = board_readcount;
	}
	public Date getBoard_date() {
		return board_date;
	}
	public void setBoard_date(Date board_date) {
		this.board_date = board_date;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	@Override
	public String toString() {
		return "BoardVO [board_num=" + board_num + ", board_name=" + board_name + ", board_pass=" + board_pass
				+ ", board_subject=" + board_subject + ", board_content=" + board_content + ", board_file=" + board_file
				+ ", board_real_file=" + board_real_file + ", board_re_ref=" + board_re_ref + ", board_re_lev="
				+ board_re_lev + ", board_re_seq=" + board_re_seq + ", board_readcount=" + board_readcount
				+ ", board_date=" + board_date + ", file=" + file + "]";
	}
	
}



















