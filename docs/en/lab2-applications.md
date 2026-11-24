---
permalink: /en/lab2-applications/
title: "2. Applications and security risks"
parent: English
nav_order: 3
---

# 2. Applications: you move apps, not servers

⏱️ About 20 min (including 10 to 15 min waiting for auto discovery) · 💰 No extra cost

The inventory holds 28 servers as **separate items**. But the unit a customer decides to migrate is not a server. It is an **application such as "order portal" or "HR system"**. In this module you group servers into applications, and look at security risks while you wait.

{: .note }
Modernization means **moving applications**, not servers. Catalyst's business case, wave plan and landing zone design all need **application level** information to work well.

## What you learn
- **Auto discover applications**: automatic grouping by server name patterns
- Reading an application's **complexity, business criticality and confidence score**
- Checking end-of-support risk with **Security Insights**

## 1. Auto discover applications

1. Open **Discover → Applications**. If there are no applications yet, you see the **No applications** screen.
2. Select **Auto discover**.
3. Read the guidance in the right pane and select **Discover applications**.

![Auto discover]({{ '/images/shots/b1-auto-discover-apps.png' | relative_url }})

As the screen explains, Catalyst groups applications using **server name patterns, inferred environments and server roles**. The server names in this lab follow the rule `krc-<app code>-<role>-<number>`, so they group well.

{: .warning }
The official documentation says completion can take **up to 1 hour**. In this lab it took **about 13 minutes**. After you select the button, make sure a notification says "initiated successfully". If there is no notification, it did not start.

## 2. While you wait: Security Insights

Open **Decide → Security Insights**.

![Security Insights]({{ '/images/shots/c6-security-insights.png' | relative_url }})

**10 of the 28 servers are at operating system end of support.** They are Windows Server 2012 R2/2008 R2 and RHEL 7.9. You already have a reason for "why modernize now" from the inventory alone.

{: .note }
Software risks (`Software end of support`, `With vulnerabilities`) show **0** in this lab. The **Software recommendations** page says "No software data". Installed software information is only visible when it is collected, for example, with an appliance.

## 3. Check the result

When auto discovery finishes, 5 applications appear under **Applications**. Select **Refresh** if you do not see them.

![Applications]({{ '/images/shots/f1-applications.png' | relative_url }})

| Application | Workloads | Complexity | Confidence |
|---|---:|---|---:|
| `Application_ord` (order portal) | 8 | **High** | 90 |
| `Application_hrm` (HR) | 7 | Medium | 92 |
| `Application_erp` (legacy ERP) | 6 | Medium | 82 |
| `Application_shr_3` (shared infrastructure) | 6 | Medium | 90 |
| `Application_rpt` (reporting/BI) | 5 | Medium | 93 |

- The total is **32 workloads**. One of the 33 workloads does not belong to any application.
- Only `Application_ord` has **High** complexity. It has web servers, app servers and a database, and it has the most servers.
- The application with the lowest **confidence score** is `Application_erp` (82). That means a name pattern alone leaves some uncertainty. In a real project, a **person should review** such an application.

{: .note }
The names such as `Application_ord` are given by the system. You can open an application and edit its name and properties (such as business criticality). **Check by eye** that the applications are grouped correctly from the server names alone.

## Check your understanding
1. Open `Application_ord` and see which servers it contains. Are the staging and development servers (`-s1`, `-dev-`) in the same application?
2. Which workload out of 33 is not in any application? (Hint: the **Application name(s)** column in All inventory)

➡️ Next: [3. Business case]({{ '/en/lab3-business-case/' | relative_url }})
