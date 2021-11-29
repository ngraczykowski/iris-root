package com.silenteight.payments.bridge.firco.datasource.service.process;

import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceBlockingStub;
import com.silenteight.datasource.api.bankidentificationcodes.v1.BankIdentificationCodesFeatureInput;
import com.silenteight.payments.bridge.common.dto.common.SolutionType;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class IdentificationMismatchAgentEtlProcess
    extends BaseAgentEtlProcess<BankIdentificationCodesFeatureInput> {

  private static final String BANK_IDENTIFICATION_CODES_FEATURE = "bankIdentificationCodes";

  IdentificationMismatchAgentEtlProcess(
      AgentInputServiceBlockingStub blockingStub, Duration timeout) {
    super(blockingStub, timeout);
  }

  @Override
  protected String getFeatureName() {
    return BANK_IDENTIFICATION_CODES_FEATURE;
  }

  @Override
  protected BankIdentificationCodesFeatureInput getFeatureInput(HitData hitData) {

    var fieldValue = Optional.of(hitData)
        .map(HitData::getHitAndWlPartyData)
        .map(HitAndWatchlistPartyData::getFieldValue)
        .orElse("");
    var matchingText = Optional.of(hitData)
        .map(HitData::getHitAndWlPartyData)
        .map(HitAndWatchlistPartyData::getMatchingText)
        .orElse("");

    return BankIdentificationCodesFeatureInput
        .newBuilder()
        .setAlertedPartyMatchingField(fieldValue)
        .setWatchlistMatchingText(matchingText)
        .setFeature(getFullFeatureName())
        .addAllWatchlistSearchCodes(setWatchlistSearchCodes(hitData))
        .build();
  }

  private static List<String> setWatchlistSearchCodes(HitData hitData) {
    var solutionType = Optional.of(hitData)
        .map(HitData::getHitAndWlPartyData)
        .map(HitAndWatchlistPartyData::getSolutionType)
        .orElse(null);

    var searchCodes = Optional.of(hitData)
        .map(HitData::getHitAndWlPartyData)
        .map(HitAndWatchlistPartyData::getSearchCodes)
        .orElseGet(Collections::emptyList);
    var passports = Optional.of(hitData)
        .map(HitData::getHitAndWlPartyData)
        .map(HitAndWatchlistPartyData::getPassports)
        .orElseGet(Collections::emptyList);
    var natIds = Optional.of(hitData)
        .map(HitData::getHitAndWlPartyData)
        .map(HitAndWatchlistPartyData::getNatIds)
        .orElseGet(Collections::emptyList);
    var bics = Optional.of(hitData)
        .map(HitData::getHitAndWlPartyData)
        .map(HitAndWatchlistPartyData::getBics)
        .orElseGet(Collections::emptyList);

    if (SolutionType.SEARCH_CODE == solutionType) {
      return searchCodes;
    } else if (SolutionType.PASSPORT == solutionType) {
      return passports;
    } else if (SolutionType.NATIONAL_ID == solutionType) {
      return natIds;
    } else if (SolutionType.BIC == solutionType) {
      return bics;
    }

    return Collections.emptyList();
  }
}
