/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gnsparty;

import lombok.Builder;
import lombok.Value;

import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Value
@Builder
class IdentificationPattern {

  String pattern;
  boolean caseSensitive;

  static Set<Pattern> compileAll(IdentificationPattern... patterns) {
    return stream(patterns).map(IdentificationPattern::compile).collect(Collectors.toSet());
  }

  Pattern compile() {
    var flags = caseSensitive ? 0 : Pattern.CASE_INSENSITIVE;
    var regex = "(^|.*\\W)" + pattern + "(\\W.*|$)";
    return Pattern.compile(regex, flags);
  }
}
