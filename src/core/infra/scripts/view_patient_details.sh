#! /bin/bash
echo "Please wait..."

# Check if UUID is provided
if [[ $# -eq 0 ]]; then
    echo "Error: UUID not provided"
    exit 1
fi
UUID=$1
COMP_LIFESPAN=$2
FILE_PATH="$PWD/infra/data/user-store.txt";

# Check if file exists
if [[ ! -f "$FILE_PATH" ]]; then
    echo "Error: Data file not found"
    exit 1
fi

# Search for the patient with the given UUID and return selected data
RESULT=$(awk -F',' -v uuid="$UUID" '$1 == uuid && $3 == "PATIENT" {print $2","$3","$4","$5","$6","$8","$9","$10","$11","$12","$13}' "$FILE_PATH")


if [[ -z "$RESULT" ]]; then
    echo "Patient not found man man!!"
    exit 1
else
     # If comp lifespan is provided, append it to the result
    if [[ -n "$COMP_LIFESPAN" ]]; then
        RESULT="$RESULT,$COMP_LIFESPAN"
    fi
    echo "$RESULT"
    exit 0
fi