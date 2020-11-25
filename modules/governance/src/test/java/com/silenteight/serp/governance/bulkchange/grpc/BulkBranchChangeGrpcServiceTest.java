package com.silenteight.serp.governance.bulkchange.grpc;

import lombok.Getter;
import lombok.SneakyThrows;

import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesRequest;
import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesResponse;
import com.silenteight.proto.serp.v1.api.ValidateBulkChangeRequest;
import com.silenteight.proto.serp.v1.api.ValidateBulkChangeResponse;
import com.silenteight.serp.governance.bulkchange.BulkChangeQueries;
import com.silenteight.serp.governance.bulkchange.ListBulkBranchChangeViewTestFixtures;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("gRCP service test suite")
class BulkBranchChangeGrpcServiceTest {

  @Mock
  private BulkChangeQueries bulkChangeQueries;

  @InjectMocks
  private BulkBranchChangeGrpcService underTest;

  @Test
  void shouldReturnAppliedChanges() {
    //Given
    ListBulkBranchChangesRequest request = ListBulkBranchChangeViewTestFixtures.request();
    FakeObserver<ListBulkBranchChangesResponse> responseObserver = new FakeObserver<>();

    //When
    underTest.listBulkBranchChanges(request, responseObserver);

    //Then
    verify(bulkChangeQueries).listBulkBranchChanges(request);
    assertThat(responseObserver.isCompleted()).isTrue();
  }

  @Test
  void shouldReturnAllChanges() {
    //Given
    ValidateBulkChangeRequest request = ValidateBulkChangeRequest.newBuilder().build();
    FakeObserver<ValidateBulkChangeResponse> responseObserver = new FakeObserver<>();

    //When
    underTest.validateBulkChange(request, responseObserver);

    //Then
    verify(bulkChangeQueries).validateBulkBranchChange(request);
    assertThat(responseObserver.isCompleted()).isTrue();
  }

  @Getter
  private static class FakeObserver<T> implements StreamObserver<T> {

    private final List<T> responses = new ArrayList<>();
    private boolean completed = false;

    @Override
    public void onNext(T value) {
      responses.add(value);
    }

    @SneakyThrows
    @Override
    public void onError(Throwable t) {
      throw t;
    }

    @Override
    public void onCompleted() {
      completed = true;
    }
  }
}
