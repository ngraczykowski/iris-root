package com.silenteight.warehouse.backup.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.silenteight.warehouse.backup.storage.ProductionDataIndexRequestFixtures.ANALYSIS_NAME;
import static com.silenteight.warehouse.backup.storage.ProductionDataIndexRequestFixtures.PRODUCTION_REQUEST_V2;
import static com.silenteight.warehouse.backup.storage.ProductionDataIndexRequestFixtures.REQUEST_ID;
import static org.assertj.core.api.Assertions.*;

class StorageServiceTest {

  StorageService underTest;

  InMemoryBackupMessageRepository messageRepository;

  @BeforeEach
  void setUp() {
    messageRepository = new InMemoryBackupMessageRepository();
    underTest = new StorageService(messageRepository, false);
  }

  @Test
  void shouldSaveProductionDataIndexRequestAsMessage() {
    //given
    assertThat(messageRepository.count()).isZero();
    //when
    underTest.save(PRODUCTION_REQUEST_V2, REQUEST_ID, ANALYSIS_NAME);
    //then
    assertThat(messageRepository.count()).isEqualTo(1);
    BackupMessage savedBackupMessage = messageRepository.findOne();
    assertThat(savedBackupMessage).isNotNull();
    assertThat(PRODUCTION_REQUEST_V2.toByteArray())
        .isEqualTo(savedBackupMessage.getData());
    assertThat(savedBackupMessage.getRequestId())
        .isEqualTo(PRODUCTION_REQUEST_V2.getRequestId());
    assertThat(savedBackupMessage.getAnalysisName())
        .isEqualTo(PRODUCTION_REQUEST_V2.getAnalysisName());
    assertThat(savedBackupMessage.getCreatedAt()).isNotNull();
  }
}
