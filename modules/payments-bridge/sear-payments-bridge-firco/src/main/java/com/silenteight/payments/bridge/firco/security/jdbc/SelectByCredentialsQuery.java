package com.silenteight.payments.bridge.firco.security.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class SelectByCredentialsQuery {

  private final JdbcTemplate jdbcTemplate;

  @Language("PostgreSQL")
  private static final String SQL = "SELECT count(*) >= 1\n"
      + "FROM pb_credentials\n"
      + "WHERE username = ?\n"
      + "AND password = ?";

  boolean execute(String userName, String password) {
    return jdbcTemplate.queryForObject(SQL, Boolean.class, userName, password);
  }
}
