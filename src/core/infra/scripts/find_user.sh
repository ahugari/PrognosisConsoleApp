#! /bin/bash

# USER_STORE="$HOME/PrognosisConsoleApp/src/core/infra/data/user-store.txt" # Change the file path
# "$PWD/core/infra/data/user-store.txt"
USER_STORE="$PWD/core/infra/data/user-store.txt"

function find_user() {
    local uuid="$1"

    while IFS=',' read -r uUID stored_email role isProfileComplete firstName lastName stored_password userId dateOfBirth isHIVPositive diagnosisDate isOnART ARTStartDate countryISO; do
        
        if [[ "$uuid" == "$uUID" ]]; then
            if [[ "$role" == "ADMIN" ]]; then
                printf "firstName:$firstName,lastName:$lastName,uuid:$uuid,role:$role,email:$stored_email,isProfileComplete:$isProfileComplete";
                exit 0;
            else
                printf "firstName:$firstName,lastName:$lastName,uuid:$uuid,role:$role,dob:$dateOfBirth,isHIVPositive:$isHIVPositive,diagnosisDate:$diagnosisDate,isOnART:$isOnART,ARTStartDate:$ARTStartDate,countryISO:$countryISO";
                exit 0;
            fi
        fi
    done < "$USER_STORE"

    exit 1
}

find_user "$1"