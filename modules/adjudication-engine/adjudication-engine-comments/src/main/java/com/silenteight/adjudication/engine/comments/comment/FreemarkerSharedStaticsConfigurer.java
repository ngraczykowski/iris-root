package com.silenteight.adjudication.engine.comments.comment;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;
import freemarker.template.Version;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@RequiredArgsConstructor
class FreemarkerSharedStaticsConfigurer {

  @NonNull
  private final Version version;

  void configure(Configuration configuration, Class<?>... classes) {
    var wrapper = new BeansWrapper(version);
    var statics = wrapper.getStaticModels();
    for (Class<?> clazz : classes) {
      try {
        configuration.setSharedVariable(getSharedVariableName(clazz), statics.get(clazz.getName()));
      } catch (TemplateModelException e) {
        throw new FreemarkerAddSharedStaticsFailureException(clazz, e);
      }
    }
  }

  private static String getSharedVariableName(Class<?> clazz) {
    if (clazz.isAnnotationPresent(FreemarkerUtils.class)) {
      var name = clazz.getAnnotation(FreemarkerUtils.class).name();
      if (isNotEmpty(name))
        return name;
    }
    return clazz.getSimpleName();
  }

  public static class FreemarkerAddSharedStaticsFailureException extends RuntimeException {

    private static final long serialVersionUID = -2682708621909444671L;

    FreemarkerAddSharedStaticsFailureException(Class<?> clazz, Throwable cause) {
      super(format("Cannot add static methods from: %s", clazz.getName()), cause);
    }
  }
}
