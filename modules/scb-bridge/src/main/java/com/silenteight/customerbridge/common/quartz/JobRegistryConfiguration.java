package com.silenteight.customerbridge.common.quartz;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobRegistryConfiguration {

  @Bean
  JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(
      JobRegistry jobRegistry,
      BeanFactory beanFactory) {

    JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
    jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
    jobRegistryBeanPostProcessor.setBeanFactory(beanFactory);
    return jobRegistryBeanPostProcessor;
  }
}
