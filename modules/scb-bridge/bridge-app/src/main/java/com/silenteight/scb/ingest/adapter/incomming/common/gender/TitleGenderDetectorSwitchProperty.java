package com.silenteight.scb.ingest.adapter.incomming.common.gender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class TitleGenderDetectorSwitchProperty {

  private static boolean titleGenderDetectorEnabled = true;

  @Value("${silenteight.scb-bridge.title-gender-detector.enabled:true}")
  void setTitleGenderDetectorEnabled(boolean enabled) {
    titleGenderDetectorEnabled = enabled;
  }

  static boolean isTitleGenderDetectorDisabled() {
    return !titleGenderDetectorEnabled;
  }
}
