package com.silenteight.sens.webapp.common.util;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class WhitelistPredicate implements StringPredicate {

  private final List<String> whitelist;

  public WhitelistPredicate(List<String> whitelist) {
    this.whitelist = whitelist.stream().map(String::toUpperCase).collect(toList());
  }

  @Override
  public boolean test(String value) {
    if (whitelist.isEmpty())
      return true;

    return whitelist.contains(value.toUpperCase());
  }

  @Override
  public boolean isEnabled() {
    return !whitelist.isEmpty();
  }

  @Override
  public boolean defaultValue() {
    return false;
  }
}
