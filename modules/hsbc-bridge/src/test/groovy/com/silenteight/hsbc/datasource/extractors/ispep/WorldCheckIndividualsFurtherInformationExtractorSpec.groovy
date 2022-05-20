package com.silenteight.hsbc.datasource.extractors.ispep

import com.silenteight.hsbc.bridge.json.internal.model.WorldCheckIndividual

import spock.lang.Specification

class WorldCheckIndividualsFurtherInformationExtractorSpec extends Specification {

  def 'returns last updated further information for individuals'() {
    given:
    def firstFurtherInformation = new WorldCheckIndividual(
        furtherInformation: furtherInformation1,
        lastUpdatedDate: lastUpdatedDate1)

    def secondFurtherInformation = new WorldCheckIndividual(
        furtherInformation: furtherInformation2,
        lastUpdatedDate: lastUpdatedDate2)

    when:
    def furtherInformation = new WorldCheckIndividualsFurtherInformationExtractor(
        [firstFurtherInformation, secondFurtherInformation]).extract()

    then:
    expected == furtherInformation

    where:
    furtherInformation1 || lastUpdatedDate1 || furtherInformation2 || lastUpdatedDate2 || expected
    'PEP'               || '09-Jul-2021'    || 'NOT_PEP'           || '08-Jul-2021'    || 'PEP'
    'PEP'               || '08-Jul-2021'    || 'NOT_PEP'           || '09-Jul-2021'    || 'NOT_PEP'
    'PEP'               || '08-Jul-2021'    || 'NOT_PEP'           || '08-Jul-2021'    || 'NOT_PEP'
    'PEP'               || ''               || 'NOT_PEP'           || ''               || 'NOT_PEP'
    'PEP'               || '08-Jul-2021'    || 'NOT_PEP'           || ''               || 'PEP'
    'PEP'               || ''               || 'NOT_PEP'           || '08-Jul-2021'    || 'NOT_PEP'
    'PEP'               || null             || 'NOT_PEP'           || null             || 'NOT_PEP'
    'PEP'               || null             || 'NOT_PEP'           || '08-Jul-2021'    || 'NOT_PEP'
    'PEP'               || '08-Jul-2021'    || 'NOT_PEP'           || null             || 'PEP'
    null                || '08-Jul-2021'    || 'NOT_PEP'           || null             || ''
    null                || null             || null                || null             || ''
    ''                  || ''               || ''                  || ''               || ''
    'PEP'               || '08-Jul-2021'    || null                || null             || 'PEP'
  }
}
