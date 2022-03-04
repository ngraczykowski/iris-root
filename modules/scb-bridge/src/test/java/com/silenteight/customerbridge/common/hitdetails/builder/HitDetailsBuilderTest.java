package com.silenteight.customerbridge.common.hitdetails.builder;

import com.silenteight.customerbridge.common.hitdetails.model.Attribute;
import com.silenteight.customerbridge.common.hitdetails.model.HitDetails;
import com.silenteight.customerbridge.common.hitdetails.model.Suspect;
import com.silenteight.customerbridge.common.hitdetails.model.Synonym;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.*;

@RunWith(Enclosed.class)
public class HitDetailsBuilderTest {

  public abstract static class GivenHitDetailsBuilder {

    HitDetailsBuilder hitDetailsBuilder;

    @Before
    public void setUp() {
      hitDetailsBuilder = new HitDetailsBuilder(new HitDetailsBuilderConfig());
    }
  }

  public static class IllegalArgsTest extends GivenHitDetailsBuilder {


    @Test(expected = NullPointerException.class)
    public void givenNullAttribute_throwsNPE() {
      hitDetailsBuilder.onAttribute(null);
    }

    @Test(expected = NullPointerException.class)
    public void givenNullSynonym_throwsNPE() {
      hitDetailsBuilder.onSynonym(null);
    }

    @Test(expected = NullPointerException.class)
    public void givenNullText_throwsNPE() {
      hitDetailsBuilder.onText(null);
    }
  }

  public static class UnknownAttributesTest extends GivenHitDetailsBuilder {

    @Test
    public void givenUnknownHitDetailsAttribute_returnsValidHitDetails() {
      hitDetailsBuilder.onAttribute(new Attribute("unknown", "unknown"));

      HitDetails hitDetails = hitDetailsBuilder.build();

      assertThat(hitDetails).isEqualTo(new HitDetails());
    }

    @Test
    public void givenUnknownSuspectAttribute_returnsValidHitDetails() {
      hitDetailsBuilder.onDivider();
      hitDetailsBuilder.onText("Suspect detected #" + 0);
      hitDetailsBuilder.onAttribute(new Attribute("unknown", "unknown"));
      hitDetailsBuilder.onDivider();

      HitDetails hitDetails = hitDetailsBuilder.build();

      HitDetails expected = new HitDetails();
      Suspect suspect = new Suspect();
      suspect.setIndex(0);
      expected.getSuspects().add(suspect);
      assertThat(hitDetails).isEqualTo(expected);
    }

    @Test
    public void givenUnknownSuspectSynonym_returnsValidHitDetails() {
      hitDetailsBuilder.onDivider();
      hitDetailsBuilder.onText("Suspect detected #" + 0);
      hitDetailsBuilder.onAttribute(new Attribute("unknown", "unknown"));
      hitDetailsBuilder.onSynonym(new Synonym("synonym", false));
      hitDetailsBuilder.onDivider();

      HitDetails hitDetails = hitDetailsBuilder.build();

      HitDetails expected = new HitDetails();
      Suspect suspect = new Suspect();
      suspect.setIndex(0);
      expected.getSuspects().add(suspect);
      assertThat(hitDetails).isEqualTo(expected);
    }
  }

  public static class NormalBehaviourTest extends GivenHitDetailsBuilder {

    @Test
    public void givenSystemIdAttribute_hitDetailsHasValidAttributes() {
      hitDetailsBuilder.onAttribute(new Attribute("SystemId", "<systemId>"));

      HitDetails hitDetails = hitDetailsBuilder.build();

      assertThat(hitDetails.getSystemId()).isEqualTo("<systemId>");
    }

    @Test
    public void givenHasSndRcvIn_hitDetailsHasValidAttributes() {
      hitDetailsBuilder.onText("HasSndRcvIn");

      HitDetails hitDetails = hitDetailsBuilder.build();

      assertThat(hitDetails.getHasSndRcvIn()).isEqualTo(true);
    }

    @Test
    public void givenSuspectDetected_hitDetailsHasValidSuspectAttributes() {
      addSuspectAttributes(1);

      HitDetails hitDetails = hitDetailsBuilder.build();

      assertThat(hitDetails.getSuspects())
          .first()
          .satisfies(s -> assertThat(s.getIndex()).isEqualTo(1));
    }

    private void addSuspectAttributes(int index, Attribute... attributes) {
      hitDetailsBuilder.onDivider();
      hitDetailsBuilder.onText("Suspect detected #" + index);
      for (Attribute attribute : attributes) {
        hitDetailsBuilder.onAttribute(attribute);
      }
      hitDetailsBuilder.onDivider();
    }

    @Test
    public void givenOfacId_hitDetailsHasValidSuspectAttributes() {
      assertSuspectAttribute("OFAC ID", "ofac id", Suspect::getOfacId, "ofac id");
    }

    private <T> void assertSuspectAttribute(
        String attrKey, String attrValue, Function<Suspect, T> function, T expectedValue) {
      addSuspectAttributes(new Attribute(attrKey, attrValue));

      HitDetails hitDetails = hitDetailsBuilder.build();

      assertThat(hitDetails.getSuspects())
          .first()
          .satisfies(s -> assertThat(function.apply(s)).isEqualTo(expectedValue));
    }

    private void addSuspectAttributes(Attribute... attributes) {
      addSuspectAttributes(1, attributes);
    }

    @Test
    public void givenNameSynonyms_hitDetailsHasValidSuspectSynonyms() {
      assertSuspectSynonymAttribute("NAME", Suspect::getNameSynonyms,
          new Synonym("syn1", false),
          new Synonym("syn2", true));
    }

    private void assertSuspectSynonymAttribute(
        String attrKey, Function<Suspect, List<Synonym>> function, Synonym... synonyms) {
      addSuspectSynonymAttribute(new Attribute(attrKey, "value"), synonyms);

      HitDetails hitDetails = hitDetailsBuilder.build();

      assertThat(hitDetails.getSuspects())
          .first()
          .satisfies(s -> assertThat(function.apply(s)).isEqualTo(Arrays.asList(synonyms)));
    }

    private void addSuspectSynonymAttribute(Attribute attribute, Synonym... synonyms) {
      addSuspectSynonymAttribute(1, attribute, synonyms);
    }

    private void addSuspectSynonymAttribute(int index, Attribute attribute, Synonym... synonyms) {
      hitDetailsBuilder.onDivider();
      hitDetailsBuilder.onText("Suspect detected #" + index);
      hitDetailsBuilder.onAttribute(attribute);
      for (Synonym synonym : synonyms) {
        hitDetailsBuilder.onSynonym(synonym);
      }
      hitDetailsBuilder.onDivider();
    }

    @Test
    public void givenMultipleSuspectsWithDifferentAttribute_returnsValidHitDetails() {
      addSuspectAttributes(1, new Attribute("OFAC ID", "ofac_id1"));
      addSuspectAttributes(2, new Attribute("OFAC ID", "ofac_id2"));
      addSuspectAttributes(3, new Attribute("OFAC ID", "ofac_id3"));

      assertThat(hitDetailsBuilder.build())
          .satisfies(hd -> assertThat(hd.getSuspects())
              .hasSize(3)
              .satisfies(sl -> assertThat(sl.get(0))
                  .satisfies(s -> assertThat(s.getIndex()).isEqualTo(1))
                  .satisfies(s -> assertThat(s.getOfacId()).isEqualTo("ofac_id1")))
              .satisfies(sl -> assertThat(sl.get(1))
                  .satisfies(s -> assertThat(s.getIndex()).isEqualTo(2))
                  .satisfies(s -> assertThat(s.getOfacId()).isEqualTo("ofac_id2")))
              .satisfies(sl -> assertThat(sl.get(2))
                  .satisfies(s -> assertThat(s.getIndex()).isEqualTo(3))
                  .satisfies(s -> assertThat(s.getOfacId()).isEqualTo("ofac_id3"))));
    }
  }
}
