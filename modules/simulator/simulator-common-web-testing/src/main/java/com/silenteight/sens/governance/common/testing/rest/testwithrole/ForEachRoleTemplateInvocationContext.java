package com.silenteight.sens.governance.common.testing.rest.testwithrole;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;

import java.util.List;

import static java.lang.String.format;
import static java.util.Collections.singletonList;

@RequiredArgsConstructor
class ForEachRoleTemplateInvocationContext
    implements BeforeEachCallback, TestTemplateInvocationContext {

  private final String roleName;
  private final SecurityContextRoleSetter securityContextRoleSetter;
  private final String testName;

  @Override
  public void beforeEach(ExtensionContext context) {
    securityContextRoleSetter.setRoles(singletonList(roleName));
  }

  @Override
  public String getDisplayName(int invocationIndex) {
    return format("[%s] %s (%s)", roleName, testName, invocationIndex);
  }

  @Override
  public List<Extension> getAdditionalExtensions() {
    return singletonList(this);
  }
}
