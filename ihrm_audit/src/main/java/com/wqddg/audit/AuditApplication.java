package com.wqddg.audit;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.wqddg.common.utils.IdWorker;
import com.wqddg.common.utils.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

/**
 * @Author: wqddg
 * @ClassName SystemApplication
 * @DateTime: 2022/1/5 21:40
 * @remarks : #
 */
//springboot的注解扫描
@SpringBootApplication(scanBasePackages = "com.wqddg",exclude ={org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class} )
//配置jpa注解的扫描
@EntityScan(value = "com.wqddg.domain")
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class
})
public class AuditApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuditApplication.class,args);
    }

    @Bean
    public IdWorker getIdWork(){
        return new IdWorker();
    }

    @Bean
    public JwtUtils getJwtUtils(){
        return new JwtUtils();
    }
    /**
     * 解决 no   session
     * @return
     */
    @Bean
    public OpenEntityManagerInViewFilter openEntityManagerInViewFilter(){
        return new OpenEntityManagerInViewFilter();
    }


    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        FastJsonHttpMessageConverter fjc = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.PrettyFormat);
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("identityLinks");
        config.setSerializeFilters(filter);
        fjc.setFastJsonConfig(config);
        HttpMessageConverter<?> converter = fjc;
        return new HttpMessageConverters(converter);
    }
}
