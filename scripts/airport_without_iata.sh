#!/bin/bash

CSV_FILE="./src/main/resources/data/airports.csv"

awk -F ',' '
{
    iata = $3
    country = $7

    if (iata == "" && country!= "") {
        arr[country] = country
    }
}
END {
    for (i in arr) {
        print arr[i]
    }
}' "$CSV_FILE"