package com.silenteight.payments.bridge.firco.datasource.service.process;

import lombok.AllArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceBlockingStub;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;
import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.firco.datasource.model.EtlProcess;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertEtlResponse;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import com.google.protobuf.Message;
import io.grpc.Deadline;

import java.time.Duration;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.silenteight.payments.bridge.firco.datasource.util.HitDataUtils.filterHitsData;

@AllArgsConstructor
abstract class BaseAgentEtlProcess<T extends Message> implements EtlProcess {

  private static final String FEATURE_PREFIX = "features/";

  private AgentInputServiceBlockingStub blockingStub;
  private Duration timeout;

  @Override
  public void extractAndLoad(AeAlert alert, AlertEtlResponse alertEtlResponse) {
    List<HitData> hitsData = alertEtlResponse.getHits();
    var alertName = alert.getAlertName();

    alert.getMatches().entrySet()
        .forEach(matchItem -> handleMatches(hitsData, alertName, matchItem));
  }

  private void handleMatches(
      List<HitData> hitsData, String alertName, Entry<String, String> matchItem) {

    filterHitsData(hitsData, matchItem)
        .forEach(hitData -> callAgentService(alertName, matchItem.getValue(), hitData));
  }

  private void callAgentService(
      String alertName, String matchName, HitData hitData) {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    var dataSourceFeatureInputs = createDataSourceFeatureInputs(hitData);

    var agentInputs = dataSourceFeatureInputs.stream()
        .map(featureInput -> AgentInput.newBuilder()
            .setAlert(alertName)
            .setMatch(matchName)
            .addFeatureInputs(featureInput)
            .build())
        .collect(Collectors.toList());

    var batchInputRequest =
        createBatchInputRequest(agentInputs);

    var batchCreateAgentInputsResponse = blockingStub
        .withDeadline(deadline)
        .batchCreateAgentInputs(batchInputRequest);
  }

  private static BatchCreateAgentInputsRequest createBatchInputRequest(
      List<AgentInput> agentInputs) {

    return BatchCreateAgentInputsRequest.newBuilder()
        .addAllAgentInputs(agentInputs)
        .build();
  }

  protected abstract List<FeatureInput> createDataSourceFeatureInputs(HitData hitData);

  protected static String getFullFeatureName(String featureName) {
    return FEATURE_PREFIX + featureName;
  }
}
