apiVersion: apps/v1
kind: Deployment
metadata:
  name: {{ include "sear.componentName" . }}
  labels:
    {{- include "sear.componentLabels" . | nindent 4 }}
spec:
  {{- if not .component.autoscaling.enabled }}
  replicas: {{ .component.replicaCount }}
  {{- end }}
  selector:
    matchLabels:
      {{- include "sear.componentSelectorLabels" . | nindent 6 }}
  template:
    metadata:
      annotations:
        fluentbit.io/parser: spring-logback-json
        {{- if include "useConfigmap" . }}
        silenteight.com/config-checksum: {{ tpl ($.Files.Get "component/spring/configmap.yaml.tpl") . | sha256sum }}
        {{- end }}
        {{- with .Values.podAnnotations }}
          {{- toYaml . | nindent 8 }}
        {{- end }}
      labels:
        {{- include "sear.componentSelectorLabels" . | nindent 8 }}
    spec:
      {{- with .component.image.pullSecrets }}
      imagePullSecrets:
        {{- toYaml . | nindent 8 }}
      {{- end }}
      serviceAccountName: {{ include "sear.serviceAccountName" . }}
      {{- with .Values.podSecurityContext }}
      securityContext:
        {{- toYaml . | nindent 8 }}
      {{- end }}
      initContainers:
        {{- if include "useConfigServer" . }}
        {{- include "checkConfigServerReadyInitContainer" . | indent 8 }}
        {{- else }}
        {{- if .component.db.enabled }}
        {{- include "checkPostgresReadyInitContainer" . | indent 8 }}
        {{- end }}
        {{- if and .component.rabbit.enabled .component.rabbit.check }}
        {{- include "checkRabbitMqInitContainer" . | indent 8 }}
        {{- end }}
        {{- end }}
      containers:
        - name: {{ .componentName }}
          {{- with .Values.securityContext }}
          securityContext:
            {{- toYaml . | nindent 12 }}
          {{- end }}
          image: "{{ .component.image.repository }}:{{ .component.image.tag | default .Chart.AppVersion }}"
          imagePullPolicy: {{ .component.image.pullPolicy }}
          ports:
            - name: http
              containerPort: {{ .component.containerPorts.http.port }}
              protocol: TCP
            - name: management
              containerPort: {{ .component.containerPorts.management.port }}
              protocol: TCP
            {{- if .component.containerPorts.grpc.enabled }}
            - name: grpc
              containerPort: {{ .component.containerPorts.grpc.port }}
              protocol: TCP
            {{- end }}
          {{- if .component.startupProbe.enabled }}
          startupProbe:
            httpGet:
              path: /management/health/liveness
              port: management
            initialDelaySeconds: {{ .component.startupProbe.initialDelaySeconds }}
            periodSeconds: {{ .component.startupProbe.periodSeconds }}
            timeoutSeconds: {{ .component.startupProbe.timeoutSeconds }}
            failureThreshold: {{ .component.startupProbe.failureThreshold }}
          {{- end }}
          {{- if .component.livenessProbe.enabled }}
          livenessProbe:
            httpGet:
              path: /management/health/liveness
              port: management
            periodSeconds: {{ .component.livenessProbe.periodSeconds }}
            timeoutSeconds: {{ .component.livenessProbe.timeoutSeconds }}
            failureThreshold: {{ .component.livenessProbe.failureThreshold }}
          {{- end }}
          {{- if .component.readinessProbe.enabled }}
          readinessProbe:
            httpGet:
              path: /management/health/readiness
              port: management
            periodSeconds: {{ .component.readinessProbe.periodSeconds }}
            timeoutSeconds: {{ .component.readinessProbe.timeoutSeconds }}
            successThreshold: {{ .component.readinessProbe.successThreshold }}
            failureThreshold: {{ .component.readinessProbe.failureThreshold }}
          {{- end }}
          {{- with .component.resources }}
          resources:
            {{- toYaml . | nindent 12 }}
          {{- end }}
          {{- if .component.command }}
          command:
            {{- toYaml .component.command | nindent 12 }}
          {{- end }}
          args:
            - --spring.application.name={{ .component.name | default .componentName }}
            {{- if include "useConfigServer" . }}
            - --spring.config.import=configserver:{{ include "sear.configServerUrl" . }}
            {{- else if include "isConfigServer" . }}
            - --server.port={{ .component.containerPorts.http.port }}
            - --management.server.port={{ .component.containerPorts.management.port }}
            - --management.server.base-path=/
            - --management.endpoints.web.base-path=/management
            - --spring.config.additional-location=file:/etc/spring/config/
            - --spring.profiles.include=kubernetes
            {{- else }}
            - --server.port={{ .component.containerPorts.http.port }}
            - --server.servlet.context-path=/rest/{{ .component.webPath }}
            - --spring.webflux.base-path=/rest/{{ .component.webPath }}
            - --spring.config.additional-location=file:/etc/spring/config/,file:/etc/spring/config/kubernetes.yml
            - --logging.config=file:/etc/spring/config/logback.xml
            - --management.server.port={{ .component.containerPorts.management.port }}
            - --management.server.base-path=/
            - --management.endpoints.web.base-path=/management
            {{- if .Values.mailhog.enabled }}
            - --spring.mail.host={{ include "mailhog.fullname" . }}.{{ .Release.Namespace }}.svc
            - --spring.mail.port={{ .Values.mailhog.service.port.smtp }}
            - --spring.mail.username=test
            - --spring.mail.password=test
            {{- end }}
            {{- if .component.sentry.dsn }}
            - --sentry.dsn={{ .component.sentry.dsn }}
            - --sentry.environment={{ include "sear.sentryEnvironment" . }}
            {{- end }}
            {{- if .component.db.enabled }}
            - --spring.datasource.url={{ include "sear.spring.jdbcUrl" . }}
            {{- end }}
            {{- if .component.containerPorts.grpc.enabled }}
            - --grpc.server.port={{ .component.containerPorts.grpc.port }}
            {{- end }}
            {{- if .component.args }}
              {{- include "sear.tplvalues.render" (dict "value" .component.args "context" $) | nindent 12 }}
            {{- end }}
            {{- end }}
            {{- if .component.profiles }}
            - --spring.profiles.include={{ join "," .component.profiles }}
            {{- end }}
            {{- range $key, $value := .component.properties }}
            {{- if $value }}
            - --{{ $key }}={{ include "sear.tplvalues.render" (dict "value" $value "context" $) }}
            {{- end }}
            {{- end }}
          volumeMounts:
            {{- if include "useConfigmap" . }}
            - name: config
              mountPath: "/etc/spring/config"
              readOnly: true
            {{- end }}
            {{- if or (include "isConfigServer" . ) (not (include "useConfigServer" .)) }}
            {{- if .component.rabbit.enabled }}
            - name: secret-rabbitmq
              mountPath: "/var/run/secrets/spring/rabbitmq"
              readOnly: true
            {{- end }}
            {{- if .component.db.enabled }}
            - name: secret-postgres
              mountPath: "/var/run/secrets/spring/postgres"
              readOnly: true
            {{- end }}
            {{- end }}
          env:
            {{- if include "isConfigServer" . }}
            - name: IRIS_RABBIT_HOST
              valueFrom:
                secretKeyRef:
                  name: {{ include "sear.rabbitmqSecretName" . }}
                  key: host
            - name: IRIS_RABBIT_PORT
              valueFrom:
                secretKeyRef:
                  name: {{ include "sear.rabbitmqSecretName" . }}
                  key: port
            - name: IRIS_RABBIT_USERNAME
              valueFrom:
                secretKeyRef:
                  name: {{ include "sear.rabbitmqSecretName" . }}
                  key: username
            - name: IRIS_RABBIT_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: {{ include "sear.rabbitmqSecretName" . }}
                  key: password
            - name: IRIS_DB_USERNAME
              valueFrom:
                secretKeyRef:
                  name: {{ include "sear.postgresqlSecretName" . }}
                  key: username
            - name: IRIS_DB_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: {{ include "sear.postgresqlSecretName" . }}
                  key: password
            {{- end }}
      volumes:
        {{- if include "useConfigmap" . }}
        - name: config
          configMap:
            name: {{ include "sear.componentName" . }}
        {{- end }}
        {{- if not (include "useConfigServer" .) }}
        {{- if .component.rabbit.enabled }}
        - name: secret-rabbitmq
          secret:
            secretName: {{ include "sear.rabbitmqSecretName" . }}
            items:
              - key: host
                path: spring/rabbitmq/host
              - key: port
                path: spring/rabbitmq/port
              - key: username
                path: spring/rabbitmq/username
              - key: password
                path: spring/rabbitmq/password
        {{- end }}
        {{- if .component.db.enabled }}
        - name: secret-postgres
          secret:
            secretName: {{ include "sear.postgresqlSecretName" . }}
            items:
              - key: username
                path: spring/datasource/username
              - key: password
                path: spring/datasource/password
        {{- end }}
        {{- end }}
      {{- with .component.nodeSelector }}
      nodeSelector:
        {{- toYaml . | nindent 8 }}
      {{- end }}
      {{- with .component.affinity }}
      affinity:
        {{- toYaml . | nindent 8 }}
      {{- end }}
      {{- with .component.tolerations }}
      tolerations:
        {{- toYaml . | nindent 8 }}
      {{- end }}
