/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gender;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nullable;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class NameSeparatingGenderDetector {

  private final NameGenderDetector genderDetector;

  public static NameSeparatingGenderDetector getDefault() {
    NameGenderDetector genderDetector = new NameGenderDetector(ResourcesNameGenderData.create());
    return new NameSeparatingGenderDetector(genderDetector);
  }

  public Gender detect(@NonNull List<String> names) {
    return genderDetector.detect(getSingleNameList(names));
  }

  private static List<String> getSingleNameList(List<String> names) {
    return names
        .stream()
        .flatMap(name -> Stream.of(safeSplit(name)))
        .collect(toList());
  }

  private static String[] safeSplit(@Nullable String name) {
    if (name != null) {
      String notAlphanumericOrQuotesRegex = "[^a-zA-Z0-9'\"]";
      return name.split(notAlphanumericOrQuotesRegex);
    } else {
      return new String[0];
    }
  }
}
