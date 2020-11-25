package com.silenteight.serp.governance.app.grpc;

import com.silenteight.proto.serp.v1.api.DecisionTreeGovernanceGrpc.DecisionTreeGovernanceBlockingStub;
import com.silenteight.proto.serp.v1.api.DecisionTreeResponse;
import com.silenteight.proto.serp.v1.api.GetDecisionTreeRequest;
import com.silenteight.proto.serp.v1.api.ListDecisionTreesResponse;
import com.silenteight.proto.serp.v1.governance.DecisionTreeSummary;
import com.silenteight.sep.base.testing.grpc.GrpcServerExtension;
import com.silenteight.serp.governance.app.grpc.GetDecisionTreeUseCase.GetDecisionTreeUseCaseListener;

import com.google.protobuf.Empty;
import com.google.rpc.Code;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;

import static com.silenteight.proto.serp.v1.api.DecisionTreeGovernanceGrpc.newBlockingStub;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DecisionTreeGrpcServiceTest {

  @RegisterExtension
  GrpcServerExtension grpcServer = new GrpcServerExtension().directExecutor();

  @Mock
  private GetDecisionTreeUseCase getUseCase;
  @Mock
  private ListDecisionTreesUseCase listUseCase;

  private DecisionTreeGovernanceBlockingStub stub;

  @BeforeEach
  void setUp() {
    DecisionTreeGrpcService service = new DecisionTreeGrpcService(getUseCase, listUseCase);

    grpcServer.addService(service);
    stub = newBlockingStub(grpcServer.getChannel());
  }

  ObjectAssert<Status> assertStatusException(Executable executable) {
    Status status = assertThrows(StatusRuntimeException.class, executable).getStatus();
    return assertThat(status);
  }

  @Nested
  class GetDecisionTreeTest {

    GetDecisionTreeRequest getRequest = GetDecisionTreeRequest.newBuilder()
        .setDecisionTreeId(1L)
        .build();

    DecisionTreeResponse decisionTreeResponse = DecisionTreeResponse.newBuilder()
        .setDecisionTree(DecisionTreeSummary.newBuilder()
            .setName("decisionTreeName")
            .build())
        .build();

    @Test
    void returnResponse_whenDecisionTreeFound() {
      mockAction(getRequest, l -> l.onDecisionTreeFound(decisionTreeResponse));

      DecisionTreeResponse response = stub.getDecisionTree(getRequest);

      assertThat(response).isEqualTo(decisionTreeResponse);
    }

    @Test
    void throwStatusRuntimeExceptionWithInvalidArgumentCode_whenMissingDecisionTreeSpec() {
      mockAction(getRequest, GetDecisionTreeUseCaseListener::onMissingDecisionTreeSpec);

      assertStatusException(() -> stub.getDecisionTree(getRequest))
          .extracting(s -> s.getCode().value())
          .isEqualTo(Code.INVALID_ARGUMENT_VALUE);
    }

    @Test
    void throwStatusRuntimeExceptionWithNotFoundCode_whenDecisionTreeNotFound() {
      mockAction(getRequest, GetDecisionTreeUseCaseListener::onDecisionTreeNotFound);

      assertStatusException(() -> stub.getDecisionTree(getRequest))
          .extracting(s -> s.getCode().value())
          .isEqualTo(Code.NOT_FOUND_VALUE);
    }

    @Test
    void statusRuntimeExceptionWithUnknownCode_whenThrownException() {
      mockAction(getRequest, l -> {
        throw new RuntimeException();
      });

      assertStatusException(() -> stub.getDecisionTree(getRequest))
          .extracting(s -> s.getCode().value())
          .isEqualTo(Code.UNKNOWN_VALUE);
    }

    private void mockAction(
        GetDecisionTreeRequest expected,
        Consumer<GetDecisionTreeUseCaseListener> listenerConsumer) {

      doAnswer(i -> {
        GetDecisionTreeRequest request = i.getArgument(0);
        assertThat(request).isEqualTo(expected);
        listenerConsumer.accept(i.getArgument(1));
        return null;
      }).when(getUseCase).activate(any(), any());
    }
  }

  @Nested
  class ListDecisionTreesTest {

    private ListDecisionTreesResponse listResponse = ListDecisionTreesResponse.newBuilder()
        .addDecisionTree(DecisionTreeSummary.newBuilder()
            .setName("decisionTree")
            .build())
        .build();

    @Test
    void returnResponseWhenSuccess() {
      when(listUseCase.activate()).thenReturn(listResponse);

      ListDecisionTreesResponse response = stub.listDecisionTrees(Empty.getDefaultInstance());

      assertThat(response).isEqualTo(listResponse);
    }

    @Test
    void statusRuntimeExceptionWithUnknownCode_whenThrownException() {
      when(listUseCase.activate()).thenThrow(RuntimeException.class);

      assertStatusException(() -> stub.listDecisionTrees(Empty.getDefaultInstance()))
          .extracting(s -> s.getCode().value())
          .isEqualTo(Code.UNKNOWN_VALUE);
    }
  }
}
