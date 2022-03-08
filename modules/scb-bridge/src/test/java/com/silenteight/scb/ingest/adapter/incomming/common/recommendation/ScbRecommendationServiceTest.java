package com.silenteight.scb.ingest.adapter.incomming.common.recommendation;

import com.silenteight.proto.serp.scb.v1.ScbAlertDetails;
import com.silenteight.proto.serp.v1.alert.Alert;
import com.silenteight.proto.serp.v1.common.ObjectId;
import com.silenteight.proto.serp.v1.recommendation.AlertRecommendation;
import com.silenteight.proto.serp.v1.recommendation.Recommendation;
import com.silenteight.proto.serp.v1.recommendation.RecommendedAction;
import com.silenteight.sep.base.testing.transaction.RunWithoutTransactionManager;

import com.google.protobuf.Any;
import com.google.protobuf.Timestamp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;

import static com.silenteight.proto.serp.v1.recommendation.RecommendedAction.ACTION_FALSE_POSITIVE;
import static java.util.Comparator.comparing;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWithoutTransactionManager
class ScbRecommendationServiceTest {

  public static final String SYSTEM_ID = "systemId";
  public static final String DISCRIMINATOR = "321";
  private ScbRecommendationRepository scbRecommendationRepository =
      new InMemoryScbRecommendationRepository();
  @Mock
  private DiscriminatorFetcher discriminatorFetcher;
  @Mock
  private ScbDiscriminatorMatcher scbDiscriminatorMatcher;

  private Fixtures fixtures = new Fixtures();
  private ScbRecommendationService underTest;

  @BeforeEach
  void setUp() {
    underTest = new ScbRecommendationService(
        scbRecommendationRepository,
        discriminatorFetcher,
        scbDiscriminatorMatcher);
  }

  @Test
  void shouldStoreToDatabaseAndTakeLatestWithSystemIdAndDiscriminator() {
    //given
    when(discriminatorFetcher.fetch(anyString()))
        .thenReturn(Optional.of("321"));

    //when
    underTest.saveRecommendation(fixtures.firstAlertRecommendation);
    underTest.saveRecommendation(fixtures.secondAlertRecommendation);

    Optional<ScbRecommendation> currentRecommendation =
        underTest.findCurrentRecommendation(SYSTEM_ID);

    //then
    assertThat(currentRecommendation.isPresent()).isTrue();

    ScbRecommendation scbRecommendation = currentRecommendation.get();

    assertThat(scbRecommendation.getComment())
        .isEqualTo(fixtures.secondRecommendation.getComment());
    assertThat(scbRecommendation.getDecision())
        .isEqualTo(fixtures.secondRecommendation.getAction().name());
    assertThat(scbRecommendation.getSystemId())
        .isEqualTo(SYSTEM_ID);
    assertThat(scbRecommendation.getDiscriminator())
        .isEqualTo(DISCRIMINATOR);
    assertThat(scbRecommendation.getRecommendedAt().toInstant().getEpochSecond())
        .isEqualTo(fixtures.secondRecommendation.getCreatedAt().getSeconds());
  }

  @Test
  void shouldStoreToDatabaseAndFindCurrentOrLatestRecommendation() {
    //when
    underTest.saveRecommendation(fixtures.firstAlertRecommendation);
    underTest.saveRecommendation(fixtures.secondAlertRecommendation);

    Optional<ScbRecommendation> currentRecommendation =
        underTest.findCurrentOrLatestRecommendation(SYSTEM_ID);

    //then
    assertThat(currentRecommendation.isPresent()).isTrue();

    ScbRecommendation scbRecommendation = currentRecommendation.get();

    assertThat(scbRecommendation.getComment())
        .isEqualTo(fixtures.secondRecommendation.getComment());
    assertThat(scbRecommendation.getDecision())
        .isEqualTo(fixtures.secondRecommendation.getAction().name());
    assertThat(scbRecommendation.getSystemId())
        .isEqualTo(SYSTEM_ID);
    assertThat(scbRecommendation.getDiscriminator())
        .isEqualTo(DISCRIMINATOR);
    assertThat(scbRecommendation.getRecommendedAt().toInstant().getEpochSecond())
        .isEqualTo(fixtures.secondRecommendation.getCreatedAt().getSeconds());
  }

  @Test
  void shouldStoreToDatabaseAndCheckIfRecommendationAlreadyExists() {
    //given
    var alertRecommendation = fixtures.firstAlertRecommendation;
    var alertId = alertRecommendation.getAlert().getId();
    when(scbDiscriminatorMatcher.match(anyString(), anyString())).thenReturn(true);

    // when
    underTest.saveRecommendation(alertRecommendation);
    boolean result =
        underTest.alertRecommendationExists(alertId.getSourceId(), alertId.getDiscriminator());

    //then
    assertThat(result).isTrue();
  }

  private static class Fixtures {

    private static final Timestamp TIME_1 =
        Timestamp.newBuilder().setSeconds(Instant.now().getEpochSecond()).build();
    private static final Timestamp TIME_2 =
        Timestamp
            .newBuilder()
            .setSeconds(Instant.now().plus(1, ChronoUnit.MINUTES).getEpochSecond())
            .build();
    private static final RecommendedAction RECOMMENDATION_ACTION = ACTION_FALSE_POSITIVE;

    Recommendation firstRecommendation = createRecommendation("comment_1", TIME_1);
    Recommendation secondRecommendation = createRecommendation("comment_2", TIME_2);

    AlertRecommendation firstAlertRecommendation = createAlertRecommendation(
        firstRecommendation,
        "123");
    AlertRecommendation secondAlertRecommendation = createAlertRecommendation(
        secondRecommendation,
        DISCRIMINATOR);

    private static AlertRecommendation createAlertRecommendation(
        Recommendation recommendation, String discriminator) {
      ObjectId alertId = ObjectId
          .newBuilder()
          .setDiscriminator(discriminator)
          .setSourceId(SYSTEM_ID)
          .build();

      ScbAlertDetails details = ScbAlertDetails.newBuilder().build();
      Alert alert = Alert.newBuilder().setId(alertId).setDetails(Any.pack(details)).build();

      return AlertRecommendation
          .newBuilder()
          .setRecommendation(recommendation)
          .setAlert(alert)
          .setAlertId(alertId)
          .build();
    }

    @Nonnull
    private static Recommendation createRecommendation(String comment, Timestamp time) {
      return Recommendation.newBuilder()
          .setComment(comment)
          .setAction(RECOMMENDATION_ACTION)
          .setCreatedAt(time)
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
