#! /bin/bash


FILE_PATH="$PWD/core/infra/data/user-store.txt";


# Check if file exists
if [[ ! -f "$FILE_PATH" ]]; then
    echo "Error: Data file not found"
    exit 1
fi


PATIENT_DETAILS=$(awk -F',' '$3 == "PATIENT" {print $2","$4","$5","$9","$10","$11","$12","$13}' "$FILE_PATH")

echo "email,role,isProfileComplete,dateOfBirth,isHIVPositive,diagnosisDate,isOnART,ARTStartDate,countryISO" > patient_data.csv
echo "$PATIENT_DETAILS" >> patient_data.csv

echo "Patient data saved to 'patient_data.csv' file."