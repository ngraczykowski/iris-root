package com.silenteight.scb.ingest.adapter.incomming.common.recommendation;

import com.silenteight.scb.ingest.domain.model.AlertMetadata;
import com.silenteight.scb.ingest.domain.payload.PayloadConverter;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Alert;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Recommendation;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.RecommendedAction;
import com.silenteight.sep.base.testing.transaction.RunWithoutTransactionManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;

import static java.util.Comparator.comparing;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWithoutTransactionManager
class ScbRecommendationServiceTest {

  private static final String SYSTEM_ID = "systemId";
  private static final String DISCRIMINATOR = "321";
  private static final String PAYLOAD = "payload";
  private ScbRecommendationRepository scbRecommendationRepository =
      new InMemoryScbRecommendationRepository();
  @Mock
  private DiscriminatorFetcher discriminatorFetcher;
  @Mock
  private ScbDiscriminatorMatcher scbDiscriminatorMatcher;
  @Mock
  private PayloadConverter payloadConverter;

  private Fixtures fixtures = new Fixtures();
  private ScbRecommendationService underTest;

  @BeforeEach
  void setUp() {
    underTest = new ScbRecommendationService(
        scbRecommendationRepository,
        discriminatorFetcher,
        scbDiscriminatorMatcher,
        payloadConverter);
  }

  @Test
  void shouldStoreToDatabaseAndTakeLatestWithSystemIdAndDiscriminator() {
    //given
    AlertMetadata alertMetadata = new AlertMetadata(null, DISCRIMINATOR, SYSTEM_ID);
    when(discriminatorFetcher.fetch(anyString()))
        .thenReturn(Optional.of("321"));
    when(payloadConverter.deserializeFromJsonToObject(PAYLOAD, AlertMetadata.class))
        .thenReturn(alertMetadata);

    //when
    underTest.saveRecommendation(fixtures.firstRecommendation);
    underTest.saveRecommendation(fixtures.secondRecommendation);

    Optional<ScbRecommendation> currentRecommendation =
        underTest.findCurrentRecommendation(SYSTEM_ID);

    //then
    assertThat(currentRecommendation.isPresent()).isTrue();

    ScbRecommendation scbRecommendation = currentRecommendation.get();

    assertThat(scbRecommendation.getComment())
        .isEqualTo(fixtures.secondRecommendation.recommendedComment());
    assertThat(scbRecommendation.getDecision())
        .isEqualTo(fixtures.secondRecommendation.recommendedAction().name());
    assertThat(scbRecommendation.getSystemId())
        .isEqualTo(SYSTEM_ID);
    assertThat(scbRecommendation.getDiscriminator())
        .isEqualTo(DISCRIMINATOR);
    assertThat(scbRecommendation.getRecommendedAt())
        .isEqualTo(fixtures.secondRecommendation.recommendedAt());
  }

  @Test
  void shouldStoreToDatabaseAndFindCurrentOrLatestRecommendation() {
    //when
    var alertMetadata = new AlertMetadata(null, DISCRIMINATOR, SYSTEM_ID);
    when(payloadConverter.deserializeFromJsonToObject(PAYLOAD, AlertMetadata.class))
        .thenReturn(alertMetadata);
    underTest.saveRecommendation(fixtures.firstRecommendation);
    underTest.saveRecommendation(fixtures.secondRecommendation);

    Optional<ScbRecommendation> currentRecommendation =
        underTest.findCurrentOrLatestRecommendation(SYSTEM_ID);

    //then
    assertThat(currentRecommendation.isPresent()).isTrue();

    ScbRecommendation scbRecommendation = currentRecommendation.get();

    assertThat(scbRecommendation.getComment())
        .isEqualTo(fixtures.secondRecommendation.recommendedComment());
    assertThat(scbRecommendation.getDecision())
        .isEqualTo(fixtures.secondRecommendation.recommendedAction().name());
    assertThat(scbRecommendation.getSystemId())
        .isEqualTo(SYSTEM_ID);
    assertThat(scbRecommendation.getDiscriminator())
        .isEqualTo(DISCRIMINATOR);
    assertThat(scbRecommendation.getRecommendedAt())
        .isEqualTo(fixtures.secondRecommendation.recommendedAt());
  }

  @Test
  void shouldStoreToDatabaseAndCheckIfRecommendationAlreadyExists() {
    //given
    var alertMetadata = new AlertMetadata(null, DISCRIMINATOR, SYSTEM_ID);
    when(payloadConverter.deserializeFromJsonToObject(PAYLOAD, AlertMetadata.class))
        .thenReturn(alertMetadata);
    var alertRecommendation = fixtures.firstRecommendation;
    var alertId = alertRecommendation.alert().id();
    when(scbDiscriminatorMatcher.match(anyString(), anyString())).thenReturn(true);

    // when
    underTest.saveRecommendation(alertRecommendation);
    boolean result =
        underTest.alertRecommendationExists(alertId, "");

    //then
    assertThat(result).isTrue();
  }

  private static class Fixtures {

    private static final OffsetDateTime TIME_1 = OffsetDateTime.now();
    private static final OffsetDateTime TIME_2 = OffsetDateTime.now();
    private static final RecommendedAction RECOMMENDATION_ACTION =
        RecommendedAction.ACTION_FALSE_POSITIVE;

    Recommendation firstRecommendation = createRecommendation("comment_1", TIME_1);
    Recommendation secondRecommendation = createRecommendation("comment_2", TIME_2);

    @Nonnull
    private static Recommendation createRecommendation(String comment, OffsetDateTime time) {
      return Recommendation.builder()
          .recommendedComment(comment)
          .recommendedAction(RECOMMENDATION_ACTION)
          .recommendedAt(time)
          .alert(Alert.builder()
              .id(SYSTEM_ID)
              .metadata(PAYLOAD)
              .build())
          .build();
    }
  }

  static class InMemoryScbRecommendationRepository implements ScbRecommendationRepository {

    private List<ScbRecommendation> inMemoryScbRecommendationRepository = new ArrayList<>();

    @Override
    public Optional<ScbRecommendation>
        findFirstBySystemIdAndDiscriminatorAndWatchlistIdIsNullOrderByRecommendedAtDesc(
        String systemId, String discriminator) {
      return inMemoryScbRecommendationRepository
          .stream()
          .filter(
              s -> s.getSystemId().equals(systemId)
                  && s.getDiscriminator().equals(discriminator)
                  && s.getWatchlistId() == null)
          .max(comparing(ScbRecommendation::getRecommendedAt, OffsetDateTime::compareTo));
    }

    @Override
    public Optional<ScbRecommendation>
        findFirstBySystemIdAndWatchlistIdIsNullOrderByRecommendedAtDesc(
        String systemId) {
      return inMemoryScbRecommendationRepository
          .stream()
          .filter(s -> s.getSystemId().equals(systemId) && s.getWatchlistId() == null)
          .max(comparing(ScbRecommendation::getRecommendedAt, OffsetDateTime::compareTo));
    }

    @Override
    public Collection<ScbRecommendation> findAll() {
      return inMemoryScbRecommendationRepository;
    }

    @Override
    public void save(ScbRecommendation scbRecommendation) {
      inMemoryScbRecommendationRepository.add(scbRecommendation);
    }

    @Override
    public Optional<ScbRecommendation> findFirstBySystemIdOrderByRecommendedAtDesc(
        String systemId) {
      return inMemoryScbRecommendationRepository
          .stream()
          .filter(s -> s.getSystemId().equals(systemId))
          .max(comparing(ScbRecommendation::getRecommendedAt, OffsetDateTime::compareTo));
    }
  }
}
