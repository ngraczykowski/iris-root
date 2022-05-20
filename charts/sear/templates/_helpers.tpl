{{/*
Expand the name of the chart.
*/}}
{{- define "sear.name" -}}
  {{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "sear.fullname" -}}
  {{- if .Values.fullnameOverride }}
    {{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
  {{- else }}
    {{- $name := default .Chart.Name .Values.nameOverride }}
    {{- if contains $name .Release.Name }}
      {{- .Release.Name | trunc 63 | trimSuffix "-" }}
    {{- else }}
      {{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
    {{- end }}
  {{- end }}
{{- end }}

{{/*
Create a default fully qualified component name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
*/}}
{{- define "sear.componentName" -}}
  {{- printf "%s-%s" (include "sear.fullname" .) .componentName | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "sear.chart" -}}
  {{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "sear.labels" -}}
helm.sh/chart: {{ include "sear.chart" . }}
{{ include "sear.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "sear.selectorLabels" -}}
app.kubernetes.io/name: {{ include "sear.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Component labels
*/}}
{{- define "sear.componentLabels" -}}
{{- include "sear.labels" . }}
app.kubernetes.io/component: {{ .componentName }}
{{- end }}

{{/*
Component selector labels
*/}}
{{- define "sear.componentSelectorLabels" -}}
{{- include "sear.selectorLabels" . }}
app.kubernetes.io/component: {{ .componentName }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "sear.serviceAccountName" -}}
  {{- if .Values.serviceAccount.create }}
    {{- default (include "sear.fullname" .) .Values.serviceAccount.name }}
  {{- else }}
    {{- default "default" .Values.serviceAccount.name }}
  {{- end }}
{{- end }}

{{/*
Creates the default name of host
*/}}
{{- define "sear.defaultHost" -}}
  {{- printf "%s-%s.prv.dev.s8ops.com" (include "sear.fullname" $) $.Release.Namespace }}
{{- end }}

{{/*
Creates the name of Sentry environment
*/}}
{{- define "sear.sentryEnvironment" -}}
  {{- default (printf "%s-%s.prv.dev.s8ops.com" (include "sear.fullname" $) $.Release.Namespace) .component.sentry.environment }}
{{- end }}

{{ define "sear.rabbitmqSecretName" }}{{ include "sear.fullname" . }}-rabbitmq-default-user{{ end }}
{{ define "sear.postgresqlSecretName" }}{{ printf "%s.%s" .component.dbName (include "sear.fullname" .) }}-postgres.credentials.postgresql.acid.zalan.do{{ end }}
{{ define "sear.postgresqlService" }}{{ include "sear.fullname" . }}-postgres.{{ .Release.Namespace }}.svc{{ end }}

{{- define "sear.mergedComponents" }}

  {{- $output := dict}}

  {{- range $key, $value := $.Values.components }}
    {{- $common := $.Values.common }}
    {{- $command := concat $common.command (default (list) $value.command) }}
    {{- $args := concat $common.args (default (list) $value.args) }}
    {{- $mergedValue := mergeOverwrite (deepCopy $common) $value (dict "args" $args "command" $command ) }}
    {{- $mergedValue = mergeOverwrite (deepCopy $mergedValue) (dict "dbName" (default $key $mergedValue.dbName) "webPath" (default $key $mergedValue.webPath) ) }}
    {{- if $mergedValue.enabled -}}
      {{- $_ := set $output $key $mergedValue }}
    {{- end }}
  {{- end }}

  {{- range $key, $value := $.Values.agents }}
    {{- $common := mergeOverwrite (deepCopy $.Values.common) $.Values.agentsCommon }}
    {{- if hasKey $value "from" }}
      {{- $value = mergeOverwrite (deepCopy $value) (get $.Values.agents $value.from) }}
      {{- $_ := unset $value "from" }}
    {{- end }}
    {{- $concatenated := dict }}
    {{- range $toConcatenate := list "args" "command" "profiles" }}
      {{- $_ := set $concatenated $toConcatenate (concat (get $common $toConcatenate) (default list (get $value $toConcatenate) ) ) }}
    {{- end }}
    {{- range $toMerge := list "properties" }}
      {{- $_ := set $concatenated $toMerge (mergeOverwrite (deepCopy (get $common $toMerge)) (default dict (get $value $toMerge) ) ) }}
    {{- end }}
    {{- $mergedValue := mergeOverwrite (deepCopy $common) $value $concatenated }}
    {{- $mergedValue = mergeOverwrite (deepCopy $mergedValue) (dict "dbName" (default $key $mergedValue.dbName) "webPath" (default $key $mergedValue.webPath) ) }}
    {{- if $mergedValue.enabled -}}
      {{- $_ := set $output $key $mergedValue }}
    {{- end }}
  {{- end }}
  {{- $output | mustToJson }}
{{- end }}
{{- define "checkRabbitMqInitContainer" }}
- name: check-rabbitmq-ready
  image: busybox
  command:
     - sh
     - -c
     - >-
       until wget -q http://$RABBITMQ_USERNAME:$RABBITMQ_PASSWORD@$RABBITMQ_HOST:15672/api/aliveness-test/%2F; do
         echo waiting for rabbitmq;
         sleep 2;
       done;
  env:
    - name: RABBITMQ_HOST
      valueFrom:
        secretKeyRef:
          name: {{ include "sear.rabbitmqSecretName" . }}
          key: host
    - name: RABBITMQ_PORT
      valueFrom:
        secretKeyRef:
          name: {{ include "sear.rabbitmqSecretName" . }}
          key: port
    - name: RABBITMQ_USERNAME
      valueFrom:
        secretKeyRef:
          name: {{ include "sear.rabbitmqSecretName" . }}
          key: username
    - name: RABBITMQ_PASSWORD
      valueFrom:
        secretKeyRef:
          name: {{ include "sear.rabbitmqSecretName" . }}
          key: password
{{- end }}
{{- define "checkPostgresReadyInitContainer" }}
- name: check-db-ready
  image: "postgres:{{ .Values.database.postgresql.version }}"
  command:
    - sh
    - -c
    - >-
      until pg_isready -h {{ include "sear.postgresqlService" . }} -p 5432; do
        echo waiting for database;
        sleep 2;
      done;
{{- end }}
{{- define "initScripts" }}
- name: init-scripts
  image: busybox
  command: ['sh', '-c', "run-parts /var/run/initScripts"]
  volumeMounts:
    - name: init-scripts
      mountPath: "/var/run/initScripts"
      readOnly: true
    - name: generated
      mountPath: "/tmp"
      readOnly: false
  env:
    - name: RABBITMQ_HOST
      valueFrom:
        secretKeyRef:
          name: {{ include "sear.rabbitmqSecretName" . }}
          key: host
    - name: RABBITMQ_PORT
      valueFrom:
        secretKeyRef:
          name: {{ include "sear.rabbitmqSecretName" . }}
          key: port
    - name: RABBITMQ_USERNAME
      valueFrom:
        secretKeyRef:
          name: {{ include "sear.rabbitmqSecretName" . }}
          key: username
    - name: RABBITMQ_PASSWORD
      valueFrom:
        secretKeyRef:
          name: {{ include "sear.rabbitmqSecretName" . }}
          key: password
    - name: UDS_ADDRESS
      value: {{ include "sear.fullname" . }}-universal-data-source.{{ .Release.Namespace }}.svc:9090
{{- end }}
