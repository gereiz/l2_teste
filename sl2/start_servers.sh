#!/bin/bash

mkdir -p /app/sl2/{game,login}/log

# permisao
chmod 777 /app/sl2/{game,login}/log

# Iniciar LoginServer em segundo plano
cd /app/sl2/login/ || exit 1
./LoginServer.sh &

# Iniciar GameServer em segundo plano
cd /app/sl2/game/ || exit 1
./GameServer.sh &

# Esperar indefinidamente para manter o contêiner em execução
tail -f /dev/null
