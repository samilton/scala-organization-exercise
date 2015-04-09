#!/bin/bash

# build me
if [ ! -f runner ]; then
  echo "Runner binary doesn't exist. Building."
  ./deploy.sh
fi

# Run something

# Here's the issue... if the class takes arbitrary args, we can't validate them when we parse the runner's args. For
# example, if someone misspells holiday (./runner class --date 11112233 --holidy --otherarg) we want to know that it's
# an invalid arg and not one meant for the class.
#
# I think the best bet is to separate the args for the class from the args for the runner with a --

./runner com.pragmafs.dataplant.FIXLogLoader --date 20150408 -- --database jdbc://localhost:5432/postgres --domain foo

