/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.configserver.dynamic.generator;

import lombok.RequiredArgsConstructor;

import com.silenteight.configserver.IrisDbProperties;
import com.silenteight.configserver.dynamic.IrisDynamicPropertiesGenerator;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static java.util.Map.entry;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(IrisDbProperties.class)
class Db implements IrisDynamicPropertiesGenerator {

  private final IrisDbProperties irisDbProperties;

  @Override
  public Map<String, Object> generate(String application, String profile, String label) {

    var schemaName = Optional.ofNullable(irisDbProperties.getSchema()).orElse(application);
    var dbUrl =
        "jdbc:postgresql://"
            + irisDbProperties.getHost()
            + ":"
            + irisDbProperties.getPort()
            + "/"
            + irisDbProperties.getName()
            + "?currentSchema="
            + schemaName
            + Optional.ofNullable(irisDbProperties.getOptions()).map(x -> "&" + x).orElse("");
    return Map.ofEntries(
        entry("spring.datasource.url", dbUrl),
        entry("spring.datasource.username", irisDbProperties.getUsername()),
        entry("spring.datasource.password", irisDbProperties.getPassword()),
        entry("spring.sql.init.mode", "never"),
        entry("spring.jpa.properties.hibernate.default_schema", schemaName),
        entry("spring.datasource.hikari.schema", schemaName),
        entry("spring.liquibase.default-schema", schemaName),
        entry("spring.liquibase.url", dbUrl),
        entry("spring.liquibase.user", irisDbProperties.getUsername()),
        entry("spring.liquibase.password", irisDbProperties.getPassword()),
        entry("spring.liquibase.liquibase-schema", schemaName));
  }
}
