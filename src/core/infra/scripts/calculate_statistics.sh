#!/bin/bash

# Paths to files
USER_STORE="$HOME/PrognosisConsoleApp/src/core/infra/data/user-store.txt"
STATS_FILE_CSV="$HOME/PrognosisConsoleApp/src/core/infra/resources/statistics.csv"
STATS_FILE_TXT="$HOME/PrognosisConsoleApp/src/core/infra/resources/statistics.txt"

# Temporary file to store life expectancy values
TEMP_LIFE_EXPECTANCY="$HOME/PrognosisConsoleApp/src/core/infra/data/temp_life_expectancy.txt"

# Clear the temp file if it exists
> "$TEMP_LIFE_EXPECTANCY"

# Declare associative arrays to hold country statistics
declare -A country_patient_count
declare -A country_hiv_positive_count

# Iterate through the users and calculate life expectancy
while IFS=',' read -r uUID stored_email role isProfileComplete firstName lastName stored_password userId dateOfBirth isHIVPositive diagnosisDate isOnART ARTStartDate countryISO; do
    if [[ "$role" == "PATIENT" ]]; then
        # Call the calculateSurvivalRate method for each patient
        lifeSpan=$(java -cp lpmt.jar core.infra.middleware.ProcessManager calculateSurvivalRate "$uUID")
        echo "$lifeSpan" >> "$TEMP_LIFE_EXPECTANCY"
        
        # Validate and increment patient count for the country
        if [[ -n "$countryISO" ]]; then
            country_patient_count["$countryISO"]=$((country_patient_count["$countryISO"] + 1))
        
            # Increment HIV positive count for the country if applicable
            if [[ "$isHIVPositive" == "true" ]]; then
                country_hiv_positive_count["$countryISO"]=$((country_hiv_positive_count["$countryISO"] + 1))
            fi
        fi
    fi
done < "$USER_STORE"

# Read all life expectancy values into an array
mapfile -t lifeExpectancies < "$TEMP_LIFE_EXPECTANCY"

# Calculate average
average=$(awk '{sum+=$1} END {if (NR > 0) print sum/NR; else print 0}' "$TEMP_LIFE_EXPECTANCY")

# Calculate median
IFS=$'\n' sorted=($(sort -n <<<"${lifeExpectancies[*]}"))
unset IFS
count=${#sorted[@]}
if (( $count > 0 )); then
    if (( $count % 2 )); then
        median=${sorted[$((count/2))]}
    else
        median=$(echo "(${sorted[$((count/2-1))]} + ${sorted[$((count/2))]} ) / 2" | bc -l)
    fi

    # Calculate 25th and 75th percentiles
    percentile_25=${sorted[$((count/4))]}
    percentile_75=${sorted[$((3*count/4))]}
else
    median=0
    percentile_25=0
    percentile_75=0
fi

# Save life expectancy statistics to CSV file
echo "Metric,Value" > "$STATS_FILE_CSV"
echo "Average,$average" >> "$STATS_FILE_CSV"
echo "Median,$median" >> "$STATS_FILE_CSV"
echo "25th Percentile,$percentile_25" >> "$STATS_FILE_CSV"
echo "75th Percentile,$percentile_75" >> "$STATS_FILE_CSV"

# Save country statistics to CSV file
echo "Country,Total Patients,HIV Positive Patients" >> "$STATS_FILE_CSV"
for country in "${!country_patient_count[@]}"; do
    total_patients=${country_patient_count["$country"]}
    hiv_positive_patients=${country_hiv_positive_count["$country"]}
    echo "$country,$total_patients,${hiv_positive_patients:-0}" >> "$STATS_FILE_CSV"
done

# Save formatted statistics to TXT file
{
    printf "%-20s %-15s\n" "Metric" "Value"
    printf "%-20s %-15.2f\n" "Average" "$average"
    printf "%-20s %-15.2f\n" "Median" "$median"
    printf "%-20s %-15.2f\n" "25th Percentile" "$percentile_25"
    printf "%-20s %-15.2f\n" "75th Percentile" "$percentile_75"
    echo ""
    printf "%-15s %-20s %-20s\n" "Country" "Total Patients" "HIV Positive Patients"
    for country in "${!country_patient_count[@]}"; do
        total_patients=${country_patient_count["$country"]}
        hiv_positive_patients=${country_hiv_positive_count["$country"]}
        printf "%-15s %-20d %-20d\n" "$country" "$total_patients" "${hiv_positive_patients:-0}"
    done
} > "$STATS_FILE_TXT"

# Clean up
rm "$TEMP_LIFE_EXPECTANCY"

echo "Statistics saved to $STATS_FILE_CSV and $STATS_FILE_TXT"