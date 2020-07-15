package com.silenteight.sens.auth.authentication;

import java.util.regex.Pattern;

public class WebappRoleNameNormalizer implements AuthorityNameNormalizer {

  private static final Pattern NON_ALPHA_NUMERIC =
      Pattern.compile("[^A-Za-z0-9]");

  @Override
  public String apply(String roleName) {
    return "ROLE_" + NON_ALPHA_NUMERIC.matcher(roleName)
        .replaceAll("_").toUpperCase();
  }
}
