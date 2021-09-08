package com.silenteight.serp.governance.qa;

import com.silenteight.serp.governance.qa.sampling.generator.dto.Filter;

import java.util.List;

import static java.util.List.of;

public class FilterFixture {

  private FilterFixture() {

  }

  public static final String FIELD_ALERT_RECOMMENDATION = "alert_s8_recommendation";
  public static final List<String> VALUES_ALERT_RECOMMENDATION = of("ACTION_FALSE_POSITIVE");
  public static final Filter ALERT_RECOMMENDATION_FILTER = Filter.builder()
      .field(FIELD_ALERT_RECOMMENDATION)
      .values(VALUES_ALERT_RECOMMENDATION)
      .build();
}
