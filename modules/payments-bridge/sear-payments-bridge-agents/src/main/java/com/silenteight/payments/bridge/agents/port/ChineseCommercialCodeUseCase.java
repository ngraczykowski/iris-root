package com.silenteight.payments.bridge.agents.port;

import lombok.NonNull;

import com.silenteight.payments.bridge.agents.model.ChineseCommercialCodesAgentResponse;

public interface ChineseCommercialCodeUseCase {

  ChineseCommercialCodesAgentResponse invoke(@NonNull String matchingField);
}
