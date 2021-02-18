variable "simulator_artifact" {
  type = string
  description = "The name of file containing Simulator artifact"
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

job "simulator" {
  type = "service"

  datacenters = [
    "dc1"
  ]

  update {
    auto_revert = true
  }

  group "simulator" {
    count = 2

    network {
      port "http" {
      }
    }

    service {
      name = "simulator"
      port = "http"
      tags = concat([
        "http",
        "traefik.enable=true"
      ], var.http_tags)

      check_restart {
        limit = 3
        grace = "90s"
        ignore_warnings = false
      }

      check {
        name = "Simulator HTTP Health Check"
        type = "http"
        path = "/rest/simulator/management/health"
        method = "GET"
        interval = "10s"
        timeout = "2s"
      }
    }


    task "simulator" {
      driver = "raw_exec"

      artifact {
        source = var.simulator_artifact
        mode = "file"
        destination = "local/simulator-app.jar"
      }

      template {
        data = "{{ key \"simulator/secrets\" }}"
        destination = "secrets/simulator.env"
        env = true
      }

      template {
        data = file("./conf/application.yml")
        destination = "local/conf/application.yml"
        change_mode = "noop"
      }

      template {
        data = file("./conf/application-database.yml")
        destination = "local/conf/application-database.yml"
        change_mode = "noop"
      }

      template {
        data = file("./conf/application-messaging.yml")
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
          "-jar",
          "local/simulator-app.jar",
          "--spring.profiles.active=linux,simulator,database,rabbitmq,messaging",
          "--spring.config.additional-location=file:local/conf/"
        ]
      }

      logs {
        max_files = 10
        max_file_size = 20
      }

      resources {
        cpu = 400
        memory = var.memory
      }
    }
  }
}
