package com.silenteight.warehouse.indexer.query.streaming;

import java.util.Collection;
import java.util.function.Consumer;

public interface DataProvider {

  void fetchData(FetchDataRequest request, Consumer<Collection<String>> consumer);
}
