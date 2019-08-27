package com.jh;

//import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import com.jh.controller.LoginHandlerInterceptor;
import com.jh.controller.WebConfigurer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

@SpringBootApplication(
//        exclude = {
//        org.activiti.spring.boot.SecurityAutoConfiguration.class,

//        exclude={DataSourceAutoConfiguration.class}*/ ,
        exclude = {
//                DataSourceAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.activiti.spring.boot.SecurityAutoConfiguration.class,
                //org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration.class
//                PageHelperAutoConfiguration.class
        }
)
@MapperScan("com.jh.dao")
public class ProjectApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    @Override//为了打包springboot项目
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder) {
        return builder.sources(ProjectApplication.class);
    }

}
