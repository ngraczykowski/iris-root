import com.silenteight.payments.bridge.svb.oldetl.model.CreateAlertedPartyEntitiesRequest
import com.silenteight.payments.bridge.svb.oldetl.port.CreateAlertedPartyEntitiesUseCase
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure
import com.silenteight.payments.bridge.svb.oldetl.service.CreateAlertPartyEntities

import spock.lang.Specification

import static com.silenteight.payments.bridge.agents.model.AlertedPartyKey.*

class CreateAlertPartyEntitiesTestSpec extends Specification {

  private final CreateAlertedPartyEntitiesUseCase createAlertedPartyEntitiesUseCase =
      new CreateAlertPartyEntities()

  def "should extract alerted party names and addresses from #allMatchingTexts"() {
    expect:
    def request = CreateAlertedPartyEntitiesRequest.builder()
        .alertedPartyData(
            createAlertedPartyData(
                names, ctryTowns, addresses, nameAddresses, messageFieldStructure))
        .allMatchingText(allMatchingTexts)
        .build()
    createAlertedPartyEntitiesUseCase.create(request) == expectedAlertedPartyEnities

    where:
    allMatchingTexts                                                              | names |
        addresses                                                                                             |
        ctryTowns                         |
        nameAddresses                                            |
        messageFieldStructure                                           ||
        expectedAlertedPartyEnities

    ["BUSIN\r\n2/ESS"]                                                            |
        ["ABC PVT"]                                                                       |
        ["LTD SERCO HOUSE 16 BARTLEY WOOD BUSINESS PARK BARTLEY WAY"]                                         |
        ["CTRYTOWN"]                      |
        [""]                                                     |
        MessageFieldStructure.NAMEADDRESS_FORMAT_F                      ||
        Map.of(ALERTED_ADDRESS_KEY, "LTD SERCO HOUSE 16 BARTLEY WOOD BUSINESS PARK BARTLEY WAY")

    ["BUSIN\n2/ESS"]                                                              |
        ["ABC PVT"]                                                                       |
        ["LTD SERCO HOUSE 16 BARTLEY WOOD BUSINESS PARK BARTLEY WAY"]                                         |
        ["CTRYTOWN"]                      |
        [""]                                                     |
        MessageFieldStructure.NAMEADDRESS_FORMAT_F                      ||
        Map.of(ALERTED_ADDRESS_KEY, "LTD SERCO HOUSE 16 BARTLEY WOOD BUSINESS PARK BARTLEY WAY")

    ["Pa \r\n wel Przyb\nylek"]                                                   |
        ["PA  WEL PRZYBYLEK"]                                                             |
        ["ALFA BETA STREET"]                                                                                  |
        ["PL/ZAMEK"]                      |
        [""]                                                     | null ||
        Map.of(ALERTED_NAME_KEY, "PA  WEL PRZYBYLEK")

    ["Pa \r wel Przyb\r\nylek"]                                                   |
        ["PA  WEL PRZYBYLEK"]                                                             |
        ["ALFA BETA STREET"]                                                                                  |
        ["PL/ZAMEK"]                      |
        [""]                                                     | null ||
        Map.of(ALERTED_NAME_KEY, "PA  WEL PRZYBYLEK")

    ["Pa \n wel Przyb\nylek"]                                                     |
        ["PA  WEL PRZYBYLEK"]                                                             |
        ["ALFA BETA STREET"]                                                                                  |
        ["PL/ZAMEK"]                      |
        [""]                                                     | null ||
        Map.of(ALERTED_NAME_KEY, "PA  WEL PRZYBYLEK")

    ["Pa \n\r\n \nwel Przyb\nylek"]                                               |
        ["PA  WEL PRZYBYLEK"]                                                             |
        ["ALFA BETA STREET"]                                                                                  |
        ["PL/ZAMEK"]                      |
        [""]                                                     | null ||
        Map.of(ALERTED_NAME_KEY, "PA  WEL PRZYBYLEK")

    ["\nPEENYA"]                                                                  |
        ["ACE MANUFACTURING SYSTEMS LIMITED"]                                             |
        ["PLOT NO 467 TO 469 12 CROSS 4 PHS,PEENYA INDUSTRIAL AREA BANGALORE KARNATAKA58/INDIA"]              |
        ["KARNATAKA58/INDIA"]             |
        [""]                                                     | null ||
        Map.of(
            ALERTED_ADDRESS_KEY,
            "PLOT NO 467 TO 469 12 CROSS 4 PHS,PEENYA INDUSTRIAL AREA BANGALORE KARNATAKA58/INDIA")

    ["ICICQ"]                                                                     |
        ["ICICQ BANK LTD"]                                                                |
        ["IFSCICICQ0001874 CITY NASIK, BRANCH INDIRANAGAR INDIA"]                                             |
        [""]                              |
        [""]                                                     | null ||
        Map.of(
            ALERTED_ADDRESS_KEY, "IFSCICICQ0001874 CITY NASIK, BRANCH INDIRANAGAR INDIA",
            ALERTED_NAME_KEY, "ICICQ BANK LTD")

    ["ICICQ"]                                                                     |
        ["Icicq Bank Ltd"]                                                                |
        ["IFSCICICQ0001874 CITY NASIK, BRANCH INDIRANAGAR INDIA"]                                             |
        [""]                              |
        [""]                                                     | null ||
        Map.of(
            ALERTED_ADDRESS_KEY, "IFSCICICQ0001874 CITY NASIK, BRANCH INDIRANAGAR INDIA",
            ALERTED_NAME_KEY, "Icicq Bank Ltd")

    ["JOHL"]                                                                      |
        ["SMITH JOHN"]                                                                    |
        ["299, PARK AVENUE"]                                                                                  |
        ["US/NEW YORK, NY 10017"]         |
        [""]                                                     | null ||
        Map.of(NO_MATCH, "JOHL")

    ["Pawel Przyb\rylek"]                                                         |
        ["PAWEL PRZYBYLEK"]                                                               |
        ["ALFA BETA STREET"]                                                                                  |
        ["PL/ZAMEK"]                      |
        [""]                                                     | null ||
        Map.of(ALERTED_NAME_KEY, "PAWEL PRZYBYLEK")

    ["Pawel Przyb\n1/ylek"]                                                       |
        ["PAWEL PRZYBYLEK"]                                                               |
        ["ALFA BETA STREET"]                                                                                  |
        ["PL/ZAMEK"]                      |
        [""]                                                     |
        MessageFieldStructure.NAMEADDRESS_FORMAT_F                      ||
        Map.of(ALERTED_NAME_KEY, "PAWEL PRZYBYLEK")

    ["Pawel Przyb\n2/ylek"]                                                       |
        ["PAWEL PRZYBYLEK"]                                                               |
        ["ALFA BETA STREET"]                                                                                  |
        ["PL/ZAMEK"]                      |
        [""]                                                     |
        MessageFieldStructure.NAMEADDRESS_FORMAT_F                      ||
        Map.of(ALERTED_NAME_KEY, "PAWEL PRZYBYLEK")

    ["Pawel Przyb\n6/ylek"]                                                       |
        ["PAWEL PRZYBYLEK"]                                                               |
        ["ALFA BETA STREET"]                                                                                  |
        ["PL/ZAMEK"]                      |
        [""]                                                     |
        MessageFieldStructure.NAMEADDRESS_FORMAT_F                      ||
        Map.of(ALERTED_NAME_KEY, "PAWEL PRZYBYLEK")

    ["Pawel Przyb\n6/ylek"]                                                       |
        ["PAWEL PRZYBYLEK"]                                                               |
        ["ALFA BETA STREET"]                                                                                  |
        ["PL/ZAMEK"]                      |
        [""]                                                     |
        MessageFieldStructure.NAMEADDRESS_FORMAT_F                      ||
        Map.of(ALERTED_NAME_KEY, "PAWEL PRZYBYLEK")

    ["Pawel Przyb\n8/\n1/ylek"]                                                   |
        ["PAWEL PRZYBYLEK"]                                                               |
        ["ALFA BETA STREET"]                                                                                  |
        ["PL/ZAMEK"]                      |
        [""]                                                     |
        MessageFieldStructure.NAMEADDRESS_FORMAT_F                      ||
        Map.of(ALERTED_NAME_KEY, "PAWEL PRZYBYLEK")

    ["P\n8/\n1/\n8/\n1/awel Przyb\n2/ylek"]                                       |
        ["PAWEL PRZYBYLEK"]                                                               |
        ["ALFA BETA STREET"]                                                                                  |
        ["PL/ZAMEK"]                      |
        [""]                                                     |
        MessageFieldStructure.NAMEADDRESS_FORMAT_F                      ||
        Map.of(ALERTED_NAME_KEY, "PAWEL PRZYBYLEK")

    ["Pa\n3/\n6/wel Przyb\n5/ylek"]                                               |
        ["PAWEL PRZYBYLEK"]                                                               |
        ["ALFA BETA STREET"]                                                                                  |
        ["PL/ZAMEK"]                      |
        [""]                                                     |
        MessageFieldStructure.NAMEADDRESS_FORMAT_F                      ||
        Map.of(ALERTED_NAME_KEY, "PAWEL PRZYBYLEK")

    ["Pa--!wel Przyb\rylek"]                                                      |
        ["PA--!WEL PRZYBYLEK"]                                                            |
        ["ALFA BETA STREET"]                                                                                  |
        ["PL/ZAMEK"]                      |
        [""]                                                     | null ||
        Map.of(ALERTED_NAME_KEY, "PA--!WEL PRZYBYLEK")

    ["QJOHNE"]                                                                    |
        ["QSMITH QJOHNE"]                                                                 |
        ["299, PARK AVENUE"]                                                                                  |
        ["US/NEW YORK, NY 10017"]         |
        [""]                                                     | null ||
        Map.of(ALERTED_NAME_KEY, "QSMITH QJOHNE")

    ["JOHNE   "]                                                                  |
        ["SMITH JOHNE  "]                                                                 |
        ["299, PARK AVENUE"]                                                                                  |
        ["US/NEW YORK, NY 10017"]         |
        [""]                                                     | null ||
        Map.of(ALERTED_NAME_KEY, "SMITH JOHNE  ")

    ["BO\n\nB   "]                                                                |
        ["SMITH JOHNE  "]                                                                 |
        ["299, PARK AVENUE"]                                                                                  |
        ["US/NEW YORK, NY 10017"]         |
        [""]                                                     | null ||
        Map.of(NO_MATCH, "BO\n\nB")

    ["BO\n\n   B   "]                                                             |
        ["SMITH JOHNE  "]                                                                 |
        ["299, PARK AVENUE"]                                                                                  |
        ["US/NEW YORK, NY 10017"]         |
        [""]                                                     | null ||
        Map.of(NO_MATCH, "BO\n\n   B")

    ["JO\nH\nN"]                                                                  |
        ["SMITH JOHN"]                                                                    |
        ["299, PARK AVENUE"]                                                                                  |
        ["US/NEW YORK, NY 10017"]         |
        [""]                                                     | null ||
        Map.of(ALERTED_NAME_KEY, "SMITH JOHN")

    ["JOHN"]                                                                      |
        ["SMITH JOHN"]                                                                    |
        ["299, PARK AVENUE"]                                                                                  |
        ["US/NEW YORK, NY 10017"]         |
        [""]                                                     | null ||
        Map.of(ALERTED_NAME_KEY, "SMITH JOHN")

    ["Pawel Przybylek"]                                                           |
        ["PAWEL PRZYBYLEK"]                                                               |
        ["ALFA BETA STREET"]                                                                                  |
        ["PL/ZAMEK"]                      |
        [""]                                                     | null ||
        Map.of(ALERTED_NAME_KEY, "PAWEL PRZYBYLEK")

    ["PEENYA"]                                                                    |
        ["ACE MANUFACTURING SYSTEMS LIMITED"]                                             |
        ["PLOT NO 467 TO 469 12 CROSS 4 PHS,PEENYA INDUSTRIAL AREA BANGALORE KARNATAKA58/INDIA"]              |
        ["KARNATAKA58/INDIA"]             |
        [""]                                                     | null ||
        Map.of(
            ALERTED_ADDRESS_KEY,
            "PLOT NO 467 TO 469 12 CROSS 4 PHS,PEENYA INDUSTRIAL AREA BANGALORE KARNATAKA58/INDIA")

    ["OSAMA"]                                                                     |
        ["ACE MANUFACTURING SYSTEMS LIMITED"]                                             |
        ["PLOT NO 467 TO 469 12 CROSS 4 PHS,PEENYA INDUSTRIAL AREA BANGALORE KARNATAKA58/INDIA"]              |
        ["KARNATAKA58/INDIA"]             |
        [""]                                                     | null ||
        Map.of(NO_MATCH, "OSAMA")

    ["BOGAWANTALAWA", "BETA"]                                                     |
        ["BOGAWANTALAWA PVT LTD"]                                                         |
        ["ALFA BETA STREET"]                                                                                  |
        ["KARNATAKA58/INDIA"]             |
        [""]                                                     | null ||
        Map.of(
            ALERTED_ADDRESS_KEY, "ALFA BETA STREET",
            ALERTED_NAME_KEY, "BOGAWANTALAWA PVT LTD")

    ["ALLA"]                                                                      |
        ["BOGAWANTALAWA PVT LTD"]                                                         |
        ["Alfa Beta STREET ALLA"]                                                                             |
        ["US"]                            |
        [""]                                                     | null ||
        Map.of(ALERTED_ADDRESS_KEY, "Alfa Beta STREET ALLA")

    ["TALAL ABU-GHAZALEH INTERNATIONAL", "TAGI HOUSE", "NO 26 PRINCE SHAKER BIN"] |
        ["ABC PVT LTD"]                                                                   |
        ["921100, AMMAN 11192 JORDAN TAGI HOUSE, NO 26 PRINCE SHAKER BIN ZAID STREET, SHEMISANI/JORDAN"]      |
        ["SHEMISANI/JORDAN"]              |
        [""]                                                     | null ||
        Map.of(
            ALERTED_ADDRESS_KEY,
            "921100, AMMAN 11192 JORDAN TAGI HOUSE, NO 26 PRINCE SHAKER BIN ZAID STREET, SHEMISANI/JORDAN",
            NO_MATCH, "TALAL ABU-GHAZALEH INTERNATIONAL")

    ["SAUDI ARABIA", "SA"]                                                        |
        ["GERALD YAP"]                                                                    |
        ["HO RIYADH KINGDOM OF SAUDI ARABIA P.O. BOX 56006 RIYADH"]                                           |
        ["SA/HO RIYADH KINGDOM OF SAUDI"] |
        [""]                                                     | null ||
        Map.of(
            ALERTED_ADDRESS_KEY, "HO RIYADH KINGDOM OF SAUDI ARABIA P.O. BOX 56006 RIYADH",
            ALERTED_COUNTRY_TOWN_KEY, "SA/HO RIYADH KINGDOM OF SAUDI")

    ["SABA"]                                                                      |
        ["SABA INTERNATIONAL"]                                                            |
        ["COMPANY 4TH FLOOR SABA ISLAMIC BUILDING OPP NOUGAPRIX SUPERMARKET REPUBLIC OF DJIBOUTI / DJIBOUTI"] |
        [""]                              |
        [""]                                                     | null ||
        Map.of(
            ALERTED_ADDRESS_KEY,
            "COMPANY 4TH FLOOR SABA ISLAMIC BUILDING OPP NOUGAPRIX SUPERMARKET REPUBLIC OF DJIBOUTI / DJIBOUTI",
            ALERTED_NAME_KEY, "SABA INTERNATIONAL")

    ["ICIC"]                                                                      |
        ["ICIC BANK LTD"]                                                                 |
        ["IFSCICIC0001874 CITY NASIK, BRANCH INDIRANAGAR INDIA"]                                              |
        [""]                              |
        [""]                                                     | null ||
        Map.of(
            ALERTED_ADDRESS_KEY, "IFSCICIC0001874 CITY NASIK, BRANCH INDIRANAGAR INDIA",
            ALERTED_NAME_KEY, "ICIC BANK LTD")

    ["Aviation Leasing"]                                                          |
        ["X Aviation Leasing Co LLC"]                                                     | [""]              |
        [""]                              | ["Aviation Leasing"] | null ||
        Map.of(ALERTED_NAME_KEY, "X Aviation Leasing Co LLC")
  }

  private static def createAlertedPartyData(
      List<String> names, List<String> countryTowns, List<String> addresses,
      List<String> nameAddresses,
      MessageFieldStructure messageFieldStructure) {
    return AlertedPartyData.builder()
        .names(names)
        .ctryTowns(countryTowns)
        .addresses(addresses)
        .nameAddress(nameAddresses)
        .messageFieldStructure(messageFieldStructure)
        .build()
  }

}
