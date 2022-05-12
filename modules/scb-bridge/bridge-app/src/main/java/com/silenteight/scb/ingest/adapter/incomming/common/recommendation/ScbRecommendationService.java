package com.silenteight.scb.ingest.adapter.incomming.common.recommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.domain.model.AlertMetadata;
import com.silenteight.scb.ingest.domain.payload.PayloadConverter;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Recommendation;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.silenteight.scb.ingest.adapter.incomming.common.domain.GnsSyncConstants.PRIMARY_TRANSACTION_MANAGER;

@RequiredArgsConstructor
public class ScbRecommendationService {

  private final ScbRecommendationRepository scbRecommendationRepository;
  private final ScbDiscriminatorMatcher scbDiscriminatorMatcher;
  private final PayloadConverter payloadConverter;

  public List<ScbRecommendation> getRecommendations(List<String> systemIds) {
    return scbRecommendationRepository.findAllBySystemIdIn(systemIds);
  }

  public Optional<ScbRecommendation> getRecommendation(
      String systemId, String discriminator, List<ScbRecommendation> recommendations) {
    return recommendations
        .stream()
        .filter(reco -> reco.getSystemId().equals(systemId)
            && scbDiscriminatorMatcher.match(discriminator, reco.getDiscriminator()))
        .findFirst();
  }

  @Transactional(PRIMARY_TRANSACTION_MANAGER)
  public void updateRecomStatus(String systemId, String watchlistId, String status) {
    scbRecommendationRepository.updateRecomStatus(systemId, watchlistId, status);
  }

  @Transactional(PRIMARY_TRANSACTION_MANAGER)
  public void saveRecommendations(List<Recommendations.Recommendation> recommendations) {
    recommendations.forEach(this::saveRecommendation);
  }

  @Transactional(PRIMARY_TRANSACTION_MANAGER)
  public void saveRecommendation(Recommendation recommendation) {
    scbRecommendationRepository.save(toScbRecommendation(recommendation));
  }

  private ScbRecommendation toScbRecommendation(Recommendation recommendation) {
    var alertMetadata = parseAlertMetadata(recommendation);
    return ScbRecommendation.builder()
        .systemId(alertMetadata.systemId())
        .alertName(recommendation.alert().name())
        .decision(recommendation.recommendedAction().name())
        .comment(recommendation.recommendedComment())
        .recommendedAt(recommendation.recommendedAt())
        .discriminator(alertMetadata.discriminator())
        .watchlistId(alertMetadata.watchlistId())
        .build();
  }

  private AlertMetadata parseAlertMetadata(Recommendation recommendation) {
    return payloadConverter.deserializeFromJsonToObject(
        recommendation.alert().metadata(), AlertMetadata.class);
  }
}
