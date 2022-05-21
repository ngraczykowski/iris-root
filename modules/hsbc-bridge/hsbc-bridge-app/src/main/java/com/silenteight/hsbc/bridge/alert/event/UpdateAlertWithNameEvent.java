package com.silenteight.hsbc.bridge.alert.event;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public class UpdateAlertWithNameEvent {

  @NonNull
  private final Map<Long, String> alertIdToName;
}
