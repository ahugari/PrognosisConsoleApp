#! /bin/bash

USER_STORE="$HOME/PrognosisConsoleApp/src/core/infra/data/user-store.txt"

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
        echo "$email,$uuid,$role,$isProfileComplete" >> "$USER_STORE"
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
    if grep -q "$UUID," "$USER_STORE"; then
        # Extract the profile completion status (last field)
        local isProfileComplete=$(grep ",$UUID," "$USER_STORE" | awk -F',' '{print $(NF)}')

        if [[ "$isProfileComplete" == "true" ]]; then
            echo "Info: Profile is already complete."
            return 1
        else
            # Update the profile with the provided details and set isProfileComplete to true
            sed -i "s/.*,$UUID,.*/$UUID,$firstName,$lastName,$userId,$hashed_password,$email,$role,$dateOfBirth,$isHIVPositive,$diagnosisDate,$isOnART,$ARTStartDate,$countryISO,true/" "$USER_STORE"
            echo "Patient profile completed successfully."
            return 0
        fi
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

    # Find the line with the matching UUID and email, and update the profile
    if grep -q "$email,$UUID" "$USER_STORE"; then
        # Extract the profile completion status
        local isProfileComplete=$(grep "$email,$UUID" "$USER_STORE" | awk -F',' '{print $4}')

        if [[ "$isProfileComplete" == "true" ]]; then
            echo "Error: Profile is already complete."
            return 1
        else
            # Update the profile with the provided details and set isProfileComplete to true
            sed -i "s|$email,$UUID,.*$|$UUID,$firstName,$lastName,$userId,$hashed_password,$email,0,true|" "$USER_STORE"
            echo "Admin profile completed successfully."
            return 0
        fi
    else
        echo "Error: No user found with the provided UUID and email."
        return 1
    fi
}

# Function to handle user login
function login() {
    local email="$1"
    local password="$2"

    while IFS=',' read -r UUID firstName lastName userId stored_password stored_email role dateOfBirth isHIVPositive diagnosisDate isOnART ARTStartDate countryISO isProfileComplete; do
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
