sh database-redeploy.sh
mvn clean compile install package tomcat7:redeploy -DskipTests=true	