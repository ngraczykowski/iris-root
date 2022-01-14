package com.silenteight.bridge.core.registration.adapter.outgoing;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.registration.domain.model.Match;
import com.silenteight.bridge.core.registration.domain.port.outgoing.MatchRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcMatchRepository implements MatchRepository {

  private final CrudMatchRepository crudMatchRepository;

  @Override
  public void updateStatusByNameIn(Match.Status status, List<String> names) {
    crudMatchRepository.updateStatusByNameIn(MatchEntity.Status.valueOf(status.name()), names);
  }
}
