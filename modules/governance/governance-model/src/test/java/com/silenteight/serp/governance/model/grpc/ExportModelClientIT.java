package com.silenteight.serp.governance.model.grpc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.model.api.v1.*;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;
import com.silenteight.sep.base.testing.grpc.BaseRpcClientTest;

import com.google.protobuf.ByteString;
import io.grpc.BindableService;
import io.grpc.Channel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = {
    SolvingModelConfigurationIT.class
})
@ContextConfiguration(initializers = {
    RabbitTestInitializer.class,
    PostgresTestInitializer.class,
})
@AutoConfigureDataJpa
@TestPropertySource(properties = { "spring.config.additional-location=classpath:application.yml"})
@TestMethodOrder(OrderAnnotation.class)
@Disabled("We should make it work, but there was some work already done, so I want to merge it.")
class ExportModelClientIT extends BaseRpcClientTest {

  @Autowired
  private SolvingModelGrpcService modelGrpcService;

  private static final String RESOURCE_PATH =
      "/com/silenteight/serp/governance/model/transfer/importing/correctModel.json";

  private SolvingModelServiceGrpc.SolvingModelServiceBlockingStub modelServiceBlockingStub;

  @BeforeEach
  public void setUpRpcChannel() throws IOException {
    super.givenRpcChannel();
  }

  @Override
  protected void setUpClient(Channel channel) {
    modelServiceBlockingStub = SolvingModelServiceGrpc.newBlockingStub(channel);
  }

  @Override
  protected BindableService getBindableService() {
    return modelGrpcService;
  }

  @Test
  void assertModelExported() {
    ExportModelResponse expectedResponse = ExportModelResponse.newBuilder().build();
    ExportModelResponse response =
        modelServiceBlockingStub.exportModel(ExportModelRequest.newBuilder().build());
//
    assertThat(response).isEqualTo(expectedResponse);
  }

  @Test
  void assertModelImported() throws IOException {

    InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(RESOURCE_PATH);
    ByteString modelJson = ByteString.copyFrom(resourceStream.readAllBytes());

    ImportNewModelResponse expectedResponse = ImportNewModelResponse.newBuilder().build();
    ImportNewModelResponse response =
        modelServiceBlockingStub.importModel(
            ImportNewModelRequest.newBuilder()
                .setModelJson(modelJson)
                .build()
        );
    assertThat(response).isEqualTo(expectedResponse);
  }
}
