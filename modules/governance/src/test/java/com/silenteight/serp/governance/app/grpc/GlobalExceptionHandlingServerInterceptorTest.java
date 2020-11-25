package com.silenteight.serp.governance.app.grpc;

import com.silenteight.proto.serp.v1.api.BranchGovernanceGrpc.BranchGovernanceBlockingStub;
import com.silenteight.proto.serp.v1.api.GetReasoningBranchRequest;
import com.silenteight.sep.base.common.grpc.EntityNotFoundGrpcExceptionHandler;
import com.silenteight.sep.base.common.grpc.GlobalExceptionHandlingServerInterceptor;
import com.silenteight.sep.base.common.grpc.StatusRuntimeGrpcExceptionHandler;
import com.silenteight.sep.base.testing.grpc.GrpcServerExtension;

import io.grpc.ServerInterceptors;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;

import static com.silenteight.proto.serp.v1.api.BranchGovernanceGrpc.newBlockingStub;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

// FIXME(ahaczewski): This test must be moved to serp-common, but there are no
@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlingServerInterceptorTest {

  @RegisterExtension
  GrpcServerExtension grpcServer = new GrpcServerExtension().directExecutor();

  @Mock
  private ListReasoningBranchesUseCase listReasoningBranchesUseCase;
  @Mock
  private GetReasoningBranchUseCase getReasoningBranchUseCase;
  @Mock
  private GetVectorSolutionsUseCase getVectorSolutionsUseCase;
  @Mock
  private GetReasoningBranchIdCollectionUseCase getReasoningBranchIdCollectionUseCase;

  private GetReasoningBranchRequest request;

  private BranchGovernanceBlockingStub branchStub;

  @BeforeEach
  void setUp() {
    BranchGrpcService branchGrpcService = new BranchGrpcService(
        listReasoningBranchesUseCase,
        getReasoningBranchUseCase,
        getVectorSolutionsUseCase,
        getReasoningBranchIdCollectionUseCase);

    var interceptor = new GlobalExceptionHandlingServerInterceptor(asList(
        new EntityNotFoundGrpcExceptionHandler(),
        new StatusRuntimeGrpcExceptionHandler()));

    grpcServer.addService(ServerInterceptors.intercept(
        branchGrpcService, interceptor));

    branchStub = newBlockingStub(grpcServer.getChannel());

    request = GetReasoningBranchRequest.newBuilder().build();
  }

  @Test
  void shouldHandleEntityNotFoundExceptionAndMapItToStatusNotFound() {
    //given
    when(getReasoningBranchUseCase.activate(request)).thenThrow(
        EntityNotFoundException.class);

    //when
    StatusRuntimeException statusRuntimeException =
        assertThrows(
            StatusRuntimeException.class, () -> branchStub.getReasoningBranch(request));

    //then
    assertThat(statusRuntimeException.getStatus().getCode())
        .isEqualTo(Status.NOT_FOUND.getCode());
  }


  @Test
  void shouldHandleExceptionDifferentThenEntityNotFoundExceptionAndMapItToStatusUnknown() {
    //given
    when(getReasoningBranchUseCase.activate(request)).thenThrow(
        RuntimeException.class);

    //when
    StatusRuntimeException statusRuntimeException =
        assertThrows(
            StatusRuntimeException.class, () -> branchStub.getReasoningBranch(request));

    //then
    assertThat(statusRuntimeException.getStatus().getCode()).isEqualTo(Status.UNKNOWN.getCode());
  }

  @Test
  void propagatesStatusFromStatusRuntimeException() {
    //given
    when(getReasoningBranchUseCase.activate(request)).thenThrow(
        new StatusRuntimeException(Status.ABORTED));

    //when
    StatusRuntimeException statusRuntimeException =
        assertThrows(
            StatusRuntimeException.class, () -> branchStub.getReasoningBranch(request));

    //then
    assertThat(statusRuntimeException.getStatus().getCode()).isEqualTo(Status.ABORTED.getCode());
  }
}
