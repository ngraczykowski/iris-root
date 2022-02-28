package com.silenteight.payments.bridge.data.retention.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.data.retention.adapter.FileDataRetentionAccessPort;
import com.silenteight.payments.bridge.data.retention.model.FilesExpiredEvent;
import com.silenteight.payments.bridge.data.retention.port.CheckFileExpirationUseCase;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@EnableConfigurationProperties(DataRetentionProperties.class)
@Slf4j
@RequiredArgsConstructor
class CheckFileExpirationService implements CheckFileExpirationUseCase {

  private final DataRetentionProperties dataRetentionProperties;
  private final FileDataRetentionAccessPort dataRetentionAccessPort;
  private final ApplicationEventPublisher applicationEventPublisher;

  @Override
  public void execute() {
    var dateBefore = dataRetentionProperties.getFile().getExpiration();
    var dateTime = OffsetDateTime.now().minus(dateBefore);
    var expiredFiles = dataRetentionAccessPort.findFileNameBefore(dateTime);
    applicationEventPublisher.publishEvent(
        FilesExpiredEvent.builder().fileNames(expiredFiles).build());
  }
}
