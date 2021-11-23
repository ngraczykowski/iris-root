package com.silenteight.payments.bridge.firco.datasource.service.process;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceBlockingStub;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;
import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.bankidentificationcodes.v1.BankIdentificationCodesFeatureInput;
import com.silenteight.payments.bridge.common.dto.common.SolutionType;
import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.firco.datasource.model.EtlProcess;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertEtlResponse;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import com.google.protobuf.Any;
import io.grpc.Deadline;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.silenteight.payments.bridge.firco.datasource.util.HitDataUtils.filterHitsData;

@RequiredArgsConstructor
class IdentificationMismatchAgentEtlProcess implements EtlProcess {

  private final AgentInputServiceBlockingStub blockingStub;
  private final Duration timeout;

  @Override
  public void extractAndLoad(AeAlert alert, AlertEtlResponse alertEtlResponse) {
    List<HitData> hitsData = alertEtlResponse.getHits();
    alert.getMatches().entrySet()
        .forEach(matchItem -> handleMatches(hitsData, matchItem));
  }

  private void handleMatches(List<HitData> hitsData, Entry<String, String> matchItem) {
    filterHitsData(hitsData, matchItem)
        .stream()
        .forEach(hitData -> callAgentService(matchItem, hitData));
  }

  private void callAgentService(Entry<String, String> matchItem, HitData hitData) {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    String fieldValue = Optional.of(hitData)
        .map(HitData::getHitAndWlPartyData)
        .map(HitAndWatchlistPartyData::getFieldValue)
        .orElse("");
    String matchingText = Optional.of(hitData)
        .map(HitData::getHitAndWlPartyData)
        .map(HitAndWatchlistPartyData::getMatchingText)
        .orElse("");
    SolutionType solutionType = Optional.of(hitData)
        .map(HitData::getHitAndWlPartyData)
        .map(HitAndWatchlistPartyData::getSolutionType)
        .orElse(null);

    List<String> searchCodes = Optional.of(hitData)
        .map(HitData::getHitAndWlPartyData)
        .map(HitAndWatchlistPartyData::getSearchCodes)
        .orElseGet(Collections::emptyList);
    List<String> passports = Optional.of(hitData)
        .map(HitData::getHitAndWlPartyData)
        .map(HitAndWatchlistPartyData::getPassports)
        .orElseGet(Collections::emptyList);
    List<String> natIds = Optional.of(hitData)
        .map(HitData::getHitAndWlPartyData)
        .map(HitAndWatchlistPartyData::getNatIds)
        .orElseGet(Collections::emptyList);
    List<String> bics = Optional.of(hitData)
        .map(HitData::getHitAndWlPartyData)
        .map(HitAndWatchlistPartyData::getBics)
        .orElseGet(Collections::emptyList);

    var batchInputRequest = createBatchInputRequest(
        matchItem.getValue(),
        fieldValue,
        matchingText,
        solutionType,
        searchCodes,
        passports,
        natIds,
        bics
    );
    blockingStub
        .withDeadline(deadline)
        .batchCreateAgentInputs(batchInputRequest);
  }

  private BatchCreateAgentInputsRequest createBatchInputRequest(
      String matchValue,
      String fieldValue,
      String matchingText,
      SolutionType solutionType,
      List<String> searchCodes,
      List<String> passports,
      List<String> natIds,
      List<String> bics) {

    var bankIdentificationCodesInput =
        createBankIdentificationCodesInput(
            fieldValue, matchingText, solutionType, searchCodes, passports, natIds, bics);
    var batchInputRequest = BatchCreateAgentInputsRequest.newBuilder()
        .addAgentInputs(AgentInput.newBuilder()
            .setMatch(matchValue)
            .addFeatureInputs(
                FeatureInput.newBuilder()
                    .setFeature("features/bankIdentificationCodes")
                    .setAgentFeatureInput(Any.pack(bankIdentificationCodesInput))
                    .build())
            .build())
        .build();

    return batchInputRequest;
  }

  private BankIdentificationCodesFeatureInput createBankIdentificationCodesInput(
      String fieldValue,
      String matchingText,
      SolutionType solutionType,
      List<String> searchCodes,
      List<String> passports,
      List<String> natIds,
      List<String> bics) {

    BankIdentificationCodesFeatureInput.Builder builder = BankIdentificationCodesFeatureInput
        .newBuilder()
        .setAlertedPartyMatchingField(fieldValue)
        .setWatchlistMatchingText(matchingText)
        .setFeature("features/bankIdentificationCodes");

    if (SolutionType.SEARCH_CODE == solutionType) {
      builder.addAllWatchlistSearchCodes(searchCodes);
    } else if (SolutionType.PASSPORT == solutionType) {
      builder.addAllWatchlistSearchCodes(passports);
    } else if (SolutionType.NATIONAL_ID == solutionType) {
      builder.addAllWatchlistSearchCodes(natIds);
    } else if (SolutionType.BIC == solutionType) {
      builder.addAllWatchlistSearchCodes(bics);
    }

    return builder.build();
  }
}
