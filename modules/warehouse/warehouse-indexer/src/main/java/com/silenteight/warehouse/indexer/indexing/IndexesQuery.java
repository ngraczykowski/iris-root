package com.silenteight.warehouse.indexer.indexing;

import java.util.List;

public interface IndexesQuery {

  List<String> getIndexesForAnalysis(String analysisName);
}
