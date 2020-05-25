package com.silenteight.serp.common.messaging.metrics;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
class Size {

  SizeUnit unit;
  double value;

  Size(SizeUnit unit, double value) {
    this.unit = unit;
    this.value = value;
  }

  Size convert(SizeUnit unit) {
    double multiplier = (double) this.unit.getMultiplier() / unit.getMultiplier();
    return new Size(unit, value * multiplier);
  }

  long getBytes() {
    return (long) (unit.getMultiplier() * value);
  }

  @RequiredArgsConstructor
  enum SizeUnit {
    BYTES("Bytes", "B", 1),
    //Binary
    KIBIBYTES("Kibibytes", "KiB", 1024),
    MEBIBYTES("Mebibytes", "MiB", 1024 * 1024),
    GIBIBYTES("Gibibytes", "GiB", 1024 * 1024 * 1024),
    //Decimal
    KILOBYTES("Kilobytes", "kB", 1000),
    MEGABYTES("Megabytes", "MB", 1000 * 1000),
    GIGABYTES("Gigabytes", "GB", 1000 * 1000 * 1000);

    @NonNull
    @Getter
    private final String name;
    @NonNull
    @Getter
    private final String abbreviation;
    @Getter
    private final long multiplier;
  }
}
