package com.wqddg.salarys;

import com.wqddg.common.utils.IdWorker;
import com.wqddg.common.utils.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

//springboot的注解扫描
@SpringBootApplication(scanBasePackages = "com.wqddg")
//配置jpa注解的扫描
@EntityScan(value = "com.wqddg.domain")
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
public class SalarysApplication {
    /**
     * 启动方法
     */
    public static void main(String[] args) {
        SpringApplication.run(SalarysApplication.class, args);
    }

    @Bean
    public JwtUtils getJwtUtils(){
        return new JwtUtils();
    }

    @Bean
    public IdWorker idWorker() {
        return new IdWorker();
    }
}
