package com.byaffe.learningking.controllers;

import javax.ws.rs.ext.Provider;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Provider
public class CORSFilter implements javax.servlet.Filter {

    @Override
    public void destroy() {
        //enter code here
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        System.out.println("Inside org.pahappa.systems.web.controllers.CORSFilter.doFilter()...");

        HttpServletResponse resp = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;
//        resp.addHeader("Access-Control-Allow-Origin", "*");
//        resp.addHeader("Access-Control-Allow-Headers", "*");
//        resp.addHeader("Access-Control-Allow-Methods", "*");

        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET,PUT, OPTIONS, DELETE");
        resp.setHeader("Access-Control-Max-Age", "3600");
        resp.setHeader("Access-Control-Allow-Headers", "*");
        if ("OPTIONS".equals(req.getMethod())) {
            resp.setStatus(200);
            return;
         
        }
        chain.doFilter(request, resp);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //  throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
