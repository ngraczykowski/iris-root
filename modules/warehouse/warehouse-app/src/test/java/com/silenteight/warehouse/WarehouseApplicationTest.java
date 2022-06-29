package com.silenteight.warehouse;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = WarehouseApplication.class)
@ContextConfiguration(initializers = {PostgresTestInitializer.class, RabbitTestInitializer.class})
class WarehouseApplicationTest {

  @Test
  @SuppressWarnings("squid:S2699")
  void testContext() {
  }
}
