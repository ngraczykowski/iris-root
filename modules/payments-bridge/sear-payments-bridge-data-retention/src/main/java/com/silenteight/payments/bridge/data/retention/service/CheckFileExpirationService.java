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
import java.util.List;

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
    var expiredFiles = getExpiredFiles();

    if (expiredFiles.isEmpty()) {
      return;
    }

    applicationEventPublisher.publishEvent(
        FilesExpiredEvent.builder().fileNames(expiredFiles).build());
    dataRetentionAccessPort.update(expiredFiles);
  }

  private List<String> getExpiredFiles() {
    var dateBefore = dataRetentionProperties.getFile().getExpiration();
    var dateTime = OffsetDateTime.now().minus(dateBefore);
    return dataRetentionAccessPort.findFileNameBefore(dateTime);
  }
}
