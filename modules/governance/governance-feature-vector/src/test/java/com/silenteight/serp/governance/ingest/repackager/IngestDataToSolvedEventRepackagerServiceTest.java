package com.silenteight.serp.governance.ingest.repackager;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.serp.governance.ingest.IngestFixtures;
import com.silenteight.solving.api.v1.FeatureVectorSolvedEvent;
import com.silenteight.solving.api.v1.FeatureVectorSolvedEventBatch;

import com.google.protobuf.ByteString;
import com.google.protobuf.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static com.silenteight.serp.governance.ingest.IngestFixtures.*;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

class IngestDataToSolvedEventRepackagerServiceTest {

  IngestDataToSolvedEventRepackagerService underTest;

  @BeforeEach
  void setUp() {
    underTest = new IngestDataToSolvedEventRepackagerService(
        IngestFixtures.FEATURE_OR_CATEGORY_REGEX,
        IngestFixtures.PREFIX_AND_SUFFIX_REGEX,
        FV_SIGNATURE_KEY_NAME);
  }

  @Test
  void activateShouldRepackIngestAlertsToFeatureVectorSolvedEventBatch() {
    //when
    FeatureVectorSolvedEventBatch featureVectorSolvedEventBatch = underTest.activate(ALERTS);
    //then
    assertThat(featureVectorSolvedEventBatch.getEventsCount()).isEqualTo(2);
    FeatureVectorSolvedEvent firstSolvedEvent = featureVectorSolvedEventBatch.getEvents(0);
    assertThat(firstSolvedEvent.getId()).isNotNull();
    assertThat(firstSolvedEvent.getCorrelationId()).isNotNull();
    assertThat(firstSolvedEvent.getFeatureVector().getFeatureValueList())
        .containsExactlyInAnyOrder(NO_DATA, MATCH, DATA_SOURCE_ERROR);
    assertThat(firstSolvedEvent.getFeatureCollection().getFeatureList())
        .containsExactlyInAnyOrder(FEATURE_NAME, FEATURE_GENDER, FEATURE_AP_TYPE);
    assertThat(firstSolvedEvent.getFeatureVectorSignature())
        .isEqualTo(getStringValueBytes(FV_SIGNATURE_KEY_FIRST));

    FeatureVectorSolvedEvent secondSolvedEvent = featureVectorSolvedEventBatch.getEvents(1);
    assertThat(secondSolvedEvent.getId()).isNotNull();
    assertThat(secondSolvedEvent.getCorrelationId()).isNotNull();
    assertThat(secondSolvedEvent.getFeatureVector().getFeatureValueList())
        .containsExactlyInAnyOrder(MATCH, MATCH, DATA_SOURCE_ERROR);
    assertThat(secondSolvedEvent.getFeatureCollection().getFeatureList())
        .containsExactlyInAnyOrder(FEATURE_NAME, FEATURE_GENDER, FEATURE_AP_TYPE);
    assertThat(secondSolvedEvent.getFeatureVectorSignature())
        .isEqualTo(getStringValueBytes(FV_SIGNATURE_KEY_SECOND));
  }

  private ByteString getStringValueBytes(String value) {
    return getValueFromString(value).getStringValueBytes();
  }

  @Test
  void activateShouldNotRepackLearningAlertToFeatureVectorSolvedEventBatch() {
    //given
    HashMap<String, Value> alertPayload = new HashMap<>();
    alertPayload.put("DN_CASE.ExtendedAttribute5", getValueFromString(IngestFixtures.SANCTION));
    List<Alert> alerts = of(getAlert(alertPayload));
    FeatureVectorSolvedEventBatch featureVectorSolvedEventBatch;
    //when
    featureVectorSolvedEventBatch = underTest.activate(alerts);
    //then
    assertThat(featureVectorSolvedEventBatch.getEventsCount()).isZero();
    assertThat(featureVectorSolvedEventBatch.getEventsList()).isEmpty();
  }
}
