//package com.booster.vocabulary.config;
//
//import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.DispatcherServlet;
//
//import javax.servlet.Servlet;
//
//@Configuration
//public class DispatcherServletConfig {
//
//    @Bean
//    public DispatcherServlet dispatcherServlet() {
//        return new DispatcherServlet();
//    }
//
//    @Bean
//    public ServletRegistrationBean<Servlet> dispatcherServletRegistration() {
//        var registration = new ServletRegistrationBean<Servlet>(
//                dispatcherServlet(),
//                "/api"
//        );
//        registration.setName(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);
//        return registration;
//    }
//
//}
