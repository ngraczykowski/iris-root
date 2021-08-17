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


    task "company-name-agent" {
      driver = "docker"

      template {
        data = "{{ key \"${var.namespace}/company-name-agent/secrets\" }}"
        destination = "secrets/company-name-agent.env"
        env = true
      }

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
        image = "python:3.7"
        command = "python"
        args = ["/app/company_name-${var.company_name_agent_version}.pyz", "-c", "/app/config", "-v"]
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
