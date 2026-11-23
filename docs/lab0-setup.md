---
permalink: /lab0-setup/
title: "0. 준비"
nav_order: 2
---

# 0. 준비: 구독, 리소스 공급자, Catalyst 프로젝트

⏱️ 약 15분 · 💰 Catalyst 자체는 **무료**입니다. 프로젝트를 만들면 메타데이터를 저장하는 **스토리지 계정**이 하나 생기며, 마지막 모듈에서 삭제합니다.

## 이 모듈에서 하는 일
1. 실습용 샘플 데이터 받기
2. Azure 리소스 공급자 등록
3. 리소스 그룹 만들기
4. **Azure Catalyst 프로젝트** 만들기

## 사전 요건

| 항목 | 설명 |
|---|---|
| Azure 구독 | 리소스 그룹과 프로젝트를 만들 수 있는 **Owner 또는 Contributor** 권한 |
| Azure Copilot | 테넌트에서 **Azure Copilot가 켜져 있어야** Catalyst 에이전트를 쓸 수 있습니다. 관리자가 접근을 제어합니다. |
| 브라우저 | Azure Portal에 로그인할 수 있는 최신 브라우저 |
| (선택) Azure CLI | 아래 `Azure CLI` 탭 명령을 쓸 때 필요합니다. |

{: .note }
이 랩은 **서버를 만들지 않습니다.** 실제 온프렘 환경이나 Appliance가 없어도 됩니다.

## 1. 샘플 데이터 받기

저장소의 `data/` 폴더에서 아래 파일을 받습니다.

| 파일 | 설명 |
|---|---|
| [`contoso-korea-cmdb.zip`](https://github.com/Taeyangmoon/azure-catalyst-lab/raw/main/data/contoso-korea-cmdb.zip) | 1단계에서 **그대로 업로드**할 ZIP (CMDB 내보내기 + 컨텍스트 파일) |
| `servicenow_cmdb_export.csv` | ZIP 안의 ServiceNow CMDB 내보내기 (31행) |
| `context-naming-conventions.md` | ZIP 안의 컨텍스트 파일 (열 의미, 단위, 제외 규칙) |

ZIP은 풀지 않고 그대로 씁니다. 내용이 궁금하면 CSV를 열어 보세요. 일부러 **폐기(Retired) 서버 2대, 대소문자만 다른 중복 1건, 값이 빈 서버 1대**를 넣었습니다. 이 "지저분한 데이터"를 Catalyst가 어떻게 다루는지가 1단계의 핵심입니다.

## 2. 리소스 공급자 등록

Catalyst는 `Microsoft.Migrate`와 `Microsoft.OffAzure`를 사용합니다. 이미 등록되어 있으면 건너뜁니다.

{% raw %}
<details open markdown="1">
<summary><b>Azure CLI</b></summary>

```bash
az login
az account set --subscription "<구독 이름 또는 ID>"

az provider register --namespace Microsoft.Migrate
az provider register --namespace Microsoft.OffAzure
az provider show --namespace Microsoft.Migrate --query registrationState -o tsv
```

`Registered`가 나오면 완료입니다.
</details>

<details markdown="1">
<summary><b>Azure Portal</b></summary>

**구독** → 해당 구독 → **리소스 공급자**에서 `Microsoft.Migrate`, `Microsoft.OffAzure`를 선택하고 **등록**을 누릅니다.
</details>
{% endraw %}

## 3. 리소스 그룹 만들기

{% raw %}
<details open markdown="1">
<summary><b>Azure CLI</b></summary>

```bash
az group create --name rg-catalyst-lab --location koreacentral \
  --tags purpose=catalyst-lab
```
</details>

<details markdown="1">
<summary><b>Azure Portal</b></summary>

**리소스 그룹** → **만들기** → 이름 `rg-catalyst-lab`, 지역 **Korea Central** → **검토 + 만들기**.
</details>
{% endraw %}

## 4. Catalyst 프로젝트 만들기

1. Azure Portal 검색창에 `Azure Catalyst`를 입력하고 서비스를 엽니다.
2. 왼쪽 **All projects**에서 **Create project**를 선택합니다.
3. 아래 값을 입력하고 **Create**를 누릅니다.

| 항목 | 값 |
|---|---|
| Subscription | 실습에 사용할 구독 |
| Resource group | `rg-catalyst-lab` |
| Project | `catalyst-lab` |
| Geography | **Korea** |
| Artifact store | **Use same subscription and resource group as project** 체크 (스토리지 계정 이름은 자동 생성) |

![프로젝트 만들기]({{ '/images/ko/a1-create-project-form.png' | relative_url }})

{: .warning }
**Geography**는 프로젝트 메타데이터가 저장되는 위치입니다. 만든 뒤에는 바꿀 수 없습니다.

프로젝트 생성은 1~2분 정도 걸립니다. 완료되면 **All projects**에 `catalyst-lab`이 나타납니다.

## 확인

{% raw %}
<details open markdown="1">
<summary><b>Azure CLI</b></summary>

```bash
az resource list -g rg-catalyst-lab --query "[].{name:name,type:type}" -o table
```

`Microsoft.Migrate/migrateprojects`와 `Microsoft.Storage/storageAccounts`가 하나씩 보이면 정상입니다.
</details>
{% endraw %}

{: .note }
**비용 안내**: Catalyst는 무료이고 스토리지 계정은 몇 개의 작은 파일만 저장하므로 비용이 거의 없습니다. 그래도 랩이 끝나면 [정리]({{ '/lab8-cleanup/' | relative_url }}) 단계에서 리소스 그룹을 삭제하세요.

➡️ 다음: [1. 발견: AI 파일 가져오기]({{ '/lab1-discover/' | relative_url }})
