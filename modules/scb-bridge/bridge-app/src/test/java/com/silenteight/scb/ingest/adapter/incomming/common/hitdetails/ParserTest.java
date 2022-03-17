package com.silenteight.scb.ingest.adapter.incomming.common.hitdetails;

import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.*;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.*;

public class ParserTest {

  private HitDetailsParser hitDetailsParser;

  @Before
  public void setUp() {
    hitDetailsParser = new HitDetailsParser();
  }

  @Test(expected = NullPointerException.class)
  public void givenNullInput_throwsParseException() {
    hitDetailsParser.parse(null);
  }

  @Test
  public void givenTextFromResource_returnsValidHitDetails() {
    String text = ResourceUtil.readTextFromResource("ParserTest/complex_test_resource_1.txt");

    HitDetails hitDetails = hitDetailsParser.parse(text);

    HitDetails expected = createExpectedHitDetails();
    assertThat(hitDetails).isEqualTo(expected);
  }

  private HitDetails createExpectedHitDetails() {
    HitDetails expected = new HitDetails();
    expected.setLimited(0);
    expected.setSystemId("SOME_SYSTEM_ID");
    expected.setHasSndRcvIn(true);
    expected.getSuspects().add(createExpectedSuspect1());
    expected.getSuspects().add(createExpectedSuspect2());
    expected.getSuspects().add(createExpectedSuspect3());
    return expected;
  }

  private Suspect createExpectedSuspect1() {
    Suspect suspect = new Suspect();
    suspect.setIndex(1);
    suspect.setOfacId("AS00247275");
    suspect.setMatch(0f);
    suspect.setTag(Tag.NATIONAL_ID);
    suspect.setMatchingText("1136,");
    suspect.setBatchId("2017/07/12_0001_BH_BTCH_DENY");
    suspect.setName("MUHAMMADI GUL, MOHAMMAD MOSLIM HAQQANI");
    suspect.setOrigin("BOFE");
    suspect.setDesignation("AFGHANISTAN");
    suspect.setType(Type.INDIVIDUAL);
    suspect.setUserData1("46721");
    suspect.setOfficialRef("2014-03-21 15:10:26 BofE");
    suspect.setNationalId("1136");
    suspect.setBirthPlace("Gawargan village, Pul-e-Khumri District, Baghlan Province, Afghanistan");
    suspect.setBirthDate("1965");
    suspect.setAdditionalInfo(
        "List ID: 29 / Create Date: 10/06/2008 14:31:43 / Last Update Date: 06/18/2015 15:08:00 / "
            + "Program: AFGHANISTAN / Title: DEPUTY MINISTER OF HAJ AND RELIGIOUS AFFAIRS UNDER "
            + "THE TALIBAN REGIME; DEPUTY MINISTER OF HIGHER EDUCATION UNDER THE TALIBAN REGIME; "
            + "MAULAVI / Nationality: AFGHAN / NATIONAL NO: 1136 OtherInfo: Afghan, tazkira / "
            + "OtherInformation: UN Ref TI.H.73.01. Ethnic Pashtun from Baghlan Province. Believed "
            + "to be in Afghanistan/Pakistan border area. Speaks fluent English, Urdu and Arabic.; "
            + "NATIONAL NO: 1136 (Afghan, tazkira) / EffectiveDate: 3/21/2014 / OriginalID: 7169");
    suspect.setFmlType(1);
    suspect.setFmlPriority(0);
    suspect.setFmlConfidentiality(0);
    suspect.setPep(0);
    suspect.setFep(0);
    suspect.setTys(4);
    suspect.setIsn(-1);

    var noteMap = new HashMap<String, String>();
    noteMap.put("2008 14", "31:43");
    noteMap.put(
        "pakistan border area. speaks fluent english, urdu and arabic.; national no",
        "1136 (Afghan, tazkira)");
    noteMap.put("create date", "10");
    noteMap.put("national no", "1136 OtherInfo: Afghan, tazkira");
    noteMap.put("2015 15", "08:00");
    noteMap.put("list id", "29");
    noteMap.put("program", "AFGHANISTAN");
    noteMap.put(
        "title",
        "DEPUTY MINISTER OF HAJ AND RELIGIOUS AFFAIRS UNDER THE TALIBAN REGIME; DEPUTY MINISTER OF"
            + " HIGHER EDUCATION UNDER THE TALIBAN REGIME; MAULAVI");
    noteMap.put("effectivedate", "3");
    noteMap.put("last update date", "06");
    noteMap.put("nationality", "AFGHAN");
    noteMap.put(
        "otherinformation",
        "UN Ref TI.H.73.01. Ethnic Pashtun from Baghlan Province. Believed to be in Afghanistan");
    noteMap.put("originalid", "7169");
    suspect.setNotes(noteMap);

    suspect.getNameSynonyms().add(new Synonym("HAQQANI, MOSLIM", false));
    return suspect;
  }

  private Suspect createExpectedSuspect2() {
    Suspect suspect = new Suspect();
    suspect.setIndex(2);
    suspect.setOfacId("CL20012587");
    suspect.setMatch(0f);
    suspect.setTag(Tag.NAME);
    suspect.setMatchingText("ALI MERZA, BH,");
    suspect.setBatchId("2017/07/12_0001_BH_BTCH_DENY");
    suspect.setName("ALI, MIRZA MOHAMMED");
    suspect.setCountry("BAHRAIN");
    suspect.setOrigin("DENYCOUNTRY");
    suspect.setDesignation("BH-BM");
    suspect.setType(Type.INDIVIDUAL);
    suspect.setUserData1("ANTI-MONEY LAUNDERING Financial Others");
    suspect.setOfficialRef("12/19/2016");
    suspect.setNationalId("791007286");
    suspect.setAdditionalInfo(
        "This party was frozen by the Central Bank of Bahrain.Reference Number CD/36/2012 No new "
            + "account/services/transactions are allowed for this party. For escalation, please "
            + "refer to Bahrain FCC Team.");
    suspect.setFmlType(1);
    suspect.setFmlPriority(0);
    suspect.setFmlConfidentiality(0);
    suspect.setPep(0);
    suspect.setFep(0);
    suspect.setTys(1);
    suspect.setIsn(0);

    suspect.getCountrySynonyms().add(new Synonym("BAHREIN", false));
    return suspect;
  }

  private Suspect createExpectedSuspect3() {
    Suspect suspect = new Suspect();
    suspect.setIndex(3);
    suspect.setOfacId("AS00012844");
    suspect.setMatch(0f);
    suspect.setTag(Tag.NAME);
    suspect.setMatchingText("HASAN ALI,");
    suspect.setBatchId("2017/07/12_0001_BH_BTCH_DENY");
    suspect.setName("FADHIL, MUSTAFA MOHAMED");
    suspect.setOrigin("UST");
    suspect.setDesignation("SDGT");
    suspect.setType(Type.INDIVIDUAL);
    suspect.getSearchCodes().add("12773667");
    suspect.getSearchCodes().add("201735161");
    suspect.setUserData1("45708");
    suspect.setOfficialRef("2009-10-14 14:07:01 UST");
    suspect.setBirthPlace("CAIRO, EGYPT");
    suspect.setBirthDate("06/23/1976");
    suspect.setAdditionalInfo(
        "List ID: 0 / Create Date: 10/12/2001 00:00:00 / Last Update Date: 06/23/2016 11:01:52 / "
            + "Program: SDGT / Weak Alias: 'ANIS, ABU'; 'HUSSEIN'; 'KHALID'; 'YUSSRR, ABU' / "
            + "Citizenship: EGYPT; KENYA / KenyanIDNo: 12773667; SerialNo: 201735161 / "
            + "OriginalID: 6924");
    suspect.setFmlType(1);
    suspect.setFmlPriority(0);
    suspect.setFmlConfidentiality(0);
    suspect.setPep(0);
    suspect.setFep(0);
    suspect.setTys(1);
    suspect.setIsn(6);
    var noteMap = new HashMap<String, String>();
    noteMap.put("kenyanidno", "12773667; SerialNo: 201735161");
    noteMap.put("2016 11", "01:52");
    noteMap.put("create date", "10");
    noteMap.put("citizenship", "EGYPT; KENYA");
    noteMap.put("list id", "0");
    noteMap.put("2001 00", "00:00");
    noteMap.put("program", "SDGT");
    noteMap.put("originalid", "6924");
    noteMap.put("weak alias", "'ANIS, ABU'; 'HUSSEIN'; 'KHALID'; 'YUSSRR, ABU'");
    noteMap.put("last update date", "06");
    suspect.setNotes(noteMap);

    suspect.getNameSynonyms().add(new Synonym("MOHAMMED, MUSTAFA", false));
    suspect.getNameSynonyms().add(new Synonym("JIHAD, ABU", false));
    suspect.getNameSynonyms().add(new Synonym("FAZUL, MUSTAFA", false));
    suspect.getNameSynonyms().add(new Synonym("FADIL, MUSTAFA MUHAMAD", false));
    suspect.getNameSynonyms().add(new Synonym("ELBISHY, MOUSTAFA ALI", false));
    suspect.getNameSynonyms().add(new Synonym("ALI, HASSAN", true));
    suspect.getNameSynonyms().add(new Synonym("AL-NUBI, ABU", false));
    suspect.getNameSynonyms().add(new Synonym("AL MASRI, ABD AL WAKIL", false));
    return suspect;
  }

}
