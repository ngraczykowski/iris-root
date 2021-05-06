package com.silenteight.warehouse.common.opendistro.elastic;

import lombok.SneakyThrows;

import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroKibanaContainer.OpendistroKibanaContainerInitializer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = OpendistroElasticTestConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroKibanaContainerInitializer.class,
    OpendistroElasticContainerInitializer.class
})
class OpendistroElasticClientTest {

  private static final String TEST_TENANT_ID = "test_tenant";
  private static final String TEST_TENANT_DESCRIPTION = "description";

  @Autowired
  OpendistroElasticClient opendistroElasticClient;

  @Test
  @SneakyThrows
  void shouldReturnAddedTenant() {
    //given
    opendistroElasticClient.createTenant(TEST_TENANT_ID, TEST_TENANT_DESCRIPTION);

    //when
    var tenantsNameList = opendistroElasticClient.getTenantsList();

    //then
    assertThat(tenantsNameList).contains(TEST_TENANT_ID);
  }
}
