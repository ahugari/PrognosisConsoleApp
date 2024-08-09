#! /bin/bash

# use in debug mode only
# USER_STORE="$PWD/src/core/infra/data/user-store.txt"

# use in production mode
USER_STORE="$PWD/core/infra/data/user-store.txt"

function find_user_by_role() {
    local user_id="$1"
    local role="$2"

    while IFS=',' read -r UUID email sRole isProfileComplete firstName lastName sPassword userId dateOfBirth isHIVPositive diagnosisDate isOnART ARTStartDate countryISO; do
        
        if [[ "$user_id" == "$UUID" && "$role" == "$sRole" && "true" == "$isProfileComplete" ]]; then
            exit 0
        fi
        #user does not match the conditions specified above
        exit 1
    done < "$USER_STORE"

    echo "Something went wrong while trying to find user."
    exit 2
}

find_user_by_role "$1" "$2"