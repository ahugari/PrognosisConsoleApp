#! /bin/bash

USER_STORE="$PWD/core/infra/data/user-store.txt"

function verify_Country_ISO() {
    local isoCode="$1"
    
    local result = res=$(awk 'BEGIN {FS=OFS=","} NF { print $1,$6}' life-expectancy.csv | sort | grep "$car" | awk 'BEGIN {FS=OFS=","} NF {print $1}')
        if [[ -n "$result" ]]; then
            echo "Country found:" + $result;
            return 0
        else
            echo "Country not found."
            return 1
        fi

    return 1
}

login "$1" "$2"