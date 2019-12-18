package com.silenteight.sens.webapp.common.support.jackson;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.commons.collections.SetBuilder;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class JacksonModuleFinder {

  private static final Set<String> DEFAULT_ALLOWED_MODULES = SetBuilder.of(
      // FIXME(ahaczewski): Try to replace with
      //  "com.fasterxml.jackson.datatype.jsr310.JavaTimeModule", because JSR310Module is
      //  deprecated, but SENS codebase depends on ZonedDateTime serialization, which is different
      //  between these two modules.
      "com.fasterxml.jackson.datatype.jsr310.JSR310Module",
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
}

