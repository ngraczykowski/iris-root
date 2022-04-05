package com.silenteight.fab.dataprep.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.fab.dataprep.domain.ex.DataPrepException;
import com.silenteight.proto.fab.api.v1.AlertMessageStored.State;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.Map;
import javax.validation.constraints.NotNull;

@Validated
@ConstructorBinding
@ConfigurationProperties("feeding.alert.state")
@Value
@Builder
class AlertStateProperties {

  @NotNull
  Map<State, String> analystDecisions;

  String getAnalystDecision(State state) {
    if (!analystDecisions.containsKey(state)) {
      throw new DataPrepException("Missing analyst decision for state: " + state);
    }
    return analystDecisions.get(state);
  }
}
