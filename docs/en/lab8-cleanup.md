---
permalink: /en/lab8-cleanup/
title: "8. Cleanup"
parent: English
nav_order: 9
---

# 8. Cleanup: delete everything you created

⏱️ About 5 min · 💰 This is the step that stops the cost

Catalyst is free, but a **storage account** remains, and the **MySQL, ACR and Container Apps environment** from module 7 cost money for as long as they run. Delete them as soon as the lab is over.

## Delete everything at once

```bash
./scripts/cleanup.sh
```

The script deletes the `rg-orders-lab` (module 7) and `rg-catalyst-lab` (module 0) resource groups.

## Delete by hand

{% raw %}
<details open markdown="1">
<summary><b>Azure CLI</b></summary>

```bash
az group delete --name rg-orders-lab --yes --no-wait
az group delete --name rg-catalyst-lab --yes --no-wait
```
</details>

<details markdown="1">
<summary><b>Azure Portal</b></summary>

**Resource groups** → `rg-orders-lab` → **Delete resource group**, and do the same for `rg-catalyst-lab`. You must type the resource group name in the confirmation pane.
</details>
{% endraw %}

## Verify

```bash
az group list --query "[?starts_with(name,'rg-orders') || starts_with(name,'rg-catalyst')].name" -o tsv
```

If nothing is returned, the deletion is complete. (Deletion takes a few minutes.)

{: .note }
The personal GitHub repository you pushed to and the local folder created by the Modernize CLI (`~/orders`) cost nothing, but delete them if you do not need them.

## Check the cost

Check the cost of the lab period in **Cost Management → Cost analysis**. This lab is almost free because **no server was created**, the resources we did create use the smallest SKUs and consumption pricing, and we **deleted them right away**.
