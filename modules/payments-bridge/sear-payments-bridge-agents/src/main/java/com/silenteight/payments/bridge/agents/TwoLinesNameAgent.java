package com.silenteight.payments.bridge.agents;

import lombok.NonNull;

import java.util.List;

class TwoLinesNameAgent {

  private static final List<String> COMPANY_PATTERNS = List.of(
      ".*\\sGLOBAL.*", ".*\\sCOMPANY.*", ".*\\sLIMITED.*", ".*\\sCORPORATION.*",
      ".*\\sINCORPORATED.*",
      ".*\\sPRIVATE.*", ".*(\\sPTE[\\., ]+.*|PTE[\\.,]*)", ".*(\\sLTD[\\., ]+.*|LTD[\\.,]*)",
      ".*(\\sCO[\\., ]+.*|\\sCO[\\.,]*)", ".*\\sCORP[\\.,]*.*",
      ".*(\\sINC[\\., ]+.*|\\sINC[\\.,]*)",
      ".*(\\sLDA[\\., ]+.*|\\sLDA[\\.,]*)", ".*(\\sPLC[\\., ]+.*|\\sPLC[\\.,]*)",
      ".*(\\sPVT[\\., ]+.*|\\sPVT[\\.,]*)", ".*\\sL[\\., ]*L[\\., ]*C[\\.,]*.*");

  @NonNull
  TwoLinesNameAgentResponse invoke(
      @NonNull TwoLinesNameAgentRequest twoLinesNameAgentRequest) {

    List<String> alertedPartyAddresses = twoLinesNameAgentRequest.getAlertedPartyAddresses();

    if (alertedPartyAddresses.isEmpty())
      return TwoLinesNameAgentResponse.NO_DATA;

    for (var alertedPartAddress : alertedPartyAddresses) {
      if (lineContainsCompanyPattern(alertedPartAddress))
        return TwoLinesNameAgentResponse.YES;
    }

    return TwoLinesNameAgentResponse.NO;
  }

  private static boolean lineContainsCompanyPattern(String line) {
    for (String pattern : COMPANY_PATTERNS) {
      if (line.toUpperCase().matches(pattern))
        return true;
    }
    return false;
  }
}
