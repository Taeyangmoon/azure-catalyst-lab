---
title: 홈
layout: home
nav_order: 1
---

# Azure Catalyst 핸즈온 랩

**ServiceNow CMDB 내보내기 파일 하나**에서 시작해, Azure Catalyst(구 Azure Migrate)로 **발견 → 평가 → 비즈니스 케이스 → Catalyst 에이전트 → 계획**까지 해 보고, 마지막에는 레거시 **Java(Tomcat) 앱을 Azure Container Apps로 현대화**하는 실습입니다.

{: .note }
이 랩은 **Azure Portal**을 중심으로 진행하고, 가능한 단계에는 **Azure CLI** 대안을 함께 적었습니다. 모든 화면은 실제 Azure Portal에서 캡처했습니다.

## 이 랩의 특징

| 특징 | 설명 |
|---|---|
| 비용 거의 없음 | Catalyst는 무료 서비스이고, Appliance나 VM 없이 **파일 가져오기**로 시작합니다. 마지막 배포 모듈만 소액이 들며 정리 단계에서 모두 삭제합니다. |
| 서버 없는 시작 | 실제 온프렘 환경이 없어도 됩니다. 가상 고객 **Contoso Korea**의 CMDB 내보내기(서버 31대)를 제공합니다. |
| AI 중심 | AI 기반 인벤토리 가져오기와 **Catalyst 에이전트**를 직접 사용합니다. |
| 계획에서 실행까지 | Catalyst가 만든 계획을 GitHub Copilot Modernize CLI로 실제 코드 현대화에 연결합니다. |

## 진행 순서

| # | 모듈 | 하는 일 | 시간 |
|---|---|---|---|
| 0 | [준비]({{ site.baseurl }}/lab0-setup/) | 구독 준비, 리소스 공급자 등록, Catalyst 프로젝트 만들기 | 15분 |
| 1 | [발견: AI 파일 가져오기]({{ site.baseurl }}/lab1-discover/) | CMDB 내보내기를 올려 인벤토리 만들기 | 20분 |

{: .note }
나머지 모듈은 작성 중입니다.

## 시나리오: Contoso Korea

| 앱 | 설명 | 특징 |
|---|---|---|
| ORD | 주문 포털 | Java 8 + Tomcat 9, MySQL. **마지막 모듈에서 현대화** |
| HRM | 인사 시스템 | .NET Framework 4.8, IIS, SQL Server 2019 |
| ERP | 레거시 ERP | Windows Server 2012 R2, SQL Server 2012 (지원 종료) |
| RPT | 리포팅 / BI | SQL Server 2017 BI |
| SHR | 공용 인프라 | AD, 파일 서버, 모니터링, 백업 |
