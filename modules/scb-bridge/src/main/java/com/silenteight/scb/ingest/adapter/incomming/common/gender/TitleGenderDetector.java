package com.silenteight.scb.ingest.adapter.incomming.common.gender;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.regex.Pattern.compile;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TitleGenderDetector {

  private static final Pattern SPLIT_PARTS_PATTERN = compile(",\\s*");

  private static final Pattern MALE_PREFIX_PATTERN = compile("^(mr)\\W.*");
  private static final Pattern FEMALE_PREFIX_PATTERN = compile("^(mrs|ms|miss)\\W.*");

  private static final Pattern MALE_RELATION_PATTERN = compile("(^|.*\\s)(s/o|h/o)\\W.*");
  private static final Pattern FEMALE_RELATION_PATTERN = compile("(^|.*\\s)(d/o|w/o)\\W.*");

  public static Gender detect(@NonNull List<String> names) {
    List<Gender> genders = names
        .stream()
        .filter(StringUtils::isNotBlank)
        .map(String::toLowerCase)
        .flatMap(TitleGenderDetector::splitNameParts)
        .map(TitleGenderDetector::detectGenderForNamePart)
        .collect(Collectors.toList());
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

