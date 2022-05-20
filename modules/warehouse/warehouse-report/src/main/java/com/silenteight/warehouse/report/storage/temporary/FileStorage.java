package com.silenteight.warehouse.report.storage.temporary;

import java.io.InputStream;
import java.util.Collection;
import java.util.function.Consumer;

public interface FileStorage {

  Consumer<Collection<String>> getConsumer();

  InputStream getInputStream();
}
