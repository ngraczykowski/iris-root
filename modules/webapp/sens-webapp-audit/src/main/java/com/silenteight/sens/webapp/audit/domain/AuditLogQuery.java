package com.silenteight.sens.webapp.audit.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.api.dto.AuditLogDto;
import com.silenteight.sens.webapp.audit.api.list.ListAuditLogsQuery;

import java.time.OffsetDateTime;
import java.util.Collection;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
class AuditLogQuery implements ListAuditLogsQuery {

  @NonNull
  private final AuditLogRepository repository;

  @Override
  public Collection<AuditLogDto> list(OffsetDateTime from, OffsetDateTime to) {
    return repository
        .findAllByTimestampBetween(from, to)
        .stream()
        .map(AuditLog::toDto)
        .collect(toList());
  }
}
