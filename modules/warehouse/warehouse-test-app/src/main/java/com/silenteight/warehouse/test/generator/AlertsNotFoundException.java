package com.silenteight.warehouse.test.generator;

import java.time.Instant;

import static java.lang.String.format;

public class AlertsNotFoundException extends RuntimeException {

  AlertsNotFoundException(Instant dateFrom, Instant dateTo) {
    super(format("Cannot generate QaAlert for dateRange: [%s, %s]",
        dateFrom, dateTo));
  }
}