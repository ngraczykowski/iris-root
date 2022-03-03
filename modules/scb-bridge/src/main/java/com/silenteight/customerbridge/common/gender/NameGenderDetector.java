package com.silenteight.customerbridge.common.gender;

import lombok.NonNull;

import java.util.Collection;
import java.util.List;

import static com.silenteight.customerbridge.common.gender.Gender.FEMALE;
import static com.silenteight.customerbridge.common.gender.Gender.MALE;
import static com.silenteight.customerbridge.common.gender.Gender.UNKNOWN;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * Detects gender from list of single-token names.
 *
 * <p>Uses dictionaries with male and female names to determine gender.
 */
class NameGenderDetector {

  private static final int USE_LIST_ELEMENTS_THRESHOLD = 1_000;

  private final Collection<String> maleNames;
  private final Collection<String> femaleNames;

  NameGenderDetector(NameGenderData nameGenderData) {
    this.maleNames = prepareCollection(nameGenderData.getMaleNames());
    this.femaleNames = prepareCollection(nameGenderData.getFemaleNames());
  }

  private static Collection<String> prepareCollection(List<String> names) {
    // NOTE(ahaczewski): It is more efficient to search ArrayList when there are less than 1000
    //  elements, otherwise using HashSet is better suited for contains() call.
    if (names.size() < USE_LIST_ELEMENTS_THRESHOLD) {
      // NOTE(ahaczewski): It is more efficient to use ArrayList#sort() instead of sorted()
      //  stream call, because sorting already created array is less memory hungry than
      //  sorting lazy stream (requires double the size of memory to do so).
      //noinspection FuseStreamOperations
      List<String> list = names.stream().map(String::toLowerCase).collect(toList());
      list.sort(String::compareTo);
      return list;
    } else {
      return names.stream().map(String::toLowerCase).collect(toSet());
    }
  }

  Gender detect(@NonNull List<String> names) {
    int inputSize = names.size();
    boolean containsMale = false;
    boolean containsFemale = false;

    // NOTE(ahaczewski): Using classic for-loop to avoid creating list iterator.
    //  Would be inefficient for `names` being linked list, but that is not the case.
    //noinspection ForLoopReplaceableByForEach
    for (int nameIndex = 0; nameIndex < inputSize; nameIndex++) {
      String name = names.get(nameIndex).toLowerCase();

      containsMale |= maleNames.contains(name);
      containsFemale |= femaleNames.contains(name);

      if (containsMale && containsFemale)
        break;
    }

    if (containsMale && !containsFemale)
      return MALE;
    else if (containsFemale && !containsMale)
      return FEMALE;
    else
      return UNKNOWN;
  }
}
