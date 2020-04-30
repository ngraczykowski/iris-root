package com.silenteight.auditing.bs;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nullable;

@RequiredArgsConstructor
public class AuditingLogger {

  private static final String INSERT_LOG_QUERY = "INSERT INTO audit"
      + " VALUES (:evid, :coid, :time, :type, :prnc, :enid, :encl, :enac, :detl)";

  @NonNull
  private final NamedParameterJdbcTemplate jdbcTemplate;

  @Setter
  @Nullable
  private TransactionTemplate transactionTemplate;

  public void log(AuditDataDto auditDataDto) {
    if (transactionTemplate != null)
      transactionTemplate.execute(status -> doLog(auditDataDto));
    else
      doLog(auditDataDto);
  }

  private int doLog(AuditDataDto auditDataDto) {
    MapSqlParameterSource paramSource = new MapSqlParameterSource()
        .addValue("evid", auditDataDto.getEventId())
        .addValue("coid", auditDataDto.getCorrelationId())
        .addValue("time", auditDataDto.getTimestamp())
        .addValue("type", auditDataDto.getType())
        .addValue("prnc", auditDataDto.getPrincipal())
        .addValue("enid", auditDataDto.getEntityId())
        .addValue("encl", auditDataDto.getEntityClass())
        .addValue("enac", auditDataDto.getEntityAction())
        .addValue("detl", auditDataDto.getDetails());

    return jdbcTemplate.update(INSERT_LOG_QUERY, paramSource);
  }
}
