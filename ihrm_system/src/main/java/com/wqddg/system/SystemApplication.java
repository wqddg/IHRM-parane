package com.wqddg.system;

import com.wqddg.common.utils.IdWorker;
import com.wqddg.common.utils.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

/**
 * @Author: wqddg
 * @ClassName SystemApplication
 * @DateTime: 2022/1/5 21:40
 * @remarks : #
 */
//springboot的注解扫描
@SpringBootApplication(scanBasePackages = "com.wqddg")
//配置jpa注解的扫描
@EntityScan(value = "com.wqddg.domain.system")
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class,args);
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
}
