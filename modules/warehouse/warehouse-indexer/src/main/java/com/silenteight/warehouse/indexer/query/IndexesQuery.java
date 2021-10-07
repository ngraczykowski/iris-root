package com.silenteight.warehouse.indexer.query;

import java.util.List;

public interface IndexesQuery {

  List<String> getIndexesForAnalysis(String analysisName);
}
