{{- if .component.autoscaling.enabled -}}
apiVersion: autoscaling/v2beta1
kind: HorizontalPodAutoscaler
metadata:
  name: {{ include "sear.componentName" . }}
  labels:
    {{- include "sear.componentLabels" . | nindent 4 }}
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: {{ include "spring.componentName" . }}
  minReplicas: {{ .component.autoscaling.minReplicas }}
  maxReplicas: {{ .component.autoscaling.maxReplicas }}
  metrics:
    {{- if .component.autoscaling.targetCPUUtilizationPercentage }}
    - type: Resource
      resource:
        name: cpu
        targetAverageUtilization: {{ .component.autoscaling.targetCPUUtilizationPercentage }}
    {{- end }}
    {{- if .component.autoscaling.targetMemoryUtilizationPercentage }}
    - type: Resource
      resource:
        name: memory
        targetAverageUtilization: {{ .component.autoscaling.targetMemoryUtilizationPercentage }}
    {{- end }}
{{- end -}}
