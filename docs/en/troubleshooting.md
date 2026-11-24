---
permalink: /en/troubleshooting/
title: Troubleshooting
parent: English
nav_order: 10
---

# Troubleshooting

These are problems we actually met while building this lab.

## Catalyst

| Symptom | Cause and action |
|---|---|
| The file analysis ends as **Failed** and keeps failing even after a retry | One project kept failing after the context file was **uploaded separately**, or after **Retry with new file**. **Create a new project** and upload the **ZIP once** from the start. |
| The context file is not applied | Upload the context file **together with the inventory file in the first upload**. In this lab the CSV and the context are bundled in one ZIP. |
| **All inventory** is empty after saving | Processing the imported inventory takes **10 to 15 minutes**. **Refresh** until the message disappears. |
| I want to edit after **Save to inventory** | You **cannot edit** a saved inventory. Import again in a new project. |
| Auto discovery of applications does not start | If the notification does not say **"initiated successfully"** after you select the button, it did not start. Select it again. Completion takes 10 to 15 minutes (up to 1 hour). |
| **Azure value uplift is $0** in the business case ("no applications in scope") | A business case **cannot change its scope after creation**, and **Recompute** does not fix it. Define the applications and then create a **new business case**. |
| Cannot use **Performance-based** sizing | File import has no performance history. Choose **As on-premises**. |
| The agent asks "which assessment" | The application list and strategy are looked up for a specific **assessment**. Tell it the assessment name too. |
| The agent panel does not open / **Catalyst** is not in the menu | Check whether **Azure Copilot** is enabled in your tenant and whether your administrator allows access. |
| **Web apps** is 0 | File import recognizes Tomcat/IIS only as **web server software** on servers. Web app assessment and Code Insights need appliance-based discovery. |
| The Platform Landing Zone region is EASTUS | **View settings** has no region field. Ask the **Catalyst agent** to create the PLZ with Korea Central (see module 6). |

## Java modernization and deployment

| Symptom | Cause and action |
|---|---|
| `az mysql flexible-server create` fails with `MissingSubscriptionRegistration` | The `Microsoft.DBforMySQL` provider is not registered in your subscription. `az provider register --namespace Microsoft.DBforMySQL --wait` |
| `brew install modernize` fails with an untrusted tap error | Use the official install script (`install.sh`) or run `brew trust microsoft/modernize`. |
| The Modernize plan contains an Azure deployment task | Read `tasks.json` and `plan.md` before executing, and edit the deployment task so it **only creates the Dockerfile**. |
| The first app response is slow | `--min-replicas 0` means it is starting again from 0. Wait a few seconds. |
| `/health` is OK but `/orders` returns 500 | Check the DB environment variables (`DB_HOST`, `DB_USER`, `DB_PASSWORD`) and the MySQL firewall (`0.0.0.0`) rule. Look at the logs with `az containerapp logs show`. |
| Uploaded files disappear | The upload path (`/tmp/uploads`) is the temporary disk of the container. Move it to Azure Blob Storage or Azure Files. |
