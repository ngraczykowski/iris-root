package com.silenteight.hsbc.datasource.extractors.name

import com.silenteight.hsbc.datasource.dto.name.ForeignAliasDto

import spock.lang.Specification
import spock.lang.Unroll

import java.util.stream.Collectors
import java.util.stream.Stream

class NameExtractorSpec extends Specification {

  @Unroll
  def 'should collect given names'() {
    given:
    def inputStream = listToStream(input)

    when:
    def names = NameExtractor.collectNames(inputStream)

    then:
    streamToList(names) == expected

    where:
    input                                                         | expected
    null                                                          | []
    []                                                            | []
    [null]                                                        | []
    [null, null, '', '   ', null]                                 | []
    ['Osama Bin Laden', null]                                     | ['Osama Bin Laden']
    ['Osama Bin Laden', 'Osama Bin Laden']                        | ['Osama Bin Laden']
    ['Osama Bin Laden', 'Usama Bin Laden']                        | ['Osama Bin Laden', 'Usama Bin Laden']
    ['Osama Bin Laden', 'Usama Bin Laden', 'Usama Bin Laden']     | ['Osama Bin Laden', 'Usama Bin Laden']
    ['  OSAMA BIN LADEN  ', 'Osama Bin Laden', 'osama bin laden'] | ['Osama Bin Laden']
  }

  @Unroll
  def 'should join name parts'() {
    when:
    def joinedName = NameExtractor.joinNameParts(input as String[])

    then:
    joinedName == expected

    where:
    input                               | expected
    null                                | ''
    []                                  | ''
    [null]                              | ''
    [null, '', null]                    | ''
    ['Osama', 'Bin Laden']              | 'Osama Bin Laden'
    ['Osama', null, 'Laden']            | 'Osama Laden'
    ['Osama', '', 'Laden']              | 'Osama Laden'
    ['  Osama   ', '  ', '   Laden   '] | 'Osama Laden'
  }

  @Unroll
  def 'should remove unnecessary asterisk'() {
    when:
    def output = NameExtractor.removeUnnecessaryAsterisk(input)

    then:
    output == expected

    where:
    input          | expected
    null           | ''
    ''             | ''
    '   '          | ''
    'John Smith'   | 'John Smith'
    '*John Smith'  | 'John Smith'
    'John *Smith'  | 'John Smith'
    'John** Smith' | 'John Smith'
    'John Smith**' | 'John Smith'
    'John Sm*th'   | 'John Sm*th'
  }

  @Unroll
  def 'should extract name and original script aliases'() {
    when:
    def extractedNames = NameExtractor.extractNameAndOriginalScriptAliases(input)

    then:
    streamToList(extractedNames) == expected

    where:
    input                             | expected
    null                              | []
    ''                                | []
    '   '                             | []
    'Silent Eight (Singapore)'        | ['Silent Eight (Singapore)']
    'Silent Eight'                    | ['Silent Eight']
    'Silent Eight (Pte Ltd)'          | ['Silent Eight (Pte Ltd)']
    'Silent Eight (123)'              | ['Silent Eight (123)']
    'HSBC (????????????????????????)'                 | ['HSBC', '????????????????????????']
    '(????????????????????????) HSBC'                 | ['HSBC', '????????????????????????']
    'HSBC (????????????????????????) Pte. Ltd.'       | ['HSBC Pte. Ltd.', '????????????????????????']
    'Osama bin Laden (?????????? ???? ????????)' | ['Osama bin Laden', '?????????? ???? ????????']
  }

  @Unroll
  def 'should collect country matching aliases'() {
    when:
    def output = NameExtractor.collectCountryMatchingAliases(foreignAliases, countries)

    then:
    output == expected

    where:
    foreignAliases                                                | countries | expected
    null                                                          | null      | []
    null                                                          | []        | []
    []                                                            | []        | []
    [null]                                                        | []        | []
    [null]                                                        | [null]    | []
    [new ForeignAliasDto('a', 'zh-CN')] | [null] | []
    [null]                                                        | ['CN']    | []
    [new ForeignAliasDto('?????????', 'zh-CN')]                         | ['CHINA'] | ['?????????']
    [new ForeignAliasDto('?????????', 'zh-CN'),
     new ForeignAliasDto('?????????????????? ?????????????????????????? ????????????', 'ru')] | ['CHINA'] | ['?????????']
  }

  @Unroll
  def 'should apply original script enhancements'() {
    given:
    def ap = alertedParty
    def wp = watchlistParty

    when:
    def output = NameExtractor.applyOriginalScriptEnhancements(ap, wp, nns)

    then:
    output.alertedPartyIndividuals == apExpected
    output.watchlistPartyIndividuals == wpExpected
    output.nnsIndividuals == nnsExpected

    where:
    alertedParty                          | watchlistParty                        | nns                                     | apExpected                              | wpExpected                                | nnsExpected
    []                                    | []                                    | []                                      | []                                      | []                                        | []
    null                                  | null                                  | null                                    | []                                      | []                                        | []
    null                                  | []                                    | null                                    | []                                      | []                                        | []
    ['Osama Bin Laden']                   | ['Osama Bin Laden']                   | ['Osama Bin Laden']                     | ['Osama Bin Laden']                     | ['Osama Bin Laden']                       | ['Osama Bin Laden']
    ['Osama', '??????????']                    | ['Osama', '??????????']                    | ['Osama', '??????????']                      | ['??????????']                               | ['??????????']                                 | ['??????????']
    ['Lee Hsien Loong']                   | ['Lee Hsien Loong', '?????????', '?????????'] | ['Lee Hsien Loong', '?????????', '?????????']   | ['Lee Hsien Loong']                     | ['Lee Hsien Loong', '?????????', '?????????']     | ['Lee Hsien Loong', '?????????', '?????????']
    ['Lee Hsien Loong', '?????????', '?????????'] | ['Lee Hsien Loong']                   | ['Lee Hsien Loong']                     | ['Lee Hsien Loong', '?????????', '?????????']   | ['Lee Hsien Loong']                       | ['Lee Hsien Loong']
    ['Lee Hsien Loong', '?????????', '?????????'] | ['Lee Hsien Loong', '?????????', '1234']  | ['Lee Hsien Loong', '?????????', '1234']    | ['?????????', '?????????']                      | ['?????????']                                 | ['?????????']
    ['Lee Hsien Loong', '1234']           | ['Lee Hsien Loong', '1234']           | ['Lee Hsien Loong', '1234']             | ['Lee Hsien Loong', '1234']             | ['Lee Hsien Loong', '1234']               | ['Lee Hsien Loong', '1234']
  }

  private static streamToList(Stream<String> names) {
    names.collect(Collectors.toList())
  }

  private static listToStream(List<String> names) {
    names.stream()
  }
}
