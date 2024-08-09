#! /bin/bash

USER_STORE="$PWD/core/infra/data/user-store.txt"

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
        local isProfileComplete=$(grep "$UUID," "$USER_STORE" | awk -F',' '{print $(NF)}')

        if [[ "$isProfileComplete" == "true" ]]; then
            echo "Info: Profile is already complete."
            return 1
        else
            # Update the profile with the provided details and set isProfileComplete to true
            sed -i "s/$UUID,.*/$UUID,$email,PATIENT,true,$firstName,$lastName,$hashed_password,$userId,$dateOfBirth,$isHIVPositive,$diagnosisDate,$isOnART,$ARTStartDate,$countryISO,$lifespan" "$USER_STORE"
            echo "Patient profile completed successfully."
            return 0
        fi
    else
        echo "Error: No user found with the provided UUID."
        return 1
    fi
}

patient_complete_registration "$1" "$2" "$3" "$4" "$5" "$6" "$7" "$8" "$9" "${10}" "${11}" "${12}" 