package com.silenteight.agent.common.database.hibernate;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.google.common.base.Preconditions;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.slf4j.Marker;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;

import static org.slf4j.MarkerFactory.getDetachedMarker;

@Slf4j
public class SilentEightPhysicalNamingStrategy extends SpringPhysicalNamingStrategy {

  static final Marker INFRASTRUCTURE_MARKER = getDetachedMarker("INFRASTRUCTURE");

  private static final String SEQUENCE_SUFFIX = "_seq";
  private final String prefix;

  SilentEightPhysicalNamingStrategy(@NonNull String prefix) {
    Preconditions.checkArgument(!prefix.isBlank(), "Prefix must not be blank");

    this.prefix = prefix;

    log.info(
        INFRASTRUCTURE_MARKER, "Agent Physical Naming Strategy instantiated: prefix={}", prefix);
  }

  @Override
  public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment jdbcEnvironment) {
    var sequenceName = super.toPhysicalSequenceName(name, jdbcEnvironment);
    if (sequenceName == null) {
      return null;
    }

    return new Identifier(
        prefix + "_" + sequenceName.getText() + SEQUENCE_SUFFIX, sequenceName.isQuoted());
  }
}
