---
permalink: /lab8-cleanup/
title: "8. 정리"
parent: 한국어
nav_order: 10
---

# 8. 정리: 만든 리소스 모두 삭제

⏱️ 약 5분 · 💰 비용을 멈추는 단계입니다

Catalyst는 무료지만 **스토리지 계정**이 남고, 7장에서 만든 **MySQL, ACR, Container Apps 환경**은 켜 둔 시간만큼 비용이 듭니다. 실습이 끝나면 바로 삭제하세요.

## 한 번에 삭제

```bash
./scripts/cleanup.sh
```

스크립트는 `rg-orders-lab`(7장)과 `rg-catalyst-lab`(0장) 리소스 그룹을 삭제합니다.

## 직접 삭제

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

**리소스 그룹** → `rg-orders-lab` → **리소스 그룹 삭제**, 같은 방법으로 `rg-catalyst-lab`도 삭제합니다. 확인 창에 리소스 그룹 이름을 입력해야 합니다.
</details>
{% endraw %}

## 확인

```bash
az group list --query "[?starts_with(name,'rg-orders') || starts_with(name,'rg-catalyst')].name" -o tsv
```

아무것도 나오지 않으면 삭제가 끝난 것입니다. (삭제에는 몇 분이 걸립니다.)

{: .note }
GitHub에 올린 개인 저장소와 Modernize CLI가 만든 로컬 폴더(`~/orders`)는 비용이 없지만 필요 없으면 지우세요.

## 비용 확인

**Cost Management → Cost analysis**에서 실습 기간의 비용을 확인해 보세요. 이 랩이 거의 무료인 이유는 **서버를 하나도 만들지 않고**, 만든 리소스는 가장 작은 SKU와 소비형 요금제를 쓰고, **바로 삭제**했기 때문입니다.
