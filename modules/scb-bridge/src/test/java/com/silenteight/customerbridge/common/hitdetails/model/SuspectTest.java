package com.silenteight.customerbridge.common.hitdetails.model;

import com.silenteight.customerbridge.common.gender.GenderDetector;
import com.silenteight.proto.serp.scb.v1.ScbWatchlistPartyDetails;
import com.silenteight.proto.serp.v1.alert.Party;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.TreeSet;

import static com.google.common.collect.ImmutableSet.of;
import static com.silenteight.sep.base.common.protocol.AnyUtils.maybeUnpack;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SuspectTest {

  @Test
  void givenSuspectsWithDifferentOfacId_merge_throwsException() {
    Suspect suspect1 = createSuspect("ofacId1", Tag.NAME, emptySet());
    Suspect suspect2 = createSuspect("ofacId2", Tag.NAME, emptySet());

    assertThrows(IllegalArgumentException.class, () -> suspect1.merge(suspect2));
  }

  private Suspect createSuspect(String ofacId, Tag mainTag, Set<Tag> otherTags) {
    Suspect suspect = new Suspect();
    suspect.setOfacId(ofacId);
    suspect.setTag(mainTag);
    suspect.getOtherTags().addAll(otherTags);
    return suspect;
  }

  @Test
  void givenTwoEmptySuspects_merge_shouldPass() {
    Suspect suspect1 = new Suspect();
    Suspect suspect2 = new Suspect();

    suspect1.merge(suspect2);
  }

  @Test
  void givenThreeSuspects_tagsShouldBeCopiedFromAllOfThem() {
    Suspect suspect1 = createSuspect("ofacId1", Tag.NATIONAL_ID, emptySet());
    Suspect suspect2 = createSuspect("ofacId1", Tag.PASSPORT, emptySet());
    Suspect suspect3 = createSuspect("ofacId1", Tag.SEARCH_CODE, emptySet());
    Suspect suspect4 = createSuspect("ofacId1", Tag.NAME, emptySet());

    suspect1.merge(suspect2);
    suspect1.merge(suspect3);
    suspect1.merge(suspect4);

    Set<String> expectedTags = new TreeSet<>(asList(
        Tag.SEARCH_CODE.name(), Tag.NAME.name(), Tag.PASSPORT.name(), Tag.NATIONAL_ID.name()
    ));
    assertThat(suspect1.getTags()).isEqualTo(expectedTags);
  }

  @Test
  void givenEmptyAndFilledSuspect_merge_shouldReturnSuspectWithValidValues() {
    Tag moreImportantTag = Tag.values()[0];
    Tag lessImportantTag = Tag.values()[1];
    Suspect emptySuspect = new Suspect();
    Suspect filledSuspect = createSuspect("ofacId", moreImportantTag, Set.of(lessImportantTag));

    Suspect actual = filledSuspect.merge(emptySuspect);

    assertThat(actual.getTag()).isEqualTo(moreImportantTag);
    assertThat(actual.getOtherTags()).containsOnly(lessImportantTag);
  }

  @Test
  void givenTwoSuspectsWithSameTagsAndOfac_merge_returnsSuspectWithSameTagsAsGiven() {
    Tag moreImportantTag = Tag.values()[0];
    Tag lessImportantTag = Tag.values()[1];

    Suspect suspect1 = createSuspect("ofacId", moreImportantTag, Set.of(lessImportantTag));
    Suspect suspect2 = createSuspect("ofacId", moreImportantTag, Set.of(lessImportantTag));

    Suspect actual = suspect1.merge(suspect2);

    assertThat(actual.getTag()).isEqualTo(moreImportantTag);
    assertThat(actual.getOtherTags()).containsOnly(lessImportantTag);
  }

  @Test
  void twoSuspectsAndOneWithMoreImportantTag_merge_returnsSuspectWithMoreImportantTagAsMainTag() {
    Tag moreImportantTag = Tag.values()[0];
    Tag lessImportantTag = Tag.values()[1];

    Suspect suspect1 = createSuspect("ofacId", moreImportantTag, emptySet());
    Suspect suspect2 = createSuspect("ofacId", lessImportantTag, emptySet());

    Suspect actualMergedFromFirst = suspect1.merge(suspect2);
    Suspect actualMergedFromSecond = suspect2.merge(suspect1);

    assertThat(actualMergedFromFirst.getTag()).isEqualTo(moreImportantTag);
    assertThat(actualMergedFromSecond.getTag()).isEqualTo(moreImportantTag);
  }

  @Test
  void twoSuspectsAndOneWithMoreImportantTag_merge_otherTagsContainsOnlyLessImportantTag() {
    Tag moreImportantTag = Tag.values()[0];
    Tag lessImportantTag = Tag.values()[1];

    Suspect suspect1 = createSuspect("ofacId", moreImportantTag, emptySet());
    Suspect suspect2 = createSuspect("ofacId", lessImportantTag, emptySet());

    Suspect actualMergedFromFirst = suspect1.merge(suspect2);
    Suspect actualMergedFromSecond = suspect2.merge(suspect1);

    assertThat(actualMergedFromFirst.getOtherTags()).containsOnly(lessImportantTag);
    assertThat(actualMergedFromSecond.getOtherTags()).containsOnly(lessImportantTag);
  }

  @Test
  void twoSuspectsWithDifferentImportanceTags_merge_returnsSubjectWithCorrectValues() {
    Tag mostImportantTag = Tag.values()[0];
    Tag lessImportantTag = Tag.values()[1];
    Tag evenLesserImportanceTag = Tag.values()[2];

    Suspect suspect1 = createSuspect(
        "ofacId", mostImportantTag, of(lessImportantTag, evenLesserImportanceTag));
    Suspect suspect2 = createSuspect("ofacId", lessImportantTag, Set.of(evenLesserImportanceTag));

    Suspect actualMergedFromFirst = suspect1.merge(suspect2);
    Suspect actualMergedFromSecond = suspect2.merge(suspect1);

    assertThat(actualMergedFromFirst.getTag()).isEqualTo(mostImportantTag);
    assertThat(actualMergedFromSecond.getTag()).isEqualTo(mostImportantTag);
    assertThat(actualMergedFromSecond.getOtherTags())
        .containsOnly(lessImportantTag, evenLesserImportanceTag);
    assertThat(actualMergedFromSecond.getOtherTags())
        .containsOnly(lessImportantTag, evenLesserImportanceTag);
  }

  @Test
  void givenPrimaryTag_returnPrimaryTagInTags() {
    Suspect suspect = createSuspect("ofacId", Tag.SEARCH_CODE, emptySet());

    Set<String> expected = of("SEARCH_CODE");
    assertThat(suspect.getTags()).isEqualTo(expected);
  }

  @Test
  void givenPrimaryTagAndOtherTags_mergeToTags() {
    Suspect suspect = createSuspect("ofacId", Tag.SEARCH_CODE, of(Tag.NAME, Tag.NATIONAL_ID));

    Set<String> expected = of("SEARCH_CODE", "NAME", "NATIONAL_ID");
    assertThat(suspect.getTags()).isEqualTo(expected);
  }

  @Test
  void givenNullPrimaryTag_returnEmptySetAsTags() {
    Suspect suspect = createSuspect("ofacId", null, emptySet());

    Set<String> expected = emptySet();
    assertThat(suspect.getTags()).isEqualTo(expected);
  }

  @Test
  void givenTwoSuspectsWithDifferentIndex_merge_returnsSuspectWithLowestIndex() {
    Suspect suspect1 = suspectWithIndex(5);
    Suspect suspect2 = suspectWithIndex(3);

    Suspect actual = suspect1.merge(suspect2);

    assertThat(actual.getIndex()).isEqualTo(3);
  }

  private Suspect suspectWithIndex(int index) {
    Suspect suspect = new Suspect();
    suspect.setIndex(index);
    return suspect;
  }

  @Test
  void givenThreeSuspectsWithDifferentBatchId_merge_returnsSuspectWithLatestBatchId() {
    // given
    Suspect suspect1 = suspectWithBatchId("2019/01/23_0003_IN_SCIC_DUDL");
    Suspect suspect2 = suspectWithBatchId("2019/01/27_0003_IN_SCIC_DUDL");
    Suspect suspect3 = suspectWithBatchId("2019/01/22_0003_IN_SCIC_DUDL");

    // when
    Suspect actual = suspect1.merge(suspect2);
    actual.merge(suspect3);

    assertThat(actual.getBatchId()).isEqualTo(suspect2.getBatchId());
  }

  private Suspect suspectWithBatchId(String batchId) {
    Suspect suspect = new Suspect();
    suspect.setBatchId(batchId);
    return suspect;
  }

  @Test
  void givenCountryAndDesignation_mapBothToProto() {
    // given
    Suspect suspect = createSuspect("ofacId", Tag.SEARCH_CODE, emptySet());
    suspect.setCountry("country");
    suspect.setDesignation("designation");

    // and
    GenderDetector genderDetector = mock(GenderDetector.class);
    when(genderDetector.determineWlGenderFromName(anyString(), anyList())).thenReturn("");

    // when
    Party party = suspect.makeWatchlistParty("typeOfRec", genderDetector);
    ScbWatchlistPartyDetails partyDetails =
        maybeUnpack(party.getDetails(), ScbWatchlistPartyDetails.class).get();

    // then
    assertThat(partyDetails.getWlCountry()).isEqualTo("country");
    assertThat(partyDetails.getWlDesignation()).isEqualTo("designation");
  }
}
