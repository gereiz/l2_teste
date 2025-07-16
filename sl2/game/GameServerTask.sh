#!/bin/bash

# exit codes of GameServer:
#  0 normal shutdown
#  2 reboot attempt

# Função para registrar no terminal e no arquivo
log_to_both() {
    echo "$1" | tee -a "log/$(date +%Y-%m-%d_%H-%M-%S)_stdout.log"
}

while :; do
    # Verificar se o arquivo de log existe e renomeá-lo com a data/hora atual
    if [ -f log/java0.log.0 ]; then
        mv log/java0.log.0 "log/$(date +%Y-%m-%d_%H-%M-%S)_java.log"
        log_to_both "Arquivo java0.log.0 movido para $(date +%Y-%m-%d_%H-%M-%S)_java.log"
    fi
    if [ -f log/stdout.log ]; then
        mv log/stdout.log "log/$(date +%Y-%m-%d_%H-%M-%S)_stdout.log"
        log_to_both "Arquivo stdout.log movido para $(date +%Y-%m-%d_%H-%M-%S)_stdout.log"
    fi

    # Iniciar o servidor Java e redirecionar saída para log e terminal
    java -Djava.awt.headless=true $(cat "java.cfg") -jar ../libs/GameServer.jar |
        tee -a "log/stdout.log" |
        log_to_both "Saída do servidor:"

    # Verificar se o código de saída não é 2 (tentativa de reboot)
    if [ $? -ne 2 ]; then
        break
    fi

    # Espera antes de reiniciar
    sleep 10
done
