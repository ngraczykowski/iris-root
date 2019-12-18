package com.silenteight.sens.webapp.backend;

import com.silenteight.sens.webapp.backend.application.grpc.RpcModule;
import com.silenteight.sens.webapp.backend.config.WebModule;
import com.silenteight.sens.webapp.backend.rest.RestModule;
import com.silenteight.sens.webapp.common.app.SensWebAppApplicationContextCallback;
import com.silenteight.sens.webapp.common.app.SensWebAppApplicationTemplate;
import com.silenteight.sens.webapp.users.UsersModule;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;

@SpringBootApplication(
    scanBasePackageClasses = {
        RestModule.class,
        WebModule.class,
        RpcModule.class,
        UsersModule.class
    },
    exclude = {
        FreeMarkerAutoConfiguration.class
    })
public class WebApplication {

  public static void main(String[] args) {
    new SensWebAppApplicationTemplate(args, WebApplication.class)
        .contextCallback(new SensWebAppApplicationContextCallback())
        .run(app -> app
            .configName("sens-webapp"));
  }
}
