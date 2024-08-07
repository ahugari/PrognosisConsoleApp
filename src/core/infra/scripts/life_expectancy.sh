#! /bin/bash

LIFE_EXPECTANCY="$PWD/core/infra/data/life-expectancy.csv"

function get_life_expectancy()
{
     awk 'BEGIN {FS=","}; {print $6,$NF}' $LIFE_EXPECTANCY
     exit 0
}

get_life_expectancy