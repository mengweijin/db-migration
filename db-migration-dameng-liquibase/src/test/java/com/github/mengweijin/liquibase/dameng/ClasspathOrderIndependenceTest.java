package com.github.mengweijin.liquibase.dameng;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClasspathOrderIndependenceTest {

    private static final String LIQUIBASE_CORE_JAR_PREFIX = "liquibase-core-";

    @ParameterizedTest(name = "extensionFirst={0}")
    @ValueSource(booleans = {false, true})
    void coreAndExtensionWorkInEitherClasspathOrder(boolean extensionFirst) throws Exception {
        List<URL> classpath = orderedClasspath(extensionFirst);
        try (URLClassLoader loader = new URLClassLoader(classpath.toArray(URL[]::new),
                ClassLoader.getPlatformClassLoader())) {
            Thread thread = Thread.currentThread();
            ClassLoader previousContextLoader = thread.getContextClassLoader();
            thread.setContextClassLoader(loader);
            try {
                Class<?> abstractJdbcDatabase = loader.loadClass("liquibase.database.AbstractJdbcDatabase");
                Class<?> databaseConnection = loader.loadClass("liquibase.database.DatabaseConnection");
                Class<?> dmDatabaseClass = loader.loadClass(
                        "com.github.mengweijin.liquibase.dameng.database.DmDatabase");

                assertTrue(isLiquibaseCoreJar(codeSource(abstractJdbcDatabase)));
                assertTrue(codeSource(dmDatabaseClass).endsWith("db-migration-dameng-liquibase/target/classes/"));
                assertThrows(ClassNotFoundException.class,
                        () -> loader.loadClass("liquibase.database.core.DmDatabase"));
                assertFalse(Arrays.stream(abstractJdbcDatabase.getDeclaredMethods())
                        .anyMatch(method -> method.getName().equals("setSuperConnection")));

                Object dmDatabase = dmDatabaseClass.getConstructor().newInstance();
                Object offlineConnection = loader.loadClass("liquibase.database.OfflineConnection")
                        .getConstructor().newInstance();
                Method setConnection = dmDatabaseClass.getMethod("setConnection", databaseConnection);
                setConnection.invoke(dmDatabase, offlineConnection);
                assertEquals("dm", dmDatabaseClass.getMethod("getShortName").invoke(dmDatabase));

                assertServicePresent(loader, "liquibase.database.Database", dmDatabaseClass.getName());
                assertServicePresent(loader, "liquibase.snapshot.SnapshotGenerator",
                        "com.github.mengweijin.liquibase.dameng.snapshot.DmColumnSnapshotGenerator");
                assertServicePresent(loader, "liquibase.datatype.LiquibaseDataType",
                        "com.github.mengweijin.liquibase.dameng.datatype.DmBooleanType");
                loader.loadClass("liquibase.snapshot.SnapshotGeneratorFactory").getMethod("getInstance").invoke(null);
                assertDmDataTypeSelected(loader, dmDatabase, "boolean",
                        "com.github.mengweijin.liquibase.dameng.datatype.DmBooleanType");
            } finally {
                thread.setContextClassLoader(previousContextLoader);
            }
        }
    }

    private List<URL> orderedClasspath(boolean extensionFirst) throws Exception {
        List<URL> remaining = new ArrayList<>();
        URL extension = null;
        URL core = null;
        for (String entry : System.getProperty("java.class.path").split(System.getProperty("path.separator"))) {
            URL url = Path.of(entry).toAbsolutePath().normalize().toUri().toURL();
            String path = url.getPath();
            if (path.endsWith("db-migration-dameng-liquibase/target/classes/")) {
                extension = url;
            } else if (isLiquibaseCoreJar(path)) {
                core = url;
            } else {
                remaining.add(url);
            }
        }
        if (extension == null || core == null) {
            throw new IllegalStateException("Could not locate extension and liquibase-core on the test classpath");
        }
        List<URL> ordered = new ArrayList<>();
        ordered.add(extensionFirst ? extension : core);
        ordered.add(extensionFirst ? core : extension);
        ordered.addAll(remaining);
        return ordered;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void assertServicePresent(ClassLoader loader, String serviceName, String implementationName)
            throws Exception {
        Class service = loader.loadClass(serviceName);
        boolean found = ServiceLoader.load(service, loader).stream()
                .map(provider -> ((ServiceLoader.Provider<?>) provider).type().getName())
                .anyMatch(implementationName::equals);
        assertTrue(found, () -> implementationName + " was not registered for " + serviceName);
    }

    private String codeSource(Class<?> type) throws Exception {
        URI uri = type.getProtectionDomain().getCodeSource().getLocation().toURI();
        return uri.toString();
    }

    private boolean isLiquibaseCoreJar(String path) {
        int fileNameStart = path.lastIndexOf('/') + 1;
        String fileName = path.substring(fileNameStart);
        return fileName.startsWith(LIQUIBASE_CORE_JAR_PREFIX) && fileName.endsWith(".jar");
    }

    private void assertDmDataTypeSelected(ClassLoader loader, Object dmDatabase, String description,
                                          String expectedType) throws Exception {
        Class<?> factoryClass = loader.loadClass("liquibase.datatype.DataTypeFactory");
        Object factory = factoryClass.getMethod("getInstance").invoke(null);
        Object type = factoryClass.getMethod("fromDescription", String.class,
                        loader.loadClass("liquibase.database.Database"))
                .invoke(factory, description, dmDatabase);
        assertEquals(expectedType, type.getClass().getName());
    }
}
