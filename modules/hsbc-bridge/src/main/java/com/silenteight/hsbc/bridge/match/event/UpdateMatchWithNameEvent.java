package com.silenteight.hsbc.bridge.match.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class UpdateMatchWithNameEvent {

  private final List<String> names;

}


