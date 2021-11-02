package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.domain.SaveRegisteredAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisteredAlertDataAccessPort;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
class RegisteredAlertJdbcDataAccess implements RegisteredAlertDataAccessPort {

  private final InsertRegisteredAlertQuery insertRegisteredAlertQuery;

  private final SelectRegisteredAlertQuery selectRegisteredAlertQuery;

  private final InsertMatchQuery insertMatchQuery;

  @Override
  @Transactional
  public void save(SaveRegisteredAlertRequest request) {
    insertRegisteredAlertQuery.execute(request.getAlertId(), request.getAlertName());
    insertMatchQuery.execute(request.getAlertId(), request.getMatchNames());
  }

  @Override
  public String getAlertId(String alertName) {
    return selectRegisteredAlertQuery.execute(alertName);
  }
}
