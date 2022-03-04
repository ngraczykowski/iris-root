package com.silenteight.customerbridge.common.hitdetails.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;

class HitDetailsTest {

  @Test
  void shouldMergeSuspectsGroupedByOfacIdAndSortedByBatchIdStartedWithDigits() {
    List<Suspect> suspects = asList(
        createSuspect("WL00088539", "2019/01/24_BATCH", Tag.NATIONAL_ID),
        createSuspect("AS09016116", "2019/02/21_BATCH", Tag.NAME),
        createSuspect("WL00088539", "2019/01/24_BATCH", Tag.PASSPORT),
        createSuspect("WL00088539", "2019/01/22_BATCH", Tag.NAME)
    );

    HitDetails hitDetails = new HitDetails();
    hitDetails.getSuspects().addAll(suspects);

    Suspect[] expectedSuspects = new Suspect[] {
        createSuspect("WL00088539", "2019/01/24_BATCH", Tag.PASSPORT, Tag.NATIONAL_ID),
        createSuspect("AS09016116", "2019/02/21_BATCH", Tag.NAME)
    };

    Collection<Suspect> output = hitDetails.extractUniqueSuspects();
    Assertions.assertThat(output).containsExactlyInAnyOrder(expectedSuspects);
  }

  private static Suspect createSuspect(String ofacId, String batchId, Tag mainTag) {
    Suspect suspect = new Suspect();
    suspect.setOfacId(ofacId);
    suspect.setTag(mainTag);
    suspect.setBatchId(batchId);
    return suspect;
  }

  private static Suspect createSuspect(
      String ofacId,
      String batchId,
      Tag mainTag,
      Tag... otherTags) {

    Suspect suspect = createSuspect(ofacId, batchId, mainTag);
    suspect.getOtherTags().addAll(asList(otherTags));
    return suspect;
  }

  @Test
  void shouldMergeSuspectsGroupedByOfacIdAndSortedByBatchIdStartedWithLetters() {
    List<Suspect> suspects = asList(
        createSuspect("WL00088539", "BATCH_2", Tag.NATIONAL_ID),
        createSuspect("AS09016116", "BATCH_1", Tag.NAME),
        createSuspect("WL00088539", "BATCH_2", Tag.PASSPORT),
        createSuspect("WL00088539", "BATCH_3", Tag.NAME)
    );

    HitDetails hitDetails = new HitDetails();
    hitDetails.getSuspects().addAll(suspects);

    Suspect[] expectedSuspects = new Suspect[] {
        createSuspect("WL00088539", "BATCH_3", Tag.NAME),
        createSuspect("AS09016116", "BATCH_1", Tag.NAME)
    };

    Collection<Suspect> output = hitDetails.extractUniqueSuspects();
    Assertions.assertThat(output).containsExactlyInAnyOrder(expectedSuspects);
  }

  @Test
  void shouldMergeSuspectsGroupedByOfacIdAndSortedByBatchIdNotStartedWithLettersOrDigits() {
    List<Suspect> suspects = asList(
        createSuspect("WL00088539", "_#ABC", Tag.NATIONAL_ID),
        createSuspect("AS09016116", "BATCH_1", Tag.NAME),
        createSuspect("WL00088539", "_#123", Tag.PASSPORT),
        createSuspect("WL00088539", "_#ABC", Tag.NAME)
    );

    HitDetails hitDetails = new HitDetails();
    hitDetails.getSuspects().addAll(suspects);

    Suspect[] expectedSuspects = new Suspect[] {
        createSuspect("WL00088539", "_#ABC", Tag.NAME, Tag.NATIONAL_ID),
        createSuspect("AS09016116", "BATCH_1", Tag.NAME)
    };

    Collection<Suspect> output = hitDetails.extractUniqueSuspects();
    Assertions.assertThat(output).containsExactlyInAnyOrder(expectedSuspects);
  }

  @Test
  void shouldMergeSuspectsGroupedByOfacIdAndSortedByNewestBatchId() {
    List<Suspect> suspects = asList(
        createSuspect("WL00088539", "BATCH_2", Tag.NATIONAL_ID),
        createSuspect("AS09016116", "BATCH_1", Tag.NAME),
        createSuspect("WL00088539", "BATCH_2", Tag.PASSPORT),
        createSuspect("WL00088539", "BATCH_3", Tag.NAME),
        createSuspect("WL00088539", "2019/01/24_BATCH", Tag.NATIONAL_ID),
        createSuspect("WL00088539", "2019/01/24_BATCH", Tag.PASSPORT),
        createSuspect("WL00088539", "2019/01/22_BATCH", Tag.PASSPORT),
        createSuspect("WL00088539", "¯\\_(ツ)_/¯", Tag.PASSPORT),
        createSuspect("AS11111111", "2019/05/30_BATCH", Tag.NAME),
        createSuspect("AS11111111", "OBSOLETE-2018/01/01", Tag.PASSPORT),
        createSuspect("AS11111111", null, Tag.NATIONAL_ID),
        createSuspect("AS22222222", null, Tag.NATIONAL_ID),
        createSuspect("AS09016116", "BATCH_1", Tag.NATIONAL_ID)
    );

    HitDetails hitDetails = new HitDetails();
    hitDetails.getSuspects().addAll(suspects);

    Suspect[] expectedSuspects = new Suspect[] {
        createSuspect("WL00088539", "2019/01/24_BATCH", Tag.PASSPORT, Tag.NATIONAL_ID),
        createSuspect("AS09016116", "BATCH_1", Tag.NAME, Tag.NATIONAL_ID),
        createSuspect("AS11111111", "2019/05/30_BATCH", Tag.NAME),
        createSuspect("AS22222222", null, Tag.NATIONAL_ID)
    };

    Collection<Suspect> output = hitDetails.extractUniqueSuspects();
    Assertions.assertThat(output).containsExactlyInAnyOrder(expectedSuspects);
  }
}
