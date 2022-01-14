package com.silenteight.bridge.core.registration.domain.port.outgoing;

import com.silenteight.bridge.core.registration.domain.model.Match;

import java.util.List;

public interface MatchRepository {

  void updateStatusByNameIn(Match.Status status, List<String> names);
}
