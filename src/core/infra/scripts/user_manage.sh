#! /bin/bash

USER_STORE="$HOME/PrognosisConsoleApp/src/core/infra/data/user-store.txt"


# function to initialize the user registration
function initiate_user_registration() {
    # register a new user
    local email=$1
    local uuid=$2
    local role=$3

    # check if email already exists
    if grep -q "^$email," "$USER_STORE" || grep -q ", $uuid," "$USER_STORE"; then
        echo "Error: A user with this email already exists."
        return 1
    else
        # add the user to the user-store.txt 
        echo "$email,$uuid,$role" >> "$USER_STORE"
        echo "User registration initiated successfully."
        return 0
    fi
}

# Function to complete patient registration
function patient_complete_registration() {
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

    local hashed_password=$(echo -n "$password" | openssl dgst -sha256 | awk '{print $2}')

    # Find the line with the matching UUID
    if grep -q ",$UUID," "$USER_STORE"; then
        # Extract the role from the existing entry
        local role=$(grep ",$UUID," "$USER_STORE" | awk -F',' '{print $3}')
        
        # Replace the entire line while preserving the role
        sed -i "s/.*,$UUID,.*/$UUID,$firstName,$lastName,$userId,$hashed_password,$email,$role,$dateOfBirth,$isHIVPositive,$diagnosisDate,$isOnART,$ARTStartDate,$countryISO/" "$USER_STORE"
        echo "Patient profile completed successfully."
        return 0
    else
        echo "Error: No user found with the provided UUID."
        return 1
    fi
}


# Function to complete admin registration
function admin_complete_registration() {
    local firstName="$1"
    local lastName="$2"
    local userId="$3"
    local password="$4"
    local UUID="$5"
    local email="$6"

    local hashed_password=$(echo -n "$password" | openssl dgst -sha256 | awk '{print $2}')

    # Find the line with the matching UUID and email, andN update the profile
    if grep -q "$email,$UUID" "$USER_STORE"; then
        sed -i "s|$email,$UUID,.*$|$UUID,$firstName,$lastame,$userId,$hashed_password,$email,0|" "$USER_STORE"
        echo "Admin profile completed successfully."
        return 0
    else
        echo "Error: No user found with the provided UUID and email."
        return 1
    fi
}


# Function to handle user login
function login() {
    local email="$1"
    local password="$2"

    while IFS=',' read -r UUID firstName lastName userId stored_password stored_email role dateOfBirth isHIVPositive diagnosisDate isOnART ARTStartDate countryISO; do
        if [[ "$email" == "$stored_email" ]]; then
            # Hash the entered password using SHA-256 to match the stored hashed password
            local hashed_password=$(echo -n "$password" | openssl dgst -sha256 | awk '{print $2}')
            
            if [[ "$hashed_password" == "$stored_password" ]]; then
                echo "Login successful for $firstName $lastName (UserID: $userId)."
                return 0
            else
                echo "Login failed. Invalid credentials."
                return 1
            fi
        fi
    done < "user-store.txt"

    echo "Login failed. Invalid credentials."
    return 1
}




