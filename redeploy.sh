#sh database-redeploy.sh
native2ascii web/src/main/resources/ErrorMessages_pl.properties  web/src/main/resources/ErrorMessages_pl.properties 
native2ascii web/src/main/resources/HeadersLocalization_pl.properties  web/src/main/resources/HeadersLocalization_pl.properties 
native2ascii web/src/main/resources/LabelsLocalization_pl.properties  web/src/main/resources/LabelsLocalization_pl.properties 
mvn clean compile install package tomcat7:redeploy -DskipTests=true	