package com.github.mengweijin.flyway.database;

import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.extensibility.PluginMetadata;
import org.flywaydb.core.internal.util.FileUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author mengweijin
 * @since 2024/9/28
 */
public class GbaseDatabaseExtension implements PluginMetadata {
    public String getDescription() {
        return "Gbase database support " + readVersion() + " by Redgate";
    }

    private static String readVersion() {
        try {
            return FileUtils.copyToString(
                    GbaseDatabaseExtension.class.getClassLoader().getResourceAsStream("com/github/mengweijin/flyway/database/version.txt"),
                    StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new FlywayException("Unable to read extension version: " + e.getMessage(), e);
        }
    }
}
