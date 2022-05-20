package com.silenteight.payments.bridge.data.retention.service;

import com.silenteight.payments.bridge.data.retention.adapter.FileDataRetentionAccessPort;
import com.silenteight.payments.bridge.data.retention.model.FileDataRetention;
import com.silenteight.payments.bridge.data.retention.port.CreateFileRetentionUseCase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateFileRetentionServiceTest {


  private CreateFileRetentionUseCase createFileRetentionUseCase;
  @Mock
  private FileDataRetentionAccessPort fileDataRetentionAccessPort;

  @BeforeEach
  void setUp() {
    createFileRetentionUseCase = new CreateFileRetentionService(fileDataRetentionAccessPort);
  }

  @Test
  void shouldSaveFileRetention() {
    createFileRetentionUseCase.create(
        List.of(FileDataRetention.builder().fileName("newfile").build()));
    verify(fileDataRetentionAccessPort, times(1)).create(any());
  }
}
