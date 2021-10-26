package com.silenteight.warehouse.backup.storage;

import lombok.SneakyThrows;

import com.silenteight.data.api.v2.ProductionDataIndexRequest;

import com.google.protobuf.util.JsonFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.silenteight.warehouse.backup.storage.ProductionDataIndexRequestFixtures.PRODUCTION_DATA_INDEX_REQUEST_1;
import static org.assertj.core.api.Assertions.*;

class StorageServiceWithDiagnosticTest {

  StorageService underTest;

  InMemoryBackupMessageRepository messageRepository;

  @BeforeEach
  void setUp() {
    messageRepository = new InMemoryBackupMessageRepository();
    underTest = new StorageService(messageRepository, true);
  }

  @SneakyThrows
  @Test
  void shouldSaveProductionDataIndexRequestAsMessage() {
    //given
    ProductionDataIndexRequest productionDataIndexRequest = PRODUCTION_DATA_INDEX_REQUEST_1;
    assertThat(messageRepository.count()).isZero();
    //when
    underTest.save(productionDataIndexRequest);
    //then
    assertThat(messageRepository.count()).isEqualTo(1);
    BackupMessage savedBackupMessage = messageRepository.findOne();
    assertThat(savedBackupMessage).isNotNull();
    assertThat(productionDataIndexRequest.toByteArray()).isEqualTo(savedBackupMessage.getData());
    assertThat(savedBackupMessage.getRequestId())
        .isEqualTo(productionDataIndexRequest.getRequestId());
    assertThat(savedBackupMessage.getAnalysisName())
        .isEqualTo(productionDataIndexRequest.getAnalysisName());
    assertThat(savedBackupMessage.getDiagnostic()).isEqualTo(
        JsonFormat.printer().print(PRODUCTION_DATA_INDEX_REQUEST_1));
    assertThat(savedBackupMessage.getCreatedAt()).isNotNull();
  }
}
