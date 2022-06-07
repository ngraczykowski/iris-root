/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package utils;

import io.restassured.authentication.NoAuthScheme;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.spi.AuthFilter;

public class CustomAuthFilter implements AuthFilter {

  @Override
  public Response filter(
      FilterableRequestSpecification filterableRequestSpecification,
      FilterableResponseSpecification filterableResponseSpecification,
      FilterContext filterContext) {
    if (filterableRequestSpecification.getAuthenticationScheme() instanceof NoAuthScheme) {
      filterableRequestSpecification.auth().oauth2(AuthUtils.getAuthTokenHeaderForDefaultUser());
    }
    return filterContext.next(filterableRequestSpecification, filterableResponseSpecification);
  }
}
