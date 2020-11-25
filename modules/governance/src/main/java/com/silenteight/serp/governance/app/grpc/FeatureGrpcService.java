package com.silenteight.serp.governance.app.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.api.FeatureCollectionResponse;
import com.silenteight.proto.serp.v1.api.FeatureGovernanceGrpc;
import com.silenteight.proto.serp.v1.api.GetBranchFeatureCollectionRequest;
import com.silenteight.proto.serp.v1.api.GetFeatureCollectionRequest;
import com.silenteight.proto.serp.v1.model.Feature;
import com.silenteight.proto.serp.v1.model.FeatureCollection;
import com.silenteight.serp.governance.featureset.FeatureSetFinder;
import com.silenteight.serp.governance.featureset.dto.FeatureSetViewDto;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
class FeatureGrpcService extends FeatureGovernanceGrpc.FeatureGovernanceImplBase {

  private final FeatureSetFinder featureSetFinder;

  @Override
  public void getFeatureCollection(GetFeatureCollectionRequest request,
      StreamObserver<FeatureCollectionResponse> responseObserver) {

    FeatureSetViewDto features =
        featureSetFinder.getByFeaturesSignature(request.getFeaturesSignature());

    responseObserver.onNext(mapToFeatureCollectionResponse(features));
    responseObserver.onCompleted();
  }

  @Override
  public void getBranchFeatureCollection(
      GetBranchFeatureCollectionRequest request,
      StreamObserver<FeatureCollectionResponse> responseObserver) {

    FeatureSetViewDto features =
        featureSetFinder.getByFeatureVectorId(request.getFeatureVectorId());

    responseObserver.onNext(mapToFeatureCollectionResponse(features));
    responseObserver.onCompleted();
  }

  private static FeatureCollectionResponse mapToFeatureCollectionResponse(
      FeatureSetViewDto featureSet) {
    return FeatureCollectionResponse.newBuilder()
        .setFeatureSetId(featureSet.getId())
        .setFeatureCollection(mapFeatures(featureSet.getFeatures()))
        .build();
  }

  private static FeatureCollection mapFeatures(List<String> featureNames) {
    List<Feature> features = featureNames.stream()
        .map(f -> Feature.newBuilder()
            .setName(f)
            .build())
        .collect(Collectors.toList());

    return FeatureCollection.newBuilder()
        .addAllFeatures(features)
        .build();
  }
}
