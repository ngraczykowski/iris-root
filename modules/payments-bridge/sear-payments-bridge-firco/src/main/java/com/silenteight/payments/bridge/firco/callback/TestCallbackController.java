package com.silenteight.payments.bridge.firco.callback;


import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.dto.output.AlertRecommendationDto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TestCallbackController {

  private final CallbackRequestFactory callbackRequestFactory;

  @GetMapping("/test-callback")
  public String getTest() {
    var callback = callbackRequestFactory.create(new AlertRecommendationDto("FP", "false alarm"));
    callback.invoke();
    return "Callback invoked\n";
  }
}
