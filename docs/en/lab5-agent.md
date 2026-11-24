---
permalink: /en/lab5-agent/
title: "5. Catalyst agent"
parent: English
nav_order: 6
---

# 5. Catalyst agent: what it does well and how far to trust it

⏱️ About 20 min · 💰 No extra cost

The **Catalyst agent** (formerly Azure Copilot Migration Agent, Preview) lets you **ask questions in natural language and analyze** the inventory, applications, business case and assessment data of your project. In this module you separate for yourself **the questions it answers well** from **the questions where it is limited**.

{: .warning }
The agent is a **tool that helps with planning and analysis**. In this lab some answers contained **numbers that differ from the portal screens**. Always **check agent answers against the original portal screens**.

## What you learn
- How to open the agent and choose a project
- **Strong questions** (interpreting the business case) and **weak questions** (application level strategy details)
- The habit of **verifying**

## 1. Open the agent

1. Select **Copilot** at the top of the Azure Portal.
2. In the drop-down next to **New chat**, choose **Catalyst**.
3. Under the input box, select the `catalyst-lab` project in **Work in a project**.

![Open the agent]({{ '/images/shots/d1-agent-open.png' | relative_url }})

{: .note }
If you do not see the agent, check that **Azure Copilot is enabled** in your tenant and that your administrator allows access. In module 1, selecting **Review inventory** opened the same agent in a right-hand panel.

## 2. A strong question: interpreting the business case

Enter the following.

```text
Summarize my business case bc-contoso-apps: on-premises cost, recommended path, future cost, savings and Azure value uplift. Why is the PaaS preferred path recommended even though its savings are lower than lift and shift?
```

![Business case summary]({{ '/images/shots/d2-agent-business-case.png' | relative_url }})

After about a minute the agent answered with **tables and charts**. It **matches** the numbers you read yourself in module 3.

| Item | Agent answer |
|---|---|
| On-premises / Azure cost | $314.76K / $201.83K (per year) |
| Savings / Value uplift / Total benefit | $112.94K / $106.69K / $219.63K |
| Items that create savings | **Storage $54.94K**, **IT labor $45.05K**, **Management $33.04K**, and more |
| Items more expensive on Azure | **Network -$13.74K**, **Compute & licensing -$21.80K** |
| Why PaaS preferred | Because of the `Modernize (AI ready)` preference, and its total economic benefit is higher |

This is the answer to the first "question to think about" in module 3. The agent summarizes in one go the category comparison that the portal only shows when you **expand** it.

## 3. A weak question 1: group the servers into applications

Right after module 1, before the applications existed, we asked the following.

```text
Group the servers into applications using the naming convention krc-<app>-<role>-<number> (ORD, HRM, ERP, RPT, SHR). For each application list its servers, environment and any end-of-support operating system or database.
```

**The agent did not group them itself.** It answered that it cannot read or regroup the servers directly here, and instead suggested three methods: **Auto discover**, **CSV import** and **manual definition**. The **Auto discover** you did in module 2 is exactly that method. Sometimes the agent is a **guide rather than an executor**.

## 4. A weak question 2: explain the strategy per application

```text
My application assessment is named bc-contoso-apps-asmt. List its applications with workloads, complexity and business criticality, then explain the recommended migration strategy and Azure target for each application, and why ERP and HRM are replatform while ORD is rehost.
```

![Assessment question]({{ '/images/shots/d3-agent-6r.png' | relative_url }})

This time the agent only gave the **assessment-wide summary** and admitted it **cannot give per-application details**. It suggested opening the Applications view in the portal. When we first asked the same question **without the assessment name**, it asked back which assessment to use.

### Compare with the portal

| Item | Agent | Portal (module 4) |
|---|---|---|
| Workload count | **27** | **32** |
| Strategy ratio | Rehost 81% / Replatform 19% (**per workload**) | Rehost 40% / Replatform 60% (**per application**) |
| Strategy per application | Could not say | Each of the 5 applications is shown |

The two numbers may be on **different bases** (workloads versus applications), or the agent may have seen only part of the data. Either way, **quoting only the agent's numbers to a customer is risky**.

## Summary: how to work with the agent

| What it does well | Be careful with |
|---|---|
| Business case summary, category comparison, **explaining reasons** | Per application strategy details, workload lists |
| Guiding next steps (Auto discover, Import, and so on) | "Please run it for me" |
| The **inventory review and fix proposals** you saw in module 1 (merging, filling values) | Not stating the **target name** (assessment, business case) exactly |

{: .note }
The answer language **can follow your portal language setting**. In this lab the agent once answered in Korean even though we asked in English.

## Check your understanding
1. Where does the agent's "27 workloads" come from? Where in the portal can you find 27? (Hint: compare whether the module 4 assessment screens count workloads in servers, SQL instances or applications.)
2. If you quote the agent's answer in a customer meeting, what wording should you add?

➡️ Next: [6. Wave plan and landing zone]({{ '/en/lab6-plan/' | relative_url }})
