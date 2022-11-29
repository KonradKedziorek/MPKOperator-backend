package pl.kedziorek.mpkoperator.utils;

import javax.servlet.http.Cookie;

public class CookieUtils {
    public static Cookie buildCookie(int maxAge, boolean secure, boolean httpOnly, String path, String domain, String value, String name){
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(secure);
        cookie.setHttpOnly(httpOnly);
        cookie.setPath(path);
        cookie.setDomain(domain);
        return cookie;
    }
}
