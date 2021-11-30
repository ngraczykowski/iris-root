
package com.silenteight.payments.bridge.svb.newlearning.port;

import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningRequest;
import com.silenteight.payments.bridge.svb.newlearning.domain.DeleteLearningFileRequest;

import org.springframework.core.io.Resource;

public interface CsvFileResourceProvider {

  Resource getResource(LearningRequest learningRequest);

  void deleteLearningFile(DeleteLearningFileRequest deleteLearningFileRequest);
}
