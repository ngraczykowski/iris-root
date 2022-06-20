/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gender;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gender.TitleGenderDetectorSwitchProperty.isTitleGenderDetectorDisabled;
import static java.util.regex.Pattern.compile;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TitleGenderDetector {

  private static final Pattern SPLIT_PARTS_PATTERN = compile(",\\s*");
  private static final Pattern MALE_PREFIX_PATTERN = compile("^(Mr)\\W.*");
  private static final Pattern FEMALE_PREFIX_PATTERN =
      compile("^(mrs|MRS|Mrs|Ms|miss|MISS|Miss)\\W.*");
  private static final Pattern MALE_RELATION_PATTERN =
      compile("(^|.*\\s)(s/o|S/o|S/O|s/O|h/o|H/o|H/O|h/O)\\W.*");
  private static final Pattern FEMALE_RELATION_PATTERN =
      compile("(^|.*\\s)(d/o|D/o|D/O|d/O|w/o|W/o|W/O|w/O)\\W.*");

  public static Gender detect(@NonNull List<String> names) {
    if (isTitleGenderDetectorDisabled()) {
      return Gender.UNKNOWN;
    }
    List<Gender> genders = names
        .stream()
        .filter(StringUtils::isNotBlank)
        .flatMap(TitleGenderDetector::splitNameParts)
        .map(TitleGenderDetector::detectGenderForNamePart)
        .toList();
    return reduceGenders(genders);
  }

  private static Stream<String> splitNameParts(String name) {
    return Stream.of(SPLIT_PARTS_PATTERN.split(name));
  }

  private static Gender detectGenderForNamePart(String name) {
    if (isFemale(name))
      return Gender.FEMALE;

    if (isMale(name))
      return Gender.MALE;

    return Gender.UNKNOWN;
  }

  private static boolean isFemale(String name) {
    return FEMALE_PREFIX_PATTERN.matcher(name).matches()
        || FEMALE_RELATION_PATTERN.matcher(name).matches();
  }

  private static boolean isMale(String name) {
    return MALE_PREFIX_PATTERN.matcher(name).matches()
        || MALE_RELATION_PATTERN.matcher(name).matches();
  }

  private static Gender reduceGenders(List<Gender> genders) {
    boolean maleMatch = genders.stream().anyMatch(gender -> gender == Gender.MALE);
    boolean femaleMatch = genders.stream().anyMatch(gender -> gender == Gender.FEMALE);

    if (maleMatch && !femaleMatch)
      return Gender.MALE;

    if (femaleMatch && !maleMatch)
      return Gender.FEMALE;

    return Gender.UNKNOWN;
  }
}

