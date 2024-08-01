#!/bin/bash

USER_STORE="user-store.txt"

# function to initialize the user registration
function initiate_user_registration() {
    # register a new user
    local email=$1
    local uuid=$2
    local role=$3

    # check if email already exists
    if grep -q "^$email," "user-store.txt"; then
        echo "Error: A user with this email already exists."
        return 1
    else
        # add the user to the user-store.txt 
        echo "$email,$uuid,$role" >> "user-store.txt"
        echo "User registration initiated successfully."
    fi
}

# Function to complete user registration
complete_registration() {
    local firstName="$1"
    local lastName="$2"
    local userId="$3"
    local password="$4"
    local UUID="$5"
    local email="$6"
    local dateOfBirth="$7"
    local isHIVPositive="$8"
    local diagnosisDate="$9"
    local isOnART="${10}"
    local ARTStartDate="${11}"
    local countryISO="${12}"

    local hashed_password="$password"

    # Find the line with the matching UUID and update the profile
    if grep -q ",$UUID," "user-store.txt"; then
        sed -i "s/,$UUID,.*$/$UUID,$firstName,$lastName,$userId,$hashed_password,$email,$dateOfBirth,$isHIVPositive,$diagnosisDate,$isOnART,$ARTStartDate,$countryISO/" "$USER_STORE"
        echo "User profile completed successfully."
    else
        echo "Error: No user found with the provided UUID."
        return 1
    fi
}


# Function to handle user login
login() {
    local email="$1"
    local password="$2"

    while IFS=: read -r firstName lastName userId stored_password role UUID stored_email dateOfBirth isHIVPositive diagnosisDate isOnART ARTStartDate countryISO; do
        if [[ "$email" == "$stored_email" ]] && [[ "$password" == "$stored_password" ]]; then
            echo "Login successful for $firstName $lastName (UserID: $userId)."
            return 0
        fi
    done < "user-store.txt"

    echo "Login failed. Invalid credentials."
    return 1
}


