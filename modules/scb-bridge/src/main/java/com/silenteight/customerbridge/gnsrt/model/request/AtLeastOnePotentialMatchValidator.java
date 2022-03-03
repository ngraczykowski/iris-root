package com.silenteight.customerbridge.gnsrt.model.request;

import java.util.List;
import javax.annotation.Nullable;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

public final class AtLeastOnePotentialMatchValidator
    implements ConstraintValidator<AtLeastOnePotentialMatch, List<GnsRtAlert>> {

  @Override
  public boolean isValid(
      @Nullable List<GnsRtAlert> gnsRtAlerts, ConstraintValidatorContext context) {

    return emptyIfNull(gnsRtAlerts)
        .stream()
        .anyMatch(GnsRtAlert::isPotentialMatch);
  }
}
