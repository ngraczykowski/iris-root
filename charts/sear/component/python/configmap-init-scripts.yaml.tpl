{{- if .component.initScripts }}
apiVersion: v1
kind: ConfigMap
metadata:
  name: {{ include "sear.componentName" . }}-init-scripts
  labels:
    {{- include "sear.componentLabels" . | nindent 4 }}
binaryData:
{{- range $key, $value := .component.initScripts }}
  {{ $key }}: {{ $value | b64enc }}
{{- end }}
{{- end }}
