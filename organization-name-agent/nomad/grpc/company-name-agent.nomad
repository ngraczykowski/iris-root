variable "company_name_agent_version" {
  type = string
  description = "Company name agent version"
}

variable "company_name_agent_artifact" {
  type = string
  description = "The name of file containing artifact"
}

variable "company_name_agent_artifact_checksum" {
  type = string
  description = "Checksum of file containing artifact"
}

variable "company_name_agent_config" {
  type = string
  description = "Configuration files"
}

variable "namespace" {
  type = string
  default = "dev"
}

job "company-name-agent" {
  type = "service"

  datacenters = [
    "dc1"
  ]

  namespace = "${var.namespace}"

  update {
    auto_revert = true
  }

  group "company-name-agent" {
    count = 1

    network {
      port "grpc" {
      }
    }

    service {
      name = "${var.namespace}-company-name-agent"
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
      name = "${var.namespace}-grpc-company-name-agent"
      port = "grpc"
      tags = [
        "grpc",
        "gRPC.port=${NOMAD_PORT_grpc}",
        "gRPC_port=${NOMAD_PORT_grpc}",
      ]
    }


    task "company-name-agent" {
      driver = "raw_exec"

      artifact {
        source = var.company_name_agent_artifact
        options {
          checksum = "${var.company_name_agent_artifact_checksum}"
        }
      }

      artifact {
        source = var.company_name_agent_config
      }

      template {
        source = "local/config/application.nomad.yaml"
        destination = "local/config/application.yaml"
        change_mode = "restart"
      }

      config {
        command = "/home/suroptusr/miniconda3/bin/python"
        args = ["local/company_name-${var.company_name_agent_version}.pyz", "-c", "local/config", "--grpc"]
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
