package com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import com.silenteight.scb.ingest.adapter.incomming.cbs.domain.CbsHitDetails;
import com.silenteight.scb.ingest.adapter.incomming.cbs.domain.NeoFlag;
import com.silenteight.scb.ingest.adapter.incomming.common.WlName;
import com.silenteight.scb.ingest.adapter.incomming.common.WlNameType;
import com.silenteight.scb.ingest.adapter.incomming.common.gender.GenderDetector;
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.support.StringsList;
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.support.SynonymsList;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.MatchDetails;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.MatchedParty;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.MatchedParty.WatchlistName;
import com.silenteight.scb.ingest.adapter.incomming.common.util.AlertParserUtils;
import com.silenteight.scb.ingest.adapter.incomming.common.util.MatchingTextTransformer;
import com.silenteight.scb.ingest.adapter.incomming.common.validation.ChineseCharactersValidator;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.silenteight.scb.ingest.adapter.incomming.common.util.AlertParserUtils.determineApType;
import static com.silenteight.scb.ingest.adapter.incomming.common.util.AlertParserUtils.mapString;
import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Data
public class Suspect {

  private final Set<Tag> otherTags = EnumSet.noneOf(Tag.class);
  private final SynonymsList nameSynonyms = new SynonymsList();
  private final SynonymsList addressSynonyms = new SynonymsList();
  private final SynonymsList citySynonyms = new SynonymsList();
  private final SynonymsList countrySynonyms = new SynonymsList();
  private final SynonymsList stateSynonyms = new SynonymsList();
  private final StringsList searchCodes = new StringsList();
  private final StringsList bicCodes = new StringsList();
  private Integer index;
  private String ofacId;
  private Float match;
  private Tag tag;
  private String matchingText;
  private Set<String> mergedMatchingTexts = new HashSet<>();
  private String batchId;
  private String name;
  private String address;
  private String city;
  private String country;
  private String state;
  private String origin;
  private String designation;
  private String type;
  private String userData1;
  private String userData2;
  private String officialRef;
  private String passport;
  private String nationalId;
  private String birthPlace;
  private String birthDate;
  private String nationality;
  private String additionalInfo;
  private Integer fmlType;
  private Integer fmlPriority;
  private Integer fmlConfidentiality;
  private String fmlInfo;
  private Integer pep;
  private Integer fep;
  private Integer tys;
  private Integer isn;
  private Map<String, String> notes = new HashMap<>();
  private NeoFlag neoFlag;

  @Getter(AccessLevel.NONE)
  private Set<WlName> activeNames;

  private static Set<WlName> union(Set<WlName> set1, List<WlName> set2) {
    HashSet<WlName> output = new HashSet<>(set1);
    output.addAll(set2);
    return output;
  }

  private static Optional<Tag> selectMostImportantTag(Collection<Tag> tags) {
    return tags
        .stream()
        .sorted()
        .findFirst();
  }

  private static List<WatchlistName> mapWatchlistNames(List<WlName> watchlistNames) {
    return watchlistNames
        .stream()
        .map(name -> new WatchlistName(name.getName(), name.getType().name()))
        .collect(Collectors.toList());
  }

  @Nullable
  public String getBatchId() {
    return batchId;
  }

  public void setMatchingText(String matchingText) {
    if (isNotBlank(this.matchingText))
      mergedMatchingTexts.remove(this.matchingText);

    this.matchingText = matchingText;

    if (isNotBlank(matchingText))
      mergedMatchingTexts.add(matchingText);
  }

  Suspect merge(Suspect other) {
    if (ofacId != null && other.ofacId != null && !ofacId.equals(other.ofacId))
      throw new IllegalArgumentException("Can't merge suspects with different OFAC IDs");

    mergeNames(other);
    mergeTagsIntoThisSuspect(other);
    mergeIndex(other);
    mergeBatchId(other.getBatchId());
    mergeMatchingTexts(other.matchingText);

    return this;
  }

  private void mergeMatchingTexts(String otherMatchingText) {
    if (isNotBlank(otherMatchingText))
      mergedMatchingTexts.add(otherMatchingText);
  }

  private void mergeNames(Suspect other) {
    boolean thisHasTagName = hasTag(Tag.NAME);
    boolean otherHasTagName = other.hasTag(Tag.NAME);

    boolean bothHaveTagName = thisHasTagName && otherHasTagName;
    boolean noneHaveTagName = !thisHasTagName && !otherHasTagName;
    if (bothHaveTagName || noneHaveTagName) {
      mergeActiveNames(other.getActiveNames());
    } else if (thisHasTagName) {
      initActiveNames();
    } else {
      overwriteOurActiveNamesWithOthers(other.getActiveNames());
    }

    mergeNameSynonyms(other.getNameSynonyms());
  }

  private void mergeNameSynonyms(SynonymsList otherNameSynonyms) {
    this.nameSynonyms.merge(otherNameSynonyms);
  }

  private void overwriteOurActiveNamesWithOthers(List<WlName> otherActiveNames) {
    activeNames = new HashSet<>(otherActiveNames);
  }

  private void mergeTagsIntoThisSuspect(Suspect otherSuspect) {
    Set<Tag> tags = EnumSet.noneOf(Tag.class);

    Tag thisTag = getTag();
    if (thisTag != null)
      tags.add(thisTag);
    tags.addAll(getOtherTags());

    Tag otherTag = otherSuspect.getTag();
    if (otherTag != null)
      tags.add(otherTag);
    tags.addAll(otherSuspect.getOtherTags());

    Optional<Tag> mostImportantTag = selectMostImportantTag(tags);

    if (mostImportantTag.isPresent()) {
      setTag(mostImportantTag.get());
      otherTags.clear();
      otherTags.addAll(tags);
      otherTags.remove(mostImportantTag.get());
    } else {
      setTag(null);
      otherTags.clear();
    }
  }

  private void mergeBatchId(String otherBatchId) {
    if (batchId == null || otherBatchId != null && otherBatchId.compareTo(batchId) > 0)
      batchId = otherBatchId;
  }

  private void mergeIndex(Suspect other) {
    if (index != null && other.index != null)
      index = Math.min(index, other.index);
  }

  private void initActiveNames() {
    if (activeNames == null) {
      activeNames = createActiveNamesSet();
    }
  }

  private void mergeActiveNames(List<WlName> otherSuspectActiveNames) {
    initActiveNames();
    activeNames = union(this.activeNames, otherSuspectActiveNames);
  }

  public Set<String> getTags() {
    Set<String> tags = new TreeSet<>();

    if (tag != null)
      tags.add(tag.name());

    otherTags.stream().map(Tag::name).forEach(tags::add);

    return tags;
  }

  private boolean hasTag(Tag tagToCheck) {
    return getTags().contains(tagToCheck.name());
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setType(Type type) {
    this.type = type.getAbbreviation();
  }

  public List<WlName> getActiveNames() {
    initActiveNames();
    return new ArrayList<>(activeNames);
  }

  public List<WlName> getOriginalChineseNames() {
    var chineseNames = new ArrayList<WlName>();

    if (hasOriginalChineseName(name)) {
      chineseNames.add(new WlName(name, WlNameType.NAME));
    }

    if (hasTag(Tag.NAME)) {
      streamChineseNameSynonyms()
          .map(name -> new WlName(name, WlNameType.ALIAS))
          .forEach(chineseNames::add);
    }

    return chineseNames;
  }

  private boolean hasOriginalChineseName(String name) {
    return ChineseCharactersValidator.isValid(name);
  }

  private Set<WlName> createActiveNamesSet() {
    if (hasTag(Tag.NAME)) {
      Set<WlName> activeNamesPrototype = streamActiveNameSynonyms()
          .map(name -> new WlName(name, WlNameType.ALIAS))
          .collect(toSet());
      if (!activeNamesPrototype.isEmpty())
        return activeNamesPrototype;
    }

    return getPrimaryNameOrSynonymsIfNotAvailable();
  }

  private Set<WlName> getPrimaryNameOrSynonymsIfNotAvailable() {
    if (name != null)
      return Collections.singleton(new WlName(name, WlNameType.NAME));
    else
      return nameSynonyms.asSetOfNames()
          .stream()
          .map(name -> new WlName(name, WlNameType.ALIAS)).collect(
              Collectors.toSet());
  }

  private Stream<String> streamActiveNameSynonyms() {
    return nameSynonyms
        .stream()
        .filter(Synonym::isActive)
        .map(Synonym::getText);
  }

  private Stream<String> streamChineseNameSynonyms() {
    return nameSynonyms
        .stream()
        .map(Synonym::getText)
        .filter(this::hasOriginalChineseName);
  }

  public boolean hasNeoFlag() {
    return nonNull(neoFlag);
  }

  public void loadSuspectWithNeoFlag(List<CbsHitDetails> cbsHitDetails) {
    Integer seqNo = getIndex();
    if (seqNo == null || cbsHitDetails.isEmpty())
      return;

    cbsHitDetails.stream()
        .filter(r -> seqNo.equals(r.getSeqNo()))
        .findFirst()
        .ifPresent(c -> setNeoFlag(c.getHitNeoFlag()));
  }

  public MatchDetails makeMatchDetails(
      Set<String> matchingTexts, @Nullable String name, List<String> alternateNames) {

    MatchDetails.MatchDetailsBuilder builder =
        isNameTag() ? buildNameMatchDetails(name, alternateNames)
                    : buildNonNameMatchDetails(name, alternateNames);

    return builder.matchingTexts(matchingTexts).build();
  }

  private boolean isNameTag() {
    return tag == Tag.NAME;
  }

  @Nonnull
  private MatchDetails.MatchDetailsBuilder buildNameMatchDetails(
      @Nullable String name, List<String> alternateNames) {

    MatchingTextTransformer transformer =
        new MatchingTextTransformer(getMergedMatchingTexts(), name, alternateNames);

    transformer.transform();

    String matchedName = transformer.getName();
    Collection<String> matchedAlternateNames = transformer.getAlternateNames();

    Set<String> matchedNames = new TreeSet<>();
    if (matchedName != null)
      matchedNames.add(matchedName);
    if (matchedAlternateNames != null)
      matchedNames.addAll(matchedAlternateNames);

    return MatchDetails.builder().matchedApNames(matchedNames);
  }

  @Nonnull
  private static MatchDetails.MatchDetailsBuilder buildNonNameMatchDetails(
      @Nullable String name, List<String> alternateNames) {
    if (StringUtils.isNotEmpty(name))
      alternateNames.add(name);

    return MatchDetails.builder().matchedApNames(alternateNames);
  }

  public MatchedParty makeWatchlistParty(String typeOfRec, GenderDetector genderDetector) {
    List<String> wlNationalIds = AlertParserUtils.expand(
        singletonList(getNationalId()), getSearchCodes().asList());
    List<String> names = AlertParserUtils.expand(
        singletonList(getName()), getNameSynonyms().asListOfNames());

    MatchedParty.MatchedPartyBuilder builder = MatchedParty.builder()
        .id(AlertParserUtils.makeWatchlistPartyId(getOfacId(), getBatchId()))
        .wlGenderFromName(genderDetector.determineWlGenderFromName(typeOfRec, names))
        .wlNameSynonyms(getNameSynonyms().asListOfNames())
        .wlNames(mapWatchlistNames(getActiveNames()))
        .wlHitNames(getActiveNames().stream().map(WlName::getName).collect(toList()))
        .wlSearchCodes(getSearchCodes().asList())
        .wlNationalIds(wlNationalIds)
        .wlBicCodes(getBicCodes().asList())
        .wlHitType(getTags())
        .wlOriginalCnNames(mapWatchlistNames(getOriginalChineseNames()));

    mapString(determineApType(typeOfRec, getType()), builder::apType);
    mapString(getNotes().getOrDefault("gender", EMPTY), builder::wlGender);
    mapString(getOfacId(), builder::wlId);
    mapString(getName(), builder::wlName);
    mapString(getType(), builder::wlType);
    mapString(getBirthDate(), builder::wlDob);
    mapString(getNotes().getOrDefault("nationality", EMPTY), builder::wlNationality);
    mapString(getCountry(), builder::wlResidence);
    mapString(getNationalId(), builder::wlNationalId);
    mapString(getPassport(), builder::wlPassport);
    mapString(getCountry(), builder::wlCountry);
    mapString(getDesignation(), builder::wlDesignation);
    mapString(getNotes().getOrDefault("title", EMPTY), builder::wlTitle);

    return builder.build();
  }
}
