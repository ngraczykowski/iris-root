package com.silenteight.hsbc.datasource.feature.name

import com.silenteight.hsbc.datasource.datamodel.*
import com.silenteight.hsbc.datasource.dto.name.EntityType
import com.silenteight.hsbc.datasource.dto.name.ForeignAliasDto
import com.silenteight.hsbc.datasource.dto.name.GetNameInformationRequestDto
import com.silenteight.hsbc.datasource.dto.name.GetNameInformationResponseDto
import com.silenteight.hsbc.datasource.dto.name.WatchlistNameDto.NameType
import com.silenteight.hsbc.datasource.extractors.name.NameInformationServiceClient
import com.silenteight.hsbc.datasource.extractors.name.NameQueryConfigurer
import com.silenteight.hsbc.datasource.feature.Feature

import spock.lang.Specification

class NameFeatureSpec extends Specification {

  def client = Mock(NameInformationServiceClient)

  def nameQueryConfigurer = new NameQueryConfigurer().create()

  def underTest = new NameFeature(nameQueryConfigurer)

  def 'should retrieve name values when customer is individual'() {
    given:
    def getNameInformationResponseDto = GetNameInformationResponseDto.builder()
        .foreignAliases(createForeignAliases())
        .build()

    def getNameInformationRequestDto = GetNameInformationRequestDto.builder()
        .watchlistUuid('12')
        .build()

    def customerIndividual = Mock(CustomerIndividual) {
      getProfileFullName() >> ''
      getGivenName() >> 'ALAKSANDR LEANIDAVICH'
      getMiddleName() >> ''
      getFamilyNameOriginal() >> 'ZIMOUSKI'
      getFullNameDerived() >> 'AKIAKSANDR LEANIDAVICH ZIMOUSKI'
      getOriginalScriptName() >> ''
    }

    def worldCheckIndividual = Mock(WorldCheckIndividual) {
      getListRecordId() >> '12'
      getPrimaryName() >> 'Aleksander ZIMOWSKI'
      getFullNameOriginal() >> 'Aleksandr Leonidovich ZIMOWSKI'
      getFullNameDerived() >> 'Aleksandr Leonidovich ZIMOWSKI'
      getOriginalScriptName() >> ''
      getGivenNamesOriginal() >> 'Akiaksandr Leanidavich'
      getFamilyNameOriginal() >> 'ZIMOWSKI'
      getFullNameOriginal() >> 'Akiaksandr Leanidavich ZIMOUSKI'
      getCountryOfBirth() >> 'GERMANY'
      getNationalities() >> 'DE'
      getPassportCountry() >> ''
      getNativeAliasLanguageCountry() >> ''
      getAddressCountry() >> 'BY RU'
      getResidencyCountry() >> 'BY RU'
      getCountryCodesAll() >> 'BY DE RU'
      getCountriesAll() >> 'BELARUS;GERMANY;RUSSIAN FEDERATION'
      getCountriesOriginal() >> 'GERMANY'
    }

    def privateListIndividual = Mock(PrivateListIndividual) {
      getPrimaryName() >> ''
      getFullNameOriginal() >> 'Aleksandr Leonidovich ZIMOWSKI'
      getGivenNamesOriginal() >> 'Akiaksandr Leanidavich'
      getFamilyNameOriginal() >> 'ZIMOUSKI'
      getFullNameDerived() >> 'AKIAKSANDR LEANIDAVICH ZIMOUSKI'
    }

    def matchData = Mock(MatchData) {
      isIndividual() >> true
      getCustomerIndividuals() >> [customerIndividual]
      getWorldCheckIndividuals() >> [worldCheckIndividual]
      hasWorldCheckIndividuals() >> true
      getPrivateListIndividuals() >> [privateListIndividual]
      hasPrivateListIndividuals() >> true
      getNnsIndividuals() >> []
    }

    when:
    def result = underTest.retrieve(matchData, client)

    then:
    1 * client.getNameInformation(getNameInformationRequestDto) >>
        Optional.of(getNameInformationResponseDto)

    with(result) {
      feature == Feature.NAME.fullName
      alertedPartyNames.size() == 2
      with(alertedPartyNames.name) {
        containsAll(['ALAKSANDR LEANIDAVICH ZIMOUSKI', 'AKIAKSANDR LEANIDAVICH ZIMOUSKI'])
      }
      watchlistNames.size() == 5
      with(watchlistNames.name) {
        containsAll(
            ['Aleksander ZIMOWSKI',
             'Aleksandr Leonidovich ZIMOWSKI',
             'AKIAKSANDR LEANIDAVICH ZIMOUSKI',
             'Akiaksandr Leanidavich ZIMOWSKI',
             'Akiaksandr Leanidavich ZIMOUSKI'])
      }
      with(watchlistNames.type) {
        containsAll(
            [NameType.REGULAR, NameType.REGULAR, NameType.REGULAR,
             NameType.ALIAS, NameType.ALIAS])
      }
      alertedPartyType == EntityType.INDIVIDUAL
      matchingTexts == []
    }
  }

  def 'should retrieve name values when match is individual Negative News Screening'() {
    given:
    def getNameInformationResponseDto = GetNameInformationResponseDto.builder()
        .foreignAliases(createForeignAliases())
        .build()

    def getNameInformationRequestDto = GetNameInformationRequestDto.builder()
        .watchlistUuid('12')
        .build()

    def customerIndividual = Mock(CustomerIndividual) {
      getProfileFullName() >> ''
      getGivenName() >> 'ALAKSANDR LEANIDAVICH'
      getMiddleName() >> ''
      getFamilyNameOriginal() >> 'ZIMOUSKI'
      getFullNameDerived() >> 'AKIAKSANDR LEANIDAVICH ZIMOUSKI'
      getOriginalScriptName() >> ''
    }

    def nnsIndividuals = Mock(NegativeNewsScreeningIndividuals) {
      getListRecordId() >> '12'
      getPrimaryName() >> 'Aleksander ZIMOWSKI'
      getOriginalFullName() >> 'Aleksandr Leonidovich ZIMOWSKI'
      getDerivedFullName() >> 'Aleksandr Leonidovich ZIMOWSKI'
      getOriginalScriptName() >> ''
      getOriginalGivenNames() >> 'Akiaksandr Leanidavich'
      getOriginalFamilyName() >> 'ZIMOWSKI'
      getOriginalFullName() >> 'Akiaksandr Leanidavich ZIMOUSKI'
      getCountryOfBirth() >> 'GERMANY'
      getNationalities() >> 'DE'
      getPassportCountry() >> ''
      getNativeAliasLanguageCountry() >> ''
      getAddressCountry() >> 'BY RU'
      getResidenceCountry() >> 'BY RU'
      getAllCountryCodes() >> 'BY DE RU'
      getAllCountries() >> 'BELARUS;GERMANY;RUSSIAN FEDERATION'
      getOriginalCountries() >> 'GERMANY'
    }

    def matchData = Mock(MatchData) {
      isIndividual() >> true
      getCustomerIndividuals() >> [customerIndividual]
      getWorldCheckIndividuals() >> []
      hasWorldCheckIndividuals() >> true
      getPrivateListIndividuals() >> []
      hasPrivateListIndividuals() >> true
      getNnsIndividuals() >> [nnsIndividuals]
    }

    when:
    def result = underTest.retrieve(matchData, client)

    then:
    1 * client.getNameInformation(getNameInformationRequestDto) >>
        Optional.of(getNameInformationResponseDto)

    with(result) {
      feature == Feature.NAME.fullName
      alertedPartyNames.size() == 2
      with(alertedPartyNames.name) {
        containsAll(['ALAKSANDR LEANIDAVICH ZIMOUSKI', 'AKIAKSANDR LEANIDAVICH ZIMOUSKI'])
      }
      watchlistNames.size() == 2
      with(watchlistNames.name) {
        containsAll(
            ['Aleksander ZIMOWSKI',
             'Aleksandr Leonidovich ZIMOWSKI'])
      }
      with(watchlistNames.type) {
        containsAll(
            [NameType.REGULAR, NameType.REGULAR])
      }
      alertedPartyType == EntityType.INDIVIDUAL
      matchingTexts == []
    }
  }

  def 'should retrieve name values when customer is entity'() {
    given:
    def getNameInformationResponseDto = GetNameInformationResponseDto.builder()
        .foreignAliases(createForeignAliases())
        .build()

    def getNameInformationRequestDto = GetNameInformationRequestDto.builder()
        .watchlistUuid('12')
        .build()

    def customerEntity = Mock(CustomerEntity) {
      getEntityNameOriginal() >>
          'AKTSIONERNOE OBSHCHESTVO KORPORATSIIA AVIAKOSMICHESKOE OBORUDOVANIE'
      getEntityName() >>
          'AKTSIONERNOE OBSHCHESTVO KORPORATSIIA AVIAKOSMICHESKOE OBORUDOVANIE'
      getOriginalScriptName() >> ''
    }

    def worldCheckEntity = Mock(WorldCheckEntity) {
      getListRecordId() >> '12'
      getPrimaryName() >> 'AKTSIONERNOER'
      getEntityNameOriginal() >> 'AKTSIONERNOE'
      getEntityNameDerived() >> 'AKTSIONERNOE'
      getOriginalScriptName() >> ''
      getCountryCodesAll() >> 'RU'
      getCountriesAll() >> 'RUSSIAN FEDERATION'
      getRegistrationCountry() >> 'RUSSIAN FEDERATION'
      getAddressCountry() >> 'RU'
      getOperatingCountries() >> 'RUSSIAN FEDERATION'
      getCountryCodesAll() >> 'RU'
      getCountriesAll() >> 'RUSSIAN FEDERATION'
      getNativeAliasLanguageCountry() >> ''
    }

    def privateListEntity = Mock(PrivateListEntity) {
      getEntityNameOriginal() >>
          'AKTSIONERNOE OBSHCHESTVO KORPORATSIIA AVIAKOSMICHESKOE OBORUDOVANIE'
      getEntityNameDerived() >>
          'AKTSIONERNOE OBSHCHESTVO KORPORATSIIA AVIAKOSMICHESKOE OBORUDOVANIE'
    }

    def matchData = Mock(MatchData) {
      isEntity() >> true
      getCustomerEntities() >> [customerEntity]
      getWorldCheckEntities() >> [worldCheckEntity]
      hasWorldCheckEntities() >> true
      getPrivateListEntities() >> [privateListEntity]
      hasPrivateListEntities() >> true
      getNnsEntities() >> []
    }

    when:
    def result = underTest.retrieve(matchData, client)

    then:
    1 * client.getNameInformation(getNameInformationRequestDto) >>
        Optional.of(getNameInformationResponseDto)

    with(result) {
      feature == Feature.NAME.fullName
      alertedPartyNames.size() == 1
      with(alertedPartyNames.name) {
        containsAll(['AKTSIONERNOE OBSHCHESTVO KORPORATSIIA AVIAKOSMICHESKOE OBORUDOVANIE'])
      }
      watchlistNames.size() == 3
      with(watchlistNames.name) {
        containsAll(
            ['AKTSIONERNOER',
             'AKTSIONERNOE',
             'AKTSIONERNOE OBSHCHESTVO KORPORATSIIA AVIAKOSMICHESKOE OBORUDOVANIE'])
      }
      with(watchlistNames.type) {
        containsAll([NameType.REGULAR, NameType.REGULAR, NameType.REGULAR])
      }
      alertedPartyType == EntityType.ORGANIZATION
      matchingTexts == []
    }
  }

  def 'should retrieve name values when match is entity Negative News Screening'() {
    given:
    def getNameInformationResponseDto = GetNameInformationResponseDto.builder()
        .foreignAliases(createForeignAliases())
        .build()

    def getNameInformationRequestDto = GetNameInformationRequestDto.builder()
        .watchlistUuid('12')
        .build()

    def customerEntity = Mock(CustomerEntity) {
      getEntityNameOriginal() >>
          'AKTSIONERNOE OBSHCHESTVO KORPORATSIIA AVIAKOSMICHESKOE OBORUDOVANIE'
      getEntityName() >>
          'AKTSIONERNOE OBSHCHESTVO KORPORATSIIA AVIAKOSMICHESKOE OBORUDOVANIE'
      getOriginalScriptName() >> ''
    }

    def nnsEntity = Mock(NegativeNewsScreeningEntities) {
      getListRecordId() >> '12'
      getPrimaryName() >> 'AKTSIONERNOER'
      getOriginalEntityName() >> 'AKTSIONERNOE'
      getDerivedEntityName() >> 'AKTSIONERNOE'
      getOriginalScriptName() >> ''
      getAllCountryCodes() >> 'RU'
      getAllCountries() >> 'RUSSIAN FEDERATION'
      getRegistrationCountry() >> 'RUSSIAN FEDERATION'
      getAddressCountry() >> 'RU'
      getOperatingCountries() >> 'RUSSIAN FEDERATION'
      getAllCountryCodes() >> 'RU'
      getAllCountries() >> 'RUSSIAN FEDERATION'
      getNativeAliasLanguageCountry() >> ''
    }

    def matchData = Mock(MatchData) {
      isEntity() >> true
      getCustomerEntities() >> [customerEntity]
      getWorldCheckEntities() >> []
      hasWorldCheckEntities() >> true
      getPrivateListEntities() >> []
      hasPrivateListEntities() >> true
      getNnsEntities() >> [nnsEntity]
    }

    when:
    def result = underTest.retrieve(matchData, client)

    then:
    1 * client.getNameInformation(getNameInformationRequestDto) >>
        Optional.of(getNameInformationResponseDto)

    with(result) {
      feature == Feature.NAME.fullName
      alertedPartyNames.size() == 1
      with(alertedPartyNames.name) {
        containsAll(['AKTSIONERNOE OBSHCHESTVO KORPORATSIIA AVIAKOSMICHESKOE OBORUDOVANIE'])
      }
      watchlistNames.size() == 2
      with(watchlistNames.name) {
        containsAll(
            ['AKTSIONERNOER',
             'AKTSIONERNOE'])
      }
      with(watchlistNames.type) {
        containsAll([NameType.REGULAR, NameType.REGULAR])
      }
      alertedPartyType == EntityType.ORGANIZATION
      matchingTexts == []
    }
  }

  private static createForeignAliases() {
    def chineseForeignAlias = new ForeignAliasDto('李显龙', 'zh-CN')
    def russianForeignAlias = new ForeignAliasDto('Влади́мир Влади́мирович Пу́тин', 'ru')
    [chineseForeignAlias, russianForeignAlias]
  }
}
