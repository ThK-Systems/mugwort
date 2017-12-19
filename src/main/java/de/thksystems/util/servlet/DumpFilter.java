/*
 * tksCommons / mugwort
 *
 * Author : Thomas Kuhlmann (ThK-Systems, http://www.thk-systems.de) License : LGPL (https://www.gnu.org/licenses/lgpl.html)
 */
package de.thksystems.util.servlet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * This filter dumps everything to {@link System#out} using {@link ServletUtils#getServletDump(HttpServletRequest)}.
 */
public class DumpFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        if (request instanceof HttpServletRequest) {
            System.out.println(ServletUtils.getServletDump((HttpServletRequest) request));
        }
    }

    @Override
    public void destroy() {
    }

}
