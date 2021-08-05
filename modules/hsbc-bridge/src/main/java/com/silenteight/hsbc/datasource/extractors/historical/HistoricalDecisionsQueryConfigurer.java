package com.silenteight.hsbc.datasource.extractors.historical;

import com.silenteight.hsbc.datasource.feature.historical.HistoricalDecisionsQuery;

public class HistoricalDecisionsQueryConfigurer {

  public HistoricalDecisionsQuery.Factory create() {
    return HistoricalDecisionsQueryFacade::new;
  }
}
