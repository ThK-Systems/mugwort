/*
 * tksCommons / mugwort
 *
 * Author : Thomas Kuhlmann (ThK-Systems, http://www.thk-systems.de) License : LGPL (https://www.gnu.org/licenses/lgpl.html)
 */

package de.thksystems.util.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ServletUtils {

    private static final String DEFAULT_SEPARATOR = ",";

    /**
     * Get http params as comma-separated string-list.
     */
    public static String getHttpParamsAsString(ServletRequest request) {
        ArrayList<String> paramList = new ArrayList<>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramKey = paramNames.nextElement();
            String paramValue = request.getParameter(paramKey);
            paramList.add(String.format("'%s'='%s'", paramKey, paramValue));
        }
        return StringUtils.join(paramList, DEFAULT_SEPARATOR);
    }

    /**
     * Get http headers as comma-separated string-list.
     */
    public static String getHttpHeadersAsString(HttpServletRequest request) {
        ArrayList<String> headerList = new ArrayList<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames != null && headerNames.hasMoreElements()) {
            String headerKey = headerNames.nextElement();
            String headerValue = request.getHeader(headerKey);
            headerList.add(String.format("'%s'='%s'", headerKey, headerValue));
        }
        return StringUtils.join(headerList, DEFAULT_SEPARATOR);
    }

    /**
     * Get http cookies as comma-separated string-list.
     */
    public static String getHttpCookiesAsString(HttpServletRequest request) {
        ArrayList<String> cookieList = new ArrayList<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookieList.add(ToStringBuilder.reflectionToString(cookie));
            }
            return StringUtils.join(cookieList, DEFAULT_SEPARATOR);
        } else {
            return "";
        }
    }

    /**
     * Returns the whole servlet (Params, Headers, Cookies, Body) dump
     */
    public static String getServletDump(HttpServletRequest request) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SERVLET REQUEST").append(IOUtils.LINE_SEPARATOR);
            sb.append(String.format(" FROM %s (%s:%d", request.getRemoteHost(), request.getRemoteAddr(), request.getRemotePort()));
            sb.append(String.format(" TO %s (%s:%d)", request.getLocalName(), request.getLocalAddr(), request.getLocalPort()));
            sb.append(IOUtils.LINE_SEPARATOR);
            sb.append("  URL: ").append(request.getPathInfo()).append(IOUtils.LINE_SEPARATOR).append(IOUtils.LINE_SEPARATOR);
            getServletDumpAddList(sb, "HEADERS", getHttpHeadersAsString(request).split(DEFAULT_SEPARATOR));
            getServletDumpAddList(sb, "PARAMETERS", getHttpParamsAsString(request).split(DEFAULT_SEPARATOR));
            getServletDumpAddList(sb, "COOKIES", getHttpCookiesAsString(request).split(DEFAULT_SEPARATOR));
            List<String> bodyReader = IOUtils.readLines(request.getReader());
            getServletDumpAddList(sb, "BODY", bodyReader.toArray(new String[bodyReader.size()]));
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getServletDumpAddList(StringBuilder sb, String capitol, String[] elements) {
        sb.append("  ").append(capitol).append(IOUtils.LINE_SEPARATOR);
        for (String element : elements) {
            sb.append("    ").append(element).append(IOUtils.LINE_SEPARATOR);
        }
        sb.append(IOUtils.LINE_SEPARATOR);
    }
}
