package com.silenteight.serp.governance.ingest.listener;

import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.serp.governance.ingest.repackager.IngestDataToSolvedEventRepackagerService;
import com.silenteight.serp.governance.ingest.repackager.IngestDataValidator;
import com.silenteight.serp.governance.policy.solve.event.FeatureVectorEventStrategyService;
import com.silenteight.solving.api.v1.FeatureVectorSolvedEventBatch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.silenteight.serp.governance.ingest.IngestFixtures.*;
import static com.silenteight.serp.governance.policy.solve.event.FeatureVectorEventStrategy.INGEST;
import static com.silenteight.serp.governance.policy.solve.event.FeatureVectorEventStrategy.SOLVE;
import static org.assertj.core.api.Assertions.*;

class IngestDataHandlerTest {

  IngestDataHandler underTest;

  @Nested
  class IngestDataHandlerSolveStrategyTest {

    @BeforeEach
    void setUp() {
      var repackager = new IngestDataToSolvedEventRepackagerService(FEATURE_OR_CATEGORY_REGEX,
          PREFIX_AND_SUFFIX_REGEX, FV_SIGNATURE_KEY_NAME);
      var validator = new IngestDataValidator(FV_SIGNATURE_KEY_NAME);
      var fvEventStrategyService = new FeatureVectorEventStrategyService(INGEST);
      underTest = new IngestDataHandler(repackager, validator, fvEventStrategyService);
    }

    @Test
    void whenHandlingIngestRequest_returnsUsageEvents() {
      FeatureVectorSolvedEventBatch result = underTest.handle(getIngestRequest());

      assertThat(result.getEventsCount()).isEqualTo(ALERTS.size());
    }

    @Test
    void whenHandlingLearningRequest_returnsNull() {
      FeatureVectorSolvedEventBatch result = underTest.handle(getLearningRequest());

      assertThat(result).isNull();
    }
  }

  private ProductionDataIndexRequest getIngestRequest() {
    return ProductionDataIndexRequest
        .newBuilder()
        .addAllAlerts(ALERTS)
        .build();
  }

  private ProductionDataIndexRequest getLearningRequest() {
    return ProductionDataIndexRequest
        .newBuilder()
        .addAllAlerts(LEARNING_ALERTS)
        .build();
  }

  @Test
  void whenEventStrategySolve_returnsNull() {
    //given
    var repackager = new IngestDataToSolvedEventRepackagerService(FEATURE_OR_CATEGORY_REGEX,
        PREFIX_AND_SUFFIX_REGEX, FV_SIGNATURE_KEY_NAME);
    var validator = new IngestDataValidator(FV_SIGNATURE_KEY_NAME);
    var fvEventStrategyService = new FeatureVectorEventStrategyService(SOLVE);
    underTest = new IngestDataHandler(repackager, validator, fvEventStrategyService);
    //when
    FeatureVectorSolvedEventBatch result = underTest.handle(getIngestRequest());
    //then
    assertThat(result).isNull();
  }
}
