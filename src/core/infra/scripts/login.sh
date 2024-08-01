#! /bin/bash

USER_STORE="$PWD/core/infra/data/user-store.txt"

function login() {
    local email="$1"
    local password="$2"

    while IFS=',' read -r UUID stored_email role isProfileComplete firstName lastName stored_password userId dateOfBirth isHIVPositive diagnosisDate isOnART ARTStartDate countryISO; do
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
    done < "$USER_STORE"

    echo "Login failed. Invalid credentials."
    return 1
}

login "$1" "$2"