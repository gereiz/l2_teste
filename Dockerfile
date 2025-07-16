FROM openjdk:24-slim-bullseye

# Atualizar e instalar bash e dependências
RUN apt-get update && \
    apt-get install -y bash wget && \
    apt-get clean

# Definir diretório de trabalho
WORKDIR /app

# Copiar conteúdo para o contêiner
COPY ./sl2 /app/sl2

# Dar permissão de execução aos scripts principais
RUN chmod +x \
    /app/sl2/game/*.sh \
    /app/sl2/login/*.sh \
    /app/sl2/start_servers.sh \
    /app/sl2/LoginServer.sh \
    /app/sl2/GameServer.sh

# Expor as portas necessárias
EXPOSE 2106 9014 7777

# Executar o script usando bash
CMD [ "bash", "/app/sl2/start_servers.sh" ]
