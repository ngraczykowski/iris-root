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

import com.google.protobuf.Any;
import com.google.protobuf.Message;
import io.grpc.Deadline;

import java.time.Duration;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

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

    var featureInput = getFeatureInput(hitData);

    var batchInputRequest =
        createBatchInputRequest(alertName, matchName, featureInput);

    var batchCreateAgentInputsResponse = blockingStub
        .withDeadline(deadline)
        .batchCreateAgentInputs(batchInputRequest);
  }

  private BatchCreateAgentInputsRequest createBatchInputRequest(
      String alertName, String matchName, T featureInput) {
    FeatureInput dataSourceFeatureInput = getDataSourceFeatureInput(featureInput);

    var agentInput = AgentInput.newBuilder()
        .setAlert(alertName)
        .setMatch(matchName)
        .addFeatureInputs(dataSourceFeatureInput)
        .build();

    return BatchCreateAgentInputsRequest.newBuilder()
        .addAgentInputs(agentInput)
        .build();
  }

  private FeatureInput getDataSourceFeatureInput(T featureInput) {
    return FeatureInput
        .newBuilder()
        .setFeature(getFullFeatureName())
        .setAgentFeatureInput(Any.pack(featureInput))
        .build();
  }

  protected String getFullFeatureName() {
    return FEATURE_PREFIX + getFeatureName();
  }

  protected abstract String getFeatureName();

  protected abstract T getFeatureInput(HitData hitData);
}
