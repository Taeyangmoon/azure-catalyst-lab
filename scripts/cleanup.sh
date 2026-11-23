#!/usr/bin/env bash
# 랩에서 만든 리소스 그룹을 삭제합니다.
set -euo pipefail
for rg in rg-orders-lab rg-catalyst-lab; do
  if [ "$(az group exists -n "$rg")" = "true" ]; then
    echo "삭제: $rg"
    az group delete -n "$rg" --yes --no-wait
  fi
done
echo "삭제를 요청했습니다. 완료까지 몇 분 걸립니다."
