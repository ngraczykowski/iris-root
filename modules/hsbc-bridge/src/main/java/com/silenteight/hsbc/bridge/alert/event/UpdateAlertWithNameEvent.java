package com.silenteight.hsbc.bridge.alert.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public class UpdateAlertWithNameEvent {

  private final Map<String, String> alertIdToName;
}
