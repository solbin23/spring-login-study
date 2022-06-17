package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whiteList = {"/","/members/add","/login","/logout","/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            log.info("인증 체크 필터 시작{}" , requestURI);

            if (isLoginCheckPath(requestURI)){ //로그인을 체크하는 경로 ,whiteList면 이 경로를 타지 않음
                  log.info("인증 체크 로직 실행 {}" ,requestURI);
                HttpSession session = httpRequest.getSession(false);//session에 데이터가 들어와있는지 보고
                if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) { //세션이 없거나 로그인 한 흔적이 없다면?
                    log.info("미인증 사용자 요청 {}", requestURI);
                    //로그인으로 redirect
                    httpResponse.sendRedirect("/login?redirectURL = " + requestURI);
                    return; //미인증 사용자는 다음으로 진행하지 않고 끝!
                }
            }
            chain.doFilter(request, response);
        }catch (Exception e) {
            throw e; //예외 로깅 가능하지만, 톰캣까지 예외를 보내주어야 함
        }finally{
            log.info("인증 체크 필터 종료 {}" ,requestURI);
        }
    }


    /**
     * 화이트 리스트의 경우 인증 체크 X
     */
    private boolean isLoginCheckPath(String requestURI){
        return !PatternMatchUtils.simpleMatch(whiteList,requestURI); //whiteList 와 requestURI 가 매칭 되는가?
    }
}
