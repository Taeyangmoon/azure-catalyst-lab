---
permalink: /lab6-plan/
title: "6. 웨이브 계획과 랜딩 존"
nav_order: 8
---

# 6. 계획: 무엇부터 옮기고, 어디에 옮길까

⏱️ 약 20분 · 💰 추가 비용 없음

평가를 바탕으로 **웨이브 계획**(옮기는 순서)과 **Platform Landing Zone**(옮길 대상 환경의 뼈대)을 만듭니다.

{: .note }
이 랩에서는 **계획까지만** 합니다. 웨이브를 **Commit**하거나 랜딩 존을 **배포**하지 않습니다. 실제로 옮기려면 Appliance 기반 discovery와 복제 도구가 필요합니다.

## 배울 것
- **Wave plan**의 구성과 **위험(Risk)** 읽기
- 웨이브보다 **먼저 필요한** Platform Landing Zone
- 자동 생성된 **설계 문서(HLD/LLD)** 읽기

## 1. 웨이브 계획 만들기

**Plan → Assessments**에서 `bc-contoso-apps-asmt`를 열고, 권장 경로 **PaaS preferred**의 **View path details**에서 **Generate wave plan**을 누릅니다.

![웨이브 계획 만들기]({{ '/images/ko/h1-generate-wave-plan.png' | relative_url }})

몇 분 뒤 **Plan → Wave plan**에 계획이 나타납니다.

![웨이브 계획]({{ '/images/ko/h3-wave-plan.png' | relative_url }})

| 항목 | 이 랩에서 나온 값 |
|---|---|
| Waves | **1** (Wave1, 상태 **Proposed**) |
| Applications / Workloads | 5 / 32 |
| Risk | **2** |
| 월 Azure 비용 | $9,287.40 |

웨이브가 **1개**뿐입니다. 정확한 이유는 화면이 단정하지 않지만, 아래 **위험 01**이 중요한 단서를 줍니다.

### 위험과 권고

![웨이브 위험]({{ '/images/ko/h4-wave-risks.png' | relative_url }})

| 위험 | 심각도 | 내용 | 권고 |
|---|---|---|---|
| 01 | **Medium** | **의존성 분석 데이터가 없어서** 부분 입력으로 만든 계획 (27개 워크로드 영향) | **dependency analysis**를 켜고 데이터를 모은 뒤 다시 생성하거나, **태그**로 함께 옮길 워크로드를 묶기 |
| 02 | **High** | **복잡도가 높은 앱이 이른 웨이브(Wave1)** 에 있음 (`Application_ord`, 8개 워크로드) | 복잡한 앱을 나중 웨이브로 옮기거나, 일찍 옮긴다면 **리허설과 롤백 확인** |

**Wave plan reasoning**에는 이렇게 적혀 있습니다. "각 애플리케이션의 워크로드는 한 웨이브에 둔다. 준비도가 높고, 단순하고, 업무 중요도가 낮고, 노력이 적게 드는(rehost 먼저) 워크로드가 앞선 웨이브로 간다." 이 랩의 **파일 가져오기에는 의존성 정보가 없고**, 그 상태에서 만든 계획은 웨이브가 하나뿐이며 복잡도가 가장 높은 `Application_ord`가 Wave1에 들어갔습니다. **계획을 그대로 믿으면 안 되는** 좋은 예입니다.

{: .note }
웨이브를 직접 고치려면 **Configure → Waves**에서 매핑 파일을 내려받아 편집하고 다시 올립니다. 이 랩에서는 파일을 내려받지 않았습니다.

## 2. Platform Landing Zone

웨이브 계획 화면의 안내문은 이렇게 말합니다. **"Platform Landing Zone은 어떤 웨이브보다 먼저 구축해야 한다."** 네트워크, 아이덴티티, 보안, 거버넌스의 공통 기반이기 때문입니다. 예상 비용은 **월 $451.95**이며 웨이브 비용에는 **포함되지 않습니다.**

**Plan → Application design**을 엽니다. 앱 목록 위의 **Related artifacts**에 **Wave plan**과 **Platform landing zone**이 있습니다.

![Application design]({{ '/images/ko/h2-application-design.png' | relative_url }})

**Platform landing zone**을 선택합니다.

![Platform Landing Zone]({{ '/images/ko/h7-platform-landing-zone.png' | relative_url }})

Catalyst가 **설계 문서**(Platform Landing Zone - Design Document)를 만들어 줍니다.

| 항목 | 내용 |
|---|---|
| 구조 | **단일 리전 Hub-and-Spoke**, **Azure Firewall Basic** |
| 규모 | **Lite** 티어 |
| 기반 | Azure Landing Zone(ALZ) accelerator, CAF/WAF 지침 |
| 구성요소 | 관리 그룹 계층, 플랫폼 구독, 허브 VNet, Azure Firewall, VPN Gateway, Log Analytics + Defender for Cloud |
| 구성 | **A. HLD**(아키텍트용 상위 설계), **B. LLD**(IaC 엔지니어용 리소스별 값) |

### 지역을 Korea Central로 바꾸기

{: .warning }
Catalyst가 처음 만든 Platform Landing Zone의 **Region은 EASTUS**입니다. 비즈니스 케이스에서 Korea Central을 지정했어도 **기본 PLZ는 따로 생성**되기 때문입니다. 그리고 아래 **View settings**에는 **지역을 고르는 항목이 없습니다.** 지역은 **Catalyst 에이전트**로 바꿉니다. (Learn의 Platform Landing Zone 문서도 에이전트와 대화로 생성하도록 안내합니다.)

1. 상단 **Copilot**에서 **Catalyst** 에이전트를 열고 `catalyst-lab` 프로젝트를 선택합니다.
2. 아래처럼 요청합니다.

```text
I want to create a Platform Landing Zone (PLZ). Use Korea Central as the Azure region.
```

에이전트는 **Single-region (Korea Central)** 로 구성안을 제안합니다. 이때 기본 제안은 **Full 티어**(Azure Firewall **Premium**, DDoS, ExpressRoute Gateway, Bastion, Private DNS 모두 **On**)입니다. 실습에서는 비용과 단순함을 위해 **Lite**로 유지하겠다고 다시 요청합니다.

```text
Keep the Lite scale tier with Azure Firewall Basic, Hub and Spoke, region Korea Central. Turn off DDoS Protection, ExpressRoute Gateway, Azure Bastion and Private DNS. Use GitHub for deployment. Generate the Platform Landing Zone now.
```

에이전트가 생성을 시작하고, **설계 문서와 IaC ZIP**이 비동기로 만들어진다고 안내합니다(약 2분). 완료 후 **Application design → Platform landing zone**을 다시 열면 설계 문서의 **Region이 KOREACENTRAL**로 바뀌어 있습니다.

![에이전트로 Korea Central 요청]({{ '/images/ko/h10-agent-plz-request.png' | relative_url }})

| 항목 | 처음 (기본) | 에이전트 요청 후 |
|---|---|---|
| Region | EASTUS | **KOREACENTRAL** |
| 티어 / 방화벽 | Lite / Azure Firewall Basic | Lite / Azure Firewall Basic (동일) |
| 예상 월 비용 | $451.95 | $451.95 (표시된 값은 동일) |

{: .note }
에이전트가 제안한 **Full 티어를 그대로 수락하면** 구성요소가 크게 늘어 비용도 올라갑니다. 이 랩에서는 Full 티어의 비용은 확인하지 않았습니다. 또 생성된 **IaC ZIP(Terraform)** 은 내려받아 VS Code에서 다듬고 배포할 수 있지만([문서](https://learn.microsoft.com/azure/migrate/platform-landing-zone)), 이 랩에서는 **배포하지 않았고 ZIP도 내려받지 않았습니다.**

### 설정 바꾸기 (View settings)

![설정]({{ '/images/ko/h8-plz-settings.png' | relative_url }})

설정에서 **구독 배치**(Identity, Connectivity, Management, Security), **네트워크 선호**(Hub and spoke 또는 Virtual WAN), **방화벽**(Azure Firewall 또는 타사 NVA), **배포 방식**(GitHub, Azure DevOps, 로컬 파일 시스템)과 **DDoS Protection, ExpressRoute Gateway, Private DNS, Bastion** 옵션을 고릅니다.

## 3. IaC 생성 (살펴보기)

`Application_ord`를 열면 상단에 **Generate IaC** 버튼이 있습니다. 누르면 앱 설계를 코드로 만드는 작업이 시작됩니다.

{: .warning }
이 랩에서는 버튼을 눌러 **작업이 시작되는 것까지만** 확인했고, 만들어진 IaC 파일의 내용과 위치는 확인하지 못했습니다. 이 기능은 직접 살펴보세요.

## 확인 문제
1. 웨이브 계획이 1개로 끝난 것을 개선하려면 무엇을 해야 할까요? (힌트: 위험 01의 권고)
2. 위험 02(High)를 해소하려고 `Application_ord`를 나중 웨이브로 옮기면, 설계상 어떤 **종속성**을 먼저 확인해야 할까요?
3. Platform Landing Zone이 웨이브 비용에 **포함되지 않는** 이유는 무엇일까요?

➡️ 다음: [7. Java 앱 현대화와 배포]({{ '/lab7-java-modernize/' | relative_url }})
