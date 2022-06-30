package com.silenteight.warehouse.statistics.get;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor(staticName = "of")
public class StatisticsRequest {

  @NonNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  public LocalDate from;

  @NonNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  public LocalDate to;
}
