#Levantar el bot
Si tenes docker instalado podes usar la docker file publicada <br/>
`docker run carbonecar/testbot`
Si tarda mucho podes bajar el proyecto y buildear la docker file <br/>

Se puede buildear la dockerfile con: <br/>
`docker build -t [nombretag] .`

Luego se levanta con: <br/>
`docker run [nombretag]`

##Si no tenes docker configurado o tenes win$ <10 podes usar la Vagrantfile
Pendiente de prueba (y la version de docker es vieja quiero usar [packer](https://www.packer.io/intro/why.html) para crear mi box) <br/>
`vagrant init williamyeh/ubuntu-trusty64-docker; vagrant up --provider virtualbox`

##Para hablar con el bot podes usar
https://telegram.me/pruebacarlos_bot


#Tutorial de:
https://github.com/rubenlagus/TelegramBots/blob/master/HOWTO.md

Este archivo lo previsualice con grip (pip install grip)

El la api se baja de [aca] (https://github.com/rubenlagus/TelegramBots/releases/download/v2.3.3.7/telegrambots-2.3.3.7-jar-with-dependencies.jar) y la subí a mi maven con:

mvn install:install-file \
-Dfile=telegrambots-2.3.3.7-jar-with-dependencies.jar \ <br/>
-DgroupId=telegrambot \
-DartifactId=telegrambot \
-Dversion=2.3.3.7-full \
-Dpackaging=jar \
-DgeneratePom=true <br/>


#Que use:
Se uso java 8 y spring-boot. Tambien tiene agregado un kie (drools).

#Compilar y levantar
Se compila con maven: <br/>
`mvn clean package`

Se corre con:
`java -jar target/bottest-0.0.1-SNAPSHOT.jar`

En caso de tener problemas al levantar se puede usar:
`java -jar -Djava.security.egd=file:/dev/./urandom bottest-0.0.1-SNAPSHOT.jar`
