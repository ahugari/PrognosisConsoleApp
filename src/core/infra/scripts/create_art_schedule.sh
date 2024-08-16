#! bin/bash

ART_STORE="$PWD/core/infra/data/art-schedule.csv"

function create_art_schedule() {
    local UUID="$1"
    local interval="$2"
    local lastDateOfART="$3"
    local points="$4"
    local currentAchievement="$5"
    
    if grep -q -E "^$UUID," "$ART_STORE"; then
        echo "Error: A user with this email or UUID already exists."
        return 1
    else
        echo "$UUID,$interval,$lastDateOfART,$points,$currentAchievement" >> "$ART_STORE"
        return 0
    fi
}

create_art_schedule "$1" "$2" "$3" "$4" "$5"
