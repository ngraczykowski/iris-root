package com.silenteight.customerbridge.common.quartz;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("serp.scb.bridge.learning.watchlist")
@Component
@Data
@Validated
@EqualsAndHashCode(callSuper = true)
public class ScbBridgeWatchlistLevelLearningJobProperties extends ScbBridgeLearningJobProperties {

  private String deltaJobName = "SCB_LEARNING_WATCHLIST_LEVEL";

  private boolean watchlistLevel = true;

}
