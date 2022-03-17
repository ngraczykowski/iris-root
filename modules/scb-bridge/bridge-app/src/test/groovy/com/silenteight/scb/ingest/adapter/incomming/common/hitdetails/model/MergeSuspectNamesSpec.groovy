package com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model

import com.silenteight.scb.ingest.adapter.incomming.common.WlName
import com.silenteight.scb.ingest.adapter.incomming.common.WlNameType
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Synonym

import spock.lang.Specification
import spock.lang.Unroll

import static com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Tag.NAME
import static com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Tag.SEARCH_CODE

class MergeSuspectNamesSpec extends Specification {

  private static def synonymOf(String input) {
    if (input.startsWith('- '))
      return new Synonym(input.substring(2), false)
    else if (input.startsWith('* '))
      return new Synonym(input.substring(2), true)
    else
      throw new IllegalArgumentException(
          'Input format "' + input + '" is illegal. Should start with - or * and space')
  }

  private static Suspect suspectOf(suspectDef) {
    def suspect = new Suspect()
    suspect.setBatchId('some_batch_id')
    suspect.setTag(suspectDef[0])
    suspect.setName(suspectDef[1])
    suspectDef[2].each {suspect.getNameSynonyms().add(synonymOf(it))}
    return suspect
  }

  private static Set<WlName> wlNames(wlNameDef) {
    def wlNames = new HashSet();
    wlNameDef.each {wlNames.add(wlNameOf(it))}
    return wlNames;
  }

  private static WlName wlNameOf(wlNameDef) {
    return new WlName(wlNameDef[0], WlNameType.valueOf(wlNameDef[1]))
  }

  @Unroll
  def "given one suspect=#suspect with Tag.NAME, check active names"() {
    expect:
    def suspect1 = suspectOf(suspect)
    assert suspect1.getActiveNames().toSet() == wlNames(expectedActiveNames)

    where:
    suspect                      || expectedActiveNames
    [NAME, 'A', []]              || [['A', 'NAME']]
    [NAME, 'A', ['* B']]         || [['B', 'ALIAS']]
    [NAME, 'A', ['- B', '- C']]  || [['A', 'NAME']]
    [NAME, 'A', ['- B', '* C']]  || [['C', 'ALIAS']]
    [NAME, null, ['- B', '- C']] || [['B', 'ALIAS'], ['C', 'ALIAS']]
    [NAME, null, ['- B', '* C']] || [['C', 'ALIAS']]
  }

  @Unroll
  def "given one suspect=#suspect with Tag.SEARCH_CODE, return as active the primary name only"() {
    expect:
    def suspect1 = suspectOf(suspect)
    assert suspect1.getActiveNames().toSet() == wlNames(expectedActiveNames)

    where:
    suspect                             || expectedActiveNames
    [SEARCH_CODE, 'A', []]              || [['A', 'NAME']]
    [SEARCH_CODE, 'A', ['* B']]         || [['A', 'NAME']]
    [SEARCH_CODE, 'A', ['- B', '- C']]  || [['A', 'NAME']]
    [SEARCH_CODE, 'A', ['- B', '* C']]  || [['A', 'NAME']]
    [SEARCH_CODE, null, ['- B', '- C']] || [['B', 'ALIAS'], ['C', 'ALIAS']]
    [SEARCH_CODE, null, ['- B', '* C']] || [['B', 'ALIAS'], ['C', 'ALIAS']]
  }

  @Unroll
  def "given suspects=[#suspect1, #suspect2] with Tag.NAME, check active names"() {
    expect:
    Suspect suspect1Obj = suspectOf(suspect1)
    Suspect suspect2Obj = suspectOf(suspect2)
    Suspect actualObj = suspect1Obj.merge(suspect2Obj)
    assert actualObj.getActiveNames().toSet() == wlNames(expectedActiveNames)

    where:
    suspect1                     | suspect2                    || expectedActiveNames
    [NAME, 'A', []]              | [NAME, 'B', []]             || [['A', 'NAME'], ['B', 'NAME']]
    [NAME, 'A', []]              | [NAME, 'B', ['- C']]        || [['A', 'NAME'], ['B', 'NAME']]
    [NAME, 'A', []]              | [NAME, 'B', ['* C']]        || [['A', 'NAME'], ['C', 'ALIAS']]
    [NAME, 'A', ['* B']]         | [NAME, 'C', ['* D']]        || [['B', 'ALIAS'], ['D', 'ALIAS']]
    [NAME, 'A', ['- B']]         | [NAME, 'C', ['- D', '- E']] || [['A', 'NAME'], ['C', 'NAME']]
    [NAME, 'A', []]              | [NAME, 'B', ['* C', '- D']] || [['A', 'NAME'], ['C', 'ALIAS']]
    [NAME, null, ['* B']]        | [NAME, 'C', ['* D']]        || [['B', 'ALIAS'], ['D', 'ALIAS']]
    [NAME, null, ['- B']]        | [NAME, 'C', ['- D', '- E']] || [['B', 'ALIAS'], ['C', 'NAME']]
    [NAME, null, ['* A', '- B']] | [NAME, 'C', ['- D', '- E']] || [['A', 'ALIAS'], ['C', 'NAME']]
    [NAME, 'A', ['- B', '- C']]  | [NAME, 'A', ['* B', '- C']] || [['A', 'NAME'], ['B', 'ALIAS']]
    [NAME, 'A', ['- B', '- C']]  | [NAME, 'D', ['* E', '- F']] || [['A', 'NAME'], ['E', 'ALIAS']]
    [NAME, 'A', ['* B', '- C']]  | [NAME, 'D', ['- E', '- F']] || [['B', 'ALIAS'], ['D', 'NAME']]
  }

  @Unroll
  def "given suspects=[#suspect1, #suspect2], both with != Tag.NAME, return primary names only"() {
    expect:
    Suspect suspect1Obj = suspectOf(suspect1)
    Suspect suspect2Obj = suspectOf(suspect2)
    Suspect actualObj = suspect1Obj.merge(suspect2Obj)
    assert actualObj.getActiveNames().toSet() == wlNames(expectedActiveNames.toSet())

    where:
    suspect1                            | suspect2                           ||
        expectedActiveNames
    [SEARCH_CODE, 'A', []]              | [SEARCH_CODE, 'A', []]             || [['A', 'NAME']]
    [SEARCH_CODE, 'A', []]              | [SEARCH_CODE, 'B', []]             ||
        [['A', 'NAME'], ['B', 'NAME']]
    [SEARCH_CODE, 'A', []]              | [SEARCH_CODE, 'B', ['- C']]        ||
        [['A', 'NAME'], ['B', 'NAME']]
    [SEARCH_CODE, 'A', []]              | [SEARCH_CODE, 'B', ['* C']]        ||
        [['A', 'NAME'], ['B', 'NAME']]
    [SEARCH_CODE, 'A', ['* B']]         | [SEARCH_CODE, 'C', ['* D']]        ||
        [['A', 'NAME'], ['C', 'NAME']]
    [SEARCH_CODE, 'A', ['- B']]         | [SEARCH_CODE, 'C', ['- D', '- E']] ||
        [['A', 'NAME'], ['C', 'NAME']]
    [SEARCH_CODE, 'A', []]              | [SEARCH_CODE, 'B', ['* C', '- D']] ||
        [['A', 'NAME'], ['B', 'NAME']]
    [SEARCH_CODE, null, ['- A', '- B']] | [SEARCH_CODE, 'C', ['- D', '- E']] ||
        [['A', 'ALIAS'], ['B', 'ALIAS'], ['C', 'NAME']]
    [SEARCH_CODE, null, ['- A', '* B']] | [SEARCH_CODE, 'C', ['* D', '- E']] ||
        [['A', 'ALIAS'], ['B', 'ALIAS'], ['C', 'NAME']]
  }

  @Unroll
  def "suspects=[#suspect1, #suspect2] with different tags, return names from Tag.NAME suspect"() {
    expect:
    Suspect suspect1Obj = suspectOf(suspect1)
    Suspect suspect2Obj = suspectOf(suspect2)
    Suspect actualObj = suspect1Obj.merge(suspect2Obj)
    assert actualObj.getActiveNames().toSet() == wlNames(expectedActiveNames.toSet())

    where:
    suspect1                     | suspect2                           || expectedActiveNames
    [SEARCH_CODE, 'A', []]       | [NAME, 'B', []]                    || [['B', 'NAME']]
    [SEARCH_CODE, 'A', []]       | [NAME, 'B', ['- C']]               || [['B', 'NAME']]
    [SEARCH_CODE, 'A', []]       | [NAME, 'B', ['* C']]               || [['C', 'ALIAS']]
    [SEARCH_CODE, 'A', ['* B']]  | [NAME, 'C', ['* D']]               || [['D', 'ALIAS']]
    [SEARCH_CODE, 'A', ['- B']]  | [NAME, 'C', ['- D', '- E']]        || [['C', 'NAME']]
    [SEARCH_CODE, 'A', []]       | [NAME, 'B', ['* C', '- D']]        || [['C', 'ALIAS']]
    [NAME, 'A', []]              | [SEARCH_CODE, 'B', []]             || [['A', 'NAME']]
    [NAME, 'A', []]              | [SEARCH_CODE, 'B', ['- C']]        || [['A', 'NAME']]
    [NAME, 'A', []]              | [SEARCH_CODE, 'B', ['* C']]        || [['A', 'NAME']]
    [NAME, 'A', ['* B']]         | [SEARCH_CODE, 'C', ['* D']]        || [['B', 'ALIAS']]
    [NAME, 'A', ['- B']]         | [SEARCH_CODE, 'C', ['- D', '- E']] || [['A', 'NAME']]
    [NAME, 'A', []]              | [SEARCH_CODE, 'B', ['* C', '- D']] || [['A', 'NAME']]
    [NAME, null, ['- A', '- B']] | [SEARCH_CODE, 'C', ['- D', '- E']] ||
        [['A', 'ALIAS'], ['B', 'ALIAS']]
    [NAME, null, ['- A', '* B']] | [SEARCH_CODE, 'B', ['* C', '- D']] || [['B', 'ALIAS']]
  }

  @Unroll
  def "given suspects=[#suspect1, #suspect2, #suspect3] results in names: #expectedActiveNames"() {
    expect:
    Suspect suspect1Obj = suspectOf(suspect1)
    Suspect suspect2Obj = suspectOf(suspect2)
    Suspect suspect3Obj = suspectOf(suspect3)

    suspect1Obj.merge(suspect2Obj)
    Suspect actualObj = suspect1Obj.merge(suspect3Obj)

    assert actualObj.getActiveNames().toSet() == wlNames(expectedActiveNames.toSet())

    where:
    suspect1                     | suspect2                    | suspect3                    ||
        expectedActiveNames
    [NAME, 'A', []]              | [NAME, 'B', []]             | [NAME, 'H', []]             ||
        [['A', 'NAME'], ['B', 'NAME'], ['H', 'NAME']]
    [NAME, 'A', []]              | [NAME, 'B', ['- C']]        | [NAME, 'H', []]             ||
        [['A', 'NAME'], ['B', 'NAME'], ['H', 'NAME']]
    [NAME, 'A', []]              | [NAME, 'B', ['* C']]        | [NAME, 'H', []]             ||
        [['A', 'NAME'], ['C', 'ALIAS'], ['H', 'NAME']]
    [NAME, 'A', ['- B']]         | [NAME, 'C', ['- D', '- E']] | [NAME, 'H', []]             ||
        [['A', 'NAME'], ['C', 'NAME'], ['H', 'NAME']]
    [NAME, 'A', []]              | [NAME, 'B', ['* C', '- D']] | [SEARCH_CODE, 'H', ['* I']] ||
        [['A', 'NAME'], ['C', 'ALIAS']]
    [NAME, null, ['* B']]        | [NAME, 'C', ['* D']]        | [NAME, 'H', []]             ||
        [['B', 'ALIAS'], ['D', 'ALIAS'], ['H', 'NAME']]
    [NAME, null, ['* A', '- B']] | [NAME, 'C', ['- D', '- E']] | [NAME, 'H', []]             ||
        [['A', 'ALIAS'], ['C', 'NAME'], ['H', 'NAME']]
    [NAME, null, ['* B']]        | [NAME, 'C', ['* D']]        | [SEARCH_CODE, 'H', []]      ||
        [['B', 'ALIAS'], ['D', 'ALIAS']]
    [NAME, null, ['* A', '- B']] | [NAME, 'C', ['- D', '- E']] | [SEARCH_CODE, 'H', []]      ||
        [['A', 'ALIAS'], ['C', 'NAME']]
  }

  @Unroll
  def "suspects=[#suspect1,#suspect2,#suspect3] results in name synonyms: #expectedNameSynonyms"() {
    expect:
    Suspect suspect1Obj = suspectOf(suspect1)
    Suspect suspect2Obj = suspectOf(suspect2)
    Suspect suspect3Obj = suspectOf(suspect3)

    suspect1Obj.merge(suspect2Obj)
    Suspect actualObj = suspect1Obj.merge(suspect3Obj)

    assert actualObj.getNameSynonyms().asSetOfNames() == expectedNameSynonyms.toSet()

    where:
    suspect1                     | suspect2                    | suspect3                    ||
        expectedNameSynonyms
    [NAME, 'A', []]              | [NAME, 'B', []]             | [NAME, 'H', []]             || []
    [NAME, 'A', []]              | [NAME, 'B', ['- C']]        | [NAME, 'H', []]             ||
        ['C']
    [NAME, 'A', []]              | [NAME, 'B', ['* C']]        | [NAME, 'H', []]             ||
        ['C']
    [NAME, 'A', ['- B']]         | [NAME, 'C', ['- D', '- E']] | [NAME, 'H', []]             ||
        ['B', 'D', 'E']
    [NAME, 'A', []]              | [NAME, 'B', ['* C', '- D']] | [SEARCH_CODE, 'H', ['* I']] ||
        ['C', 'D', 'I']
    [NAME, null, ['* B']]        | [NAME, 'C', ['* D']]        | [NAME, 'H', []]             ||
        ['B', 'D']
    [NAME, null, ['* A', '- B']] | [NAME, 'C', ['- D', '- E']] | [NAME, 'H', []]             ||
        ['A', 'B', 'D', 'E']
    [NAME, null, ['* B']]        | [NAME, 'C', ['* D']]        | [SEARCH_CODE, 'H', []]      ||
        ['B', 'D']
    [NAME, null, ['* A', '- B']] | [NAME, 'C', ['- D', '- E']] | [SEARCH_CODE, 'H', []]      ||
        ['A', 'B', 'D', 'E']
  }
}
