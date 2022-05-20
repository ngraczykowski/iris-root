package com.silenteight.hsbc.datasource.extractors.name;

import com.silenteight.hsbc.datasource.feature.name.NameQuery;

public class NameQueryConfigurer {

  public NameQuery.Factory create() {
    return NameQueryFacade::new;
  }
}
