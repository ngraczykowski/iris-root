package com.silenteight.sens.webapp.backend.presentation.dto.restriction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class EditRestrictionRequest {

  String name;

  @NotNull
  List<String> units = new ArrayList<>();

  @NotNull
  List<String> countries = new ArrayList<>();

  @NotNull
  List<Long> userIds = new ArrayList<>();
}
