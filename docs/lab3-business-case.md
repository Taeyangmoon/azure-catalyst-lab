---
permalink: /lab3-business-case/
title: "3. 비즈니스 케이스"
parent: 한국어
nav_order: 5
---

# 3. 비즈니스 케이스: 옮기면 얼마나 이득인가?

⏱️ 약 20분 (계산 대기 8~15분 포함) · 💰 추가 비용 없음

서버를 하나도 옮기기 전에, **온프렘 비용과 Azure 비용을 비교**해 경영진에게 보여 줄 첫 숫자를 만듭니다. 2장에서 만든 **애플리케이션**을 범위로 지정하는 것이 이 모듈의 핵심입니다.

## 배울 것
- 비즈니스 케이스의 **범위(scope)** 가 왜 중요한가
- **PaaS preferred**와 **Lift and shift** 경로 비교
- 비용 절감과 **Azure Value Uplift**를 합친 **Total economic benefit** 읽기

## 1. 비즈니스 케이스 만들기

1. **Decide → Business case → Create business case**를 선택합니다.
2. **Name**에 `bc-contoso-apps`를 입력합니다. **Configuration**에서 **Define configuration**을 고르고 **Define configuration** 버튼을 누릅니다.

### Basics 탭: 범위는 "애플리케이션"으로

**Scope**에서 **Select workloads or applications**를 선택하고 **Add applications**를 누릅니다. 앱 5개를 모두 선택하고 **Add**를 누릅니다. 자동 발견된 앱이라는 확인 창이 뜨면 **Confirm**을 누릅니다.

![앱 선택]({{ '/images/shots/c1b-bc-select-apps.png' | relative_url }})

**5 applications (with 32 workloads) selected**라고 나오면 됩니다.

![Basics]({{ '/images/shots/c1-bc-basics.png' | relative_url }})

{: .warning }
**범위는 만든 뒤에 바꿀 수 없습니다.** 앱을 정의하기 전에 **Entire data center**로 만들면 [아래 "앱 없이 만들면"](#앱-없이-만들면)처럼 결과가 달라지고, **Recompute**로도 고쳐지지 않습니다.

### General 탭

| 설정 | 값 | 이유 |
|---|---|---|
| Default target location | **Korea Central** | 비용 계산 기준 지역 |
| Migration preference | **Modernize (AI-ready)** | 가능한 워크로드는 IaaS보다 **PaaS를 우선** |
| Default savings option | 3 years reserved as applicable | 예약 인스턴스 3년 가정 |
| Include Platform Landing Zone recommendations | **체크** | 6장 랜딩 존에서 사용 |
| Sizing criteria | **As on-premises** | 파일 가져오기에는 **성능 데이터가 없음** |

![General]({{ '/images/shots/c2-bc-general.png' | relative_url }})

{: .warning }
**Performance-based**(성능 기반)는 CPU/메모리 사용률 이력이 필요합니다. 파일 가져오기로 만든 인벤토리에는 이력이 없으므로 **As on-premises**(현재 사양 기준)를 선택해야 합니다.

### Advanced 탭과 생성

Advanced는 기본값 그대로 **Save**를 누르고, **Create business case**를 누릅니다.

![Advanced]({{ '/images/shots/c3-bc-advanced.png' | relative_url }})

![Create]({{ '/images/shots/c4-bc-create.png' | relative_url }})

## 2. 결과 읽기

계산에는 **약 8~15분**이 걸립니다. 상태가 **Ready**가 되면 `bc-contoso-apps`를 엽니다.

![비즈니스 케이스 개요]({{ '/images/shots/c5-bc-overview.png' | relative_url }})

| 항목 | PaaS preferred (권장) | Lift and shift |
|---|---:|---:|
| 온프렘 비용 (연간) | $314.76K | $314.76K |
| Azure 미래 비용 | $201.83K | $195.58K |
| 비용 절감 (Savings) | $112.94K | **$119.18K** |
| **Azure value uplift** | **$106.69K** | $67.81K |
| **Total economic benefit** | **$219.63K** | $186.99K |

- **Total economic benefit = 비용 절감 + Azure value uplift** 입니다.
- Value uplift는 세 가지로 쪼개집니다. PaaS preferred는 **Business agility $80K(75%)**, **Resiliency $24.3K(23%)**, Productivity $2.39K(2%)입니다.
- **PaaS preferred는 순수 비용 절감이 Lift and shift보다 적지만, Value uplift까지 더하면 총 편익이 더 큽니다.** 그래서 `Modernize (AI-ready)`를 선택했을 때 PaaS preferred가 권장 경로가 됩니다.

{: .note }
숫자는 **추정치**이며 실행 시점의 가격과 가정에 따라 달라집니다. 이 랩에서는 숫자 자체보다 **구조를 읽는 것**이 중요합니다.

## 앱 없이 만들면

비교를 위해 **Entire data center** 범위로 만든 비즈니스 케이스도 확인했습니다. 서버 28대와 SQL 5개를 통째로 넣은 경우입니다.

![앱 없이 만든 경우]({{ '/images/shots/f3-value-uplift-zero.png' | relative_url }})

**Azure value uplift가 $0**이고 "Not calculated for this business case"라고 나옵니다. 이유는 화면에 적힌 대로 **Azure Value Uplift는 애플리케이션마다 계산되기 때문**입니다. 이 비즈니스 케이스에는 앱이 없어서 33개 워크로드가 모두 "ungrouped"입니다. 해결책도 화면에 있습니다. **새 비즈니스 케이스를 만들어 앱을 범위에 포함**해야 합니다.

## 생각해 볼 질문
1. 절감을 만드는 항목과 Azure가 **오히려 더 비싼** 항목은 무엇일까요? (5장에서 에이전트에게 물어봅니다. 힌트: 스토리지, IT 인력, 관리 vs 네트워크, 컴퓨트와 라이선스)
2. 고객이 "그냥 비용만 보여 달라"고 하면 어느 경로를 권하시겠습니까? Value uplift는 얼마나 믿을 수 있을까요? (화면의 "published industry benchmarks, risk adjusted" 문구를 참고하세요.)
3. 앱 이름을 `Application_ord` 대신 `주문 포털`로 바꾸고 중요도를 High로 올리면 Value uplift가 어떻게 달라질까요? (직접 시험해 보세요.)

➡️ 다음: [4. 평가와 마이그레이션 전략]({{ '/lab4-assessment/' | relative_url }})
