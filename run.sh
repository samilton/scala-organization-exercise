#!/bin/bash

# build me
if [ ! -f runner ]; then
  echo "Runner binary doesn't exist. Building."
  ./deploy.sh
fi

# Run something

./runner com.pragmafs.dataplant.FIXLogLoader --date 20150408 --database jdbc://localhost:5432/postgres --domain foo

