---
permalink: /lab2-applications/
title: "2. 애플리케이션과 보안 위험"
nav_order: 4
---

# 2. 애플리케이션: 서버가 아니라 앱을 옮긴다

⏱️ 약 20분 (자동 발견 대기 10~15분 포함) · 💰 추가 비용 없음

인벤토리에는 서버 28대가 **낱개로** 들어 있습니다. 하지만 고객이 이전을 결정하는 단위는 서버가 아니라 **"주문 포털", "인사 시스템" 같은 애플리케이션**입니다. 이 모듈에서는 서버를 앱 단위로 묶고, 기다리는 동안 보안 위험을 살펴봅니다.

{: .note }
Ignite BRK409의 핵심 메시지 중 하나가 "서버를 마이그레이션하는 것이 아니라 **애플리케이션을 마이그레이션**한다"입니다. Catalyst의 비즈니스 케이스, 웨이브 계획, 랜딩 존 설계는 모두 **앱 단위** 정보가 있어야 제대로 동작합니다.

## 배울 것
- **Auto discover applications**: 서버 이름 패턴으로 앱을 자동 그룹핑
- 앱의 **복잡도, 비즈니스 중요도, 신뢰도 점수** 읽기
- **Security Insights**로 지원 종료 위험 확인

## 1. 애플리케이션 자동 발견

1. **Discover → Applications**를 엽니다. 아직 앱이 없으면 **No applications** 화면이 보입니다.
2. **Auto discover**를 선택합니다.
3. 오른쪽 창의 안내를 읽고 **Discover applications**를 누릅니다.

![Auto discover]({{ '/images/ko/b1-auto-discover-apps.png' | relative_url }})

화면의 설명처럼 Catalyst는 **서버 이름 패턴, 추정한 환경, 서버 역할**을 이용해 앱을 묶습니다. 이 랩의 서버 이름은 `krc-<앱코드>-<역할>-<번호>` 규칙을 따르기 때문에 잘 묶입니다.

{: .warning }
완료까지 공식 문서는 **최대 1시간**이라고 안내합니다. 이 랩에서는 **약 13분**이 걸렸습니다. 버튼을 누르고 알림에 "initiated successfully"가 뜨는지 꼭 확인하세요. 알림이 없으면 시작되지 않은 것입니다.

## 2. 기다리는 동안: Security Insights

**Decide → Security Insights**를 엽니다.

![Security Insights]({{ '/images/ko/c6-security-insights.png' | relative_url }})

28대 중 **10대가 운영체제 지원 종료(End of support)** 입니다. Windows Server 2012 R2/2008 R2와 RHEL 7.9입니다. "왜 지금 현대화해야 하는가"의 근거가 인벤토리만으로 생겼습니다.

{: .note }
Software 위험(`Software end of support`, `With vulnerabilities`)이 0으로 나오는 이유는 **파일 가져오기에 설치된 소프트웨어의 정밀 정보가 없기 때문**입니다. Appliance로 발견하면 더 많은 위험이 보입니다.

## 3. 결과 확인

자동 발견이 끝나면 **Applications**에 아래와 같이 앱 5개가 나타납니다. 안 보이면 **Refresh**를 누르세요.

![Applications]({{ '/images/ko/f1-applications.png' | relative_url }})

| 앱 | 워크로드 수 | 복잡도 | 신뢰도 |
|---|---:|---|---:|
| `Application_ord` (주문 포털) | 8 | **High** | 90 |
| `Application_hrm` (인사) | 7 | Medium | 92 |
| `Application_erp` (레거시 ERP) | 6 | Medium | 82 |
| `Application_shr_3` (공용 인프라) | 6 | Medium | 90 |
| `Application_rpt` (리포팅/BI) | 5 | Medium | 93 |

- 합계는 **32개 워크로드**입니다. 전체 33개 중 1개는 어떤 앱에도 속하지 않습니다.
- `Application_ord`만 복잡도가 **High**입니다. 웹 서버, 앱 서버, DB가 모두 있고 서버 수가 가장 많기 때문입니다.
- **신뢰도(Confidence score)** 가 가장 낮은 앱은 `Application_erp`(82)입니다. 이름 패턴만으로는 불확실한 부분이 있다는 뜻이며, 실제 프로젝트에서는 이런 앱을 **사람이 검토**해야 합니다.

{: .note }
앱 이름은 `Application_ord`처럼 시스템이 붙인 이름입니다. 앱을 열어 이름과 속성(비즈니스 중요도 등)을 고칠 수 있습니다. 서버 이름만으로 앱이 맞게 묶였는지 **눈으로 확인**하세요.

## 확인 문제
1. `Application_ord`를 열어 어떤 서버가 포함되어 있는지 확인하세요. 스테이징/개발 서버(`-s1`, `-dev-`)도 같은 앱에 들어 있나요?
2. 33개 중 앱에 묶이지 않은 1개의 워크로드는 무엇일까요? (힌트: All inventory의 **Application name(s)** 열)

➡️ 다음: [3. 비즈니스 케이스]({{ '/lab3-business-case/' | relative_url }})
