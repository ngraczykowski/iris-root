package com.silenteight.payments.bridge.mock.ae;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v2.GetRecommendationRequest;
import com.silenteight.adjudication.api.v2.RecommendationMetadata;
import com.silenteight.adjudication.api.v2.RecommendationServiceGrpc.RecommendationServiceImplBase;
import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;
import com.silenteight.payments.bridge.event.RecommendationGeneratedEvent;

import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

@Profile("mockae")
@GrpcService
@RequiredArgsConstructor
class RecommendationServiceGrpc extends RecommendationServiceImplBase {

  @Override
  public void getRecommendation(GetRecommendationRequest request,
      StreamObserver<Recommendation> responseObserver) {
    responseObserver.onNext(
        Recommendation.newBuilder()
                .setAlert(cache.get(request.getRecommendation())).build());
    responseObserver.onCompleted();
  }

  @Override
  public void getRecommendationWithMetadata(
      GetRecommendationRequest request,
      StreamObserver<RecommendationWithMetadata> responseObserver) {
    responseObserver.onNext(
        RecommendationWithMetadata.newBuilder()
            .setRecommendation(
                Recommendation.newBuilder()
                  .setName(request.getRecommendation())
                  .setAlert(cache.get(request.getRecommendation()))
                  .setCreateTime(Timestamp.newBuilder().build())
                  .setRecommendationComment("MOCKAE recommendation comment")
                  .setRecommendedAction("MOCKAE recommendation action")
            )
            .setMetadata(
                RecommendationMetadata.newBuilder()
                  .setAlert(cache.get(request.getRecommendation()))
                  .setName(request.getRecommendation() + "/metadata")
            )
            .build());
    responseObserver.onCompleted();
  }

  private final Map<String, String> cache = new LinkedHashMap<>() {
    @Override
    protected boolean removeEldestEntry(Entry<String, String> eldest) {
      return size() > 1000;
    }
  };

  @Order(0)
  @ServiceActivator(inputChannel = RecommendationGeneratedEvent.CHANNEL)
  void cacheAlertId(RecommendationGeneratedEvent event) {
    event.getRecommendationsGenerated()
        .getRecommendationInfosList().forEach(info -> {
          cache.put(info.getRecommendation(), info.getAlert());
        });
  }

}
