package com.silenteight.connector.ftcc.callback.decision;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

import com.silenteight.connector.ftcc.callback.decision.statemapping.StateMappingStrategy.MapStateInput;
import com.silenteight.connector.ftcc.common.dto.input.StatusInfoDto;

import java.util.List;
import java.util.Optional;

@Value
@Builder
public class MapStatusRequest {

  @NonNull
  String dataCenter;

  @NonNull
  String unit;

  @NonNull
  String currentStatusName;

  @NonNull
  String recommendedAction;

  @Singular
  List<StatusInfoDto> nextStatuses;

  Optional<StatusInfoDto> findNextStatus(String nextStatusName) {
    return nextStatuses.stream()
        .filter(ns -> ns.getName().equals(nextStatusName))
        .findFirst();
  }

  @SuppressWarnings("FeatureEnvy")
  MapStateInput toMapStateInput() {
    return MapStateInput
        .builder()
        .sourceState(currentStatusName)
        .dataCenter(dataCenter)
        .unit(unit)
        .recommendedAction(recommendedAction)
        .build();
  }
}
