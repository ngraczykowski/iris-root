package com.silenteight.payments.bridge.svb.learning.step.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
@Slf4j
class ReservationQueryExecutor {

  private final JdbcTemplate jdbcTemplate;
  private final String query;
  private final Long jobId;
  private final String fileName;

  int reserve() {
    var result = jdbcTemplate.update(query, jobId, jobId, fileName, jobId);
    log.info(
        "Reservation has been accomplished with alerts count: {} for jobId: {}, fileName: {}",
        result, jobId, fileName);
    return result;
  }
}
