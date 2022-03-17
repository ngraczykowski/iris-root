package com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.builder.consumerrepo;

import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.HitDetails;

import org.junit.Before;
import org.junit.Test;

import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class HitDetailsAttributeConsumerRepositoryTest {

  private HitDetailsAttributeConsumerRepository repo;

  @Before
  public void setUp() {
    repo = new HitDetailsAttributeConsumerRepository();
  }

  @Test
  public void systemIdTest() {
    repoTestString("SystemId", HitDetails::getSystemId);
  }

  private void repoTestString(String key, Function<HitDetails, String> function) {
    repoTest(key, "text", "text", function);
  }

  private <T> void repoTest(
      String key,
      String value,
      T expectedValue,
      Function<HitDetails, T> function) {

    assertThat(repo.find(key))
        .isPresent()
        .hasValueSatisfying(c -> {
          HitDetails hitDetails = new HitDetails();
          c.accept(hitDetails, value);
          assertThat(function.apply(hitDetails)).isEqualTo(expectedValue);
        });
  }

  @Test
  public void hasSndRcvInTest() {
    repoTest("HasSndRcvIn", "HasSndRcvIn", true, HitDetails::getHasSndRcvIn);
  }

  @Test
  public void limitedTest() {
    repoTestInteger("Limited", HitDetails::getLimited);
  }

  private void repoTestInteger(String key, Function<HitDetails, Integer> function) {
    repoTest(key, "1", 1, function);
  }
}
