package com.silenteight.sep.auth.authentication;

import java.util.regex.Pattern;

@Deprecated
public class WebappAuthorityNameNormalizer implements AuthorityNameNormalizer {

  private static final Pattern NON_ALPHA_NUMERIC =
      Pattern.compile("[^A-Za-z0-9]");

  @Override
  public String apply(String authorityName) {
    return NON_ALPHA_NUMERIC.matcher(authorityName).replaceAll("_").toUpperCase();
  }
}
