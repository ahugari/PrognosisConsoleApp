#! /bin/bash

USER_STORE="$HOME/PrognosisConsoleApp/src/core/infra/data/user-store.txt" # Change the file path
# "$PWD/core/infra/data/user-store.txt"

# function lookup_user() {
#     local uuid="$1"

#     # echo "Looking for user: $uuid"
#     found_user=false
#     while IFS=',' read -r uUID stored_email role isProfileComplete firstName lastName stored_password userId dateOfBirth isHIVPositive diagnosisDate isOnART ARTStartDate countryISO; do
        
#         if [[ "$uuid" == "$uUID" ]]; then
            
#             # returning the user object based on the role to ease the update
#             if [[ "$role" == "ADMIN" ]]; then
#                 printf "$uUID,$stored_email,$role,$isProfileComplete,$firstName,$lastName,$stored_password,$userId"
#             else
#                 printf "$uUID,$stored_email,$role,$isProfileComplete,$firstName,$lastName,$stored_password,$userId,$dateOfBirth,$isHIVPositive,$diagnosisDate,$isOnART,$ARTStartDate,$countryISO"
#             fi
#             found_user=true
#             return 0
#             break
#         fi
#     done < "$USER_STORE"

#     if [ "$found_user" = true ]; then
#         return 0
#     else
#         echo "Could not find user."
#         return 1
#     fi
#     exit 1
# }

function lookup_user() {
    local uuid="$1"

    found_user=false
    while IFS=',' read -r uUID stored_email role isProfileComplete firstName lastName stored_password userId dateOfBirth isHIVPositive diagnosisDate isOnART ARTStartDate countryISO; do
        
        if [[ "$uuid" == "$uUID" ]]; then
            
            # Constructing the user data string without printing it
            if [[ "$role" == "ADMIN" ]]; then
                user_data="$uUID,$stored_email,$role,$isProfileComplete,$firstName,$lastName,$stored_password,$userId"
            else
                user_data="$uUID,$stored_email,$role,$isProfileComplete,$firstName,$lastName,$stored_password,$userId,$dateOfBirth,$isHIVPositive,$diagnosisDate,$isOnART,$ARTStartDate,$countryISO"
            fi
            found_user=true
            echo "$user_data"
            return 0
            break
        fi
    done < "$USER_STORE"

    if [ "$found_user" = true ]; then
        return 0
    else
        echo "Error:: Could not find user."
        return 1
    fi
    exit 1
}


lookup_user "$1"