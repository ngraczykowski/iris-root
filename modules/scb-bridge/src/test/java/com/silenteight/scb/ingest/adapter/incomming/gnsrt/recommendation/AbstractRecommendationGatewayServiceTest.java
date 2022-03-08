package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation;

import com.silenteight.proto.serp.v1.alert.Alert;
import com.silenteight.proto.serp.v1.api.ReactorRecommendationGatewayGrpc;
import com.silenteight.proto.serp.v1.api.ReactorRecommendationGatewayGrpc.ReactorRecommendationGatewayStub;
import com.silenteight.proto.serp.v1.api.ReactorRecommendationGatewayGrpc.RecommendationGatewayImplBase;
import com.silenteight.proto.serp.v1.common.ObjectId;
import com.silenteight.sep.base.testing.grpc.GrpcServerExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.RegisterExtension;

@TestInstance(Lifecycle.PER_METHOD)
abstract class AbstractRecommendationGatewayServiceTest {

  protected static int deadlineInSeconds = 2;
  protected static RecommendationGatewayService underTest;
  @RegisterExtension
  GrpcServerExtension grpcServer = new GrpcServerExtension().directExecutor();

  static Alert createAlert(String id) {
    var objectId = ObjectId.newBuilder()
        .setSourceId(id)
        .setDiscriminator("discriminator")
        .build();

    return Alert.newBuilder()
        .setId(objectId)
        .build();
  }

  @BeforeEach
  public void setup() {
    grpcServer.addService(getMockedService());

    ReactorRecommendationGatewayStub stub = ReactorRecommendationGatewayGrpc
        .newReactorStub(grpcServer.getChannel());

    underTest = new RecommendationGatewayService(stub, deadlineInSeconds);
  }

  abstract RecommendationGatewayImplBase getMockedService();
}
