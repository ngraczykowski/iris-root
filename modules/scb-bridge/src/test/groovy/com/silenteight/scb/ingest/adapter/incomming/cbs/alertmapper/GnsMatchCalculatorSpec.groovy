package com.silenteight.scb.ingest.adapter.incomming.cbs.alertmapper

import com.silenteight.scb.ingest.adapter.incomming.cbs.domain.NeoFlag
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect

import spock.lang.Specification
import spock.lang.Unroll

import static com.silenteight.proto.serp.v1.alert.Match.Flags.*

class GnsMatchCalculatorSpec extends Specification {

  @Unroll
  def 'calculate flags into #expectedResult for neoFlag: #neoFlag, batchId: #batchId'() {
    given:
    def suspect = new Suspect(neoFlag: neoFlag, batchId: batchId)
    def lastDecBatchId = ''
    def hasLastDecision = false

    when:
    def result = new GnsMatchCalculator(suspect, lastDecBatchId, hasLastDecision).calculateFlags()

    then:
    result == expectedResult

    where:
    neoFlag          | batchId         | expectedResult
    null             | null            | FLAG_OBSOLETE_VALUE
    null             | ''              | FLAG_NONE_VALUE
    NeoFlag.NEW      | null            | FLAG_NONE_VALUE
    NeoFlag.EXISTING | null            | FLAG_SOLVED_VALUE
    NeoFlag.OBSOLETE | null            | FLAG_OBSOLETE_VALUE
    NeoFlag.NEW      | ''              | FLAG_NONE_VALUE
    NeoFlag.EXISTING | ''              | FLAG_SOLVED_VALUE
    NeoFlag.OBSOLETE | ''              | FLAG_OBSOLETE_VALUE
    NeoFlag.NEW      | 'obsolete-123'  | FLAG_NONE_VALUE
    NeoFlag.EXISTING | 'obsolete-123'  | FLAG_SOLVED_VALUE
    NeoFlag.OBSOLETE | 'obsolete-123'  | FLAG_OBSOLETE_VALUE
    NeoFlag.NEW      | '2018/12/01_01' | FLAG_NONE_VALUE
    NeoFlag.EXISTING | '2018/12/01_01' | FLAG_SOLVED_VALUE
    NeoFlag.OBSOLETE | '2018/12/01_01' | FLAG_OBSOLETE_VALUE
  }

  @Unroll
  def 'calculate flags when neo flag is empty, #batchId, #lastDecBatchId, #hasLastDecision'() {
    given:
    def suspect = new Suspect(batchId: batchId)

    when:
    def result = new GnsMatchCalculator(suspect, lastDecBatchId, hasLastDecision).calculateFlags()

    then:
    result == expectedResult

    where:
    batchId         | lastDecBatchId  | hasLastDecision | expectedResult
    null            | null            | false           | FLAG_OBSOLETE_VALUE
    null            | 'obsolete-123'  | false           | FLAG_OBSOLETE_VALUE
    null            | '2018/12/01_02' | false           | FLAG_OBSOLETE_VALUE
    null            | null            | true            | FLAG_OBSOLETE_VALUE
    null            | 'obsolete-123'  | true            | FLAG_OBSOLETE_VALUE
    null            | '2018/12/01_02' | true            | FLAG_OBSOLETE_VALUE
    ''              | null            | false           | FLAG_NONE_VALUE
    ''              | null            | true            | FLAG_NONE_VALUE
    ''              | ''              | true            | FLAG_NONE_VALUE
    'obsolete-123'  | null            | false           | FLAG_OBSOLETE_VALUE
    'obsolete-123'  | 'obsolete-123'  | true            | FLAG_OBSOLETE_VALUE
    '2018/12/01_01' | '2018/12/01_01' | true            | FLAG_SOLVED_VALUE
    '2018/12/01_01' | '2018/12/01_02' | true            | FLAG_SOLVED_VALUE
    '2018/12/01_01' | '2018/12/01_01' | false           | FLAG_NONE_VALUE
    '2018/12/01_01' | '2018/12/01_02' | false           | FLAG_NONE_VALUE
  }
}
