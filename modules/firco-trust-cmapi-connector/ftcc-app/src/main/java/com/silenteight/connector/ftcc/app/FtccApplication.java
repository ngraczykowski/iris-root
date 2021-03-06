package com.silenteight.connector.ftcc.app;

import com.silenteight.connector.ftcc.app.amqp.AmqpListenerModule;
import com.silenteight.connector.ftcc.app.grpc.GrpcModule;
import com.silenteight.connector.ftcc.callback.CallbackModule;
import com.silenteight.connector.ftcc.common.database.DatabaseModule;
import com.silenteight.connector.ftcc.common.integration.AmqpCommonModule;
import com.silenteight.connector.ftcc.ingest.IngestModule;
import com.silenteight.connector.ftcc.request.RequestModule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

import static org.springframework.context.annotation.AdviceMode.ASPECTJ;

@EnableAutoConfiguration
@EnableRetry
@EnableAsync(mode = ASPECTJ)
@ComponentScan(basePackageClasses = {
    // Domain modules
    CallbackModule.class,
    IngestModule.class,
    RequestModule.class,
    // Interface modules
    AmqpCommonModule.class,
    AmqpListenerModule.class,
    DatabaseModule.class,
    GrpcModule.class
})
public class FtccApplication {

  public static void main(String[] args) {
    SpringApplication.run(FtccApplication.class, args);
  }
}
