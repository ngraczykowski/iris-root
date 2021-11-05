package com.silenteight.adjudication.api.library.v1.dataset;

import com.silenteight.adjudication.api.library.v1.GrpcServerExtension;
import com.silenteight.adjudication.api.v1.CreateDatasetRequest;
import com.silenteight.adjudication.api.v1.Dataset;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceImplBase;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class DatasetGrpcAdapterTest {

  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();

  private DatasetGrpcAdapter underTest;

  @BeforeEach
  public void setup() {
    grpcServerExtension.addService(new MockedDatasetServiceGrpcServer());
    var stub = DatasetServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());
    underTest = new DatasetGrpcAdapter(stub, 1L);
  }

  @Test
  @DisplayName("should create dataset")
  void createDataset() {
    //given
    var alerts = List.of("alerts/1");

    //when
    var response = underTest.createDataset(alerts);

    //then
    assertThat(response).isEqualTo("datasetName");
  }

  static class MockedDatasetServiceGrpcServer extends DatasetServiceImplBase {

    @Override
    public void createDataset(
        CreateDatasetRequest request, StreamObserver<Dataset> responseObserver) {
      responseObserver.onNext(Dataset.newBuilder().setName("datasetName").build());
      responseObserver.onCompleted();
    }
  }
}
