#/bin/bash

if [[ -n $1 ]]
then 
  echo "executing script $1"
else
  echo "need to supply a sql script to execute"
  echo "command: execute_sql path/to/script"
  exit 1
fi

psql postgres://meroron:meromeromero@localhost:5432/merobot_db -f $1
