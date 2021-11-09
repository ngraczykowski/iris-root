package com.silenteight.payments.bridge.data.retention.port;

import com.silenteight.dataretention.api.v1.PersonalInformationExpired;

public interface SendPersonalInformationExpiredPort {

  void send(PersonalInformationExpired personalInformationExpired);

}
