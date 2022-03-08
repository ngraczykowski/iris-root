package com.silenteight.fab.dataprep.domain

import spock.lang.Specification

class TransformServiceTest extends Specification {

  def "should correct parse payload #payload"() {
    when:
    def result = new TransformService().convert(payload)
    then:
    result.getSalutation() == expected[0]
    result.getName() == expected[1]
    result.getShortName() == expected[2]
    result.getCustomerType() == expected[3]
    result.getDob() == expected[4]
    result.getDateOfEstablishment() == expected[5]
    result.getGender() == expected[6]
    result.getSwiftBic() == expected[7]
    result.getAddress1() == expected[8]
    result.getAddress2() == expected[9]
    result.getCity() == expected[10]
    result.getState() == expected[11]
    result.getCountry() == expected[12]
    result.getCountryOfIncorporation() == expected[13]
    result.getCountryOfDomicile() == expected[14]
    result.getCountryOfBirth() == expected[15]
    result.getCustomerSegment() == expected[16]
    result.getProfession() == expected[17]
    result.getPassportNum() == expected[18]
    result.getNational() == expected[19]
    result.getTradeLicPlaceOfIssue() == expected[20]
    result.getGroupOrCompanyName() == expected[21]
    result.getSource() == expected[22]
    result.getSourceSystemId() == expected[23]
    result.getCustomerNumber() == expected[24]
    result.getAlternate() == expected[25]
    result.getLatestCustomerNumber() == expected[26]
    result.getLastUpdateTime() == expected[27]

    where:
    payload << new File("src/test/resources/payload.txt").readLines()
    expected <<
        new File("src/test/resources/payload.txt").readLines().stream().map(l -> l.split(";", -1))
  }

  def "should throws exception when can't payload #payload"() {
    when:
    new TransformService().convert(payload)
    then:
    thrown(IllegalArgumentException)

    where:
    payload << ["", ";".repeat(TransformService.NUMBER_OF_SEGMENTS - 2)]
  }

}
