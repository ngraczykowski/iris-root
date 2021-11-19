#!/usr/bin/env bash

DIR="$(cd -- "$(dirname -- "${0}")" && pwd)"
ROOT_DIR="$(cd -- "$DIR" && cd .. && pwd)"

. "$ROOT_DIR"/.env


urls=(
"https://silenteight.com/artifactory/dist-public/aia-pov/name-screening-source-encrypted.zip"   # built in gitlab ci
"https://silenteight.com/artifactory/dist-public/aia-pov/pip-dependencies-encrypted.zip"        # built in gitlab ci
"https://silenteight.com/artifactory/dist-public/aia-pov/serp-opt-installer-1.25.0.run"         # built in jenkins
"https://repo.silenteight.com:443/artifactory/dist-public/ds/ds-installer-1.0.26.run"               # built in jenkins, python 3.8
"https://silenteight.com/artifactory/dist-public/aia-pov/cli-commentator-encrypted.zip"         # built in gitlab ci
"https://repo.silenteight.com/artifactory/dist-public/ds/spark-ivy.tar.bz2"			# built in jenkins
"https://repo.silenteight.com:443/artifactory/libs-release-local/com/silenteight/country/country-agent-dist/2.10.0/country-agent-dist-2.10.0.tar.bz2"
"https://repo.silenteight.com:443/artifactory/libs-release-local/com/silenteight/dates/date-agent-dist/1.10.0/date-agent-dist-1.10.0.tar.bz2"
"https://repo.silenteight.com:443/artifactory/libs-build-local/com/silenteight/document/document-comparer-agent-dist/2.16.0-BUILD.8/document-comparer-agent-dist-2.16.0-BUILD.8.tar.bz2"
"https://repo.silenteight.com:443/artifactory/libs-build-local/com/silenteight/document/document-number-agent-dist/2.16.0-BUILD.8/document-number-agent-dist-2.16.0-BUILD.8.tar.bz2"
"https://repo.silenteight.com:443/artifactory/libs-release-local/com/silenteight/gender/gender-agent-dist/1.7.0/gender-agent-dist-1.7.0.tar.bz2"
"https://repo.silenteight.com:443/artifactory/libs-build-local/com/silenteight/geo/geo-agent-dist/1.5.0-BUILD.152/geo-agent-dist-1.5.0-BUILD.152.tar.bz2"
"https://repo.silenteight.com:443/artifactory/libs-release-local/com/silenteight/linguistics/name-agent-dist/3.10.0/name-agent-dist-3.10.0.tar.bz2"

# running via serp/sear works but doesn't provide any value at the moment
# "https://silenteight.com/artifactory/dist-public/aia-pov/serp-ns-installer-3.11.2.run"          # built in jenkins

)


if [[ "$#" -lt 1 ]]; then
    echo "ERROR: Illegal number of parameters"
    echo "Usage: download-and-upload <local|remote> [--debug]"
    exit 1
fi

if [[ "$#" -eq 2 ]]; then

    if [[ "$2" == "--debug" ]]; then
        set -xeu -o pipefail
    else
        set -eu -o pipefail
    fi
fi


DIR="$(cd -- "$(dirname -- "${0}")" && pwd)"
ROOT_DIR="$(cd -- "$DIR" && cd .. && pwd)"

slack_webhook_username="AIA env"
slack_webhook_channel="aia-ds"
slack_webhook_url="https://hooks.slack.com/services/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"


get_artifactory_sha256() {
    curl -kfs -H "X-JFrog-Art-Api: $artifactory_api_key" "$1.sha256"
}

get_artifactory_as_file_name() {

    if command -v axel &> /dev/null 
    then
        echo "Downloading via axel"
        echo  axel -a -n 10 --insecure -H "X-JFrog-Art-Api: $artifactory_api_key" "$1" "$2" "$3"
        axel -a -n 10 --insecure -H "X-JFrog-Art-Api: $artifactory_api_key" "$1" "$2" "$3"
    else 
        echo "Downloading via curl"
        curl -kf -H "X-JFrog-Art-Api: $artifactory_api_key" "$1" "$2" "$3"
    fi
}

server_file_exist() {
    
    if ssh -q "$host" "test -f $1"; then
        return 0
    else 
        return 1
    fi
}

local_file_exists() {
    local filename
    filename="$(basename "$1")"

    if [[ -f "$cache_dir/$filename.sha256" ]]; then
    
        echo 0
    else
        echo 1
    fi
}

get_server_sha256() {
    local filename
    filename="$(basename "$1")"

    #echo "$filename"
    target_file_path="$remote_dir/$filename"

    sha256="$(ssh "$host" sha256sum "$target_file_path" | awk '{ print $1 }')"

    echo -n "$sha256"

}


get_local_sha256() {
    local filename
    filename="$(basename "$1")"

    if [[ -f "$cache_dir/$filename.sha256" ]]; then
        cat "$cache_dir/$filename.sha256"
    else
        echo ""
    fi
}


download_and_upload_file() {
    local url dau_tmp_dir filename target_file_path artifactory_sha256 server_sha256

    url="$1"
    dau_tmp_dir="$(mktemp -d)"
    filename="$(basename "$url")"
    target_file_path="$remote_dir/$filename"

    artifactory_sha256="$(get_artifactory_sha256 "$url")"
    server_sha256=""
    if server_file_exist "$target_file_path"; then
        server_sha256="$(get_server_sha256 "$target_file_path")"
    fi

    if [[ "$artifactory_sha256" == "$server_sha256" ]]; then

        echo "Remote file $filename exists and is up to date."
        return

    else 
 
        echo "different hash"
        return 

        if local_file_exists "$ROOT_DIR/env/installer/$filename" ; then
            server_sha256="$(get_local_sha256 "$target_file_path")"

            if [[ "$artifactory_sha256" == "$server_sha256" ]]; then
                ssh $host mkdir -p "$remote_dir"
                echo "Copy previously downloaded file "$ROOT_DIR/env/installer/$filename""
                scp "$ROOT_DIR/env/installer/$filename" "$host:$remote_dir/$filename"

                return
            fi
        fi

        echo "Either no file or not up to date sha. Downloading file"
        axel -a -n 10 --insecure -H "X-JFrog-Art-Api: $artifactory_api_key" "$url"   - "$dau_tmp_dir/$filename" 
        
        #wget --header -o "$dau_tmp_dir/$filename" "$url" 
        scp "$dau_tmp_dir/$filename" "$host:$target_file_path.tmp"

        ssh "$host" chmod 666 "$target_file_path.tmp"
        ssh "$host" mv "$target_file_path.tmp" "$target_file_path"
        rm -f "$dau_tmp_dir/$filename"
        echo -n "$artifactory_sha256" > "$cache_dir/$filename.sha256"
        


        # curl -k -X POST -H 'Content-type: application/json' \
        #     --data '{"text":"Uploaded *'"$filename"'* to customer premises. SHA256: *'"$artifactory_sha256"'*", "username": "'"$slack_webhook_username"'", "channel": "'"$slack_webhook_channel"'" }' \
        #     "$slack_webhook_url"
    fi


}


download_file() {
    local url dau_tmp_dir filename target_file_path artifactory_sha256 server_sha256

    url="$1"
    dau_tmp_dir="$(mktemp -d)"

    filename="$(basename "$url")"
    target_file_path="$ROOT_DIR/env/installer/$filename"

    artifactory_sha256="$(get_artifactory_sha256 "$url")"
    server_sha256=""
    if local_file_exists "$target_file_path"; then
        server_sha256="$(get_local_sha256 "$target_file_path")"
    fi

    if [[ "$artifactory_sha256" == "$server_sha256" ]]; then
        echo "File $filename is up to date, skipping update"
        return
    else

        echo -n "$artifactory_sha256" > "$cache_dir/$filename.sha256"
        echo "Downloading" "$url"
        get_artifactory_as_file_name "$url" -o "$target_file_path"
    fi

 }


if [[ -f "$ROOT_DIR/.env" ]]; then
    . "$ROOT_DIR/.env"
else
    echo "Missing .env file"
    exit 1
fi


MODE="$1"

if [[ -z "$REPO_TOKEN" ]]; then
    echo "ERROR: artifactory_api_key environment variable must be set."
    exit 1
fi

artifactory_api_key=$REPO_TOKEN


cache_dir="$ROOT_DIR/env/installer/.cache"
mkdir -p "$cache_dir"

if [[ "$MODE" == "remote" ]]; then

    host=$POV_HOST
    remote_dir="$POV_DIR"/name-screening-pov/env/installer

    for url in "${urls[@]}"
        do
            download_and_upload_file "$url" || true
        done
elif [[ "$MODE" == "local" ]]; then
    for url in "${urls[@]}"
        do
            download_file "$url" || true
        done

fi

