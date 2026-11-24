---
permalink: /en/lab0-setup/
title: "0. Setup"
parent: English
nav_order: 1
---

# 0. Setup: subscription, resource providers, Catalyst project

⏱️ About 15 min · 💰 Catalyst itself is **free**. Creating a project creates one **storage account** that holds metadata. You delete it in the last module.

## What you do in this module
1. Get the sample data
2. Register Azure resource providers
3. Create a resource group
4. Create the **Azure Catalyst project**

## Prerequisites

| Item | Description |
|---|---|
| Azure subscription | **Owner or Contributor** permission to create a resource group and a project |
| Azure Copilot | **Azure Copilot must be enabled** in your tenant to use the Catalyst agent. Your administrator controls access. |
| Browser | A recent browser that can sign in to the Azure Portal |
| (Optional) Azure CLI | Needed if you use the `Azure CLI` commands below |

{: .note }
This lab **does not create any servers.** You do not need a real on-premises environment or an appliance.

## 1. Get the sample data

Download the following files from the `data/` folder of the repository.

| File | Description |
|---|---|
| [`contoso-korea-cmdb.zip`](https://github.com/Taeyangmoon/azure-catalyst-lab/raw/main/data/contoso-korea-cmdb.zip) | The ZIP you upload **as is** in module 1 (CMDB export + context file) |
| `servicenow_cmdb_export.csv` | The ServiceNow CMDB export inside the ZIP (31 rows) |
| `context-naming-conventions.md` | The context file inside the ZIP (column meanings, units, exclusion rules) |

Do not unzip the ZIP. Use it as it is. If you are curious, open the CSV. It intentionally contains **2 retired servers, 1 duplicate that differs only by letter case, and 1 server with empty values**. How Catalyst handles this "messy data" is the point of module 1.

## 2. Register resource providers

Catalyst uses `Microsoft.Migrate` and `Microsoft.OffAzure`. Skip this if they are already registered.

{% raw %}
<details open markdown="1">
<summary><b>Azure CLI</b></summary>

```bash
az login
az account set --subscription "<subscription name or ID>"

az provider register --namespace Microsoft.Migrate
az provider register --namespace Microsoft.OffAzure
az provider show --namespace Microsoft.Migrate --query registrationState -o tsv
```

You are done when `Registered` is returned.
</details>

<details markdown="1">
<summary><b>Azure Portal</b></summary>

Go to **Subscriptions** → your subscription → **Resource providers**, select `Microsoft.Migrate` and `Microsoft.OffAzure`, and select **Register**.
</details>
{% endraw %}

## 3. Create a resource group

{% raw %}
<details open markdown="1">
<summary><b>Azure CLI</b></summary>

```bash
az group create --name rg-catalyst-lab --location koreacentral \
  --tags purpose=catalyst-lab
```
</details>

<details markdown="1">
<summary><b>Azure Portal</b></summary>

**Resource groups** → **Create** → name `rg-catalyst-lab`, region **Korea Central** → **Review + create**.
</details>
{% endraw %}

## 4. Create the Catalyst project

1. Type `Azure Catalyst` in the Azure Portal search box and open the service.
2. On the left, under **All projects**, select **Create project**.
3. Enter the values below and select **Create**.

| Field | Value |
|---|---|
| Subscription | The subscription you use for the lab |
| Resource group | `rg-catalyst-lab` |
| Project | `catalyst-lab` |
| Geography | **Korea** |
| Artifact store | Check **Use same subscription and resource group as project** (the storage account name is generated automatically) |

![Create project]({{ '/images/shots/a1-create-project-form.png' | relative_url }})

{: .warning }
**Geography** is where the project metadata is stored. You cannot change it after creation.

Project creation takes 1 to 2 minutes. When it is done, `catalyst-lab` appears under **All projects**.

## Verify

{% raw %}
<details open markdown="1">
<summary><b>Azure CLI</b></summary>

```bash
az resource list -g rg-catalyst-lab --query "[].{name:name,type:type}" -o table
```

It is fine when you see one `Microsoft.Migrate/migrateprojects` and one `Microsoft.Storage/storageAccounts`.
</details>
{% endraw %}

{: .note }
**Cost**: Catalyst is free and the storage account only holds a few small files, so the cost is almost zero. Still, delete the resource group in the [cleanup]({{ '/en/lab8-cleanup/' | relative_url }}) step when the lab is over.

➡️ Next: [1. Discover: AI file import]({{ '/en/lab1-discover/' | relative_url }})
