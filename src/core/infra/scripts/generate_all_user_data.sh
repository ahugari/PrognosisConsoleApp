#! /bin/bash

USER_STORE="$PWD/core/infra/data/user-store.txt"
REPORT_STORE="$PWD/core/infra/resources/user-data.csv"

function generate_all_user_data(){
    if [[ -f $REPORT_STORE ]]; then
        rm $REPORT_STORE
    fi
    
    awk 'BEGIN {FS=","} NR==1 {printf "%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",$1,$2,$3,$5,$6,$9,$10,$11,$12,$13,$14}' $USER_STORE > $REPORT_STORE;
    awk 'BEGIN {FS=","; OFS=","} NR>1 { 
        userRowData="";
        if(length($1)>0) {userRowData=$1;};
        if(length($2)>0) {userRowData=userRowData","$2;};
        if(length($3)>0) {userRowData=userRowData","$3;};
        if(length($5)>0) {userRowData=userRowData","$5;};
        if(length($6)>0) {userRowData=userRowData","$6;};
        if(length($9)>0) {userRowData=userRowData","$9;};
        if(length($10)>0) {userRowData=userRowData","$10;};
        if(length($11)>0) {userRowData=userRowData","$11;};
        if(length($12)>0) {userRowData=userRowData","$12;};
        if(length($13)>0) {userRowData=userRowData","$13;};
        if(length($14)>0) {userRowData=userRowData","$14s;};
        printf userRowData "\n";
      }' $USER_STORE >> $REPORT_STORE;
    exit 1
}


generate_all_user_data