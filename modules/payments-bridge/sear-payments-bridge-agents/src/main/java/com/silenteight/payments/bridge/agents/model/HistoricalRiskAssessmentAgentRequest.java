package com.silenteight.payments.bridge.agents.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class HistoricalRiskAssessmentAgentRequest {

  @NonNull String accountNumber;
  @NonNull String ofacID;
}
