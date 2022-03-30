package com.silenteight.scb.ingest.adapter.incomming.common.gender

import spock.lang.Specification
import spock.lang.Unroll

import static com.silenteight.scb.ingest.adapter.incomming.common.gender.Gender.*

class TitleGenderDetectorSpec extends Specification {

  @Unroll
  def "From names input '#names' TitleGenderDetector.detect should return: #expected"() {
    expect:
    TitleGenderDetector.detect(names) == expected

    where:
    names                                   | expected
    ["KIM NGUYEN,mS NGUYEN"]                | FEMALE
    ["mS NGUYEN"]                           | FEMALE
    ["mRS KIM NGUYEN"]                      | FEMALE
    ["KIM NGUYEN D/o LEE"]                  | FEMALE
    ["KIM w/o LEE"]                         | FEMALE
    ["mrs LEE NGUYEN,KIM NGUYEN"]           | FEMALE
    ["mrs LEE NGUYEN,MS LEE NGUYEN"]        | FEMALE
    ["LEE NGUYEN,ms KIM NGUYEN"]            | FEMALE
    ["LEE w/o KIM,ms lee NGUYEN"]           | FEMALE
    ["LEE W/O CHIANG"]                      | FEMALE
    ["Lee d/o Chiang"]                      | FEMALE
    ["Lee,d/o Chiang"]                      | FEMALE
    ["MISS KIM NGUYEN"]                     | FEMALE
    ["Mr LEE NGUYEN"]                       | MALE
    ["LEE NGUYEN s/O KIM"]                  | MALE
    ["LEE h/o KIM"]                         | MALE
    ["lee h/o KIM,lee nguyen"]              | MALE
    ["LEE s/o lee,mr lee nguyen,lee"]       | MALE
    ["LEE s/o kim,lee h/o,lee"]             | MALE
    ["LEE H/O KIM"]                         | MALE
    ["S/O KIM, LEE"]                        | MALE
    [null, 'Mr Kim Chiang']                 | MALE
    ["KIM NGUYEN"]                          | UNKNOWN
    ["NGUYEN MS"]                           | UNKNOWN
    ["KIM NGUYEN MRS"]                      | UNKNOWN
    ["LEE NGUYEN MR"]                       | UNKNOWN
    ["mr LEE NGUYEN,ms kim, mr lee"]        | UNKNOWN
    ["lee s/o kim,mr lee,ms LEE"]           | UNKNOWN
    ['Mr Barack Obama', 'Ms Obama']         | UNKNOWN
    ['TYAS WASKITO MSC']                    | UNKNOWN
    ['MSC TYAS WASKITO']                    | UNKNOWN
    ["KOH/OUYANG"]                          | UNKNOWN
    ["MR Barack Obama", "Barack d/o Obama"] | UNKNOWN
    ["Katarzyna Mroczek"]                   | UNKNOWN
    ["Lee s/d/o KIM"]                       | UNKNOWN
    ["KIM, LEE S/O"]                        | UNKNOWN
    [null]                                  | UNKNOWN
  }

}