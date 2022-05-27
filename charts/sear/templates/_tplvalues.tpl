{{/*
Renders a value that contains template.
NOTE(ahaczewsk): Shamelessly copied from bitnami-common chart.
Usage:
{{ include "sear.tplvalues.render" ( dict "value" .Values.path.to.the.Value "context" $) }}
*/}}
{{- define "sear.tplvalues.render" -}}
    {{- if typeIs "string" .value }}
        {{- tpl .value .context }}
    {{- else }}
        {{- tpl (.value | toYaml) .context }}
    {{- end }}
{{- end -}}
