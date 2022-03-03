package com.silenteight.customerbridge.common.batch;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.customerbridge.common.alertrecord.AlertRecord;
import com.silenteight.customerbridge.common.gender.GenderDetector;
import com.silenteight.customerbridge.common.gnsparty.GnsParty;
import com.silenteight.customerbridge.common.gnsparty.SupplementaryInformationHelper;
import com.silenteight.customerbridge.common.validation.ChineseCharactersValidator;
import com.silenteight.proto.serp.scb.v1.ScbAlertDetails;
import com.silenteight.proto.serp.scb.v1.ScbAlertedPartyDetails;
import com.silenteight.proto.serp.v1.alert.Alert;
import com.silenteight.proto.serp.v1.alert.Alert.Flags;
import com.silenteight.proto.serp.v1.alert.Alert.State;
import com.silenteight.proto.serp.v1.alert.AnalystSolution;
import com.silenteight.proto.serp.v1.alert.Match;
import com.silenteight.proto.serp.v1.alert.Party;
import com.silenteight.proto.serp.v1.alert.Party.Source;
import com.silenteight.proto.serp.v1.common.ObjectId;
import com.silenteight.sep.base.common.protocol.AnyUtils;

import com.google.protobuf.Any;
import com.google.protobuf.Timestamp;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Strings.nullToEmpty;
import static com.silenteight.customerbridge.common.util.AlertParserUtils.makeAlertedPartyId;
import static com.silenteight.customerbridge.common.util.AlertParserUtils.makeId;
import static com.silenteight.customerbridge.common.util.AlertParserUtils.mapString;
import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static com.silenteight.protocol.utils.MoreTimestamps.toTimestampOrDefault;
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

    Alert.Builder builder = Alert
        .newBuilder()
        .setId(createId(watchlistId))
        .setFlags(getFlags())
        .setDecisionGroup(nullToEmpty(alertData.getUnit()))
        .setSecurityGroup(getCountry())
        .setState(State.STATE_CORRECT)
        .setGeneratedAt(toTimestampOrDefault(filtered, Timestamp.getDefaultInstance()))
        .setReceivedAt(toTimestamp(receivedAt))
        .setAlertedParty(makeAlertedParty(alertedParty))
        .addAllMatches(matches)
        .setDetails(createDetails(watchlistId))
        .addAllDecisions(decisionsCollection.getDecisions());

    if (isDamagedAlert(suspects)) {
      log.debug("Alert: {} has no suspects or invalid record / hit details data", getSystemId());
      builder.setState(State.STATE_DAMAGED);
    }

    return builder.build();
  }

  private boolean isDamagedAlert(SuspectsCollection suspects) {
    return !suspects.hasSuspects() || alertedParty.isEmpty();
  }

  String getSystemId() {
    return alertData.getSystemId();
  }

  private Any createDetails(String watchlistId) {
    ScbAlertDetails.Builder detailsBuilder = ScbAlertDetails
        .newBuilder()
        .setBatchId(nullToEmpty(alertData.getBatchId()))
        .setUnit(nullToEmpty(alertData.getUnit()))
        .setAccount(nullToEmpty(alertData.getDbAccount()))
        .setSystemId(getSystemId())
        .setWatchlistId(watchlistId);

    return AnyUtils.pack(detailsBuilder.build());
  }

  private String getWatchlistId(List<Match> matches) {
    if (watchlistLevel) {
      validateWatchlistLevelMatches(matches.size());
      return matches.get(0).getMatchedParty().getId().getSourceId();
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

  private int getFlags() {
    int flags = Flags.FLAG_NONE_VALUE;

    if (requestRecommendation)
      flags |= Flags.FLAG_RECOMMEND_VALUE | Flags.FLAG_PROCESS_VALUE | Flags.FLAG_ATTACH_VALUE;

    if (requestProcessing)
      flags |= Flags.FLAG_PROCESS_VALUE;

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

  private Party makeAlertedParty(GnsParty party) {
    List<String> allNames = new ArrayList<>();
    party.getName().ifPresent(allNames::add);
    allNames.addAll(party.getAlternateNames());

    String genderFromName =
        GenderDetector.determineApGenderFromName(alertData.getTypeOfRec(), allNames);

    ScbAlertedPartyDetails.Builder builder = ScbAlertedPartyDetails
        .newBuilder()
        .setApId(getSystemId())
        .setApGenderFromName(genderFromName)
        .addAllApDocNationalIds(party.getNationalIds())
        .addAllApNameSynonyms(party.getAlternateNames())
        .addAllApOriginalCnNames(getChineseNames(party));

    party.getName().ifPresent(builder::setApName);

    mapString(party.getSourceSystemIdentifier(), builder::setApSrcSysId);

    party
        .mapString("nationalityAll", builder::setApNationality)
        .mapString("registOrResidenAddCntry", builder::setApResidence)
        .mapString("customerStatus", builder::setApCustStatus)
        .mapString("gender", builder::setApGender)
        .mapString("bookingLocation", builder::setApBookingLocation)
        .mapCollection("nationalities", builder::addAllApNationalitySynonyms)
        .mapCollection("residencies", builder::addAllApResidenceSynonyms)
        .mapCollection("residentialAddresses", builder::addAllApResidentialAddresses)
        .mapCollection("passportNumbers", builder::addAllApDocPassports)
        .mapCollection("identifications", builder::addAllApDocOthers)
        .mapString("clientType", builder::setCustType)
        .mapString("dateOfBirthOrRegis", builder::setApDobDoi)
        .mapString("bookingLocation", builder::setApDbCountry);

    return Party
        .newBuilder()
        .setId(makeAlertedPartyId(nullToEmpty(alertData.getRecordId()), recordSignature))
        .setSource(Source.SOURCE_CONFIDENTIAL)
        .setDetails(AnyUtils.pack(builder.build()))
        .build();
  }

  static Set<String> getChineseNames(GnsParty party) {
    var chineseNames = new HashSet<String>();

    new SupplementaryInformationHelper(party).getChineseNameFromSupplementaryInformation1()
        .ifPresent(chineseNames::add);

    party.getName().filter(ChineseCharactersValidator::isValid)
        .ifPresent(chineseNames::add);

    party.getAlternateNames().stream()
        .filter(ChineseCharactersValidator::isValid)
        .forEach(chineseNames::add);

    return chineseNames;
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
