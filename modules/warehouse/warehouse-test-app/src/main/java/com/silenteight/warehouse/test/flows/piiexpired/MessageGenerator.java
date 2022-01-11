package com.silenteight.warehouse.test.flows.piiexpired;

import lombok.RequiredArgsConstructor;

import com.silenteight.dataretention.api.v1.PersonalInformationExpired;

import java.util.List;

@RequiredArgsConstructor
class MessageGenerator {

  PersonalInformationExpired generatePersonalInformationExpired(List<String> alertNames) {
    return PersonalInformationExpired
        .newBuilder()
        .addAllAlerts(alertNames)
        .build();
  }
}
