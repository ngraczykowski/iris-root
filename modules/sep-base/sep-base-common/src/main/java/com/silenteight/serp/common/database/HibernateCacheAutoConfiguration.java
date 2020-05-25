package com.silenteight.serp.common.database;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(name = "org.hibernate.SessionFactory")
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class HibernateCacheAutoConfiguration {

  @Configuration
  @ConditionalOnClass(name = {
      "org.hibernate.cache.jcache.JCacheHelper",
      "com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider"
  })
  @AutoConfigureBefore(
      name = "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration")
  public static class CaffeineJCache {
    @Bean
    HibernatePropertiesCustomizer caffeineSecondLevelCacheHibernateCustomizer() {
      return hibernateProperties -> {
        hibernateProperties.put(
            "hibernate.cache.region.factory_class",
            "jcache");
        hibernateProperties.put(
            "hibernate.javax.cache.provider",
            "com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider");
        hibernateProperties.put("hibernate.cache.use_second_level_cache", Boolean.TRUE);
      };
    }
  }
}
