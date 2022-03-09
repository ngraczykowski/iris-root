package com.silenteight.scb.ingest.adapter.incomming.cbs.alertmapper

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertmapper.AlertMapper.Option
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord.AlertRecordComposite
import com.silenteight.scb.ingest.adapter.incomming.cbs.domain.CbsHitDetails
import com.silenteight.scb.ingest.adapter.incomming.cbs.domain.NeoFlag
import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord
import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.DecisionRecord
import com.silenteight.scb.ingest.adapter.incomming.common.batch.DateConverter
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect
import com.silenteight.proto.serp.v1.alert.Alert
import com.silenteight.proto.serp.v1.alert.Alert.Flags
import com.silenteight.proto.serp.v1.alert.Alert.State
import com.silenteight.proto.serp.v1.alert.AnalystSolution
import com.silenteight.proto.serp.v1.alert.Match

import spock.lang.Specification

import java.time.Instant

import static java.util.Optional.empty

class AlertMapperSpec extends Specification {

  def dateConverter = Mock(DateConverter)
  def matchCollector = Mock(MatchCollector)
  def suspectsCollector = Mock(SuspectsCollector)
  def objectUnderTest = new AlertMapper(dateConverter, matchCollector, suspectsCollector)

  def 'should map alert level alert record composite into single alert'() {
    given:
    def composite = AlertRecordComposite.builder()
        .alert(alertRecord)
        .cbsHitDetails(cbsHitDetails)
        .decisions(decisions)
        .build()

    when:
    def result = objectUnderTest.fromAlertRecordComposite(composite, alertLevelOptions)

    then:
    1 * dateConverter.convert(alertRecord.filteredString) >> empty()
    1 * matchCollector.collectMatches(suspects, _ as AlertContext) >> matches
    1 * suspectsCollector.collect(alertRecord.details, cbsHitDetails) >> suspects
    result.size() == 1
    verifyAlert(result.first())
  }

  def 'should map watchlist level alert record composite into alerts'() {
    given:
    def composite = AlertRecordComposite.builder()
        .alert(alertRecord)
        .cbsHitDetails(cbsHitDetails)
        .decisions(decisions)
        .build()

    when:
    def result = objectUnderTest.fromAlertRecordComposite(composite, watchlistLevelOptions)

    then:
    2 * dateConverter.convert(alertRecord.filteredString) >> empty()
    1 * matchCollector.collectMatches([suspect1], _ as AlertContext) >> matches
    1 * matchCollector.collectMatches([suspect2], _ as AlertContext) >> matches
    1 * suspectsCollector.collect(alertRecord.details, cbsHitDetails) >> suspects
    result.size() == 2
    verifyAlert(result.first())
    verifyAlert(result.get(1))
  }

  def 'should map watchlist level alert record composite into alerts and ignore solved hits'() {
    given:
    def suspectWithNeoFlag1 = new Suspect(neoFlag: NeoFlag.NEW, ofacId: '1')
    def suspectWithNeoFlag2 = new Suspect(neoFlag: NeoFlag.NEW, ofacId: '2')
    def suspectWithoutNeoFlag = new Suspect(ofacId: '3')
    def suspects = [suspectWithNeoFlag1, suspectWithNeoFlag2, suspectWithoutNeoFlag]
    def composite = AlertRecordComposite.builder()
        .alert(alertRecord)
        .cbsHitDetails(cbsHitDetails)
        .decisions(decisions)
        .build()

    when:
    def result = objectUnderTest.fromAlertRecordComposite(composite, watchlistLevelOptions)

    then:
    2 * dateConverter.convert(alertRecord.filteredString) >> empty()
    1 * matchCollector.collectMatches([suspectWithNeoFlag1], _ as AlertContext) >> matches
    1 * matchCollector.collectMatches([suspectWithNeoFlag2], _ as AlertContext) >> matches
    0 * matchCollector.collectMatches([suspectWithoutNeoFlag], _ as AlertContext)
    1 * suspectsCollector.collect(alertRecord.details, cbsHitDetails) >> suspects
    result.size() == 2
    verifyAlert(result.first())
  }

  def 'should map to damaged alert and do not request for recommendation'() {
    given:
    def options = [Option.FOR_RECOMMENDATION, Option.ATTACH_ALERT] as Option[]
    def composite = AlertRecordComposite.builder()
        .alert(alertRecord)
        .cbsHitDetails(cbsHitDetails)
        .decisions(decisions)
        .build()

    when:
    def result = objectUnderTest.fromAlertRecordComposite(composite, options)

    then:
    1 * dateConverter.convert(alertRecord.filteredString) >> empty()
    1 * matchCollector.collectMatches([], _ as AlertContext) >> []
    1 * suspectsCollector.collect(alertRecord.details, cbsHitDetails) >> []
    result.size() == 1
    with(result.first()) {
      flags == Flags.FLAG_NONE_VALUE
      state == State.STATE_DAMAGED
    }
  }

  def 'should do not allow to map solved alert when ONLY_UNSOLVED was passed'() {
    given:
    def onlyUnsolvedOptions = (defaultOptions + [Option.ONLY_UNSOLVED]) as Option[]
    def composite = AlertRecordComposite.builder()
        .alert(alertRecord)
        .cbsHitDetails(cbsHitDetails)
        .decisions(decisions)
        .build()

    when:
    def result = objectUnderTest.fromAlertRecordComposite(composite, onlyUnsolvedOptions)

    then:
    1 * dateConverter.convert(alertRecord.filteredString) >> empty()
    1 * matchCollector.collectMatches([], _ as AlertContext) >> []
    1 * suspectsCollector.collect(alertRecord.details, cbsHitDetails) >> []
    result.isEmpty()
  }

  def verifyAlert(Alert alert) {
    alert.alertedParty
    alert.decisionGroup == alertRecord.unit
    alert.decisionsCount == decisions.size()
    alert.details
    alert.flags == (Flags.FLAG_RECOMMEND_VALUE | Flags.FLAG_PROCESS_VALUE | Flags.FLAG_ATTACH_VALUE)
    alert.generatedAt
    alert.id
    alert.matchesList == matches
    alert.securityGroup == 'MY'
    alert.state == State.STATE_CORRECT
  }

  def alertRecord = AlertRecord.builder()
      .charSep((char) '~')
      .details('details')
      .filteredString('2019/01/24 19:58:53')
      .fmtName('SCB-ADV')
      .record(
          '''(CLOB) CCMS~HKCCNS600~I~1~~John Smith~~75 W Mondamin St, Minooka, IL 60447, USA
        ~~~~~~118296867~~19690926~~~~~~~HK~ACTIVE~HKCB~1~D693360~~~~~~~~''')
      .systemId('MY_EMPL_DENY!34CB2561-578C4AD8-93A341A5-47A55B7A')
      .unit('MY_EMPL_DENY')
      .build()
  def cbsHitDetails = [CbsHitDetails.builder().build()]
  def decision = DecisionRecord.builder()
      .decisionDate(Instant.now())
      .operator('FSK')
      .systemId('')
      .solution(AnalystSolution.ANALYST_NO_SOLUTION)
      .build()
  def decisions = [decision]
  def match = Match.newBuilder().build()
  def matches = [match]
  def suspect1 = new Suspect(ofacId: '1')
  def suspect2 = new Suspect(ofacId: '1')
  def suspects = [suspect1, suspect2]
  def defaultOptions = [Option.FOR_RECOMMENDATION, Option.ATTACH_ALERT, Option.ONLY_UNSOLVED]
  def watchlistLevelOptions = (defaultOptions + [Option.WATCHLIST_LEVEL]) as Option[]
  def alertLevelOptions = defaultOptions as Option[]
}
