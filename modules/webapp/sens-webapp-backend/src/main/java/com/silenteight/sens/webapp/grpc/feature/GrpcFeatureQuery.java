package com.silenteight.sens.webapp.grpc.feature;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.api.FeatureGovernanceGrpc.FeatureGovernanceBlockingStub;
import com.silenteight.proto.serp.v1.api.GetBranchFeatureCollectionRequest;
import com.silenteight.proto.serp.v1.model.Feature;
import com.silenteight.sens.webapp.backend.deprecated.reasoningbranch.report.FeatureQuery;
import com.silenteight.sens.webapp.grpc.GrpcCommunicationException;

import io.vavr.control.Try;

import java.util.List;

import static com.google.rpc.Code.NOT_FOUND;
import static com.silenteight.sens.webapp.grpc.GrpcCommunicationException.codeIs;
import static com.silenteight.sens.webapp.grpc.GrpcCommunicationException.mapStatusExceptionsToCommunicationException;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.of;
import static io.vavr.control.Try.success;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class GrpcFeatureQuery implements FeatureQuery {

  private final FeatureGovernanceBlockingStub featuresStub;

  @Override
  public List<String> findFeaturesNames(long reasoningBranchId) {
    GetBranchFeatureCollectionRequest request = GetBranchFeatureCollectionRequest
        .newBuilder()
        .setFeatureVectorId(reasoningBranchId)
        .build();

    Try<List<String>> features =
        of(() -> featuresStub
            .getBranchFeatureCollection(request)
            .getFeatureCollection()
            .getFeaturesList()
            .stream()
            .map(Feature::getName)
            .collect(toList()));

    return mapStatusExceptionsToCommunicationException(features)
        .recoverWith(
            GrpcCommunicationException.class,
            exception -> Match(exception).of(
                Case($(codeIs(NOT_FOUND)), () -> success(emptyList())),
                Case($(), () -> failure(exception))))
        .get();
  }
}
