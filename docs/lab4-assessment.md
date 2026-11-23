---
permalink: /lab4-assessment/
title: "4. 평가와 마이그레이션 전략"
nav_order: 6
---

# 4. 평가: 앱마다 어디로, 어떻게 옮길까?

⏱️ 약 15분 · 💰 추가 비용 없음

비즈니스 케이스를 만들면 Catalyst가 **평가(Assessment)** 도 함께 만들어 줍니다. 평가는 애플리케이션마다 **마이그레이션 전략(6R), Azure 대상 서비스, 준비 상태, 월 비용**을 보여 줍니다.

## 배울 것
- **경로(Path)** 비교: PaaS preferred / PaaS only / Lift and shift
- **Strategy distribution**: 앱별 Rehost, Replatform 분포
- **애플리케이션 단위**로 전략과 비용 읽기

## 1. 평가 목록

**Plan → Assessments**를 엽니다. 비즈니스 케이스를 만들 때 자동으로 생성된 `bc-contoso-apps-asmt`가 있습니다.

![평가 목록]({{ '/images/ko/e1-assessments-list.png' | relative_url }})

{: .note }
이 캡처에는 3장의 비교 실험에서 만든 `bc-contoso-korea-asmt`(앱 없이 만든 비즈니스 케이스)도 함께 보입니다. 이 랩을 그대로 따라 하면 `bc-contoso-apps-asmt` 하나만 보입니다.

## 2. 평가 개요

`bc-contoso-apps-asmt`를 엽니다.

![평가 개요]({{ '/images/ko/e2-assessment-overview.png' | relative_url }})

| 항목 | 이 랩에서 나온 값 |
|---|---|
| Recommended path | **PaaS preferred** |
| 범위 | **5개 애플리케이션 (32개 워크로드)** |
| Azure readiness | **100% Ready** |
| Monthly cost estimate | 약 **$11,052** (Compute $5,393 / Storage $3,485 / Network $1,765 / Security $403) |
| Strategy distribution (앱 기준) | **Replatform 60%**, **Rehost 40%** |
| Lift and shift 경로 | 월 약 **$9,124** (전부 Rehost) |

{: .note }
**PaaS preferred(월 약 $11,052)가 Lift and shift(약 $9,124)보다 비싼데 왜 권장일까요?** 평가의 월 비용은 **인프라 비용 비교**입니다. PaaS 경로의 장점(관리 부담과 패치 감소, 가용성)은 3장의 **비즈니스 케이스**(IT 인력/관리 절감, Value uplift)에서 보입니다. 이 숫자에는 **네트워크 비용($1,765)** 도 포함됩니다. 비즈니스 케이스에서 Platform Landing Zone을 포함했기 때문입니다.

## 3. 경로 상세 (View path details)

**PaaS preferred**의 **View path details**를 엽니다. 앱마다 전략이 나옵니다.

![경로 상세]({{ '/images/ko/e3-path-details.png' | relative_url }})

| 앱 | 전략 | 월 비용 (USD) | 워크로드 |
|---|---|---:|---:|
| `Application_shr_3` (공용 인프라) | **Rehost** | 3,633.56 | 6 |
| `Application_ord` (주문 포털) | **Rehost** | 1,360.82 | 8 |
| `Application_erp` (레거시 ERP) | **Replatform** | 1,446.03 | 5 |
| `Application_hrm` (인사) | **Replatform** | 1,376.50 | 5 |
| `Application_rpt` (리포팅/BI) | **Replatform** | 2,783.25 | 3 |

**Replatform** 앱(ERP, HRM, RPT)은 SQL Server가 **Azure SQL Managed Instance** 같은 PaaS 대상이 되는 앱입니다. **Rehost** 앱(공용 인프라, 주문 포털)은 서버를 Azure VM으로 옮기는 것으로 평가되었습니다.

## 4. 앱 상세 (Application design)

**Plan → Application design**에서 `Application_ord`를 엽니다.

![Application_ord 설계]({{ '/images/ko/h5-app-design-ord.png' | relative_url }})

| 항목 | `Application_ord` |
|---|---|
| Application readiness | **Ready with conditions** (8개 워크로드 모두 "조건부 준비") |
| Migration strategy | **Rehost** |
| 월 비용 | $1,360.82 (Compute 626 / Storage 202 / Security 119 / Network 412) |
| 탭 | Servers(8), Network(39), Peripherals(19) 등 |

**Code insights** 상자에는 이렇게 적혀 있습니다. *"Code insights는 **Replatform과 Refactor** 전략에만 적용됩니다. 이 앱은 Rehost로 권장되었습니다."* 이 앱의 Tomcat/Java 코드를 현대화(7장)하려면 Catalyst 평가의 Code insights가 아니라 **별도로 Modernize CLI를 실행**해야 한다는 뜻이기도 합니다.

{: .note }
`Ready with conditions`가 무엇 때문인지는 **Show readiness issue/warnings**에서 확인하세요. 이 랩에서 서버 8대의 OS 버전이나 지원 종료 상태가 영향을 줬을 가능성이 있지만, 정확한 원인은 직접 열어서 확인하지 않았습니다.

## 확인 문제
1. **Rehost**로 평가된 `Application_ord`를 **Replatform**으로 바꾸려면 어떤 정보가 더 필요할까요? (힌트: 4번 절의 Code insights 설명과 2장의 Web apps 0건)
2. ERP는 **Windows Server 2012 R2 + SQL Server 2012**입니다. Replatform으로 평가된 것이 "지금 바로 옮겨도 안전하다"는 뜻일까요?
3. 평가의 **Rehost 40% / Replatform 60%** 는 **앱 기준**입니다. 워크로드 기준 비율과 같을까요? (5장의 에이전트 답변과 비교해 보세요.)

➡️ 다음: [5. Catalyst 에이전트]({{ '/lab5-agent/' | relative_url }})
