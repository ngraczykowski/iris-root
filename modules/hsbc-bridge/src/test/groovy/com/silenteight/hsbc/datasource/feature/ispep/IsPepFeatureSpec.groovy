package com.silenteight.hsbc.datasource.feature.ispep

import com.silenteight.hsbc.datasource.datamodel.*
import com.silenteight.hsbc.datasource.extractors.ispep.IsPepQueryConfigurer
import com.silenteight.hsbc.datasource.feature.Feature

import spock.lang.Specification

class IsPepFeatureSpec extends Specification {

  def isPepQueryConfigurer = new IsPepQueryConfigurer().create()

  def underTest = new IsPepFeature(isPepQueryConfigurer)

  def 'should retrieve isPep values when customer is individual'() {
    given:
    def matchName = "alerts/f20b466a-8bc7-4703-9dc3-48bea4d97872/matches/e6b4ebd0-e901-4671-9969-a61bf537ca75"

    def customerIndividual = Mock(CustomerIndividual) {
      getEdqLobCountryCode() >> 'SG'
    }

    def worldCheckIndividual = Mock(WorldCheckIndividual) {
      getListRecordId() >> '376829'
      getLinkedTo() >> '28966;376830;376831;376832;448756;80217'
      getFurtherInformation() >> Fixtures.FURTHER_INFORMATION
      getCountryCodesAll() >> 'PL US RU'
    }

    def matchData = Mock(MatchData) {
      isIndividual() >> true
      getWatchlistId() >> Optional.of('376829')
      getWatchlistType() >> Optional.of(WatchlistType.WORLDCHECK_INDIVIDUALS)
      getCustomerIndividuals() >> [customerIndividual]
      getWorldCheckIndividuals() >> [worldCheckIndividual]
      hasWorldCheckIndividuals() >> true
    }

    when:
    def result = underTest.retrieve(matchData, matchName)

    then:
    with(result) {
      match == matchName
      isPepFeatureInput.feature == Feature.IS_PEP.fullName
      isPepFeatureInput.watchListItem.id == '376829'
      isPepFeatureInput.watchListItem.type == 'WorldCheckIndividuals'
      isPepFeatureInput.watchListItem.furtherInformation == Fixtures.FURTHER_INFORMATION
      isPepFeatureInput.watchListItem.linkedPepsUids == ['28966;376830;376831;376832;448756;80217']
      isPepFeatureInput.watchListItem.countries == ['PL', 'US', 'RU']
      isPepFeatureInput.alertedPartyItem.country == 'SG'
    }
  }

  def 'should retrieve isPep values when customer is entity'() {
    given:
    def matchName = "alerts/f20b466a-8bc7-4703-9dc3-48bea4d97872/matches/e6b4ebd0-e901-4671-9969-a61bf537ca75"

    def customerEntity = Mock(CustomerEntity) {
      getEdqLobCountryCode() >> 'SG'
    }

    def worldCheckEntity = Mock(WorldCheckEntity) {
      getListRecordId() >> '376829'
      getLinkedTo() >> '28966;376830;376831;376832;448756;80217'
      getFurtherInformation() >> Fixtures.FURTHER_INFORMATION
      getCountryCodesAll() >> 'PL US RU'
    }

    def matchData = Mock(MatchData) {
      isEntity() >> true
      getWatchlistId() >> Optional.of('376829')
      getWatchlistType() >> Optional.of(WatchlistType.WORLDCHECK_ENTITIES)
      getCustomerEntities() >> [customerEntity]
      getWorldCheckEntities() >> [worldCheckEntity]
      hasWorldCheckEntities() >> true
    }

    when:
    def result = underTest.retrieve(matchData, matchName)

    then:
    with(result) {
      match == matchName
      isPepFeatureInput.feature == Feature.IS_PEP.fullName
      isPepFeatureInput.watchListItem.id == '376829'
      isPepFeatureInput.watchListItem.type == 'WorldCheckEntities'
      isPepFeatureInput.watchListItem.furtherInformation == Fixtures.FURTHER_INFORMATION
      isPepFeatureInput.watchListItem.linkedPepsUids == ['28966;376830;376831;376832;448756;80217']
      isPepFeatureInput.watchListItem.countries == ['PL', 'US', 'RU']
      isPepFeatureInput.alertedPartyItem.country == 'SG'
    }
  }

  def 'should not retrieve countries, furtherInformation and linkedPepsUids when watchlist type is different than WorldCheck'() {
    def matchName = "alerts/f20b466a-8bc7-4703-9dc3-48bea4d97872/matches/e6b4ebd0-e901-4671-9969-a61bf537ca75"

    def customerEntity = Mock(CustomerEntity) {
      getEdqLobCountryCode() >> 'SG'
    }

    def privateListEntity = Mock(PrivateListEntity) {
      getListRecordId() >> '376829'
    }

    def matchData = Mock(MatchData) {
      isEntity() >> true
      getWatchlistId() >> Optional.of('376829')
      getWatchlistType() >> Optional.of(WatchlistType.PRIVATE_LIST_ENTITIES)
      getCustomerEntities() >> [customerEntity]
      getPrivateListEntities() >> [privateListEntity]
      hasPrivateListEntities() >> true
    }

    when:
    def result = underTest.retrieve(matchData, matchName)

    then:
    with(result) {
      match == matchName
      isPepFeatureInput.feature == Feature.IS_PEP.fullName
      isPepFeatureInput.watchListItem.id == '376829'
      isPepFeatureInput.watchListItem.type == 'PrivateListEntities'
      isPepFeatureInput.watchListItem.furtherInformation == ''
      isPepFeatureInput.watchListItem.linkedPepsUids == []
      isPepFeatureInput.alertedPartyItem.country == 'SG'
    }
  }

  class Fixtures {

    static def FURTHER_INFORMATION = '"[EU SANCTIONS] EC 765/2006. 2006/276/CFSP (Feb 2016 - removed). 2004/661/CFSP (repealed). ' +
        'PRIMARY NAME: Zimouski Aliaksandr Leanidavich. Zimovski, Aleksandr Leonidovich.' +
        ' Identifying Information: DOB: 10.1.1961. POB: Germany (GDR) ID: 3100161A078PB5. Reasons for listing:' +
        ' Media adviser and former President of the State Radio-television company.' +
        ' Former Member of the Upper House of the Parliament. He was the main actor of the regime\'s ' +
        'propaganda until December 2010, by systematically denigrating the opposition and justifying gross violations ' +
        'of human rights and recurrent crackdowns on the opposition and on civil society in Belarus. On 29 December 2012,' +
        ' he admitted that he was recruited to lead an information war and he proudly said that he succeded in ' +
        'leading it to victory and that he has not changed his views. [SANCTIONS NOTE] EU provides for the suspension ' +
        'of the restrictive measures imposed on certain persons and entities until 29 February 2016.' +
        '[SWITZERLAND SANCTIONS - SECO] SSID: 20-4443 (Jun 2006 - addition. Mar 2016 - removed). PRIMARY NAME:' +
        ' Zimouski Aliaksandr Leanidavich Spelling variant: a) Zimovski Aleksandr Leonidovich (Russian) DOB: 10 ' +
        'Jan 1961 POB: Germany Identification document: ID card No. 3100161A078PB5, Belarus Justification: Media ' +
        'adviser and former President of the State Radio-television company. Former Member of the Upper House of the' +
        ' Parliament. He was the main actor of the regime\'s propaganda until December 2010, by systematically ' +
        'denigrating the opposition and justifying gross violations of human rights and recurrent crackdowns on the' +
        ' opposition and on civil society in Belarus. On 29 Dec 2012, he admitted that he was recruited to lead an' +
        ' information war and he proudly said that he succeeded in leading it to victory and that he has not changed ' +
        'his views. Other information: a) POB: Gernamy (GDR) b) Travel ban according to article 3 paragraph 1 and ' +
        'financial sanctions according to article 1 do not apply until 15 March 2016. [UK SANCTIONS - UKHMT] ' +
        'May 2006 - addition. Nov 2015 - removed. PRIMARY NAME: ZIMOUSKI,ALIAKSANDR LEANIDAVICH. DOB: 10/01/1961. POB:' +
        ' Germany a.k.a: ZIMOVSKI, Aleksandr , Leonidovich National Identification no: 3100161A078PB5 Other Information:' +
        ' Media advisor and former President of the State Radio-television company. Group ID: 8878. [SANCTIONS NOTE]' +
        ' UKHMT provides for the suspension of the restrictive measures imposed on certain persons and entities until ' +
        '29 February 2016. These persons and entities have been removed from the Consolidated List.[USA SANCTIONS - ' +
        'OFAC] SDN Ref No 9762 - Belarus (Jun 2006 - addition). PRIMARY NAME: ZIMOUSKY, Aliaksandr Leanidavich. a.k.a.' +
        ' SIMOWSKI, Aliaksandr Leanidavich; a.k.a. ZIMOUSKI",'
  }
}
