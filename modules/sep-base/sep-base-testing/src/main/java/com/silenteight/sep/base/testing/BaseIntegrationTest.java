package com.silenteight.sep.base.testing;

import com.silenteight.sep.base.common.messaging.IntegrationConfiguration;
import com.silenteight.sep.base.common.messaging.MessagingConfiguration;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.test.context.MockIntegrationContext;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@SpringIntegrationTest
@ContextConfiguration(initializers = { RabbitTestInitializer.class })
@ImportAutoConfiguration({
    IntegrationConfiguration.class,
    MessagingConfiguration.class,
    RabbitAutoConfiguration.class,
})
@SuppressWarnings("squid:S1694")
public abstract class BaseIntegrationTest {

  @Autowired
  protected MockIntegrationContext integrationContext;
}
