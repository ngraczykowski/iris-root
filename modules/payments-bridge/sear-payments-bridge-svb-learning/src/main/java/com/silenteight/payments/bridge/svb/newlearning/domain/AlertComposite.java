package com.silenteight.payments.bridge.svb.newlearning.domain;

import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterMatchResponse;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlert;
import com.silenteight.payments.bridge.common.app.AgentsUtils;
import com.silenteight.payments.bridge.data.retention.model.AlertDataRetention;
import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.etl.processing.model.MessageTag;
import com.silenteight.payments.bridge.svb.migration.DecisionMapper;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.service.AlertParserService;
import com.silenteight.proto.learningstore.historicaldecision.v1.api.*;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

import static java.util.stream.Collectors.toList;

@Value
@Builder
@Slf4j
public class AlertComposite {

  UUID alertMessageId;

  // Learning engine is multi-tenant db so some discriminator is required.
  String discriminator;

  AlertDetails alertDetails;

  List<HitComposite> hits;

  List<ActionComposite> actions;

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
            .map(hit -> mapToAlert(decisionMapper, featureTypeDiscriminator, hit))
            .peek(alert -> {
              if (log.isTraceEnabled()) {
                log.trace(
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

  @Nonnull
  private Alert mapToAlert(
      DecisionMapper decisionMapper, String featureTypeDiscriminator, HitComposite hit) {
    return Alert.newBuilder()
        .setAlertId(alertDetails.getSystemId())
        .setAlertedParty(mapAlertedParty(featureTypeDiscriminator, hit))
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
            .map(a -> a.getStatusName())
            .collect(Collectors.toList());
    var decisionMade = decisionMapper.map(previousDecisions, lastAction.getStatusName());
    log.debug("Final decision mapped -> {} actionId: {}", decisionMade, lastAction.getActionId());

    return Decision.newBuilder()
        .setId(String.valueOf(lastAction.getActionId()))
        .setCreatedAt(lastAction.getActionDatetime().toInstant().toEpochMilli())
        .setValue(decisionMade)
        .build();

  }

  private List<ActionComposite> createPreviousActions(List<ActionComposite> actions) {
    if (actions.size() > 1) {
      return actions.subList(0, actions.size() - 1);
    } else {
      return List.copyOf(actions);
    }
  }

  private AlertedParty mapAlertedParty(String featureTypeDiscriminator, HitComposite hit) {
    var messageData = new MessageData(List.of(
        new MessageTag(hit.getFkcoVMatchedTag(), hit.getFkcoVMatchedTagContent())));

    var alertedPartyData = AlertParserService.extractAlertedPartyData(messageData,
        hit.getFkcoVMatchedTag(),
        alertDetails.getFkcoVFormat(), alertDetails.getFkcoVApplication());

    var alertedPartyId =
        mapAlertedPartyIdByFeatureDiscriminator(featureTypeDiscriminator, alertedPartyData);
    var country = hit.getFkcoVCountryMatchedText();
    return AlertedParty.newBuilder()
        .setId(alertedPartyId)
        .setCountry(country)
        .build();
  }

  private String mapAlertedPartyIdByFeatureDiscriminator(
      String featureTypeDiscriminator, AlertedPartyData alertedPartyData) {
    switch (featureTypeDiscriminator) {
      case AgentsUtils.HISTORICAL_RISK_ACCOUNT_NUMBER_LEARNING_DISC:
        return alertedPartyData.getAccountNumberOrEmpty();
      case AgentsUtils.HISTORICAL_RISK_CUSTOMER_NAME_LEARNING_DISC:
        return alertedPartyData.getFirstAlertedPartyName().orElse("");
      default:
        throw new UnsupportedHistoricalDecisionAgentFeature(
            String.format(
                "This historical decision feature discriminator is not supported: %s",
                featureTypeDiscriminator));
    }
  }

  private Discriminator mapDiscriminator(String featureTypeDiscriminator) {
    return Discriminator.newBuilder()
        .setValue(buildDiscriminatorValue(featureTypeDiscriminator))
        .build();
  }

  private static Watchlist mapWatchlistType(HitComposite hit) {
    return Watchlist.newBuilder()
        .setId(hit.getFkcoVListFmmId())
        .setType(hit.getFkcoVListType())
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
