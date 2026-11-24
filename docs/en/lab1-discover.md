---
permalink: /en/lab1-discover/
title: "1. Discover: AI file import"
parent: English
nav_order: 2
---

# 1. Discover: AI file import

⏱️ About 25 min (including 5 to 10 min waiting for the AI analysis) · 💰 No extra cost

You upload **the file exported from the customer's ServiceNow CMDB as is** and build an inventory of servers, SQL instances and web servers. No appliance and no server credentials are needed.

{: .note }
**AI-powered file import is in Preview.** Even for the same file, values the AI interprets (such as the number of warnings) can differ a little. It is normal if your screens and numbers differ slightly from this document.

## What you learn
- How to upload a customer tool (CMDB) export **without converting it to a template**
- How a **context file** tells the AI about column meanings, units and exclusion rules
- How to **review and fix the AI proposal with the Catalyst agent**

## Why the context file matters

The ZIP you upload contains `context-naming-conventions.md` together with the CSV. It tells the AI the following.

| What the context file says | Effect |
|---|---|
| `install_status` value 7 = Retired. Exclude | The 2 retired servers are left out of the inventory |
| Server names that differ only by case are the same server | The duplicate candidate is recognized |
| `ram` is in MB, `disk_space` is in GB | Unit interpretation |
| `u_installed_software` contains database and web server information | SQL Server, IIS and Tomcat are recognized |

{: .warning }
**Upload the context file together with the CSV in the first upload (or as a ZIP).** Also, **you cannot edit the imported metadata after Save to inventory.** Review carefully before saving.

## 1. Start the import

1. Open the `catalyst-lab` project in the Azure Portal. (Azure Catalyst → All projects → `catalyst-lab`)
2. On **Overview**, select **Start discovery**.
3. Select **Using file import (AI-powered)**, choose **Inventory exports from ServiceNow, Ansible, Dynatrace, etc.** under **File type**, and select **Continue**.

![Choose a discovery method]({{ '/images/shots/a2-discover-file-type.png' | relative_url }})

## 2. Upload the ZIP and analyze

1. Select the browse icon of **File source** and choose [`contoso-korea-cmdb.zip`](https://github.com/Taeyangmoon/azure-catalyst-lab/raw/main/data/contoso-korea-cmdb.zip).
2. Select **Analyze file**. The analysis **usually takes 3 to 6 minutes**.

![ZIP selected]({{ '/images/shots/a3-file-selected.png' | relative_url }})

When the analysis finishes, a summary appears.

![Analysis result]({{ '/images/shots/a4-analysis-result.png' | relative_url }})

| Result item | Value seen in this lab | Interpretation |
|---|---|---|
| Files analyzed | 2 (CSV + context) | The context file was interpreted too |
| Servers | **29** | **2 retired servers excluded** from the 31 source rows |
| SQL instances | **5** | Recognized from servers with SQL Server installed (SQL of retired servers excluded) |
| IIS / Tomcat web servers | 4 / 4 | Recognized from `u_installed_software` |
| Priority items | 1 server with empty CPU, memory and disk | `krc-tst-web-99` has empty values |
| 0 records were correlated or merged | The duplicate is **not merged yet** | `KRC-ORD-WEB-01` is still separate |

{: .note }
The exact number of warnings can differ slightly each time. What matters is that you can read **what needs to be fixed**.

## 3. Review and fix with the Catalyst agent

1. Select **Review inventory** at the lower right of the summary. A **Catalyst Agent** panel opens on the right and shows a summary.
2. Ask the agent the following two things.

```text
Which server is missing CPU, memory and disk values? Also, KRC-ORD-WEB-01 is the same server as krc-ord-web-01 (names differ only by case). Please merge them and show me the proposed changes.
```

The agent answers that `krc-tst-web-99` has empty values and that it **merged the two duplicate rows into one**. (Server count 29 → 28)

```text
For krc-tst-web-99, use the same sizing as krc-ord-dev-01 (4 vCores, 8192 MB memory, 150 GB storage). Apply the change and confirm the final server count.
```

![Review with the agent]({{ '/images/shots/a5-review-with-agent.png' | relative_url }})

{: .note }
The values the agent changes are **proposals before saving**. The agent reflects such changes in `import-logic.txt` (mapping rules) and `AllInventory-*.csv` (values). If you prefer to edit by hand, you can also use **Download file**, edit the files and upload them again.

## 4. Save to inventory

Select **Save to inventory**. You cannot edit the data after saving.

![Save]({{ '/images/shots/a6-save-to-inventory.png' | relative_url }})

Processing the inventory after saving takes about **10 to 15 minutes**. While it is processing, **All inventory** shows the message "Inventory upload using file import is in progress". The official documentation recommends waiting about **30 minutes** before assessments, but in this lab we moved on to the next step as soon as the message disappeared.

## 5. Check the result

Open **Discover → All inventory**.

![All inventory]({{ '/images/shots/a7-all-inventory.png' | relative_url }})

What to check:

| Check | Expected result |
|---|---|
| `krc-old-*` servers | Not shown (retired servers excluded) |
| `KRC-ORD-WEB-01` | Not shown separately. Only `krc-ord-web-01` exists (merged) |
| Security risks column | Windows Server 2012 R2/2008 R2 and RHEL 7.9 are shown as **end of support** |
| Databases | 5 SQL Server instances |

{: .warning }
**Web apps is 0.** File import recognizes Tomcat and IIS only as **web server software**. Web app (application level) assessment and **Code Insights** require appliance-based discovery. The Java modernization module of this lab uses the GitHub Copilot Modernize CLI directly.

## Troubleshooting

| Symptom | Action |
|---|---|
| The analysis ends as **Failed** | Instead of using **Retry with new file** in the same project, it was safer to **create a new project and try again**. (While building this lab, one project kept failing after a retry.) |
| It failed when I uploaded the context file separately | The context must be uploaded **together as a ZIP in the first upload**. |
| The agent panel does not open | Select **Review inventory** again or check that Azure Copilot is enabled. |
| The inventory is empty after saving | Wait 10 to 15 minutes and select **Refresh** |

➡️ Next: [2. Applications and security risks]({{ '/en/lab2-applications/' | relative_url }})
