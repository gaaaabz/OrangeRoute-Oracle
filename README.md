# OrangeRoute API

API REST do projeto **OrangeRoute**, desenvolvida em **Java com Spring Boot**, responsável por gerenciar dados da plataforma de orientação de carreira em tecnologia.

A aplicação permite realizar operações **CRUD (Create, Read, Update e Delete)** em entidades como:

* Usuários
* Tipos de Usuário
* Trilhas de Carreira
* Comentários
* Tags
* Favoritos
* Links

A aplicação é implantada utilizando **Microsoft Azure**, com:

* **Azure App Service** (execução da aplicação)
* **Azure SQL Database** (banco de dados)
* **Application Insights** (monitoramento)
* **GitHub Actions** (CI/CD)

---

# Tecnologias Utilizadas

* Java 17
* Spring Boot
* Spring Data JPA
* Maven
* Azure App Service
* Azure SQL Database
* GitHub Actions
* Azure CLI

---

# Arquitetura da Solução

A aplicação segue o modelo **API + Banco de Dados em Nuvem**.

Fluxo:

Cliente → API Spring Boot → Azure SQL Database

```
Usuário / Cliente
        │
        │ HTTP
        ▼
Azure App Service
(Spring Boot API)
        │
        │ JDBC
        ▼
Azure SQL Database
```

---

# Pré-requisitos

Antes de executar o projeto, é necessário instalar:

* Java 17
* Maven
* Azure CLI
* Git
* PowerShell ou Bash

Verifique as versões:

```bash
java -version
mvn -version
az --version
```

---

# 1. Criar Infraestrutura na Azure

Execute o script abaixo para criar:

* Resource Group
* Azure SQL Server
* Azure SQL Database
* Firewall

```powershell
$RG = "rg-OrangeRoute"
$LOCATION = "mexicocentral"
$SERVER_NAME = "sqlserver-rm559597"
$USERNAME = "admsql"
$PASSWORD = "Fiap@2tdsvms"
$DBNAME = "dimdimdb"

az group create --name $RG --location $LOCATION

az sql server create `
  -l $LOCATION `
  -g $RG `
  -n $SERVER_NAME `
  -u $USERNAME `
  -p $PASSWORD `
  --enable-public-network true

az sql db create `
  -g $RG `
  -s $SERVER_NAME `
  -n $DBNAME `
  --service-objective Basic `
  --backup-storage-redundancy Local `
  --zone-redundant false

az sql server firewall-rule create `
  -g $RG `
  -s $SERVER_NAME `
  -n AllowAll `
  --start-ip-address 0.0.0.0 `
  --end-ip-address 255.255.255.255
```

---

# 2. Criar Estrutura do Banco de Dados

Execute o script SQL abaixo para criar as tabelas da aplicação.

```sql
CREATE TABLE T_OR_TIPO_USUARIO (
    id_tipo_usuario INT IDENTITY(1,1) PRIMARY KEY,
    nm_tipo_usuario VARCHAR(50) NOT NULL
);

CREATE TABLE T_OR_USUARIO (
    id_usuario INT IDENTITY(1,1) PRIMARY KEY,
    nm_usuario VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha VARCHAR(150) NOT NULL,
    foto VARBINARY(MAX),
    at_usuario CHAR(1) CHECK (at_usuario IN ('0','1')),
    id_tipo_usuario INT NOT NULL,
    CONSTRAINT fk_usuario_tipo FOREIGN KEY (id_tipo_usuario)
        REFERENCES T_OR_TIPO_USUARIO (id_tipo_usuario)
);

CREATE TABLE T_OR_TRILHA_CARREIRA (
    id_trilha_carreira INT IDENTITY(1,1) PRIMARY KEY,
    tt_trilha_carreira VARCHAR(150) NOT NULL,
    cd_trilha_carreira VARCHAR(MAX)
);

CREATE TABLE T_OR_COMENTARIO (
    id_comentario INT IDENTITY(1,1) PRIMARY KEY,
    cd_comentario VARCHAR(MAX) NOT NULL,
    at_comentario CHAR(1) CHECK (at_comentario IN ('0','1')),
    id_usuario INT NOT NULL,
    id_trilha_carreira INT NOT NULL,
    CONSTRAINT fk_comentario_usuario FOREIGN KEY (id_usuario)
        REFERENCES T_OR_USUARIO (id_usuario),
    CONSTRAINT fk_comentario_trilha FOREIGN KEY (id_trilha_carreira)
        REFERENCES T_OR_TRILHA_CARREIRA (id_trilha_carreira)
);
```

Inserção inicial:

```sql
INSERT INTO T_OR_TIPO_USUARIO (nm_tipo_usuario)
VALUES ('ADMIN');
```

---

# 3. Criar o Serviço de Aplicação

Execute o script abaixo para criar:

* App Service
* Application Insights
* Plano de execução
* Variáveis de ambiente

```bash
export RESOURCE_GROUP_NAME="rg-OrangeRoute"
export WEBAPP_NAME="OrangeRoute-rm559597"
export APP_SERVICE_PLAN="planOrangeRoute"
export LOCATION="mexicocentral"
export RUNTIME="JAVA:17-java17"
export GITHUB_REPO_NAME="gaaaabz/OrangeRoute-Oracle"
export BRANCH="main"
export APP_INSIGHTS_NAME="ai-OrangeRoute"
```

Criar Application Insights:

```bash
az monitor app-insights component create \
  --app "$APP_INSIGHTS_NAME" \
  --location "$LOCATION" \
  --resource-group "$RESOURCE_GROUP_NAME" \
  --application-type web
```

Criar plano de aplicação:

```bash
az appservice plan create \
  --name "$APP_SERVICE_PLAN" \
  --resource-group "$RESOURCE_GROUP_NAME" \
  --location "$LOCATION" \
  --sku F1 \
  --is-linux
```

Criar Web App:

```bash
az webapp create \
  --name "$WEBAPP_NAME" \
  --resource-group "$RESOURCE_GROUP_NAME" \
  --plan "$APP_SERVICE_PLAN" \
  --runtime "$RUNTIME"
```

---

# 4. Configurar Conexão com Banco

Configurar variáveis de ambiente:

```bash
SPRING_DATASOURCE_USERNAME=admsql
SPRING_DATASOURCE_PASSWORD=Fiap@2tdsvms
SPRING_DATASOURCE_URL=jdbc:sqlserver://sqlserver-rm559597.database.windows.net:1433;database=dimdimdb;encrypt=true
```

---

# 5. Deploy Automático com GitHub Actions

O deploy é configurado automaticamente com:

```bash
az webapp deployment github-actions add \
  --name "$WEBAPP_NAME" \
  --resource-group "$RESOURCE_GROUP_NAME" \
  --repo "$GITHUB_REPO_NAME" \
  --branch "$BRANCH" \
  --login-with-github
```

A cada **push na branch main**, o deploy será executado automaticamente.

---

# 6. Acessar a Aplicação

Após o deploy:

```
https://OrangeRoute-rm559597.azurewebsites.net
```

---

# 7. Testar a API

Exemplo de criação de usuário:

```
POST /usuarios
```

Body JSON:

```json
{
  "nmUsuario": "Julia",
  "email": "RMxxxxxx@fiap.com.br",
  "senha": "123",
  "atUsuario": "1",
  "tipoUsuario": {
    "idTipoUsuario": 1
  }}
```

---

# Estrutura do Projeto

```
src
 ├── controller
 ├── model
 ├── repository
 └── application
```

---

# Monitoramento

A aplicação utiliza **Azure Application Insights**, permitindo:

* monitorar erros
* acompanhar performance
* visualizar logs da aplicação

---

# Integrantes

Projeto acadêmico – FIAP

* Julia Damasceno Busso – RM560293
* Gabriel Gomes Cardoso – RM559597
* Jhonatan Quispe Torrez – RM560601


---
# Vídeo do youtube

https://www.youtube.com/watch?v=TNTRpvHSpsQ
