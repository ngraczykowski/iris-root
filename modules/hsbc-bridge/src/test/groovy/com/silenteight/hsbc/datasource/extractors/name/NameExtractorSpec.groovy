package com.silenteight.hsbc.datasource.extractors.name

import spock.lang.Specification
import spock.lang.Unroll

import java.util.stream.Stream

import static com.silenteight.hsbc.datasource.extractors.name.NameExtractor.*
import static java.util.stream.Collectors.toList

class NameExtractorSpec extends Specification {

  @Unroll
  def 'should collect given names'() {
    given:
    def inputStream = listToStream(input)

    when:
    def names = collectNames(inputStream)

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
    def joinedName = joinNameParts(input as String[])

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
    def output = removeUnnecessaryAsterisk(input)

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
    def extractedNames = extractNameAndOriginalScriptAliases(input)

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
    'HSBC (汇丰控股有限公司)'                 | ['HSBC', '汇丰控股有限公司']
    '(汇丰控股有限公司) HSBC'                 | ['HSBC', '汇丰控股有限公司']
    'HSBC (汇丰控股有限公司) Pte. Ltd.'       | ['HSBC Pte. Ltd.', '汇丰控股有限公司']
    'Osama bin Laden (أسامة بن لادن)' | ['Osama bin Laden', 'أسامة بن لادن']
  }

  @Unroll
  def 'should collect country matching aliases'() {
    when:
    def output = collectCountryMatchingAliases(foreignAliases, countries)

    then:
    output == expected

    where:
    foreignAliases                                                | countries | expected
    null                                                          | null      | []
    null                                                          | []        | []
    []                                                            | []        | []
    [null]                                                        | []        | []
    [null]                                                        | [null]    | []
    [new ForeignAliasDto('a', 'zh-CN')]                           | [null]    | []
    [null]                                                        | ['CN']    | []
    [new ForeignAliasDto('李显龙', 'zh-CN')]                         | ['CHINA'] | ['李显龙']
    [new ForeignAliasDto('李显龙', 'zh-CN'),
     new ForeignAliasDto('Влади́мир Влади́мирович Пу́тин', 'ru')] | ['CHINA'] | ['李显龙']
  }

  @Unroll
  def 'should apply original script enhancements'() {
    given:
    def ap = alertedParty
    def wp = watchlistParty

    when:
    def output = applyOriginalScriptEnhancements(ap, wp)

    then:
    output.alertedPartyIndividuals == apExpected
    output.watchlistPartyIndividuals == wpExpected

    where:
    alertedParty                      | watchlistParty                     | apExpected                        | wpExpected
    []                                | []                                 | []                                | []
    null                              | null                               | []                                | []
    null                              | []                                 | []                                | []
    ['Osama Bin Laden']               | ['Osama Bin Laden']                | ['Osama Bin Laden']               | ['Osama Bin Laden']
    ['Osama', 'أسامة']                | ['Osama', 'أسامة']                 | ['أسامة']                         | ['أسامة']
    ['Lee Hsien Loong']               | ['Lee Hsien Loong', '李显龙', '李顯龍']  | ['Lee Hsien Loong']               | ['Lee Hsien Loong', '李显龙', '李顯龍']
    ['Lee Hsien Loong', '李显龙', '李顯龍'] | ['Lee Hsien Loong']                | ['Lee Hsien Loong', '李显龙', '李顯龍'] | ['Lee Hsien Loong']
    ['Lee Hsien Loong', '李显龙', '李顯龍'] | ['Lee Hsien Loong', '李顯龍', '1234'] | ['李显龙', '李顯龍']                    | ['李顯龍']
    ['Lee Hsien Loong', '1234']       | ['Lee Hsien Loong', '1234']        | ['Lee Hsien Loong', '1234']       | ['Lee Hsien Loong', '1234']
  }

  private static streamToList(Stream<String> names) {
    names.collect(toList())
  }

  private static listToStream(List<String> names) {
    names.stream()
  }
}
