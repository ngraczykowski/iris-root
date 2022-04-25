package com.silenteight.fab.dataprep.domain.tokenizer

import com.silenteight.fab.dataprep.domain.ex.DataPrepException

import groovy.util.logging.Slf4j
import spock.lang.Specification

@Slf4j
class MessageDataTokenizerTest extends Specification {

  def tokenizer = new MessageDataTokenizer()

  def "should correct parse payload #payload"() {
    when:
    def result = tokenizer.convert(payload)

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
    result.getNationalId() == expected[19]
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
    tokenizer.convert(payload)

    then:
    thrown(DataPrepException)

    where:
    payload << ["", ";".repeat(MessageDataTokenizer.NUMBER_OF_SEGMENTS - 3)]
  }

  def 'payload without shortName should be accepted'() {
    given:
    def payload = 'Mr.;ISLAMIC MOVEMENT OF UZBEKISTAN CAAO;Individial;01/01/1970;;;;FGB SCRAMBLE NAME-2 | FGB SCRAMBLE OVERSEA.ADDRESS;  | FGB SCRAMBLE STREET ; FGB SCRAMBLE COUNTRY;;IN;IN;AE;;RETAIL;;G1010101;784198148613635;;;T24CONV;1-4LFY;7110041;;7110041;20/04/2022'

    when:
    def result = tokenizer.convert(payload)
    log.info("$result")

    then:
    result.getSalutation() == 'Mr.'
    result.getCustomerType() == 'Individial'
    result.getCustomerSegment() == 'RETAIL'
    result.getDob() == '01/01/1970'
    result.getLastUpdateTime() == '20/04/2022'
    result.getCountry() == 'IN'
  }
}
