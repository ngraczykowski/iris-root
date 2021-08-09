package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.json.external.model.CaseInformation;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static java.time.LocalDate.parse;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@RequiredArgsConstructor
@Slf4j
class AlertTimeCalculator {

  private final DateTimeFormatter formatter;

  public Optional<OffsetDateTime> calculateAlertTime(CaseInformation caseInformation) {
    var updatedDateTime = caseInformation.getUpdatedDateTime();

    return getNewAlertDate(caseInformation).or(() -> getReAlertDate(updatedDateTime))
        .map(l -> l.atStartOfDay(ZoneOffset.UTC).toOffsetDateTime());
  }

  private Optional<LocalDate> getReAlertDate(String updatedDateTime) {
    return safeGetAsDate(updatedDateTime);
  }

  private Optional<LocalDate> getNewAlertDate(CaseInformation caseInformation) {
    var createdDateResult = safeGetAsDate(caseInformation.getCreatedDateTime());
    var modifiedDateResult = safeGetAsDate(caseInformation.getExtendedAttribute13DateTime());

    if (createdDateResult.isEmpty() || modifiedDateResult.isEmpty()) {
      return empty();
    }

    var createdDate = createdDateResult.get();
    var modifiedDate = modifiedDateResult.get();
    return modifiedDate.isAfter(createdDate) ? of(createdDate) : empty();
  }

  private Optional<LocalDate> safeGetAsDate(String date) {
    if (isEmpty(date)) {
      return empty();
    }

    try {
      return of(parse(date.toUpperCase(), formatter));
    } catch (RuntimeException ex) {
      log.error("Invalid date format, value = {}", date, ex);
      return empty();
    }
  }
}
