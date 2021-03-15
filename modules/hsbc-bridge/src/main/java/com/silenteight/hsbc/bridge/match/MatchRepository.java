package com.silenteight.hsbc.bridge.match;

import org.springframework.data.repository.Repository;

interface MatchRepository extends Repository<MatchEntity, Long> {

  void save(MatchEntity matchEntity);

  MatchEntity findById(long id);
}
