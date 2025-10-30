#!/bin/bash

# sets inventory to the first arguement
inventory="$1"
# sets tmpfile path
tmpfile="/tmp/ims_temp"

# checks if inventory file path doesnt exist or is empty
if [ -z "$inventory" ] || [ ! -f "$inventory" ]; then
  echo "Need Products to search"
  exit 1
fi

# sets the selected to one sleected, uses jq to process the json file, it takes every element, formats it as productID : name, and pipes that into fzf
# it then pipes that into cut ising ':' as a delimeter and sets the id to the slected pid
selected_id=$(jq -r '.[] | "\(.productID): \(.name)"' "$inventory" | fzf --prompt="Select product: " | cut -d':' -f1)

# checks if pid exists
[ -z "$selected_id" ] && exit 0

#puts the selected id in the tmpfile
echo "$selected_id" > "$tmpfile"
