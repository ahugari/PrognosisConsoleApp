#!/bin/bash

source ./core/infra/scripts/find_user.sh
# USER_STORE="$HOME/PrognosisConsoleApp/src/core/infra/data/user-store.txt"
# "$PWD/core/infra/data/user-store.txt" ---> Make sure to change the path
USER_STORE="$PWD/core/infra/data/user-store.txt"

function update_profile() {
    local uuid="$1"
    local current_value="$2"
    local new_value="$3"


    local user_data
    user_data=$(lookup_user "$uuid")

    # checking if the exit status of the find_user function is not equal to 0: user not found
    if [[ $? -ne 0 ]]; then
        echo "Error:: User with the UUID $uuid not found"
        return 1
    fi
echo "please wait..."
    # track the line number for the user data
    local line_number=0
    local update_line=""

    # find the user's line number in the file 
    while IFS="," read -r uUID stored_email role isProfileComplete firstName lastName stored_password userId dateOfBirth isHIVPositive diagnosisDate isOnART ARTStartDate countryISO; do 
        ((line_number++))
echo "please wait..."

        if [[ "$uuid" == "$uUID" ]]; then
echo "please wait..."

            if [[ 
                "$stored_email" == "$current_value" || 
                "$firstName" == "$current_value" || 
                "$lastName" == "$current_value" || 
                "$dateOfBirth" == "$current_value" || 
                "$isHIVPositive" == "$current_value" || 
                "$diagnosisDate" == "$current_value" || 
                "$isOnART" == "$current_value" ||
                "$ARTStartDate" == "$current_value" ||
                "$countryISO" == "$current_value" 
                 ]]; then
echo "please wait..."

            # NOTE: do we enable users to update their passwords? If so, how to we handle that since the password is encrypted and we don't know the data parsed is for password so we encrypt before checking?
                update_line=$(echo "$user_data" | sed "s/$current_value/$new_value/")
echo "please wait..."

                echo "$update_line"
                break
            else 
                echo "Error:: Current value $current_value was not found in the user data"
                return 1
            fi
        fi

    done < "$USER_STORE"
    
    # checking if the update_line is not empty
    if [[ -z "$update_line" ]]; then
        echo "Error:: Profile Update Unsuccessful"
        return 0
    fi

    # Replacing the previous line number with the updated line
    sed -i "${line_number}s|.*|$update_line|" "$USER_STORE"

    # checking the status code
    if [[ $? -eq 0 ]]; then 
        echo "Success:: Profile Updated successfully"
        return 0
    else 
        echo "Error:: Profile Update Unsuccessful"
        return 1
        
    fi
    
}

patient_update_profile "$1" "$2" "$3" 
