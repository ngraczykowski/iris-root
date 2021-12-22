package com.silenteight.payments.bridge.svb.newlearning.step.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
@Slf4j
class ReservationQueryExecutor {

  private final JdbcTemplate jdbcTemplate;
  private final String query;
  private final Long jobId;

  int reserve() {
    var result = jdbcTemplate.update(query, jobId);
    log.info("Reservation has been accomplished with alerts count: {} for jobId: {}", result,
        jobId);
    return result;
  }
}
