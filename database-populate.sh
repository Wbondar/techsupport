USERNAME=root
PASSWORD=root
DATABASE=techsupport
mysql -u $USERNAME -p$PASSWORD -D $DATABASE < database/src/test/sql/member.sql
mysql -u $USERNAME -p$PASSWORD -D $DATABASE < database/src/test/sql/message.sql
mysql -u $USERNAME -p$PASSWORD -D $DATABASE < database/src/test/sql/issue.sql
mysql -u $USERNAME -p$PASSWORD -D $DATABASE < database/src/test/sql/tag.sql
mysql -u $USERNAME -p$PASSWORD -D $DATABASE < database/src/test/sql/tag_usage.sql