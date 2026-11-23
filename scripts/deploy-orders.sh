#!/usr/bin/env bash
# 현대화한 주문 포털을 Azure에 배포합니다. (MySQL + ACR + Container Apps)
# 사용법: ./scripts/deploy-orders.sh <앱 폴더>   예) ./scripts/deploy-orders.sh apps/legacy-orders-modernized
# 필요: az login 완료, 구독 선택 완료
set -euo pipefail

APP_DIR="${1:?앱 폴더를 지정하세요. 예: apps/legacy-orders-modernized}"
RG="${RG:-rg-orders-lab}"
LOC="${LOC:-koreacentral}"
SUF="${SUF:-$(openssl rand -hex 3)}"
MYSQL="orders-mysql-$SUF"
ACR="ordersacr$SUF"
DB_PASSWORD="${DB_PASSWORD:-Lab-$(openssl rand -hex 8)!A1}"

echo "== 리소스 공급자 등록 =="
for ns in Microsoft.DBforMySQL Microsoft.ContainerRegistry Microsoft.App Microsoft.OperationalInsights; do
  az provider register --namespace "$ns" --wait -o none
done

echo "== 리소스 그룹 =="
az group create -n "$RG" -l "$LOC" --tags purpose=catalyst-lab-java -o none

echo "== Azure Database for MySQL (Burstable B1ms) =="
az mysql flexible-server create -g "$RG" -n "$MYSQL" -l "$LOC" \
  --admin-user ordersadmin --admin-password "$DB_PASSWORD" \
  --sku-name Standard_B1ms --tier Burstable --storage-size 20 --version 8.0.21 \
  --public-access 0.0.0.0 -d orders --yes -o none

echo "== ACR 만들고 이미지 빌드 (로컬 Docker 불필요) =="
az acr create -g "$RG" -n "$ACR" --sku Basic -o none
az acr build -r "$ACR" -t orders:v1 "$APP_DIR"

echo "== Container Apps 환경과 앱 =="
az containerapp env create -g "$RG" -n orders-env -l "$LOC" -o none
az containerapp create -g "$RG" -n orders-web --environment orders-env \
  --image "$ACR.azurecr.io/orders:v1" \
  --registry-server "$ACR.azurecr.io" --registry-identity system --system-assigned \
  --ingress external --target-port 8080 --min-replicas 0 --max-replicas 2 --cpu 0.5 --memory 1Gi \
  --secrets db-password="$DB_PASSWORD" \
  --env-vars DB_HOST="$MYSQL.mysql.database.azure.com" DB_NAME=orders DB_USER=ordersadmin \
             DB_PASSWORD=secretref:db-password UPLOAD_DIR=/tmp/uploads DB_SSL_MODE=REQUIRED \
  --query "properties.configuration.ingress.fqdn" -o tsv | sed 's#^#앱 주소: https://#'
