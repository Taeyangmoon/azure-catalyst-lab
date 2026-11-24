---
permalink: /en/lab3-business-case/
title: "3. Business case"
parent: English
nav_order: 4
---

# 3. Business case: how much do you gain by moving?

⏱️ About 20 min (including 8 to 15 min waiting for the calculation) · 💰 No extra cost

Before moving a single server, you **compare on-premises cost with Azure cost** and create the first number you can show to management. The key point of this module is to set the **applications** you created in module 2 as the scope.

## What you learn
- Why the **scope** of a business case matters
- Comparing the **PaaS preferred** and **Lift and shift** paths
- Reading **Total economic benefit**, which adds cost savings and **Azure Value Uplift**

## 1. Create the business case

1. Select **Decide → Business case → Create business case**.
2. Enter `bc-contoso-apps` in **Name**. Under **Configuration**, choose **Define configuration** and select the **Define configuration** button.

### Basics tab: scope = "applications"

Under **Scope**, choose **Select workloads or applications** and select **Add applications**. Select all 5 applications and select **Add**. If a confirmation appears saying the applications are auto-discovered, select **Confirm**.

![Select applications]({{ '/images/shots/c1b-bc-select-apps.png' | relative_url }})

It is fine when you see **5 applications (with 32 workloads) selected**.

![Basics]({{ '/images/shots/c1-bc-basics.png' | relative_url }})

{: .warning }
**You cannot change the scope after creation.** If you create it with **Entire data center** before defining applications, the result differs as described in ["Without applications"](#without-applications) below, and **Recompute** does not fix it.

### General tab

| Setting | Value | Why |
|---|---|---|
| Default target location | **Korea Central** | Region used for cost calculation |
| Migration preference | **Modernize (AI-ready)** | Prefer **PaaS** over IaaS where possible |
| Default savings option | 3 years reserved as applicable | Assumes 3-year reservations |
| Include Platform Landing Zone recommendations | **Checked** | Used by the landing zone in module 6 |
| Sizing criteria | **As on-premises** | File import has **no performance data** |

![General]({{ '/images/shots/c2-bc-general.png' | relative_url }})

{: .warning }
**Performance-based** sizing needs CPU and memory utilization history. An inventory created with file import has no history, so you must choose **As on-premises** (based on current specifications).

### Advanced tab and creation

Keep the defaults on Advanced, select **Save**, and then select **Create business case**.

![Advanced]({{ '/images/shots/c3-bc-advanced.png' | relative_url }})

![Create]({{ '/images/shots/c4-bc-create.png' | relative_url }})

## 2. Read the result

The calculation takes **about 8 to 15 minutes**. When the status is **Ready**, open `bc-contoso-apps`.

![Business case overview]({{ '/images/shots/c5-bc-overview.png' | relative_url }})

| Item | PaaS preferred (recommended) | Lift and shift |
|---|---:|---:|
| On-premises cost (per year) | $314.76K | $314.76K |
| Azure future cost | $201.83K | $195.58K |
| Cost savings | $112.94K | **$119.18K** |
| **Azure value uplift** | **$106.69K** | $67.81K |
| **Total economic benefit** | **$219.63K** | $186.99K |

- **Total economic benefit = cost savings + Azure value uplift.**
- Value uplift splits into three parts. For PaaS preferred it is **Business agility $80K (75%)**, **Resiliency $24.3K (23%)** and Productivity $2.39K (2%).
- **PaaS preferred saves less pure cost than Lift and shift, but its total benefit is larger once value uplift is added.** That is why PaaS preferred is the recommended path when you chose `Modernize (AI-ready)`.

{: .note }
The numbers are **estimates** and depend on prices and assumptions at the time you run it. In this lab, **reading the structure** matters more than the numbers themselves.

## Without applications

For comparison, we also checked a business case created with the **Entire data center** scope. It contains all 28 servers and 5 SQL instances together.

![Created without applications]({{ '/images/shots/f3-value-uplift-zero.png' | relative_url }})

**Azure value uplift is $0** and the page says "Not calculated for this business case". The reason is written on the screen: **Azure Value Uplift is calculated per application.** This business case has no applications, so all 33 workloads are "ungrouped". The fix is also on the screen. **Create a new business case and include the applications in the scope.**

## Questions to think about
1. Which items create the savings, and which items are **more expensive** on Azure? (You ask the agent in module 5. Hint: storage, IT labor and management versus network, compute and licensing)
2. If a customer says "just show me the cost", which path would you recommend? How far can you trust the value uplift? (See the phrase "published industry benchmarks, risk adjusted" on the screen.)
3. If you rename `Application_ord` to `Order portal` and raise its criticality to High, how does the value uplift change? (Try it yourself.)

➡️ Next: [4. Assessment and migration strategy]({{ '/en/lab4-assessment/' | relative_url }})
