package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.domain.SaveRegisteredAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisteredAlertDataAccessPort;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
class RegisteredAlertJdbcDataAccess implements RegisteredAlertDataAccessPort {

  private final InsertRegisteredAlertQuery insertRegisteredAlertQuery;

  private final SelectRegisteredAlertQuery selectRegisteredAlertQuery;

  private final InsertMatchQuery insertMatchQuery;

  private final DeleteRegisteredAlertQuery deleteRegisteredAlertQuery;

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void save(List<SaveRegisteredAlertRequest> request) {
    request.forEach(r -> {
      var registeredAlertId = insertRegisteredAlertQuery.execute(r);
      insertMatchQuery.execute(r, registeredAlertId);
    });
  }

  @Override
  public UUID getAlertMessageId(String alertName) {
    return selectRegisteredAlertQuery.execute(alertName);
  }

  @Override
  @Transactional
  public List<String> delete(List<String> alertNames) {
    return deleteRegisteredAlertQuery.execute(alertNames);
  }
}
