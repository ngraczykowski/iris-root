apiVersion: v1
kind: ConfigMap
metadata:
  name: {{ include "sear.componentName" . }}
  labels:
    {{- include "sear.componentLabels" . | nindent 4 }}
data:
  {{- if .component.configFiles }}
    {{- toYaml .component.configFiles | nindent 2 }}
  {{- end }}
  kubernetes.yml: |
    spring:
      config:
        import: configtree:/var/run/secrets/spring/*/

      security:
        oauth2:
          resourceserver:
            jwt:
              issuer-uri: https://auth.silenteight.com/auth/realms/sens-webapp
              jwk-set-uri: https://auth.silenteight.com/auth/realms/sens-webapp/protocol/openid-connect/certs
  logback.xml: |
    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE configuration>

    <configuration>
      <include resource="org/springframework/boot/logging/logback/defaults.xml"/>
      <include resource="logback-defaults.xml"/>

      <springProperty name="SERVICE_NAME" source="spring.application.name" defaultValue="service"/>

      <springProfile name="loglines">
        <!-- Includes line number with the logger -->
        <property name="LOG_LOGGER_PATTERN" value="-40.40logger{39}:%L"/>
      </springProfile>

      <!-- https://logback.qos.ch/manual/configuration.html#shutdownHook and https://jira.qos.ch/browse/LOGBACK-1090 -->
      <shutdownHook class="ch.qos.logback.core.hook.DelayingShutdownHook"/>

      <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="co.elastic.logging.logback.EcsEncoder">
          <serviceName>${SERVICE_NAME}</serviceName>
          <includeMarkers>true</includeMarkers>
          <additionalField>
            <key>host.name</key>
            <value>${HOSTNAME}</value>
          </additionalField>
        </encoder>
        <target>System.out</target>
      </appender>

      <appender name="ASYNC_CONSOLE" class="ch.qos.logback.classic.AsyncAppender">
        <queueSize>512</queueSize>
        <appender-ref ref="CONSOLE"/>
      </appender>

      <root level="INFO">
        <appender-ref ref="ASYNC_CONSOLE"/>
      </root>
    </configuration>
