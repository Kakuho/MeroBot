#/bin/bash

if [[ -n $1 ]]
then 
  echo "executing script $1"
else
  echo "need to supply a sql script to execute"
  echo "command: execute_sql path/to/script"
  exit 1
fi

psql postgres://postgres:pw@localhost:8091/merobot_db -f $1

# should be able to connect this way but idk

#psql -h localhost -p 8091 -U postgres -f ./sql/create_config_table.sql
