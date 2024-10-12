package com.github.mengweijin.flyway;

/**
 * 用于兼容 flyway 的 10 和 9 版本
 * @author mengweijin
 * @since 2024-10-12
 */
public interface ISupportJdbcConnectionFactory {
    /**
     * 用于兼容 flyway 的 10 和 9 版本
     * @author mengweijin
     * @since 2024-10-12
     */
    boolean supportsBatch();
}
