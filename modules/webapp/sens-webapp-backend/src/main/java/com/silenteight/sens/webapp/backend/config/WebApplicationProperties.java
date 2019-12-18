package com.silenteight.sens.webapp.backend.config;

import lombok.Data;

import com.silenteight.sens.webapp.backend.config.FrontEndProperties.DecisionConfig;
import com.silenteight.sens.webapp.common.util.StringPredicate;

import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;
import java.util.Map;
import javax.validation.Valid;

@Data
@Validated
public class WebApplicationProperties {

  @Valid
  @NestedConfigurationProperty
  private final CorsConfiguration cors = new CorsConfiguration();

  @Valid
  @NestedConfigurationProperty
  private final ApiProperties api = new ApiProperties();

  public Map<String, StringPredicate> getExposedAlertFilterMap() {
    return api.getExposedAlertFilterMap();
  }

  public boolean isSolutionSyncCheck() {
    return api.isSolutionSyncCheck();
  }

  public String getOutdatedMessage() {
    return api.getOutdatedMessage();
  }

  public String getAlertDetailsPageUrlTemplate() {
    return api.getAlertDetailsPageUrlTemplate();
  }

  public List<DecisionConfig> getDecisionsConfiguration() {
    return api.getDecisionsConfiguration();
  }
}
