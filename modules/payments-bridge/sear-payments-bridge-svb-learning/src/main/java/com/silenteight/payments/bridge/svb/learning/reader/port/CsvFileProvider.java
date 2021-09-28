package com.silenteight.payments.bridge.svb.learning.reader.port;

import com.silenteight.payments.bridge.svb.learning.reader.domain.AlertsReadingResponse;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningCsv;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningRequest;

import java.util.function.Function;

public interface CsvFileProvider {

  AlertsReadingResponse getLearningCsv(
      LearningRequest learningRequest,
      Function<LearningCsv, AlertsReadingResponse> csvConsumer);
}
