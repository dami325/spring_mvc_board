package com.itwillbs.mvc_board.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.mvc_board.cipher.MyMessageDigest;
import com.itwillbs.mvc_board.service.MemberService;
import com.itwillbs.mvc_board.vo.MemberVO;

@Controller
public class MemberController {
	// Service 객체를 직접 생성하지 않고, 자동 주입 기능을 위한 어노테이션 사용
	// => @Inject(자바-플랫폼 공용) 또는 @Autowired(스프링 전용) 어노테이션 사용 가능
	// => 어노테이션 지정 후 자동 주입으로 객체를 생성하면 저장될 클래스 타입 변수 선언
	// => 단, 해당 클래스는 반드시 어노테이션을 통해 역할이 지정되어야 한다!
	@Autowired
	private MemberService service;
	
	// "/MemberJoinForm.me" 요청에 대해 member/member_join_form.jsp 페이지 실행
	@GetMapping(value = "/MemberJoinForm.me")
	public String join() {
		return "member/member_join_form";
	}
	
	// "/MemberJoinPro.me" 요청에 대해 비즈니스 로직 처리할 joinPro() 메서드 정의 - POST
	// => 파라미터 : 회원 가입 정보(MemberVO), Model 객체
	@PostMapping(value = "/MemberJoinPro.me")
	public String joinPro(@ModelAttribute MemberVO member, Model model) {
		// 파라미터로 전달받은 패스워드를 암호화 후 덮어쓰기("SHA-256" 알고리즘 사용)
//		MyMessageDigest md = new MyMessageDigest("SHA-256");
//		member.setPasswd(md.hashing(member.getPasswd()));
//		System.out.println(member);

		// ------------------ BCryptPasswordEncoder 활용한 해싱 ----------------------
		// BCryptPasswordEncoder 를 활용하여 해싱할 경우 Salting 을 통해
		// 동일한 평문이라도 매번 다른 결과값을 갖는 해싱이 가능하다!
		// 1. BCryptPasswordEncoder 객체 생성(spring-security-web 라이브러리 필수)
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		// 2. BCryptPasswordEncoder 객체의 encode() 메서드를 호출하여 해싱 결과 리턴
		//    => 파라미터 : 평문    리턴타입 : String
		String securePasswd = encoder.encode(member.getPasswd());
//		System.out.println(securePasswd);
		// 3. MemberVO 객체의 패스워드에 암호문 저장
		member.setPasswd(securePasswd);
		// ---------------------------------------------------------------------------
		// MemberService 객체의 joinMember() 메서드 호출
		// => 파라미터 : MemberVO 객체   리턴타입 : int(insertCount)
//		MemberService service = new MemberService();
		// @AutoWired 어노테이션을 통해 MemberService 객체를 별도로 생성하지 않아도 
		// 자동 주입(= 의존 주입 = DI)되므로 객체를 직접 생성하지 않고 사용 가능
		int insertCount = service.joinMember(member);
//		int insertCount = 0;
		
		if(insertCount > 0) { // 가입 성공
//			System.out.println("가입 성공!");
			return "redirect:/";
		} else { // 가입 실패
			// 가입 실패 메세지 출력 후 "member/fail_back.jsp" 페이지로 이동(Dispatch)
			// => Model 객체를 통해 "msg" 속성명으로 "가입 실패!" 메세지 전달
//			System.out.println("가입 실패!");
			model.addAttribute("msg", "가입 실패!");
			return "member/fail_back";
		}
		
	}
	
	
	// "/MemberLoginForm.me" 요청에 대해 member/member_login_form.jsp 페이지 실행
	@GetMapping(value = "/MemberLoginForm.me")
	public String login() {
		return "member/member_login_form";
	}
	
	// "/MemberLoginPro.me" 요청에 대해 비즈니스 로직 처리 - POST
	@PostMapping(value = "/MemberLoginPro.me")
	public String loginPro(@ModelAttribute MemberVO member, Model model, HttpSession session) {
		// 파라미터로 전달받은 패스워드를 암호화 후 덮어쓰기("SHA-256" 알고리즘 사용)
//		MyMessageDigest md = new MyMessageDigest("SHA-256");
//		member.setPasswd(md.hashing(member.getPasswd()));
		// MemberService - loginMember() 호출
		// => 파라미터 : MemberVO 객체, 리턴타입 : MemberVO(memberResult)
//		MemberVO memberResult = service.loginMember(member);
		
		// ------------------ BCryptPasswordEncoder 활용한 로그인 판별 ----------------------
		// 1. BCryptPasswordEncoder 객체 생성
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		// 2. member 테이블에서 id 에 해당하는 패스워드 조회 후 리턴값 저장(getPasswd())
		//    => 파라미터 : 아이디    리턴타입 : String(passwd)
		String passwd = service.getPasswd(member.getId());
//		System.out.println(passwd);
		
		// 3. 조회 결과를 활용하여 로그인 성공 여부 판별
		//    1) 아이디가 없을 경우(passwd 값이 null) 실패
		//    2) 패스워드 비교(BCryptPasswordEncoder 객체의 matches() 메서드 활용)
		//       2-1) 다를 경우 실패
		//       2-2) 같을 경우 성공
		if(passwd == null || !encoder.matches(member.getPasswd(), passwd)) {
//			System.out.println("로그인 실패!");
			model.addAttribute("msg", "로그인 실패!");
			return "member/fail_back";
		} else {
//			System.out.println("로그인 성공!");
			// HttpSession 객체에 세션 아이디 저장 후 메인페이지로 이동(Redirect 방식)
			session.setAttribute("sId", member.getId());
			return "redirect:/";
		}
		
	}
	
	// MemberLogout.me 요청에 대한 로그아웃 처리
	@GetMapping(value = "/MemberLogout.me")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	// MemberInfo.me 요청에 대한 회원정보 조회 처리 => GET
	// => 파라미터 : 아이디, 
	//               조회 결과(회원 1명의 정보)를 저장하는 MemberVO 객체를 저장할 Model 타입 변수 필요
	//               전달받은 아이디와 비교를 통해 권한 확인에 사용될 세션 객체 필요
	@GetMapping(value = "/MemberInfo.me")
	public String getMemberInfo(
			@RequestParam String id, Model model, HttpSession session) {
		// 세션 아이디 가져와서 sId 변수에 저장
		String sId = (String)session.getAttribute("sId");
		System.out.println(id + ", " + sId);
		
		// 아이디와 세션 아이디가 null 이 아니고, 아이디가 세션 아이디와 같거나
		// 아니면, 세션 아이디가 "admin" 일 경우 회원 정보 조회하는 조건
//		if(id != null && sId != null && id.equals(sId) || sId.equals("admin")) {}
		
		// 전달받은 아이디가 null 이거나 세션 아이디가 null 이거나
		// 전달받은 아이디와 세션 아이디가 같지 않거나, 세션 아이디가 "admin" 이 아니면
		// Model 객체에 "msg" 속성명으로 "잘못된 접근입니다!" 저장 후 fail_back.jsp 로 이동
		// 아니면, 회원 정보 조회 수행을 위해 Service 객체의 getMemberInfo() 메서드 호출
		// => 파라미터 : 아이디, 리턴타입 : MemberVO(member)
		if(id == null || sId == null || id.equals("") || (!id.equals(sId) && !sId.equals("admin"))) {
			model.addAttribute("msg", "잘못된 접근입니다!");
			return "member/fail_back";
		} else {
			MemberVO member = service.getMemberInfo(id); // 파라미터는 검색할 아이디 전달
			// Model 객체에 "member" 속성명으로 MemberVO 객체 저장
			System.out.println(member);
			model.addAttribute("member", member);
			// member/member_info.jsp 페이지로 이동
			return "member/member_info";
		}
	}
		
	@GetMapping(value = "/AdminMain.me")
	public String admin(Model model, HttpSession session) {
		// 세션 아이디 가져와서 sId 변수에 저장
		String sId = (String)session.getAttribute("sId");
		
		if(sId == null || !sId.equals("admin")) {
			model.addAttribute("msg", "잘못된 접근입니다!");
			return "member/fail_back";
		} else {
			// Service 객체의 getMemberList() 메서드를 호출하여 전체 회원 목록 조회
			// => 파라미터 : 없음   리턴타입 : List<MemberVO>(memberList)
			List<MemberVO> memberList = service.getMemberList();
			
			// Model 객체에 "memberList" 라는 속성명으로 List 객체 추가
			model.addAttribute("memberList", memberList);
			
			// 관리자 메인페이지(admin/admin_main.jsp) 로 이동
			return "admin/admin_main";
		}
		
	}
	
	@GetMapping(value = "MemberDelete.me")
	public String delete(@RequestParam String id, Model model, HttpSession session) {
		String sId = (String)session.getAttribute("sId");
		// 아이디와 세션 아이디가 null 이 아니고, 아이디가 세션 아이디와 같거나
		// 아니면, 세션 아이디가 "admin" 일 경우 회원 삭제하는 조건
		if(id != null && sId != null && id.equals(sId) || sId.equals("admin")) {
			// Service 객체의 removeMember() 메서드 호출하여 회원 삭제
			// => 파라미터 : 아이디(id), 리턴타입 : int(deleteCount)
			int deleteCount = service.removeMember(id);
			
			// 삭제 결과(deleteCount) 성공(0보다 클 경우)이면
			if(deleteCount > 0) {
				// 세션 아이디가 "admin" 일 경우 "AdminMain.me" 서블릿 요청하고
				// 아니면, 세션 초기화 후 메인페이지로 리다이렉트
				if(sId.equals("admin")) {
					return "redirect:/AdminMain.me";
				} else {
					session.invalidate();
					return "redirect:/";
				}
			} else {
				// 아니면(= 실패) Model 객체의 "msg" 속성에 "삭제 실패!" 저장 후 fail_back.jsp 이동
				model.addAttribute("msg", "삭제 실패!");
				return "member/fail_back";
			}
		} else {
			model.addAttribute("msg", "잘못된 접근입니다!");
			return "member/fail_back";
		}
	}
	
	@PostMapping(value = "MemberModify.me")
	public String modify(
			@ModelAttribute MemberVO member, @RequestParam String newPasswd, 
			Model model, HttpSession session) {
		String sId = (String)session.getAttribute("sId");
		String id = member.getId();
		
		if(id != null && sId != null && id.equals(sId) || sId.equals("admin")) {
			// 기존 패스워드와 새 패스워드 모두 MyMessageDigest 객체를 활용하여 해싱 후 덮어쓰기
			MyMessageDigest md = new MyMessageDigest("SHA-256");
			member.setPasswd(md.hashing(member.getPasswd()));
			
			// 새 패스워드는 존재할 경우에만 해싱
			if(newPasswd != null && !newPasswd.equals("")) {
				newPasswd = md.hashing(newPasswd);
			}
			
			// Service 객체의 modifyMember() 메서드를 호출하여 회원 정보 수정
			// => 파라미터 : MemberVO 객체(member), 새 패스워드(newPasswd)
			// => 리턴타입 : int(updateCount)
			int updateCount = service.modifyMember(member, newPasswd);
			System.out.println(member);
			System.out.println(newPasswd);
			System.out.println("결과 : " + updateCount);
			
			// 삭제 결과(updateCount) 성공(0보다 클 경우)이면
			if(updateCount > 0) {
				// "MemberInfo.me" 서블릿 요청(id 파라미터 필요)
				return "redirect:/MemberInfo.me?id=" + id;
			} else {
				// 아니면(= 실패) Model 객체의 "msg" 속성에 "수정 실패!" 저장 후 fail_back.jsp 이동
				model.addAttribute("msg", "수정 실패!");
				return "member/fail_back";
			}
		} else {
			model.addAttribute("msg", "잘못된 접근입니다!");
			return "member/fail_back";
		}
		
	}
	
	
}











