package com.silenteight.hsbc.datasource.extractors.ispep;

import com.silenteight.hsbc.datasource.feature.ispep.IsPepQuery;

public class IsPepQueryConfigurer {

  public IsPepQuery.Factory create() {
    return IsPepQueryFacade::new;
  }
}
