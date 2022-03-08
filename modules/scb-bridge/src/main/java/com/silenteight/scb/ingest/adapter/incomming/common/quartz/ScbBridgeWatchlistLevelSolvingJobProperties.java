package com.silenteight.scb.ingest.adapter.incomming.common.quartz;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("serp.scb.bridge.solving.watchlist")
@Component
@Data
@EqualsAndHashCode(callSuper = true)
@Validated
public class ScbBridgeWatchlistLevelSolvingJobProperties extends
    ScbBridgeSolvingJobProperties {

  private String name = "SCB_SOLVING_WATCHLIST_LEVEL";
}
