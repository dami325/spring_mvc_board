package com.itwillbs.mvc_board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.mvc_board.mapper.MemberMapper;
import com.itwillbs.mvc_board.vo.MemberVO;

@Service
public class MemberService {
	// SQL 구문 실행을 담당할 XXXMapper.xml 파일과 연동되는 XXXMapper 객체 자동 주입 설정
	// MemberMapper 객체 자동 주입을 위한 어노테이션 설정
	@Autowired
	private MemberMapper mapper;

	// 회원 가입 joinMember() 메서드
	// => 파라미터 : MemberVO 객체   리턴타입 : int(insertCount)
	public int joinMember(MemberVO member) {
//		System.out.println("MemberService - joinMember");
		// Mapper 객체의 메서드 호출하여 SQL 구문 실행 요청(DAO 객체 없이도 실행)
		// => Mapper 객체의 메서드 실행 후 리턴되는 값을 직접 return 문에 사용하도록
		//    메서드 호출 코드 자체를 return 문 뒤에 바로 작성
		//    (리턴값이 없을 경우 메서드 호출만 기술)
		// => 단, 메서드 호출 후에도 추가 작업이 필요한 경우 메서드 호출과 리턴을 분리
		return mapper.insertMember(member);
	}

	// 로그인 loginMember()
	// => 파라미터 : MemberVO 객체, 리턴타입 : MemberVO
//	public MemberVO loginMember(MemberVO member) {
//		return mapper.loginMember(member);
//	}
	
	// BCryptPasswordEncoder 활용한 로그인을 위해 패스워드 조회 - getPasswd()
	// => 파라미터 : 아이디, 리턴타입 : String
	public String getPasswd(String id) {
		return mapper.selectPasswd(id);
	}


	// 회원 정보 조회 수행 getMemberInfo()
	// => 파라미터 : 아이디, 리턴타입 : MemberVO(member)
	public MemberVO getMemberInfo(String id) {
		return mapper.selectMemberInfo(id);
	}

	// 전체 회원 목록 조회 getMemberList()
	// => 파라미터 : 없음   리턴타입 : List<MemberVO>(memberList)
	public List<MemberVO> getMemberList() {
		return mapper.selectMemberList();
	}

	// 회원 삭제 removeMember()
	// => 파라미터 : 아이디(id), 리턴타입 : int(deleteCount)
	public int removeMember(String id) {
		return mapper.deleteMember(id);
	}

	// 회원 정보 수정 modifyMember()
	// => 파라미터 : MemberVO 객체(member), 새 패스워드(newPasswd)
	// => 리턴타입 : int(updateCount)
	public int modifyMember(MemberVO member, String newPasswd) {
		return mapper.updateMember(member, newPasswd);
	}

}













