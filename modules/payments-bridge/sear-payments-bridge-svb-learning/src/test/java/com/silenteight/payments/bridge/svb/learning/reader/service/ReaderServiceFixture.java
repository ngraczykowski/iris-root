package com.silenteight.payments.bridge.svb.learning.reader.service;

import com.silenteight.payments.bridge.svb.learning.reader.domain.AlertMetaData;
import com.silenteight.payments.bridge.svb.learning.reader.domain.AnalystDecision;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import java.time.OffsetDateTime;
import java.util.List;

class ReaderServiceFixture {

  public static LearningAlertBatch createBatchAlertRequest() {
    var batch = new LearningAlertBatch(AlertMetaData.builder().build());
    batch.addLearningAlert(createLearningAlert(1));
    return batch;
  }

  public static LearningAlert createLearningAlert(int alertId) {
    return LearningAlert
        .builder()
        .alertId("alertId" + alertId)
        .systemId("systemId")
        .messageId("messageId")
        .alertTime(OffsetDateTime.now())
        .matches(List.of(createLearningMatch()))
        .analystDecision(AnalystDecision.builder().comment("git").build())
        .build();
  }

  public static LearningMatch createLearningMatch() {
    return LearningMatch.builder().build();
  }
}
