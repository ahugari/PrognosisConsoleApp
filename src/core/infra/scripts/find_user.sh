#! /bin/bash

USER_STORE="$HOME/PrognosisConsoleApp/src/core/infra/data/user-store.txt" # Change the file path
# "$PWD/core/infra/data/user-store.txt"

function find_user() {
    local uuid="$1"

    # echo "Looking for user: $uuid"
    found_user=false
    while IFS=',' read -r uUID stored_email role isProfileComplete firstName lastName stored_password userId dateOfBirth isHIVPositive diagnosisDate isOnART ARTStartDate countryISO; do
        
        if [[ "$uuid" == "$uUID" ]]; then
            # returning the user object based on the role to ease the update
            if [[ "$role" == "ADMIN" ]]; then
                echo "$uUID,$stored_email,$role,$isProfileComplete,$firstName,$lastName,$stored_password,$userId"
            else
                echo "$uUID,$stored_email,$role,$isProfileComplete,$firstName,$lastName,$stored_password,$userId,$dateOfBirth,$isHIVPositive,$diagnosisDate,$isOnART,$ARTStartDate,$countryISO"
            fi
            found_user=true
            return 0
            break
            printf "firstName:$firstName,lastName:$lastName,uuid:$uuid,role:$role,dob:$dateOfBirth,isHIVPositive:$isHIVPositive,diagnosisDate:$diagnosisDate,isOnART:$isOnART,ARTStartDate:$ARTStartDate,countryISO:$countryISO";
            exit 0;
        fi
    done < "$USER_STORE"

    if [ "$found_user" = true ]; then
        return 0
    else
        echo "Could not find user."
        return 1
    fi
    exit 1
}

find_user "$1"