package com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.builder.consumerrepo;

import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect;
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Synonym;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.*;

public class SuspectSynonymConsumerRepositoryTest {

  private SuspectSynonymConsumerRepository repo;

  @Before
  public void setUp() {
    repo = new SuspectSynonymConsumerRepository();
  }

  @Test
  public void nameTest() {
    testRepo("NAME", Suspect::getNameSynonyms);
  }

  private void testRepo(String key, Function<Suspect, List<Synonym>> function) {
    assertThat(repo.find(key))
        .isPresent()
        .hasValueSatisfying(c -> {
          Suspect t = new Suspect();
          Synonym synonym = new Synonym("a", true);
          c.accept(t, synonym);
          assertThat(function.apply(t)).containsExactly(synonym);
        });
  }

  @Test
  public void addressTest() {
    testRepo("ADDRESS", Suspect::getAddressSynonyms);
  }

  @Test
  public void cityTest() {
    testRepo("CITY", Suspect::getCitySynonyms);
  }

  @Test
  public void countryTest() {
    testRepo("COUNTRY", Suspect::getCountrySynonyms);
  }

  @Test
  public void stateTest() {
    testRepo("STATE", Suspect::getStateSynonyms);
  }
}
