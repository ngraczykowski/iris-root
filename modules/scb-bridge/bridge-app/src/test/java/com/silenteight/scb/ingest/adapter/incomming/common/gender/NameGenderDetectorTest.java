package com.silenteight.scb.ingest.adapter.incomming.common.gender;

import org.assertj.core.api.AbstractComparableAssert;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.silenteight.scb.ingest.adapter.incomming.common.gender.Gender.FEMALE;
import static com.silenteight.scb.ingest.adapter.incomming.common.gender.Gender.MALE;
import static com.silenteight.scb.ingest.adapter.incomming.common.gender.Gender.UNKNOWN;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;

class NameGenderDetectorTest {

  @Nested
  class EmptyData {

    @Test
    void emptyDict_returnUnknown() {
      NameGenderDetector checker = new NameGenderDetector(new EmptyNameGenderData());

      Gender gender = checker.detect(singletonList("Name"));

      assertThat(gender).isEqualTo(UNKNOWN);
    }
  }

  @Nested
  class StubData {

    @Test
    void maleNameFromDict_giveMale() {
      assertThatGenderOf("kamil").isEqualTo(MALE);
    }

    private AbstractComparableAssert<?, Gender> assertThatGenderOf(String... names) {
      NameGenderData genderDataStub = new NameGenderDataStub();

      NameGenderDetector detector =
          new NameGenderDetector(genderDataStub);

      return assertThat(detector.detect(asList(names)));
    }

    @Test
    void femaleNameFromDict_giveFemale() {
      assertThatGenderOf("Emilia").isEqualTo(FEMALE);
    }

    @Test
    void multipleMaleNames_giveMale() {
      assertThatGenderOf("Piotr", "Kamil").isEqualTo(MALE);
    }

    @Test
    void multipleFemaleNames_giveFemale() {
      assertThatGenderOf("Aleksandra", "Emilia").isEqualTo(FEMALE);
    }

    @Test
    void maleAndFemaleNames_giveUnknown() {
      assertThatGenderOf("Kamil", "Emilia").isEqualTo(UNKNOWN);
    }

    @Test
    void namesNotAvailableInDict_giveUnknown() {
      assertThatGenderOf("Arnold").isEqualTo(UNKNOWN);
    }
  }
}
