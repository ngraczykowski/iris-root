package utils;

import lombok.Getter;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.util.Objects;
import java.util.stream.Collectors;

// TODO(pputerla): tidy up this class
public class CustomLogFilter implements Filter {

  @Getter private final ThreadLocal<String> callLog = ThreadLocal.withInitial(() -> "");

  @Override
  public Response filter(
      FilterableRequestSpecification filterableRequestSpecification,
      FilterableResponseSpecification filterableResponseSpecification,
      FilterContext filterContext) {
    var stringBuilder = new StringBuilder();

    stringBuilder.append(Objects.toString(filterableRequestSpecification.getMethod(), ""));
    stringBuilder.append(" ");
    stringBuilder.append(Objects.toString(filterableRequestSpecification.getURI(), ""));
    stringBuilder.append("\n");
    stringBuilder
        .append("Form Params: ")
        .append(Objects.toString(filterableRequestSpecification.getFormParams(), ""));
    stringBuilder.append("\n");
    stringBuilder
        .append("Request Param: ")
        .append(Objects.toString(filterableRequestSpecification.getRequestParams(), ""));
    stringBuilder.append("\n");
    stringBuilder
        .append("Headers: ")
        .append(Objects.toString(filterableRequestSpecification.getHeaders(), ""));
    stringBuilder.append("\n");
    stringBuilder
        .append("Cookies: ")
        .append(Objects.toString(filterableRequestSpecification.getCookies(), ""));
    stringBuilder.append("\n");
    stringBuilder
        .append("Proxy: ")
        .append(Objects.toString(filterableRequestSpecification.getProxySpecification(), ""));
    stringBuilder.append("\n");
    stringBuilder
        .append("Body: ")
        .append(Objects.toString(filterableRequestSpecification.getBody(), ""));

    callLog.set(
        callLog.get()
            + stringBuilder
                .toString()
                .lines()
                .map(l -> ">>> " + l)
                .collect(Collectors.joining("\n")));

    stringBuilder = new StringBuilder();

    Response response =
        filterContext.next(filterableRequestSpecification, filterableResponseSpecification);

    stringBuilder.append("Response Status: ");
    stringBuilder.append(response.getStatusLine());
    stringBuilder.append("\n");
    stringBuilder.append("Response Cookies: ").append(response.getDetailedCookies());
    stringBuilder.append("\n");
    stringBuilder.append("Response Content Type: ").append(response.getContentType());
    stringBuilder.append("\n");
    stringBuilder.append("Response Headers: ").append(response.getHeaders());
    stringBuilder.append("\n");
    stringBuilder.append("Response Body: " + "\n\n").append(response.getBody().prettyPrint());

    callLog.set(
        callLog.get()
            + "\n\n"
            + stringBuilder
                .toString()
                .lines()
                .map(l -> "<<< " + l)
                .collect(Collectors.joining("\n"))
            + "\n\n\n");

    return response;
  }

  public void clear() {
    callLog.set("");
  }
}
