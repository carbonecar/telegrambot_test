
#Tutorial de:
https://github.com/rubenlagus/TelegramBots/blob/master/HOWTO.md

Este archivo lo previsualice con grip (pip install grip)

El componente lo subi a mi maven con:

mvn install:install-file \ <br/>
-Dfile=telegrambots-2.3.3.7-jar-with-dependencies.jar \ <br/>
-DgroupId=telegrambot \ <br/>
-DartifactId=telegrambot \ <br/>
-Dversion=2.3.3.7-full \ <br/>
-Dpackaging=jar \ <br/>
-DgeneratePom=true <br/>


#Que use:
Se uso java 8 y spring-boot. Tambien tiene agregado un kie (drools).

#Compilar y levantar
Se compila con maven: <br/>
`mvn clean package`

Se corre con:
`java -jar target/bottest-0.0.1-SNAPSHOT.jar`

##Docker
Se puede buildear la dockerfile con: <br/>
`docker build -t [nombretag] .`

Luego se levanta con: <br/>
`docker run testbot`

##Descargar la docker file
Tambien se puede descargar la docker file de carbonecar/testbot </br>
`docker run carbonecar/testbot`
