#! /bin/bash

USER_STORE="$PWD/core/infra/data/user-store.txt"

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
            echo "Info: Profile is already complete."
            return 1
        else
            # Update the profile with the provided details and set isProfileComplete to true
            sed -i "s|$email,$UUID,.*$|$UUID,$email,ADMIN,true,$firstName,$lastName,$hashed_password,$userId,|" "$USER_STORE"
            echo "Admin profile completed successfully."
            return 0
        fi
    else
        echo "Info: No user found with the provided UUID and email."
        return 1
    fi
}

admin_complete_registration "$1" "$2" "$3" "$4" "$5" "$6"