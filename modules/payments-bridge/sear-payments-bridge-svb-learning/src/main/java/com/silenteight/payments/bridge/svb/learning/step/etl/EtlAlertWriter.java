package com.silenteight.payments.bridge.svb.learning.step.etl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.learning.port.LearningDataAccess;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
@Service
class EtlAlertWriter implements ItemWriter<LearningProcessedAlert> {

  private final LearningDataAccess learningDataAccess;

  @SuppressWarnings("unchecked")
  @Override
  public void write(List<? extends LearningProcessedAlert> items) {
    log.info(
        "Writing processed alerts = {} with result = {}",
        items.stream().map(LearningProcessedAlert::getFkcoVSystemId).collect(
            toList()),
        items.stream().map(LearningProcessedAlert::getResult).collect(
            toList()));
    learningDataAccess.saveResult((List<LearningProcessedAlert>) items);
  }
}
