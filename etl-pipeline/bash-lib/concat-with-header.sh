#!/usr/bin/env bash
#
# Concatenates multiple files preserving the header from only the first
# and discarding header from the others. Source files are left untouched
# and the output is saved to stdout.
#
# For example, consider the following input:
# file1.csv:
#     col1,col2,col3
#     row11,row12,row13
#     row21,row22,row23
#
# file2.csv
#     col1,col2,col3
#     row31,row32,row33
#
# Then, the command "concat file1.csv file2.csv" will give the following output:
#     col1,col2,cole3
#     row11,row12,row13
#     row21,row22,row23
#     row31,row32,row33

set -eu -o pipefail
bl_dir="$(cd -- "$(dirname -- "${0}")" && pwd)"
source $bl_dir/util.sh

bl_merge_with_header_func() {
    if [[ "$#" == "0" ]]; then
        bl_echoerr "usage: $(basename "${BASH_SOURCE[0]}") file [ file [ ... file ] ]"
        exit 1
    fi

    cat "$1"
    shift

    while [[ "$#" != "0" ]]; do
        tail -n +2 "$1"
        shift
    done
}

bl_merge_with_header_func "$@"
