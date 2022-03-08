package com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request;

import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

public final class PotentialMatchAlertIdValidator
    implements ConstraintValidator<AllPotentialMatchesHasValidAlertId, List<GnsRtAlert>> {

  private static final Pattern CONTAINS_UNIT_SEPARATOR = Pattern.compile("(.)+!(.)+");

  @Override
  public boolean isValid(
      @Nullable List<GnsRtAlert> gnsRtAlerts, ConstraintValidatorContext context) {
    return emptyIfNull(gnsRtAlerts)
        .stream()
        .filter(GnsRtAlert::isPotentialMatch)
        .map(GnsRtAlert::getAlertId)
        .allMatch(PotentialMatchAlertIdValidator::isValidAlertId);
  }

  private static boolean isValidAlertId(String alertId) {
    if (isBlank(alertId))
      return false;

    return CONTAINS_UNIT_SEPARATOR.matcher(alertId).matches();
  }
}
