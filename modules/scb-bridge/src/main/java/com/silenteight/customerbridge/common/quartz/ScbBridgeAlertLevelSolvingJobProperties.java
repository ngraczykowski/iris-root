package com.silenteight.customerbridge.common.quartz;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("serp.scb.bridge.solving.alert")
@Component
@Data
@EqualsAndHashCode(callSuper = true)
@Validated
public class ScbBridgeAlertLevelSolvingJobProperties extends
    ScbBridgeSolvingJobProperties {

  private String name = "SCB_SOLVING_ALERT_LEVEL";

}
