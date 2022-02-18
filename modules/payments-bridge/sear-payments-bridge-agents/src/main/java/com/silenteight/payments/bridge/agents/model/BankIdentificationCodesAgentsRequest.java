package com.silenteight.payments.bridge.agents.model;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.common.dto.common.SolutionType;

import java.util.List;

@Value
@Builder
public class BankIdentificationCodesAgentsRequest {

  String feature;
  String fieldValue;
  String matchingText;
  SolutionType solutionType;
  List<String> searchCodes;
  List<String> passports;
  List<String> natIds;
  List<String> bics;

}
