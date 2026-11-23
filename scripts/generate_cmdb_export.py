#!/usr/bin/env python3
"""가상 고객 'Contoso Korea'의 ServiceNow CMDB 서버 내보내기(CSV)를 생성합니다.

AI 인벤토리 가져오기가 다음과 같은 '현실적인 지저분함'을 어떻게 다루는지 보여 주려고
일부러 폐기(Retired) CI, 대소문자만 다른 중복, 단위가 섞인 메모리 값, 빈 값을 넣었습니다.
외부 라이브러리 없이 표준 라이브러리만 사용합니다.
"""
import csv
import pathlib

OUT = pathlib.Path(__file__).resolve().parent.parent / "data"

# (name, class, os, os_version, cpu, ram, disk_gb, ip, env, app, status, criticality, group, location, software)
LNX, WIN = "cmdb_ci_linux_server", "cmdb_ci_win_server"
R = "RHEL"
rows = [
    # --- ORD: 주문 포털 (Java / Tomcat) ---
    ("krc-ord-web-01", LNX, "Red Hat Enterprise Linux", "8.8", 4, 8192, 100, "10.10.1.11", "P", "ORD", "1", "1", "Order-Ops", "Seoul-DC1", "nginx 1.22"),
    ("krc-ord-web-02", LNX, "Red Hat Enterprise Linux", "8.8", 4, 8192, 100, "10.10.1.12", "P", "ORD", "1", "1", "Order-Ops", "Seoul-DC1", "nginx 1.22"),
    ("krc-ord-app-01", LNX, "Red Hat Enterprise Linux", "8.8", 8, 16384, 200, "10.10.1.21", "P", "ORD", "1", "1", "Order-Ops", "Seoul-DC1", "Apache Tomcat 9.0.65; OpenJDK 1.8.0_352"),
    ("krc-ord-app-02", LNX, "Red Hat Enterprise Linux", "8.8", 8, 16384, 200, "10.10.1.22", "P", "ORD", "1", "1", "Order-Ops", "Seoul-DC1", "Apache Tomcat 9.0.65; OpenJDK 1.8.0_352"),
    ("krc-ord-db-01", LNX, "Red Hat Enterprise Linux", "7.9", 8, "32 GB", 1000, "10.10.1.31", "P", "ORD", "1", "1", "DBA", "Seoul-DC1", "MySQL 5.7.40"),
    ("krc-ord-app-s1", LNX, "Red Hat Enterprise Linux", "8.8", 4, 8192, 100, "10.10.2.21", "S", "ORD", "1", "3", "Order-Ops", "Seoul-DC1", "Apache Tomcat 9.0.65; OpenJDK 1.8.0_352"),
    ("krc-ord-db-s1", LNX, "Red Hat Enterprise Linux", "7.9", 4, 16384, 300, "10.10.2.31", "S", "ORD", "1", "3", "DBA", "Seoul-DC1", "MySQL 5.7.40"),
    ("krc-ord-dev-01", LNX, "Ubuntu", "20.04", 4, 8192, 150, "10.10.3.11", "D", "ORD", "1", "4", "Order-Ops", "Seoul-DC1", "Apache Tomcat 9.0.65; MySQL 5.7.40"),
    # --- HRM: 인사 시스템 (.NET Framework / IIS / SQL Server) ---
    ("krc-hrm-web-01", WIN, "Microsoft Windows Server 2019 Standard", "10.0.17763", 4, 8192, 120, "10.20.1.11", "P", "HRM", "1", "2", "HR-IT", "Seoul-DC1", "IIS 10; .NET Framework 4.8"),
    ("krc-hrm-web-02", WIN, "Microsoft Windows Server 2019 Standard", "10.0.17763", 4, 8192, 120, "10.20.1.12", "P", "HRM", "1", "2", "HR-IT", "Seoul-DC1", "IIS 10; .NET Framework 4.8"),
    ("krc-hrm-app-01", WIN, "Microsoft Windows Server 2019 Standard", "10.0.17763", 4, 16384, 150, "10.20.1.21", "P", "HRM", "1", "2", "HR-IT", "Seoul-DC1", ".NET Framework 4.8; Windows Service"),
    ("krc-hrm-db-01", WIN, "Microsoft Windows Server 2019 Standard", "10.0.17763", 8, 32768, 800, "10.20.1.31", "P", "HRM", "1", "2", "DBA", "Seoul-DC1", "SQL Server 2019 Standard"),
    ("krc-hrm-dev-01", WIN, "Microsoft Windows Server 2019 Standard", "10.0.17763", 4, 8192, 200, "10.20.3.11", "D", "HRM", "1", "4", "HR-IT", "Seoul-DC1", "IIS 10; SQL Server 2019 Express"),
    # --- ERP: 레거시 ERP (EOS OS / DB) ---
    ("krc-erp-app-01", WIN, "Microsoft Windows Server 2012 R2 Standard", "6.3.9600", 8, 16384, 250, "10.30.1.21", "P", "ERP", "1", "1", "ERP-Ops", "Busan-DC2", "Custom ERP client-server; .NET Framework 4.5"),
    ("krc-erp-app-02", WIN, "Microsoft Windows Server 2012 R2 Standard", "6.3.9600", 8, 16384, 250, "10.30.1.22", "P", "ERP", "1", "1", "ERP-Ops", "Busan-DC2", "Custom ERP client-server; .NET Framework 4.5"),
    ("krc-erp-db-01", WIN, "Microsoft Windows Server 2012 R2 Datacenter", "6.3.9600", 16, 65536, 4000, "10.30.1.31", "P", "ERP", "1", "1", "DBA", "Busan-DC2", "SQL Server 2012 Enterprise"),
    ("krc-erp-bat-01", WIN, "Microsoft Windows Server 2008 R2 Standard", "6.1.7601", 4, 8192, 300, "10.30.1.41", "P", "ERP", "1", "2", "ERP-Ops", "Busan-DC2", "Batch scheduler (VB6)"),
    ("krc-erp-app-s1", WIN, "Microsoft Windows Server 2012 R2 Standard", "6.3.9600", 4, 8192, 200, "10.30.2.21", "S", "ERP", "1", "3", "ERP-Ops", "Busan-DC2", "Custom ERP client-server"),
    # --- RPT: 리포팅 / BI ---
    ("krc-rpt-etl-01", WIN, "Microsoft Windows Server 2016 Standard", "10.0.14393", 8, 32768, 500, "10.40.1.11", "P", "RPT", "1", "3", "BI-Team", "Seoul-DC1", "SQL Server 2017 Integration Services"),
    ("krc-rpt-db-01", WIN, "Microsoft Windows Server 2016 Standard", "10.0.14393", 16, 131072, 6000, "10.40.1.31", "P", "RPT", "1", "3", "DBA", "Seoul-DC1", "SQL Server 2017 Enterprise"),
    ("krc-rpt-web-01", WIN, "Microsoft Windows Server 2016 Standard", "10.0.14393", 4, 8192, 120, "10.40.1.12", "P", "RPT", "1", "3", "BI-Team", "Seoul-DC1", "IIS 10; SSRS 2017; Power BI Report Server"),
    # --- SHR: 공용 인프라 ---
    ("krc-shr-dc-01", WIN, "Microsoft Windows Server 2016 Standard", "10.0.14393", 4, 8192, 120, "10.1.0.11", "P", "SHR", "1", "1", "Infra", "Seoul-DC1", "Active Directory Domain Services; DNS"),
    ("krc-shr-dc-02", WIN, "Microsoft Windows Server 2016 Standard", "10.0.14393", 4, 8192, 120, "10.1.0.12", "P", "SHR", "1", "1", "Infra", "Busan-DC2", "Active Directory Domain Services; DNS"),
    ("krc-shr-file-01", WIN, "Microsoft Windows Server 2016 Standard", "10.0.14393", 4, 16384, 8000, "10.1.0.21", "P", "SHR", "1", "2", "Infra", "Seoul-DC1", "File Server; DFS"),
    ("krc-shr-mon-01", LNX, "Ubuntu", "20.04", 4, 8192, 500, "10.1.0.31", "P", "SHR", "1", "3", "Infra", "Seoul-DC1", "Zabbix 6.0"),
    ("krc-shr-bkp-01", WIN, "Microsoft Windows Server 2019 Standard", "10.0.17763", 8, 16384, 20000, "10.1.0.41", "P", "SHR", "1", "3", "Infra", "Seoul-DC1", "Veeam Backup & Replication"),
    ("krc-shr-jmp-01", WIN, "Microsoft Windows Server 2019 Standard", "10.0.17763", 2, 4096, 80, "10.1.0.51", "P", "SHR", "1", "4", "Infra", "Seoul-DC1", "Jump host"),
    # --- 지저분한 데이터 (의도적) ---
    ("KRC-ORD-WEB-01", LNX, "Red Hat Enterprise Linux", "8.8", 4, 8192, 100, "10.10.1.11", "P", "ORD", "1", "1", "Order-Ops", "Seoul-DC1", "nginx 1.22"),   # 대소문자만 다른 중복
    ("krc-old-app-07", WIN, "Microsoft Windows Server 2008 R2 Standard", "6.1.7601", 2, 4096, 100, "10.30.9.7", "P", "ERP", "7", "4", "ERP-Ops", "Busan-DC2", ""),  # 폐기(Retired)
    ("krc-old-db-03", WIN, "Microsoft Windows Server 2003", "5.2", 2, 2048, 80, "10.30.9.3", "P", "ERP", "7", "4", "DBA", "Busan-DC2", "SQL Server 2005"),  # 폐기(Retired)
    ("krc-tst-web-99", LNX, "CentOS", "7.9", "", "", "", "10.99.0.99", "D", "", "1", "", "", "Seoul-DC1", ""),  # 값이 비어 있음
]

header = [
    "sys_id", "name", "sys_class_name", "os", "os_version", "cpu_count", "ram", "disk_space",
    "ip_address", "u_environment", "u_app_code", "install_status", "u_criticality",
    "support_group", "location", "u_installed_software",
]
OUT.mkdir(exist_ok=True)
with open(OUT / "servicenow_cmdb_export.csv", "w", newline="", encoding="utf-8") as f:
    w = csv.writer(f)
    w.writerow(header)
    for i, r in enumerate(rows, start=1):
        sys_id = f"{i:04x}a1b2c3d4e5f60718293a4b5c6d7e"[:32]
        name, cls, os_, osv, cpu, ram, disk, ip, env, app, status, crit, grp, loc, sw = r
        w.writerow([sys_id, name, cls, os_, osv, cpu, ram, disk, ip, env, app, status, crit, grp, loc, sw])
print(f"{len(rows)} rows -> {OUT / 'servicenow_cmdb_export.csv'}")
