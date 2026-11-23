# Contoso Korea – CMDB 해석 가이드 (컨텍스트 파일)

AI 인벤토리 가져오기가 CMDB 값을 올바르게 해석하도록 돕는 **조직 컨텍스트**입니다.

## 서버 이름 규칙
`krc-<앱코드>-<역할>-<번호>`

| 역할 코드 | 의미 |
|---|---|
| web | 웹/프런트엔드 계층 |
| app | 애플리케이션 계층 |
| db | 데이터베이스 계층 |
| etl | 데이터 통합(ETL) |
| bat | 배치 서버 |
| dc | 도메인 컨트롤러 |
| file / mon / bkp / jmp | 파일 / 모니터링 / 백업 / 점프 호스트 |

번호가 `s1`이면 스테이징, `dev`이면 개발 서버입니다.

## 앱 코드
| 코드 | 애플리케이션 | 비고 |
|---|---|---|
| ORD | 주문 포털 (Order Portal) | Java(Tomcat) 기반 |
| HRM | 인사 시스템 | .NET Framework + SQL Server |
| ERP | 레거시 ERP | 클라이언트-서버, 구형 OS |
| RPT | 리포팅 / BI | SQL Server BI |
| SHR | 공용 인프라 | AD, 파일, 모니터링, 백업 |

## 필드 해석
- `u_environment`: P = 운영(Production), S = 스테이징, D = 개발
- `install_status`: 1 = 설치됨(사용 중), 7 = 폐기(Retired) → 폐기 CI는 마이그레이션 대상에서 제외
- `u_criticality`: 1 = 미션 크리티컬, 2 = 높음, 3 = 보통, 4 = 낮음
- `ram`: 기본 단위는 MB입니다. 값에 `GB`가 붙어 있으면 GB입니다.
- `disk_space`: GB 단위
- 대소문자만 다른 서버 이름은 같은 서버입니다(이름은 소문자 기준).
