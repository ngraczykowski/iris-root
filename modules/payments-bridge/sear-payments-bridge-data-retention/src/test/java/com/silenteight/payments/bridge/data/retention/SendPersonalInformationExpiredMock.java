package com.silenteight.payments.bridge.data.retention;

import com.silenteight.dataretention.api.v1.PersonalInformationExpired;
import com.silenteight.payments.bridge.data.retention.port.SendPersonalInformationExpiredPort;

import java.util.LinkedList;
import java.util.List;

class SendPersonalInformationExpiredMock implements SendPersonalInformationExpiredPort {

  private final List<PersonalInformationExpired> personalInformationExpiredList =
      new LinkedList<>();

  @Override
  public void send(
      PersonalInformationExpired personalInformationExpired) {
    personalInformationExpiredList.add(personalInformationExpired);
  }
}
