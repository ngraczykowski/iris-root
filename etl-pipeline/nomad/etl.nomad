variable "namespace" {
  type    = string
  default = "mike"
}

job "etl-pipeline" {
  type = "service"

  datacenters = [
    "dc1"
  ]

  namespace = "${var.namespace}"

  update {
    auto_revert = true
  }

  group "etl-pipeline" {
    count = 1

    network {
      port "grpc" {
        to = 9090
      }
    }
    task "etl-pipeline" {
      driver = "docker"

      template {
        data        = "{{ key \"${var.namespace}/etl_service/secrets\" }}"
        destination = "secrets/etl_service.env"
        env         = true
      }

      template {
        data        = file("../config/pipeline/pipeline.yaml")
        destination = "local/config/pipeline/pipeline.yaml"
      }

      template {
        data        = file("../config/service/service.nomad.yaml")
        destination = "local/config/service/service.yaml"
      }

      template {
        data        = file("../config/agents/agents_input_WM_ADDRESS.yaml")
        destination = "local/config/agents/agents_input_WM_ADDRESS.yaml"
      }

      config {
        image = "docker.repo.silenteight.com/etl-pipeline-service:0.5.7-dev"
        mount {
          type   = "bind"
          source = "local/config/pipeline/pipeline.yaml"
          target = "/config/pipeline/pipeline.yaml"
        }
        mount {
          type   = "bind"
          source = "local/config/service/service.yaml"
          target = "/config/service/service.yaml"
        }
        mount {
          type   = "bind"
          source = "local/config/agents/agents_input_WM_ADDRESS.yaml"
          target = "/config/agents/agents_input_WM_ADDRESS.yaml"
        }
        ports = [
          "grpc"
        ]
      }

      service {
        name = "${var.namespace}-etl-pipeline"
        port = "grpc"
        check {
          type     = "tcp"
          interval = "30s"
          timeout  = "5s"
        }
      }

      resources {
        cpu    = 2048
        # MHz
        memory = 2048
        # MB
      }
    }
  }
}
