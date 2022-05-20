package com.silenteight.payments.bridge.svb.learning.domain;

import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterMatchResponse;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlert;
import com.silenteight.payments.bridge.common.agents.AlertedPartyIdContextAdapter;
import com.silenteight.payments.bridge.common.agents.ContextualAlertedPartyIdModel;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.data.retention.model.AlertDataRetention;
import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.etl.processing.model.MessageTag;
import com.silenteight.payments.bridge.svb.learning.mapping.DecisionMapper;
import com.silenteight.payments.bridge.svb.oldetl.service.AlertParserService;
import com.silenteight.proto.learningstore.historicaldecision.v1.api.*;

import com.google.common.base.Strings;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.CONTEXTUAL_LEARNING_DISC_FP;
import static com.silenteight.payments.bridge.common.app.AgentsUtils.CONTEXTUAL_LEARNING_DISC_TP;
import static com.silenteight.payments.bridge.common.app.AgentsUtils.HISTORICAL_RISK_ACCOUNT_NUMBER_LEARNING_DISC_FP;
import static com.silenteight.payments.bridge.common.app.AgentsUtils.HISTORICAL_RISK_ACCOUNT_NUMBER_LEARNING_DISC_TP;
import static com.silenteight.payments.bridge.common.dto.common.MessageStructure.isMessageStructured;
import static java.util.stream.Collectors.toList;

@Value
@Builder
@Slf4j
public class AlertComposite {

  private static final String ANALYST_DECISION_FALSE_POSITIVE = "analyst_decision_false_positive";
  private static final String ANALYST_DECISION_TRUE_POSITIVE = "analyst_decision_true_positive";

  UUID alertMessageId;

  // Learning engine is multi-tenant db so some discriminator is required.
  String discriminator;

  AlertDetails alertDetails;

  List<HitComposite> hits;

  List<ActionComposite> actions;

  ContextualAlertedPartyIdModel.ContextualAlertedPartyIdModelBuilder contextualModelBuilder;

  public String getSystemId() {
    return alertDetails.getSystemId();
  }

  public String toFindRegisterAlertRequest() {
    return alertDetails.getSystemId();
  }

  public HistoricalDecisionLearningStoreExchangeRequest toHistoricalDecisionRequest(
      DecisionMapper decisionMapper, String featureTypeDiscriminator) {
    var alerts =
        hits.stream()
            .filter(
                hit -> shouldBeAddedToRequest(hit.getFkcoVMatchedTag(), featureTypeDiscriminator))
            .map(hit -> mapToAlert(decisionMapper, featureTypeDiscriminator, hit))
            .filter(AlertComposite::isAlertValid)
            .peek(alert -> {
              if (log.isTraceEnabled()) {
                log.debug(
                    "Mapped for featureDiscriminator:{} alert details alertedId:{}"
                        + " featureDiscriminatorValue:{}",
                    featureTypeDiscriminator, alert.getAlertId(),
                    alert.getDiscriminator().getValue());
              }
            })
            .collect(Collectors.toList());

    return HistoricalDecisionLearningStoreExchangeRequest.newBuilder()
        .addAllAlerts(alerts)
        .build();
  }

  private static boolean isAlertValid(Alert alert) {
    return isAlertPartyIdNotEmpty(alert) && isDecisionFinal(alert);
  }

  private static boolean isAlertPartyIdNotEmpty(Alert alert) {
    return !alert.getAlertedParty().getId().isEmpty();
  }

  private static boolean isDecisionFinal(Alert alert) {
    for (var decision : alert.getDecisionsList()) {
      var analystDecision = decision.getValue();
      if (isAnalystDecisionFinal(analystDecision)) {
        return true;
      }
    }
    log.debug(
        "Analyst decision is not final. Alert: {} won't be send to Learning Engine",
        alert.getAlertId());
    return false;
  }

  private static boolean isAnalystDecisionFinal(String analystDecision) {
    return analystDecision.equals(ANALYST_DECISION_TRUE_POSITIVE) ||
        analystDecision.equals(ANALYST_DECISION_FALSE_POSITIVE);
  }

  private static boolean shouldBeAddedToRequest(String tag, String featureTypeDiscriminator) {
    switch (featureTypeDiscriminator) {
      case HISTORICAL_RISK_ACCOUNT_NUMBER_LEARNING_DISC_FP:
      case HISTORICAL_RISK_ACCOUNT_NUMBER_LEARNING_DISC_TP:
        return isMessageStructured(tag);
      case CONTEXTUAL_LEARNING_DISC_FP:
      case CONTEXTUAL_LEARNING_DISC_TP:
        return true;
      default:
        throw new UnsupportedHistoricalDecisionAgentFeature(
            String.format(
                "This historical decision feature discriminator is not supported: %s",
                featureTypeDiscriminator));
    }
  }


  @Nonnull
  private Alert mapToAlert(
      DecisionMapper decisionMapper, String featureTypeDiscriminator, HitComposite hit) {
    return Alert.newBuilder()
        .setAlertId(alertDetails.getSystemId())
        .setAlertedParty(createAlertedParty(featureTypeDiscriminator, hit))
        .setMatchId(hit.getFkcoVListFmmId())
        .setWatchlist(mapWatchlistType(hit))
        .addDecisions(getDecision(decisionMapper))
        .setDiscriminator(mapDiscriminator(featureTypeDiscriminator))
        .build();
  }

  private Decision getDecision(DecisionMapper decisionMapper) {
    List<ActionComposite> previousActions = createPreviousActions(actions);

    var lastAction = ((LinkedList<ActionComposite>) actions).getLast();
    var previousDecisions =
        previousActions.stream()
            .map(ActionComposite::getStatusName)
            .collect(Collectors.toList());
    var decisionMade = decisionMapper.map(previousDecisions, lastAction.getStatusName());
    log.debug("Final decision mapped -> {} actionId: {}", decisionMade, lastAction.getActionId());

    return Decision.newBuilder()
        .setId(String.valueOf(lastAction.getActionId()))
        // LE is using seconds not milliseconds (HSBC requirement).
        .setCreatedAt(lastAction.getActionDatetime().toEpochSecond())
        .setValue(decisionMade)
        .build();

  }

  private AlertedParty createAlertedParty(
      String featureTypeDiscriminator, HitComposite hit) {
    switch (featureTypeDiscriminator) {
      case HISTORICAL_RISK_ACCOUNT_NUMBER_LEARNING_DISC_FP:
      case HISTORICAL_RISK_ACCOUNT_NUMBER_LEARNING_DISC_TP:
        return createAlertedPartyData(hit);
      case CONTEXTUAL_LEARNING_DISC_FP:
      case CONTEXTUAL_LEARNING_DISC_TP:
        return createContextualAlertedParty(hit);
      default:
        throw new UnsupportedHistoricalDecisionAgentFeature(
            String.format(
                "This historical decision feature discriminator is not supported: %s",
                featureTypeDiscriminator));
    }
  }

  private static List<ActionComposite> createPreviousActions(List<ActionComposite> actions) {
    if (actions.size() > 1) {
      return actions.subList(0, actions.size() - 1);
    } else {
      return List.copyOf(actions);
    }
  }

  private AlertedParty createAlertedPartyData(HitComposite hit) {
    var messageData = new MessageData(List.of(
        new MessageTag(hit.getFkcoVMatchedTag(), hit.getFkcoVMatchedTagContent())));

    var alertedPartyData = AlertParserService.extractAlertedPartyData(messageData,
        hit.getFkcoVMatchedTag(),
        alertDetails.getFkcoVFormat(), alertDetails.getFkcoVApplication());

    var alertedPartyId = alertedPartyData.getAccountNumberOrEmpty();
    var country = hit.getFkcoVCountryMatchedText();

    if (log.isDebugEnabled()) {
      log.debug("Mapped alertedPartyId is blankOrEmpty: {} and country blankOrEmpty: {}",
          Strings.isNullOrEmpty(alertedPartyId), Strings.isNullOrEmpty(country));
    }
    return AlertedParty.newBuilder()
        .setId(alertedPartyId)
        .setCountry(country)
        .build();
  }

  private AlertedParty createContextualAlertedParty(HitComposite hit) {

    String matchingField = hit.getFkcoVMatchedTagContent();
    String matchedText = hit.getFkcoVNameMatchedText();

    var request =
        contextualModelBuilder
            .matchingField(matchingField)
            .matchText(matchedText)
            .build();

    var alertedPartyId = AlertedPartyIdContextAdapter.generateAlertedPartyId(request);

    var country = hit.getFkcoVCountryMatchedText();

    return AlertedParty.newBuilder()
        .setId(alertedPartyId)
        .setCountry(country)
        .build();
  }

  private Discriminator mapDiscriminator(String featureTypeDiscriminator) {
    return Discriminator.newBuilder()
        .setValue(buildDiscriminatorValue(featureTypeDiscriminator))
        .build();
  }

  private static Watchlist mapWatchlistType(HitComposite hit) {
    return Watchlist.newBuilder()
        .setId(hit.getFkcoVListFmmId())
        .setType(WatchlistType.ofCode(hit.getFkcoVListType()).toString())
        .build();
  }

  public RegisterAlertRequest toRegisterAlertRequest(Long jobId) {
    return alertDetails.toRegisterAlertRequest(jobId, hits);
  }

  public LearningRegisteredAlert toLearningRegisteredAlert(RegisteredAlert registeredAlert) {
    var registeredMatches = getRegisteredMatches(registeredAlert);
    return LearningRegisteredAlert
        .builder()
        .alertRegistrationSource(AlertRegistrationSource.FIRCO)
        .alertName(registeredAlert.getAlertName())
        .alertDetails(alertDetails)
        .registeredMatches(registeredMatches)
        .build();
  }

  public LearningRegisteredAlert toLearningRegisteredAlert(RegisterAlertResponse registeredAlert) {
    var registeredMatches =
        registeredAlert
            .getMatchResponses()
            .stream()
            .map(this::getRegisteredMatch)
            .collect(toList());
    return LearningRegisteredAlert
        .builder()
        .alertRegistrationSource(AlertRegistrationSource.LEARNING)
        .alertName(registeredAlert.getAlertName())
        .alertDetails(alertDetails)
        .registeredMatches(registeredMatches)
        .build();
  }

  public HitComposite getHitById(String matchId) {
    return hits
        .stream()
        .filter(hit -> hit.getMatchId().equals(matchId))
        .findFirst()
        .orElseThrow(() -> new NoCorrespondingHitException(
            String.format("There is no corresponding hit for = %s", matchId)));
  }

  @Nonnull
  private List<RegisteredMatch> getRegisteredMatches(RegisteredAlert registeredAlert) {
    var matches = registeredAlert.getMatches();
    return hits
        .stream()
        .map(hit -> {
          var match =
              matches.stream().filter(m -> hit.getMatchId().equals(m.getMatchId())).findFirst();
          match.orElseThrow();
          return RegisteredMatch
              .builder()
              .matchName(match.get().getMatchName())
              .hitComposite(hit)
              .build();
        }).collect(toList());
  }

  public AlertDataRetention alertDataRetention(RegisterAlertResponse registeredAlert) {
    return new AlertDataRetention(
        registeredAlert.getAlertName(),
        alertDetails.getFkcoDFilteredDateTime());
  }

  private RegisteredMatch getRegisteredMatch(RegisterMatchResponse match) {
    var hit = hits.stream().filter(h -> h.getMatchId().equals(match.getMatchId())).findFirst();
    hit.orElseThrow();
    return RegisteredMatch
        .builder()
        .hitComposite(hit.get())
        .matchName(match.getMatchName())
        .build();
  }

  private String buildDiscriminatorValue(String feature) {
    return discriminator + "_" + feature;
  }
}
