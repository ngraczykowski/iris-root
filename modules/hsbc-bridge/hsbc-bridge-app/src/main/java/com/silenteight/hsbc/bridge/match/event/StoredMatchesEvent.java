package com.silenteight.hsbc.bridge.match.event;

import lombok.*;

import com.silenteight.hsbc.bridge.match.MatchComposite;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class StoredMatchesEvent {

  List<MatchComposite> matchComposites;
}
