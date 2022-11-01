package com.itwillbs.mvc_board.vo;

/*
 회원 인증 정보 저장하는 auth_info 테이블 정의
 id 컬럼 - 16글자 NN
 auth_code 컬럼 - 50글자 NN
 --------------------------------
 CREATE TABLE auth_info (
 	id VARCHAR(16) NOT NULL,
 	auth_code VARCHAR(50) NOT NULL
 );
 */
public class AuthInfoVO {
	private String id;
	private String auth_code;
	
	public AuthInfoVO() {}

	public AuthInfoVO(String id, String auth_code) {
		this.id = id;
		this.auth_code = auth_code;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAuth_code() {
		return auth_code;
	}
	public void setAuth_code(String auth_code) {
		this.auth_code = auth_code;
	}
	
}

















