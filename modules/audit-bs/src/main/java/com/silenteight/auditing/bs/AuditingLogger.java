package com.silenteight.auditing.bs;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
public class AuditingLogger {

  private final JdbcTemplate jdbcTemplate;

  public void log(AuditDataDto auditDataDto) {

  }
}
