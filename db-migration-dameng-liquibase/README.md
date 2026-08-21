# Dameng Liquibase extension

This module is a standalone Liquibase extension. It depends only on `liquibase-core` and does not package or
override Liquibase core classes.

## Tests

Unit tests include service discovery, DM-only datatype/snapshot-generator priority checks, and the same linkage
checks with both `liquibase-core -> extension` and `extension -> liquibase-core` URL classpath orders.

Run the DM database regression suite with a disposable schema:

```shell
export DM_JDBC_URL='jdbc:dm://localhost:5236'
export DM_JDBC_USERNAME='LIQUIBASE_TEST'
export DM_JDBC_PASSWORD='change-me'
mvn -pl db-migration-dameng-liquibase -am -Pdm-integration integration-test
```

The integration test covers update, status, rollback, re-update, lock acquisition/release, snapshot, and diff.
