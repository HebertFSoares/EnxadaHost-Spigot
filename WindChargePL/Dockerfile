# Use uma imagem base com OpenJDK 21
FROM openjdk:21-jdk-slim

# Define o diretório de trabalho no contêiner
WORKDIR /app

# Baixa o arquivo JAR do servidor Minecraft
RUN apt-get update && apt-get install -y wget
RUN wget https://download.getbukkit.org/spigot/spigot-1.21.jar -O minecraft_server.jar

# Copia o arquivo JAR do plugin para o contêiner
COPY build/libs/WindCharge-1.0-SNAPSHOT.jar /app/plugins/WindChargePL.jar

# Aceita o EULA
RUN echo "eula=true" > eula.txt

# Expõe a porta em que o servidor Minecraft será executado
EXPOSE 25565

# Comando para executar o servidor Minecraft
ENTRYPOINT ["java", "-jar", "minecraft_server.jar", "nogui"]
