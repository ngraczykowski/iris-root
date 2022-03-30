variable "dataprep_artifact" {
  type = string
  description = "The dataprep artifact"
}

variable "dataprep_artifact_checksum" {
  type = string
  description = "Artifact SHA256 checksum should be provided"
}

variable "namespace" {
  type    = string
  default = "foxtrot"
}

variable "memory" {
  default = 1500
}

variable "http_tags" {
  type    = list(string)
  default = []
}

locals {
  jvm_memory                = ceil(var.memory * 0.7)
  perm_memory               = ceil(var.memory * 0.2)
}

job "data-prep" {
  type = "service"

  datacenters = [
    "dc1"
  ]

  namespace = "${var.namespace}"

  update {
    auto_revert = true
  }

  group "data-prep" {
    count = 1

    network {
      port "http" {
      }
    }

    service {
      name = "${var.namespace}-dataprep"
      port = "http"
      tags = concat([
        "http",
        "traefik.enable=true",
        # Those 2 tags are required to gather metrics by prometheus
        "traefik.protocol=http"
      ], var.http_tags)

      check_restart {
        limit           = 3
        grace           = "90s"
        ignore_warnings = false
      }

      check {
        name     = "Core Bridge HTTP Health Check"
        type     = "http"
        path     = "/rest/dataprep/management/health"
        method   = "GET"
        interval = "30s"
        timeout  = "10s"
      }
    }

    task "dataprep" {
      driver = "raw_exec"

      artifact {
        source = "${var.dataprep_artifact}"
        options {
          checksum = "${var.dataprep_artifact_checksum}"
        }
        mode        = "file"
        destination = "local/dataprep.jar"
      }

      template {
        data        = "{{ key \"${var.namespace}/dataprep/secrets\" }}"
        destination = "secrets/dataprep.env"
        env         = true
      }

      template {
        data = file("./conf/logback.xml")
        destination = "secrets/conf/logback.xml"
      }

      template {
        data        = file("./conf/application.yml")
        destination = "local/conf/application.yml"
        change_mode = "restart"
      }

      env {
        LOG_PATH = "${NOMAD_ALLOC_DIR}/logs"
      }

      config {
        command = "java"
        args    = [
          format("-Xms%dm", local.jvm_memory),
          format("-Xmx%dm", local.jvm_memory),
          format("-XX:MaxMetaspaceSize=%dm", local.perm_memory),
          "-Dfile.encoding=UTF-8",
          "-Dsun.jnu.encoding=UTF-8",
          "-Djava.net.preferIPv4Stack=true",
          "-Djava.io.tmpdir=${meta.silenteight.home}/tmp",
          "-Dlogging.config=secrets/conf/logback.xml",
          "-jar",
          "local/dataprep.jar",
          "--spring.profiles.active=linux,consul",
          "--spring.config.additional-location=file:local/conf/",
        ]
      }

      logs {
        max_files     = 10
        max_file_size = 20
      }

      resources {
        cpu    = 400
        memory = var.memory
      }
    }

    task "fluentbit" {
      driver = "docker"

      lifecycle {
        hook = "prestart"
        sidecar = true
      }

      config {
        image = "fluent/fluent-bit:1.7.7"
        network_mode = "host"
        volumes = [
          "secrets/fluent-bit.conf:/fluent-bit/etc/fluent-bit.conf",
          "local/fluent-parsers.conf:/fluent-bit/etc/fluent-parsers.conf",
        ]
      }

      resources {
        cpu = 50
        memory = 100
      }

      template {
        data = file("./conf/fluent-bit.conf")
        destination = "secrets/fluent-bit.conf"
      }

      template {
        data = file("./conf/fluent-parsers.conf")
        destination = "local/fluent-parsers.conf"
      }
    }
  }
}
