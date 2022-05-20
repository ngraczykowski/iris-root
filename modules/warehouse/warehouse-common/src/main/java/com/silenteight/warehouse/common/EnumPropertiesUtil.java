package com.silenteight.warehouse.common;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Helper to convert mappers from properties file to appropriate Enum.
 */
@UtilityClass
public final class EnumPropertiesUtil {

  public static <T extends Enum> Map<String, T> mapPropertiesToEnum(
      Class<T> enumClass, Map<String, List<String>> values) {
    return values.entrySet()
        .stream()
        .map(map -> EnumPropertiesUtil.revertKeyValuesInMap(enumClass, map))
        .flatMap(map -> map.entrySet().stream())
        .collect(Collectors.toMap(
            Entry::getKey,
            Entry::getValue));
  }

  private static <T extends Enum> Map<String, T> revertKeyValuesInMap(
      Class<T> enumClass,
      Entry<String, List<String>> entry) {
    return entry
        .getValue()
        .stream()
        .collect(
            Collectors.toMap(
                Function.identity(), v -> (T) Enum.valueOf(enumClass, entry.getKey())));
  }
}
