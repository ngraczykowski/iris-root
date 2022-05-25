package com.silenteight.scb.ingest.adapter.incomming.common.batch;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord;
import com.silenteight.scb.ingest.adapter.incomming.common.gender.GenderDetector;
import com.silenteight.scb.ingest.adapter.incomming.common.gnsparty.ChineseNamesResolver;
import com.silenteight.scb.ingest.adapter.incomming.common.gnsparty.GnsParty;
import com.silenteight.scb.ingest.adapter.incomming.common.model.ObjectId;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert.Flag;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert.State;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertDetails;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertedParty;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertedParty.AlertedPartyBuilder;
import com.silenteight.scb.ingest.adapter.incomming.common.model.decision.Decision.AnalystSolution;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;

import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Strings.nullToEmpty;
import static com.silenteight.scb.ingest.adapter.incomming.common.util.AlertParserUtils.makeAlertedPartyId;
import static com.silenteight.scb.ingest.adapter.incomming.common.util.AlertParserUtils.makeId;
import static com.silenteight.scb.ingest.adapter.incomming.common.util.AlertParserUtils.mapString;
import static java.util.Optional.ofNullable;

@Value
@Builder
@Slf4j
public class RecordToAlertMapper {

  public static final int ANALYST_TEXT_OFFSET = 8;
  private static final String EMPTY_WATCHLIST_ID = "";

  @NonNull
  AlertRecord alertData;

  @Nullable
  Instant filtered;

  Instant receivedAt = Instant.now();

  @NonNull
  DecisionsCollection decisionsCollection;

  @NonNull
  GnsParty alertedParty;

  @NonNull
  String recordSignature;

  boolean requestProcessing;

  boolean requestRecommendation;

  boolean watchlistLevel;

  Alert toAlert(SuspectsCollection suspects) {
    List<Match> matches = makeMatches(suspects);
    String watchlistId = getWatchlistId(matches);

    Alert.AlertBuilder builder = Alert
        .builder()
        .id(createId(watchlistId))
        .flags(getFlags())
        .decisionGroup(nullToEmpty(alertData.getUnit()))
        .securityGroup(getCountry())
        .state(State.STATE_CORRECT)
        .generatedAt(filtered)
        .receivedAt(receivedAt)
        .alertedParty(makeAlertedParty(alertedParty))
        .matches(matches)
        .details(createDetails(watchlistId))
        .decisions(decisionsCollection.getDecisions());

    if (isDamagedAlert(suspects)) {
      log.debug("Alert: {} has no suspects or invalid record / hit details data", getSystemId());
      builder.state(State.STATE_DAMAGED);
    }

    return builder.build();
  }

  private boolean isDamagedAlert(SuspectsCollection suspects) {
    return !suspects.hasSuspects() || alertedParty.isEmpty();
  }

  private AlertDetails createDetails(String watchlistId) {
    return AlertDetails
        .builder()
        .batchId(nullToEmpty(alertData.getBatchId()))
        .unit(nullToEmpty(alertData.getUnit()))
        .account(nullToEmpty(alertData.getDbAccount()))
        .systemId(alertData.getSystemId())
        .watchlistId(watchlistId)
        .build();
  }

  private String getWatchlistId(List<Match> matches) {
    if (watchlistLevel) {
      validateWatchlistLevelMatches(matches.size());
      return matches.get(0).matchedParty().id().sourceId();
    }
    return EMPTY_WATCHLIST_ID;
  }

  private static void validateWatchlistLevelMatches(int matchCount) {
    if (matchCount != 1)
      throw new IllegalStateException("Processed alert has incorrect number of matches");
  }

  private ObjectId createId(String watchlistId) {
    Instant lastResetDecisionDate = decisionsCollection
        .getLastResetDecisionDate()
        .orElse(getFiltered());

    return makeId(nullToEmpty(getSystemId()), watchlistId, lastResetDecisionDate);
  }

  String getSystemId() {
    return alertData.getSystemId();
  }

  private int getFlags() {
    int flags = Flag.NONE.getValue();

    if (requestRecommendation)
      flags |= Flag.RECOMMEND.getValue() | Flag.PROCESS.getValue() | Flag.ATTACH.getValue();

    if (requestProcessing)
      flags |= Flag.PROCESS.getValue();

    return flags;
  }

  private String getCountry() {
    return ofNullable(getSystemId()).map(id -> id.substring(0, 2)).orElse("");
  }

  private List<Match> makeMatches(SuspectsCollection suspects) {
    return suspects.makeMatches(buildAlertContext());
  }

  AlertContext buildAlertContext() {
    var builder = AlertContext.builder();

    alertedParty.getName().ifPresent(builder::partyName);

    return builder
        .typeOfRec(nullToEmpty(alertData.getTypeOfRec()))
        .lastDecBatchId(alertData.getLastDecBatchId())
        .lastDecisionPresent(decisionsCollection.getLastDecision().isPresent())
        .partyAlternateNames(new ArrayList<>(alertedParty.getAlternateNames()))
        .build();
  }

  private AlertedParty makeAlertedParty(GnsParty party) {
    List<String> allNames = new ArrayList<>();
    party.getName().ifPresent(allNames::add);
    allNames.addAll(party.getAlternateNames());

    String genderFromName =
        GenderDetector.determineApGenderFromName(alertData.getTypeOfRec(), allNames);

    AlertedPartyBuilder builder = AlertedParty.builder()
        .id(makeAlertedPartyId(nullToEmpty(alertData.getRecordId()), recordSignature))
        .apId(getSystemId())
        .apGenderFromName(genderFromName)
        .apDocNationalIds(party.getNationalIds())
        .apNameSynonyms(party.getAlternateNames())
        .apOriginalCnNames(getChineseNames(party));

    party.getName().ifPresent(builder::apName);
    mapString(party.getSourceSystemIdentifier(), builder::apSrcSysId);
    party
        .mapString("nationalityAll", builder::apNationality)
        .mapString("registOrResidenAddCntry", builder::apResidence)
        .mapString("customerStatus", builder::apCustStatus)
        .mapString("gender", builder::apGender)
        .mapString("bookingLocation", builder::apBookingLocation)
        .mapCollection("nationalities", builder::apNationalitySynonyms)
        .mapCollection("residencies", builder::apResidenceSynonyms)
        .mapCollection("residentialAddresses", builder::apResidentialAddresses)
        .mapCollection("passportNumbers", builder::apDocPassports)
        .mapCollection("identifications", builder::apDocOthers)
        .mapString("clientType", builder::custType)
        .mapString("dateOfBirthOrRegis", builder::apDobDoi)
        .mapString("bookingLocation", builder::apDbCountry);

    return builder.build();
  }

  static Set<String> getChineseNames(GnsParty party) {
    return ChineseNamesResolver.getChineseNames(party);
  }

  @Nonnull
  Iterable<Tag> getMeasuringTags() {
    String analystSolution =
        decisionsCollection.getLastSolution().orElse(AnalystSolution.ANALYST_NO_SOLUTION).name();
    String sourceSystemIdentifier = alertedParty.getSourceSystemIdentifier();

    return Tags.of(
        "unit", nullToEmpty(alertData.getUnit()),
        "country", getCountry(),
        "solution", analystSolution.substring(ANALYST_TEXT_OFFSET),
        "ssi", nullToEmpty(sourceSystemIdentifier));
  }
}
