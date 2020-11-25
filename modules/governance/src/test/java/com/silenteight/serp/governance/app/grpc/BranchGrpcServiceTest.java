package com.silenteight.serp.governance.app.grpc;

import com.silenteight.proto.serp.v1.api.BranchGovernanceGrpc.BranchGovernanceBlockingStub;
import com.silenteight.proto.serp.v1.api.GetReasoningBranchIdCollectionRequest;
import com.silenteight.proto.serp.v1.api.GetReasoningBranchIdCollectionResponse;
import com.silenteight.sep.base.testing.grpc.GrpcServerExtension;

import com.google.protobuf.ByteString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.proto.serp.v1.api.BranchGovernanceGrpc.newBlockingStub;
import static com.silenteight.sep.base.testing.utils.ByteStringTestUtils.createSignature;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BranchGrpcServiceTest {

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

  private BranchGovernanceBlockingStub branchStub;

  @BeforeEach
  void setUp() {
    BranchGrpcService branchGrpcService = new BranchGrpcService(
        listReasoningBranchesUseCase,
        getReasoningBranchUseCase,
        getVectorSolutionsUseCase,
        getReasoningBranchIdCollectionUseCase);

    grpcServer.addService(branchGrpcService);
    branchStub = newBlockingStub(grpcServer.getChannel());
  }

  @Nested
  class GetReasoningBranchIdsTest {

    private final ByteString signature1 = createSignature("abc");
    private final ByteString signature2 = createSignature("bca");
    private final ByteString signature3 = createSignature("cab");

    private final GetReasoningBranchIdCollectionRequest request =
        GetReasoningBranchIdCollectionRequest.newBuilder()
            .addVectorSignatures(signature1)
            .addVectorSignatures(signature2)
            .addVectorSignatures(signature3)
            .build();

    private final GetReasoningBranchIdCollectionResponse response =
        GetReasoningBranchIdCollectionResponse.newBuilder()
            .addFeatureVectorId(4)
            .addFeatureVectorId(5)
            .addFeatureVectorId(6)
            .build();

    @Test
    void returnResponseWhenSuccess() {
      when(getReasoningBranchIdCollectionUseCase.activate(request))
          .thenReturn(response);

      GetReasoningBranchIdCollectionResponse actualResponse =
          branchStub.getReasoningBranchIdCollection(request);

      assertThat(actualResponse).isEqualTo(response);
    }
  }
}
