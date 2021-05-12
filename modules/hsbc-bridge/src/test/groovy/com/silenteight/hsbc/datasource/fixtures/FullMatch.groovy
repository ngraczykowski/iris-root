package com.silenteight.hsbc.datasource.fixtures

import com.silenteight.hsbc.bridge.json.internal.model.CaseInformation
import com.silenteight.hsbc.bridge.json.internal.model.CustomerIndividual
import com.silenteight.hsbc.bridge.json.internal.model.PrivateListIndividual
import com.silenteight.hsbc.bridge.json.internal.model.WorldCheckIndividual
import com.silenteight.hsbc.datasource.datamodel.MatchData

trait FullMatch {

  static MatchData FULL_MATCH_1 = [
      getCaseInformation       : {new CaseInformation(id: 1)},
      getCustomerIndividual    : {
        new CustomerIndividual(
            gender: "M",
            identificationDocument1: "\"P\",\"ZS12398745\",\"\",\"\",\"PASSPORT\"",
            identificationDocument2: "###### THE PASSPORT POLICE OF IRAN",
            identificationDocument3: "Iran, Islamic Republic Of",
            identificationDocument4: "\"NID\",\"Y999999\",\"HK\",\"\",\"\"",
            identificationDocument5: "Passport",
            identificationDocument6: "<docno> Ref XIFEID memo dt 01Jan2016 for eID&VA details",
            identificationDocument7: "\"ID\",\"987456\"",
            identificationDocument8: "",
            identificationDocument9: null,
            residenceCountries: "Polska",
            addressCountry: "PL",
            countryOfResidence: "Iran",
            edqResidenceCountriesCode: "IRN",
            sourceCountry: "UK"
        )
      },
      getWorldCheckIndividuals : {
        [new WorldCheckIndividual(
            gender: "M",
            passportNumber: "KJ0114578 (VIET NAM);KJ4514578 (IRAN);A501245",
            idNumbers: "BC 78845 (UNK-UNKW)|ID78845 (UNK-UNKW)|78845ID (UNK-UNKW)",
            addressCountry: "PL",
            residencyCountry: "Polska"

        )]
      },
      getPrivateListIndividuals: {
        [new PrivateListIndividual(
            gender: "M",
            edqTaxNumber: "GOHA784512R12",
            passportNumber: "K45R78986,T3GD45689",
            nationalId: "4568795132,5465498756",
            edqDrivingLicence: "sadasdas76@hotmail.com",
            edqSuffix: "ID42342"
        )]
      }
  ] as MatchData

  static MatchData FULL_MATCH_2 = [
      getCaseInformation       : {new CaseInformation(id: 1)},
      getCustomerIndividual    : {new CustomerIndividual()},
      getWorldCheckIndividuals : {[]},
      getPrivateListIndividuals: {[]}
  ] as MatchData
}
