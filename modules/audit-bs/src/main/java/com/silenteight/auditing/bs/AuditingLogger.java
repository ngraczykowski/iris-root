package com.silenteight.auditing.bs;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.audit.bs.api.v1.AuditData;
import com.silenteight.auditing.bs.amqp.AuditDataMessageGateway;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nullable;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static com.silenteight.protocol.utils.Uuids.fromJavaUuid;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class AuditingLogger {

  private static final String INSERT_LOG_QUERY = "INSERT INTO audit"
      + " VALUES (:evid, :coid, :time, :type, :prnc, :enid, :encl, :enac, :detl)";

  @NonNull
  private final NamedParameterJdbcTemplate jdbcTemplate;

  @NonNull
  private final AuditDataMessageGateway messageGateway;

  @Setter
  @Nullable
  private TransactionTemplate transactionTemplate;

  public void log(AuditDataDto auditDataDto) {
    if (transactionTemplate != null)
      transactionTemplate.execute(status -> logInDatabase(auditDataDto));
    else
      logInDatabase(auditDataDto);

    sendLogMessage(auditDataDto);
  }

  private int logInDatabase(AuditDataDto auditDataDto) {
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

  private void sendLogMessage(AuditDataDto auditDataDto) {
    messageGateway.send(toAuditData(auditDataDto));
  }

  private static AuditData toAuditData(AuditDataDto auditDataDto) {
    AuditData.Builder builder = AuditData.newBuilder()
        .setEventId(fromJavaUuid(auditDataDto.getEventId()))
        .setCorrelationId(fromJavaUuid(auditDataDto.getCorrelationId()))
        .setTimestamp(toTimestamp(auditDataDto.getTimestamp().toInstant()))
        .setType(auditDataDto.getType());

    ofNullable(auditDataDto.getPrincipal()).ifPresent(builder::setPrincipal);
    ofNullable(auditDataDto.getEntityId()).ifPresent(builder::setEntityId);
    ofNullable(auditDataDto.getEntityClass()).ifPresent(builder::setEntityClass);
    ofNullable(auditDataDto.getEntityAction()).ifPresent(builder::setEntityAction);
    ofNullable(auditDataDto.getDetails()).ifPresent(builder::setDetails);

    return builder.build();
  }
}
