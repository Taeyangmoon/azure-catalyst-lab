---
permalink: /en/lab4-assessment/
title: "4. Assessment and migration strategy"
parent: English
nav_order: 5
---

# 4. Assessment: where and how to move each app

⏱️ About 15 min · 💰 No extra cost

When you create the business case, Catalyst also creates an **assessment** for you. For each application, the assessment shows the **migration strategy (6R), Azure target service, readiness and monthly cost**.

## What you learn
- Comparing **paths**: PaaS preferred / PaaS only / Lift and shift
- **Strategy distribution**: Rehost and Replatform share per application
- Reading strategy and cost at the **application level**

## 1. Assessment list

Open **Plan → Assessments**. `bc-contoso-apps-asmt`, created automatically when you created the business case, is there.

![Assessment list]({{ '/images/shots/e1-assessments-list.png' | relative_url }})

{: .note }
This screenshot also shows `bc-contoso-korea-asmt` (from the business case created without applications in module 3, for comparison). If you follow this lab as written, you see only `bc-contoso-apps-asmt`.

## 2. Assessment overview

Open `bc-contoso-apps-asmt`.

![Assessment overview]({{ '/images/shots/e2-assessment-overview.png' | relative_url }})

| Item | Value seen in this lab |
|---|---|
| Recommended path | **PaaS preferred** |
| Scope | **5 applications (32 workloads)** |
| Azure readiness | **100% Ready** |
| Monthly cost estimate | About **$11,052** (Compute $5,393 / Storage $3,485 / Network $1,765 / Security $403) |
| Strategy distribution (per application) | **Replatform 60%**, **Rehost 40%** |
| Lift and shift path | About **$9,124** per month (everything Rehost) |

{: .note }
**Why is PaaS preferred (about $11,052/month) recommended when it costs more than Lift and shift (about $9,124)?** The monthly cost in the assessment is an **infrastructure cost comparison**. The benefits of the PaaS path (less management and patching, availability) show up in the **business case** from module 3 (IT labor and management savings, value uplift). This number also includes **network cost ($1,765)**, because you included the Platform Landing Zone in the business case.

## 3. Path details (View path details)

Open **View path details** for **PaaS preferred**. The strategy is shown per application.

![Path details]({{ '/images/shots/e3-path-details.png' | relative_url }})

| App | Strategy | Monthly cost (USD) | Workloads |
|---|---|---:|---:|
| `Application_shr_3` (shared infrastructure) | **Rehost** | 3,633.56 | 6 |
| `Application_ord` (order portal) | **Rehost** | 1,360.82 | 8 |
| `Application_erp` (legacy ERP) | **Replatform** | 1,446.03 | 5 |
| `Application_hrm` (HR) | **Replatform** | 1,376.50 | 5 |
| `Application_rpt` (reporting/BI) | **Replatform** | 2,783.25 | 3 |

The **Replatform** applications (ERP, HRM, RPT) are those whose SQL Server becomes a PaaS target such as **Azure SQL Managed Instance**. The **Rehost** applications (shared infrastructure, order portal) were assessed as moving their servers to Azure VMs.

## 4. Application detail (Application design)

Open `Application_ord` under **Plan → Application design**.

![Application_ord design]({{ '/images/shots/h5-app-design-ord.png' | relative_url }})

| Item | `Application_ord` |
|---|---|
| Application readiness | **Ready with conditions** (all 8 workloads are "ready with conditions") |
| Migration strategy | **Rehost** |
| Monthly cost | $1,360.82 (Compute 626 / Storage 202 / Security 119 / Network 412) |
| Tabs | Servers (8), Network (39), Peripherals (19), and more |

The **Code insights** box says: *"Code insights apply exclusively to **Replatform and Refactor** strategies. This application has been recommended for Rehost."* It also means that to modernize this app's Tomcat/Java code (module 7), you run the **Modernize CLI separately** rather than using Catalyst assessment Code insights.

{: .note }
Check **Show readiness issue/warnings** to see what causes `Ready with conditions`. In this lab the OS version or end-of-support status of the 8 servers may be involved, but we did not open it to confirm the exact cause.

## Check your understanding
1. What additional information would you need to change `Application_ord` from **Rehost** to **Replatform**? (Hint: the Code insights note in section 4 and the 0 Web apps from module 1)
2. ERP is **Windows Server 2012 R2 + SQL Server 2012**. Does a Replatform assessment mean it is safe to move right now?
3. The **Rehost 40% / Replatform 60%** in the assessment is **per application**. Is it the same as the workload level ratio? (Compare with the agent answer in module 5.)

➡️ Next: [5. Catalyst agent]({{ '/en/lab5-agent/' | relative_url }})
