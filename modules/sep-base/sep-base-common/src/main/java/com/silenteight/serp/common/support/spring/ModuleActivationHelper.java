package com.silenteight.serp.common.support.spring;

import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
class ModuleActivationHelper {

  private final ClassLoader classLoader;

  boolean hasDatabaseClasses() {
    return allClassesAvailable("com.zaxxer.hikari.HikariDataSource")
        && anyClassAvailable("org.postgresql.Driver", "org.h2.Driver",
                             "org.hsqldb.jdbc.JDBCDriver");
  }

  boolean hasMessagingClasses() {
    return allClassesAvailable("com.rabbitmq.client.ConnectionFactory");
  }

  boolean hasBatchClasses() {
    return allClassesAvailable("org.springframework.batch.core.repository.dao.JdbcJobInstanceDao");
  }

  private boolean anyClassAvailable(String... classNames) {
    return Stream.of(classNames).anyMatch(this::isClassAvailable);
  }

  private boolean allClassesAvailable(String... classNames) {
    return Stream.of(classNames).allMatch(this::isClassAvailable);
  }

  private boolean isClassAvailable(String className) {
    try {
      classLoader.loadClass(className);
      return true;
    } catch (ClassNotFoundException e) {
      return false;
    }
  }
}
