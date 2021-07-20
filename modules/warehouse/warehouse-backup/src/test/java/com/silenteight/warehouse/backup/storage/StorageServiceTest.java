package com.silenteight.warehouse.backup.storage;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.ProductionDataIndexRequest;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

class StorageServiceTest {

  private static final Value FALSE_POSITIVE_VALUE =
      Value.newBuilder()
          .setStringValue("FALSE_POSITIVE")
          .build();

  private static final String RECOMMENDATION = "recommendation";
  private static final String DISCRIMINATOR = "457b1498-e348-4a81-8093-6079c1173010";
  private static final String REQUEST_ID = "390ad223-58ed-48bb-8d94-2cd11f290c03";
  private static final String ANALYSIS_NAME = "analysis/a373baf7-80ef-443f-b3b2-036c118973e5";

  StorageService underTest;

  InMemoryMessageRepository messageRepository;

  @BeforeEach
  void setUp() {
    messageRepository = new InMemoryMessageRepository();
    underTest = new StorageService(messageRepository);
  }

  @Test
  void shouldSaveProductionDataIndexRequestAsMessage() {
    //given
    ProductionDataIndexRequest productionDataIndexRequest = getProductionDataIndexRequest();
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
    assertThat(savedBackupMessage.getCreatedAt()).isNotNull();
  }

  private ProductionDataIndexRequest getProductionDataIndexRequest() {
    return ProductionDataIndexRequest.newBuilder()
        .addAllAlerts(of(getAlert()))
        .setRequestId(REQUEST_ID)
        .setAnalysisName(ANALYSIS_NAME)
        .build();
  }

  private Alert getAlert() {
    return Alert.newBuilder()
        .setDiscriminator(DISCRIMINATOR)
        .setPayload(Struct.newBuilder()
            .putFields(RECOMMENDATION, FALSE_POSITIVE_VALUE)
            .build())
        .build();
  }
}
