package com.silenteight.sens.webapp.common.support.hibernate;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitEntityNameSource;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;

@Slf4j
public class SensWebAppImplicitNamingStrategy extends SpringImplicitNamingStrategy {

  private static final long serialVersionUID = -4194289288977226811L;
  private static final String TABLE_PREFIX = "Webapp";

  public SensWebAppImplicitNamingStrategy() {
    log.info("SENS WebApp Implicit Naming Strategy instantiated");
  }

  @Override
  public Identifier determinePrimaryTableName(ImplicitEntityNameSource source) {
    Identifier tableIdentifier = super.determinePrimaryTableName(source);
    String tableName = TABLE_PREFIX + removeEntitySuffix(tableIdentifier.getText());
    return Identifier.toIdentifier(tableName, tableIdentifier.isQuoted());
  }

  private static String removeEntitySuffix(String entityName) {
    int indexOfEntity = entityName.toLowerCase().lastIndexOf("entity");
    return indexOfEntity >= 0 ? entityName.substring(0, indexOfEntity) : entityName;
  }
}
