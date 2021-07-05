package com.silenteight.hsbc.datasource.extractors.geolocation

import spock.lang.Specification
import spock.lang.Unroll

import java.util.stream.Stream

import static com.silenteight.hsbc.datasource.extractors.geolocation.GeoLocationExtractor.*
import static com.silenteight.hsbc.datasource.extractors.geolocation.GeoLocationExtractor.SignType.*
import static java.util.stream.Collectors.toList

class GeoLocationExtractorSpec extends Specification {

  @Unroll
  def 'should join given fields: #input to expected string: #expected'() {
    when:
    def joinedFields = joinFields(input as String[])

    then:
    joinedFields == expected

    where:
    input                               | expected
    null                                | ''
    []                                  | ''
    [null]                              | ''
    [null, '', null]                    | ''
    ['First', 'Second']                 | 'First Second'
    ['First', null, 'Third']            | 'First Third'
    ['First', '', 'Third']              | 'First Third'
    ['First', 'First', 'Third']         | 'First First Third'
    ['First', 'SECOND', 'Third']        | 'First SECOND Third'
    ['  First   ', '  ', '   Third   '] | 'First Third'
  }

  @Unroll
  def 'should merge given fields: #input to expected string: #expected'() {
    when:
    def mergedFields = mergeFields(input as List<String>)

    then:
    mergedFields == expected

    where:
    input                               | expected
    null                                | ''
    []                                  | ''
    [null]                              | ''
    [null, '', null]                    | ''
    ['First', 'Second']                 | 'FIRST SECOND'
    ['First', null, 'Third']            | 'FIRST THIRD'
    ['First', '', 'Third']              | 'FIRST THIRD'
    ['First', 'First', 'Third']         | 'FIRST THIRD'
    ['First', 'SECOND', 'Third']        | 'FIRST SECOND THIRD'
    ['  First   ', '  ', '   Third   '] | 'FIRST THIRD'
  }

  @Unroll
  def 'should split extracted value: #input by SEMICOLON to specific stream: #expected'() {
    when:
    def result = splitExtractedValueBySign(SEMICOLON, input as String)

    then:
    streamToList(result) == expected

    where:
    input                       | expected
    null                        | []
    'Ala has a;cat'             | ['Ala has a', 'cat']
    'Ala has a; cat '           | ['Ala has a', ' cat ']
    'ALA has a; CAT '           | ['ALA has a', ' CAT ']
    'ALA has a; CAT and;Mouse;' | ['ALA has a', ' CAT and', 'Mouse']
  }

  @Unroll
  def 'should split extracted value: #input by COMA to specific stream: #expected'() {
    when:
    def result = splitExtractedValueBySign(COMA, input as String)

    then:
    streamToList(result) == expected

    where:
    input                       | expected
    null                        | []
    'Ala has a,cat'             | ['Ala has a', 'cat']
    'Ala has a, cat '           | ['Ala has a', ' cat ']
    'ALA has a, CAT '           | ['ALA has a', ' CAT ']
    'ALA has a, CAT and,Mouse,' | ['ALA has a', ' CAT and', 'Mouse']
  }

  @Unroll
  def 'should split extracted value: #input by SPACE to specific stream: #expected'() {
    when:
    def result = splitExtractedValueBySign(SPACE, input as String)

    then:
    streamToList(result) == expected

    where:
    input                   | expected
    null                    | []
    'Ala has a cat'         | ['Ala', 'has', 'a', 'cat']
    'ALA has a CAT'         | ['ALA', 'has', 'a', 'CAT']
    ' Ala  has   a    cat ' | ['', 'Ala', '', 'has', '', '', 'a', '', '', '', 'cat']
  }

  private static streamToList(Stream<String> names) {
    names.collect(toList())
  }
}
