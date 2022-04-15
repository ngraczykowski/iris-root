package com.silenteight.sens.webapp.audit.api.list;

import com.silenteight.sens.webapp.audit.api.dto.AuditLogDto;

import java.time.OffsetDateTime;
import java.util.Collection;

public interface ListAuditLogsQuery {

  Collection<AuditLogDto> list(OffsetDateTime from, OffsetDateTime to);
}
