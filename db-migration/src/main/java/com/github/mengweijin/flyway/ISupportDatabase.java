package com.github.mengweijin.flyway;

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2023</p>
 * <p>Company:      https://github.com/godfather1103</p>
 * 提供一个接口用于兼容flyway的8跟9版本
 *
 * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * @version 1.0
 * @date 创建时间：2023/11/29 14:21
 * @since 1.0
 */
public interface ISupportDatabase {

    /**
     * 用于兼容flyway的8跟9版本<BR>
     *
     * @return 结果
     * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
     * @since 创建时间：2023/11/29 14:22
     */
    boolean supportsChangingCurrentSchema();

    /**
     * 用于兼容 flyway 的 10 和 9 版本
     * @author mengweijin
     * @since 2024-10-12
     */
    void ensureSupported();

}
