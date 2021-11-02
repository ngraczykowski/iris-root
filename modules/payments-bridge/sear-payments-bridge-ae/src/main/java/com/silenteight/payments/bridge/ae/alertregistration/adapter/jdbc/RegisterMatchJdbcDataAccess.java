package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterMatchDataAccessPort;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
class RegisterMatchJdbcDataAccess implements RegisterMatchDataAccessPort {

  private final RegisterMatchQuery registerMatchQuery;

  @Override
  @Transactional
  public void save(UUID alertId, List<String> matchNames) {
    registerMatchQuery.execute(alertId, matchNames);
  }
}
