{{- if include "useConfigmap" . }}
apiVersion: v1
kind: ConfigMap
metadata:
  name: {{ include "sear.componentName" . }}
  labels:
    {{- include "sear.componentLabels" . | nindent 4 }}
data:
  {{- if .component.configFiles }}
    {{- tpl (toYaml .component.configFiles) . | nindent 2 }}
  {{- end }}
  {{- if or (include "isConfigServer" . ) (include "useConfigServer" . ) }}
  {{- else }}
  kubernetes.yml: |
    spring.rabbitmq.addresses: ""
    spring:
      config:
        import: optional:configtree:/var/run/secrets/spring/*/

      {{/* NOTE(ahaczewski): After Tadeusz Kleszcz recommendation, disable health check on exceptions when communicating via gRPC. */}}
      autoconfigure:
        exclude: net.devh.boot.grpc.client.autoconfigure.GrpcClientHealthAutoConfiguration

      security:
        oauth2:
          resourceserver:
            jwt:
              issuer-uri: "{{ tpl .Values.keycloak.authServerUrl . }}/realms/{{ .Values.keycloak.realm }}"
              jwk-set-uri: "{{ tpl .Values.keycloak.authServerUrl . }}/realms/{{ .Values.keycloak.realm }}/protocol/openid-connect/certs"

    keycloak:
      frontend-client-id: {{ .Values.keycloak.frontend.clientId | quote }}
      adapter:
        auth-server-url: {{ (tpl .Values.keycloak.authServerUrl . ) | quote }}
        realm: {{ (tpl .Values.keycloak.realm . ) | quote }}
        #TODO(dsniezek): Should be fixed see UserAuthActivityReportGenerator and LastLoginTimeCacheUpdater
        resource: {{ .Values.keycloak.backend.clientId | quote }}
        credentials.secret: {{ .Values.keycloak.backend.clientSecret | quote }}

    grpc:
      server:
        max-inbound-message-size: 8MB

      client:
        GLOBAL:
          {{/* TODO(ahaczewski): Use TLS for gRPC-based communication. */}}
          negotiation-type: PLAINTEXT
          max-inbound-message-size: 8MB

        datasource:
          address: dns:///{{ include "sear.fullname" . }}-universal-data-source.{{ .Release.Namespace }}.svc:9090

        {{/* TODO(ahaczewski): Fix naming inconsistencies between applications. */}}
        ae:
          address: dns:///{{ include "sear.fullname" . }}-adjudication-engine.{{ .Release.Namespace }}.svc:9090
        adjudication-engine:
          address: dns:///{{ include "sear.fullname" . }}-adjudication-engine.{{ .Release.Namespace }}.svc:9090
        adjudicationengine:
          address: dns:///{{ include "sear.fullname" . }}-adjudication-engine.{{ .Release.Namespace }}.svc:9090
        companynamesurroundingagent:
          address: dns:///{{ include "sear.fullname" . }}-company-name-surrounding-agent.{{ .Release.Namespace }}.svc:9090

        {{ range tuple "governance" "simulator" "webapp" "warehouse" -}}
        {{ . }}:
          address: dns:///{{ include "sear.fullname" $ }}-{{ . }}.{{ $.Release.Namespace }}.svc:9090
        {{ end }}
    {{- if .component.additionalConfig }}
    {{- .component.additionalConfig | toYaml | nindent 4}}
    {{- end }}

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

      <include resource="org/springframework/boot/logging/logback/console-appender.xml" />

      <!--
      //TODO: pputerla - disabling temporarily to be able to read logs more easily
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
      -->

      <appender name="ASYNC_CONSOLE" class="ch.qos.logback.classic.AsyncAppender">
        <queueSize>512</queueSize>
        <appender-ref ref="CONSOLE"/>
      </appender>

      <root level="INFO">
        <appender-ref ref="ASYNC_CONSOLE"/>
      </root>
    </configuration>
  {{- end }}
{{- end }}
