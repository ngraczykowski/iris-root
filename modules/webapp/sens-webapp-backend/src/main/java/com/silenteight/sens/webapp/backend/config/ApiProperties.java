package com.silenteight.sens.webapp.backend.config;

import lombok.Data;

import com.silenteight.sens.webapp.backend.config.FrontEndProperties.DecisionConfig;
import com.silenteight.sens.webapp.common.util.StringPredicate;

import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;
import java.util.Map;
import javax.validation.Valid;

@Data
class ApiProperties {

  @Valid
  @NestedConfigurationProperty
  private SolutionProperties solution = new SolutionProperties();

  @Valid
  @NestedConfigurationProperty
  private FrontEndProperties frontEnd = new FrontEndProperties();

  Map<String, StringPredicate> getExposedAlertFilterMap() {
    return solution.getExposedAlertFilterMap();
  }

  boolean isSolutionSyncCheck() {
    return solution.isSyncCheck();
  }

  String getOutdatedMessage() {
    return solution.getOutdatedMessagePrefix();
  }

  String getAlertDetailsPageUrlTemplate() {
    return frontEnd.getAlertDetailsUrlTemplate();
  }

  List<DecisionConfig> getDecisionsConfiguration() {
    return frontEnd.getDecisionsConfiguration();
  }
}
