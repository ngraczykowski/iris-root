package com.silenteight.sens.webapp.common.database;

import com.silenteight.sens.webapp.common.support.hibernate.NamingConventionConfiguration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@ConditionalOnClass(name = "com.zaxxer.hikari.HikariDataSource")
@Import(NamingConventionConfiguration.class)
public class DataSourceAutoConfiguration {

}
