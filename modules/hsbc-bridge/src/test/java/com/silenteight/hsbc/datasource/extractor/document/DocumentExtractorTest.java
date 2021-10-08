package com.silenteight.hsbc.datasource.extractor.document;

import com.silenteight.hsbc.bridge.json.internal.model.CustomerIndividual;
import com.silenteight.hsbc.bridge.json.internal.model.PrivateListIndividual;
import com.silenteight.hsbc.bridge.json.internal.model.WorldCheckIndividual;
import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.extractors.document.DocumentExtractor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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
    customerIndividuals.setIdentificationDocument7("\"ID\",\"K987456 NumBeR\"");
    customerIndividuals.setIdentificationDocument8("");
    customerIndividuals.setIdentificationDocument9(null);

    worldCheckIndividuals = new WorldCheckIndividual();
    var worldCheckIndividuals = this.worldCheckIndividuals;
    worldCheckIndividuals.setPassportNumber("KZ0212573 (VIET NAM);KW4444587 (IRAN);A52345");
    worldCheckIndividuals.setPassportCountry("PH PH PH");
    worldCheckIndividuals.setIdNumbers("BC 78845 (UNK-UNKW)|ID78845 (UNK-UNKW)|78845ID (UNK-UNKW)");

    privateListIndividuals = new PrivateListIndividual();
    var privateListIndividuals = this.privateListIndividuals;
    privateListIndividuals.setEdqTaxNumber("GOHA784512R12");
    privateListIndividuals.setPassportNumber(
        "K45R78986I (PL);A4671286I (GER);B1232353456");
    privateListIndividuals.setNationalId("4568795132,5465498756,,,");
    privateListIndividuals.setEdqDrivingLicence("sadasdas76@hotmail.com");
    privateListIndividuals.setEdqSuffix("SUFFIX-A");
  }

  @Test
  void shouldExtractAlertedPartyDocumentNumbers() {
    //given
    var nationalIdFeatureConverter = new DocumentExtractor();

    //when
    var document =
        nationalIdFeatureConverter.convertAlertedPartyDocumentNumbers(of(customerIndividuals));

    //then
    assertThat(document.getPassportNumbers()).containsOnlyOnceElementsOf(of("ZS12398745"));
    assertThat(document.getNationalIds()).containsOnlyOnceElementsOf(of("Y999999"));
    assertThat(document.getOtherDocuments()).containsOnlyOnceElementsOf(of("K987456 NumBeR"));
  }

  @Test
  void shouldExtractMatchedPartyDocumentNumbers() {
    //given
    var nationalIdFeatureConverter = new DocumentExtractor();
    given(matchData.getWorldCheckIndividuals()).willReturn(of(worldCheckIndividuals));
    given(matchData.getPrivateListIndividuals()).willReturn(of(privateListIndividuals));

    //when
    when(matchData.hasPrivateListIndividuals()).thenReturn(true);
    when(matchData.hasWorldCheckIndividuals()).thenReturn(true);
    var document = nationalIdFeatureConverter.convertMatchedPartyDocumentNumbers(matchData);

    //then
    assertThat(document.getPassportNumbers()).containsOnlyOnceElementsOf(
        of("KZ0212573", "KW4444587", "K45R78986I", "A4671286I", "SUFFIX-A"));
    assertThat(document.getNationalIds()).containsOnlyOnceElementsOf(
        of("4568795132", "5465498756", "78845ID", "SUFFIX-A"));
    assertThat(document.getOtherDocuments()).containsOnlyOnceElementsOf(
        of("BC 78845", "ID78845", "GOHA784512R12", "sadasdas76@hotmail.com", "SUFFIX-A"));
    assertThat(document.getAllCountries()).contains("VIET NAM", "IRAN", "UNK UNKW");
  }
}
