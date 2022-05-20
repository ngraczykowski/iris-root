package com.silenteight.payments.bridge.svb.learning.step.remove.csvrow;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.learning.port.LearningDataAccess;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
class RemoveCsvRowDataWriter implements ItemWriter<Long> {

  private final LearningDataAccess learningDataAccess;

  @Override
  public void write(List<? extends Long> items) throws Exception {
    learningDataAccess.removeCsvRows(Collections.unmodifiableList(items));
  }
}
