package com.silenteight.fab.dataprep.domain.feature

import com.google.common.io.Resources
import groovy.util.logging.Slf4j
import spock.lang.Specification
import spock.lang.Unroll

@Slf4j
class AdditionalInfoHelperTest extends Specification {

  def 'field should be returned #additionalInfo'() {
    when:
    log.info("additionalInfo: $additionalInfo")
    def result = AdditionalInfoHelper.getValue(additionalInfo, fieldName)

    then:
    result == expectedValue

    where:
    additionalInfo                                 | fieldName      | expectedValue
    'Nationality: INDIA'                           | 'Nationality'  | 'INDIA'
    ' / Nationality: INDIA / Gender: MALE'         | 'Nationality'  | 'INDIA'
    ' / Nationality: UNITED STATES / Gender: MALE' | 'Nationality'  | 'UNITED STATES'
    ' / Nationality: INDIA / Gender: MALE '        | 'Gender'       | 'MALE'
    ' / Nationality: INDIA / Gender: MALE'         | 'UnknownField' | ''
    'List ID: 1054 / ID: 100 / Gender: MALE'       | 'ID'           | '100'
    'ID: 100 / List ID: 1054 / Gender: MALE'       | 'ID'           | '100'
    'List ID: 1054 / ID: 100'                      | 'ID'           | '100'
  }

  @Unroll
  def 'parse production example #id #fieldName'() {
    given:
    def additionalInfo = new File(Resources.getResource('additional-info.txt').toURI())
        .readLines()[id]
    when:
    def result = AdditionalInfoHelper.getValue(additionalInfo, fieldName)

    then:
    result == expectedValue

    where:
    id | fieldName          | expectedValue
    0  | 'List ID'          | '1054'
    0  | 'Create Date'      | '08/14/2013 14:42:14'
    0  | 'Last Update Date' | '1/07/2015 15:43:20'
    0  | 'Program'          | 'INTERPOL NOTICES'
    0  | 'Nationality'      | 'INDIA'
    0  | 'Gender'           | 'MALE'
    0  | 'OtherInformation' | 'WANTED BY THE JUDICIAL AUTHORITIES OF INDIA FOR PROSECUTION / TO ' +
        'SERVE A SENTENCE'
    0  | 'LanguagesSpoken'  | 'TAMIL'

    1  | 'List ID'          | '0'
    1  | 'Program'          | 'SDNTK & SDGT'
    1  | 'Nationality'      | 'INDIA'
    1  | 'Passport'         | 'A-333602 OtherInfo: INDIA; A-S01801 OtherInfo: INDIA; A-717288 ' +
        'OtherInfo: UNITED ARAB EMIRATES; F-823692 OtherInfo: YEMEN; G-866537 OtherInfo: PAKISTAN; ' +
        'G-869537 OtherInfo: PAKISTAN; K-560098 OtherInfo: INDIA; M-110522 OtherInfo: INDIA ISSUED ' +
        '11/13/78; P-537849 OtherInfo: INDIA; R-841697 OtherInfo: INDIA; V-57865 OtherInfo: INDIA'
    1  | 'Citizenship'      | 'INDIA; PAKISTAN; UNITED ARAB EMIRATES'
    1  | 'OriginalID'       | '9758'
    1  | 'Gender'           | ''

    2  | 'List ID'          | '0'
    2  | 'Program'          | 'SDNTK'
    2  | 'Weak Alias'       | "'MUSHTAQ'; 'MUSTAQ'; 'SIKANDER'; 'TIGER MEMON'"
    2  | 'Passport'         | 'AA762402 OtherInfo: PAKISTAN; L152818 OtherInfo: INDIA'
    2  | 'OriginalID'       | '13102'

    3  | 'List ID'          | '44'
    3  | 'Program'          | 'TALIBAN'
    3  | 'Title'            | 'HAJI; MINISTER OF FINANCE UNDER THE TALIBAN REGIME; MULLAH; ' +
        'PRESIDENT OF CENTRAL BANK (DA AFGHANISTAN BANK) UNDER THE TALIBAN REGIME'
    3  | 'OtherInformation' | 'Believed to be in Afghanistan/Pakistan border area. Belongs to ' +
        'Kakar tribe. He is a member of the Taliban Supreme Council. INTERPOL-UN Security Council ' +
        'Special Notice web link: https: //www.interpol.int/en/How-we-work/Notices/View-UN-Notices-Individuals'
    3  | 'OriginalID'       | 'TAi.031'

    4  | 'List ID'          | '1172'
    4  | 'Program'          | "DEMOCRATIC PEOPLE'S REPUBLIC OF KOREA"
    4  | 'PHONE'            | '+850-218-111 (ext. 8636)'
    4  | 'SourceLink'       | 'https://waw.undocs.org/S/2019/171'

    5  | 'List ID'          | '33'
    5  | 'Program'          | 'IRAQ - FORMER PRESIDENT SADDAM HUSSEIN'
    5  | 'Legal_Basis'      | '2119/2003 (03 L 318)'
  }
}
