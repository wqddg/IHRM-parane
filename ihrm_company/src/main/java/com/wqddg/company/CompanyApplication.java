package com.wqddg.company;

import com.wqddg.common.utils.IdWorker;
import com.wqddg.common.utils.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

/**
 * @Author: wqddg
 * @ClassName ConpanyApplication
 * @DateTime: 2022/1/3 22:36
 * @remarks : #
 */
//springboot的注解扫描
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "com.wqddg")
//配置jpa注解的扫描
@EntityScan(value = "com.wqddg.domain.company")
public class CompanyApplication {
    public static void main(String[] args) {
        SpringApplication.run(CompanyApplication.class,args);
    }

    @Bean
    public IdWorker getIdWork(){
        return new IdWorker();
    }

    @Bean
    public JwtUtils getJwtUtils(){
        return new JwtUtils();
    }
}
