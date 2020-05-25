package com.silenteight.serp.common.batch;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Configuration of {@link UniversalBatchConfigurer}.
 *
 * <p>because auto-configuration does not work for BatchConfigurers due to ordering, please
 * import it into your {@code @Configuration} class:
 *
 * <blockquote><pre>
 *   &#x40;Configuration
 *   &#x40;EnableBatchProcessing
 *   &#x40;Import(HibernateBatchConfigurerConfiguration.class)
 *   class BatchJobConfiguration {
 *     // ...
 *   }
 * </pre></blockquote>
 */
@Configuration
public class DefaultBatchConfigurerConfiguration {

  @Bean
  public UniversalBatchConfigurer batchConfigurer(
      BatchProperties properties,
      DataSource dataSource,
      ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers,
      EntityManagerFactory entityManagerFactory) {

    return new UniversalBatchConfigurer(
        properties,
        dataSource,
        transactionManagerCustomizers.getIfAvailable(),
        entityManagerFactory);
  }
}
