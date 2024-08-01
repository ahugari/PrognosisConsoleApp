#! /bin/bash

USER_STORE="$PWD/core/infra/data/user-store.txt"

# function to initialize the user registration
function initiate_user_registration() {
    # register a new user
    local email=$1
    local uuid=$2
    local role=$3
    local isProfileComplete="false"
    
    # Check if email or UUID already exists in any format
    if grep -q -E "^$email,|^$uuid," "$USER_STORE"; then
        echo "Error: A user with this email or UUID already exists."
        return 1
    else
        # Add the user to the user-store.txt with isProfileComplete set to false
        echo "$uuid,$email,$role,$isProfileComplete" >> "$USER_STORE"
        echo "User registration initiated successfully."
        return 0
    fi
}

initiate_user_registration "$1" "$2" "$3"