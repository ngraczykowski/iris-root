package com.silenteight.hsbc.datasource.feature.converter;

import com.silenteight.hsbc.bridge.domain.CustomerIndividuals;
import com.silenteight.hsbc.bridge.domain.IndividualComposite;
import com.silenteight.hsbc.bridge.domain.PrivateListIndividuals;
import com.silenteight.hsbc.bridge.domain.WorldCheckIndividuals;
import com.silenteight.hsbc.bridge.match.MatchRawData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class NationalIdFeatureConverterTest {

  private MatchRawData matchRawData;

  @BeforeEach
  void setUp() {
    matchRawData = new MatchRawData();

    var customerIndividuals = new CustomerIndividuals();

    customerIndividuals.setIdentificationDocument1("\"P\",\"ZS12398745\",\"\",\"\",\"PASSPORT\"");
    customerIndividuals.setIdentificationDocument2("###### THE PASSPORT POLICE OF IRAN");
    customerIndividuals.setIdentificationDocument3("Iran, Islamic Republic Of");
    customerIndividuals.setIdentificationDocument4("\"NID\",\"Y999999\",\"HK\",\"\",\"\"");
    customerIndividuals.setIdentificationDocument5("Passport");
    customerIndividuals.setIdentificationDocument6(
        "<docno> Ref XIFEID memo dt 01Jan2016 for eID&VA details");
    customerIndividuals.setIdentificationDocument7("\"ID\",\"987456\"");
    customerIndividuals.setIdentificationDocument8("");
    customerIndividuals.setIdentificationDocument9(null);

    var worldCheckIndividuals = new WorldCheckIndividuals();
    worldCheckIndividuals.setPassportNumber("KJ0114578 (VIET NAM);KJ4514578 (IRAN);A501245");
    worldCheckIndividuals.setIdNumbers("BC 78845 (UNK-UNKW)|ID78845 (UNK-UNKW)|78845ID (UNK-UNKW)");

    var privateListIndividuals = new PrivateListIndividuals();
    privateListIndividuals.setEdqTaxNumber("GOHA784512R12");
    privateListIndividuals.setPassportNumber("K45R78986,T3GD45689");
    privateListIndividuals.setNationalId("4568795132,5465498756");
    privateListIndividuals.setEdqDrivingLicence("sadasdas76@hotmail.com");
    privateListIndividuals.setEdqSuffix("ID42342");

    var individualComposite = new IndividualComposite(
        customerIndividuals,
        List.of(worldCheckIndividuals),
        List.of(privateListIndividuals),
        Collections.emptyList());

    matchRawData.setIndividualComposite(individualComposite);
  }

  @Test
  void shouldExtractAlertedPartyDocumentNumbers() {
    //given
    var nationalIdFeatureConverter = new NationalIdFeatureConverter();

    //when
    var document =
        nationalIdFeatureConverter.convertAlertedPartyDocumentNumbers(
            matchRawData.getIndividualComposite().getCustomerIndividuals());

    //then
    assertThat(document.getPassportNumbers()).containsOnlyOnceElementsOf(
        List.of("ZS12398745"));
    assertThat(document.getNationalIdNumbers()).containsOnlyOnceElementsOf(
        List.of("Y999999"));
    assertThat(document.getOtherDocumentNumbers()).containsOnlyOnceElementsOf(
        List.of("987456"));
  }

  @Test
  void shouldExtractMatchedPartyDocumentNumbers() {
    //given
    var nationalIdFeatureConverter = new NationalIdFeatureConverter();

    //when
    var document =
        nationalIdFeatureConverter.convertMatchedPartyDocumentNumbers(
            matchRawData.getIndividualComposite());

    System.out.println(document);
    //then
    assertThat(document.getPassportNumbers()).containsOnlyOnceElementsOf(
        List.of("KJ0114578", "KJ4514578", "A501245", "K45R78986", "T3GD45689"));
    assertThat(document.getNationalIdNumbers()).containsOnlyOnceElementsOf(
        List.of("78845ID", "4568795132", "5465498756"));
    assertThat(document.getOtherDocumentNumbers()).containsOnlyOnceElementsOf(
        List.of("BC 78845", "ID78845", "GOHA784512R12", "sadasdas76@hotmail.com",
            "ID42342"));
    assertThat(document.getAllCountries())
        .contains("VIET NAM", "IRAN", "UNK UNKW");
  }

}
