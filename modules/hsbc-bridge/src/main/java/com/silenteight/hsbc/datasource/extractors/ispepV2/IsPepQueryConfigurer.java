package com.silenteight.hsbc.datasource.extractors.ispepV2;

import com.silenteight.hsbc.datasource.feature.ispep.IsPepQueryV2;

public class IsPepQueryConfigurer {

  public IsPepQueryV2.Factory create() {
    return IsPepQueryFacade::new;
  }
}
