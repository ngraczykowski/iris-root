package com.silenteight.payments.bridge.svb.learning.reader.port;

import com.silenteight.payments.bridge.common.resource.csv.file.provider.model.FileRequest;
import com.silenteight.payments.bridge.svb.learning.reader.domain.AlertsReadingResponse;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningCsv;

import java.util.function.Function;

public interface CsvFileProvider {

  AlertsReadingResponse getLearningCsv(
      FileRequest learningRequest,
      Function<LearningCsv, AlertsReadingResponse> csvConsumer);
}
