package com.wqddg.audit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import javax.sql.DataSource;

/**
*@ConfigurationProperties 配置源的数据
*@Primary默认配置
* Qualifier 设置名称 使用时候要确定
*/
@Configuration
public class AuditDatasourceConfig  {
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.act")
    @Qualifier("activitiDataSource")
    public DataSource activitiDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.ihrm")
    @Qualifier("ihrmDataSource")
    public DataSource ihrmDataSource() {
        return DataSourceBuilder.create().build();
    }
}
