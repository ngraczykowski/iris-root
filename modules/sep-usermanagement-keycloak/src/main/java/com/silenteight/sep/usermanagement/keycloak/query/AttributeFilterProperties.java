package com.silenteight.sep.usermanagement.keycloak.query;

import lombok.Data;

import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import javax.validation.constraints.NotNull;

import static java.util.stream.Collectors.toList;

@Data
@Validated
@ConstructorBinding
class AttributeFilterProperties {

  private final List<AttributeProperties> attributes;

  public List<Attribute> getAttributeFilters() {
    return getAttributes()
        .stream()
        .map(AttributeProperties::toAttribute)
        .collect(toList());
  }

  @Data
  @Validated
  @ConstructorBinding
  public static class AttributeProperties {

    @NotNull
    private final String name;
    @NotNull
    private final String value;

    Attribute toAttribute() {
      return new Attribute(getName(), getValue());
    }
  }
}
