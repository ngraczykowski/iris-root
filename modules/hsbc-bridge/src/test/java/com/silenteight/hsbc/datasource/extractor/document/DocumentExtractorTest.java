package com.silenteight.hsbc.datasource.extractor.document;

import com.silenteight.hsbc.bridge.json.internal.model.CustomerIndividual;
import com.silenteight.hsbc.bridge.json.internal.model.PrivateListIndividual;
import com.silenteight.hsbc.bridge.json.internal.model.WorldCheckIndividual;
import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.extractors.document.DocumentExtractor;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Ignore
@ExtendWith(MockitoExtension.class)
class DocumentExtractorTest {

  @Mock
  private MatchData matchData;

  private CustomerIndividual customerIndividuals;
  private WorldCheckIndividual worldCheckIndividuals;
  private PrivateListIndividual privateListIndividuals;

  @BeforeEach
  void setUp() {
    customerIndividuals = new CustomerIndividual();
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

    worldCheckIndividuals = new WorldCheckIndividual();
    var worldCheckIndividuals = this.worldCheckIndividuals;
    worldCheckIndividuals.setPassportNumber("KJ0114578 (VIET NAM);KJ4514578 (IRAN);A501245");
    worldCheckIndividuals.setIdNumbers("BC 78845 (UNK-UNKW)|ID78845 (UNK-UNKW)|78845ID (UNK-UNKW)");

    privateListIndividuals = new PrivateListIndividual();
    var privateListIndividuals = this.privateListIndividuals;
    privateListIndividuals.setEdqTaxNumber("GOHA784512R12");
    privateListIndividuals.setPassportNumber("K45R78986,T3GD45689");
    privateListIndividuals.setNationalId("4568795132,5465498756");
    privateListIndividuals.setEdqDrivingLicence("sadasdas76@hotmail.com");
    privateListIndividuals.setEdqSuffix("ID42342");
  }

  @Test
  void shouldExtractAlertedPartyDocumentNumbers() {
    //given
    var nationalIdFeatureConverter = new DocumentExtractor();

    //when
    var document =
        nationalIdFeatureConverter.convertAlertedPartyDocumentNumbers(customerIndividuals);

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
    var nationalIdFeatureConverter = new DocumentExtractor();
    given(matchData.getWorldCheckIndividuals()).willReturn(List.of(worldCheckIndividuals));
    given(matchData.getPrivateListIndividuals()).willReturn(List.of(privateListIndividuals));

    //when
    when(matchData.hasPrivateListIndividuals()).thenReturn(true);
    when(matchData.hasWorldCheckIndividuals()).thenReturn(true);
    var document =
        nationalIdFeatureConverter.convertMatchedPartyDocumentNumbers(matchData);

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
