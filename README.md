# Azure Catalyst 핸즈온 랩

ServiceNow CMDB 내보내기에서 시작해 Azure Catalyst(구 Azure Migrate)로 현대화 계획을 세우고, 레거시 Java 앱을 Azure Container Apps로 옮겨 보는 실습입니다. (한국어)

📖 **랩 문서(GitHub Pages)**: https://taeyangmoon.github.io/azure-catalyst-lab/

> 교육용 샘플입니다. Azure Catalyst의 AI 파일 가져오기와 Catalyst 에이전트는 **Preview**이며 화면과 동작이 바뀔 수 있습니다. 모든 화면은 실제 Azure Portal에서 캡처했습니다.

## 모듈

| # | 모듈 | 시간 |
|---|---|---|
| 0 | 준비: 구독, 리소스 공급자, 프로젝트 | 15분 |
| 1 | 발견: AI 파일 가져오기 (ServiceNow CMDB + 컨텍스트 파일) | 25분 |
| 2 | 애플리케이션 자동 발견과 Security Insights | 20분 |
| 3 | 비즈니스 케이스 (Azure Value Uplift) | 25분 |
| 4 | 평가와 마이그레이션 전략(6R) | 15분 |
| 5 | Catalyst 에이전트의 강점과 한계 | 20분 |
| 6 | 웨이브 계획과 Platform Landing Zone | 20분 |
| 7 | Java 앱 현대화(Modernize CLI)와 Container Apps 배포 | 90분 |
| 8 | 정리 | 5분 |

## 비용

Catalyst는 무료이고 Appliance나 VM 없이 파일 가져오기로 시작합니다. 7장의 배포(MySQL B1ms, ACR Basic, Container Apps)만 소액이 들며 정리 단계에서 삭제합니다. (정가 기준 추정 총 $0.3 안팎, Cost Management에서 확인하세요.)

## 저장소 구성

| 경로 | 내용 |
|---|---|
| `docs/` | 랩 문서(GitHub Pages) |
| `data/` | Contoso Korea CMDB 내보내기와 컨텍스트 파일, 업로드용 ZIP |
| `scripts/` | CMDB 데이터 생성, Azure 배포, 정리 스크립트 |
| `apps/legacy-orders/` | 현대화할 레거시 Java(Tomcat) 앱 |
| `apps/legacy-orders-modernized/` | Modernize CLI로 현대화한 결과 (비교용 참고 정답) |

## 알려진 한계

- Catalyst의 웹앱 평가와 Code Insights는 Appliance 기반 discovery가 필요해 이 랩(파일 가져오기)에서는 쓸 수 없습니다.
- 성능 기반 sizing은 성능 이력이 없어서 `As on-premises`를 사용합니다.
- AI 결과(경고 개수, 에이전트 답변)는 실행할 때마다 조금씩 다를 수 있습니다.
