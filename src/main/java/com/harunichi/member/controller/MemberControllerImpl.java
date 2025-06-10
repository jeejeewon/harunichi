package com.harunichi.member.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.harunichi.common.util.FileUploadUtil;
import com.harunichi.member.service.MemberService;
import com.harunichi.member.vo.MemberVo;
import com.harunichi.test.controller.TestController;

@Controller("memberController")
@RequestMapping(value="/member")
public class MemberControllerImpl implements MemberController{
	
	
	private static final Logger logger = LoggerFactory.getLogger(MemberControllerImpl.class);
	
	@Autowired 
	private MemberService memberService;
	
	@Override //요청 페이지 보여주는 메소드
	@RequestMapping(value = {"/loginpage.do", "/addMemberForm.do", "/emailAuthForm.do", "/addMemberWriteForm.do", "/profileImgAndMyLikeSetting.do"}, method = RequestMethod.GET)
	public ModelAndView showForms(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
	    String viewName = (String) request.getAttribute("viewName");
	    ModelAndView mav = new ModelAndView(viewName);
	    
	    logger.debug("Returning viewName: " + viewName); // Logger로 디버깅 메시지 출력
	    
	    return mav;
	    //"/loginpage.do"요청시->loginpage.jsp보여줌
	    //"/addMemberForm.do"요청시->addMemberForm.jsp보여줌
	}

	
	@Override//로그인메소드
	public ModelAndView login(Map<String, String> loginMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Attempting login with parameters: " + loginMap); // 로그인 시도 로그
		return null;
	}
	
	
	// 카카오 API 설정 (REST API 키, Redirect URI)
    private final String KAKAO_REST_API_KEY = "e0c7dc056f537df0edb757b015e72883";
    private final String KAKAO_REDIRECT_URI = "http://localhost:8090/harunichi/member/KakaoCallback.do";
    
    // KakaoCallback 메소드
    @RequestMapping(value = "/KakaoCallback.do", method = RequestMethod.GET)
    public ModelAndView kakaoCallback(@RequestParam("code") String code, 
    		 						  @RequestParam(value = "state", required = false, defaultValue = "login") String mode,
    								  HttpServletRequest request) {
    	
    	
        ModelAndView mav = new ModelAndView();
        HttpSession session = request.getSession();
        
        // 1. 인증 코드 확인
        System.out.println("카카오로부터 받은 인증 코드: " + code);

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
        MemberVo memberVo = new MemberVo();
        String kakaoId = String.valueOf(userinfo.get("id"));
        //카카오에서 받아온 정보를 MemberVo에 매핑하기
        memberVo.setId("kakao_" + kakaoId);//id는 "kakao_카카오아이디"로 저장
        memberVo.setKakao_id(kakaoId);//원래카카오아이디는 kakao_id칼럼에 저장
        memberVo.setContry("kr"); //국적 - 카카오가입시 국적은 무조건 kr(한국)으로 저장
        memberVo.setPass(GenerateRandomPassword(12));//비밀번호는 12자리 무작위 생성후 저장

        // 닉네임
        if (userinfo.containsKey("properties")) {
            Map<String, Object> properties = (Map<String, Object>) userinfo.get("properties");
            memberVo.setNick((String) properties.get("nickname"));
        }

        // kakao_account 내부 정보
        if (userinfo.containsKey("kakao_account")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) userinfo.get("kakao_account");

            // 이름
            if (kakaoAccount.containsKey("name")) {
                memberVo.setName((String) kakaoAccount.get("name"));
            }

            // 이메일
            if (kakaoAccount.containsKey("email")) {
                memberVo.setEmail((String) kakaoAccount.get("email"));
            }

            // 성별 (선택)
            if (kakaoAccount.containsKey("gender")) {
                memberVo.setGender((String) kakaoAccount.get("gender"));
            }

            // 전화번호 (선택)
            if (kakaoAccount.containsKey("phone_number")) {
                memberVo.setTel((String) kakaoAccount.get("phone_number"));
            }

            // 생년월일 (필수)
            if (kakaoAccount.containsKey("birthday") && kakaoAccount.containsKey("birthyear")) {
                try {
                    String birthday = (String) kakaoAccount.get("birthday"); // MMDD
                    String birthyear = (String) kakaoAccount.get("birthyear");
                    Calendar cal = Calendar.getInstance();
                    cal.set(Integer.parseInt(birthyear), Integer.parseInt(birthday.substring(0, 2)) - 1, Integer.parseInt(birthday.substring(2)));
                    memberVo.setYear(new Date(cal.getTimeInMillis()));
                } catch (Exception e) {
                    System.err.println("생년월일 파싱 오류: " + e.getMessage());
                }
            }

            // 배송지 주소 (선택)
            if (kakaoAccount.containsKey("shipping_address")) {
                Map<String, Object> shippingAddress = (Map<String, Object>) kakaoAccount.get("shipping_address");
                if (shippingAddress != null && shippingAddress.containsKey("base_address")) {
                    memberVo.setAddress((String) shippingAddress.get("base_address"));
                }
            }
        }

        // 5. 세션에 MemberVo 객체 저장
        session.setAttribute("memberVo", memberVo);
        session.setAttribute("authType", "kakao"); // 인증 방식도 세션에 저장 (회원가입 폼에서 활용)

        //6. DB에 이미 가입된 회원인지 확인 (selectMemberByKakaoId 호출)
        MemberVo dbMember = null;
		try {
			logger.info("DB 조회 시도: kakao_id = {}", kakaoId); //조회하는 ID 로그
			dbMember = memberService.selectMemberByKakaoId(kakaoId);
			logger.info("DB 조회 결과 (dbMember): {}", dbMember); // 조회 결과 로그
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("카카오 ID로 회원 정보 조회 중 오류 발생", e);
			dbMember = new MemberVo();// dbMember를 기본 객체로 초기화
		}
		
		//7. 분기처리 ( 로그인버튼을 눌러서 왔을 경우와 회원가입버튼을 눌러서 왔을경우)
		if ("login".equals(mode)) {
		    if (dbMember != null && dbMember.getId() != null) {
		        // [카카오 로그인] 성공
		        session.setAttribute("member", dbMember);
		        session.setAttribute("isLogOn", true);
		        session.setAttribute("id", dbMember.getId());

		        mav.setViewName("redirect:/");
		    } else {
		        // [카카오 로그인] → 비회원인 경우
		        session.setAttribute("kakaoUserInfo", userinfo);
		        try {
		            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		            response.setContentType("text/html;charset=UTF-8");
		            PrintWriter out = response.getWriter();

		            String contextPath = request.getContextPath();

		            out.println("<script>");
		            out.println("if (confirm('회원이 아닙니다. 카카오 계정으로 가입하시겠습니까?')) {");
		            out.println("    location.href='" + contextPath + "/member/profileImgAndMyLikeSetting.do';");
		            out.println("} else {");
		            out.println("    location.href='" + contextPath + "/member/loginpage.do';");
		            out.println("}");
		            out.println("</script>");

		            out.flush();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		        return null; // ModelAndView 반환 안 함
		    }

		} else if ("join".equals(mode)) {
		    if (dbMember != null && dbMember.getId() != null) {
		        // 이미 가입된 카카오 계정으로 회원가입 시도한 경우
		        session.setAttribute("message", "이미 가입된 카카오 계정입니다. 로그인해주세요.");
		        mav.setViewName("redirect:/member/loginForm.jsp");
		    } else {
		        // 회원가입 로직 그대로
		        session.setAttribute("memberVo", memberVo);
		        session.setAttribute("authType", "kakao");
		        mav.setViewName("redirect:/member/profileImgAndMyLikeSetting.do");
		    }
		}

		return mav;

    }
    
    
    // NaverCallback 메소드
    @RequestMapping(value = "/NaverCallback.do", method = RequestMethod.GET)
    public ModelAndView naverCallback(@RequestParam("code") String code,
                                      @RequestParam(value = "state", required = false, defaultValue = "login") String mode,
                                      HttpServletRequest request) {

        ModelAndView mav = new ModelAndView();
        HttpSession session = request.getSession();

        // 네이버 로그인 설정값
        String clientId = "v80rEgQ4aPt_g050ZNtj";
        String clientSecret = "nJd3qHAENe";
        String redirectUri = "http://localhost:8090/harunichi/member/NaverCallback.do";

        // 1. Access Token 요청
        String tokenUrl = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code"
                        + "&client_id=" + clientId
                        + "&client_secret=" + clientSecret
                        + "&code=" + code
                        + "&state=" + mode;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> tokenResponse = restTemplate.getForEntity(tokenUrl, Map.class);
        Map<String, Object> tokenInfo = tokenResponse.getBody();
        String accessToken = (String) tokenInfo.get("access_token");

        // 2. 사용자 정보 요청
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<String> userInfoRequest = new HttpEntity<>(headers);

        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
            "https://openapi.naver.com/v1/nid/me", HttpMethod.GET, userInfoRequest, Map.class);
        Map<String, Object> responseMap = (Map<String, Object>) userInfoResponse.getBody().get("response");

        // 3. MemberVo 객체 생성
        MemberVo memberVo = new MemberVo();
        String naverId = (String) responseMap.get("id");
        memberVo.setId("naver_" + naverId);
        memberVo.setNaver_id(naverId);
        memberVo.setContry("kr");
        memberVo.setPass(GenerateRandomPassword(12));

        memberVo.setEmail((String) responseMap.get("email"));
        memberVo.setName((String) responseMap.get("name"));
        memberVo.setNick((String) responseMap.get("nickname"));
        memberVo.setGender((String) responseMap.get("gender"));
        memberVo.setTel((String) responseMap.get("mobile"));
        memberVo.setAddress((String) responseMap.get("address"));

        // 생일 데이터 가공 (옵션)
        if (responseMap.containsKey("birthyear") && responseMap.containsKey("birthday")) {
            try {
                String birthyear = (String) responseMap.get("birthyear");   // 예: "1990"
                String birthday = (String) responseMap.get("birthday");     // 예: "12-25" (MM-DD)

                // "1990-12-25" 형태로 변환
                String fullBirth = birthyear + "-" + birthday;

                // java.sql.Date로 변환
                Date birthDate = Date.valueOf(fullBirth);

                // MemberVo에 저장
                memberVo.setYear(birthDate);
            } catch (Exception e) {
                System.err.println("생년월일 파싱 오류: " + e.getMessage());
            }
        }

        // 4. 세션 저장
        session.setAttribute("memberVo", memberVo);
        session.setAttribute("authType", "naver");

        // 5. DB 조회 → 로그인 또는 회원가입 분기
        MemberVo dbMember = null;
        try {
        	dbMember = memberService.selectMemberByNaverId(naverId);
        } catch (Exception e) {
            e.printStackTrace();
            dbMember = new MemberVo();
        }

        if ("login".equals(mode)) {
            if (dbMember != null && dbMember.getId() != null) {
                session.setAttribute("member", dbMember);
                session.setAttribute("isLogOn", true);
                session.setAttribute("id", dbMember.getId());
                mav.setViewName("redirect:/");
            } else {
                // 비회원 → 알림창 후 회원가입 화면
                try {
                    HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
                    response.setContentType("text/html;charset=UTF-8");
                    PrintWriter out = response.getWriter();

                    String contextPath = request.getContextPath();
                    out.println("<script>");
                    out.println("if (confirm('회원이 아닙니다. 네이버 계정으로 가입하시겠습니까?')) {");
                    out.println("    location.href='" + contextPath + "/member/profileImgAndMyLikeSetting.do';");
                    out.println("} else {");
                    out.println("    location.href='" + contextPath + "/member/loginpage.do';");
                    out.println("}");
                    out.println("</script>");
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        } else if ("join".equals(mode)) {
            if (dbMember != null && dbMember.getId() != null) {
                session.setAttribute("message", "이미 가입된 네이버 계정입니다. 로그인해주세요.");
                mav.setViewName("redirect:/member/loginForm.jsp");
            } else {
                session.setAttribute("memberVo", memberVo);
                session.setAttribute("authType", "naver");
                mav.setViewName("redirect:/member/profileImgAndMyLikeSetting.do");
            }
        }

        return mav;
    }

    
    // 프로필이미지, 관심사 세팅 후 가입완료까지(insert까지 처리)
	@RequestMapping(value = "/profileImgAndMyLikeSettingProcess.do", method = RequestMethod.POST)
	public String profileImgAndMyLikeSettingProcess( @RequestParam("profileImg") MultipartFile profileImg, @RequestParam(value = "myLike", required = false) String[] myLikes, HttpServletRequest request, Model model){
		System.out.println("profileImgAndMyLikeSettingProcess 메소드 시작!");
		//1.세션에서 memberVo 객체 가져오기
		MemberVo memberVo = (MemberVo) request.getSession().getAttribute("memberVo");
		if (memberVo == null) {
            // 세션에 memberVo가 없으면 잘못된 접근
            model.addAttribute("message", "잘못된 접근입니다. 다시 시도해주세요.");
            return "redirect:/member/addMemberForm.do"; // 다시 addMemberForm.do로 리다이렉트
        }
		
		System.out.println("memberVo 이름 값 확인: " + memberVo.getName());
		
		
		//2. 프로필 이미지 처리
		String filePath = handleProfileImage(profileImg, request);
		System.out.println("profileImgAndMyLikeSettingProcess - 파일 경로: " + filePath);
		//3. 관심사 처리
		String myLikeStr = handleMyLikes(myLikes);
		System.out.println("profileImgAndMyLikeSettingProcess - 관심사: " + myLikeStr);
		//4. 세션에서 가져온 MemberVo 객체에 프로필 이미지, 관심사 정보 설정하기
		if (filePath != null) {
		    memberVo.setProfileImg(filePath);
		}
        memberVo.setMyLike(myLikeStr);
        System.out.println("profileImgAndMyLikeSettingProcess - 설정 후 memberVo: " + memberVo.toString());
        //5. 이메일 중복 체크
        if (memberService.isEmailDuplicate(memberVo.getEmail())) {
            model.addAttribute("message", "이미 사용 중인 이메일입니다.");
            return "redirect:/member/addMemberForm.do";
        }
		//6. DB에 저장하기
		try {
			System.out.println("profileImgAndMyLikeSettingProcess - insertMember 호출 직전!");
			memberService.insertMember(memberVo); //mapper에 설정한 insert문을 호출하여 db에 저장하게된다~
			System.out.println("profileImgAndMyLikeSettingProcess - insertMember 호출 성공!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("profileImgAndMyLikeSettingProcess - insertMember 호출 중 에러 발생!");
			model.addAttribute("message", "회원가입 처리 중 오류가 발생했습니다.");
			return "redirect:/member/addMemberForm.do"; // 다시 addMemberForm.do로 리다이렉트
		}
		System.out.println("profileImgAndMyLikeSettingProcess - DB 저장 완료!");
		//7. 세션에 있던 memberVo객체 삭제
		request.getSession().removeAttribute("memberVo");
		//8. 인증 방식 정보도 삭제
		request.getSession().removeAttribute("authType");
		//9. 회원가입 완료 후 메인페이지로 리다이렉트하기전에, 로그인을 먼저 시켜주기
		HttpSession session = request.getSession();
		session.setAttribute("member", memberVo);
		session.setAttribute("isLogOn", true);
		session.setAttribute("id", memberVo.getId());
		//10. 모두 완료후 메인페이지로 리다이렉트
		System.out.println("profileImgAndMyLikeSettingProcess - 메인 페이지로 리다이렉트");
		return "redirect:/";
	}
	

	// 프로필 이미지 처리 메소드
	private String handleProfileImage(MultipartFile profileImg, HttpServletRequest request) {
		String uploadDir = request.getServletContext().getRealPath("/resources/images/profile");
		System.out.println("파일 저장 경로: " + uploadDir);
		String fileName = null;
		try {
			if(profileImg != null && !profileImg.isEmpty()) {
				File dir = new File(uploadDir);
	            if (!dir.exists()) {
					boolean success = dir.mkdirs(); // 디렉토리 없으면 생성
					if (!success) {
						System.err.println("폴더 생성 실패!");
						return null; // 폴더 생성 실패 시 null 반환
					}
	            }
				fileName = FileUploadUtil.uploadFile(profileImg, uploadDir);//파일업로드
				return request.getContextPath() + "/resources/images/profile/" + fileName;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; //파일이 없을 경우에 null 반환
	}

	// 관심사 처리 메소드
	private String handleMyLikes(String[] myLikes) {
		if(myLikes != null && myLikes.length > 0) {
			return String.join(",", myLikes);
		}
		return "";
	}
	
	@Override//로그아웃메소드
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("User logged out."); // 로그아웃 로그
		return null;
	}
	
	
	// 국적에 따른 회원가입 폼 내용을 반환하는 메소드
    @RequestMapping(value = "/getRegistrationForm", method = RequestMethod.GET)
    public String getRegistrationForm(@RequestParam("nationality") String nationality, HttpSession session) {
    	// 1. 세션에서 기존 memberVo 객체 꺼내기 시도
    	MemberVo memberVo = (MemberVo) session.getAttribute("memberVo");
    	// 2. 만약 세션에 memberVo가 없으면 새로 생성
        if (memberVo == null) {
            System.out.println("세션에 memberVo 객체가 없어서 새로 생성합니다.");
            memberVo = new MemberVo();
        } else {
             System.out.println("세션에서 기존 memberVo 객체를 가져왔습니다.");
        }
        // 3. MemberVo 객체에 받아온 nationality 값 셋팅
        memberVo.setContry(nationality);
        System.out.println("세션 memberVo에 국가 정보 (" + nationality + ") 저장 완료");
        
        // 4. 업데이트된 memberVo를 세션에 다시 저장 (새로 만들었든 기존것을 가져왔든 다시 저장)
        session.setAttribute("memberVo", memberVo);
        
        
        // nationality에 따라 하단의 View 반환
        if ("kr".equals(nationality)) {
        	logger.info("Returning Korean registration form."); // kr 선택
            return "member/addMemberFormSelectKr"; // KR선택시 addMemberFormKr
        } else if ("jp".equals(nationality)) {
        	logger.info("Returning Japanese registration form."); // jp 선택
            return "member/addMemberFormSelectJp"; // JP선택시 addMemberFormJp
        } else {
        	logger.warn("Invalid nationality: " + nationality); // 잘못된 국적
            // 잘못된 국적 값이 넘어왔을 경우
            return "error/invalidNationality";
        }
    }
    
    // 랜덤 비밀번호 생성 메소드 (매개변수는 비밀번호의 길이)
    public static String GenerateRandomPassword(int len) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }
        String password = sb.toString();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    
	@Override//일반 회원가입메소드
	public ResponseEntity addMember(MemberVo member, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("회원가입 시도 : " + member); // 회원가입 시도 로그
		return null;
	}

	
	@Override//아이디 중복확인 메소드
	@RequestMapping(value = "/checkId.do", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Boolean>> checkId(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response) throws Exception {

		logger.debug("🔍 아이디 중복 체크 요청: " + id);

		boolean checkId = memberService.checkId(id); // 이름 바뀜!

		Map<String, Boolean> result = new HashMap<>();
		result.put("exists", checkId); // true면 이미 있음

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
}
