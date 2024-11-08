#!/bin/sh

# Vault 서버 시작 (백그라운드)
vault server -dev -dev-root-token-id="$VAULT_DEV_ROOT_TOKEN_ID" -dev-listen-address="$VAULT_DEV_LISTEN_ADDRESS" &

# Vault 서버가 시작될 때까지 대기
sleep 2

# Vault 주소 설정
export VAULT_ADDR='http://127.0.0.1:8200'

# Vault 로그인
vault login "$VAULT_DEV_ROOT_TOKEN_ID"

# 시크릿 저장
vault kv put secret/dev \
  jwt.access.secret="9295d3232d3428c866241359d89485431868fa3c0207ab2fef5b2dff1a371ea95a9df0b74588f1d34bb6c2ce0078e39d8b9faf32c36cf97b56c09e8876007416" \
  jwt.refresh.secret="e3545b171440dc2bddb1757e49c8b1470221d243f986a19dec44669d3c0d0f9437b4cf769756b9a2197e605e9b1924bb58faac5f578424be00ea92369fcc521d"

# 컨테이너가 종료되지 않도록 대기
tail -f /dev/null
