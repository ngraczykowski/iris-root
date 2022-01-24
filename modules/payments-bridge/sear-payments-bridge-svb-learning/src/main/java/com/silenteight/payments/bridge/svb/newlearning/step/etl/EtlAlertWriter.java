package com.silenteight.payments.bridge.svb.newlearning.step.etl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
class EtlAlertWriter implements ItemWriter<LearningProcessedAlert> {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public void write(List<? extends LearningProcessedAlert> items) {
    log.info("Mocking writing processed alert");
  }
}
