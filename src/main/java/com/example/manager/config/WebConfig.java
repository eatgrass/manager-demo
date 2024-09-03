package com.example.manager.config;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.manager.dao.UserRepository;
import com.example.manager.model.Role;
import com.example.manager.security.AuthorizationFilter;
import com.example.manager.security.Authorizer;
import com.example.manager.utils.StringUtils;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<AuthorizationFilter> adminAuthFilter(@Autowired UserRepository userRepository) {
        FilterRegistrationBean<AuthorizationFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new AuthorizationFilter(new Authorizer(userRepository, Role.admin)));
        bean.addUrlPatterns("/admin/*");
        return bean;
    }

    @Bean
    public FilterRegistrationBean<AuthorizationFilter> userAuthFilter(
            @Autowired UserRepository userRepository

    ) {
        FilterRegistrationBean<AuthorizationFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new AuthorizationFilter(new Authorizer(userRepository, Role.user)));
        bean.addUrlPatterns("/user/*");
        return bean;
    }

    @Bean
    public File dbFile() throws IOException {

        String dbFilePath = System.getenv("APP_DB_FILE_PATH");

        if (StringUtils.isEmpty(dbFilePath)) {
            dbFilePath = "./data/db";
        }

        File dbFile = new File(dbFilePath);
        if (!dbFile.exists()) {
            try {
                dbFile.getParentFile().mkdirs();
                dbFile.createNewFile();
            } catch (IOException e) {
                throw e;
            }
        }
        return dbFile;
    }

}
