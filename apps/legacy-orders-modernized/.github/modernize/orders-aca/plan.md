# Modernization Plan: Upgrade to Java 17 / Jakarta EE 10 and Run on Azure Container Apps

**Project**: Contoso Order Portal (orders)

---

## Technical Framework

- **Language**: Java 8
- **Framework**: Java EE (javax.servlet 3.1), no application framework (plain servlets/JSP)
- **Build Tool**: Maven (war packaging)
- **Database**: MySQL (connected via mysql-connector-java 5.1.49, legacy `com.mysql.jdbc.Driver`)
- **Key Dependencies**: log4j 1.2.17 (file-based logging), commons-fileupload 1.3.3 (local disk file uploads)

---

## Overview

This migration modernizes the Contoso Order Portal from a legacy Java EE 8 application into a
cloud-ready, containerized service running on Azure. The application currently hardcodes database
credentials and the upload directory path in a bundled properties file, writes logs to a local
rolling log file, and has no container packaging. The new architecture will:

- Upgrade the runtime to Java 17 and migrate the servlet/web layer from Java EE (javax.*) to
  Jakarta EE 10 running on Tomcat 10.1, removing the end-of-support JDK and outdated APIs.
- Externalize database connection settings and the upload directory path to environment variables
  so the application can connect to Azure Database for MySQL and run in any environment without
  code changes or hardcoded secrets.
- Redirect all application logging to stdout so logs are collected by the container platform
  instead of being written to a local file that disappears when a container is replaced.
- Package the application in a Dockerfile. (Deployment to Azure Container Apps is done manually in the lab.)

The migration proceeds sequentially: runtime upgrade first, then configuration and logging
changes, followed by security remediation, and finally containerization and deployment.

---

## Migration Impact Summary

| Application | Original Service | New Azure Service | Authentication | Comments |
|-------------|------------------|--------------------|-----------------|----------|
| orders | Java 8 / Java EE app on local Tomcat | Azure Container Apps (Java 17 / Jakarta EE 10 / Tomcat 10.1) | Managed Identity (ACA system identity) | Containerized via new Dockerfile |
| orders | On-prem MySQL w/ hardcoded creds in db.properties | Azure Database for MySQL | Environment variables (as requested) | DB host/user/password read from env vars, not Key Vault/MI |
| orders | Local rolling file log (log4j, /var/log/...) | stdout (collected by Azure Container Apps) | N/A | No code change to log levels/format, only output target |
| orders | Local disk upload folder path hardcoded in db.properties | Local disk path, now configurable via env var | N/A | Upload directory resolved from environment variable at runtime |

---

## Open Questions & Questionnaire

- [ ] Infrastructure provisioning (Bicep/Terraform for Azure Database for MySQL and Azure
      Container Apps) was not explicitly requested, so no infrastructure task was created. Let us
      know if you want IaC generated and resources provisioned as part of this plan.
- [ ] Integration testing was not explicitly requested, so no setupBaseline/integrationTest task
      was created. Let us know if you'd like post-migration integration tests added.
