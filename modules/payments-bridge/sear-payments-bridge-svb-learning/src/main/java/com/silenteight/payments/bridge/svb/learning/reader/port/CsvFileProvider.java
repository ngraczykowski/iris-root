package com.silenteight.payments.bridge.svb.learning.reader.port;

import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningCsv;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningRequest;

import java.io.FileNotFoundException;

public interface CsvFileProvider {

  LearningCsv getLearningCsv(LearningRequest learningRequest) throws FileNotFoundException;
}
