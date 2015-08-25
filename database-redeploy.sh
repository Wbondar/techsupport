USERNAME="root"
PASSWORD="root"
DATABASE="techsupport"
BASELINE="database/src/main/sql/baseline.sql"
mysql -u $USERNAME -p$PASSWORD -e "DROP DATABASE $DATABASE;"
mysql -u $USERNAME -p$PASSWORD -e "CREATE DATABASE $DATABASE;"
mysql -v -u $USERNAME -p$PASSWORD -D $DATABASE < $BASELINE > logs/database-redeployment.log
cat logs/database-redeployment.log