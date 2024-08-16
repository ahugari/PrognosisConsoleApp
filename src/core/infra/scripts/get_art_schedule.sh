#! /bin/bash

ART_STORE="$PWD/core/infra/data/art-schedule.csv"

function get_art_schedule() {
    local uuid="$1"

    while IFS=',' read -r UUID interval lastDateOfART points currentAchievement; do
        if [[ "$uuid" == "$UUID" ]]; then
            printf "uuid:$UUID,interval:$interval,lastDateOfART:$lastDateOfART,points:$points,currentAchievement:$currentAchievement";
            exit 0
        fi
    done < "$ART_STORE"

    exit 1
}

get_art_schedule "$1"