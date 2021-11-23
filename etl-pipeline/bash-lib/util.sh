#!/usr/bin/env bash

bl_check_required_variables() {
  error=0

  for i in "$@"; do
    if [ "${!i:-}" == "" ]; then
      bl_tsecho_err $i variable is not initialized >&2
      error=1
    fi
  done

  if [ "$error" == "1" ]; then
    exit 1
  fi
}

bl_sleep() {
  (
    set +x
    local seconds_to_sleep="$1"
    while [[ "$seconds_to_sleep" -gt 0 ]]; do
      bl_echoerr -en "\rWaiting $seconds_to_sleep seconds...  \033[2D"
      sleep 1
      seconds_to_sleep=$((seconds_to_sleep-1))
    done
    echo -en "\r                      \r"
  )
}

bl_wait_for() {
    local timeout="$1"
    shift
    local cmd="$@"
    local ts_start="$SECONDS"

    while true; do
        if (eval $cmd 2>/dev/null); then
            break
        else
            local ts_current="$SECONDS"
            if (( $ts_current > $ts_start + $timeout )); then
                exit 71
            fi
            sleep 1
        fi
    done
}

bl_wait_for_url() {
    local timeout="$1"
    local url="$2"

    bl_wait_for "$timeout" "curl -kfL $url"
}

bl_parse_opts() {
  local prefix=$1
  shift
  while (("$#")); do
    opt=$1
    shift
    case $opt in
    --*)
      local arg=$(sed -r 's/-/_/g' <<<"${opt:2}")
      eval "export ${prefix}${arg^^}=$1"
      shift
      ;;
    *)
      usage
      ;;
    esac
  done
}

bl_echoerr() {
    echo "$@" >&2
}

bl_ts() {
  date +%Y%m%d_%H%M%S_%N
}

bl_tsecho() {
  echo "[$(bl_ts)] $@"
}

bl_tsecho_err() {
  bl_tsecho "$@" >&2
}

bl_log() {
  bl_tsecho_err "$(basename ${0}): $@"
}

bl_ensure_file_exists() {
    local filename="$1"
    if [[ -z "$filename" ]]; then
        bl_tsecho_err "bl_ensure_file_exists could not verify if file exists as its name was not provided. Maybe you forgot to set variable value? Try this command: set -u"
        exit 2
    fi

    if [[ ! -f "$filename" ]]; then
        bl_tsecho_err "File $filename does not exist. Exiting..."
        exit 3
    fi
}
