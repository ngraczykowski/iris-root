package com.silenteight.customerbridge.common.alertrecord;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Represents alert record read from GNS database.
 *
 * <p>Please take a note, that some {@code @javax.annotation.Nullable} fields, have
 * {@code @javax.validation.constraints.NotNull} annotations. {@code javax.annotation} annotations
 * serve as programming-level assumptions, because database allows those fields to carry NULL
 * values. {@code javax.validation.constraints} annotations serve for logically validating external
 * database input.
 */
@Value
@Builder
public class AlertRecord {

  @NonNull
  String systemId;

  @Nullable
  String unit;

  @Nullable
  String dbAccount;

  @Nullable
  @NotNull
  @Size(min = 1)
  String batchId;

  @Nullable
  String typeOfRec;

  @Nullable
  String details;

  @Nullable
  String dbCity;

  @Nullable
  String dbCountry;

  @Nullable
  String dbDob;

  @Nullable
  String dbName;

  @Nullable
  String dbPob;

  @Nullable
  String filteredString;

  @Nullable
  String recordId;

  @Nullable
  String lastDecBatchId;

  @Nullable
  String fmtName;

  @Nullable
  String record;

  char charSep;
}
