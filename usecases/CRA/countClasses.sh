#!/bin/bash
# This script expects a directory path as its only argument, parses all .xmi 
# files (models) in the given directory and counts how many non-empty and empty
# classes are in the models in average.

COUNT=0
CLASSES=0
EMPTY_CLASSES=0

while IFS= read -r -d $'\0'
do
	(( COUNT++ ))
	TEMP_CLASSES=$(grep -c -e "<classes.*feature.*" "$REPLY")
	CLASSARRAY[$COUNT]=$TEMP_CLASSES
		
	if [[ $TEMP_CLASSES -gt $MAX_CLASSES ]]
	then
	  MAX_CLASSES=$TEMP_CLASSES	  
	fi
	(( CLASSES = $CLASSES + $TEMP_CLASSES ))	
		
	if [[ ! -v MIN_CLASSES || $TEMP_CLASSES -lt $MIN_CLASSES ]]
	then
	  MIN_CLASSES=$TEMP_CLASSES
	fi
	
	(( EMPTY_CLASSES = $EMPTY_CLASSES + $(grep -c -E "<classes[[:space:]]*(/>)?$" "$REPLY") ))
	
done < <(find "$1" -maxdepth 1 -type f -iname "*.xmi" -print0)
readarray -t SORTED < <(printf '%s\n' "${CLASSARRAY[@]}" | sort -n)
LENGTH=${#SORTED[@]}

if [[ $(($LENGTH%2)) -eq 0 ]]
	then
	  MEDIAN=$(( (${SORTED[$LENGTH/2-1]} + ${SORTED[$LENGTH/2]}) ))
	  MEDIAN=$(echo print $MEDIAN/2 | perl)
	else
	  MEDIAN=$(( ${SORTED[$LENGTH/2+1]} ))
fi
OUTPUT="List of class counts: ${SORTED[@]}
Total solutions: $LENGTH
Avg. classes: $(echo print $CLASSES/$COUNT | perl)
Med. classes: $MEDIAN

Avg. empty classes: $(echo print $EMPTY_CLASSES/$COUNT | perl )
Max. classes found: $MAX_CLASSES
Min. classes found: $MIN_CLASSES"
FILENAME=$(basename $1)

echo "$OUTPUT" | tee ./class_count_$FILENAME.log



