#!/bin/bash

CSV_FILE="./src/main/resources/data/airports.csv"

awk -F ',' '
{
   if (NF == 1) {
      next
    }
    timezone = $11

    if (timezone != "" && timezone !~ /^[0-9]$+/) {
        arr[timezone] += 1
    }
}
END {
    for (i in arr) {
        print arr[i], i
    }
}' "$CSV_FILE" | sort -k1 -nr | head -n 10