package com.harunichi.member.controller;

import java.sql.Date;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.harunichi.member.dao.MemberDao;
import com.harunichi.member.dao.MemberDaoImpl;
import com.harunichi.member.service.MemberService;
import com.harunichi.member.vo.MemberVo;
import com.harunichi.test.controller.TestController;

@Controller("memberController")
@RequestMapping(value="/member")
public class MemberControllerImpl implements MemberController{
	
	
	private static final Logger logger = LoggerFactory.getLogger(MemberControllerImpl.class);
	
	@Autowired
    private MemberDao memberDao;
	
	@Autowired 
	private MemberService memberService;
	
	
	@Override //요청 페이지 보여주는 메소드
	@RequestMapping(value = {"/loginpage.do", "/addMemberForm.do", "/emailAuthForm.do", "/addMemberWriteForm.do"}, method = RequestMethod.GET)
	public ModelAndView showForms(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
	    String viewName = (String) request.getAttribute("viewName");
	    ModelAndView mav = new ModelAndView(viewName);
	    
	    logger.debug("Returning viewName: " + viewName); // Logger로 디버깅 메시지 출력
	    
	    return mav;
	    //"/loginpage.do"요청시->/loginpage.jsp보여줌
	    //"/addMemberForm.do"요청시->/addMemberForm.jsp보여줌
	}

	
	@Override//로그인메소드
	public ModelAndView login(Map<String, String> loginMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Attempting login with parameters: " + loginMap); // 로그인 시도 로그
		return null;
	}
	
	
	// 카카오 API 설정 (REST API 키, Redirect URI)
    private final String KAKAO_REST_API_KEY = "e0c7dc056f537df0edb757b015e72883";
    private final String KAKAO_REDIRECT_URI = "http://localhost:8090/harunichi/member/KakaoCallback.do";
    
    // KakaoCallback 앤드포인트 (카카오 인증 서버로부터 리다이렉트)
    @RequestMapping(value = "/KakaoCallback.do", method = RequestMethod.GET)
    public ModelAndView kakaoCallback(@RequestParam("code") String code, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        HttpSession session = request.getSession();

        System.out.println("카카오로부터 받은 인증 코드: " + code); // 1. 인증 코드 확인

        // 2. 인증 코드를 사용하여 Access Token을 발급받기 (POST 요청)
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        // Header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // Body 설정 (MultiValueMap 사용)
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", KAKAO_REST_API_KEY);
        params.add("redirect_uri", KAKAO_REDIRECT_URI);
        params.add("code", code); // 받은 인증 코드

        HttpEntity<MultiValueMap<String, String>> tokenRequestEntity = new HttpEntity<>(params, headers);

        // POST 요청 보내기 (Access Token 발급 요청)
        ResponseEntity<Map> tokenResponse = restTemplate.exchange(tokenUrl, HttpMethod.POST, tokenRequestEntity, Map.class);

        // 응답 결과 확인 (Access Token 추출)
        Map<String, Object> tokenInfo = tokenResponse.getBody();
        System.out.println("Access Token 발급 응답: " + tokenInfo);

        String accessToken = (String) tokenInfo.get("access_token"); // Access Token 추출!

        // 3. Access Token을 사용하여 사용자 정보를 가져오기 (GET 요청)
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        // Header 설정 (Access Token 사용)
        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.add("Authorization", "Bearer " + accessToken); // Bearer 타입으로 Access Token 사용!
        userInfoHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8"); // Content-Type 설정

        HttpEntity<MultiValueMap<String, String>> userInfoRequestEntity = new HttpEntity<>(userInfoHeaders);

        // GET 요청 보내기 (사용자 정보 요청)
        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userInfoRequestEntity, Map.class);

        // 응답 결과 확인 (사용자 정보 추출)
        Map<String, Object> userinfo = userInfoResponse.getBody();
        System.out.println("사용자 정보 응답: " + userinfo); // 응답 내용 확인!

        // 4. 가져온 사용자 정보를 MemberVo 객체에 담고 세션에 저장
        MemberVo kakaoMember = new MemberVo();
        String kakaoId = String.valueOf(userinfo.get("id"));
        kakaoMember.setId("kakao_" + kakaoId);

        if (userinfo.containsKey("properties")) {
            Map<String, Object> properties = (Map<String, Object>) userinfo.get("properties");
            kakaoMember.setNick((String) properties.get("nickname"));
            // ✨ 이름 가져오기! (properties 또는 kakao_account에서) ✨
            if (properties.containsKey("name")) {
                kakaoMember.setName((String) properties.get("name"));
            }
        }
        if (userinfo.containsKey("kakao_account")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) userinfo.get("kakao_account");
            // 이메일
            if (kakaoAccount.containsKey("email")) {
                kakaoMember.setEmail((String) kakaoAccount.get("email"));
            }
            // 성별
            if (kakaoAccount.containsKey("gender")) {
                kakaoMember.setGender((String) kakaoAccount.get("gender"));
            }
            // 생년월일 (birthday, birthyear)
            if (kakaoAccount.containsKey("birthday") && kakaoAccount.containsKey("birthyear")) {
                String birthday = (String) kakaoAccount.get("birthday");
                String birthyear = (String) kakaoAccount.get("birthyear");
                try {
                    Calendar cal = Calendar.getInstance();
                    int year = Integer.parseInt(birthyear);
                    int month = Integer.parseInt(birthday.substring(0, 2));
                    int day = Integer.parseInt(birthday.substring(2));
                    cal.set(year, month - 1, day);
                    kakaoMember.setYear(new Date(cal.getTimeInMillis()));
                } catch (NumberFormatException e) {
                    System.err.println("생년월일 파싱 오류: " + e.getMessage());
                }
            }
            // 전화번호
            if (kakaoAccount.containsKey("phone_number")) {
                kakaoMember.setTel((String) kakaoAccount.get("phone_number"));
            }
            // 배송지 정보
            if (kakaoAccount.containsKey("shipping_address")) {
                Map<String, Object> shippingAddress = (Map<String, Object>) kakaoAccount.get("shipping_address");
                // 기본 주소
                if (shippingAddress != null && shippingAddress.containsKey("base_address")) {
                    kakaoMember.setAddress((String) shippingAddress.get("base_address"));
                }
            }
        }

        // 5. 세션에 MemberVo 객체 저장
        session.setAttribute("kakaoMember", kakaoMember);
        session.setAttribute("authType", "kakao"); // 인증 방식도 세션에 저장 (회원가입 폼에서 활용)

        //6. DB에 이미 가입된 회원인지 확인 (selectMemberByKakaoId 호출) ✨
        MemberVo dbMember = null;
		try {
			logger.info("DB 조회 시도: kakao_id = {}", "kakao_" + kakaoId); // ✨ 조회하는 ID 로그! ✨
            dbMember = memberService.selectMemberByKakaoId("kakao_" + kakaoId);
            logger.info("DB 조회 결과 (dbMember): {}", dbMember); // ✨ 조회 결과 로그! ✨
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("카카오 ID로 회원 정보 조회 중 오류 발생", e);
			dbMember = new MemberVo();// dbMember를 기본 객체로 초기화!
		}

        if (dbMember != null) {
            //이미 가입된 회원이면 로그인 처리 (세션 저장 후 메인 등으로 리다이렉트)
            session.setAttribute("member", dbMember); // 사용자 정보를 세션에 저장 (우리가 DB에 저장한 회원 정보)
            session.setAttribute("isLogOn", true); // 로그인 상태 표시
            session.setAttribute("memberId", dbMember.getId()); // 회원 ID 세션에 저장

            mav.setViewName("redirect:/main.do"); // 메인 페이지로 리다이렉트
            return mav; // 메서드 종료!
        } else {
            //가입되지 않은 회원이면 회원가입 폼으로 리다이렉트
            mav.setViewName("redirect:/member/addMemberWriteForm.do"); // 회원 정보를 입력받는 폼 페이지
            return mav;
        }
    }
    
    // 네이버 API 설정 (나중에 구현)
    private final String NAVER_CLIENT_ID = ""; // 네이버 Client ID
    private final String NAVER_CLIENT_SECRET = ""; // 네이버 Client Secret
    private final String NAVER_REDIRECT_URI = "http://localhost:8090/harunichi/member/NaverCallback.do"; // 네이버 Redirect URI

	
	@Override//로그아웃메소드
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("User logged out."); // 로그아웃 로그
		return null;
	}
	
	
	// 국적에 따른 회원가입 폼 내용을 반환하는 메소드
    @RequestMapping(value = "/getRegistrationForm", method = RequestMethod.GET)
    public String getRegistrationForm(@RequestParam("nationality") String nationality, Model model) {
        if ("KR".equals(nationality)) {
        	logger.info("Returning Korean registration form."); // KR 선택
            return "member/addMemberFormSelectKr"; // KR선택시 addMemberFormKr
        } else if ("JP".equals(nationality)) {
        	logger.info("Returning Japanese registration form."); // JP 선택
            return "member/addMemberFormSelectJp"; // JP선택시 addMemberFormJp
        } else {
        	logger.warn("Invalid nationality: " + nationality); // 잘못된 국적
            // 잘못된 국적 값이 넘어왔을 경우
            return "error/invalidNationality";
        }
    }

    
	@Override//회원가입메소드
	public ResponseEntity addMember(MemberVo member, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Attempting to add member: " + member); // 회원가입 시도 로그
		return null;
	}

	
	@Override//아이디 중복확인 메소드
	public ResponseEntity overlapped(String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("Checking if ID is overlapped: " + id); // 아이디 중복 확인 로그
		return null;
	}
	
}
