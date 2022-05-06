package com.silenteight.sep.auth.authentication;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import static java.util.Collections.enumeration;
import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

class XssRequestWrapper extends HttpServletRequestWrapper {

  public XssRequestWrapper(HttpServletRequest servletRequest) {
    super(servletRequest);
  }

  @Override
  public String[] getParameterValues(String parameter) {
    String[] values = super.getParameterValues(parameter);

    if (values == null) {
      return null;
    }

    int count = values.length;
    String[] encodedValues = new String[count];
    for (int i = 0; i < count; i++) {
      encodedValues[i] = stripXss(values[i]);
    }

    return encodedValues;
  }

  @Override
  public String getParameter(String parameter) {
    String value = super.getParameter(parameter);
    return stripXss(value);
  }

  @Override
  public Enumeration<String> getHeaders(String name) {
    List<String> result = new ArrayList<>();
    Enumeration<String> headers = super.getHeaders(name);
    while (headers.hasMoreElements()) {
      String header = headers.nextElement();
      String[] tokens = header.split(",");
      for (String token : tokens) {
        result.add(stripXss(token));
      }
    }
    return enumeration(result);
  }

  @Override
  public String getHeader(String name) {
    String value = super.getHeader(name);
    return stripXss(value);
  }

  private String stripXss(String value) {
    return escapeHtml4(value);
  }
}
