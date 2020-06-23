package com.silenteight.sens.webapp.grpc.feature;

import com.silenteight.proto.serp.v1.api.FeatureCollectionResponse;
import com.silenteight.proto.serp.v1.api.FeatureGovernanceGrpc.FeatureGovernanceBlockingStub;
import com.silenteight.proto.serp.v1.model.Feature;
import com.silenteight.proto.serp.v1.model.FeatureCollection;
import com.silenteight.sens.webapp.grpc.GrpcCommunicationException;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.sens.webapp.grpc.GrpcFixtures.NOT_FOUND_RUNTIME_EXCEPTION;
import static com.silenteight.sens.webapp.grpc.GrpcFixtures.OTHER_STATUS_RUNTIME_EXCEPTION;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GrpcFeatureNamesQueryTest {

  @Mock
  private FeatureGovernanceBlockingStub featureStub;

  @InjectMocks
  private GrpcFeatureNamesQuery underTest;

  @Test
  void returnsFeatureNames() {
    long featureVectorId = 2L;
    given(featureStub.getBranchFeatureCollection(
        argThat(r -> r.getFeatureVectorId() == featureVectorId)))
        .willReturn(featureCollectionResponseWithFeaturesOf("featA", "featB"));

    assertThat(underTest.findFeatureNames(featureVectorId)).containsExactly("featA", "featB");
  }

  @Test
  void returnsEmptyList_whenGrpcThrowsNotFoundStatusException_requestingFeatureNames() {
    given(featureStub.getBranchFeatureCollection(any())).willThrow(NOT_FOUND_RUNTIME_EXCEPTION);

    assertThat(underTest.findFeatureNames(1)).isEmpty();
  }

  @Test
  void throwsGrpcException_whenGrpcThrowsNotFoundStatusException_requestingFeatureNames() {
    given(featureStub.getBranchFeatureCollection(any())).willThrow(OTHER_STATUS_RUNTIME_EXCEPTION);

    ThrowingCallable featureNamesCall = () -> underTest.findFeatureNames(1);

    assertThatThrownBy(featureNamesCall).isInstanceOf(GrpcCommunicationException.class);
  }

  private FeatureCollectionResponse featureCollectionResponseWithFeaturesOf(
      String... featureNames) {
    return FeatureCollectionResponse
        .newBuilder()
        .setFeatureCollection(
            FeatureCollection
                .newBuilder()
                .addAllFeatures(featuresWithNames(featureNames))
                .build())
        .build();
  }

  private List<Feature> featuresWithNames(String... featureNames) {
    return stream(featureNames)
        .map(featureFriendlyName ->
            Feature
                .newBuilder()
                .setName(featureFriendlyName)
                .build())
        .collect(toList());
  }
}
