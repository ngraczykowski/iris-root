package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("serp.scb.gnsrt.recommendation")
@Component
@Data
@Validated
// TODO: probably we should respect that
class GnsRtRecommendationProperties {

  private int deadlineInSeconds = 7;
}
