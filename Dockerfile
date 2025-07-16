FROM openjdk:24-slim-bullseye

RUN apt-get update && \
    apt-get install -y bash wget && \
    apt-get clean

WORKDIR /app

COPY ./sl2 /app/sl2

# 🔧 Corrigido: chmod direto nos scripts-alvo
RUN chmod +x ./sl2/start_servers.sh && \
    chmod +x ./sl2/login/LoginServer.sh && \
    chmod +x ./sl2/game/GameServer.sh

EXPOSE 2106 9014 7777

CMD ["bash", "./sl2/start_servers.sh"]
