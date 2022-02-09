
package com.silenteight.payments.bridge.common.resource.csv.file.provider.port;

import com.silenteight.payments.bridge.common.resource.csv.file.provider.model.DeleteLearningFileRequest;
import com.silenteight.payments.bridge.common.resource.csv.file.provider.model.FileRequest;
import com.silenteight.payments.bridge.common.resource.csv.file.provider.model.ObjectPath;

import org.springframework.core.io.Resource;

import java.util.List;

public interface CsvFileResourceProvider {

  Resource getResource(FileRequest fileRequest);

  void deleteLearningFile(DeleteLearningFileRequest deleteLearningFileRequest);

  List<ObjectPath> getFilesList();
}
