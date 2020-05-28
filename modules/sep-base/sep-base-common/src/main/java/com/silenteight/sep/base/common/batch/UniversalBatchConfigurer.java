package com.silenteight.sep.base.common.batch;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.DefaultJpaDialect;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Optional;
import javax.annotation.Nullable;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import static java.util.Optional.empty;
import static java.util.Optional.of;

/**
 * {@link org.springframework.batch.core.configuration.annotation.BatchConfigurer} implementation
 * that properly configures the {@link JpaTransactionManager} to use the {@link HibernateJpaDialect}
 * for two reasons:
 *
 * <ul>
 *   <li>transaction isolation level support,</li>
 *   <li>interoperability between Hibernate ORM and plain JDBC database access.</li>
 * </ul>
 */
@Slf4j
public class UniversalBatchConfigurer extends BasicBatchConfigurer {

  private final DataSource dataSource;

  @Nullable
  private final EntityManagerFactory entityManagerFactory;

  /**
   * Create a new {@link UniversalBatchConfigurer} instance.
   *
   * @param properties
   *     the batch properties
   * @param dataSource
   *     the underlying data source
   * @param transactionManagerCustomizers
   *     transaction manager customizers (or {@code null})
   * @param entityManagerFactory
   *     the entity manager factory (or {@code null})
   */
  public UniversalBatchConfigurer(
      BatchProperties properties,
      DataSource dataSource,
      @Nullable TransactionManagerCustomizers transactionManagerCustomizers,
      @Nullable EntityManagerFactory entityManagerFactory) {
    super(properties, dataSource, transactionManagerCustomizers);
    this.dataSource = dataSource;
    this.entityManagerFactory = entityManagerFactory;
  }

  @Override
  protected String determineIsolationLevel() {
    String isolationLevel = getJpaDialect()
        .map(UniversalBatchConfigurer::mapToIsolationLevel)
        .orElse(null);

    if (isolationLevel != null)
      log.warn("Default JPA dialect does not support custom isolation levels");

    return isolationLevel;
  }

  @Nullable
  private static String mapToIsolationLevel(JpaDialect jpaDialect) {
    // NOTE(ahaczewski): Check for exactly DefaultJpaDialect. Subclasses support isolation levels.
    if (jpaDialect.getClass().isAssignableFrom(DefaultJpaDialect.class))
      return "ISOLATION_DEFAULT";

    return null;
  }

  @Override
  protected PlatformTransactionManager createTransactionManager() {
    return getJpaDialect()
        .map(this::createJpaTransactionManager)
        .orElseGet(this::createDataSourceTransactionManager);
  }

  private Optional<JpaDialect> getJpaDialect() {
    if (entityManagerFactory == null)
      return empty();

    JpaDialect jpaDialect = null;
    if (entityManagerFactory instanceof EntityManagerFactoryInfo) {
      EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) entityManagerFactory;
      jpaDialect = info.getJpaDialect();
    }

    if (jpaDialect == null)
      return of(new DefaultJpaDialect());

    return of(jpaDialect);
  }

  private PlatformTransactionManager createJpaTransactionManager(JpaDialect jpaDialect) {
    JpaTransactionManager transactionManager = new JpaTransactionManager();

    transactionManager.setDataSource(dataSource);
    transactionManager.setEntityManagerFactory(entityManagerFactory);
    transactionManager.setJpaDialect(jpaDialect);

    return transactionManager;
  }

  private PlatformTransactionManager createDataSourceTransactionManager() {
    return new DataSourceTransactionManager(dataSource);
  }
}
