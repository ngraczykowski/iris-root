package com.silenteight.hsbc.bridge.adjudication;

import com.silenteight.adjudication.api.v1.CreateDatasetRequest;
import com.silenteight.adjudication.api.v1.Dataset;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceImplBase;
import com.silenteight.hsbc.bridge.common.GrpcServerExtension;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class DatasetServiceApiGrpcTest {

  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();

  private DatasetServiceApiGrpc underTest;

  @BeforeEach
  public void setup() {
    grpcServerExtension.addService(new MockedDatasetServiceGrpcServer());

    var stub = DatasetServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());

    underTest = new DatasetServiceApiGrpc(stub, 1L);
  }

  @Test
  public void createDataset() {
    //given
    var alerts = List.of("alerts/1");

    //when
    var response = underTest.createDataset(alerts);

    //then
    assertThat(response).isEqualTo("dummyDataset");
  }

  class MockedDatasetServiceGrpcServer extends DatasetServiceImplBase {

    @Override
    public void createDataset(
        CreateDatasetRequest request, StreamObserver<Dataset> responseObserver) {

      responseObserver.onNext(Dataset.newBuilder()
          .setName("dummyDataset")
          .build());
      responseObserver.onCompleted();
    }
  }
}
