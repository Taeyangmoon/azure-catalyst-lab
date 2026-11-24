---
permalink: /en/lab6-plan/
title: "6. Wave plan and landing zone"
parent: English
nav_order: 7
---

# 6. Plan: what to move first, and where to

⏱️ About 20 min · 💰 No extra cost

Based on the assessment, you create a **wave plan** (the order of moving) and a **Platform Landing Zone** (the skeleton of the target environment).

{: .note }
In this lab you only **plan**. You do not **commit** waves and you do not **deploy** the landing zone. Actually moving workloads needs appliance-based discovery and replication tools.

## What you learn
- The structure of a **Wave plan** and reading its **risks**
- The Platform Landing Zone, which is needed **before any wave**
- Reading the automatically generated **design document (HLD/LLD)**

## 1. Create the wave plan

Open `bc-contoso-apps-asmt` under **Plan → Assessments**, and in **View path details** of the recommended path **PaaS preferred**, select **Generate wave plan**.

![Generate the wave plan]({{ '/images/shots/h1-generate-wave-plan.png' | relative_url }})

A few minutes later the plan appears under **Plan → Wave plan**.

![Wave plan]({{ '/images/shots/h3-wave-plan.png' | relative_url }})

| Item | Value seen in this lab |
|---|---|
| Waves | **1** (Wave1, status **Proposed**) |
| Applications / Workloads | 5 / 32 |
| Risk | **2** |
| Monthly Azure cost | $9,287.40 |

There is only **one** wave. The screen does not state the exact reason, but **risk 01** below gives an important clue.

### Risks and recommendations

![Wave risks]({{ '/images/shots/h4-wave-risks.png' | relative_url }})

| Risk | Severity | Description | Recommendation |
|---|---|---|---|
| 01 | **Medium** | The plan was created with partial inputs because **dependency analysis data is missing** (27 workloads affected) | Enable **dependency analysis**, collect data and regenerate, or use **tags** to group workloads that must move together |
| 02 | **High** | A **highly complex application is in an early wave (Wave1)** (`Application_ord`, 8 workloads) | Move the complex application to a later wave, or if it must go early, **rehearse and confirm rollback** |

**Wave plan reasoning** says roughly: "Each application's workloads are kept in a single wave. Workloads that are more ready, simpler, less business critical and need less effort (for example, rehost before refactor) go to earlier waves." The **file import in this lab has no dependency information**, and the plan created in that state has just one wave, with the most complex application `Application_ord` in Wave1. It is a good example of **not trusting a plan as it is**.

{: .note }
To edit the waves yourself, use **Configure → Waves**: download the mapping file, edit it and upload it again. We did not download the file in this lab.

## 2. Platform Landing Zone

The note on the wave plan screen says: **"The Platform Landing Zone must be established before migrating any wave."** It is the shared foundation of network, identity, security and governance. The estimated cost is **$451.95 per month** and it is **not included** in the wave cost.

Open **Plan → Application design**. Under **Related artifacts**, above the application list, you find **Wave plan** and **Platform landing zone**.

![Application design]({{ '/images/shots/h2-application-design.png' | relative_url }})

Select **Platform landing zone**.

![Platform Landing Zone]({{ '/images/shots/h7-platform-landing-zone.png' | relative_url }})

Catalyst generates a **design document** (Platform Landing Zone - Design Document).

| Item | Content |
|---|---|
| Structure | **Single-region Hub-and-Spoke**, **Azure Firewall Basic** |
| Scale | **Lite** tier |
| Basis | Azure Landing Zone (ALZ) accelerator, CAF/WAF guidance |
| Components | Management group hierarchy, platform subscriptions, hub VNet, Azure Firewall, VPN Gateway, Log Analytics + Defender for Cloud |
| Parts | **A. HLD** (high-level design for architects), **B. LLD** (per-resource values for IaC engineers) |

### Change the region to Korea Central

{: .warning }
The **Region of the Platform Landing Zone that Catalyst creates first is EASTUS.** Even if you set Korea Central in the business case, the **default PLZ is generated separately**. Also, **View settings** below has **no field to choose the region.** You change the region with the **Catalyst agent**. (The Platform Landing Zone article on Learn also guides you to generate it through a conversation with the agent.)

1. Open the **Catalyst** agent from **Copilot** at the top and select the `catalyst-lab` project.
2. Ask as follows.

```text
I want to create a Platform Landing Zone (PLZ). Use Korea Central as the Azure region.
```

The agent proposes a configuration with **Single-region (Korea Central)**. The default proposal is the **Full tier** (Azure Firewall **Premium**, DDoS, ExpressRoute Gateway, Bastion and Private DNS all **On**). For the lab, ask again to keep **Lite** for cost and simplicity.

```text
Keep the Lite scale tier with Azure Firewall Basic, Hub and Spoke, region Korea Central. Turn off DDoS Protection, ExpressRoute Gateway, Azure Bastion and Private DNS. Use GitHub for deployment. Generate the Platform Landing Zone now.
```

The agent starts the generation and says the **design document and the IaC ZIP** are created asynchronously (about 2 minutes). After it completes, open **Application design → Platform landing zone** again, and the **Region in the design document is KOREACENTRAL**.

![Request Korea Central through the agent]({{ '/images/shots/h10-agent-plz-request.png' | relative_url }})

| Item | At first (default) | After the agent request |
|---|---|---|
| Region | EASTUS | **KOREACENTRAL** |
| Tier / firewall | Lite / Azure Firewall Basic | Lite / Azure Firewall Basic (same) |
| Estimated monthly cost | $451.95 | $451.95 (the displayed value is the same) |

{: .note }
If you **accept the Full tier** the agent proposes, the number of components grows a lot and so does the cost. We did not check the cost of the Full tier in this lab. The generated **IaC ZIP (Terraform)** can be downloaded, refined in VS Code and deployed ([documentation](https://learn.microsoft.com/azure/migrate/platform-landing-zone)), but in this lab we **did not deploy it and did not download the ZIP.**

### Change settings (View settings)

![Settings]({{ '/images/shots/h8-plz-settings.png' | relative_url }})

In settings you choose the **subscription placement** (Identity, Connectivity, Management, Security), the **network preference** (Hub and spoke or Virtual WAN), the **firewall** (Azure Firewall or third-party NVA), the **deployment option** (GitHub, Azure DevOps, local file system) and the **DDoS Protection, ExpressRoute Gateway, Private DNS and Bastion** options.

## 3. Generate IaC (take a look)

When you open `Application_ord`, there is a **Generate IaC** button at the top. Selecting it starts a job that turns the application design into code.

{: .warning }
In this lab we only confirmed that **the job starts** after selecting the button. We could not confirm the content or location of the generated IaC files. Explore this feature yourself.

## Check your understanding
1. What should you do to improve a wave plan that ended with only one wave? (Hint: the recommendation of risk 01)
2. If you move `Application_ord` to a later wave to resolve risk 02 (High), which **dependencies** should you check first in the design?
3. Why is the Platform Landing Zone **not included** in the wave cost?

➡️ Next: [7. Java modernization and deployment]({{ '/en/lab7-java-modernize/' | relative_url }})
