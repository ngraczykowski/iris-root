package com.silenteight.serp.common.support.hibernate;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.google.common.base.Preconditions;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitCollectionTableNameSource;
import org.hibernate.boot.model.naming.ImplicitEntityNameSource;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;

import static com.silenteight.serp.common.logging.LogMarkers.INFRASTRUCTURE;

@Slf4j
public class SilentEightImplicitNamingStrategy extends SpringImplicitNamingStrategy {

  private static final long serialVersionUID = -4194289288977226811L;

  private final SilentEightImplicitNamingStrategyResolver resolver;

  SilentEightImplicitNamingStrategy(@NonNull String tablePrefix) {
    Preconditions.checkArgument(!tablePrefix.isBlank(), "Table name prefix must not be blank");

    this.resolver = new SilentEightImplicitNamingStrategyResolver(tablePrefix);

    log.info(
        INFRASTRUCTURE, "SERP Implicit Naming Strategy instantiated: tablePrefix={}", tablePrefix);
  }

  @Override
  public Identifier determinePrimaryTableName(ImplicitEntityNameSource source) {
    Identifier tableIdentifier = super.determinePrimaryTableName(source);
    String tableName = resolver.resolvePrimaryTableName(tableIdentifier.getText());
    return Identifier.toIdentifier(tableName, tableIdentifier.isQuoted());
  }

  @Override
  public Identifier determineCollectionTableName(ImplicitCollectionTableNameSource source) {
    Identifier tableIdentifier = super.determineCollectionTableName(source);
    String tableName = resolver.resolveCollectionTableName(tableIdentifier.getText());
    return Identifier.toIdentifier(tableName, tableIdentifier.isQuoted());
  }
}
