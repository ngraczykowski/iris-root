
package com.silenteight.payments.bridge.svb.newlearning.port;

import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningRequest;
import com.silenteight.payments.bridge.svb.newlearning.domain.DeleteLearningFileRequest;
import com.silenteight.payments.bridge.svb.newlearning.domain.ObjectPath;

import org.springframework.core.io.Resource;

import java.util.List;

public interface CsvFileResourceProvider {

  Resource getResource(LearningRequest learningRequest);

  void deleteLearningFile(DeleteLearningFileRequest deleteLearningFileRequest);

  List<ObjectPath> getFilesList();
}
