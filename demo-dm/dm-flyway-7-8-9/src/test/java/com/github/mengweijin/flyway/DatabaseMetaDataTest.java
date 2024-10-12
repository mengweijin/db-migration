package com.github.mengweijin.flyway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author mengweijin
 */
public class DatabaseMetaDataTest {

    @Test
    public void getDatabaseProductName() {
        String driver = "dm.jdbc.driver.DmDriver";
        String url = "jdbc:dm://localhost:5236";
        String user = "VTL_TEST";
        String pass = "123456";

        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, user, pass);
            DatabaseMetaData metaData = connection.getMetaData();
            Assertions.assertEquals("DM DBMS", metaData.getDatabaseProductName());
            Assertions.assertEquals(8, metaData.getDatabaseMajorVersion());
            Assertions.assertEquals(1, metaData.getDatabaseMinorVersion());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
