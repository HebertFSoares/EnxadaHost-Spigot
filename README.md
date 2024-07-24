# Minecraft Plugins: Homeplugin e WindChargePL

## Descrição

Este projeto contém dois plugins para Minecraft, desenvolvidos para a versão **Spigot 1.21**:

1. **Homeplugin**: Um plugin que permite aos jogadores definir e teletransportar-se para locais de "home" personalizados. Inclui funcionalidades de cooldown, partículas ao teleportar e armazenamento das homes em um banco de dados MySQL.

2. **WindChargePL**: Um plugin que altera o comportamento do item `Wind Charge` no jogo, permitindo o lançamento de projéteis com efeitos especiais. Projéteis lançados causam efeitos em jogadores próximos e exibem partículas ao impactar.

## Instalação

### Homeplugin

1. **Baixe o Plugin**: Encontre o arquivo JAR do Homeplugin (`HomePlugin-1.0-SNAPSHOT.jar`).
2. **Coloque o JAR na Pasta de Plugins**: Copie o arquivo JAR para a pasta `plugins` do seu servidor Minecraft.
3. **Reinicie o Servidor**: Reinicie o servidor Minecraft para carregar o plugin.

### WindChargePL

1. **Baixe o Plugin**: Encontre o arquivo JAR do WindChargePL (`WindCharge-1.0-SNAPSHOT.jar`).
2. **Coloque o JAR na Pasta de Plugins**: Copie o arquivo JAR para a pasta `plugins` do seu servidor Minecraft.
3. **Reinicie o Servidor**: Reinicie o servidor Minecraft para carregar o plugin.


## Funcionalidades

### Homeplugin

- **Definição de Home**: Permite aos jogadores definir um local de home usando o comando `/sethome`.
- **Teleportar para Home**: Jogadores podem teleportar-se para suas homes usando o comando `/home`.
- **Cooldown Configurável**: Há um tempo de espera configurável antes que o jogador possa usar o comando de teleportação novamente.
- **Partículas**: Exibe partículas ao teleportar para a home, com configuração para ativar/desativar.
- **Armazenamento em MySQL**: As homes são armazenadas em um banco de dados MySQL, configurável no arquivo `config.yml`.

### WindChargePL

- **Alteração do Item Wind Charge**: Modifica o comportamento do item `Wind Charge`, permitindo o lançamento de projéteis especiais.
- **Efeitos de Projétil**: Projéteis causam efeitos especiais, como partículas e interação com jogadores próximos.
- **Configurações**: Ajuste a velocidade do projétil e a força da explosão através do arquivo `config.yml`.

## Requisitos

- **Versão do Minecraft**: Spigot 1.21
- **Java**: OpenJDK 21
- **Banco de Dados**: MySQL para o Homeplugin

## Configuração

### Homeplugin

1. **Configuração do Banco de Dados**: 
   O arquivo `config.yml` já está pré-configurado com valores padrão para facilitar os testes. Aqui está um exemplo de configuração:

   ```yaml
   cooldown: 60
   countdown_time: 3
   show_particles: true
   database:
     url: jdbc:mysql://host:port/DB
     user: user
     password: password
   messages:
     teleport_cancelled: "Teletransporte cancelado porque você se moveu."
     teleporting: "TELETRANSPORTANDO em {time}..."
     no_home_set: "Você não tem uma home definida."
     home_set: "Home definida com sucesso!"
     teleport_success: "Teletransportado para a home."
     cooldown_wait: "Você deve esperar {time} segundos antes de usar esse comando novamente."

2. **Configurações do Projétil**: 
   Edite o arquivo `config.yml` para ajustar a velocidade do projétil e a força da explosão.

   ```yaml
   projectile_speed: 1.0
   explosion_strength: 2
   show_particles: true
   ```


## Solução de Problemas

### Homeplugin

- **Problema: Não consigo teleportar para minha home.**
  - **Solução:** Verifique se você definiu uma home com o comando `/sethome` e se o banco de dados MySQL está configurado corretamente no `config.yml`.

- **Problema: Mensagens de erro relacionadas ao banco de dados.**
  - **Solução:** Certifique-se de que o MySQL está em execução e que as credenciais no `config.yml` estão corretas.

### WindChargePL

- **Problema: O item Wind Charge não está funcionando corretamente.**
  - **Solução:** Verifique se o item foi configurado corretamente e se o arquivo `config.yml` está configurado conforme as instruções.

- **Problema: Efeitos e partículas não aparecem.**
  - **Solução:** Verifique as configurações de partículas no `config.yml` e certifique-se de que o servidor está com as configurações corretas para exibir partículas.

