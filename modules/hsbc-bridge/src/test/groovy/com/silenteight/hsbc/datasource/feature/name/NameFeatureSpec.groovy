package com.silenteight.hsbc.datasource.feature.name

import com.silenteight.hsbc.datasource.datamodel.*
import com.silenteight.hsbc.datasource.dto.name.EntityType
import com.silenteight.hsbc.datasource.dto.name.WatchlistNameDto.NameType
import com.silenteight.hsbc.datasource.extractors.name.*
import com.silenteight.hsbc.datasource.feature.Feature

import spock.lang.Specification

class NameFeatureSpec extends Specification {

  def client = Mock(NameInformationServiceClient)

  def nameQueryConfigurer = new NameQueryConfigurer().create()

  def underTest = new NameFeature(nameQueryConfigurer)

  def 'should retrieve name values when customer is individual'() {
    given:
    def getNameInformationResponseDto = GetNameInformationResponseDto.builder()
        .firstName('ALAKSANDR')
        .lastName('ZIMOUSKI')
        .aliases(['AZ'])
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
      watchlistNames.size() == 4
      with(watchlistNames.name) {
        containsAll(
            ['Aleksandr Leonidovich ZIMOWSKI',
             'Akiaksandr Leanidavich ZIMOWSKI',
             'ALAKSANDR ZIMOUSKI',
             'Akiaksandr Leanidavich ZIMOUSKI'])
      }
      with(watchlistNames.type) {
        containsAll([NameType.ALIAS, NameType.REGULAR, NameType.REGULAR, NameType.REGULAR])
      }
      alertedPartyType == EntityType.INDIVIDUAL
      matchingTexts == []
    }
  }

  def 'should retrieve name values when customer is entity'() {
    given:
    def getNameInformationResponseDto = GetNameInformationResponseDto.builder()
        .lastName('DONALD INDUSTRIES GROUP')
        .aliases(['DI', 'DIG', 'Koncern Donald Industries Group'])
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
            ['AKTSIONERNOE',
             'DONALD INDUSTRIES GROUP',
             'AKTSIONERNOE OBSHCHESTVO KORPORATSIIA AVIAKOSMICHESKOE OBORUDOVANIE AKTSIONERNOE OBSHCHESTVO KORPORATSIIA AVIAKOSMICHESKOE OBORUDOVANIE'])
      }
      with(watchlistNames.type) {
        containsAll([NameType.REGULAR, NameType.REGULAR, NameType.ALIAS])
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
