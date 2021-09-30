package com.silenteight.payments.bridge.firco.recommendation.model;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.payments.bridge.common.dto.common.StatusInfoDto;
import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;

@Value
@RequiredArgsConstructor
public class Recommendation {

  AlertMessageDto alertMessageDto;
  StatusInfoDto decision;

}
