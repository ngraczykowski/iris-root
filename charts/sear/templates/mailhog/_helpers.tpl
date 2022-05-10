{{/* vim: set filetype=mustache: */}}
{{/*
Expand the name of the chart.
*/}}
{{- define "mailhog.name" -}}
{{- printf "%s-mailhog" (include "sear.name" .) | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "mailhog.fullname" -}}
{{- printf "%s-mailhog" (include "sear.fullname" .) | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Common labels for Mailhog
*/}}
{{- define "mailhog.labels" -}}
{{- include "sear.labels" . }}
app.kubernetes.io/component: mailhog
{{- end }}

{{/*
Selector labels for Mailhog
*/}}
{{- define "mailhog.selectorLabels" -}}
{{- include "sear.selectorLabels" . }}
app.kubernetes.io/component: mailhog
{{- end }}

{{/*
Create the name for the auth secret.
*/}}
{{- define "mailhog.authFileSecret" -}}
    {{- if .Values.mailhog.auth.existingSecret -}}
        {{- .Values.mailhog.auth.existingSecret -}}
    {{- else -}}
        {{- template "mailhog.fullname" . -}}-auth
    {{- end -}}
{{- end -}}
