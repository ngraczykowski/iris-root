package com.silenteight.hsbc.bridge.match.event;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public class UpdateMatchWithNameEvent {

  @NonNull private final Map<Long, String> matchIdsWithNames;

}


