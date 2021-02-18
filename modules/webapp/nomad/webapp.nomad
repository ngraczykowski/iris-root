variable "webapp_artifact" {
  type = string
}

variable "webapp_artifact_checksum" {
  type = string
  description = "Artifact SHA256 checksum should be provided"
}

variable "memory" {
  default = 1500
}

variable "http_tags" {
  type = list(string)
  default = []
}

locals {
  jvm_memory = ceil(var.memory * 0.7)
  perm_memory = ceil(var.memory * 0.2)
}

job "webapp" {
  type = "service"

  datacenters = [
    "dc1"
  ]

  group "webapp" {
    count = 3

    network {
      port "http" {
      }
    }

    update {
      auto_revert = true
    }

    service {
      name = "webapp"
      port = "http"
      tags = concat([
        "http",
        "traefik.enable=true",
      ], var.http_tags)

      check_restart {
        limit = 3
        grace = "90s"
        ignore_warnings = false
      }

      check {
        name = "http"
        type = "http"
        path = "/rest/webapp/management/health"
        method = "GET"
        interval = "10s"
        timeout = "2s"
      }
    }

    task "webapp" {
      driver = "raw_exec"

      artifact {
        source = var.webapp_artifact
        options {
            checksum = "${var.webapp_artifact_checksum}"
        }
        mode = "file"
        destination = "local/sens-webapp-backend.jar"
      }

      template {
        data = "{{ key \"webapp/secrets\" }}"
        destination = "secrets/webapp.env"
        env = true
      }

      template {
        data = file("conf/application.yml")
        destination = "local/conf/application.yml"
        change_mode = "noop"
      }

      template {
        data = file("conf/application-database.yml")
        destination = "local/conf/application-database.yml"
        change_mode = "noop"
      }

      template {
        data = file("conf/application-messaging.yml")
        destination = "local/conf/application-messaging.yml"
        change_mode = "noop"
      }

      config {
        command = "java"
        args = [
          format("-Xms%dm", local.jvm_memory),
          format("-Xmx%dm", local.jvm_memory),
          format("-XX:MaxPermSize=%dm", local.perm_memory),
          "-Dfile.encoding=UTF-8",
          "-Dsun.jnu.encoding=UTF-8",
          "-Djava.net.preferIPv4Stack=true",
          "-Djava.io.tmpdir=${meta.silenteight.home}/tmp",
          "-Dserp.show-env=true",
          "-jar",
          "local/sens-webapp-backend.jar",
          "--spring.profiles.active=linux,webapp,database,rabbitmq,messaging,consul",
          "--spring.config.additional-location=file:${NOMAD_TASK_DIR}/conf/",
          # todo tkleszcz: enable consul for SD, in future change to envoy
          "--spring.cloud.consul.discovery.register=false",
          "--spring.cloud.consul.config.enabled=false",
          "--spring.cloud.consul.port=8500",
        ]
      }

      logs {
        max_files = 10
        max_file_size = 20
      }

      resources {
        cpu = 800
        memory = var.memory
      }
    }
  }
}
