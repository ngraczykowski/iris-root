
package com.silenteight.payments.bridge.svb.newlearning.port;

import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningRequest;

import org.springframework.core.io.Resource;

public interface CsvFileResourceProvider {

  Resource getResource(LearningRequest learningRequest);
}
