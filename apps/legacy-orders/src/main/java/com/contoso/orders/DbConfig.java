package com.contoso.orders;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/** 클래스패스의 db.properties에서 접속 정보를 읽습니다. (환경 변수나 관리 ID 미지원) */
public final class DbConfig {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = DbConfig.class.getResourceAsStream("/db.properties")) {
            PROPS.load(in);
            Class.forName(PROPS.getProperty("db.driver"));
        } catch (IOException | ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DbConfig() { }

    public static Connection open() throws SQLException {
        return DriverManager.getConnection(
                PROPS.getProperty("db.url"),
                PROPS.getProperty("db.user"),
                PROPS.getProperty("db.password"));
    }

    public static String uploadDir() {
        return PROPS.getProperty("upload.dir");
    }

    static void override(String key, String value) {
        PROPS.setProperty(key, value);
    }
}
