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
