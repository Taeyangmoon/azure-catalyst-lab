---
permalink: /troubleshooting/
title: 문제 해결
nav_order: 11
---

# 문제 해결

이 랩을 만들면서 실제로 겪은 문제를 모았습니다.

## Catalyst

| 증상 | 원인과 조치 |
|---|---|
| 파일 분석이 **Failed**로 끝나고 재시도해도 계속 실패 | 컨텍스트 파일을 **따로 추가 업로드**한 뒤, 또는 **Retry with new file** 이후에 계속 실패한 프로젝트가 있었습니다. **새 프로젝트를 만들어** 처음부터 **ZIP 한 번**으로 올리세요. |
| 컨텍스트 파일이 반영되지 않음 | 컨텍스트 파일은 **첫 업로드에 인벤토리 파일과 함께** 올려야 합니다. 이 랩은 CSV와 컨텍스트를 ZIP 하나로 묶었습니다. |
| 저장했는데 **All inventory**가 비어 있음 | 가져온 인벤토리 처리에 **10~15분**이 걸립니다. 안내 문구가 사라질 때까지 **Refresh**하세요. |
| **Save to inventory** 후 수정하고 싶음 | 저장한 인벤토리는 **수정할 수 없습니다.** 새 프로젝트에서 다시 가져오세요. |
| 앱 자동 발견이 시작되지 않음 | 버튼을 눌러도 알림에 **"initiated successfully"** 가 뜨지 않으면 시작되지 않은 것입니다. 다시 누르세요. 완료까지 10~15분(최대 1시간). |
| 비즈니스 케이스의 **Azure value uplift가 $0** ("no applications in scope") | 비즈니스 케이스는 **만든 뒤 범위를 바꿀 수 없습니다.** **Recompute**로도 해결되지 않습니다. 앱을 정의한 뒤 **새 비즈니스 케이스**를 만드세요. |
| **Performance-based** sizing을 쓸 수 없음 | 파일 가져오기에는 성능 이력이 없습니다. **As on-premises**를 선택하세요. |
| 에이전트가 "평가 이름을 알려 달라"고 함 | 앱 목록과 전략은 특정 **평가(assessment)** 기준으로 조회합니다. 평가 이름을 함께 알려 주세요. |
| 에이전트 패널이 열리지 않음 / 메뉴에 **Catalyst**가 없음 | 테넌트의 **Azure Copilot** 활성화 여부와 관리자의 접근 설정을 확인하세요. |
| **Web apps**가 0건 | 파일 가져오기는 Tomcat/IIS를 **서버의 웹 서버 소프트웨어**로만 인식합니다. 웹 앱 평가와 Code Insights는 Appliance 기반 discovery가 필요합니다. |

## Java 현대화와 배포

| 증상 | 원인과 조치 |
|---|---|
| `az mysql flexible-server create`가 `MissingSubscriptionRegistration` 오류 | 구독에 `Microsoft.DBforMySQL` 공급자가 등록되지 않았습니다. `az provider register --namespace Microsoft.DBforMySQL --wait` |
| `brew install modernize`가 untrusted tap 오류 | 공식 설치 스크립트(`install.sh`)를 쓰거나 `brew trust microsoft/modernize`를 실행하세요. |
| Modernize 계획에 Azure 배포 작업이 들어 있음 | 실행 전에 `tasks.json`과 `plan.md`를 읽고, 배포 작업은 **Dockerfile만 만들도록** 고치세요. |
| 앱 첫 응답이 느림 | `--min-replicas 0`이라 0개에서 다시 켜지는 중입니다. 몇 초 기다리세요. |
| `/health`는 OK인데 `/orders`가 500 | DB 환경 변수(`DB_HOST`, `DB_USER`, `DB_PASSWORD`)와 MySQL 방화벽(`0.0.0.0`) 규칙을 확인하세요. `az containerapp logs show`로 로그를 보세요. |
| 업로드한 파일이 사라짐 | 업로드 경로(`/tmp/uploads`)는 컨테이너의 임시 디스크입니다. Azure Blob Storage나 Azure Files로 옮겨야 합니다. |
