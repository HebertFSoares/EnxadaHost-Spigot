# Use uma imagem base com OpenJDK 21
FROM openjdk:21-jdk-slim

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Atualiza a lista de pacotes e instala o wget
RUN apt-get update && apt-get install -y wget

# Baixa o arquivo JAR do servidor Minecraft
RUN wget https://download.getbukkit.org/spigot/spigot-1.21.jar -O minecraft_server.jar

# Copia o arquivo JAR do plugin para dentro do container
COPY build/libs/HomePlugin-1.0-SNAPSHOT.jar /app/plugins/HomePlugin.jar

# Aceita o EULA do Minecraft
RUN echo "eula=true" > eula.txt

# Expõe a porta que o servidor Minecraft usará
EXPOSE 25565

# Comando para iniciar o servidor Minecraft
ENTRYPOINT ["java", "-jar", "minecraft_server.jar", "nogui"]
