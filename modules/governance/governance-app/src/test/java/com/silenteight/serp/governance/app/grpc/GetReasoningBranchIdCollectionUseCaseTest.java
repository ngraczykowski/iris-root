package com.silenteight.serp.governance.app.grpc;

import com.silenteight.proto.serp.v1.api.GetReasoningBranchIdCollectionRequest;
import com.silenteight.proto.serp.v1.api.GetReasoningBranchIdCollectionResponse;
import com.silenteight.serp.governance.featurevector.FeatureVectorFinder;
import com.silenteight.serp.governance.featurevector.dto.FeatureVectorView;

import com.google.protobuf.ByteString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import javax.annotation.Nonnull;

import static com.silenteight.sep.base.testing.utils.ByteStringTestUtils.createSignature;
import static com.silenteight.sep.base.testing.utils.ByteStringTestUtils.randomSignature;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetReasoningBranchIdCollectionUseCaseTest {

  private static final ByteString FEATURES_SIGNATURE = randomSignature();
  public static final String[] FEATURES_1 = { "NO_DATA", "OUT_OF_RANGE", "WEAK_MATCH" };
  public static final String[] FEATURES_2 = { "NO_DATA", "NO_DATA", "WEAK_MATCH" };
  public static final String[] FEATURES_3 = { "NO_DATA", "OUT_OF_RANGE", "NO_DATA" };
  public static final ByteString SIGNATURE_1 = createSignature("abc");
  public static final ByteString SIGNATURE_2 = createSignature("bca");
  public static final ByteString SIGNATURE_3 = createSignature("cab");

  @Mock
  private FeatureVectorFinder featureVectorFinder;

  private GetReasoningBranchIdCollectionUseCase underTest;

  @BeforeEach
  void setUp() {
    underTest = new GetReasoningBranchIdCollectionUseCase(featureVectorFinder);
  }

  @Test
  void returnResponseWhenSuccess() {
    List<FeatureVectorView> featureVectors = of(
        createFeatureVector(4, SIGNATURE_1, FEATURES_1),
        createFeatureVector(5, SIGNATURE_2, FEATURES_2),
        createFeatureVector(6, SIGNATURE_3, FEATURES_3)
    );

    GetReasoningBranchIdCollectionRequest request =
        createGetReasoningBranchIdCollectionRequest(of(SIGNATURE_1, SIGNATURE_2, SIGNATURE_3));

    when(featureVectorFinder.findByVectorSignatureIn(of(SIGNATURE_1, SIGNATURE_2, SIGNATURE_3)))
        .thenReturn(featureVectors);

    GetReasoningBranchIdCollectionResponse actualResponse =
        underTest.activate(request);

    assertThat(actualResponse.getFeatureVectorIdList()).containsExactly(4L, 5L, 6L);

  }

  private FeatureVectorView createFeatureVector(
      long id, ByteString signature, String... features) {
    return FeatureVectorView
        .builder()
        .id(id)
        .values(of(features))
        .featuresSignature(FEATURES_SIGNATURE)
        .vectorSignature(signature)
        .build();
  }

  @Nonnull
  private GetReasoningBranchIdCollectionRequest createGetReasoningBranchIdCollectionRequest(
      List<ByteString> featureVectorSignatures) {
    return GetReasoningBranchIdCollectionRequest
        .newBuilder()
        .addAllVectorSignatures(featureVectorSignatures)
        .build();
  }
}
