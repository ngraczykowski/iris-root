package com.silenteight.customerbridge.common.gnsparty;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecordParser {

  private static final int SOURCE_SYSTEM_IDENTIFIER_INDEX = 0;
  private static final Pattern CLOB_PATTERN = Pattern.compile("(CLOB)", Pattern.LITERAL);

  public static GnsParty parse(String systemId, char charSep, String fmtName, String record) {
    try {
      return createGnsParty(record, charSep);
    } catch (Exception e) {
      RecordInformation recordInformation = RecordParser.getRecordInformation(charSep, record);
      log.error(
          "Could not parse record: "
              + "systemId={}, fmtName={}, numberOfColumns={}, sourceSystemIdentifier={}, error={}",
          systemId,
          fmtName,
          recordInformation.getNumberOfColumns(),
          recordInformation.getSourceSystemIdentifier(),
          e.getMessage());
    }
    return GnsParty.empty();
  }

  static RecordInformation getRecordInformation(char charSep, String record) {
    String[] values = getCleanedValues(record, charSep);
    return new RecordInformation(values.length, values[SOURCE_SYSTEM_IDENTIFIER_INDEX]);
  }

  private static GnsParty createGnsParty(String record, char separator) {
    if (hasNoDataToParse(record))
      return GnsParty.empty();

    String[] values = getCleanedValues(record, separator);

    return PartyCreatorProvider
        .getCreatorsByColumnCount(record, separator)
        .map(creators -> parse(creators, values))
        .orElseThrow(() -> new IllegalArgumentException("Could not find parser"));

  }

  private static GnsParty parse(List<GnsPartyCreator> creators, String[] values) {
    return creators
        .stream()
        .filter(creator -> creator.supports(values))
        .findFirst()
        .map(creator -> creator.create(values))
        .orElseThrow(() -> new IllegalArgumentException("Wrong number of columns"));
  }

  private static boolean hasNoDataToParse(String record) {
    return StringUtils.isEmpty(record);
  }

  private static String[] getCleanedValues(String record, char separator) {
    String[] result = record.split(String.valueOf(separator), -1);
    if (result[0].startsWith("(CLOB)"))
      result[0] = CLOB_PATTERN.matcher(result[0]).replaceAll(Matcher.quoteReplacement("")).trim();
    return result;
  }
}
