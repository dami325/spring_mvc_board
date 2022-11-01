package com.itwillbs.mvc_board.cipher;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MyMessageDigest {
	private MessageDigest md; // 암호화 수행 클래스 타입 변수 선언
	private String hashAlgorithm = ""; // 알고리즘 저장 변수
	
	// 파라미터 생성자 정의
	// => 파라미터 : 암호화 알고리즘명을 전달받아 인스턴스 멤버변수 초기화
	public MyMessageDigest(String hashAlgorithm) {
		this.hashAlgorithm = hashAlgorithm;
	}
	
	// 해싱(= 단방향 암호화) 작업을 수행하는 hashing() 메서드 정의
	// => 파라미터 : 평문 데이터
	public String hashing(String strSourceData) {
		String strHashedData = ""; // 암호문 저장 변수
		
		try {
			// java.security.MessageDigest 클래스의 static 메서드 getInstance() 메서드를 호출하여
			// MessageDigest 객체 리턴받기 => 파라미터로 암호화 알고리즘(해싱 함수) 이름 전달
			// => 주의! 알고리즘 이름이 틀렸을 경우 NoSuchAlgorithmException 발생
			MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
			
			// 평문 문자열이 저장된 String 객체의 getBytes() 메서드를 호출하여
			// 평문 문자열을 byte[] 타입으로 변환
			byte[] byteData = strSourceData.getBytes();
			
			// MessageDigest 객체의 update() 메서드를 호출하여 byte[] 배열 타입 평문 전달
			md.update(byteData);
			
			// MessageDigest 객체의 digest() 메서드를 호출하여 평문을 암호문으로 변환(= 해싱)
			// => 해싱 결과값(= 암호문)이 byte[] 타입으로 리턴(기본 32byte 짜리 암호문 생성됨)
			byte[] digestResult = md.digest();
			
			// for 문을 사용하여 암호문 배열 크기만큼 반복
			for(int i = 0; i < digestResult.length; i++) {
				// int 타입 기준으로 음수데이터 -> 양수로 변환한 후
				// 16진수 문자열 대문자로 변환하여 strHashedData 에 문자열 결합
				strHashedData += Integer.toHexString(digestResult[i] & 0xFF).toUpperCase();
			}
			
			System.out.println(strHashedData);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("해당 암호화 알고리즘이 존재하지 않습니다.");
			e.printStackTrace();
		}
		
		return strHashedData;
	}
	
	// 생성된 암호문과 새로운 평문을 전달받아 두 패스워드 비교하는 matches() 메서드 정의
	// => 파라미터 : 암호문, 평문     리턴타입 : boolean
	public boolean matches(String encryptedStr, String plainStr) {
		// 전달받은 평문을 hashing() 메서드를 통해 암호문으로 변환한 후
		// 기존의 암호문과 equals() 메서드를 사용하여 비교하여 일치 여부를 리턴
		// => String 객체의 equals() 메서드를 직접 return 문에 사용하여 비교 결과를 즉시 리턴
		return encryptedStr.equals(hashing(plainStr));
	}
}
