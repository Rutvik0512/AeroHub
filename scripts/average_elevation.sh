#!/bin/bash

CSV_FILE="./src/main/resources/data/airports.csv"

awk -F',' '
{
    country = $7
    elevation = $8

    # Skip rows with blank values or non‑numeric elevation.
    if (country == "" || elevation == "" || elevation !~ /^-?[0-9]+$/) next

    sum[country]   += elevation
    count[country] += 1
}
END {
    for (c in sum) {
        avg = sum[c] / count[c]
        print c, avg
    }
}' "$CSV_FILE"

