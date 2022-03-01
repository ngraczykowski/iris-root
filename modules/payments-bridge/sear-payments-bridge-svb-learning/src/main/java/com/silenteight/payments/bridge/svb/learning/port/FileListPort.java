package com.silenteight.payments.bridge.svb.learning.port;

import com.silenteight.payments.bridge.common.resource.csv.file.provider.model.ObjectPath;

import java.util.List;

public interface FileListPort {

  List<ObjectPath> getFilesList();
}
