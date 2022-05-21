package com.silenteight.hsbc.datasource.fixtures

import com.silenteight.hsbc.bridge.json.internal.model.*
import com.silenteight.hsbc.datasource.datamodel.MatchData

trait FullMatch {

  static MatchData FULL_MATCH_1 = [
      getCaseInformation         : {new CaseInformation(id: 1)},
      getCustomerEntities        : {null},
      getCustomerIndividuals     : {
        [new CustomerIndividual(
            gender: "M",
            genderDerivedFlag: "N",
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
            sourceCountry: "UK",
            nationalityCitizenshipCountries: "UNITED KINGDOM",
            nationalityCountries: "GB",
            countryOfBirth: "GERMANY",
            countryOfBirthOriginal: "DE",
            edqBirthCountryCode: "DE",
            edqAddressCountryCode: "BY",
            edqCountriesAllCodes: "BY DE",
            countriesAll: "BELARUS;GERMANY"
        )]
      },
      getWorldCheckIndividuals   : {
        [new WorldCheckIndividual(
            gender: "M",
            genderDerivedFlag: "N",
            passportNumber: "KZ0212573 (VIET NAM);KW4444587 (GB);A52345 (IRAN)",
            passportCountry: "VNM GB IRN",
            idNumbers: "BC 78845 (UNK-UNKW)|ID78845 (UNK-UNKW)|78845ID (UNK-UNKW)",
            addressCountry: "PL",
            residencyCountry: "Polska",
            countryCodesAll: "BY DE RU",
            countriesAll: "BELARUS;GERMANY;RUSSIAN FEDERATION",
            countriesOriginal: "GERMANY"
        )]
      },
      getPrivateListIndividuals  : {
        [new PrivateListIndividual(
            gender: "M",
            genderDerivedFlag: "N",
            edqTaxNumber: "GOHA784512R12",
            passportNumber: "K45R78986,T3GD45689",
            nationalId: "4568795132,5465498756",
            countryOfBirth: "UNITED STATES",
            nationalities: "US",
            edqDrivingLicence: "sadasdas76@hotmail.com",
            edqSuffix: "ID42342",
            countryCodesAll: "IN PUNE",
            countriesAll: "INDIA;PUNE",
        )]
      },
      getCtrpScreeningIndividuals: {
        [new CtrpScreening(
            countryName: "IRAN, ISLAMIC REPUBLIC OF",
            countryCode: 'IR',
            ctrpValue: 'CHABAHAR'
        )]
      },
  ] as MatchData

  static MatchData FULL_MATCH_2 = [
      getCaseInformation       : {new CaseInformation(id: 1)},
      getCustomerEntities      : {null},
      getCustomerIndividuals   : {[new CustomerIndividual()]},
      getWorldCheckIndividuals : {[]},
      getPrivateListIndividuals: {[]}
  ] as MatchData

  static MatchData FULL_MATCH_3 = [
      getCaseInformation      : {new CaseInformation(id: 1)},
      getCustomerIndividuals  : {null},
      getCustomerEntities     : {
        [new CustomerEntity(
            tradesWithCountries: "",
            subsidiariesOperatesInCountries: "",
            countriesOfBusiness: "",
            countriesOfHeadOffice: "",
            addressCountry: "RUSSIAN FEDERATION",
            edqAddressCountryCode: "RU",
            edqBusinessCountries: "",
            countriesAll: "RUSSIAN FEDERATION",
            countriesAllCodes: "RU",
            sourceAddressCountry: "UA",
            edqTradesWithCountries: "",
            edqHeadOfficeCountries: "",
            operatingCountries: "RUSSIAN FEDERATION",
            edqOperatingCountriesCodes: ""
        )]
      },
      getWorldCheckEntities   : {
        [new WorldCheckEntity(
            addressCountry: "RU",
            operatingCountries: "RUSSIAN FEDERATION",
            countryCodesAll: "RU",
            countriesAll: "RUSSIAN FEDERATION",
            nativeAliasLanguageCountry: ""
        )]
      },
      getPrivateListEntities  : {
        [new PrivateListEntity(
            addressCountry: "UNKNOWN",
            operatingCountries: "UNITED STATES",
            countryCodesAll: "US",
            countriesAll: "UNITED STATES"
        )]
      },
      getCtrpScreeningEntities: {
        [new CtrpScreening(
            countryName: "IRAN, ISLAMIC REPUBLIC OF",
            countryCode: 'IR',
            ctrpValue: 'CHABAHAR'
        )]
      },
  ] as MatchData
}
