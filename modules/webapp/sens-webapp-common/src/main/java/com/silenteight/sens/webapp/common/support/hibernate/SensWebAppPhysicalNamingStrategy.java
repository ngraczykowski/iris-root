package com.silenteight.sens.webapp.common.support.hibernate;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;

public class SensWebAppPhysicalNamingStrategy extends SpringPhysicalNamingStrategy {

  private static final String PREFIX = "webapp_";
  private static final String SEQUENCE_SUFFIX = "_seq";

  private final Logger log = LoggerFactory.getLogger(getClass());

  public SensWebAppPhysicalNamingStrategy() {
    log.info("SENS WebApp Physical Naming Strategy instantiated");
  }

  @Override
  public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment jdbcEnvironment) {
    Identifier sequenceName = super.toPhysicalSequenceName(name, jdbcEnvironment);
    if (sequenceName == null)
      return null;

    return new Identifier(PREFIX + sequenceName.getText() + SEQUENCE_SUFFIX,
        sequenceName.isQuoted());
  }
}
