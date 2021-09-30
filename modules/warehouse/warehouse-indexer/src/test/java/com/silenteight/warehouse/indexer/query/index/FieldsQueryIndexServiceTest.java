package com.silenteight.warehouse.indexer.query.index;

import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroKibanaContainer.OpendistroKibanaContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.silenteight.warehouse.indexer.IndexerFixtures.PRODUCTION_ELASTIC_READ_ALIAS_NAME;
import static com.silenteight.warehouse.indexer.IndexerFixtures.PRODUCTION_ELASTIC_WRITE_INDEX_NAME;
import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.DISCRIMINATOR;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.DOCUMENT_ID;
import static java.util.Map.of;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = QueryIndexServiceTestConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroElasticContainerInitializer.class,
    OpendistroKibanaContainerInitializer.class
})
class FieldsQueryIndexServiceTest {

  @Autowired
  SimpleElasticTestClient testClient;

  @Autowired
  FieldsQueryIndexService underTest;

  @BeforeEach
  void init() {
    testClient.createIndexTemplate(
        PRODUCTION_ELASTIC_WRITE_INDEX_NAME, PRODUCTION_ELASTIC_READ_ALIAS_NAME);
  }

  @AfterEach
  void cleanup() {
    testClient.removeIndexTemplate();
    removeData();
  }

  @Test
  void shouldReturnListOfFields() {
    testClient.storeData(PRODUCTION_ELASTIC_WRITE_INDEX_NAME, DOCUMENT_ID, of(
        DISCRIMINATOR, DOCUMENT_ID
    ));

    List<String> fields = underTest.getFieldsList(PRODUCTION_ELASTIC_READ_ALIAS_NAME);

    assertThat(fields).containsExactlyInAnyOrder(DISCRIMINATOR);
  }

  private void removeData() {
    testClient.removeIndex(PRODUCTION_ELASTIC_WRITE_INDEX_NAME);
  }
}
