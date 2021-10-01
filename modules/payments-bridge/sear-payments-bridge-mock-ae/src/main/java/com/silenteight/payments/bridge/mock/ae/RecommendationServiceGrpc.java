package com.silenteight.payments.bridge.mock.ae;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v2.GetRecommendationRequest;
import com.silenteight.adjudication.api.v2.RecommendationServiceGrpc.RecommendationServiceImplBase;
import com.silenteight.payments.bridge.event.RecommendationGeneratedEvent;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.RECOMMENDATION_GENERATED;

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

  private final Map<String, String> cache = new LinkedHashMap<>() {
    @Override
    protected boolean removeEldestEntry(Entry<String, String> eldest) {
      return size() > 1000;
    }
  };

  @Order(0)
  @ServiceActivator(inputChannel = RECOMMENDATION_GENERATED)
  void cacheAlertId(RecommendationGeneratedEvent event) {
    event.getRecommendationsGenerated()
        .getRecommendationInfosList().forEach(info -> {
          cache.put(info.getRecommendation(), info.getAlert());
        });
  }

}
