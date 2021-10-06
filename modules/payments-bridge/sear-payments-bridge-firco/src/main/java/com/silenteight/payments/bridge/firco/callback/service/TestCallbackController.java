package com.silenteight.payments.bridge.firco.callback.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.dto.common.StatusInfoDto;
import com.silenteight.payments.bridge.common.dto.output.AlertDecisionMessageDto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.RECOMMENDED;

@RequiredArgsConstructor
@RestController
public class TestCallbackController {

  private final CallbackRequestFactory callbackRequestFactory;
  private final ClientRequestDtoFactory clientRequestDtoFactory;

  @GetMapping("/test-callback")
  public String getTest() {
    var clientRequestDto = clientRequestDtoFactory
        .create(createTestAlertDecisionMessageDto());
    var callback = callbackRequestFactory.create(clientRequestDto);
    callback.invoke();
    return "Callback invoked\n";
  }

  private AlertDecisionMessageDto createTestAlertDecisionMessageDto() {
    var status = new StatusInfoDto();
    status.setName(RECOMMENDED.name());

    var alertDecision = new AlertDecisionMessageDto();
    alertDecision.setStatus(status);
    return alertDecision;
  }
}
