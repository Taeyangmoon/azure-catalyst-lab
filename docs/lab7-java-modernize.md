---
permalink: /lab7-java-modernize/
title: "7. Java 앱 현대화와 배포"
nav_order: 9
---

# 7. 계획에서 실행으로: Java 앱을 Container Apps로

⏱️ 약 90분 (AI 실행 대기 20분, Azure 프로비저닝 대기 10분 포함) · 💰 **약 $0.3 이내** (정가 기준 추정, 실습 후 삭제)

Catalyst가 세운 계획의 마지막 단계입니다. 2장에서 만난 `Application_ord`(Tomcat 서버 + MySQL)를 대표하는 **레거시 Java 주문 포털**을 GitHub Copilot **Modernize CLI**로 현대화하고 **Azure Container Apps**에 배포합니다.

{: .warning }
**Catalyst와의 관계**: Catalyst의 웹앱 **Code Insights**(코드 변경 분석을 Catalyst 평가에 연결)는 **Appliance 기반 discovery**로 찾은 ASP.NET/Java 웹앱이 있어야 쓸 수 있습니다. 이 랩의 파일 가져오기에서는 **Web apps가 0건**이라 이 기능을 쓸 수 없습니다. 그래서 이 모듈은 Catalyst 밖에서 **같은 Modernize CLI**를 직접 실행합니다. Appliance 환경이라면 [Code Insights 문서](https://learn.microsoft.com/azure/migrate/add-copilot-code-insights)의 `modernize assess --source <config.json>` 방식으로 결과가 Catalyst에 업로드됩니다.

## 배울 것
- 레거시 Java 앱이 **클라우드에 올리기 어려운 이유**를 평가 보고서로 읽기
- **assess → plan → execute** 흐름과, 계획(`plan.md`, `tasks.json`)을 **사람이 고치는 지점**
- 결과를 **코드 diff로 검토**하고 Azure에 배포해 검증하기

## 사전 요건

| 항목 | 설명 |
|---|---|
| GitHub 계정 | **GitHub Copilot** 플랜(Free 포함) |
| GitHub CLI | `gh` 2.45 이상, `gh auth login` 완료 |
| Modernize CLI | 아래 설치 |
| Azure CLI | `az login` 완료, 구독 선택 |
| git | 계획과 실행 결과를 커밋으로 남기기 위해 필요 |

Modernize CLI 설치:

```bash
# macOS / Linux
curl -fsSL https://raw.githubusercontent.com/microsoft/modernize-cli/main/scripts/install.sh | bash
modernize --version
```

```powershell
# Windows PowerShell
iex (irm 'https://raw.githubusercontent.com/microsoft/modernize-cli/main/scripts/install.ps1')
```

{: .note }
Docker와 JDK, Maven은 **직접 설치하지 않아도** 됩니다. 이미지는 Azure에서 빌드하고(`az acr build`), Modernize CLI가 필요한 JDK와 Maven을 스스로 준비합니다.

## 1. 레거시 앱 준비

저장소의 `apps/legacy-orders`를 **독립된 git 저장소로 복사**합니다. 실행 결과가 커밋으로 남도록 하기 위해서입니다.

```bash
git clone https://github.com/Taeyangmoon/azure-catalyst-lab.git
cp -R azure-catalyst-lab/apps/legacy-orders ~/orders
cd ~/orders
git init -b main && git add -A && git commit -m "baseline"
```

이 앱은 일부러 "온프렘 시절 습관"을 담았습니다.

| 위치 | 레거시 습관 |
|---|---|
| `pom.xml` | **Java 8**, `javax.servlet` 3.1, 오래된 MySQL 드라이버(5.1), log4j 1.2 |
| `db.properties` | **DB 접속 정보와 비밀번호가 코드에 하드코딩** |
| `log4j.properties` | 로그를 **서버 로컬 파일**(`/var/log/...`)에 기록 |
| `UploadServlet` | 업로드 파일을 **서버 로컬 디스크**에 저장 |
| (없음) | Dockerfile 없음 |

## 2. 평가: 무엇이 문제인가

```bash
modernize assess --source . --no-tty --format markdown --output-path ./assess
```

처음 실행하면 평가 도구(약 140MB)를 내려받아 시간이 조금 더 걸립니다. 결과는 `assess/reports-*/repos/<폴더 이름>/report.md`에 저장됩니다. 이 문서의 예시처럼 폴더 이름이 `orders`이면 `repos/orders/report.md`입니다.

| 이슈 | 중요도 | 점수 | 의미 |
|---|---|---:|---|
| Java Version Has Reached the End of Support | **Mandatory** | 8 | JDK 8 지원 종료 |
| Avoid File System Logging in Configuration | **Mandatory** | 1 | 컨테이너가 교체되면 로그가 사라짐 |
| No Dockerfile found | **Mandatory** | 3 | 컨테이너 패키징 없음 |
| File system - Java IO | **Mandatory** | 3 | 업로드 코드가 로컬 디스크에 의존 |
| MySQL database found | Potential | 5 | Azure Database for MySQL로 옮길 후보 |
| Jakarta EE Version is not the latest stable | Optional | 8 | `javax.*` → `jakarta.*` |

요약: Mandatory 4, Potential 1. 앱 하나이지만 "Wave 2 - Core Cloud"로 분류됩니다.

## 3. 계획 만들기 (사람이 고치는 지점)

```bash
modernize plan create "Upgrade to Java 17 and Jakarta EE 10 (Tomcat 10.1), read database settings and upload path from environment variables instead of hardcoded files, write logs to stdout, add a Dockerfile, and prepare the app to run on Azure Container Apps with Azure Database for MySQL" \
  --plan-name orders-aca --language java \
  --assess-file-path ./assess/reports-*/repos/orders/report.md \
  --source . --no-tty
```

이 랩에서는 **5개의 작업**이 만들어졌습니다.

| # | 작업 | 해결하는 평가 이슈 |
|---|---|---|
| 001 | Java 17 + Jakarta EE 10 업그레이드 (Tomcat 10.1) | JDK 지원 종료, Jakarta EE |
| 002 | DB 설정과 업로드 경로를 **환경 변수**로 | 하드코딩 비밀번호, 파일 시스템 IO, MySQL |
| 003 | 로그를 **stdout**으로 | 파일 로깅 |
| 004 | 의존성 **CVE 점검과 수정** | (계획이 스스로 추가한 보안 작업) |
| 005 | **Dockerfile**과 Azure Container Apps 배포 | Dockerfile 없음 |

`.github/modernize/orders-aca/plan.md`와 `.metadata/tasks.json`을 열어 보세요. **실행 전에 사람이 계획을 고칠 수 있습니다.** 이 랩에서는 005번 작업을 고쳤습니다. 원래는 "Azure에 **배포**까지" 하도록 만들어졌는데, 비용과 통제를 위해 **Dockerfile 만들기까지만** 하도록 바꾸고 배포는 5절에서 직접 합니다.

`tasks.json`의 5번 항목 수정 예:

```json
{
  "type": "transform",
  "id": "005-transform-dockerfile",
  "description": "Containerize the application with a Dockerfile (build only, do NOT deploy to Azure).",
  "requirements": "Create a multi-stage Dockerfile that builds the WAR with Maven and Java 17 and runs it on Tomcat 10.1 (Java 17 JRE) as ROOT.war. Expose port 8080. Add a .dockerignore. Do not create or modify any Azure resources and do not run az, azd or Bicep commands."
}
```

`plan.md`에서도 같은 취지로 "Dockerfile까지만, 배포는 수동" 문장을 반영합니다.

{: .note }
**AI가 만든 계획을 그대로 실행하지 않는 것**이 핵심입니다. 이 랩의 계획에는 **의도하지 않은 Azure 리소스 생성(배포 작업)** 이 들어 있었습니다. 실행 전에 계획을 읽고 범위를 통제하세요.

## 4. 실행

```bash
modernize plan execute --plan-name orders-aca --language java --source . --no-tty
```

이 랩에서는 약 **20분**이 걸렸습니다. 작업마다 **git 커밋이 하나씩** 생깁니다.

```bash
git log --oneline
```

```text
c585d4d Update tasks.json: mark task 005 as success
afb776f Task 005: Containerize application with multi-stage Dockerfile for Tomcat 10.1/Java 17
15da9f4 Task 004: Remediate CVEs in dependencies
dd9b6cb Task 003: redirect application logging from file to console
f978414 task 002: externalize DB and upload-dir config to environment variables
87570b0 Task 001: Upgrade to Java 17 and Jakarta EE 10
dceab3f baseline
```

## 5. 결과 검토

AI가 바꾼 코드는 **반드시 사람이 읽습니다.** 핵심만 보면 다음과 같습니다.

| 변경 | 내용 |
|---|---|
| `pom.xml` | Java **17**, `jakarta.servlet-api` **6.0.0**, MySQL **Connector/J 9.3.0**, `commons-fileupload2`, log4j 1.x → **reload4j** (드롭인 대체) |
| `DbConfig.java` | `db.properties` 삭제. 환경 변수 `DB_HOST`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`, `DB_SSL_MODE`, `UPLOAD_DIR` 등으로 설정. 코드에 비밀번호가 없음 |
| `log4j.properties` | 파일 appender → **ConsoleAppender (System.out)** |
| `*Servlet.java`, `web.xml` | `javax.servlet.*` → `jakarta.servlet.*` |
| `Dockerfile` | Maven으로 WAR 빌드 → `tomcat:10.1-jre17`에 `ROOT.war`로 배포, 비루트 사용자 |

각 작업의 상세 설명은 `.github/modernize/orders-aca/00x-*/modernization-summary.md`에 남습니다. 비교용 정답은 저장소의 [`apps/legacy-orders-modernized`](https://github.com/Taeyangmoon/azure-catalyst-lab/tree/main/apps/legacy-orders-modernized)에 있습니다. AI 결과는 매번 조금씩 다르니 **같지 않아도 정상**입니다.

{: .warning }
**남은 한계**: 업로드 파일은 여전히 컨테이너의 `/tmp/uploads`(임시 디스크)에 저장됩니다. 컨테이너가 교체되면 사라집니다. 운영에서는 **Azure Blob Storage**나 **Azure Files**로 옮겨야 합니다. 평가 이슈 중 "File system - Java IO"는 환경 변수화로 **완전히 해결된 것이 아니라 완화**된 것입니다.

## 6. Azure에 배포

만들 리소스: Azure Database for MySQL(Burstable B1ms), Azure Container Registry(Basic), Container Apps 환경과 앱.

```bash
cd azure-catalyst-lab
./scripts/deploy-orders.sh apps/legacy-orders-modernized      # 직접 만든 결과를 쓰려면 ~/orders
```

스크립트가 하는 일:

1. 리소스 공급자 등록 (`Microsoft.DBforMySQL` 등. 처음 쓰는 구독에서는 **등록하지 않으면 실패**합니다.)
2. 리소스 그룹 `rg-orders-lab`
3. **MySQL 유연 서버** 생성 (약 5~8분)
4. **ACR 만들고 `az acr build`** 로 이미지 빌드 (로컬 Docker 불필요, 약 1분)
5. Container Apps 환경과 `orders-web` 앱 생성. DB 비밀번호는 **비밀(secret)** 로 넣고 `secretref:`로 참조

핵심 부분만 보면:

```bash
az containerapp create -g $RG -n orders-web --environment orders-env \
  --image $ACR.azurecr.io/orders:v1 \
  --registry-server $ACR.azurecr.io --registry-identity system --system-assigned \
  --ingress external --target-port 8080 --min-replicas 0 --max-replicas 2 \
  --secrets db-password="$DB_PASSWORD" \
  --env-vars DB_HOST=$MYSQL.mysql.database.azure.com DB_NAME=orders DB_USER=ordersadmin \
             DB_PASSWORD=secretref:db-password UPLOAD_DIR=/tmp/uploads DB_SSL_MODE=REQUIRED
```

레지스트리 인증은 비밀번호 대신 앱의 **시스템 할당 관리 ID**(`--registry-identity system`)로 합니다.

## 7. 검증

**Azure Portal**에서 `rg-orders-lab`을 열면 아래 리소스가 보입니다.

![리소스 그룹]({{ '/images/ko/g1-rg-orders-lab.png' | relative_url }})

`orders-web` Container App의 **Application Url**을 엽니다.

![Container App 개요]({{ '/images/ko/g2-containerapp-overview.png' | relative_url }})

주문을 입력하고 **주문**을 눌러 보세요. 데이터가 **Azure Database for MySQL**에 저장됩니다.

![앱 실행]({{ '/images/ko/g3-app-running.png' | relative_url }})

**Monitoring → Log stream**에서 **Application** 로그를 보면, 3번 작업으로 바꾼 로그가 **stdout을 통해 수집**되는 것을 볼 수 있습니다. (`주문 접수: 홍길동 / 노트북 x 2` 같은 줄)

![Log stream]({{ '/images/ko/g4-log-stream.png' | relative_url }})

CLI로도 확인할 수 있습니다.

```bash
curl -s https://<앱 주소>/health            # OK
az containerapp logs show -g rg-orders-lab -n orders-web --tail 20 --format text
```

{: .note }
컨테이너 앱은 `--min-replicas 0`이라 한동안 요청이 없으면 **0개로 줄었다가** 첫 요청에 다시 켜집니다. 첫 응답이 몇 초 느릴 수 있습니다.

## 비용 (정가 기준 추정)

| 리소스 | 대략 |
|---|---|
| MySQL Flexible Server B1ms | 시간당 약 $0.02 |
| ACR Basic | 하루 약 $0.17 |
| Container Apps | 요청이 없으면 0으로 축소, 소량 사용은 무료 구간 안 |
| Log Analytics | 소량 수집 |

실습은 3시간 안에 끝나므로 총 **$0.3 안팎**으로 예상되지만, **구독의 가격에 따라 다르니 Cost Management에서 확인**하세요. 실습이 끝나면 [정리]({{ '/lab8-cleanup/' | relative_url }})에서 **반드시 삭제**합니다.

## 생각해 볼 질문
1. 평가의 **Mandatory 4건** 중 이번 현대화로 **완전히 해결된 것**과 **완화에 그친 것**은 무엇일까요?
2. AI가 계획에 추가한 **004(CVE 점검)** 는 평가 보고서에 없던 작업입니다. 이런 작업을 받아들일지는 누가 결정해야 할까요?
3. 계획에 "Azure에 배포" 작업이 들어 있었다면, 실행 전에 어떤 위험을 확인해야 했을까요?

➡️ 다음: [8. 정리]({{ '/lab8-cleanup/' | relative_url }})
