package com.silenteight.auditing.bs;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.transaction.Transactional;

@RequiredArgsConstructor
public class AuditingLogger {

  private final NamedParameterJdbcTemplate jdbcTemplate;

  @Transactional
  public void log(AuditDataDto auditDataDto) {

    String insertLogSQL = "INSERT INTO audit"
        + " (event_id, correlation_id, timestamp, type, principal, entity_id, entity_class,"
        + "  entity_action, details) "
        + " VALUES (:evid, :coid, :time, :type, :prnc, :enid, :encl, :enac, :detl)";

    jdbcTemplate.update(insertLogSQL,
        new MapSqlParameterSource()
            .addValue("evid", auditDataDto.getEventId())
            .addValue("coid", auditDataDto.getCorrelationId())
            .addValue("time", auditDataDto.getTimestamp())
            .addValue("type", auditDataDto.getType())
            .addValue("prnc", auditDataDto.getPrincipal())
            .addValue("enid", auditDataDto.getEntityId())
            .addValue("encl", auditDataDto.getEntityClass())
            .addValue("enac", auditDataDto.getEntityAction())
            .addValue("detl", auditDataDto.getDetails()));
  }
}
