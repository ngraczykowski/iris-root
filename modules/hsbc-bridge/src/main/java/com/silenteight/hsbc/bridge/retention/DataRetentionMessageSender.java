package com.silenteight.hsbc.bridge.retention;

import com.silenteight.dataretention.api.v1.AlertsExpired;
import com.silenteight.dataretention.api.v1.PersonalInformationExpired;

public interface DataRetentionMessageSender {

  void send(AlertsExpired alertsExpired);
  void send(PersonalInformationExpired personalInformationExpired);
}
