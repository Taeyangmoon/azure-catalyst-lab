---
permalink: /lab1-discover/
title: "1. 발견: AI 파일 가져오기"
nav_order: 3
---

# 1. 발견: AI 파일 가져오기 (File import)

⏱️ 약 20분 (AI 분석 대기 5~10분 포함) · 💰 추가 비용 없음

고객이 **ServiceNow CMDB에서 내보낸 파일**을 그대로 올려 서버, SQL, 웹 서버 인벤토리를 만듭니다. Appliance도, 서버 접속 정보도 필요 없습니다.

{: .note }
**AI 기반 파일 가져오기는 Preview**입니다. 같은 파일이라도 AI가 해석하는 값(경고 개수 등)이 조금씩 다를 수 있습니다. 화면과 숫자가 이 문서와 약간 달라도 정상입니다.

## 배울 것
- 고객 도구(CMDB) 내보내기를 **템플릿 변환 없이** 올리는 방법
- **컨텍스트 파일**로 열 의미, 단위, 제외 규칙을 알려 주는 방법
- AI가 만든 제안을 **Catalyst 에이전트와 검토**하고 고치는 방법

## 왜 컨텍스트 파일이 중요한가

이번에 올릴 ZIP에는 CSV와 함께 `context-naming-conventions.md`가 들어 있습니다. 이 파일은 다음을 AI에게 알려 줍니다.

| 컨텍스트 파일의 내용 | 효과 |
|---|---|
| `install_status` 값 7 = 폐기(Retired). 제외 | 폐기 서버 2대가 인벤토리에서 빠짐 |
| 대소문자만 다른 서버 이름은 같은 서버 | 중복 후보를 인식 |
| `ram`은 MB, `disk_space`는 GB | 단위 해석 |
| `u_installed_software`에 DB/웹 서버 정보가 있음 | SQL Server, IIS, Tomcat을 인식 |

{: .warning }
**컨텍스트 파일은 첫 업로드에 CSV와 함께(또는 ZIP으로) 올립니다.** 그리고 **Save to inventory 후에는 가져온 메타데이터를 수정할 수 없습니다.** 저장 전에 충분히 검토하세요.

## 1. 가져오기 시작

1. Azure Portal에서 `catalyst-lab` 프로젝트를 엽니다. (Azure Catalyst → All projects → `catalyst-lab`)
2. **Overview**에서 **Start discovery**를 선택합니다.
3. **Using file import (AI-powered)** 를 선택하고, **File type**에서 **Inventory exports from ServiceNow, Ansible, Dynatrace, etc.** 를 고른 뒤 **Continue**를 누릅니다.

![발견 방법 선택]({{ '/images/ko/a2-discover-file-type.png' | relative_url }})

## 2. ZIP 업로드와 분석

1. **File source**의 찾아보기 아이콘을 눌러 [`contoso-korea-cmdb.zip`](https://github.com/Taeyangmoon/azure-catalyst-lab/raw/main/data/contoso-korea-cmdb.zip)을 선택합니다.
2. **Analyze file**을 누릅니다. 분석에는 **보통 3~6분**이 걸립니다.

![ZIP 선택]({{ '/images/ko/a3-file-selected.png' | relative_url }})

분석이 끝나면 요약이 나옵니다.

![분석 결과]({{ '/images/ko/a4-analysis-result.png' | relative_url }})

| 결과 항목 | 이 랩에서 나온 값 | 해석 |
|---|---|---|
| 분석한 파일 | 2개 (CSV + 컨텍스트) | 컨텍스트 파일도 함께 해석됨 |
| Servers | **29** | 원본 31행에서 **폐기 서버 2대 제외** |
| SQL instances | **5** | SQL Server가 설치된 서버에서 인식 (폐기 서버의 SQL 제외) |
| IIS / Tomcat web servers | 4 / 4 | `u_installed_software`에서 인식 |
| Priority items | CPU, 메모리, 디스크가 비어 있는 서버 1대 | `krc-tst-web-99` 값이 비어 있음 |
| 0 records were correlated or merged | 중복이 **아직 병합되지 않음** | `KRC-ORD-WEB-01`이 따로 남아 있음 |

{: .note }
경고의 정확한 숫자는 분석할 때마다 약간 다를 수 있습니다. 중요한 것은 **무엇이 고쳐져야 하는지** 스스로 읽어 내는 것입니다.

## 3. Catalyst 에이전트로 검토하고 고치기

1. 요약 오른쪽 아래의 **Review inventory**를 선택합니다. 오른쪽에 **Catalyst Agent** 패널이 열리고 요약을 보여 줍니다.
2. 아래 두 가지를 에이전트에게 요청합니다. (영문으로 입력해도, 한국어로 입력해도 됩니다.)

```text
Which server is missing CPU, memory and disk values? Also, KRC-ORD-WEB-01 is the same server as krc-ord-web-01 (names differ only by case). Please merge them and show me the proposed changes.
```

에이전트는 `krc-tst-web-99`의 값이 비어 있고, 중복 두 행을 **한 행으로 병합**했다고 답합니다. (서버 수 29 → 28)

```text
For krc-tst-web-99, use the same sizing as krc-ord-dev-01 (4 vCores, 8192 MB memory, 150 GB storage). Apply the change and confirm the final server count.
```

![에이전트로 검토]({{ '/images/ko/a5-review-with-agent.png' | relative_url }})

{: .note }
에이전트가 값을 바꾸는 것은 **저장 전의 제안**입니다. 에이전트는 이런 변경을 `import-logic.txt`(매핑 규칙)와 `AllInventory-*.csv`(값)에 반영합니다. 직접 고치고 싶으면 **Download file**로 받아 편집한 뒤 다시 올릴 수도 있습니다.

## 4. 인벤토리에 저장

**Save to inventory**를 선택합니다. 저장 뒤에는 수정할 수 없습니다.

![저장]({{ '/images/ko/a6-save-to-inventory.png' | relative_url }})

저장 후 인벤토리 처리에는 **10~15분** 정도 걸립니다. 처리 중에는 **All inventory**에 "Inventory upload using file import is in progress" 안내가 보입니다. 공식 문서는 평가 전에 **30분** 정도 기다리라고 권장하지만, 이 랩에서는 안내가 사라지면 다음 단계로 진행했습니다.

## 5. 결과 확인

**Discover → All inventory**를 엽니다.

![All inventory]({{ '/images/ko/a7-all-inventory.png' | relative_url }})

확인할 것:

| 확인 항목 | 기대 결과 |
|---|---|
| `krc-old-*` 서버 | 보이지 않음 (폐기 서버 제외) |
| `KRC-ORD-WEB-01` | 따로 보이지 않고 `krc-ord-web-01` 하나만 있음 (병합) |
| Security risks 열 | Windows Server 2012 R2/2008 R2, RHEL 7.9가 **지원 종료**로 표시됨 |
| Databases | SQL Server 인스턴스 5개 |

{: .warning }
**Web apps는 0건입니다.** 파일 가져오기에서는 Tomcat/IIS를 **웹 서버(소프트웨어)** 로만 인식합니다. 웹 앱(애플리케이션 단위) 평가와 **Code Insights**는 Appliance 기반 discovery가 필요합니다. 이 랩의 Java 현대화 모듈은 GitHub Copilot Modernize CLI로 직접 진행합니다.

## 문제 해결

| 증상 | 조치 |
|---|---|
| 분석이 **Failed**로 끝남 | 같은 프로젝트에서 **Retry with new file**을 쓰기보다 **새 프로젝트를 만들어 다시** 시도하는 것이 안전했습니다. (이 랩을 만들 때 재시도 후 계속 실패하는 프로젝트가 있었습니다.) |
| 컨텍스트 파일을 따로 올렸더니 실패 | 컨텍스트는 **첫 업로드에 ZIP으로 함께** 올려야 합니다. |
| 에이전트 패널이 열리지 않음 | **Review inventory**를 다시 누르거나 Azure Copilot가 켜져 있는지 확인하세요. |
| 저장했는데 인벤토리가 비어 있음 | 10~15분 정도 기다린 뒤 **Refresh** |

➡️ 다음: 2. 평가와 비즈니스 케이스 (작성 중)
