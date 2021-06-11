package com.silenteight.simulator.dataset.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.simulator.dataset.domain.DatasetState.CURRENT;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.CREATE_DATASET_REQUEST;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.DATASET;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.EXTERNAL_RESOURCE_NAME;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.ID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatasetMetadataServiceTest {

  @InjectMocks
  private DatasetMetadataService underTest;

  @Mock
  DatasetEntityRepository datasetEntityRepository;

  @Test
  void createMetadata() {
    // when
    underTest.createMetadata(CREATE_DATASET_REQUEST, DATASET);

    // then
    ArgumentCaptor<DatasetEntity> captor = ArgumentCaptor.forClass(DatasetEntity.class);
    verify(datasetEntityRepository).save(captor.capture());
    DatasetEntity value = captor.getValue();
    assertThat(value.getDatasetId()).isEqualTo(ID);
    assertThat(value.getState()).isEqualTo(CURRENT);
    assertThat(value.getExternalResourceName()).isEqualTo(EXTERNAL_RESOURCE_NAME);
  }
}
