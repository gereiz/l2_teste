#!/bin/bash

# Criar logs
mkdir -p /app/sl2/{game,login}/log
chmod 777 /app/sl2/{game,login}/log

# 🛠️ Garantir permissões de execução dos scripts
chmod +x /app/sl2/login/LoginServer.sh
chmod +x /app/sl2/game/GameServer.sh
chmod +x /app/sl2/login/LoginServerTask.sh
chmod +x /app/sl2/game/GameServerTask.sh

# Iniciar LoginServer em segundo plano
cd /app/sl2/login/ || exit 1
./LoginServer.sh &

# Iniciar GameServer em segundo plano
cd /app/sl2/game/ || exit 1
./GameServer.sh &

# Manter o container vivo
tail -f /dev/null
