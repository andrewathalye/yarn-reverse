#!/bin/bash
echo "Usage: map.sh input output mappings libraries fromtype totype"
echo "Processing: $1 $2 $3 $4 $5 $6"
java -cp "map-libs/*:map-libs" Map $1 $2 $3 $4 $5 $6
