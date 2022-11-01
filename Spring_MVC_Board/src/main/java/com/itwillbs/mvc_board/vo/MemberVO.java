package com.itwillbs.mvc_board.vo;

import java.sql.Date;

/*
 * member 테이블 정의
 * ---------------------------
   번호(idx) - INT, PK, AI
   이름(name) - VARCHAR(20) NN
   아이디(id) - VARCHAR(16), UN, NN
   패스워드(passwd) - VARCHAR(16), NN
   E-Mail(email) - VARCHAR(50), UN, NN
   성별(gender) - VARCHAR(1) NN
   가입일(date) - DATE NN
   인증여부(auth_status) - CHAR(1) NN
   ---------------------------
   CREATE TABLE member (
		idx INT PRIMARY KEY AUTO_INCREMENT,
		name VARCHAR(20) NOT NULL,
		id VARCHAR(16) UNIQUE NOT NULL,
		passwd VARCHAR(16) NOT NULL,
		email VARCHAR(50) UNIQUE NOT NULL,
		gender VARCHAR(1) NOT NULL,
		date DATE NOT NULL,
		auth_status CHAR(1) NOT NULL
   );
   -----------------------------------
   기존 테이블에 인증 상태 컬럼(auth_status) 추가할 경우
   ALTER TABLE member ADD auth_status CHAR(1) NOT NULL;
*/
public class MemberVO {
	private int idx;
	private String name;
	private String id;
	private String passwd;
	private String email;
	private String gender;
	private Date date; // java.sql 패키지
	private String auth_status;
	
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getAuth_status() {
		return auth_status;
	}
	public void setAuth_status(String auth_status) {
		this.auth_status = auth_status;
	}
	
	@Override
	public String toString() {
		return "MemberBean [idx=" + idx + ", name=" + name + ", id=" + id + ", passwd=" + passwd + ", email=" + email
				+ ", gender=" + gender + ", date=" + date + ", auth_status=" + auth_status + "]";
	}
	
	
}
















