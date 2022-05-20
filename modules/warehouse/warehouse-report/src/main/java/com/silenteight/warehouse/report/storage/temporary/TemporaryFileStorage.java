package com.silenteight.warehouse.report.storage.temporary;

import java.util.function.Consumer;

public interface TemporaryFileStorage {

  void doOnTempFile(String name, Consumer<FileStorage> fileConsumer);
}
