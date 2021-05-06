package com.silenteight.warehouse.common.opendistro.kibana;

import com.silenteight.warehouse.common.opendistro.kibana.dto.SavedObjectDto;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroKibanaContainer.OpendistroKibanaContainerInitializer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.silenteight.warehouse.common.opendistro.kibana.SavedObjectType.KIBANA_INDEX_PATTERN;
import static com.silenteight.warehouse.common.opendistro.kibana.dto.KibanaIndexFixture.ATTRIBUTES;
import static com.silenteight.warehouse.common.opendistro.kibana.dto.KibanaIndexFixture.SAVED_OBJECT_ID;
import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.TENANT;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = OpendistroKibanaTestConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroElasticContainerInitializer.class,
    OpendistroKibanaContainerInitializer.class
})
class OpendistroKibanaClientTest {

  @Autowired
  OpendistroKibanaClient opendistroKibanaClient;

  @Test
  void shouldCreateKibanaIndexPattern() {
    SavedObject kibanaIndex = SavedObject.builder()
        .attributes(ATTRIBUTES)
        .build();
    opendistroKibanaClient
        .createSavedObjects(TENANT, KIBANA_INDEX_PATTERN, SAVED_OBJECT_ID, kibanaIndex);

    List<SavedObjectDto> savedObjects =
        opendistroKibanaClient.listSavedObjects(TENANT, KIBANA_INDEX_PATTERN, 20);

    assertThat(savedObjects).hasSize(1);
    SavedObjectDto savedObject = savedObjects.get(0);
    assertThat(savedObject.getId()).isEqualTo(SAVED_OBJECT_ID);
    assertThat(savedObject.getAttributes()).containsExactlyInAnyOrderEntriesOf(ATTRIBUTES);
  }
}
