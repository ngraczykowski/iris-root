package com.silenteight.sep.base.common.support.jackson;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.toList;

final class JacksonModuleFinder {

  private static final Set<String> DEFAULT_ALLOWED_MODULES = Set.of(
      "com.fasterxml.jackson.datatype.jsr310.JavaTimeModule",
      "com.fasterxml.jackson.datatype.jdk8.Jdk8Module",
      "com.fasterxml.jackson.module.paramnames.ParameterNamesModule");

  static Collection<Module> findModules() {
    return ObjectMapper
        .findModules()
        .stream()
        .filter(JacksonModuleFinder::isAllowed)
        .collect(toList());
  }

  private static boolean isAllowed(Module module) {
    return DEFAULT_ALLOWED_MODULES.contains(module.getClass().getName());
  }

  private JacksonModuleFinder() {
  }
}
