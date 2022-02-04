package com.silenteight.payments.bridge.svb.oldetl.service

import com.silenteight.payments.bridge.svb.oldetl.model.UnsupportedMessageException
import com.silenteight.payments.bridge.svb.oldetl.service.impl.*

import spock.lang.Specification

import static com.silenteight.payments.bridge.svb.oldetl.util.CommonTerms.*

class ExtractOriginatorStrategySpec extends Specification {

  def "when factory is not found"() {

    given:
    def hitTag = ""
    def tagValueInFormatF = true
    when:
    ExtractOriginatorStrategy.choose(hitTag, tagValueInFormatF)
    then:
    thrown(UnsupportedMessageException.class)
  }

  def "when factory is found for tag originator and tagValueInFormat is true"() {

    given:
    def hitTag = TAG_ORIGINATOR
    def tagValueInFormatF = true
    when:
    def result = ExtractOriginatorStrategy.choose(hitTag, tagValueInFormatF)
    then:
    noExceptionThrown()
    result instanceof ExtractOriginatorBeneFormatFAlertedPartyData
  }

  def "when factory is found for tag originator and tagValueInFormat is false"() {

    given:
    def hitTag = TAG_ORIGINATOR
    def tagValueInFormatF = false
    when:
    def result = ExtractOriginatorStrategy.choose(hitTag, tagValueInFormatF)
    then:
    noExceptionThrown()
    result instanceof ExtractOriginatorAlertedPartyData
  }

  def "when factory is found for tag bene and tagValueInFormat is true"() {

    given:
    def hitTag = TAG_BENE
    def tagValueInFormatF = true
    when:
    def result = ExtractOriginatorStrategy.choose(hitTag, tagValueInFormatF)
    then:
    noExceptionThrown()
    result instanceof ExtractOriginatorBeneFormatFAlertedPartyData
  }

  def "when factory is found for tag bene and tagValueInFormat is false"() {

    given:
    def hitTag = TAG_BENE
    def tagValueInFormatF = false
    when:
    def result = ExtractOriginatorStrategy.choose(hitTag, tagValueInFormatF)
    then:
    noExceptionThrown()
    result instanceof ExtractBeneOrgbankInsbankAlertedPartyData
  }

  def "when factory is found for tag orgbank or insbank"(String hitTag) {

    expect:
    ExtractBeneOrgbankInsbankAlertedPartyData ==
        ExtractOriginatorStrategy.choose(hitTag, false).class

    where:
    hitTag      | _
    TAG_ORGBANK | _
    TAG_INSBANK | _
  }


  def "when factory is found for tag 50f"() {

    given:
    def hitTag = TAG_50F
    def tagValueInFormatF = true
    when:
    def result = ExtractOriginatorStrategy.choose(hitTag, tagValueInFormatF)
    then:
    noExceptionThrown()
    result instanceof Extract50FAlertedPartyData
  }

  def "when factory is found for tag receivbank"() {

    given:
    def hitTag = TAG_RECEIVBANK
    def tagValueInFormatF = true
    when:
    def result = ExtractOriginatorStrategy.choose(hitTag, tagValueInFormatF)
    then:
    noExceptionThrown()
    result instanceof ExtractReceivbankAlertedPartyData
  }

  def "when factory is found for tag is one of 50k, 59, 50"(String hitTag) {

    expect:
    Extract50k59AlertedPartyData.class == ExtractOriginatorStrategy.choose(hitTag, false).class

    where:

    hitTag  | _
    TAG_50K | _
    TAG_59  | _
    TAG_50  | _
  }
}
