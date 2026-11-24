---
title: English
nav_order: 3
has_children: true
permalink: /en/
---

# Azure Catalyst Hands-on Lab

Start from a **single ServiceNow CMDB export file**. With Azure Catalyst (formerly Azure Migrate) you will go through **discovery → applications → business case → assessment → Catalyst agent → planning**, and finally modernize a legacy **Java (Tomcat) app to Azure Container Apps**.

{: .note }
The lab is Azure Portal based, with **Azure CLI** alternatives where possible. Every screenshot was captured from a real Azure Portal. **Click a screenshot to enlarge it.** Some screenshots show Korean locale details (for example date formats or browser labels). That does not change any step.

## Highlights

| Highlight | Description |
|---|---|
| Almost no cost | Catalyst is a free service. You start with **file import**, so no appliance and no VMs are needed. Only the last deployment module costs a little, and the cleanup step deletes everything. |
| Start without servers | No on-premises environment required. You get the CMDB export (31 rows) of a fictional customer, **Contoso Korea**. |
| AI-centric | You use **AI-powered inventory import** and the **Catalyst agent** (both in Preview). |
| From plan to execution | The plan Catalyst produces leads into a real code modernization with GitHub Copilot **Modernize CLI**. |

## Modules

| # | Module | What you do | Time |
|---|---|---|---|
| 0 | [Setup]({{ '/en/lab0-setup/' | relative_url }}) | Prepare the subscription, register resource providers, create the Catalyst project | 15 min |
| 1 | [Discover: AI file import]({{ '/en/lab1-discover/' | relative_url }}) | Upload the CMDB export and build an inventory | 25 min |
| 2 | [Applications and security risks]({{ '/en/lab2-applications/' | relative_url }}) | Group servers into applications | 20 min |
| 3 | [Business case]({{ '/en/lab3-business-case/' | relative_url }}) | Azure cost and value versus on-premises | 25 min |
| 4 | [Assessment and migration strategy]({{ '/en/lab4-assessment/' | relative_url }}) | 6R strategy and Azure target services | 15 min |
| 5 | [Catalyst agent]({{ '/en/lab5-agent/' | relative_url }}) | Strengths and limits of the agent, verifying answers | 20 min |
| 6 | [Wave plan and landing zone]({{ '/en/lab6-plan/' | relative_url }}) | Wave risks and the Platform Landing Zone design | 20 min |
| 7 | [Java modernization and deployment]({{ '/en/lab7-java-modernize/' | relative_url }}) | Modernize with the Modernize CLI and deploy to Container Apps | 90 min |
| 8 | [Cleanup]({{ '/en/lab8-cleanup/' | relative_url }}) | Delete the resources | 5 min |

## Scenario: Contoso Korea

| App | Description | Notes |
|---|---|---|
| ORD | Order portal | Java 8 + Tomcat 9, MySQL. **Modernized in module 7** |
| HRM | HR system | .NET Framework 4.8, IIS, SQL Server 2019 |
| ERP | Legacy ERP | Windows Server 2012 R2, SQL Server 2012 (end of support) |
| RPT | Reporting / BI | SQL Server 2017 BI |
| SHR | Shared infrastructure | AD, file server, monitoring, backup |

## Good to know

- Many features are **Preview**. AI results (number of warnings, agent answers) can differ a little every time you run them.
- Features and screens can change, so check the [Azure Catalyst documentation](https://learn.microsoft.com/azure/migrate/migrate-services-overview) as well.
- To use the **Catalyst agent**, **Azure Copilot** must be enabled in your tenant.
