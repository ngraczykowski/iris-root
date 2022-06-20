/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.request;

import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
import java.util.Locale.IsoCountryCode;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
public final class PotentialMatchAlertIdValidator
    implements ConstraintValidator<HasValidAlertId, GnsRtAlert> {

  // e.x alert id: VN_SCIC_PEPL!FFDFE79E-60E74990-88C8F551-B058659F
  // note, the data after ! is not UUID, it only looks like one

  private static final Pattern ALERT_ID_PATTERN =
      Pattern.compile("(?<countryCode>.+?)_(?<unitIdSuffix>.+)!(?<recordId>[0-9a-fA-F-]{35})");

  private static final Set<String> COUNTRY_CODES_ISO_ALPHA2 =
      Locale.getISOCountries(IsoCountryCode.PART1_ALPHA2);

  @Override
  public boolean isValid(@Nonnull GnsRtAlert gnsRtAlert, ConstraintValidatorContext context) {
    if (!gnsRtAlert.isPotentialMatch())
      return true;
    return isValidAlertId(gnsRtAlert.getAlertId());
  }

  private static boolean isValidAlertId(String alertId) {
    if (isBlank(alertId)) {
      log.warn("Invalid alertId: empty");
      return false;
    }

    Matcher matcher = ALERT_ID_PATTERN.matcher(alertId);
    if (!matcher.matches()) {
      log.warn("Invalid alertId: '{}' - does not match AlertId RegExp Pattern", alertId);
      return false;
    }

    if (!isValidCountryCode(matcher.group("countryCode"))) {
      log.warn(
          "Invalid alertId: '{}' - unitId part does not start with valid"
              + " Country Code in ISO format",
          alertId);
      return false;
    }

    if (!isValidUnitIdSuffix(matcher.group("unitIdSuffix"))) {
      log.warn("Invalid alertId: '{}' - unitIdSuffix does not contain '_' character", alertId);
      return false;
    }

    return true;
  }

  private static boolean isValidCountryCode(String countryCode) {
    return COUNTRY_CODES_ISO_ALPHA2.contains(countryCode);
  }

  private static boolean isValidUnitIdSuffix(String unitIdSuffix) {
    return unitIdSuffix.contains("_");
  }

}
