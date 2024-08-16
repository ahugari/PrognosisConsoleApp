#! bin/bash

ART_STORE="$PWD/core/infra/data/art-schedule.csv"

function art_schedule_exists() {
    local uuid="$1"
    
    # if grep -q -E "^$uuid," "$ART_STORE"; then
    #     exit 0;
    #     # return 0
    # fi

# function find_user() {
    # local uuid="$1"

    while IFS=',' read -r UUID interval lastDateOfART points currentAchievement; do
        if [[ "$uuid" == "$UUID" ]]; then
            # if [[ "$role" == "ADMIN" ]]; then
                printf 0;
                exit 0
            # fi
        fi
    done < "$ART_STORE"

    exit 1
# }
# function get_art_schedule() {
#     local uuid="$1"

#     while IFS=',' read -r uUID interval lastDateOfART points currentAchievement; do
#         if [[ "$uuid" == "$uUID" ]]; then
#             exit 0;
#         fi
#     done < "$ART_STORE"

#     exit 1
# }
            # printf "uuid:$uUID,interval:$interval,lastDateOfART:$lastDateOfART,points:$points,currentAchievement:$currentAchievement";


    # # while IFS=',' read -r uUID stored_email role isProfileComplete firstName lastName stored_password userId dateOfBirth isHIVPositive diagnosisDate isOnART ARTStartDate countryISO; do
    # while IFS=',' read -r uUID interval lastDateOfART points currentAchievement; do
    #     if [[ "$uuid" == "$uUID" ]]; then
    #         # if [[ "$role" == "ADMIN" ]]; then
    #             # printf "firstName:$firstName,lastName:$lastName,uuid:$uuid,role:$role,email:$stored_email,isProfileComplete:$isProfileComplete";
    #             echo "andy"
    #             # exit 0;
    #         else
    #             # printf "firstName:$firstName,lastName:$lastName,uuid:$uuid,role:$role,dob:$dateOfBirth,isHIVPositive:$isHIVPositive,diagnosisDate:$diagnosisDate,isOnART:$isOnART,ARTStartDate:$ARTStartDate,countryISO:$countryISO";
    #             exit 2;
    #         # fi
    #     fi
    # done < "$ART_STORE"

    # exit 1
}

art_schedule_exists "$1" 