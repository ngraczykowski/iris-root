package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.port.RegisteredAlertDataAccessPort;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
class RegisteredAlertJdbcDataAccess implements RegisteredAlertDataAccessPort {

  private final InsertRegisteredAlertQuery insertRegisteredAlertQuery;

  private final SelectRegisteredAlertQuery selectRegisteredAlertQuery;

  @Override
  @Transactional
  public void save(UUID alertId, String alertName) {
    insertRegisteredAlertQuery.execute(alertId, alertName);
  }

  @Override
  public String getAlertId(String alertName) {
    return selectRegisteredAlertQuery.execute(alertName);
  }
}
