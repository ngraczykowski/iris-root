package com.silenteight.sens.webapp.backend.presentation.dto.common;

import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
public class FieldEntityDto {

  private final List<FieldDto> fields = new ArrayList<>();

  public void add(@NonNull String name, Object value) {
    fields.add(new FieldDto(name, value));
  }

  public void add(@NonNull FieldDto field) {
    fields.add(field);
  }
}
