package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.json.external.model.CaseInformation;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
class AlertTimeCalculator {

  private final DateTimeFormatter formatter;

  public Optional<OffsetDateTime> calculateAlertTime(CaseInformation caseInformation) {
    var updatedDateTime = caseInformation.getUpdatedDateTime();

    return getNewAlertDate(caseInformation).or(() -> getReAlertDate(updatedDateTime))
        .map(l -> l.atOffset(ZoneOffset.UTC));
  }

  private Optional<LocalDateTime> getReAlertDate(String updatedDateTime) {
    return safeGetAsDate(updatedDateTime);
  }

  private Optional<LocalDateTime> getNewAlertDate(CaseInformation caseInformation) {
    var createdDateResult = safeGetAsDate(caseInformation.getCreatedDateTime());
    var modifiedDateResult = safeGetAsDate(caseInformation.getExtendedAttribute13DateTime());

    if (createdDateResult.isEmpty() || modifiedDateResult.isEmpty()) {
      return Optional.empty();
    }

    var createdDate = createdDateResult.get();
    var modifiedDate = modifiedDateResult.get();
    return modifiedDate.isAfter(createdDate) ? Optional.of(createdDate) : Optional.empty();
  }

  private Optional<LocalDateTime> safeGetAsDate(String date) {
    if (StringUtils.isEmpty(date)) {
      return Optional.empty();
    }

    try {
      return Optional.of(LocalDateTime.parse(date.toUpperCase(), formatter));
    } catch (RuntimeException ex) {
      log.error("Invalid date format, value = {}", date, ex);
      return Optional.empty();
    }
  }
}
