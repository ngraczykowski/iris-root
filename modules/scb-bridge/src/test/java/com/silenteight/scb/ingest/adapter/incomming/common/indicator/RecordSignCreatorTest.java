package com.silenteight.scb.ingest.adapter.incomming.common.indicator;

import org.junit.Test;

import static com.silenteight.scb.ingest.adapter.incomming.common.indicator.RecordSignCreator.fromRecord;
import static com.silenteight.scb.ingest.adapter.incomming.common.indicator.RecordSignCreator.fromSourceDetails;
import static org.assertj.core.api.Assertions.*;

public class RecordSignCreatorTest {

  @Test
  public void givenSourceDetailAndRecord_singsEqual() {
    String sourceDetailsSign = fromSourceDetails(RecordSignValues.SOURCE_DETAILS);
    String recordSign = fromRecord(RecordSignValues.RECORD, '~');

    assertThat(sourceDetailsSign).isEqualTo(recordSign);
  }

  @Test
  public void simpleCase_signsEqual() {
    String sourceDetailsSign = fromSourceDetails("source_details");
    String recordSign = fromRecord("source_details", '~');

    assertThat(sourceDetailsSign).isEqualTo(recordSign);
  }

  @Test
  public void emptyValues_signsEqual() {
    String sourceDetailsSign = fromSourceDetails("source_details");
    String recordSign = fromRecord("source_details", '~');

    assertThat(sourceDetailsSign).isEqualTo(recordSign);
  }

  @Test
  public void differentValues_signsNotEqual() {
    String sourceDetailsSign = fromSourceDetails("first");
    String recordSign = fromRecord("second", '~');

    assertThat(sourceDetailsSign).isNotEqualTo(recordSign);
  }
}