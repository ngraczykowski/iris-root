package com.silenteight.hsbc.bridge.match;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.retention.DataCleaner;

import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@RequiredArgsConstructor
class MatchDataCleaner implements DataCleaner {

  private final MatchRepository matchRepository;

  @Override
  @Transactional
  public void clean(OffsetDateTime expireDate) {
    matchRepository.deletePayloadByCreatedAtBefore(expireDate);
  }
}
