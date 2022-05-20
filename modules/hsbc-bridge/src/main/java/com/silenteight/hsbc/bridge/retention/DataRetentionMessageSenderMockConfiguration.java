package com.silenteight.hsbc.bridge.retention;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.AlertsExpired;
import com.silenteight.dataretention.api.v1.PersonalInformationExpired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
@Slf4j
class DataRetentionMessageSenderMockConfiguration {

  @Bean
  DataRetentionMessageSender dataRetentionMessageSender() {
    return new DataRetentionMessageSender() {
      @Override
      public void send(AlertsExpired alertsExpired) {
        log.warn("Data retention: AlertsExpired message has been sent.");
      }

      @Override
      public void send(
          PersonalInformationExpired personalInformationExpired) {
        log.warn("Data retention: PersonalInformationExpired message has been sent.");
      }
    };
  }
}
