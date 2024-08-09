#! /bin/bash

USER_STORE="$PWD/core/infra/data/user-store.txt"
REPORT_STORE="$PWD/core/infra/resources/user-data-formatted.txt"

function generate_all_user_data_formatted(){
    if [[ -f $REPORT_STORE ]]; then
        rm $REPORT_STORE
    fi
    awk 'BEGIN {for(char=0;char<227;char++) printf "-"; printf "\n"}' >> $REPORT_STORE;
    awk 'BEGIN {FS=","} NR==1 {printf "|%38s |%20s |%10s |%12s |%12s |%12s |%15s |%30s |%10s |%30s |%15s |\n",$1,$2,$3,$5,$6,$9,$10,$11,$12,$13,$14}' $USER_STORE >> $REPORT_STORE;
    awk 'BEGIN {for(char=0;char<227;char++) printf "-"; printf "\n"}' >> $REPORT_STORE;
    awk 'BEGIN {FS=","} NR>1 {printf "|%38s |%20s |%10s |%12s |%12s |%12s |%15s |%30s |%10s |%30s |%15s |\n",$1,$2,$3,$5,$6,$9,$10,$11,$12,$13,$14}' $USER_STORE >> $REPORT_STORE;
    exit 1
}

generate_all_user_data_formatted