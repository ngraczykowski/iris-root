package com.silenteight.hsbc.bridge.alert

import com.silenteight.hsbc.bridge.json.external.model.CaseComment
import com.silenteight.hsbc.bridge.util.CustomDateTimeFormatter

import spock.lang.Specification

class CaseCommentsMapperSpec extends Specification {

  def fixtures = new Fixtures()
  def dateTimeFormatter = new CustomDateTimeFormatter("yyyy-MMM-dd HH:mm:ss")
  def underTest = new CaseCommentsMapper(dateTimeFormatter.getDateTimeFormatter())

  def "should find and return lastCaseComment and lastCaseCommentDateTime"() {
    given:
    def input = fixtures.caseComments

    when:
    def result = underTest.getLastCaseCommentWithDate(input)

    then:
    result.get("lastCaseCommentDateTime") == "2019-SEP-11 09:20:06"
    result.get("lastCaseComment") == "last_comment_value"
  }

  def "should return map with empty values for blank input data"() {
    given:
    def input = fixtures.caseCommentsBlank

    when:
    def result = underTest.getLastCaseCommentWithDate(input)

    then:
    result.get("lastCaseCommentDateTime") == ""
    result.get("lastCaseComment") == ""
  }

  class Fixtures {

    List<CaseComment> caseComments = [
        new CaseComment(
            commentDateTime: '2021-SEP-30 09:20:06',
            caseComment: 'newest_comment_value'
        ),
        new CaseComment(
            commentDateTime: '2019-SEP-11 09:20:06',
            caseComment: ''
        ),
        new CaseComment(
            commentDateTime: '',
            caseComment: 'blank_time'
        ),
        new CaseComment(
            commentDateTime: '',
            caseComment: ''
        ),
        new CaseComment(
            commentDateTime: '2019-SEP-11 09:20:06',
            caseComment: 'last_comment_value'
        )
    ]

    List<CaseComment> caseCommentsBlank = [
        new CaseComment(
            commentDateTime: '',
            caseComment: ''
        ),
        new CaseComment(
            commentDateTime: '',
            caseComment: ''
        )
    ]
  }
}
