package com.silenteight.sens.webapp.common.database;

import com.silenteight.sens.webapp.common.audit.AuditRevision;
import com.silenteight.sens.webapp.common.support.hibernate.NamingConventionConfiguration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnClass(name = "org.hibernate.SessionFactory")
@Import(NamingConventionConfiguration.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EntityScan(basePackageClasses = AuditRevision.class)
public class HibernateAutoConfiguration {
}
