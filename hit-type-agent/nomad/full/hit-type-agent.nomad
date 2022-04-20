variable "hit_type_agent_version" {
  type = string
  description = "Hit Type agent version"
}

variable "hit_type_agent_artifact" {
  type = string
  description = "The name of file containing artifact"
}

variable "hit_type_agent_artifact_checksum" {
  type = string
  description = "Checksum of file containing artifact"
}

variable "hit_type_agent_config" {
  type = string
  description = "Configuration files"
}

variable "namespace" {
  type = string
  default = "dev"
}

job "hit-type-agent" {
  type = "service"

  datacenters = [
    "dc1"
  ]

  namespace = "${var.namespace}"

  update {
    auto_revert = true
  }

  group "hit-type-agent" {
    count = 1

    network {
      port "grpc" {
      }
      port "grpcui" {
      }
    }

    service {
      name = "${var.namespace}-hit-type-agent"
      port = "grpc"

      check_restart {
        limit           = 3
        grace           = "90s"
        ignore_warnings = false
      }

      check {
        name     = "gRPC Port Alive Check"
        type     = "tcp"
        interval = "10s"
        timeout  = "2s"
      }
    }

    service {
      name = "${var.namespace}-grpc-hit-type-agent"
      port = "grpc"
      tags = [
        "grpc",
        "gRPC.port=${NOMAD_PORT_grpc}",
        "gRPC_port=${NOMAD_PORT_grpc}",
      ]
    }

    service {
      name = "${var.namespace}-hit-type-agent-grpcui"
      port = "grpcui"
      tags = concat([
        "grpcui",
        "traefik.enable=true",
        "traefik.protocol=http",
      ])
    }


    task "grpcui" {
      driver = "raw_exec"

      lifecycle {
        hook = "poststart"
        sidecar = true
      }

      artifact {
        source = "https://github.com/fullstorydev/grpcui/releases/download/v1.1.0/grpcui_1.1.0_linux_x86_64.tar.gz"
        options {
          checksum = "sha256:41b9b606a025561f7df892e78a8ac1819597ed74d2300183797ab8caa7b290a6"
        }
      }

      config {
        command = "grpcui"
        args = [
          "-plaintext",
          "-bind=${NOMAD_IP_grpcui}",
          "-port=${NOMAD_PORT_grpcui}",
          "-open-browser=false",
          "${NOMAD_ADDR_grpc}"
        ]
      }

      resources {
        cpu = 50
        memory = 100
      }
    }


    task "hit-type-agent" {
      driver = "docker"

      template {
        data = "{{ key \"${var.namespace}/hit-type-agent/secrets\" }}"
        destination = "secrets/hit-type-agent.env"
        env = true
      }

      artifact {
        source = var.hit_type_agent_artifact
        options {
          checksum = "${var.hit_type_agent_artifact_checksum}"
        }
      }

      artifact {
        source = var.hit_type_agent_config
      }

      template {
        data = file("application.nomad.yaml")
        destination = "local/config/application.yaml"
        change_mode = "restart"
      }

      config {
        image = "python:3.7"
        command = "python"
        args = ["/app/hit_type-${var.hit_type_agent_version}.pyz", "-c", "/app/config", "--grpc", "--agent-exchange", "-v"]
        network_mode = "host"
        volumes = ["local:/app"]
      }

      logs {
        max_files = 10
        max_file_size = 20
      }

      resources {
        cpu = 1000
      }

    }
  }
}
