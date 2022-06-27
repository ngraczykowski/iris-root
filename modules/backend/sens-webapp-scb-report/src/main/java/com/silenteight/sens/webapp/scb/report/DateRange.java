package com.silenteight.sens.webapp.scb.report;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
class DateRange {

  private final OffsetDateTime from;
  private final OffsetDateTime to;
}
