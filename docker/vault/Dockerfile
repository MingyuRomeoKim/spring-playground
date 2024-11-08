# 베이스 이미지로 Vault 사용
FROM vault:1.13.3

# 환경 변수 설정
ENV VAULT_ADDR=http://0.0.0.0:8200
ENV VAULT_DEV_ROOT_TOKEN_ID=playground
ENV VAULT_DEV_LISTEN_ADDRESS=0.0.0.0:8200

# 초기화 스크립트 복사
COPY init_vault.sh /init_vault.sh

# 실행 권한 부여
RUN chmod +x /init_vault.sh

# 컨테이너 시작 시 초기화 스크립트 실행
CMD ["/init_vault.sh"]
