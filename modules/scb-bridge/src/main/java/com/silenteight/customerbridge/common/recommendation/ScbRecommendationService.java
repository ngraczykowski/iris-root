package com.silenteight.customerbridge.common.recommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.common.protocol.AlertWrapper;
import com.silenteight.proto.serp.scb.v1.ScbAlertDetails;
import com.silenteight.proto.serp.v1.common.ObjectId;
import com.silenteight.proto.serp.v1.recommendation.AlertRecommendation;
import com.silenteight.proto.serp.v1.recommendation.Recommendation;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Strings.emptyToNull;
import static com.silenteight.customerbridge.common.domain.GnsSyncConstants.PRIMARY_TRANSACTION_MANAGER;
import static com.silenteight.protocol.utils.MoreTimestamps.toOffsetDateTime;

@RequiredArgsConstructor
public class ScbRecommendationService {

  private final ScbRecommendationRepository scbRecommendationRepository;
  private final DiscriminatorFetcher discriminatorFetcher;
  private final ScbDiscriminatorMatcher scbDiscriminatorMatcher;

  public Optional<ScbRecommendation> findCurrentRecommendation(String systemId) {
    return discriminatorFetcher
        .fetch(systemId)
        .flatMap(d -> findAlertRecommendationWithLatestDiscriminator(systemId, d));
  }

  private Optional<ScbRecommendation> findAlertRecommendationWithLatestDiscriminator(
      String systemId, String discriminator) {

    return scbRecommendationRepository
        .findFirstBySystemIdAndDiscriminatorAndWatchlistIdIsNullOrderByRecommendedAtDesc(
            systemId, discriminator);
  }

  public Optional<ScbRecommendation> findCurrentOrLatestRecommendation(String systemId) {
    return findLatestAlertRecommendation(systemId);
  }

  private Optional<ScbRecommendation> findLatestAlertRecommendation(String systemId) {
    return scbRecommendationRepository
        .findFirstBySystemIdAndWatchlistIdIsNullOrderByRecommendedAtDesc(systemId);
  }

  public boolean alertRecommendationExists(String systemId, String discriminator) {
    return scbRecommendationRepository.findFirstBySystemIdOrderByRecommendedAtDesc(systemId)
        .map(ScbRecommendation::getDiscriminator)
        .filter(d -> scbDiscriminatorMatcher.match(discriminator, d))
        .isPresent();
  }

  @Transactional(PRIMARY_TRANSACTION_MANAGER)
  public void saveRecommendations(List<AlertRecommendation> alertRecommendations) {
    alertRecommendations.forEach(this::saveRecommendation);
  }

  @Transactional(PRIMARY_TRANSACTION_MANAGER)
  public void saveRecommendation(AlertRecommendation alertRecommendation) {
    scbRecommendationRepository.save(toScbRecommendation(alertRecommendation));
  }

  private static ScbRecommendation toScbRecommendation(AlertRecommendation alertRecommendation) {
    ScbRecommendation scbRecommendation = new ScbRecommendation();

    ObjectId alertId = alertRecommendation.getAlertId();
    scbRecommendation.setSystemId(alertId.getSourceId());
    scbRecommendation.setDiscriminator(alertId.getDiscriminator());

    Recommendation recommendation = alertRecommendation.getRecommendation();
    scbRecommendation.setDecision(recommendation.getAction().name());
    scbRecommendation.setComment(recommendation.getComment());
    scbRecommendation.setRecommendedAt(toOffsetDateTime(recommendation.getCreatedAt()));

    if (alertRecommendation.hasAlert()) {
      AlertWrapper wrapper = new AlertWrapper(alertRecommendation.getAlert());
      wrapper.unpackDetails(ScbAlertDetails.class)
          .ifPresent(d -> scbRecommendation.setWatchlistId(emptyToNull(d.getWatchlistId())));
    }

    return scbRecommendation;
  }
}
