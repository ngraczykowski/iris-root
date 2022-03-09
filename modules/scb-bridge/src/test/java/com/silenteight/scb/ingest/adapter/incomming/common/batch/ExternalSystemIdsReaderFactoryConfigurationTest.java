package com.silenteight.scb.ingest.adapter.incomming.common.batch;

import com.silenteight.scb.ingest.adapter.incomming.cbs.batch.ScbBridgeConfigProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.quartz.ScbBridgeAlertLevelLearningJobProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.quartz.ScbBridgeWatchlistLevelLearningJobProperties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExternalSystemIdsReaderFactoryConfigurationTest {

  private static final String WL_DB_RELATION_NAME = "WL_DB_RELATION_NAME";
  private static final String AL_DB_RELATION_NAME = "AL_DB_RELATION_NAME";

  private ExternalSystemIdsReaderFactoryConfiguration externalSystemIdsReaderFactoryConfiguration;
  @Spy
  private ScbBridgeWatchlistLevelLearningJobProperties watchlistLevelLearningJobProperties;
  @Spy
  private ScbBridgeAlertLevelLearningJobProperties alertLevelLearningJobProperties;

  @BeforeEach
  void setUp() {
    watchlistLevelLearningJobProperties.setDbRelationName(WL_DB_RELATION_NAME);
    alertLevelLearningJobProperties.setDbRelationName(AL_DB_RELATION_NAME);

    ScbBridgeConfigProperties scbBridgeConfigProperties = new ScbBridgeConfigProperties();
    scbBridgeConfigProperties.setChunkSize(1);
    scbBridgeConfigProperties.setQueryTimeout(1);

    externalSystemIdsReaderFactoryConfiguration =
        new ExternalSystemIdsReaderFactoryConfiguration(scbBridgeConfigProperties,
            watchlistLevelLearningJobProperties,
            alertLevelLearningJobProperties, null, null);
  }

  @Test
  void shouldGetAlertLevelDbRelationNameFromAlertLevelProperties() {
    //when
    externalSystemIdsReaderFactoryConfiguration.alertLevelLearningSystemIdsReaderFactory(null);

    //then
    verify(alertLevelLearningJobProperties, times(1)).getDbRelationName();
  }

  @Test
  void shouldGetWatchlistLevelDbRelationNameFromWatchlistLevelProperties() {
    //when
    externalSystemIdsReaderFactoryConfiguration.watchlistLevelLearningSystemIdsReaderFactory(null);

    //then
    verify(watchlistLevelLearningJobProperties, times(1)).getDbRelationName();
  }

}
