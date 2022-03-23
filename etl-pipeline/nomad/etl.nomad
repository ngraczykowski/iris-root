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

      template {
        data        = file("../config/agents/agents_input_WM_Party.yaml")
        destination = "local/config/agents/agents_input_WM_Party.yaml"
      }

      config {
        image = "docker.repo.silenteight.com/etl-pipeline-service"
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
        tags = [
          "grpc",
          # FIXME(ahaczewski): Remove when Consul Discovery can filter through results based on tags.
          "gRPC.port=${NOMAD_PORT_grpc}",
        ]

        check {
          name = "gRPC Port Alive Check"
          type = "tcp"
          interval = "30s"
          timeout = "10s"
        }
      }
      # Dummy registration of a service required for Spring Consul Discovery.
      # FIXME(ahaczewski): Remove when Consul Discovery can filter through results based on tags.
      service {
        name = "${var.namespace}-grpc-etl-pipeline"
        port = "grpc"
        tags = [
          "grpc",
          "gRPC.port=${NOMAD_PORT_grpc}",
        ]
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
