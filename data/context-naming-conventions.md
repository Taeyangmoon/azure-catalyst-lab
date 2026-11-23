# Contoso Korea CMDB export - context file

This ServiceNow CMDB export lists the servers of Contoso Korea.

## Columns
- `name` is the server hostname. Names that differ only by letter case are the same server. Use the lowercase name.
- `sys_class_name` tells the OS family (`cmdb_ci_win_server` = Windows, `cmdb_ci_linux_server` = Linux).
- `cpu_count` is the number of CPU cores.
- `ram` is in MB. If the value ends with `GB`, it is in GB.
- `disk_space` is the total disk size in GB.
- `u_installed_software` lists the software installed on the server. It contains database engines (SQL Server, MySQL) and web servers (IIS, Apache Tomcat).
- `u_app_code` is the business application the server belongs to:
  ORD = Order Portal, HRM = HR system, ERP = legacy ERP, RPT = reporting and BI, SHR = shared infrastructure.
- `u_environment`: P = Production, S = Staging, D = Development.
- `u_criticality`: 1 = mission critical, 2 = high, 3 = medium, 4 = low.

## Exclusions
- Exclude rows where `install_status` is 7. Status 7 means Retired. Status 1 means Installed.
