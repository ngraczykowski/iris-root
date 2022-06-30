package com.silenteight.agent.facade.exchange;

import org.springframework.context.annotation.Import;


@Import(MultiFacadeRabbitBrokerTestConfiguration.class)
public abstract class MultiFacadeRabbitWithGrpcConfigIntegrationTest
    extends CommonRabbitWithGrpcConfigIntegrationTest {

}
