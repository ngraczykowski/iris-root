package com.silenteight.payments.bridge.svb.newlearning.port;

import com.silenteight.payments.bridge.svb.newlearning.domain.ObjectPath;

import java.util.List;

public interface LearningDataAccess {

  List<String> saveNonProcessedFileNames(List<ObjectPath> names);
}
