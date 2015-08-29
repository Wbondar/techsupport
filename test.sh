mvn install tomcat7:redeploy -DskipTests=false
sh database-redeploy.sh > /dev/null 2> /dev/null