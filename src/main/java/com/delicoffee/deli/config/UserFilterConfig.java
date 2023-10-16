package com.delicoffee.deli.config;

import com.delicoffee.deli.filter.UserFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserFilterConfig {

    @Bean
    public UserFilter userFilter(){
        return new UserFilter();
    }

    @Bean(name="userFilterConf")
    public FilterRegistrationBean UserFilterConfig(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(userFilter());
        filterRegistrationBean.addUrlPatterns("/cart/*");
        filterRegistrationBean.addUrlPatterns("/order/*");
        filterRegistrationBean.addUrlPatterns("/voucher/*");
        //filterRegistrationBean.addUrlPatterns("/user/update");
        filterRegistrationBean.setName("userFilterConf");
        return filterRegistrationBean;
    }
}
