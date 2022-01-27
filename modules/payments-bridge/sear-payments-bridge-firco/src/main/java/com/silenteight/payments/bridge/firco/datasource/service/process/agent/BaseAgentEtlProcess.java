package com.silenteight.payments.bridge.firco.datasource.service.process.agent;

import lombok.AllArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import com.google.protobuf.Message;

import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static com.silenteight.payments.bridge.firco.datasource.util.HitDataUtils.filterHitsData;

@AllArgsConstructor
abstract class BaseAgentEtlProcess<T extends Message> implements CreateFeatureInputStructured {

  private static final String FEATURE_PREFIX = "features/";

  @Override
  public List<AgentInput> createFeatureInputs(AeAlert alert, List<HitData> hitsData) {
    var alertName = alert.getAlertName();
    return alert.getMatches().entrySet().stream()
        .map(matchItem -> handleMatches(hitsData, alertName, matchItem))
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  private List<AgentInput> handleMatches(
      List<HitData> hitsData, String alertName, Entry<String, String> matchItem) {
    return filterHitsData(hitsData, matchItem).stream()
        .map(hitData -> createAgentInputs(alertName, matchItem.getValue(), hitData))
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  private List<AgentInput> createAgentInputs(
      String alertName, String matchName, HitData hitData) {
    var dataSourceFeatureInputs = createDataSourceFeatureInputs(hitData);
    return dataSourceFeatureInputs.stream()
        .map(featureInput -> AgentInput.newBuilder()
            .setAlert(alertName)
            .setMatch(matchName)
            .addFeatureInputs(featureInput)
            .build())
        .collect(Collectors.toList());
  }

  protected abstract List<FeatureInput> createDataSourceFeatureInputs(HitData hitData);

  protected static String getFullFeatureName(String featureName) {
    return FEATURE_PREFIX + featureName;
  }
}
