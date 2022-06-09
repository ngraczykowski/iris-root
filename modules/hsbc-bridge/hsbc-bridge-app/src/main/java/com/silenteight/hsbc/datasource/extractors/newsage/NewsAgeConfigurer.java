package com.silenteight.hsbc.datasource.extractors.newsage;

import com.silenteight.hsbc.datasource.feature.newsage.NewsAgeQuery;

public class NewsAgeConfigurer {

  public NewsAgeQuery.Factory create() {
    return NewsAgeQueryFacade::new;
  }
}
