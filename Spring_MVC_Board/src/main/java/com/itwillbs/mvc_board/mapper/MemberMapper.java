package com.itwillbs.mvc_board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.itwillbs.mvc_board.vo.MemberVO;

// Service 객체에서 사용할(호출할) 메서드 형태를 추상메서드로 정의(DAO 클래스 대신 사용)
// => 정의된 추상메서드는 XML 파일(XXXMapper.xml) 에서 연결되어 활용됨
// => 주의! 추상메서드 이름과 XML 파일에서의 id 속성값이 같아야 함!
public interface MemberMapper {
	// 1. 회원 가입에 필요한 insertMember() 메서드 정의
	// => 파라미터 : MemberVO 객체(member), 리턴타입 : int
	public int insertMember(MemberVO member);

	// 2. 로그인에 필요한 loginMember() 메서드 정의
	// => 파라미터 : MemberVO 객체(member), 리턴타입 : MemberVO
//	public MemberVO loginMember(MemberVO member);
	
	// BCryptPasswordEncoder 활용 로그인에 필요한 selectPasswd() 메서드 정의
	// => 파라미터 : 아이디, 리턴타입 String
	public String selectPasswd(String id);
	

	// 3. 회원 정보 조회에 필요한 selectMemberInfo() 메서드 정의
	// => 파라미터 : 아이디, 리턴타입 : MemberVO
	public MemberVO selectMemberInfo(String id);

	// 4. 전체 회원 목록 조회에 필요한 selectMemberList() 메서드 정의
	// => 파라미터 : 없음, 리턴타입 : List<MemberVO>
	public List<MemberVO> selectMemberList();

	// 5. 회원 삭제에 필요한 deleteMember() 메서드 정의
	// => 아이디(id), 리턴타입 : int(deleteCount)
	public int deleteMember(String id);

	// 6. 회원 정보 수정에 필요한 updateMember() 메서드 정의
	// => 파라미터 : MemberVO 객체(member), 새 패스워드(newPasswd)
	// => 리턴타입 : int(updateCount)
//	public int updateMember(MemberVO member, String newPasswd);
	// => 단, 복수개의 파라미터가 전달될 경우 각 파라미터를 Mapper 에서 구별하기 위해
	//    @Param 어노테이션을 사용하여 해당 파라미터의 이름을 지정해줘야 한다!
	//    ex) @Param("member") MemberVO member, @Param("newPasswd") String newPasswd
	public int updateMember(@Param("member") MemberVO member, @Param("newPasswd") String newPasswd);

}














