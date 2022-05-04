package com.silenteight.warehouse.statistics.get;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor(staticName = "of")
public class StatisticsRequest {

  @NotNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  public LocalDate from;

  @NotNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  public LocalDate to;
}
