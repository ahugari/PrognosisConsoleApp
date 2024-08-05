#! /bin/bash

USER_STORE="$PWD/core/infra/data/user-store.txt"

function find_user() {
    local uuid="$1"

echo "Looking for user" + $uuid
    while IFS=',' read -r uUID stored_email role isProfileComplete firstName lastName stored_password userId dateOfBirth isHIVPositive diagnosisDate isOnART ARTStartDate countryISO; do
        if [[ "$uuid" == "$uUID" ]]; then
            echo "$firstName, $lastName, $uuid"
            exit 0
        else
            echo "Could not find user."
            exit 1
        fi

    done < "$USER_STORE"

    echo "Something went wrong while trying to find user."
    exit 1
}

find_user "$1"