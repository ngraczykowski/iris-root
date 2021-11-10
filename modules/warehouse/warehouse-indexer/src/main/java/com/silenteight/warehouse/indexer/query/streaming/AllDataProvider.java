package com.silenteight.warehouse.indexer.query.streaming;

import java.util.Collection;
import java.util.function.Consumer;

public interface AllDataProvider {

  void fetchData(FetchAllDataRequest request, Consumer<Collection<FetchedDocument>> consumer);
}
