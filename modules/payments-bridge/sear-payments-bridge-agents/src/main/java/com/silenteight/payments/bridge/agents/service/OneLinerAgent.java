package com.silenteight.payments.bridge.agents.service;

import lombok.NonNull;

import com.silenteight.payments.bridge.agents.model.OneLinerAgentRequest;
import com.silenteight.payments.bridge.agents.model.OneLinerAgentResponse;
import com.silenteight.payments.bridge.agents.port.OneLinerUseCase;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.agents.model.OneLinerAgentResponse.NO;
import static com.silenteight.payments.bridge.agents.model.OneLinerAgentResponse.NO_DATA;
import static com.silenteight.payments.bridge.agents.model.OneLinerAgentResponse.YES;

@Service
public class OneLinerAgent implements OneLinerUseCase {

  @NonNull
  public OneLinerAgentResponse invoke(@NonNull OneLinerAgentRequest oneLinerAgentRequest) {
    if (!isOneLineAddress(
        oneLinerAgentRequest.isNoAcctNumFlag(),
        oneLinerAgentRequest.getNoOfLines())) {
      return NO;
    }

    return oneLinerAgentRequest.getMessageLength() == 0 ? NO_DATA : YES;
  }

  public static boolean isOneLineAddress(boolean noAcctNumFlag, int noOfLines) {
    return (noAcctNumFlag && noOfLines < 2) || (!noAcctNumFlag && noOfLines < 3);
  }
}
