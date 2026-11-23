package com.contoso.orders;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Reads database connection settings and the upload directory path from environment variables at
 * application startup. No credentials or connection strings are hardcoded or bundled in source
 * control.
 *
 * <p>Supported environment variables:
 *
 * <ul>
 *   <li>{@code DB_URL} - full JDBC URL. If not set, it is built from {@code DB_HOST}, {@code
 *       DB_PORT}, {@code DB_NAME} and {@code DB_SSL_MODE} (suitable for Azure Database for
 *       MySQL).
 *   <li>{@code DB_HOST} - database host name (e.g. the Azure Database for MySQL flexible server
 *       FQDN). Required when {@code DB_URL} is not set.
 *   <li>{@code DB_PORT} - database port. Defaults to {@code 3306}.
 *   <li>{@code DB_NAME} - database/schema name. Required when {@code DB_URL} is not set.
 *   <li>{@code DB_SSL_MODE} - value for the MySQL {@code sslMode} connection parameter (e.g.
 *       {@code REQUIRED}, {@code VERIFY_CA}, {@code VERIFY_IDENTITY}, {@code DISABLED}). Defaults
 *       to {@code REQUIRED}, matching the SSL enforcement used by Azure Database for MySQL.
 *   <li>{@code DB_USER} - database user name. Required.
 *   <li>{@code DB_PASSWORD} - database password. Required.
 *   <li>{@code DB_DRIVER} - JDBC driver class name. Defaults to {@code com.mysql.cj.jdbc.Driver}.
 *   <li>{@code UPLOAD_DIR} - directory used to store uploaded files. Required.
 * </ul>
 */
public final class DbConfig {

    private static final String DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DEFAULT_PORT = "3306";
    private static final String DEFAULT_SSL_MODE = "REQUIRED";

    /**
     * Resolved configuration values. Populated from environment variables on first use (cached
     * here), or directly via {@link #override(String, String)} for tests.
     */
    private static final Map<String, String> CONFIG = new ConcurrentHashMap<>();

    static {
        // The JDBC driver has a safe default and is registered eagerly at application startup;
        // all other settings (connection target, credentials, upload dir) are read from
        // environment variables lazily on first use so that required variables are only
        // enforced when the corresponding feature is actually exercised.
        String driver = env("DB_DRIVER", DEFAULT_DRIVER);
        CONFIG.put("db.driver", driver);
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DbConfig() { }

    private static String buildUrl() {
        String explicitUrl = System.getenv("DB_URL");
        if (explicitUrl != null && !explicitUrl.isEmpty()) {
            return explicitUrl;
        }
        String host = requireEnv("DB_HOST");
        String port = env("DB_PORT", DEFAULT_PORT);
        String name = requireEnv("DB_NAME");
        String sslMode = env("DB_SSL_MODE", DEFAULT_SSL_MODE);
        return "jdbc:mysql://" + host + ":" + port + "/" + name + "?sslMode=" + sslMode;
    }

    private static String env(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value == null || value.isEmpty()) ? defaultValue : value;
    }

    private static String requireEnv(String key) {
        String value = System.getenv(key);
        if (value == null || value.isEmpty()) {
            throw new IllegalStateException(
                    "Required environment variable '" + key + "' is not set. "
                            + "Configure database and upload directory settings via environment "
                            + "variables (DB_URL or DB_HOST/DB_PORT/DB_NAME/DB_SSL_MODE, DB_USER, "
                            + "DB_PASSWORD, DB_DRIVER, UPLOAD_DIR).");
        }
        return value;
    }

    public static Connection open() throws SQLException {
        String url = CONFIG.computeIfAbsent("db.url", k -> buildUrl());
        String user = CONFIG.computeIfAbsent("db.user", k -> requireEnv("DB_USER"));
        String password = CONFIG.computeIfAbsent("db.password", k -> requireEnv("DB_PASSWORD"));
        return DriverManager.getConnection(url, user, password);
    }

    public static String uploadDir() {
        return CONFIG.computeIfAbsent("upload.dir", k -> requireEnv("UPLOAD_DIR"));
    }

    /** Test-only hook to override a configuration value without requiring environment variables. */
    static void override(String key, String value) {
        CONFIG.put(key, value);
    }
}
