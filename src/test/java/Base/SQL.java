package Base;

import Base.BaseTest;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import io.qameta.allure.Step;
import org.apache.hc.core5.util.TextUtils;
import org.junit.jupiter.api.Assertions;

import java.sql.*;

public class SQL extends BaseTest {
    public static Connection connection;
    public static Statement statement;
    public static Session session;
    public ResultSet resultSet;
    public String value;
    public static String URL;
    public static String USERNAME;
    public static String PASSWORD;

    public static Boolean PrintSQL = true;

    public static String sshHost;
    public static String sshUser;
    public static String sshPassword;
    public static Integer sshPort;

    public static String dbHost;
    public static Integer dbPort;
    public static String dbName;
    public static String dbUser;
    public static String dbPassword;

    JSch jsch = new JSch();

    public void ReplacementConnection() {
        /** Тм-Тест */
        if (KingNumber == 1) {
            URL = "jdbc:postgresql://192.168.2.38:5432/telemed_test";
            USERNAME = "postgres";
            PASSWORD = "LBGScJFq";
        }
        /** Тм-Дев */
        if (KingNumber == 2) {
            URL = "jdbc:postgresql://192.168.2.38:5432/postgres";
            USERNAME = "postgres";
            PASSWORD = "LBGScJFq";
        }
        /** Тест Севы */
        if (KingNumber == 3) {
            sshHost = "192.168.2.86";// SSH хост
            sshUser = "devadmin";// Имя пользователя SSH
            sshPassword = "!Q@W3e4r";// Пароль SSH
            sshPort = 34079;// Порт SSH

            dbHost = "192.168.239.34";// Хост базы данных (localhost, если используется туннелирование)
            dbPort = 6432;// Порт базы данных
            dbName = "vimis";// Имя базы данных
            dbUser = "uvimis";// Имя пользователя базы данных
            dbPassword = "Q_sWJBcXpG97azVDeym6uF";// Пароль базы данных
        }
        /** Тест ХМАО */
        if (KingNumber == 4) {
            URL = "jdbc:postgresql://192.168.2.21:34031/postgres";
            USERNAME = "postgres";
            PASSWORD = "Qu28aT2";
        }
        /** Тест ЧАО */
        if (KingNumber == 5) {
//            URL = "jdbc:postgresql://192.168.137.77:6432/dev";
//            USERNAME = "postgres";
//            PASSWORD = "Qu28aT2";
//            User = "user";
//            Host = "192.168.2.7";
//            Port = 32052;
//            Password = "57NheFyjHfpLfh";
        }
        /** Аудит Тм-Дев */
        if (KingNumber == 6) {
            URL = "jdbc:postgresql://192.168.2.38:5432/audit-dev";
            USERNAME = "postgres";
            PASSWORD = "LBGScJFq";
        }
        /** Аудит Тест ХМАО */
        if (KingNumber == 7) {
            URL = "jdbc:postgresql://192.168.2.21:34031/audit";
            USERNAME = "postgres";
            PASSWORD = "Qu28aT2";
        }
        /** Аудит Тм -тест */
        if (KingNumber == 8) {
            URL = "jdbc:postgresql://192.168.2.38:5432/audit";
            USERNAME = "postgres";
            PASSWORD = "LBGScJFq";
        }
        /** Тест Алтая */
        if (KingNumber == 9) {
            sshHost = "192.168.2.37";// SSH хост
            sshUser = "user";// Имя пользователя SSH
            sshPassword = "fFDVG4tfwEFf";// Пароль SSH
            sshPort = 36012;// Порт SSH

            dbHost = "172.20.7.234";// Хост базы данных (localhost, если используется туннелирование)
            dbPort = 6432;// Порт базы данных
            dbName = "pstgrs";// Имя базы данных
            dbUser = "postgres";// Имя пользователя базы данных
            dbPassword = "sTJ7hgPFTYVcSQPm";// Пароль базы данных
        }
        /** Дев Севы Новый */
        if (KingNumber == 10) {
            sshHost = "192.168.2.86";// SSH хост
            sshUser = "miac\\Komtek-IGromov";// Имя пользователя SSH
            sshPassword = "!Q@W3e4r";// Пароль SSH
            sshPort = 21001;// Порт SSH

            dbHost = "10.92.172.67";// Хост базы данных (localhost, если используется туннелирование)
            dbPort = 5432;// Порт базы данных
            dbName = "pstgrs";// Имя базы данных
            dbUser = "pstgrs";// Имя пользователя базы данных
            dbPassword = "h6NHt5w7kzyFas_Yre2Ev3";// Пароль базы данных
        }
        /** Тест Адыгея */
        if (KingNumber == 11) {
//            sshHost = "192.168.2.15";// SSH хост
//            sshUser = "komtek";// Имя пользователя SSH
//            sshPassword = "46NfqExfAjhCjn";// Пароль SSH
//            sshPort = 30;// Порт SSH

//            dbHost = "192.168.100.11";// Хост базы данных (localhost, если используется туннелирование)
//            dbPort = 6432;// Порт базы данных
//            dbName = "pstgrs";// Имя базы данных
//            dbUser = "vimis";// Имя пользователя базы данных
//            dbPassword = "8nUIDaqBD2Rs6JUq";// Пароль базы данных

            URL = "jdbc:postgresql://192.168.2.15:50103/pstgrs";
            USERNAME = "vimis_read";
            PASSWORD = "Y22Zfki8OsUQnr9z";
        }
        /** Тест ЧАО Новый */
        if (KingNumber == 12) {
            sshHost = "192.168.2.7";// SSH хост
            sshPort = 32127;// Порт SSH
            sshUser = "komtek";// Имя пользователя SSH
            sshPassword = "22Hti<f,CghCn.";// Пароль SSH

            dbHost = "172.87.1.21";// Хост базы данных (localhost, если используется туннелирование)
            dbPort = 6432;// Порт базы данных
            dbName = "vimis";// Имя базы данных
            dbUser = "vimis";// Имя пользователя базы данных
            dbPassword = "TaKPrhkWN5BLuQc_HFpXRq";// Пароль базы данных
        }
        /** Тест Запорожская область */
        if (KingNumber == 13) {
            URL = "jdbc:postgresql://192.168.2.15:60000/vimis";
            USERNAME = "vimis";
            PASSWORD = "i5nRWk11W3AK5FYIHo_oH2y4LppRl1T7r";
        }
    }

    /**
     * Метод для подключения к БД
     * SQL - запрос к БД
     */
    @Step("Запрос к БД")
    public void StartConnection(String SQL) throws SQLException {
        if (PrintSQL) {
            System.out.println("\n" + SQL);
        }
        try {
            if (!TextUtils.isEmpty(sshHost)) {
                // Инициализация JSch
                JSch jsch = new JSch();

                // Создание SSH-сессии
                session = jsch.getSession(sshUser, sshHost, sshPort);
                session.setPassword(sshPassword);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();

                // Настройка перенаправления портов (localport:remotehost:remoteport)
                int localPort = dbPort; // Локальный порт для туннелирования
                session.setPortForwardingL(localPort, dbHost, dbPort);

                // Подключение к базе данных через туннель
                String dbUrl = "jdbc:postgresql://localhost:" + localPort + "/" + dbName;
                connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(SQL);

                connection.close();
                // Отключение SSH-сессии
                session.disconnect();
            } else {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                statement = connection.createStatement();
                resultSet = statement.executeQuery(SQL);
                connection.close();
            }
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод для подключения к БД с использованием SSH
     * SQL - запрос к БД
     */
    public void StartConnectionSSH(String SQL) throws SQLException, JSchException {
        // Инициализация JSch
        JSch jsch = new JSch();

        // Создание SSH-сессии
        session = jsch.getSession(sshUser, sshHost, sshPort);
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // Настройка перенаправления портов (localport:remotehost:remoteport)
        int localPort = dbPort; // Локальный порт для туннелирования
        session.setPortForwardingL(localPort, dbHost, dbPort);

        // Подключение к базе данных через туннель
        String dbUrl = "jdbc:postgresql://localhost:" + localPort + "/" + dbName;
        connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        statement = connection.createStatement();
        resultSet = statement.executeQuery(SQL);

        connection.close();
        // Отключение SSH-сессии
        session.disconnect();
    }

    /**
     * Метод для изменений данных в БД Тм-тест
     * SQL - запрос к БД
     */
    @Step("Изменение данные в БД")
    public void UpdateConnection(String SQL) throws SQLException {
        if (PrintSQL) {
            System.out.println("\n" + SQL);
        }
        try {
            if (!TextUtils.isEmpty(sshHost)) {
                // Инициализация JSch
                JSch jsch = new JSch();

                // Создание SSH-сессии
                session = jsch.getSession(sshUser, sshHost, sshPort);
                session.setPassword(sshPassword);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();

                // Настройка перенаправления портов (localport:remotehost:remoteport)
                int localPort = dbPort; // Локальный порт для туннелирования
                session.setPortForwardingL(localPort, dbHost, dbPort);

                // Подключение к базе данных через туннель
                String dbUrl = "jdbc:postgresql://localhost:" + localPort + "/" + dbName;
                connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

                statement = connection.createStatement();
                statement.executeUpdate(SQL);

                connection.close();
                // Отключение SSH-сессии
                session.disconnect();
            } else {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                statement = connection.createStatement();
                statement.executeUpdate(SQL);
                connection.close();
            }
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод, который проверяет, есть ли по данному запросу, какая либо запись в БД
     * SQL - запрос к БД
     */
    public void SQL(String SQL) throws SQLException {
        System.out.println("\n" + SQL);
        try {
            if (!TextUtils.isEmpty(sshHost)) {
                // Инициализация JSch
                JSch jsch = new JSch();

                // Создание SSH-сессии

                session = jsch.getSession(sshUser, sshHost, sshPort);

                session.setPassword(sshPassword);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();

                // Настройка перенаправления портов (localport:remotehost:remoteport)
                int localPort = dbPort; // Локальный порт для туннелирования
                session.setPortForwardingL(localPort, dbHost, dbPort);

                // Подключение к базе данных через туннель
                String dbUrl = "jdbc:postgresql://localhost:" + localPort + "/" + dbName;
                connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(SQL);

                connection.close();
                // Отключение SSH-сессии
                session.disconnect();

            } else {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                statement = connection.createStatement();
                resultSet = statement.executeQuery(SQL);
                connection.close();
            }
            while (resultSet.next()) {
                value = resultSet.getString("count");
                Assertions.assertNotEquals("0", value, "По данному запросу должны быть записи");
            }
            connection.close();
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод, который проверяет не добавилась запись в БД
     * SQL - запрос к БД
     */
    public void NotSQL(String SQL) throws SQLException {
        System.out.println("\n" + SQL);
        try {
            if (!TextUtils.isEmpty(sshHost)) {

                // Инициализация JSch
                JSch jsch = new JSch();

                // Создание SSH-сессии
                session = jsch.getSession(sshUser, sshHost, sshPort);
                session.setPassword(sshPassword);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();

                // Настройка перенаправления портов (localport:remotehost:remoteport)
                int localPort = dbPort; // Локальный порт для туннелирования
                session.setPortForwardingL(localPort, dbHost, dbPort);

                // Подключение к базе данных через туннель
                String dbUrl = "jdbc:postgresql://localhost:" + localPort + "/" + dbName;
                connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

                statement = connection.createStatement();
                resultSet = statement.executeQuery(SQL);

                connection.close();
                // Отключение SSH-сессии
                session.disconnect();

            } else {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                statement = connection.createStatement();
                resultSet = statement.executeQuery(SQL);
                connection.close();
            }
            while (resultSet.next()) {
                value = resultSet.getString("count");
                System.out.println(value + " Записей");
                Assertions.assertEquals("0", value);
            }
            connection.close();
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }
}
