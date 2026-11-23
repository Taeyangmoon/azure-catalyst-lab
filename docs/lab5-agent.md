---
permalink: /lab5-agent/
title: "5. Catalyst 에이전트"
nav_order: 7
---

# 5. Catalyst 에이전트: 무엇을 잘하고 어디까지 믿을까

⏱️ 약 20분 · 💰 추가 비용 없음

**Catalyst 에이전트**(구 Azure Copilot Migration Agent, Preview)는 프로젝트의 인벤토리, 앱, 비즈니스 케이스, 평가 데이터를 바탕으로 **자연어로 질문하고 분석**하게 해 줍니다. 이 모듈에서는 에이전트가 **잘 대답하는 질문**과 **한계가 있는 질문**을 직접 구분해 봅니다.

{: .warning }
에이전트는 **계획과 분석을 돕는 도구**입니다. 이 랩에서는 답변이 포털 화면과 **서로 다른 숫자**를 말하는 경우도 있었습니다. 에이전트 답변은 **반드시 포털의 원본 화면과 대조**하세요.

## 배울 것
- 에이전트 여는 법과 프로젝트 선택
- 에이전트가 **강한 질문**(비즈니스 케이스 해석)과 **약한 질문**(앱 단위 세부 전략)
- **검증하는 습관**

## 1. 에이전트 열기

1. Azure Portal 상단의 **Copilot**을 누릅니다.
2. **New chat** 옆 드롭다운에서 **Catalyst**를 선택합니다.
3. 입력창 아래 **Work in a project**에서 `catalyst-lab`을 선택합니다.

![에이전트 열기]({{ '/images/ko/d1-agent-open.png' | relative_url }})

{: .note }
에이전트가 보이지 않으면 테넌트에서 **Azure Copilot가 켜져 있는지**, 관리자가 접근을 허용했는지 확인하세요. 1장에서 **Review inventory**를 누르면 같은 에이전트가 오른쪽 패널에 열렸습니다.

## 2. 강한 질문: 비즈니스 케이스 해석

다음을 입력합니다.

```text
Summarize my business case bc-contoso-apps: on-premises cost, recommended path, future cost, savings and Azure value uplift. Why is the PaaS preferred path recommended even though its savings are lower than lift and shift?
```

![비즈니스 케이스 요약]({{ '/images/ko/d2-agent-business-case.png' | relative_url }})

에이전트는 약 1분 뒤에 **표와 차트**로 답했습니다. 3장에서 직접 읽은 숫자와 **일치**합니다.

| 항목 | 에이전트 답변 |
|---|---|
| 온프렘 / Azure 비용 | $314.76K / $201.83K (연간) |
| 절감 / Value uplift / 총 편익 | $112.94K / $106.69K / $219.63K |
| 절감을 만드는 항목 | **Storage $54.94K**, **IT labor $45.05K**, **Management $33.04K** 등 |
| Azure가 더 비싼 항목 | **Network -$13.74K**, **Compute & licensing -$21.80K** |
| 왜 PaaS preferred인가 | `Modernize (AI ready)` 선호 때문이며, 총 경제적 편익이 더 큼 |

이것이 3장의 첫 번째 "생각해 볼 질문"의 답입니다. 에이전트는 포털에서는 **펼쳐 봐야 보이는** 카테고리별 비교를 한 번에 정리해 줍니다.

## 3. 약한 질문 1: 앱 단위로 묶어 달라

앱을 만들기 전, 1장 직후에 이렇게 물어봤습니다.

```text
Group the servers into applications using the naming convention krc-<app>-<role>-<number> (ORD, HRM, ERP, RPT, SHR). For each application list its servers, environment and any end-of-support operating system or database.
```

**에이전트는 직접 묶지 않았습니다.** "여기서 서버를 직접 읽거나 재그룹할 수 없다"고 답하고, 대신 **Auto discover**, **CSV 가져오기**, **수동 정의**라는 세 가지 방법을 안내했습니다. 2장에서 한 **Auto discover**가 바로 그 방법입니다. 에이전트는 **실행 도구가 아니라 안내자**일 때가 있습니다.

## 4. 약한 질문 2: 앱별 전략을 설명해 달라

```text
My application assessment is named bc-contoso-apps-asmt. List its applications with workloads, complexity and business criticality, then explain the recommended migration strategy and Azure target for each application, and why ERP and HRM are replatform while ORD is rehost.
```

![평가 질문]({{ '/images/ko/d3-agent-6r.png' | relative_url }})

이번에는 에이전트가 **평가 전체 요약**만 말하고, **앱별 세부 내용을 말하지 못한다**고 인정했습니다. 그리고 포털에서 Applications 화면을 열어 보라고 권했습니다. 같은 질문을 처음에 **평가 이름 없이** 던졌을 때는 "어떤 평가를 쓸지 알려 달라"고 되물었습니다.

### 포털과 대조해 보기

| 항목 | 에이전트 | 포털 (4장) |
|---|---|---|
| 워크로드 수 | **27개** | **32개** |
| 전략 비율 | Rehost 81% / Replatform 19% (**워크로드 기준**) | Rehost 40% / Replatform 60% (**앱 기준**) |
| 앱별 전략 | 말하지 못함 | 앱 5개 각각 표시 |

두 숫자는 **서로 다른 기준**(워크로드 vs 앱)이거나 에이전트가 일부 데이터만 본 결과일 수 있습니다. 어느 쪽이든 **에이전트의 숫자만 믿고 고객에게 말하면 위험**합니다.

## 정리: 에이전트와 일하는 방법

| 잘하는 일 | 조심할 일 |
|---|---|
| 비즈니스 케이스 요약, 카테고리별 비교, **이유 설명** | 앱 단위 세부 전략, 워크로드 목록 |
| 다음에 할 일 안내 (Auto discover, Import 등) | "직접 실행해 줘" |
| 1장에서 본 **인벤토리 검토와 수정 제안** (병합, 값 채우기) | 질문의 **대상 이름**(평가, 비즈니스 케이스)을 정확히 말하지 않는 경우 |

{: .note }
에이전트 답변 언어는 **포털 언어 설정**을 따를 수 있습니다. 이 랩에서는 영어로 물었는데도 한국어로 답한 적이 있었습니다.

## 확인 문제
1. 에이전트의 "워크로드 27개"는 어디에서 나온 숫자일까요? 포털의 어느 화면에서 27을 찾을 수 있을까요? (힌트: 4장의 평가 화면이 워크로드를 서버, SQL 인스턴스, 앱 중 어떤 단위로 세는지 비교해 보세요.)
2. 고객 미팅에서 에이전트 답변을 인용한다면 어떤 문구를 붙여야 할까요?

➡️ 다음: [6. 웨이브 계획과 랜딩 존]({{ '/lab6-plan/' | relative_url }})
