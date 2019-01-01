package com.minhao.nov.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CookieUtil {


    private static final String COOKIE_DOMIN=".happymmall.com";

    private static final String COOKIE_NAME="mmall_login_token";



    public static String readToken(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies!=null){
            for (Cookie c :
                    cookies) {
                log.info("cookieName:{},cookieValue:{}",c.getName(),c.getValue());
                if (StringUtils.equals(c.getName(),COOKIE_NAME)){
                    log.info("return cookieName:{},cookieValue:{}",c.getName(),c.getValue());
                    return c.getValue();
                }
            }
        }
        return null;
    }





    public static void writeLoginToken(HttpServletResponse response, String token){

        Cookie ck=new Cookie(COOKIE_NAME,token);
        ck.setDomain(COOKIE_DOMIN);
        ck.setPath("/");
        ck.setHttpOnly(true);
        ck.setMaxAge(60*60*24*365);//永久

        log.info("write cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());


        response.addCookie(ck);


    }


    public static void delToken(HttpServletRequest request,HttpServletResponse response){
        Cookie[] ck=request.getCookies();
        if (ck!=null){
            for (Cookie c :
                    ck) {
                if (StringUtils.equals(c.getName(),COOKIE_NAME)){
                    c.setDomain(COOKIE_DOMIN);
                    c.setPath("/");
                    c.setMaxAge(0);//代表删除cookie
                    log.info("del cookieName:{},cookieValue:{}",c.getName(),c.getValue());
                    response.addCookie(c);
                    return;
                }

            }
        }
    }























}
