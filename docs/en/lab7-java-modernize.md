---
permalink: /en/lab7-java-modernize/
title: "7. Java modernization and deployment"
parent: English
nav_order: 8
---

# 7. From plan to execution: Java app to Container Apps

⏱️ About 90 min (including 20 min waiting for the AI run and 10 min waiting for Azure provisioning) · 💰 **About $0.3 or less** (estimated at list price, deleted after the lab)

This is the last step of the plan Catalyst made. You modernize a **legacy Java order portal**, which stands for the `Application_ord` you met in module 2 (Tomcat servers + MySQL), with the GitHub Copilot **Modernize CLI**, and deploy it to **Azure Container Apps**.

{: .warning }
**Relation to Catalyst**: Catalyst's web app **Code Insights** (which links code change analysis to Catalyst assessments) needs ASP.NET/Java web apps found by **appliance-based discovery**. With file import in this lab **Web apps is 0**, so you cannot use this feature. That is why this module runs **the same Modernize CLI** directly, outside Catalyst. In an appliance environment, the results are uploaded to Catalyst with `modernize assess --source <config.json>` as described in the [Code Insights documentation](https://learn.microsoft.com/azure/migrate/add-copilot-code-insights).

## What you learn
- Reading an assessment report to see **why a legacy Java app is hard to put in the cloud**
- The **assess → plan → execute** flow and **where a human edits** the plan (`plan.md`, `tasks.json`)
- **Reviewing the result as a code diff** and verifying it by deploying to Azure

## Prerequisites

| Item | Description |
|---|---|
| GitHub account | A **GitHub Copilot** plan (Free is fine) |
| GitHub CLI | `gh` 2.45 or later, `gh auth login` done |
| Modernize CLI | Install below |
| Azure CLI | `az login` done, subscription selected |
| git | Needed to keep the plan and execution results as commits |

Install the Modernize CLI:

```bash
# macOS / Linux
curl -fsSL https://raw.githubusercontent.com/microsoft/modernize-cli/main/scripts/install.sh | bash
modernize --version
```

```powershell
# Windows PowerShell
iex (irm 'https://raw.githubusercontent.com/microsoft/modernize-cli/main/scripts/install.ps1')
```

{: .note }
You **do not need to install** Docker, a JDK or Maven yourself. The image is built in Azure (`az acr build`), and the Modernize CLI prepares the JDK and Maven it needs.

## 1. Prepare the legacy app

**Copy `apps/legacy-orders` from the repository into its own git repository.** This is so the execution results are kept as commits.

```bash
git clone https://github.com/Taeyangmoon/azure-catalyst-lab.git
cp -R azure-catalyst-lab/apps/legacy-orders ~/orders
cd ~/orders
git init -b main && git add -A && git commit -m "baseline"
```

The app intentionally carries "habits from the on-premises era". The UI, log messages and comments are in English, and the Korean and English labs use the same app.

| Where | Legacy habit |
|---|---|
| `pom.xml` | **Java 8**, `javax.servlet` 3.1, an old MySQL driver (5.1), log4j 1.2 |
| `db.properties` | **DB connection info and password hardcoded in the source** |
| `log4j.properties` | Logs are written to a **local file on the server** (`/var/log/...`) |
| `UploadServlet` | Uploaded files are stored on the **local disk of the server** |
| (none) | No Dockerfile |

## 2. Assess: what is the problem

```bash
modernize assess --source . --no-tty --format markdown --output-path ./assess
```

The first run downloads the assessment tool (about 140 MB), so it takes a little longer. The result is saved to `assess/reports-*/repos/<folder name>/report.md`. If the folder is named `orders` as in this document, it is `repos/orders/report.md`.

| Issue | Criticality | Points | Meaning |
|---|---|---:|---|
| Java Version Has Reached the End of Support | **Mandatory** | 8 | JDK 8 is out of support |
| Avoid File System Logging in Configuration | **Mandatory** | 1 | Logs disappear when the container is replaced |
| No Dockerfile found | **Mandatory** | 3 | No container packaging |
| File system - Java IO | **Mandatory** | 3 | The upload code depends on the local disk |
| MySQL database found | Potential | 5 | Candidate to move to Azure Database for MySQL |
| Jakarta EE Version is not the latest stable | Optional | 8 | `javax.*` → `jakarta.*` |

Summary: Mandatory 4, Potential 1. It is a single app but is classified as "Wave 2 - Core Cloud".

## 3. Create the plan (where a human edits)

```bash
modernize plan create "Upgrade to Java 17 and Jakarta EE 10 (Tomcat 10.1), read database settings and upload path from environment variables instead of hardcoded files, write logs to stdout, add a Dockerfile, and prepare the app to run on Azure Container Apps with Azure Database for MySQL" \
  --plan-name orders-aca --language java \
  --assess-file-path ./assess/reports-*/repos/orders/report.md \
  --source . --no-tty
```

In this lab **5 tasks** were created.

| # | Task | Assessment issue it addresses |
|---|---|---|
| 001 | Upgrade to Java 17 + Jakarta EE 10 (Tomcat 10.1) | JDK end of support, Jakarta EE |
| 002 | DB settings and upload path from **environment variables** | Hardcoded password, file system IO, MySQL |
| 003 | Logs to **stdout** | File logging |
| 004 | Dependency **CVE check and fix** | (a security task the plan added by itself) |
| 005 | **Dockerfile** and deployment to Azure Container Apps | No Dockerfile |

Open `.github/modernize/orders-aca/plan.md` and `.metadata/tasks.json`. **A human can edit the plan before executing.** In this lab we edited task 005. It was originally made to go all the way to **deploying to Azure**, but for cost and control we changed it to **only create the Dockerfile**, and we deploy by hand in section 6.

Example edit of item 5 in `tasks.json`:

```json
{
  "type": "transform",
  "id": "005-transform-dockerfile",
  "description": "Containerize the application with a Dockerfile (build only, do NOT deploy to Azure).",
  "requirements": "Create a multi-stage Dockerfile that builds the WAR with Maven and Java 17 and runs it on Tomcat 10.1 (Java 17 JRE) as ROOT.war. Expose port 8080. Add a .dockerignore. Do not create or modify any Azure resources and do not run az, azd or Bicep commands."
}
```

Reflect the same intent in `plan.md`: "only the Dockerfile, deployment is manual".

{: .note }
The key is **not to execute an AI-made plan as it is**. The plan in this lab contained **unintended Azure resource creation (a deployment task)**. Read the plan and control its scope before you execute.

## 4. Execute

```bash
modernize plan execute --plan-name orders-aca --language java --source . --no-tty
```

It took about **20 minutes** in this lab. **One git commit per task** is created.

```bash
git log --oneline
```

```text
c585d4d Update tasks.json: mark task 005 as success
afb776f Task 005: Containerize application with multi-stage Dockerfile for Tomcat 10.1/Java 17
15da9f4 Task 004: Remediate CVEs in dependencies
dd9b6cb Task 003: redirect application logging from file to console
f978414 task 002: externalize DB and upload-dir config to environment variables
87570b0 Task 001: Upgrade to Java 17 and Jakarta EE 10
dceab3f baseline
```

## 5. Review the result

**A human must always read** the code the AI changed. The key points are:

| Change | Content |
|---|---|
| `pom.xml` | Java **17**, `jakarta.servlet-api` **6.0.0**, MySQL **Connector/J 9.3.0**, `commons-fileupload2`, log4j 1.x → **reload4j** (drop-in replacement) |
| `DbConfig.java` | `db.properties` deleted. Configured with environment variables such as `DB_HOST`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`, `DB_SSL_MODE`, `UPLOAD_DIR`. No password in the code |
| `log4j.properties` | File appender → **ConsoleAppender (System.out)** |
| `*Servlet.java`, `web.xml` | `javax.servlet.*` → `jakarta.servlet.*` |
| `Dockerfile` | Build the WAR with Maven → deploy to `tomcat:10.1-jre17` as `ROOT.war`, non-root user |

A detailed description of each task is kept in `.github/modernize/orders-aca/00x-*/modernization-summary.md`. The reference answer is in [`apps/legacy-orders-modernized`](https://github.com/Taeyangmoon/azure-catalyst-lab/tree/main/apps/legacy-orders-modernized) in the repository. AI results differ a little each time, so **it is normal if yours is not identical**.

{: .warning }
**Remaining limitation**: uploaded files are still stored in `/tmp/uploads` (temporary disk) of the container. They disappear when the container is replaced. In production you should move them to **Azure Blob Storage** or **Azure Files**. Of the assessment issues, "File system - Java IO" is **mitigated, not completely solved**, by using an environment variable.

## 6. Deploy to Azure

Resources to create: Azure Database for MySQL (Burstable B1ms), Azure Container Registry (Basic), a Container Apps environment and the app.

```bash
cd azure-catalyst-lab
./scripts/deploy-orders.sh apps/legacy-orders-modernized      # use ~/orders to deploy your own result
```

What the script does:

1. Register resource providers (such as `Microsoft.DBforMySQL`. In a subscription that never used it, it **fails if you do not register it**.)
2. Resource group `rg-orders-lab`
3. Create the **MySQL flexible server** (about 5 to 8 min)
4. **Create ACR and build the image with `az acr build`** (no local Docker needed, about 1 min)
5. Create the Container Apps environment and the `orders-web` app. The DB password is passed as a **secret** and referenced with `secretref:`

The key part:

```bash
az containerapp create -g $RG -n orders-web --environment orders-env \
  --image $ACR.azurecr.io/orders:v1 \
  --registry-server $ACR.azurecr.io --registry-identity system --system-assigned \
  --ingress external --target-port 8080 --min-replicas 0 --max-replicas 2 \
  --secrets db-password="$DB_PASSWORD" \
  --env-vars DB_HOST=$MYSQL.mysql.database.azure.com DB_NAME=orders DB_USER=ordersadmin \
             DB_PASSWORD=secretref:db-password UPLOAD_DIR=/tmp/uploads DB_SSL_MODE=REQUIRED
```

Registry authentication uses the app's **system-assigned managed identity** (`--registry-identity system`) instead of a password.

## 7. Verify

Open `rg-orders-lab` in the **Azure Portal** and you see the following resources.

![Resource group]({{ '/images/shots/g1-rg-orders-lab.png' | relative_url }})

Open the **Application Url** of the `orders-web` Container App.

![Container App overview]({{ '/images/shots/g2-containerapp-overview.png' | relative_url }})

Enter an order and select **Order**. The data is stored in **Azure Database for MySQL**.

![App running]({{ '/images/shots/g3-app-running.png' | relative_url }})

Under **Monitoring → Log stream**, look at the **Application** log. You can see that the log you changed in task 003 is **collected through stdout** (a line such as `Order received: Alice Kim / Laptop x 2`).

![Log stream]({{ '/images/shots/g4-log-stream.png' | relative_url }})

You can also check with the CLI.

```bash
curl -s https://<app address>/health            # OK
az containerapp logs show -g rg-orders-lab -n orders-web --tail 20 --format text
```

{: .note }
The container app uses `--min-replicas 0`, so after a while without requests it **scales down to 0** and starts again on the first request. The first response can be a few seconds slow.

## Cost (estimated at list price)

| Resource | Approximately |
|---|---|
| MySQL Flexible Server B1ms | About $0.02 per hour |
| ACR Basic | About $0.17 per day |
| Container Apps | Scales to 0 without requests, small usage within the free grant |
| Log Analytics | Small ingestion |

The lab ends within 3 hours, so the total is expected to be **around $0.3**, but it depends on the prices of your subscription, so **check Cost Management**. When the lab is over, **be sure to delete** everything in [cleanup]({{ '/en/lab8-cleanup/' | relative_url }}).

## Questions to think about
1. Of the **4 Mandatory** assessment issues, which ones did this modernization **completely solve** and which only **mitigated**?
2. **004 (CVE check)**, which the AI added to the plan, was not in the assessment report. Who should decide whether to accept such a task?
3. If the plan had contained a "deploy to Azure" task, what risks should you have checked before executing?

➡️ Next: [8. Cleanup]({{ '/en/lab8-cleanup/' | relative_url }})
