package com.silenteight.scb.ingest.adapter.incomming.common.indicator;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class SourceDetailsCleanerTest {

  @Test
  public void properCleanSourceDetails() {
    assertThat(SourceDetailsCleaner.clean(RecordSignValues.SOURCE_DETAILS)).isEqualTo(
        RecordSignValues.CLEANED_SOURCE_DETAILS);
  }
}
