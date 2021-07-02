package com.silenteight.hsbc.bridge.match;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

interface MatchRepository extends Repository<MatchEntity, Long> {

  void save(MatchEntity matchEntity);

  Optional<MatchEntity> findById(long id);

  List<MatchEntity> findMatchEntitiesByAlertId(long id);

  Optional<MatchEntity> findByName(String name);
}
