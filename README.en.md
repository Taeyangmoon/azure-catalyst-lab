# Azure Catalyst Hands-on Lab

🌐 [한국어](README.md) | **English**

Start from a ServiceNow CMDB export, plan a modernization with Azure Catalyst (formerly Azure Migrate), and move a legacy Java app to Azure Container Apps.

📖 **Lab guide (GitHub Pages)**: https://taeyangmoon.github.io/azure-catalyst-lab/en/

> Educational sample. AI-powered file import and the Catalyst agent are in **Preview**, and screens and behavior can change. All screenshots were captured from a real Azure Portal. Click a screenshot to enlarge it.

## Modules

| # | Module | Time |
|---|---|---|
| 0 | Setup: subscription, resource providers, project | 15 min |
| 1 | Discover: AI file import (ServiceNow CMDB + context file) | 25 min |
| 2 | Auto discover applications and Security Insights | 20 min |
| 3 | Business case (Azure Value Uplift) | 25 min |
| 4 | Assessment and migration strategy (6R) | 15 min |
| 5 | Strengths and limits of the Catalyst agent | 20 min |
| 6 | Wave plan and Platform Landing Zone | 20 min |
| 7 | Java modernization (Modernize CLI) and deployment to Container Apps | 90 min |
| 8 | Cleanup | 5 min |

## Cost

Catalyst is free and you start with file import, with no appliance or VMs. Only the deployment in module 7 (MySQL B1ms, ACR Basic, Container Apps) costs a little, and the cleanup step deletes it. (Estimated at list price, around $0.3 in total. Check Cost Management.)

## Repository layout

| Path | Content |
|---|---|
| `docs/` | Lab guide (GitHub Pages), Korean at the root and `en/` for English |
| `data/` | Contoso Korea CMDB export, context file and the ZIP to upload |
| `scripts/` | Data generation, Azure deployment and cleanup scripts |
| `apps/legacy-orders/` | The legacy Java (Tomcat) app to modernize (UI, logs and comments in **English**, shared by both labs) |
| `apps/legacy-orders-modernized/` | The result modernized with the Modernize CLI (reference answer) |

## Known limitations

- Catalyst web app assessment and Code Insights need appliance-based discovery, so they cannot be used in this lab (file import).
- Performance-based sizing needs performance history, so the lab uses `As on-premises`.
- AI results (number of warnings, agent answers) can differ a little every time.
