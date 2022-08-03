package com.wqddg.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

/**
*  EnableJpaRepositories 指定要自定义的
*/
@Configuration
@EnableJpaRepositories(
	basePackages = "com.wqddg.audit.dao",
	entityManagerFactoryRef = "ihrmEntityManager",
	transactionManagerRef = "ihrmTransactionManager"
)
public class JpaRepositoriesConfig {

	@Autowired
	private Environment env;

	@Autowired
	@Qualifier("ihrmDataSource")
	private DataSource ihrmDataSource;
	/**
	*工厂模式
	*/
	@Bean
	@Primary
	public LocalContainerEntityManagerFactoryBean ihrmEntityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(ihrmDataSource);
		em.setPackagesToScan(new String[] { "com.wqddg.audit.entity" });
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
		properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
		em.setJpaPropertyMap(properties);
		return em;
	}
	/**
	*事务管理
	*/
	@Primary
	@Bean
	public PlatformTransactionManager ihrmTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory( ihrmEntityManager().getObject());
		return transactionManager;
	}
}
