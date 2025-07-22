# 🌏 Harunichi | 한일 소셜미디어 웹사이트 제작 프로젝트

Java Spring Framework 기반으로 개발된 한일 소셜 커뮤니티 플랫폼입니다.  
한국과 일본 사용자가 함께 소통하고, 중고거래 및 실시간 채팅, 다국어 기능을 사용할 수 있는  
글로벌 소셜 미디어 웹사이트입니다.

---

## 📝 프로젝트 개요

- **프로젝트명**: Harunichi (한일 소셜미디어 웹사이트)
- **개발 기간**: 2025.05.26 ~ 2025.06.26 (1개월)
- **팀원 수**: 4명

---

## 🖥 사용 언어 및 개발 환경

### 👀 Frontend
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![JSP](https://img.shields.io/badge/JSP-007396?style=for-the-badge&logo=java&logoColor=white)
![JSTL](https://img.shields.io/badge/JSTL-003B57?style=for-the-badge)

### 🧠 Backend
![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Framework](https://img.shields.io/badge/Spring_Framework-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![MyBatis](https://img.shields.io/badge/MyBatis-ED8B00?style=for-the-badge)

### 🛢 Database
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

### 🧰 개발 도구
![STS4](https://img.shields.io/badge/STS4-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Apache Tomcat](https://img.shields.io/badge/Tomcat-005571?style=for-the-badge&logo=apachetomcat&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)

---

## 📦 DB 구성

아래는 프로젝트에서 사용된 테이블 간의 ERD(Entity Relationship Diagram)입니다.  
<img width="962" height="741" alt="ERD" src="https://github.com/user-attachments/assets/dcc257dc-96d3-430c-bdc2-7d31e82e628b" />

---

## 👩‍💻 담당 업무 (윤지원)

- **Tiles 기반 공통 레이아웃 설계**  
  `layout.jsp`, `header.jsp`, `footer.jsp` 등 재사용 가능한 구조로 구성  
  <img width="1372" height="769" src="https://github.com/user-attachments/assets/8a3d143b-5436-43aa-abce-e9618ca73f22" />

- **회원 기능 구현**  
  로그인, 로그아웃, 회원가입, 정보 수정 등  
  Spring MVC + MyBatis + JSP 연동  
  <img width="1370" height="765" src="https://github.com/user-attachments/assets/f4094220-1a75-4819-abe1-20ebcbc85a66" />
  <img width="1375" height="770" src="https://github.com/user-attachments/assets/14765f9f-d2f2-4738-9ce4-4e568e34f022" />

- **다국어 UX 흐름 개선**  
  언어 조건 분기 및 세션 기반 출력 최적화

- **번역 기능 구현**  
  선택 언어에 따라 콘텐츠 실시간 변환  
  Google Cloud Translation API 연동 (한국어 ⇄ 일본어)  
  <img width="1316" height="623" src="https://github.com/user-attachments/assets/bdbf5f39-04cd-4447-b646-16a1a9f2dbc6" />

- **마이페이지 구현**  
  사용자 정보 조회, 수정 및 선호 설정 등  
  <img width="1362" height="766" src="https://github.com/user-attachments/assets/750e0a94-58b6-404a-8485-9cb1438e96fd" />

- **팔로우 / 언팔로우 기능**  
  DB 연동 및 AJAX 기반 비동기 처리  
  <img width="1369" height="765" src="https://github.com/user-attachments/assets/511a679b-2cb8-42e4-b7e6-c5b5d85a01e9" />

- **관리자 대시보드 설계 및 구현**  
  회원 목록 및 상태 관리 UI  
  <img width="1362" height="767" src="https://github.com/user-attachments/assets/a37c7c67-de88-4689-a473-87eb4d6f23e4" />

- **소셜 로그인 기능 구현**  
  OAuth 2.0 기반 카카오 로그인 API, 네이버 로그인 API 연동  
  사용자 정보 수신 및 자동 로그인 처리  
  <img width="1366" height="762" src="https://github.com/user-attachments/assets/9fdc07cd-7673-4233-a39e-7ee2fe0ebc3e" />

---

