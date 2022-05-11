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
        silenteight.com/config-checksum: {{ tpl ($.Files.Get "component/spring/configmap.yaml.tpl") . | sha256sum }}
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
        - name: init-rabbit-secrets
          image: busybox
          command: ['sh', '-c', "sleep 1"]
      containers:
        - name: {{ .componentName }}
          {{- with .Values.securityContext }}
          securityContext:
            {{- toYaml . | nindent 12 }}
          {{- end }}
          image: "{{ .component.image.repository }}:{{ .component.image.tag | default .Chart.AppVersion }}"
          imagePullPolicy: {{ .component.image.pullPolicy }}
          ports:
            {{- if .component.containerPorts.grpc.enabled }}
            - name: grpc
              containerPort: {{ .component.containerPorts.grpc.port }}
              protocol: TCP
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
            - -v
            {{- if .component.sentry.dsn }}
#            - --sentry.dsn={{ .component.sentry.dsn }}
#            - --sentry.environment={{ include "sear.sentryEnvironment" . }}
            {{- end }}
            {{- if .component.containerPorts.grpc.enabled }}
            - --grpc
            {{- end }}
            {{- if .component.args }}
              {{- toYaml .component.args | nindent 12 }}
            {{- end }}
          volumeMounts:
            - name: config
              mountPath: "/var/run/configs/"
              readOnly: true
            - name: secret-rabbitmq
              mountPath: "/var/run/secrets/rabbitmq"
              readOnly: true
          env:
      volumes:
        - name: config
          configMap:
            name: {{ include "sear.componentName" . }}
        - name: secret-rabbitmq
          secret:
            secretName: {{ include "sear.rabbitmqSecretName" . }}
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
