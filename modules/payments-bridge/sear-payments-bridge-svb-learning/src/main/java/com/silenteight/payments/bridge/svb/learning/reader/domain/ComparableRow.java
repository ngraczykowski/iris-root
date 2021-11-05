package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;
import javax.annotation.Nonnull;

@Value
@Builder
public class ComparableRow implements Comparable<ComparableRow> {

  LearningCsvRow learningCsvRow;

  OffsetDateTime actionDateTime;

  @Override
  public int compareTo(@Nonnull ComparableRow row) {
    return -actionDateTime.compareTo(row.actionDateTime);
  }
}
