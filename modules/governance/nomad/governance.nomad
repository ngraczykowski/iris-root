variable "governance_artifact" {
  type = string
  description = "The name of file containing Governance artifact"
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

job "governance" {
  type = "service"

  datacenters = [
    "dc1"
  ]

  update {
    auto_revert = true
  }

  group "governance" {
    count = 2

    network {
      port "http" {
      }
      port "grpc" {
      }
    }

    service {
      name = "governance"
      port = "http"
      tags = concat([
        "http",
        "traefik.enable=true",
        # FIXME(ahaczewski): Remove when Consul Discovery can filter through results based on tags.
        "gRPC.port=${NOMAD_PORT_grpc}",
      ], var.http_tags)

      check_restart {
        limit = 3
        grace = "90s"
        ignore_warnings = false
      }

      check {
        name = "Governance HTTP Health Check"
        type = "http"
        path = "/rest/governance/management/health"
        method = "GET"
        interval = "10s"
        timeout = "2s"
      }
    }

    service {
      name = "governance"
      port = "grpc"
      tags = [
        "grpc",
        # FIXME(ahaczewski): Remove when Consul Discovery can filter through results based on tags.
        "gRPC.port=${NOMAD_PORT_grpc}",
      ]

      check_restart {
        limit = 3
        grace = "90s"
        ignore_warnings = false
      }

      check {
        name = "gRPC Port Alive Check"
        type = "tcp"
        interval = "10s"
        timeout = "2s"
      }
    }

    # Dummy registration of a service required for Spring Consul Discovery.
    # FIXME(ahaczewski): Remove when Consul Discovery can filter through results based on tags.
    service {
      name = "grpc-governance"
      port = "grpc"
      tags = [
        "grpc",
        "gRPC.port=${NOMAD_PORT_grpc}",
      ]
    }

    task "governance" {
      driver = "raw_exec"

      artifact {
        source = var.governance_artifact
        mode = "file"
        destination = "local/governance-app.jar"
      }

      template {
        data = "{{ key \"governance/secrets\" }}"
        destination = "secrets/governance.env"
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
          "local/governance-app.jar",
          "--spring.profiles.active=linux,governance,database,rabbitmq,messaging",
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
