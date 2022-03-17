package com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.builder.consumerrepo;

import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect;
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Tag;
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Type;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;

import static java.util.Map.of;
import static org.assertj.core.api.Assertions.*;

public class SuspectAttributeConsumerRepositoryTest {

  private SuspectAttributeConsumerRepository repo;

  @Before
  public void setUp() {
    repo = new SuspectAttributeConsumerRepository();
  }

  @Test
  public void testStringAttributes() {
    repoTestString("NAME", Suspect::getName);
    repoTestString("ADDRESS", Suspect::getAddress);
    repoTestString("CITY", Suspect::getCity);
    repoTestString("STATE", Suspect::getState);
    repoTestString("COUNTRY", Suspect::getCountry);
    repoTestString("FML INFO", Suspect::getFmlInfo);
    repoTestString("ADDITIONAL INFOS", Suspect::getAdditionalInfo);
    repoTestString("NATIONALITY", Suspect::getNationality);
    repoTestString("DATE OF BIRTH", Suspect::getBirthDate);
    repoTestString("PLACE OF BIRTH", Suspect::getBirthPlace);
    repoTestString("NATID", Suspect::getNationalId);
    repoTestString("PASSPORT", Suspect::getPassport);
    repoTestString("OFFICIAL REF", Suspect::getOfficialRef);
    repoTestString("DESIGNATION", Suspect::getDesignation);
    repoTestString("TYPE", Suspect::getType);
    repoTestString("ORIGIN", Suspect::getOrigin);
    repoTestString("OFAC ID", Suspect::getOfacId);
    repoTestString("BATCH", Suspect::getBatchId);
    repoTestString("MATCHINGTEXT", Suspect::getMatchingText);
    repoTestString("USER DATA 1", Suspect::getUserData1);
    repoTestString("USER DATA 2", Suspect::getUserData2);
  }

  private void repoTestString(String key, Function<Suspect, String> function) {
    repoTest(key, "text", "text", function);
  }

  private <T> void repoTest(
      String key, String value, T expectedValue,
      Function<Suspect, T> function) {
    assertThat(repo.find(key))
        .isPresent()
        .hasValueSatisfying(c -> {
          Suspect suspect = new Suspect();
          c.accept(suspect, value);
          assertThat(function.apply(suspect)).isEqualTo(expectedValue);
        });
  }

  @Test
  public void testIntegerAttributes() {
    repoTestInteger("ISN", Suspect::getIsn);
    repoTestInteger("TYS", Suspect::getTys);
    repoTestInteger("FML CONFIDENTIALITY", Suspect::getFmlConfidentiality);
    repoTestInteger("FML PRIORITY", Suspect::getFmlPriority);
    repoTestInteger("FML TYPE", Suspect::getFmlType);
    repoTestInteger("index", Suspect::getIndex);
  }

  private void repoTestInteger(String key, Function<Suspect, Integer> function) {
    repoTest(key, "1", 1, function);
  }

  @Test
  public void testOtherAttributes() {
    repoTest("SEARCH CODES", "123 123", Arrays.asList("123", "123"), Suspect::getSearchCodes);
    repoTest("BIC CODES", "123 123", Arrays.asList("123", "123"), Suspect::getBicCodes);
    repoTest("TAG", "NAM", Tag.NAME, Suspect::getTag);
    repoTest("TYPE", "I", Type.INDIVIDUAL.getAbbreviation(), Suspect::getType);
    repoTest("MATCH", "0.01", 0.01f, Suspect::getMatch);
    repoTest("PEP-FEP", "1 2", 1, Suspect::getPep);
    repoTest("PEP-FEP", "1 2", 2, Suspect::getFep);
    repoTest("ADDITIONAL INFOS", "", new HashMap<>(), Suspect::getNotes);
    repoTest("ADDITIONAL INFOS", "TeSt: 123 / ABC: 456",
        of("test", "123", "abc", "456"), Suspect::getNotes);
  }
}
