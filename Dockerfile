FROM openjdk:24-slim-bullseye

# Instalar bash e dependências
RUN apt-get update && \
    apt-get install -y bash wget && \
    apt-get clean

# Definir diretório de trabalho
WORKDIR /app

# Copiar todo o conteúdo do projeto
COPY ./sl2 /app/sl2

# Garantir permissão de execução para os scripts
RUN chmod +x \
    /app/sl2/*.sh \
    /app/sl2/game/*.sh \
    /app/sl2/login/*.sh

# Expor as portas necessárias
EXPOSE 2106 9014 7777

# Executar o script
CMD ["bash", "/app/sl2/start_servers.sh"]
