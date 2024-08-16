#! bin/bash

ART_STORE="$PWD/core/infra/data/art-schedule.csv"

function update_art_schedule() {
    local iuuid="$1"
    local iinterval="$2"
    local ilastDateOfART="$3"
    local ipoints="$4"
    local icurrentAchievement="$5"
    local oldARTSchedule="$6"
    
    # if grep -q -E "^$UUID," "$ART_STORE"; then
    #     echo "Error: A user with this UUID already exists."
    #     return 1
    # else
    #     echo "$UUID,$interval,$lastDateOfART,$points,$currentAchievement" >> "$ART_STORE"
    #     return 0
    # fi
            sed -i "s/$oldARTSchedule/$iuuid,$iinterval,$ilastDateOfART,$ipoints,$icurrentAchievement/" "$ART_STORE";

    # while IFS=',' read -r UUID interval lastDateOfART points currentAchievement; do
    #     if [[ "$iuuid" == "$UUID" ]]; then
    #     fi
    # done < "$ART_STORE"

    # exit 1
}

update_art_schedule "$1" "$2" "$3" "$4" "$5" "$6"


