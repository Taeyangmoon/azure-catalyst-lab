---
title: 한국어
nav_order: 2
has_children: true
permalink: /ko/
---

# Azure Catalyst 핸즈온 랩

**ServiceNow CMDB 내보내기 파일 하나**에서 시작해, Azure Catalyst(구 Azure Migrate)로 **발견 → 애플리케이션 → 비즈니스 케이스 → 평가 → Catalyst 에이전트 → 계획**까지 해 보고, 마지막에는 레거시 **Java(Tomcat) 앱을 Azure Container Apps로 현대화**하는 실습입니다.

{: .note }
이 랩은 **Azure Portal**을 중심으로 진행하고, 가능한 단계에는 **Azure CLI** 대안을 함께 적었습니다. 모든 화면은 실제 Azure Portal에서 캡처했습니다.

## 이 랩의 특징

| 특징 | 설명 |
|---|---|
| 비용 거의 없음 | Catalyst는 무료 서비스이고, Appliance나 VM 없이 **파일 가져오기**로 시작합니다. 마지막 배포 모듈만 소액이 들며 정리 단계에서 모두 삭제합니다. |
| 서버 없는 시작 | 실제 온프렘 환경이 없어도 됩니다. 가상 고객 **Contoso Korea**의 CMDB 내보내기(서버 31행)를 제공합니다. |
| AI 중심 | **AI 기반 인벤토리 가져오기**와 **Catalyst 에이전트**를 직접 사용합니다. (둘 다 Preview) |
| 계획에서 실행까지 | Catalyst가 세운 계획을 GitHub Copilot **Modernize CLI**로 실제 코드 현대화에 연결합니다. |

## 진행 순서

| # | 모듈 | 하는 일 | 시간 |
|---|---|---|---|
| 0 | [준비]({{ '/lab0-setup/' | relative_url }}) | 구독 준비, 리소스 공급자 등록, Catalyst 프로젝트 만들기 | 15분 |
| 1 | [발견: AI 파일 가져오기]({{ '/lab1-discover/' | relative_url }}) | CMDB 내보내기를 올려 인벤토리 만들기 | 25분 |
| 2 | [애플리케이션과 보안 위험]({{ '/lab2-applications/' | relative_url }}) | 서버를 앱 단위로 묶기 | 20분 |
| 3 | [비즈니스 케이스]({{ '/lab3-business-case/' | relative_url }}) | 온프렘 대비 Azure 비용과 가치 | 25분 |
| 4 | [평가와 마이그레이션 전략]({{ '/lab4-assessment/' | relative_url }}) | 6R 전략과 Azure 대상 서비스 | 15분 |
| 5 | [Catalyst 에이전트]({{ '/lab5-agent/' | relative_url }}) | 에이전트의 강점과 한계, 답변 검증 | 20분 |
| 6 | [웨이브 계획과 랜딩 존]({{ '/lab6-plan/' | relative_url }}) | 웨이브 위험과 Platform Landing Zone 설계 | 20분 |
| 7 | [Java 앱 현대화와 배포]({{ '/lab7-java-modernize/' | relative_url }}) | Modernize CLI로 현대화하고 Container Apps에 배포 | 90분 |
| 8 | [정리]({{ '/lab8-cleanup/' | relative_url }}) | 리소스 삭제 | 5분 |

## 시나리오: Contoso Korea

| 앱 | 설명 | 특징 |
|---|---|---|
| ORD | 주문 포털 | Java 8 + Tomcat 9, MySQL. **7장에서 현대화** |
| HRM | 인사 시스템 | .NET Framework 4.8, IIS, SQL Server 2019 |
| ERP | 레거시 ERP | Windows Server 2012 R2, SQL Server 2012 (지원 종료) |
| RPT | 리포팅 / BI | SQL Server 2017 BI |
| SHR | 공용 인프라 | AD, 파일 서버, 모니터링, 백업 |

## 알아 둘 점

- **Preview 기능**이 많습니다. AI 결과(경고 개수, 에이전트 답변)는 실행할 때마다 조금씩 다를 수 있습니다.
- 기능과 화면은 바뀔 수 있으니 [Azure Catalyst 문서](https://learn.microsoft.com/azure/migrate/migrate-services-overview)를 함께 확인하세요.
- **Catalyst 에이전트**를 쓰려면 테넌트에서 **Azure Copilot**가 켜져 있어야 합니다.
