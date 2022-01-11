package com.silenteight.payments.bridge.svb.newlearning.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.ae.alertregistration.domain.*;
import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.etl.processing.model.MessageTag;
import com.silenteight.payments.bridge.svb.oldetl.service.AlertParserService;
import com.silenteight.proto.learningstore.historicaldecision.v1.api.*;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

import static java.util.stream.Collectors.toList;

@Value
@Builder
public class AlertComposite {

  // Learning engine is multi-tenant db so some discriminator is required.
  private static final String DISCRIMINATOR = "";

  AlertDetails alertDetails;

  List<HitComposite> hits;

  List<ActionComposite> actions;

  public FindRegisteredAlertRequest toFindRegisterAlertRequest() {
    return alertDetails.toFindRegisterAlertRequest();
  }

  public HistoricalDecisionLearningStoreExchangeRequest toHistoricalDecisionRequest() {
    var alerts =
        hits.stream()
            .map(hit -> mapToAlert(alertDetails, hit,
                ((LinkedList<ActionComposite>) actions).getLast())).collect(Collectors.toList());

    return HistoricalDecisionLearningStoreExchangeRequest.newBuilder()
        .addAllAlerts(alerts)
        .build();
  }

  private Alert mapToAlert(
      AlertDetails alertDetails, HitComposite hit, ActionComposite lastAction) {

    return Alert.newBuilder()
        .setAlertId(alertDetails.getSystemId())
        .setAlertedParty(mapAlertedParty(hit))
        .setMatchId(hit.getFkcoVListFmmId())
        .setWatchlist(mapWatchlistType(hit))
        .addDecisions(mapDecision(lastAction))
        .setDiscriminator(mapDiscriminator())
        .build();
  }

  private AlertedParty mapAlertedParty(HitComposite hit) {
    var messageData = new MessageData(List.of(
        new MessageTag(hit.getFkcoVMatchedTag(), hit.getFkcoVMatchedTagContent())));

    var alertedPartyData = AlertParserService.extractAlertedPartyData(messageData,
        hit.getFkcoVMatchedTag(),
        alertDetails.getFkcoVFormat(), alertDetails.getFkcoVApplication());
    var alertedPartyId = alertedPartyData.getAccountNumberOrFirstName().orElse("");
    var country = hit.getFkcoVCountryMatchedText();
    return AlertedParty.newBuilder()
        .setId(alertedPartyId)
        .setCountry(country)
        .build();
  }

  private static Discriminator mapDiscriminator() {
    return Discriminator.newBuilder()
        .setValue(DISCRIMINATOR)
        .build();
  }

  private static Watchlist mapWatchlistType(HitComposite hit) {
    return Watchlist.newBuilder()
        .setId(hit.getFkcoVListFmmId())
        .setType(hit.getFkcoVListType())
        .build();
  }

  private static Decision mapDecision(ActionComposite lastAction) {
    return Decision.newBuilder()
        .setId(String.valueOf(lastAction.getActionId()))
        .setCreatedAt(lastAction.getActionDatetime().toInstant().toEpochMilli())
        .setValue(lastAction.getStatusBehaviour())
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


  private RegisteredMatch getRegisteredMatch(RegisterMatchResponse match) {
    var hit = hits.stream().filter(h -> h.getMatchId().equals(match.getMatchId())).findFirst();
    hit.orElseThrow();
    return RegisteredMatch
        .builder()
        .hitComposite(hit.get())
        .matchName(match.getMatchName())
        .build();
  }
}
