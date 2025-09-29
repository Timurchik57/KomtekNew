package Base;

import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationOutgoingUnfinished;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import api.Before.Authorization;
import com.jayway.jsonpath.DocumentContext;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.response.Response;
import org.apache.hc.core5.util.TextUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.opentest4j.AssertionFailedError;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static api.Access_Notification.Access_3534_Type18_Test.OneNotification;
import static io.restassured.RestAssured.given;

abstract public class BaseAPI extends BaseTest {
    public XML xml;
    public static SQL sql;
    public RequestSpecification requestSpecification;
    public Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    public Properties prop = new Properties();
    public Date date;
    public String Date;
    public JsonPath Response;
    public Response Responses;

    public DocumentContext jsonContext;

    public static String HostStart;
    public static String HostAddress;
    public static String HostAddressWeb;
    public static String WebVimis;
    public static String ApiVimis;
    public static String URLKREMD;
    public static String UrlCami;
    public static String MO;
    public static String NameMO;
    public static String SystemId;
    public static String Password;
    public static String Position;
    public static String Speciality;
    public static String CheckSum;
    public static String Signatures;
    public static String CheckSumSign;
    public static String PatientGuid;
    public static String Token;
    public static String BodyAuthorisation;
    public static String Departmen;
    public static String Snils;
    public static long ID;
    public static long SetID;
    public static long VN;
    public Properties props;
    public static UUID uuuid;
    public static String BODY;
    public static String TEXT;
    public Integer CountCollback = 1;
    public static String AddressNotification;
    public static String Notification;
    public static Set<Cookie> cookies;

    public static Integer RRPGlobal;
    public static String UrlRRPWeb;
    public static String LoginRRPWeb;
    public static String PasswordRRPWeb;
    public static String UrlRRPApi;
    public static String LoginRRPApi;
    public static String PasswordRRPApi;

    public static Calendar calendar;
    public static Integer Year;
    public static Integer Month;
    public static Integer Day;

    public static String YearSql;
    public static String MonthSql;
    public static String DaySql;

    public static String SerialNumberXml;

    public static String AddressSignature;
    public static String HostSignatureSoap;
    public static String SoapSystemId;
    public static String SignatureBody;
    public static String SignatureValue;
    public static String SignedInfo;
    public static String BinSecurityToken;
    public static String HostSoap;
    public static String local_uidSoap;
    public static String IdSoap;

    public static String smsBase;
    public static String logsBase;
    public static String remdBase;
    public static String attachmentsBase;
    public static String infoBase;
    public static String rrnBase;
    public static String common_infoBase;

    public void ReplacementConnectionAPI () {

        AddressSignature = "http://192.168.2.126:8082/sign/message";
        SerialNumberXml = "058283404B5A717BAC5B8700F3B1DE07";
        SoapSystemId = "6fc40ee5-32cb-5d85-f782-c6d0282134a5";

        /** Тм-Тест */
        if (KingNumber == 1) {
            HostAddress = "https://api.tm-test.pkzdrav.ru";
            HostAddressWeb = "https://tm-test.pkzdrav.ru";
            WebVimis = "http://192.168.2.126:1109";
            ApiVimis = "http://192.168.2.126:1108";
            URLKREMD = ApiVimis + "/api/rremd";
            UrlCami = "http://192.168.2.126:10234/Hl7Emulator";
            MO = "1.2.643.5.1.13.13.12.2.86.8986";
            NameMO = "БУ ХМАО-Югры \"Белоярская районная больница\"";
            SystemId = "21";
            Password = "123";
            Position = "4.5";
            Speciality = "5.3";
            PatientGuid = "4743e15e-488a-44c6-af50-dff0778dd01a";
            Departmen = "1.2.643.5.1.13.13.12.2.86.8986.0.536268";
            Snils = "15979025720";
            BodyAuthorisation = "{\n" +
                    "    \"Username\": \"" + MO + "\",\n" +
                    "    \"SystemId\": " + SystemId + ",\n" +
                    "    \"Password\": \"" + Password + "\"\n" +
                    "}";
            AddressNotification = "http://192.168.2.126:10227/api/callback";
            Notification = "http://192.168.2.126:10227";
            HostSignatureSoap = "http://192.168.2.126:8082/sign/xml";
            HostSoap = "http://192.168.2.126:1153/Vimis";

            RRPGlobal = 1;
            LOGIN_PAGE_URL = HostAddressWeb + "/auth/bysnils?snils=" + PSnils + "&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91";
            HostStart = HostAddressWeb + "/auth";
        }
        /** Тм-Дев */
        if (KingNumber == 2) {
            HostAddress = "https://api.tm-dev.pkzdrav.ru";
            HostAddressWeb = "https://tm-dev.pkzdrav.ru";
            WebVimis = "http://192.168.2.126:1133";
            ApiVimis = "http://192.168.2.126:1131";
            URLKREMD = ApiVimis + "/api/rremd";
            UrlCami = "http://192.168.2.126:10234/Hl7Emulator";
            MO = "1.2.643.5.1.13.13.12.2.86.8986";
            NameMO = "БУ ХМАО-Югры \"Белоярская районная больница\"";
            SystemId = "21";
            Password = "123";
            Position = "4.5";
            Speciality = "5.2";
            PatientGuid = "4743e15e-488a-44c6-af50-dff0778dd01a";
            // PatientGuid = "422651a6-8173-4dc9-a5b4-6ff8e50bfcf0";
            Departmen = "1.2.643.5.1.13.13.12.2.86.8986.0.155665";
            Snils = "15979025720";
            BodyAuthorisation = "{\n" +
                    "    \"Username\": \"" + MO + "\",\n" +
                    "    \"SystemId\": " + SystemId + ",\n" +
                    "    \"Password\": \"" + Password + "\"\n" +
                    "}";
            AddressNotification = "http://192.168.2.126:10227/api/callback";
            Notification = "http://192.168.2.126:10227";
            HostSignatureSoap = "http://192.168.2.126:8082/sign/xml";
            HostSoap = "http://192.168.2.126:1153/Vimis";

            RRPGlobal = 1;
            LOGIN_PAGE_URL = HostAddressWeb + "/auth/bysnils?snils=" + PSnils + "&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91";
            HostStart = HostAddressWeb + "/auth";
        }
        /** Тест Севы */
        if (KingNumber == 3) {
            HostAddressWeb = "http://192.168.2.86:35006";
            HostAddress = "http://192.168.2.86:35007";
            ApiVimis = "http://192.168.2.86:35007";
            MO = "1.2.643.5.1.13.13.12.2.92.9190";
            NameMO = "ГБУЗ Севастополя \"Городская больница№1 им. Н.И. Пирогова\"";
            SystemId = "18";
            Password = "E94C09D2C02B01DEAC425B86F1920C91";
            Position = "4.7";
            Speciality = "5.4";
            PatientGuid = "afc893c3-9a3d-46cc-b263-bcddb723ecbe";
            Departmen = "1.2.643.5.1.13.13.12.2.92.9190.0.203072";
            Snils = "15979025720";
            BodyAuthorisation = "{\n" +
                    "    \"Username\": \"" + MO + "\",\n" +
                    "    \"SystemId\": " + SystemId + ",\n" +
                    "    \"Password\": \"" + Password + "\"\n" +
                    "}";
            HostSignatureSoap = "http://192.168.2.126:8082/sign/xml";
            HostSoap = "http://192.168.2.86:35007/soap";
            LOGIN_PAGE_URL = HostAddressWeb + "/auth/bysnils?snils=" + PSnils + "&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91";
            HostStart = HostAddressWeb + "/auth";
        }
        /** Тест Хмао */
        if (KingNumber == 4) {
            HostAddress = "http://192.168.2.21:34074";
            HostAddressWeb = "https://remotecons-test.miacugra.ru";
            WebVimis = "https://10.86.6.131:1109";
            ApiVimis = "http://192.168.2.21:34154";
            URLKREMD = ApiVimis + "/api/rremd";
            UrlCami = "http://192.168.2.21:34073/Hl7Emulator";
            MO = "1.2.643.5.1.13.13.12.2.86.8986";
            NameMO = "БУ ХМАО-Югры \"Белоярская районная больница\"";
            SystemId = "21";
            Password = "123";
            Position = "4.5";
            Speciality = "5.3";
            PatientGuid = "e3c3323e-1e05-4f59-b733-9abe7dfc88ce";
            Departmen = "1.2.643.5.1.13.13.12.2.86.8986.0.536268";
            Snils = "15979025720";
            BodyAuthorisation = "{\n" +
                    "    \"Username\": \"" + MO + "\",\n" +
                    "    \"SystemId\": " + SystemId + ",\n" +
                    "    \"Password\": \"" + Password + "\"\n" +
                    "}";
            AddressNotification = "http://10.86.6.131:10227/api/callback";
            Notification = "http://192.168.2.21:34329";

            RRPGlobal = 1;
            LOGIN_PAGE_URL = HostAddressWeb + "/auth/bysnils?snils=" + PSnils + "&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91";
            HostStart = HostAddressWeb + "/auth";
        }
        /** Тест ЧАО */
        if (KingNumber == 5) {
            HostAddress = "http://192.168.2.7:31035";
            MO = "1.2.643.5.1.13.13.12.2.87.9016";
            NameMO = "ГБУЗ \"Чукотская окружная больница\" г. Анадырь";
            SystemId = "13";
            Password = "123";
            Position = "4.7";
            Speciality = "5.4";
            PatientGuid = "b7065053-874f-4eba-8650-53874f2ebadf";
            Departmen = "1.2.643.5.1.13.13.12.2.87.9016.0.18101";
            Snils = "12411395113";
            BodyAuthorisation = "{\n" +
                    "    \"Username\": \"" + MO + "\",\n" +
                    "    \"SystemId\": " + SystemId + ",\n" +
                    "    \"Password\": \"" + Password + "\"\n" +
                    "}";
            LOGIN_PAGE_URL = HostAddressWeb + "/auth/bysnils?snils=" + PSnils + "&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91";
            HostStart = HostAddressWeb + "/auth";
            HostSoap = "http://192.168.2.7:31035/soap";
        }
        /** Тест Алтай */
        if (KingNumber == 9) {
            HostAddress = "http://192.168.2.37:37021";
            HostAddressWeb = "http://192.168.2.37:37020";
            WebVimis = "http://172.20.7.234:1109";
            ApiVimis = "http://192.168.2.37:37019";
            MO = "1.2.643.5.1.13.13.12.2.22.1748";
            NameMO = "Краевое государственное бюджетное учреждение здравоохранения \"Алтайский краевой онкологический диспансер\"";
            SystemId = "18";
            Password = "4F13BD1D5711E1F39CB60CAA6B3432C6";
            Position = "4.7";
            Speciality = "5.4";
            PatientGuid = "870e7c46-df28-4ebd-b9b2-ec422726b0c0";
            Departmen = "1.2.643.5.1.13.13.12.2.22.1748.0.105173";
            Snils = "15979025720";
            BodyAuthorisation = "{\n" +
                    "    \"Username\": \"" + MO + "\",\n" +
                    "    \"SystemId\": " + SystemId + ",\n" +
                    "    \"Password\": \"" + Password + "\"\n" +
                    "}";
            AddressNotification = "http://10.86.6.131:10227/api/callback";
            Notification = "https://12345.requestcatcher.com/";

            RRPGlobal = 9;
            LOGIN_PAGE_URL = HostAddressWeb + "/auth/bysnils?snils=" + PSnils + "&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91";
            HostStart = HostAddressWeb + "/auth";
        }
        /** Тест Севы Новый */
        if (KingNumber == 10) {
            HostAddress = "http://localhost:1105";
            HostAddressWeb = "http://localhost:1100";
            WebVimis = "http://localhost:1108";
            ApiVimis = "http://localhost:1109";
            MO = "1.2.643.5.1.13.13.12.2.92.9196";
            NameMO = "ГБУЗ Севастополя \"Городская больница №9\"";
            SystemId = "23";
            Password = "7A14250142871B0E64DA61515AA9B9EB";
            Position = "4.7";
            Speciality = "5.4";
            PatientGuid = "bc08f5b8-4e01-42f8-88f5-b84e01a2f890";
            Departmen = "1.2.643.5.1.13.13.12.2.92.9196.0.201101";
            Snils = "23877246106";
            BodyAuthorisation = "{\n" +
                    "    \"Username\": \"" + MO + "\",\n" +
                    "    \"SystemId\": " + SystemId + ",\n" +
                    "    \"Password\": \"" + Password + "\"\n" +
                    "}";
            HostSignatureSoap = "http://192.168.2.126:8082/sign/xml";
            HostSoap = "http://192.168.2.86:35007/soap";
            LOGIN_PAGE_URL = HostAddressWeb + "/auth/bysnils?snils=" + PSnils + "&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91";
            HostStart = HostAddressWeb + "/auth";
        }
        /** Тест Адыгеи */
        if (KingNumber == 11) {
            HostAddress = "http://192.168.2.15:50095";
            HostAddressWeb = "http://192.168.2.15:50097";
            WebVimis = "http://10.122.10.10:1109";
            ApiVimis = "http://192.168.2.15:50095";
            URLKREMD = ApiVimis + "/api/rremd";
            MO = "1.2.643.5.1.13.13.12.2.86.9006";
            NameMO = "БУ ХМАО-Югры \"Белоярская районная больница\"";
            SystemId = "13";
            Password = "DC2EF23D9CB576F5051AD9AA47F9C21F";
            Position = "4.7";
            Speciality = "5.4";
            PatientGuid = "ff3b9521-d689-4580-990f-9d805337fefc";
            Departmen = "1.2.643.5.1.13.13.12.2.86.9006.0.558637";
            Snils = "13278770077";
            BodyAuthorisation = "{\n" +
                    "    \"Username\": \"" + MO + "\",\n" +
                    "    \"SystemId\": " + SystemId + ",\n" +
                    "    \"Password\": \"" + Password + "\"\n" +
                    "}";
            Notification = "https://12345.requestcatcher.com/";
            HostSignatureSoap = "";
            HostSoap = "";
            LOGIN_PAGE_URL = HostAddressWeb + "/auth/bysnils?snils=" + PSnils + "&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91";
            HostStart = HostAddressWeb + "/auth";
        }
        /** Тест ЧАО Новый */
        if (KingNumber == 12) {
            HostAddress = "http://localhost:1105";
            HostAddressWeb = "http://localhost:1100";
            WebVimis = "";
            ApiVimis = "";
            LoginBase = "alx";
            PasswordBase = "12345678";
            MO = "1.2.643.5.1.13.13.12.2.87.9016";
            NameMO = "ГБУЗ «Чукотская окружная больница» участковая больница п.Угольные копи";
            SystemId = "18";
            Password = "0B55A89AD58ED52286269579AF34053E";
            Position = "4.7";
            Speciality = "5.4";
            PatientGuid = "ad29f722-0b17-49d7-8649-dd93e8040431";
            Departmen = "1.2.643.5.1.13.13.12.2.87.9016.0.18101";
            Snils = "06709796613";
            BodyAuthorisation = "{\n" +
                    "    \"Username\": \"" + MO + "\",\n" +
                    "    \"SystemId\": " + SystemId + ",\n" +
                    "    \"Password\": \"" + Password + "\"\n" +
                    "}";
            HostSignatureSoap = "";
            HostSoap = "";
            LOGIN_PAGE_URL = HostAddressWeb + "/auth/bysnils?snils=" + PSnils + "&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91";
            HostStart = HostAddressWeb + "/auth";
        }
        /** Тест ЗО */
        if (KingNumber == 13) {
            HostAddress = "http://192.168.2.15:12054";
            HostAddressWeb = "http://192.168.2.15:12053";
            WebVimis = "http://10.125.85.10:1109";
            ApiVimis = "http://192.168.2.15:12054";
            LoginBase = "123";
            PasswordBase = "123";
            MO = "1.2.643.5.1.13.13.12.2.86.8986";
            NameMO = "БУ ХМАО-Югры \"Белоярская районная больница\"";
            SystemId = "21";
            Password = "123";
            Position = "4.7";
            Speciality = "5.4";
            PatientGuid = "5582a6fd-7748-47e0-b0db-09b28d344863";
            Departmen = "1.2.643.5.1.13.13.12.2.86.8986.0.536268";
            Snils = "15979025720";
            BodyAuthorisation = "{\n" +
                    "    \"Username\": \"" + MO + "\",\n" +
                    "    \"SystemId\": " + SystemId + ",\n" +
                    "    \"Password\": \"" + Password + "\"\n" +
                    "}";
            AddressNotification = "http://192.168.2.126:10227/api/callback";
            Notification = "http://192.168.2.126:10227";
            HostSignatureSoap = "http://192.168.2.126:8082/sign/xml";
            HostSoap = "http://192.168.2.126:1153/Vimis";
            LOGIN_PAGE_URL = HostAddressWeb + "/auth/bysnils?snils=" + PSnils + "&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91";
            HostStart = HostAddressWeb + "/auth";
            RRPGlobal = 13;
        }
    }

    @BeforeEach
    public void initSpec () throws IOException {
        xml = new XML();
        sql = new SQL();
        date = new Date();

        InputClass();
        InputPropFile("IfCountListner", "api");

        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");
        Date = formatForDateNow.format(date);

        ReplacementConnectionAPI();

        FileInputStream in = new FileInputStream("src/test/resources/ignored/my.properties");
        props = new Properties();
        props.load(in);
        in.close();
    }

    @Step("Универсальный запрос")
    public JsonPath Api (String url, String method, String[] params, String[] header, String body, Integer statusCode, boolean token) throws IOException {
        Authorization authorization = new Authorization();
        InputPropFile("URL", url);

        if (token) {
            try {
                Token = authorization.AuthorizationsAdd("1.2.643.5.1.13.13.12.2.86.8986", "21");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        RestAssured.baseURI = url;
        RequestSpecification Request = RestAssured.given();
        // Собираем параметры заголовков, для ВебВимис есть своя авторизация
        if (header != null | url.contains(WebVimis) & header == null) {
            if (url.contains(WebVimis)) {
                header = new String[]{"Authorization", "Basic TestKey",
                        "MedicalIdMu", "21126"};
            }

            for (int i = 0; i < header.length; i += 2) {
                Request.given().header(header[i], header[i + 1]);
            }
        }
        if (!url.contains(WebVimis)) {
            Request.given().header("Authorization", "Bearer " + Token);
        }

        // Собираем параметры запроса
        if (params != null) {
            for (int i = 0; i < params.length; i += 2) {
                Request.given().param(params[i], params[i + 1]);
            }
        }

        // Условие нужно ли записыывать в консоль тело запроса
        if (!url.contains("/api/authenticate") &&
                !url.contains("/IEMKRegionalService/services/patient/search/") &&
                !url.contains("/api/smd") &&
                !url.contains("CheckSum/CalculateCheckSum") &&
                !url.contains("sign/message") &&
                !url.contains(HostSignatureSoap) &&
                !url.contains(HostSoap)) {
            Request.log().body();
        }

        // Условие нужны ли нам куки
        if (cookies != null) {
            Request.given().cookie(String.valueOf(cookies));
        }

        /** Записываем тело запроса */
        PrintWriter out2 = new PrintWriter("src/test/resources/ignored/JsonBody.json");
        out2.println(body);
        out2.close();

        // Выбираем тип запроса
        if (url.contains(HostSoap)) {
            Request.given().contentType(ContentType.XML);
        } else {
            Request.given().contentType(ContentType.JSON);
        }

        Responses = Request
                .log().uri()
                .when()
                .body(body)
                .request(method)
                .then()
                .extract().response();

        Response = Responses.jsonPath();

        // Условие нужно ли прописывать тело ответа в консоль
        if (Responses.getStatusCode() != 200 | url.contains("/api/smd")) {
            Responses.prettyPeek();
        }

        // Условие проверяем ли мы статус ответа
        if (statusCode != 0) {
            Request.then().statusCode(statusCode);
        }

        /** Записываем тело ответа */
        if (!ReadPropFile("className").equals("Access_3239Test")) {
            System.out.println("Записываем тело ответа в файл \n");
            PrintWriter out3 = new PrintWriter("src/test/resources/ignored/JsonResponse.json");
            out3.println(Responses.asPrettyString());
            out3.close();
        }

        Allure.addAttachment("Запрос: " + ReadPropFile("URL"),
                new String(Files.readAllBytes(Paths.get("src/test/resources/ignored/JsonBody.json"))));

        Allure.addAttachment("Ответ: " + ReadPropFile("URL"),
                new String(Files.readAllBytes(Paths.get("src/test/resources/ignored/JsonResponse.json"))));

        if (url.contains("/api/direction")) {
            System.out.println("Сохраняем id удалённой консультации");
            AddConsulId();
        }

        return Response;
    }

    @Step("Метод для определения нужного токена в зависимости от контура")
    public static String TokenInit () throws IOException {

        Authorization authorization = new Authorization();

        String TokenTrue = null;
        try {
            TokenTrue = authorization.AuthorizationsAdd("1.2.643.5.1.13.13.12.2.86.8986", "21");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return TokenTrue;
    }

    @Step("Метод для Записи id удалённой консультации")
    public void AddConsulId () throws IOException {

        try {
            sql.StartConnection("Select * from telmed.directions order by id desc limit 1;");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
            }

            InputPropFile("IdDirection", sql.value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Метод для выбора данных РРП")
    public void RRPConnect () {
        try {
            // Тест ХМАО
            if (RRPGlobal == 1) {
                UrlRRPWeb = "http://192.168.2.21:34142";
                LoginRRPWeb = "11111111111";
                PasswordRRPWeb = "111";
                UrlRRPApi = "http://192.168.2.21:34142";
                LoginRRPApi = "vimis";
                PasswordRRPApi = "ZIiW6O";
            }

            // Пре прод ХМАО
            if (RRPGlobal == 2) {
                // connectAndSendRequest("miac", "192.168.2.21", 34006, "cen0$ADm!","8050", "10.86.6.187", 8050);
                UrlRRPWeb = "http://192.168.2.21:34176";
                LoginRRPWeb = "16449201366";
                PasswordRRPWeb = "vj-Qj0w";
                UrlRRPApi = "http://192.168.2.21:34176";
                LoginRRPApi = "vimis";
                PasswordRRPApi = "F0vhkwo2JlPQ";
            }

            // Тест Алтая
            if (RRPGlobal == 9) {
                UrlRRPWeb = "http://192.168.2.37:37024";
                LoginRRPWeb = "hlf";
                PasswordRRPWeb = "123";
                UrlRRPApi = "http://192.168.2.37:37024";
                LoginRRPApi = "1.2.643.5.1.13.13.12.2.22.9263";
                PasswordRRPApi = "9263";
            }

            // Тест Адыгея
            if (RRPGlobal == 11) {
                UrlRRPWeb = "http://192.168.2.15:51101";
                LoginRRPWeb = "13630723032";
                PasswordRRPWeb = "YAwicKph";
                UrlRRPApi = "http://192.168.2.15:51101";
                LoginRRPApi = "13630723032";
                PasswordRRPApi = "YAwicKph";
            }

            // Тест ЗО
            if (RRPGlobal == 13) {
                connectAndSendRequest("komtek", "192.168.2.15", 1001, "71<.lHfpJ,hGj;", "8090", "10.125.85.10", 8090);
                UrlRRPApi = "http://localhost:8090";
                LoginRRPApi = "vimis";
                PasswordRRPApi = "ZIiW6O";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Метод который изменяет снилс в нужный формат")
    public String formatSnils (String Snils) {
        // Из 15979025720 в 159-790-257 20
        // Проверяем, что длина номера соответствует ожидаемой
        if (Snils.length() != 11) {
            throw new IllegalArgumentException("Снилс должен содержать 11 цифр.");
        }

        // Форматируем номер
        String part1 = Snils.substring(0, 3); // 159
        String part2 = Snils.substring(3, 6); // 790
        String part3 = Snils.substring(6, 9); // 257
        String part4 = Snils.substring(9);    // 20

        return part1 + "-" + part2 + "-" + part3 + " " + part4;
    }

    @Step("Метод который изменяет снилс в нужный формат")
    public String formatSnils_ (String Snils) {
        // Из 159-790-257 20 в 15979025720

        // Форматируем номер
        String part1 = Snils.substring(0, 3); // 159
        String part2 = Snils.substring(4, 7); // 790
        String part3 = Snils.substring(8, 11); // 257
        String part4 = Snils.substring(12);    // 20

        return part1 + part2 + part3 + part4;
    }

    @Step("Метод для проверки настройки в vault")
    public JsonPath GetSettings () throws IOException {

        Token = TokenInit();
        JsonPath response = given()
                .filter(new AllureRestAssured())
                .header("Authorization", "Bearer " + Token)
                .contentType(ContentType.JSON)
                .when()
                .get(HostAddressWeb + "/settings")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract().jsonPath();
        return response;
    }

    /**
     Метод для поиска текста в XML
     */
    @Step("Метод поиска текста в XML")
    public static String getXml (String File, String path) {
        String str = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(File);

            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList nl = (NodeList) xpath.evaluate(path, doc, XPathConstants.NODESET);
            for (int i = 0; i < nl.getLength(); i++) {
                System.out.println(nl.item(i).getNodeValue());
                str = nl.item(i).getNodeValue();
            }
        } catch (Exception e) {
            throw new IllegalStateException("Ошибочка - " + e.getMessage());
        }
        return str;
    }

    /**
     Метод для замены текста в XML
     */
    @Step("Метод замены текста в XML")
    public static void setXml (String File, String path, String replace) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(File);

            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList nl = (NodeList) xpath.evaluate(path, doc, XPathConstants.NODESET);
            for (int i = 0; i < nl.getLength(); i++) {
                System.out.println(nl.item(i).getNodeValue());
                nl.item(i).setNodeValue(replace);
            }

            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(new DOMSource(doc), new StreamResult(new File(File)));
        } catch (Exception e) {
            throw new IllegalStateException("Ошибочка - " + e.getMessage());
        }
    }

    @Step("Метод добавления текста в XML")
    public static void addXml (String File, String path, String append) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(File);

            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList nl = (NodeList) xpath.evaluate(path, doc, XPathConstants.NODESET);
            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i);
                if (node.getNodeName().equals(path)) {
                    // Добавляем строку после указанного тега
                    doc.getDocumentElement().getFirstChild().getParentNode().insertBefore(doc.createTextNode(append),
                            doc.getDocumentElement().getFirstChild());

                    // Добавляем строку перед тегом ClinicalDocument
                    Node newNode = doc.createElement("comment");
                    newNode.setTextContent(append);
                    node.insertBefore(newNode, node.getFirstChild());
                }
            }

            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(new DOMSource(doc), new StreamResult(new File(File)));
        } catch (Exception e) {
            throw new IllegalStateException("Ошибочка - " + e.getMessage());
        }
    }

    /**
     Метод для замены значения в property
     @param FileName - путь до файла с переменными окружения, например (src/test/resources/ignored/my.properties)
     @param NameProp - название переменной
     @param Input    - параметр, который хотим указать в переменную
     */
    @Step("Запись в Properties переменной {1} = {2}")
    public static void InputProp (String FileName, String NameProp, String Input) throws IOException {
        FileInputStream in = new FileInputStream(FileName);
        Properties props = new Properties();
        props.load(in);
        in.close();
        FileOutputStream out = new FileOutputStream(FileName);
        props.setProperty(NameProp, Input);
        props.store(out, null);
        out.close();
    }

    /**
     Метод для замены значения в property
     @param NameProp - название переменной
     @param Input    - параметр, который хотим указать в переменную
     */
    @Step("Запись в Properties переменной {0} = {1}")
    public static void InputPropFile (String NameProp, String Input) throws IOException {
        FileInputStream in = new FileInputStream("src/test/resources/ignored/my.properties");
        Properties props = new Properties();
        props.load(in);
        in.close();
        FileOutputStream out = new FileOutputStream("src/test/resources/ignored/my.properties");
        props.setProperty(NameProp, Input);
        props.store(out, null);
        out.close();
    }

    /**
     Метод для чтения переменной из property
     @params FileName - путь до файла с переменными окружения, например (src/test/resources/ignored/my.properties)
     @params NameProp - название переменной, из корой хотим взять значение
     */
    public static String ReadProp (String FileName, String NameProp) throws IOException {
        try {
            FileInputStream in = new FileInputStream(FileName);
            Properties props = new Properties();
            props.load(in);
            in.close();
            String Name = props.getProperty(NameProp);
            return Name;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     Метод для чтения переменной из property
     @params FileName - путь до файла с переменными окружения, например (src/test/resources/ignored/my.properties)
     @params NameProp - название переменной, из корой хотим взять значение
     */
    public static String ReadPropFile (String NameProp) throws IOException {
        try {
            FileInputStream in = new FileInputStream("src/test/resources/ignored/my.properties");
            Properties props = new Properties();
            props.load(in);
            in.close();
            String Name = props.getProperty(NameProp);
            return Name;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Колбэк от ФВИМИС со статусом = {1}, с текстом ошибки = {2}, для таблицы = {3} утсанавливаем request_id, для vmcl = {4} с localUid = {0}")
    public void CollbekVimis (String localUid, String status, String error, String sms,
            Integer vmcl) throws SQLException {
        uuuid = UUID.randomUUID();
        ;
        BODY = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<SOAP-ENV:Envelope\n" +
                "\txmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "\t<SOAP-ENV:Header>\n" +
                "\t\t<To\n" +
                "\t\t\txmlns=\"http://www.w3.org/2005/08/addressing\" wsu:Id=\"id-to\"\n" +
                "\t\t\txmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">https://ips-test.rosminzdrav.ru/b6789b2b47581\n" +
                "\t\t</To>\n" +
                "\t\t<RelatesTo\n" +
                "\t\t\txmlns=\"http://www.w3.org/2005/08/addressing\">" + uuuid + "\n" +
                "\t\t</RelatesTo>\n" +
                "\t\t<Action\n" +
                "\t\t\txmlns=\"http://www.w3.org/2005/08/addressing\" wsu:Id=\"id-action\"\n" +
                "\t\t\txmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">sendResult\n" +
                "\t\t</Action>\n" +
                "\t\t<MessageID\n" +
                "\t\t\txmlns=\"http://www.w3.org/2005/08/addressing\" wsu:Id=\"id-messageid\"\n" +
                "\t\t\txmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">9dd49f16-3aa2-45e7-b786-1333f341fbd2\n" +
                "\t\t</MessageID>\n" +
                "\t\t<ReplyTo\n" +
                "\t\t\txmlns=\"http://www.w3.org/2005/08/addressing\" wsu:Id=\"id-replyto\"\n" +
                "\t\t\txmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
                "\t\t\t<Address>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</Address>\n" +
                "\t\t</ReplyTo>\n" +
                "\t\t<transportHeader\n" +
                "\t\t\txmlns=\"http://egisz.rosminzdrav.ru\">\n" +
                "\t\t\t<authInfo>\n" +
                "\t\t\t\t<clientEntityId>e5daa377-b705-64e2-52c4-3ddebea35d7b</clientEntityId>\n" +
                "\t\t\t</authInfo>\n" +
                "\t\t</transportHeader>\n" +
                "\t\t<wsse:Security\n" +
                "\t\t\txmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">\n" +
                "\t\t\t<wsse:BinarySecurityToken wsu:Id=\"id-x509\" EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\"\n" +
                "\t\t\t\txmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">MIIDazCCAlOgAwIBAgIUEch0BCakzmHyN1a7EoVrgAlaA5kwDQYJKoZIhvcNAQELBQAwRTELMAkGA1UEBhMCQVUxEzARBgNVBAgMClNvbWUtU3RhdGUxITAfBgNVBAoMGEludGVybmV0IFdpZGdpdHMgUHR5IEx0ZDAeFw0yMDA0MTAxODQ4NTlaFw0yMTA0MTAxODQ4NTlaMEUxCzAJBgNVBAYTAkFVMRMwEQYDVQQIDApTb21lLVN0YXRlMSEwHwYDVQQKDBhJbnRlcm5ldCBXaWRnaXRzIFB0eSBMdGQwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCrJmNObcyjysnS5dV/Hvp6RzHZyoN+n4fxX0QhiMMU4tBPh1zjaMDSe8Cw4i+3B91P33I2vyI0vfvX8EqWumDiDdV0eWc9h+3EeaG9uozqfpMjY/gnyPntwReTqsWZjPy2yyZgRN+IHdvpCfuC+tv8G0H9/UK31RbIUI7P1zeMCZ68v9LVgbG7r5oa0QZId4p/LQ4xPtjvuWiObEwFAUfW6b/vbDfGXSZ4Tq63UwyD9DzXZcs3f7w5EjHmEU22LU8pEzRqjsv/VraQd7T2OrYKbnOP4EAzZIQwuk3OcIVJFtNBjj0+F5gJ+BOmCY0UYcH4oaGjVA5ez147E46oM5AhAgMBAAGjUzBRMB0GA1UdDgQWBBTiuUZkT5Q9j0wfM566jk5aHldSYTAfBgNVHSMEGDAWgBTiuUZkT5Q9j0wfM566jk5aHldSYTAPBgNVHRMBAf8EBTADAQH/MA0GCSqGSIb3DQEBCwUAA4IBAQAM5pQpQMcuE2unZTMLzlfXW5xY3J8mfdgHVZGnBC6fLKfwmlpyoleqnTuNik8X/REe2hN2heO/irHllBAGt+TUB8fdRJpaGbdi6H5FuzrCoG7rozJVZp70B3ij9XuBqkjBmZvac4QcWyXmBYyuWG0TqxKpRwwtgZNr5ZApk2PB79sTlaI7+sLyp/G3ntA4wwQ/4785Sx/8F90/Mrg5fwVi5sQz+Id6ykRV5IZnrE/Z47KdcJ/U4rePGHV01XECn1uR2X98e3npPzR1MX4hoo/f4iZuJ1KWYZ66u1z0edW1E8BFUAsuEe5Z5dytncpodvRsIaICCcXE6do2dYQqpJi4\n" +
                "\t\t\t</wsse:BinarySecurityToken>\n" +
                "\t\t\t<Signature\n" +
                "\t\t\t\txmlns=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
                "\t\t\t\t<SignedInfo>\n" +
                "\t\t\t\t\t<CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />\n" +
                "\t\t\t\t\t<SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256\" />\n" +
                "\t\t\t\t\t<Reference URI=\"#body\">\n" +
                "\t\t\t\t\t\t<Transforms>\n" +
                "\t\t\t\t\t\t\t<Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />\n" +
                "\t\t\t\t\t\t</Transforms>\n" +
                "\t\t\t\t\t\t<DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\" />\n" +
                "\t\t\t\t\t\t<DigestValue>YEOGiS0cujt2sVo0moV4VS7mBVuS0Pl8sQeaHkDrqa0=</DigestValue>\n" +
                "\t\t\t\t\t</Reference>\n" +
                "\t\t\t\t\t<Reference URI=\"#id-messageid\">\n" +
                "\t\t\t\t\t\t<Transforms>\n" +
                "\t\t\t\t\t\t\t<Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />\n" +
                "\t\t\t\t\t\t</Transforms>\n" +
                "\t\t\t\t\t\t<DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\" />\n" +
                "\t\t\t\t\t\t<DigestValue>j8myuFMZfVE+B7gUZ0jf51ymcwKUgV2qwVN0BAw+INA=</DigestValue>\n" +
                "\t\t\t\t\t</Reference>\n" +
                "\t\t\t\t\t<Reference URI=\"#id-replyto\">\n" +
                "\t\t\t\t\t\t<Transforms>\n" +
                "\t\t\t\t\t\t\t<Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />\n" +
                "\t\t\t\t\t\t</Transforms>\n" +
                "\t\t\t\t\t\t<DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\" />\n" +
                "\t\t\t\t\t\t<DigestValue>tpA2zvq1rD20AP46VFS6MUOE9Noe/s3zfGq3XeC9vPc=</DigestValue>\n" +
                "\t\t\t\t\t</Reference>\n" +
                "\t\t\t\t\t<Reference URI=\"#id-to\">\n" +
                "\t\t\t\t\t\t<Transforms>\n" +
                "\t\t\t\t\t\t\t<Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />\n" +
                "\t\t\t\t\t\t</Transforms>\n" +
                "\t\t\t\t\t\t<DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\" />\n" +
                "\t\t\t\t\t\t<DigestValue>5qMzs7IC+8NoRSv4m3hf+XXJW3Wy7BkaQwFsPpL9744=</DigestValue>\n" +
                "\t\t\t\t\t</Reference>\n" +
                "\t\t\t\t\t<Reference URI=\"#id-action\">\n" +
                "\t\t\t\t\t\t<Transforms>\n" +
                "\t\t\t\t\t\t\t<Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />\n" +
                "\t\t\t\t\t\t</Transforms>\n" +
                "\t\t\t\t\t\t<DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\" />\n" +
                "\t\t\t\t\t\t<DigestValue>4x0l+FI91mxd0FRM/0pitJuXGT+JtVgHi1xmLKuJDzk=</DigestValue>\n" +
                "\t\t\t\t\t</Reference>\n" +
                "\t\t\t\t</SignedInfo>\n" +
                "\t\t\t\t<SignatureValue>YZ2C7Q1DbmTgm+9BSd73WEThN/er2wd/TQY/qnlKG3+MuztFAJIUhv0htOJte9imifNMtrqE60eJ4Mq7dq0p0tda0cEpMYG6YyIUMkEwRwkrYejiwyGtORoeK9GwRCV1JiUwqP2tucRDbqrHG8cg6/xCGSaMU4rXTiKIj5x+aKvlsVhPpuSNMs/tf8VJ09XtJgd5kyTlxwPxjxH1PIFUanLAJwSEv/4OoWzmqxl1KdEA0ihgdK8x/V5MBBPvdiSkTYdp6LmsdC1005VnFwG29VwW+0X1wxAvVE0ac6kyVQjYGgJWhYR+VsFHdYBV+drY0+rJWM6q0ehgymiLqoT5yw==</SignatureValue>\n" +
                "\t\t\t\t<KeyInfo>\n" +
                "\t\t\t\t\t<wsse:SecurityTokenReference>\n" +
                "\t\t\t\t\t\t<wsse:Reference ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" URI=\"#id-x509\" />\n" +
                "\t\t\t\t\t</wsse:SecurityTokenReference>\n" +
                "\t\t\t\t</KeyInfo>\n" +
                "\t\t\t</Signature>\n" +
                "\t\t</wsse:Security>\n" +
                "\t</SOAP-ENV:Header>\n" +
                "\t<SOAP-ENV:Body wsu:Id=\"body\"\n" +
                "\t\txmlns:p2=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"\n" +
                "\t\txmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
                "\t\t<sendResult\n" +
                "\t\t\txmlns=\"http://callback.mis.vimis.rosminzdrav.ru/\">\n" +
                "\t\t\t<msg_id\n" +
                "\t\t\t\txmlns=\"\">" + uuuid + "\n" +
                "\t\t\t</msg_id>\n" +
                "\t\t\t<status\n" +
                "\t\t\t\txmlns=\"\">" + status + "</status>\n" +
                "\t\t\t<description\n" +
                "\t\t\t\txmlns=\"\">" + error + "</description>\n" +
                "\t\t</sendResult>\n" +
                "\t</SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>";

        System.out.println(
                "В таблице " + sms + " устанавливаем нужный uuid, который будет в запросе на смену статуса");
        System.out.println(uuuid);
        sql.UpdateConnection(
                "update " + sms + " set request_id = '" + uuuid + "', msg_id = '" + uuuid + "', case_id = '" + uuuid + "' where local_uid = '" + localUid + "';");
        System.out.println("Отправляем запрос на смену статуса");
        String URLCollback = null;
        if (KingNumber == 1) {
            URLCollback = "http://192.168.2.126:1108/onko/callback";
        }
        if (KingNumber == 2) {
            URLCollback = "http://192.168.2.126:1131/onko/callback";
        }
        if (KingNumber == 4 && vmcl == 1) {
            URLCollback = "http://212.96.206.70:1109/CallBackSoapBinding.svc";
        }
        if (KingNumber == 4 && vmcl == 2) {
            URLCollback = "http://212.96.206.70:1109/prevention/callback";
        }
        if (KingNumber == 4 && vmcl == 3) {
            URLCollback = "http://212.96.206.70:1109/akineo/callback.svc";
        }
        if (KingNumber == 4 && vmcl == 4) {
            URLCollback = "http://212.96.206.70:1109/ssz/callbacksoapbinding.svc";
        }
        if (KingNumber == 4 && vmcl == 5) {
            URLCollback = "http://212.96.206.70:1109/CallBackSoapBinding.svc";
        }
        if (KingNumber == 9) {
            URLCollback = "http://192.168.2.37:37019/vimis/callback";
        }
        given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .when()
                .body(BODY)
                .post(URLCollback)
                .then()
                .statusCode(200);
    }

    @Step("Колбэк от КРЭМД со статусом = {1}, с текстом ошибки = {2}, для таблицы = {3} c localUid = {0}")
    public void CollbekKremd (String localUid, String status, String error, String remd) throws InterruptedException {
        System.out.println("Используем колбэк в таблицу " + remd + "");
        String value = String.valueOf(given()
                .filter(new AllureRestAssured())
                .header("Authorization", "Bearer " + Token)
                .log().uri()
                .log().body()
                .contentType(ContentType.JSON)
                .when()
                .body("{\n" +
                        "  \"id\": \"" + localUid + "\",\n" +
                        "  \"emdrId\": \"" + error + "\",\n" +
                        "  \"status\": \"" + status + "\",\n" +
                        "  \"registrationDateTime\": \"2022-08-30\",\n" +
                        "  \"errors\": [\n" +
                        "    {\n" +
                        "      \"code\": \"string\",\n" +
                        "      \"message\": \"" + error + "\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}")
                .post(ApiVimis + "/kremd/callback")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("id"));
        Assertions.assertEquals(value, "" + localUid + "",
                "--------------------Не сменился статус при использовании колбэка КРЭМД");
        Thread.sleep(1000);
    }

    @Step("Метод для ожидания смены статуса фоновым сервисом в таблице {0}")
    public void WaitStatusKremd (String REMD, String localUid) throws SQLException {
        System.out.println("Дожидаемся смены статуса фоновым сервисом");
        String error = null;
        CountCollback = 1;
        sql.PrintSQL = false;
        if (RemdStatus.equals("true")) {
            while (error == null & CountCollback < 120) {
                sql.StartConnection("Select * from " + REMD + " where local_uid = '" + localUid + "';");
                while (sql.resultSet.next()) {
                    error = sql.resultSet.getString("status");
                }
                CountCollback++;
            }
            System.out.println("Столько раз мы запросили статус - " + CountCollback);
            sql.PrintSQL = true;

            if (error == null & CountCollback == 170) {
                /** Выводим ошибку, так как не сработал фоновый сервия РЭМД и не сменил статус у смс */
                try {
                    Assertions.assertEquals(error, CountCollback, "Статус в таблице " + REMD + " не сменился");
                } catch (AssertionFailedError e) {
                    System.out.println("Статус в таблице " + REMD + " не сменился");
                }
            }
        } else {
            // Это альтернатива, чтобы не ждать статус от РЭМД, сами его устанавливаем (долго может приходить до 5 минут)
            sql.PrintSQL = true;
            System.out.println("Устанавливаем статус сами = error");
            if (REMD.equals("vimis.remd_sent_result") | REMD.equals("vimis.certificate_remd_sent_result")) {
                sql.UpdateConnection("update " + REMD + " set status = 'error' where local_uid = '" + localUid + "';");
            } else {
                sql.UpdateConnection("update " + REMD + " set status = 'error', local_uid = '" + localUid + "'\n" +
                        "where sms_id in (select s.id from " + smsBase + " s where s.local_uid = '" + localUid + "');");
            }
        }
        sql.PrintSQL = true;
    }

    @Step("Выбор нужного уведомления для типа = {0}, с transferId = {1} и vmcl = {2}")
    public String NotificationsTrue (Integer Type, String transferId, Integer vmcl) throws InterruptedException {
        //99, 100, 101
        /**
         99 - 2 тип, но региональные документы
         100 - 2 тип, но фоновый сервис
         101 - Для уведомления по записи на слот
         */
        try {
            Thread.sleep(2000);
            WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));

            int count = 0;
            String resultText = null;
            /** Проверка 14 типа уведомлений происходит после всех тестов, поэтому уведомление могжет уйти далеко вниз. Для этого увеличен цикл */
            if (Type != 14) {
                for (int i = 1; i < 20; i++) {

                    TEXT = driver.findElement(By.xpath("//div[@class='text-center']/div[" + i + "]/span")).getText();

                    //Для типа уведомлений 2 с получением статуса от КРЭМД
                    if (TEXT.contains("\"TransferId\":\"" + transferId + "\"") &
                            TEXT.contains("\"EmdId\"") &
                            vmcl == 99 & Type == 2) {
                        System.out.println("Тип 2, vmcl = 99");
                        return TEXT;
                    }
                    if (TEXT.contains("\"TransferId\":\"" + transferId + "\"") &
                            TEXT.contains("ResultDescription") &
                            !TEXT.contains("\"ResultDescription\":\"[{") &
                            vmcl != 99 & Type == 2) {
                        System.out.println("Тип 2, vmcl != 99");
                        return TEXT;
                    }
                    if (ReadPropFile("className").equals("Access_2698_Type2_Test")) {
                        if (TEXT.contains("\"LocalUid\":\"" + transferId + "\"") &
                                TEXT.contains("ResultDescription") &
                                !TEXT.contains("\"ResultDescription\":\"[{") &
                                vmcl != 99 & Type == 2) {
                            System.out.println("Тип 2, vmcl != 99");
                            return TEXT;
                        }
                    }

                    //Для типа уведомлений 2 с получением статуса от РЭМД/ФВИМИС (Фоновый сервис)
                    if (TEXT.contains("\"TransferId\":\"" + transferId + "\"") &
                            TEXT.contains("\"StatusREMD\":0") &
                            TEXT.contains("\"ResultDescription\"") &
                            TEXT.contains("\"DocTypeVersion\":\"\"") &
                            vmcl == 99 & Type == 100) {
                        System.out.println("Тип 2, vmcl = 99");
                        return TEXT;
                    }
                    if (TEXT.contains("\"TransferId\":\"" + transferId + "\"") &
                            TEXT.contains("\"StatusREMD\":null") &
                            !TEXT.contains("\"ResultDescription\":\"[{") &
                            vmcl != 99 & Type == 100) {
                        System.out.println("Тип 2, vmcl != 99");
                        return TEXT;
                    }

                    //Для региональных документов также уведомления тип 2
                    if (TEXT.contains("\"TransferId\":\"" + transferId + "\"") &
                            !TEXT.contains("\"EmdId\"") &
                            TEXT.contains("\"ResultDescription\"") &
                            TEXT.contains("\"StatusREMD\"") &
                            vmcl == 99 & Type == 99) {
                        System.out.println("Тип 20, vmcl = 99");
                        return TEXT;
                    }

                    //Для типа уведомлений 4
                    if (TEXT.contains("\"TransferId\":\"" + transferId + "\"") &
                            TEXT.contains("\"PatientGuid\":\"" + PatientGuid.toLowerCase() + "\"") &
                            !TEXT.contains("\"RequestId\":null") &
                            vmcl != 99 & Type == 4) {
                        System.out.println("Тип 4, vmcl != 99");
                        return TEXT;
                    }

                    //Для типа уведомлений 8
                    if (TEXT.contains("\"TransferId\":\"" + transferId + "\"") &
                            TEXT.contains("\"LocalUid\":") &
                            TEXT.contains("\"DocType\":") &
                            TEXT.contains("\"InitialDocType\":") &
                            TEXT.contains("\"PatientGuid\":\"" + PatientGuid.toLowerCase() + "\"") &
                            Type == 8) {
                        System.out.println("Тип 8, vmcl != 99");
                        return TEXT;
                    }

                    //Для типа уведомлений 9
                    if (TEXT.contains("\"TransferId\":\"" + transferId + "\"") &
                            TEXT.contains("\"PatientGuid\":\"" + PatientGuid.toLowerCase() + "\"") &
                            TEXT.contains("\"RequestId\":null") &
                            vmcl == 99 & Type == 9) {
                        System.out.println("Тип 9, vmcl = 99");
                        return TEXT;
                    }
                    if (TEXT.contains("\"TransferId\":\"" + transferId + "\"") &
                            TEXT.contains("\"PatientGuid\":\"" + PatientGuid.toLowerCase() + "\"") &
                            !TEXT.contains("\"RequestId\":null") &
                            vmcl != 99 & Type == 9) {
                        System.out.println("Тип 9, vmcl != 99");
                        return TEXT;
                    }

                    //Для типа уведомлений 11
                    if (TEXT.contains("\"Author\"") & TEXT.contains("\"TaskHeader\"") &
                            TEXT.contains("\"Task\"") &
                            Type == 11) {
                        System.out.println("Тип 11");
                        return TEXT;
                    }

                    //Для типа уведомлений 12
                    if (TEXT.contains("\"TransferId\":\"" + transferId + "\"") &
                            TEXT.contains("\"LocalUid\":") &
                            TEXT.contains("\"PatientGuid\":\"" + PatientGuid.toLowerCase() + "\"") &
                            TEXT.contains("\"DocType\":") &
                            TEXT.contains("\"SystemId\":") &
                            TEXT.contains("\"MoOid\":") &
                            TEXT.contains("\"DocTypeVersion\":") &
                            Type == 12 & vmcl != 99) {
                        System.out.println("Тип 12, vmcl = " + vmcl + "");
                        return TEXT;
                    }
                    if (TEXT.contains("\"TransferId\":\"" + transferId + "\"") &
                            TEXT.contains("\"LocalUid\":") &
                            TEXT.contains("\"PatientGuid\":\"" + PatientGuid.toLowerCase() + "\"") &
                            TEXT.contains("\"DocType\":") &
                            TEXT.contains("\"SystemId\":") &
                            TEXT.contains("\"MoOid\":") &
                            Type == 12) {
                        System.out.println("Тип 12, vmcl = " + vmcl + "");
                        return TEXT;
                    }

                    //Для типа уведомлений 15
                    if (TEXT.contains("\"Status\":") &
                            TEXT.contains("\"DocType\":") &
                            TEXT.contains("\"LocalUid\":") &
                            TEXT.contains("\"PatientGuid\":") &
                            TEXT.contains("\"UuidPkg\":") &
                            Type == 15) {
                        System.out.println("Тип 15");
                        return TEXT;
                    }

                    //Для типа уведомлений 16
                    if (TEXT.contains("\"LocalUid\":") &
                            TEXT.contains("\"TransferId\":\"" + transferId + "\"") &
                            TEXT.contains("\"PatientGuid\":\"" + PatientGuid.toLowerCase() + "\"") &
                            TEXT.contains("\"DocType\":") &
                            TEXT.contains("\"AttachmentType\":") &
                            TEXT.contains("\"MoOid\":") &
                            TEXT.contains("\"ResultDate\":") &
                            Type == 16 & vmcl == 99) {
                        System.out.println("Тип 16");
                        return TEXT;
                    }
                    if (TEXT.contains("\"LocalUid\":") &
                            TEXT.contains("\"TransferId\":\"" + transferId + "\"") &
                            TEXT.contains("\"PatientGuid\":\"" + PatientGuid.toLowerCase() + "\"") &
                            TEXT.contains("\"DocType\":") &
                            TEXT.contains("\"AttachmentType\":") &
                            TEXT.contains("\"MoOid\":") &
                            TEXT.contains("\"ResultDate\":") &
                            Type == 16 & vmcl != 99) {
                        System.out.println("Тип 16");
                        return TEXT;
                    }

                    //Для типа уведомлений 17
                    if (TEXT.contains("\"PatientGuid\":\"" + PatientGuid.toLowerCase() + "\"") &
                            TEXT.contains("\"LocalUid\":\"" + xml.uuid + "\"") &
                            TEXT.contains("\"CallDate\":") &
                            !TEXT.contains("\"DocType\":") &
                            Type == 17) {
                        resultText = TEXT;
                        System.out.println(resultText);
                        /** Проверка на то, что отображается 1 оповещение */
                        count++;
                        if (count > 1) {
                            throw new IllegalStateException("Найдено больше одного оповещения.");
                        }
                    }

                    //Для типа уведомлений 18 (вместо transferId здесь используется cardUid)
                    // Приходит 2 уведомления, так как установили оповещения по 2 Мо из dpc.smp_addresses, но есть и 1 уведомление по старой заявке 3534
                    if (TEXT.contains("\"CardUid\":\"" + transferId + "\"") &
                            TEXT.contains("\"PatientGuid\":") &
                            TEXT.contains("\"SmpType\":") &
                            Type == 18) {
                        System.out.println("Тип 18");
                        if (!OneNotification) {
                            System.out.println("Проверка на то, что отображается несколько оповещений");
                            resultText = TEXT;
                            System.out.println(resultText);
                            count++;
                        } else {
                            return TEXT;
                        }
                    }

                    //Для типа уведомлений 19 (вместо transferId здесь используется cardUid)
                    if (TEXT.contains("\"CardUid\":\"" + transferId + "\"") &
                            !TEXT.contains("\"PatientGuid\":") &
                            !TEXT.contains("\"SmpType\":") &
                            Type == 19) {
                        System.out.println("Тип 19");

                        /** Проверка на то, что отображается несколько оповещений */
                        resultText = TEXT;
                        System.out.println(resultText);
                        count++;
                    }

                    //Для типа уведомлений 20 - создание консультации
                    if (TEXT.contains("\"DirectionGuid\":") &
                            TEXT.contains("\"RequestMO\":") &
                            TEXT.contains("\"TargetMO\":") &
                            Type == 20) {
                        System.out.println("Тип 20");
                        return TEXT;
                    }

                    //Для типа уведомлений 22 - создание направления (transferId - DirectionGuid)
                    if (TEXT.contains("\"DirectionGuid\":\"" + transferId + "\"") &
                            TEXT.contains("\"RequestMO\":") &
                            TEXT.contains("\"TargetMO\":") &
                            Type == 22) {
                        System.out.println("Тип 22");
                        return TEXT;
                    }

                    //Для уведомления по записи на слот - 101
                    if (TEXT.contains("\"slotId\":") &
                            TEXT.contains("\"patientGuid\":") &
                            TEXT.contains("\"requestMo\":") &
                            TEXT.contains("\"targetMo\":") &
                            TEXT.contains("\"dateFrom\":") &
                            TEXT.contains("\"dateTo\":") &
                            TEXT.contains("\"accessionNumber\":") &
                            TEXT.contains("\"directionId\":") &
                            Type == 101) {
                        System.out.println("Тип 101");
                        return TEXT;
                    }

                    System.out.println(i);
                }
                /** Проверка на то, что отображается оповещение
                 * @param type17 1 уведомление
                 * @param type18 2 уведомления (В зависимости от параметра OneNotification)
                 * @param type19 2 уведомления
                 */
                if (count < 2 & Type == 19 |
                        count > 2 & Type == 19 |
                        count < 2 & Type == 18 & !OneNotification |
                        count > 2 & Type == 18 & !OneNotification) {
                    throw new IllegalStateException("Оповещений должно быть 2");
                }
                if (count == 1 & Type == 17 |
                        count > 1 & Type == 19 |
                        count > 1 & Type == 18 & !OneNotification) {
                    System.out.println("Тип " + Type + "");
                    TEXT = resultText;
                    return TEXT;
                }
            } else {
                for (int i = 1; i < 50; i++) {
                    /** Проверка 14 типа уведомлений происходит после всех тестов, поэтому уведомление могжет уйти далеко вниз. Для этого увеличен цикл */
                    TEXT = driver.findElement(By.xpath("//div[@class='text-center']/div[" + i + "]/span")).getText();

                    //Для типа уведомлений 14
                    if (TEXT.contains("\"RequestId\":") &
                            TEXT.contains("\"CreateDate\":") &
                            TEXT.contains("\"DocType\":") &
                            TEXT.contains("\"LocalUid\":") &
                            TEXT.contains("\"MessageId\":") == false &
                            Type == 14) {
                        System.out.println("Тип 14");
                        return TEXT;
                    }
                }
            }
            return null;
        } catch (IOException e) {
            throw new IllegalStateException("Ошибочка - " + e.getMessage());
        }
    }

    @Step("Метод для создания подписи Soap запроса")
    public void SoapSignatureMethod (String File, String SystemId, Integer vmcl, String doctype, Integer docTypeVersion) throws IOException, InterruptedException {
        if (KingNumber == 2 | KingNumber == 3) {

            xml.XmlStart(File, Departmen, MO, PatientGuid, NameMO, Snils, 1);

            System.out.println("Отправляем запрос на подпись для Soap");
            SignatureBody = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:a=\"http://www.w3.org/2005/08/addressing\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
                    "\t<s:Header>\n" +
                    "\t\t<a:Action wsu:Id=\"id-action\">sendDocument</a:Action>\n" +
                    "\t\t<transportHeader xmlns=\"http://egisz.rosminzdrav.ru\">\n" +
                    "\t\t\t<authInfo>\n" +
                    "\t\t\t\t<clientEntityId>" + SystemId + "</clientEntityId>\n" +
                    "\t\t\t</authInfo>\n" +
                    "\t\t</transportHeader>\n" +
                    "\t\t<a:MessageID>43afe69d-b21a-428b-a624-f113f95ec743</a:MessageID>\n" +
                    "\t\t<a:ReplyTo wsu:Id=\"id-replyto\">\n" +
                    "\t\t\t<a:Address>https://ips.rosminzdrav.ru/79f5771afb36b</a:Address>\n" +
                    "\t\t</a:ReplyTo>\n" +
                    "\t\t<a:FaultTo>\n" +
                    "\t\t\t<a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address>\n" +
                    "\t\t</a:FaultTo>\n" +
                    "\t\t<a:To>https://ips.rosminzdrav.ru/8b02e1e4e03c7</a:To>\n" +
                    "\t</s:Header>\n" +
                    "\t<s:Body xmlns:d2p1=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" d2p1:Id=\"body\">\n" +
                    "\t\t<sendDocument xmlns=\"http://vimis.rosminzdrav.ru/\">\n" +
                    "\t\t\t<vmcl xmlns=\"\">" + vmcl + "</vmcl>\n" +
                    "\t\t\t<vmcl_array xmlns=\"\">" + vmcl + "</vmcl_array>\n" +
                    "\t\t\t<docType xmlns=\"\">" + doctype + "</docType>\n" +
                    "\t\t\t<docTypeVersion xmlns=\"\">" + docTypeVersion + "</docTypeVersion>\n" +
                    "\t\t\t<interimMsg xmlns=\"\">1</interimMsg>\n" +
                    "\t\t\t<document xmlns=\"\">" + xml.encodedString + "</document>\n" +
                    "\t\t\t<triggerPoint xmlns=\"\">2</triggerPoint>\n" +
                    "\t\t</sendDocument>\n" +
                    "\t</s:Body>\n" +
                    "</s:Envelope>";
            String SignatureBodyBase64 = Base64.getEncoder().encodeToString(SignatureBody.getBytes());

            JsonPath response = given()
                    .filter(new AllureRestAssured())
                    .contentType(ContentType.JSON)
                    .when()
                    .body("{\n" +
                            "                    \"requestId\": \"1\",\n" +
                            "                    \"signReferenceUri\": \"#body\",\n" +
                            "                    \"document\": \"" + SignatureBodyBase64 + "\",\n" +
                            "                    \"algorithm\": \"Gost3411_2012_256\",\n" +
                            "                    \"systemId\": \"" + SystemId + "\"\n" +
                            "  }")
                    .post(HostSignatureSoap)
                    .prettyPeek()
                    .body()
                    .jsonPath();

            System.out.println("Декодируем SignatureValue и SignedInfo");
            String SignatureValueBase64 = response.get("result.signatureValue");
            byte[] decoded = Base64.getDecoder().decode(SignatureValueBase64);
            SignatureValue = new String(decoded, StandardCharsets.UTF_8);

            String SignedInfoBase64 = response.get("result.signedInfo");
            decoded = Base64.getDecoder().decode(SignedInfoBase64);
            SignedInfo = new String(decoded, StandardCharsets.UTF_8);
            BinSecurityToken = response.get("result.binSecurityToken");
        }
    }

    @Step("Метод для отправки смс с другим телом запроса (soap), и проверка добавления значений в нужные таблицы")
    public void SoapBodyMethod (String DocType, Integer vmcl, Integer docTypeVersion, String sms) throws InterruptedException, SQLException, IOException {
        if (KingNumber == 2 | KingNumber == 3) {
            String body = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:a=\"http://www.w3.org/2005/08/addressing\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
                    "\t<s:Header>\n" +
                    "\t\t<a:Action wsu:Id=\"id-action\">sendDocument</a:Action>\n" +
                    "\t\t<transportHeader xmlns=\"http://egisz.rosminzdrav.ru\">\n" +
                    "\t\t\t<authInfo>\n" +
                    "\t\t\t\t<clientEntityId>" + SoapSystemId + "</clientEntityId>\n" +
                    "\t\t\t</authInfo>\n" +
                    "\t\t</transportHeader>\n" +
                    "\t\t<a:MessageID>43afe69d-b21a-428b-a624-f113f95ec743</a:MessageID>\n" +
                    "\t\t<a:ReplyTo wsu:Id=\"id-replyto\">\n" +
                    "\t\t\t<a:Address>https://ips.rosminzdrav.ru/79f5771afb36b</a:Address>\n" +
                    "\t\t</a:ReplyTo>\n" +
                    "\t\t<a:FaultTo>\n" +
                    "\t\t\t<a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address>\n" +
                    "\t\t</a:FaultTo>\n" +
                    "\t\t<a:To>https://ips.rosminzdrav.ru/8b02e1e4e03c7</a:To>\n" +
                    "\t\t<wsse:Security>\n" +
                    "\t\t\t<ds:Signature Id=\"SIG-" + UUID.randomUUID() + "\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
                    "\t\t\t\t" + SignedInfo + "\n" +
                    "\t\t\t\t<SignatureValue xmlns=\"http://www.w3.org/2000/09/xmldsig#\">" + SignatureValue + "</SignatureValue>\n" +
                    "\t\t\t\t<ds:KeyInfo Id=\"KI-d11574b2-8970-4767-9e19-d9377929227d\">\n" +
                    "\t\t\t\t\t<wsse:SecurityTokenReference Id=\"" + UUID.randomUUID() + "\">\n" +
                    "\t\t\t\t\t\t<wsse:Reference ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" URI=\"#" + UUID.randomUUID() + "\"/>\n" +
                    "\t\t\t\t\t</wsse:SecurityTokenReference>\n" +
                    "\t\t\t\t</ds:KeyInfo>\n" +
                    "\t\t\t</ds:Signature>\n" +
                    "\t\t\t<wsse:BinarySecurityToken EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" wsu:Id=\"a9671d73-c729-4327-959e-31928de90b2a\">" + BinSecurityToken + "</wsse:BinarySecurityToken>\n" +
                    "\t\t</wsse:Security>\n" +
                    "\t</s:Header>\n" +
                    "\t<s:Body xmlns:d2p1=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" d2p1:Id=\"body\">\n" +
                    "\t\t<sendDocument xmlns=\"http://vimis.rosminzdrav.ru/\">\n" +
                    "\t\t\t<vmcl xmlns=\"\">" + vmcl + "</vmcl>\n" +
                    "\t\t\t<vmcl_array xmlns=\"\">" + vmcl + "</vmcl_array>\n" +
                    "\t\t\t<docType xmlns=\"\">" + DocType + "</docType>\n" +
                    "\t\t\t<docTypeVersion xmlns=\"\">" + docTypeVersion + "</docTypeVersion>\n" +
                    "\t\t\t<interimMsg xmlns=\"\">1</interimMsg>\n" +
                    "\t\t\t<document xmlns=\"\">" + xml.encodedString + "</document>\n" +
                    "\t\t\t<triggerPoint xmlns=\"\">2</triggerPoint>\n" +
                    "\t\t</sendDocument>\n" +
                    "\t</s:Body>\n" +
                    "</s:Envelope>";

            Api(HostSoap, "post", null, null, body, 200, false);

            Thread.sleep(2000);
            System.out.println("Находим id записи в таблице " + sms + "");
            sql.StartConnection(
                    "Select max(id) from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and centralized_unloading_system_id = '99' and open_telemetry_trace_id = '00000000000000000000000000000000'");

            while (sql.resultSet.next()) {
                IdSoap = sql.resultSet.getString("max");
                System.out.println(IdSoap);
            }
            System.out.println("Находим запись по id в таблице " + sms + " и сверяем значения");
            sql.StartConnection(
                    "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and id = '" + IdSoap + "'");
            while (sql.resultSet.next()) {
                String patient_guid = sql.resultSet.getString("patient_guid");
                Assertions.assertEquals(patient_guid, PGuid,
                        "patient_guid не совпадает");

                local_uidSoap = sql.resultSet.getString("local_uid");
                String transfer_id = sql.resultSet.getString("transfer_id");
                Assertions.assertEquals(transfer_id, local_uidSoap, "transfer_id не совпадает");

                String patient_snils = sql.resultSet.getString("patient_snils");
                Assertions.assertEquals(patient_snils, PSnils_, "patient_snils не совпадает");
            }
        }
    }

    @Step("Метод который принимает в себя файл json - {0} и заменяет нужные параметры")
    public String JsonMethod (String FileName, Map<String, Object> changes, boolean deleteYes, String[] delete) {

        if (FileName.contains("api_diagnostic.json") | FileName.contains("api_direction.json")) {
            // Нужно для метода /api/diagnostic, чтобы дата подставлялась всегда актуальная
            changes.put("$.DateDirection", Date + " 08:30:52");
        }

        try {
            // Чтение файла JSON
            String jsonContent = new String(Files.readAllBytes(Paths.get(FileName)));

            // Парсинг содержимого файла в JSONObject
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonContent);

            // Создание DocumentContext из JSONObject (нужно для того, чтобы можно методом JsonMethodRead прочитать актуальные значения)
            jsonContext = com.jayway.jsonpath.JsonPath.parse(jsonObject);

            // Изменение значений по указанным JSONPath
            for (Map.Entry<String, Object> entry : changes.entrySet()) {
                // Записываем изначальные параметры, чтобы можно было вернуть их назад
                xml.originalChanges.put(entry.getKey(), JsonMethodRead(FileName, entry.getKey(), false, false));
                jsonContext.set(entry.getKey(), entry.getValue());
                //jsonContext.put(entry.getKey(), entry.getValue());
            }

            if (deleteYes) {
                // Удаление (если нужно) каких то параметров
                for (String path : delete) {
                    jsonContext.delete(path);
                }
            }

            // Добавление новых элементов в JSON
            if (xml.addElements != null && !xml.addElements.isEmpty()) {
                for (Map.Entry<String, List<Map<String, Object>>> addEntry : xml.addElements.entrySet()) {
                    String path = addEntry.getKey();

                    // Создаём пустой массив, если его нет
                    try {
                        jsonContext.read(path);
                    } catch (com.jayway.jsonpath.PathNotFoundException e) {
                        jsonContext.put("$", path.substring(2), new ArrayList<Map<String, Object>>());
                    }

                    List<Map<String, Object>> listToAdd = addEntry.getValue();

                    // Добавляем каждый объект в массив
                    for (Map<String, Object> obj : listToAdd) {
                        jsonContext.add(path, obj);
                    }
                }
            }
            if (xml.addElementsFirst != null && !xml.addElementsFirst.isEmpty()) {
                for (Map.Entry<String, Object> entry : xml.addElementsFirst.entrySet()) {
                    String path = entry.getKey();
                    Object value = entry.getValue();

                    // Устанавливаем одиночное значение
                    try {
                        jsonContext.set(path, value);
                    } catch (com.jayway.jsonpath.PathNotFoundException e) {
                        jsonContext.put("$", path.substring(2), value);
                    }
                }
            }

            // Возвращение измененного JSON в виде строки
            return jsonContext.jsonString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Step("Метод который принимает в себя файл json - {0} и заменяет нужные параметры")
    public String JsonMethodReturn (String FileName, Map<String, Object> changes) {

        try {
            // Удаляем изменения, чтобы не копились и не мешали при следующем изменении
            xml.changes.clear();

            // Чтение файла JSON
            String jsonContent = new String(Files.readAllBytes(Paths.get(FileName)));

            // Парсинг содержимого файла в JSONObject
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonContent);

            // Создание DocumentContext из JSONObject (нужно для того, чтобы можно методом JsonMethodRead прочитать актуальные значения)
            jsonContext = com.jayway.jsonpath.JsonPath.parse(jsonObject);

            // Изменение значений по указанным JSONPath
            for (Map.Entry<String, Object> entry : changes.entrySet()) {
                jsonContext.set(entry.getKey(), entry.getValue());
            }

            // Удаляем оригинальные значения, чтобы записать новые
            xml.originalChanges.clear();

            // Возвращение измененного JSON в виде строки
            return jsonContext.jsonString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Step("Метод который принимает в себя файл json - {0} и возвращает значение переменной {1}")
    public Object JsonMethodRead (String FileName, String readPath, boolean readNew, boolean stringTrue) {

        try {
            // Условие true если до этого не меняли значения методом JsonMethod
            if (readNew) {
                // Чтение файла JSON
                String jsonContent = new String(Files.readAllBytes(Paths.get(FileName)));

                // Парсинг содержимого файла в JSONObject
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(jsonContent);

                // Создание DocumentContext из JSONObject (нужно для того, чтобы можно методом JsonMethodRead прочитать актуальные значения)
                jsonContext = com.jayway.jsonpath.JsonPath.parse(jsonObject);
            }

            // Условие, если нам нужно принять String (в которой есть json)
            else if (stringTrue) {
                // Парсинг содержимого файла в JSONObject
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(FileName);

                // Создание DocumentContext из JSONObject (нужно для того, чтобы можно методом JsonMethodRead прочитать актуальные значения)
                jsonContext = com.jayway.jsonpath.JsonPath.parse(jsonObject);
            }

            // Получение значения по указанному пути
            Object value = jsonContext.read(readPath);

            // Определение типа данных
            if (value != null) {
                Class<?> type = value.getClass();

                // Преобразование значения в соответствующий тип
                if (type == String.class) {
                    return (String) value;
                } else if (type == Integer.class) {
                    return (Integer) value;
                } else if (type == Boolean.class) {
                    return (Boolean) value;
                } else if (type == Double.class) {
                    return (Double) value;
                } else if (type == Long.class) {
                    return (Long) value;
                } else if (type == Float.class) {
                    return (Float) value;
                } else if (type == Short.class) {
                    return (Short) value;
                } else if (type == Byte.class) {
                    return (Byte) value;
                } else if (type == Character.class) {
                    return (Character) value;
                } else if (type == Void.class) {
                    return null;
                } else {
                    throw new IllegalArgumentException("Неизвестный тип данных: " + type.getName());
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Step("Метод который принимает массив Json объектов и возвращает нужное значение внутри этих объектов")
    public String JsonArrayRead (String jsonString, String pathJson) {
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = null;
        StringBuilder messages = new StringBuilder();

        try {
            jsonArray = (JSONArray) parser.parse(jsonString);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            messages.append(jsonObject.get(pathJson));
            if (i != jsonArray.size() - 1) {
                messages.append(";");
            }
            //System.out.println(jsonObject.get(pathJson));
        }
        return messages.toString();
    }

    @Step("Метод получения cookies для Телемеда")
    public void GetCookies () throws InterruptedException, IOException {
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);
        DirectionsForQuotas directionsForQuotas = new DirectionsForQuotas(driver);
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        Thread.sleep(2000);

        Session = driver.manage().getCookieNamed(".AspNetCore.Session");
        TelemedC1 = driver.manage().getCookieNamed(".AspNet.Core.TelemedC1");
        TelemedC2 = driver.manage().getCookieNamed(".AspNet.Core.TelemedC2");
        Telemed = driver.manage().getCookieNamed(".AspNet.Core.Telemed");

        System.out.println(Session);

        InputPropFile("Session", String.valueOf(Session));
        InputPropFile("TelemedC1", String.valueOf(TelemedC1));
        InputPropFile("TelemedC2", String.valueOf(TelemedC2));
        InputPropFile("Telemed", String.valueOf(Telemed));
    }

    @Step("Метод получения всех cookies и запись их в файл")
    public void GetCookiesFile () {
        File fileCookies = new File("src/test/resources/cookies.txt");
        Set<Cookie> cookies = driver.manage().getCookies();

        //Записываем куки в файл
        try (FileWriter writer = new FileWriter(fileCookies)) {
            for (Cookie cookie : cookies) {
                writer.write(cookie.getName() + "=" + cookie.getValue() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Step("Метод использования cookies из файла")
    public void ReadCookiesFile () {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/cookies.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String name = parts[0];
                    String value = parts[1];
                    driver.manage().addCookie(new Cookie(name, value));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Step("Метод для получения теущего года, дня месяца")
    public void GetDate () {
        calendar = new GregorianCalendar();

        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH) + 1;
        Day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Step("Метод который прибавляет к текущей дате нужное количество дней")
    public static LocalDate addDay (String dateString, Integer day) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateString, formatter);
        return date.plusDays(day);
    }

    @Step("Метод который считает разницу в годах")
    public long calculateYearDifference (String inputDate) {
        GetDate();
        // Определяем формат даты
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int yearDifference = 0;

        try {
            // Преобразуем строку в объект Date
            Calendar givenDate = new GregorianCalendar();
            givenDate.setTime(sdf.parse(inputDate));

            // Получаем текущую дату
            Calendar currentDate = Calendar.getInstance();

            // Вычисляем разницу в годах
            yearDifference = currentDate.get(Calendar.YEAR) - givenDate.get(Calendar.YEAR);

            // Проверяем, был ли день и месяц текущей даты раньше, чем у заданной даты
            if (currentDate.get(Calendar.MONTH) < givenDate.get(Calendar.MONTH) ||
                    (currentDate.get(Calendar.MONTH) == givenDate.get(Calendar.MONTH) &&
                            currentDate.get(Calendar.DAY_OF_MONTH) < givenDate.get(Calendar.DAY_OF_MONTH))) {
                yearDifference--;
            }
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }

        return yearDifference;
    }

    @Step("Метод для получения текущего года, с возможностью преобразовывать")
    public String SetDate (Integer day, Integer month) {
        calendar = new GregorianCalendar();

        calendar.add(Calendar.DAY_OF_MONTH, day);
        calendar.add(Calendar.MONTH, month);

        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH) + 1;
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        String Year_ = "";
        String Month_ = "";
        String Day_ = "";
        switch (Month) {
            case 1:
                Month_ = "01";
                break;
            case 2:
                Month_ = "02";
                break;
            case 3:
                Month_ = "03";
                break;
            case 4:
                Month_ = "04";
                break;
            case 5:
                Month_ = "05";
                break;
            case 6:
                Month_ = "06";
                break;
            case 7:
                Month_ = "07";
                break;
            case 8:
                Month_ = "08";
                break;
            case 9:
                Month_ = "09";
                break;
            default:
                Month_ = String.valueOf(Month);
        }
        switch (Day) {
            case 1:
                Day_ = "01";
                break;
            case 2:
                Day_ = "02";
                break;
            case 3:
                Day_ = "03";
                break;
            case 4:
                Day_ = "04";
                break;
            case 5:
                Day_ = "05";
                break;
            case 6:
                Day_ = "06";
                break;
            case 7:
                Day_ = "07";
                break;
            case 8:
                Day_ = "08";
                break;
            case 9:
                Day_ = "09";
                break;
            default:
                Day_ = String.valueOf(Day);
        }
        return Month_ + " " + Day_;
    }

    @Step("Метод для преобразования даты из БД - {0}")
    public void ChangeDate (String date) {
        YearSql = date.substring(0, 4);
        MonthSql = date.substring(5, 7);
        DaySql = date.substring(8, 10);
    }

    @Step("Метод для получения данных из РРП по Guid - {0}")
    public void GetRRP (String guid, String snils) throws IOException {

        RRPConnect();
        System.out.println("Авторизуемся в РРП через АПИ");
        String body = "{\n" +
                "    \"UserName\": \"" + LoginRRPApi + "\",\n" +
                "    \"Password\": \"" + PasswordRRPApi + "\"\n" +
                "}";
        System.out.println(UrlRRPApi + "/api/authenticate");
        Api(UrlRRPApi + "/api/authenticate", "post", null, null, body, 200, false);

        System.out.println("Ищем пациента в РРП через АПИ");
        if (!TextUtils.isEmpty(guid)) {
            Response = given()
                    .header("Authorization", "Bearer " + Response.getString("access_token"))
                    .contentType(ContentType.JSON)
                    .log().uri()
                    .when()
                    .get(UrlRRPApi + "/IEMKRegionalService/services/patient/search/" + guid + "")
                    .body()
                    .jsonPath();
        } else {
            Response = given()
                    .header("Authorization", "Bearer " + Response.getString("access_token"))
                    .contentType(ContentType.JSON)
                    .log().uri()
                    .when()
                    .get(UrlRRPApi + "/IEMKRegionalService/services/patient/search/?snils=" + snils + "")
                    .body()
                    .jsonPath();
        }
    }

    @Step("Метод изменения данных в РРП по Guid - {0}")
    public void PostRRP (String body_) throws IOException {

        RRPConnect();
        System.out.println("Авторизуемся в РРП через АПИ");
        String body = "{\n" +
                "    \"UserName\": \"" + LoginRRPApi + "\",\n" +
                "    \"Password\": \"" + PasswordRRPApi + "\"\n" +
                "}";
        System.out.println(UrlRRPApi + "/api/authenticate");
        Api(UrlRRPApi + "/api/authenticate", "post", null, null, body, 200, false);

        System.out.println("Обновляем пациента в РРП через АПИ");
        Response = given()
                .header("Authorization", "Bearer " + Response.getString("access_token"))
                .contentType(ContentType.JSON)
                .log().uri()
                .log().body()
                .when()
                .body(body_)
                .post(UrlRRPApi + "/IEMKRegionalService/services/patient/publish")
                .prettyPeek()
                .body()
                .jsonPath();
    }

    @Step("Метод в котором убираем дату в таблице registration_datetime, чтобы текущая была на 350 дней больше (1895, 1910)")
    public void SQLAkineo (String patientGuid) throws SQLException {

        System.out.println(
                "Убираем дату в таблице registration_datetime, чтобы текущая была на 350 дней больше (1895, 1910)");
        GetDate();
        Year = Year - 2;
        List<String> list = new ArrayList<>();
        sql.StartConnection(
                "Select sms_id from vimis.akineo_sms_v5_register where patient_guid = '" + patientGuid + "' and registration_datetime::date > '" + Year + "-05-27 05:00:00.000 +0500';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("sms_id");
            list.add(sql.value);
        }
        for (int i = 0; i < list.size(); i++) {
            sql.UpdateConnection(
                    "Update vimis.akineo_sms_v5_register set registration_datetime = '2020-05-25 05:00:00.000 +0500' where sms_id = '" + list.get(
                            i) + "';");
        }
    }

    @Step("Метод в котором проверяем есть ли нужная роль, если нет добавляем")
    public void AddRoleTrue (String nameRole) throws SQLException, IOException, InterruptedException {
        sql.PrintSQL = false;
        Integer idRole = null;
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);

        sql.StartConnection("SELECT count(*) from telmed.accessroles where name = '" + nameRole + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }
        idRole = Integer.valueOf(sql.value);

        if (idRole == 0) {
            System.out.println("Роли Тестировщик нет - добавляем");
            AuthorizationMethod(authorizationObject.OKB);
            Thread.sleep(1500);
            cookies = driver.manage().getCookies();

            sql.UpdateConnection(
                    "insert into telmed.accessroles (name, description, createdate, updatedate, visible, ldaprole, isavailable) values ('" + nameRole + "', 'Не меняйте пожалуйста доступы', '" + Date + " 18:10:00.000', '" + Date + " 18:10:00.000', '0', null, '1');");
            Thread.sleep(1500);

            sql.StartConnection("select * from telmed.accessroles where name = '" + nameRole + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
            }

            Api(HostAddressWeb + "/permission/roles/accesses", "post", null, null, "" +
                    "{\n" +
                    "   \"roleId\":" + sql.value + ",\n" +
                    "   \"selectedAccess\":[1,62,72,2,3,4,5,6,103,7,86,87,88,89,8,9,26,10,11,12,13,14,15,28,50,102,17,38,39,16,18,19,20,21,33,34,29,30,35,36,46,47,52,53,63,70,24,25,27,43,37,40,41,42,44,48,49,51,54,55,56,57,58,59,60,64,65,68,66,67,69,80,81,82,83,84,85,71,73,79,90,91,93,94,96,97,95]\n" +
                    "}", 200, false);
        }
    }

    @Step("Метод в котором проверяем есть ли нужный доступ, а после либо удаляем, либо добавляем")
    public static void AddRole (String nameRole, String Access, boolean addAndDelete) {
        String idRole = null;
        String idAccess = null;
        sql.value = null;
        sql.PrintSQL = false;

        try {
            // Проверяем есть ли доступ у нужной роли
            sql.StartConnection("SELECT count(*)  FROM telmed.rolewithaccess r\n" +
                    "join telmed.accessroles a on r.roleid = a.id \n" +
                    "join telmed.accessesdirectory d on r.accessid = d.id \n" +
                    "WHERE a.\"name\" = '" + nameRole + "' and d.\"name\" = '" + Access + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("count");
            }

            sql.StartConnection("SELECT * from telmed.accessroles where name = '" + nameRole + "'");
            while (sql.resultSet.next()) {
                idRole = sql.resultSet.getString("id");
            }
            sql.StartConnection("SELECT * from telmed.accessesdirectory where name = '" + Access + "';");
            while (sql.resultSet.next()) {
                idAccess = sql.resultSet.getString("id");
            }

            if (sql.value.equals("0") & addAndDelete) {
                sql.UpdateConnection(
                        "insert into telmed.rolewithaccess (roleid, accessid) values ('" + idRole + "', '" + idAccess + "');");
            }

            if (sql.value.equals("1") & !addAndDelete) {
                sql.UpdateConnection(
                        "delete from telmed.rolewithaccess where roleid = '" + idRole + "' and accessid = '" + idAccess + "';");
            }
            sql.PrintSQL = true;
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибочка - " + e.getMessage());
        }
    }

    @Step("Метод для отправки СМС в ВИМИС по всем направлениям")
    public void AddXmlAll () throws IOException, SQLException, InterruptedException {

        xml.ApiSmd("SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21);
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");

        xml.ApiSmd("SMS/SMS3.xml", "3", 2, 2, true, 3, 1, 9, 18, 1, 57, 21);
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");

        xml.ApiSmd("SMS/SMS3.xml", "3", 3, 2, true, 2, 1, 9, 18, 1, 57, 21);
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");

        xml.ApiSmd("SMS/SMS3.xml", "3", 4, 2, true, 2, 1, 9, 18, 1, 57, 21);
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");

        xml.ApiSmd("SMS/SMS3.xml", "3", 5, 2, true, 3, 1, 9, 18, 1, 57, 21);
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");

        xml.ApiSmd("SMS/SMS3.xml", "3", 99, 2, true, 0, 1, 9, 18, 1, 57, 21);
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
    }

    @Step("Метод для отправки СМС")
    public void AddSms (String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1) throws IOException, SQLException, InterruptedException {
        sql = new SQL();
        TableVmcl(vmcl);
        String idDirectionSms = null;

        System.out.println("Отправляем смс с Doctype = " + DocType + "");
        xml.ApiSmd(FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                position1, speciality1);
        sql.StartConnection(
                "Select * from " + smsBase + " where local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            idDirectionSms = sql.resultSet.getString("id");
            System.out.println(idDirectionSms);
        }
        InputPropFile("idDirectionSms", idDirectionSms);
    }

    @Step("Метод создания удалённой консультации")
    public void CreateConsul (String MoAuthorization, String TargetMO, String GuidPatient, String profile) throws SQLException, InterruptedException, IOException {
        ConsultationOutgoingUnfinished consultationOU = new ConsultationOutgoingUnfinished(driver);
        Authorization authorization = new Authorization();

        System.out.println("Отменяем созданную консультацию");
        consultationOU.CancelConsultation(GuidPatient);

        if (MoAuthorization.equals("1.2.643.5.1.13.13.12.2.86.8902")) {
            Token = authorization.AuthorizationsAdd("1.2.643.5.1.13.13.12.2.86.8902", "21");
        }

        if (profile.equals("акушерству и неонатологии")) {
            profile = "2";
        }

        xml.changes.put("$.PatientGuid", GuidPatient);
        xml.changes.put("$.TargetMOId", TargetMO);
        xml.changes.put("$.Profile", 19);
        String body = JsonMethod("SMS/Body/api_direction.json", xml.changes, false, null);
        Api(HostAddress + "/api/direction", "post", null, null, body, 200, false);
        Assertions.assertNotEquals(Response.getString("StatusCode"), "400",
                "Ошибки при создании консультации быть не должно");
    }

    @Step("Метод который берёт нужные таблицы в зависимости от направления")
    public void TableVmcl (Integer vmcl) {
        if (vmcl == 1) {
            smsBase = "vimis.sms";
            logsBase = "vimis.documentlogs";
            remdBase = "vimis.remd_onko_sent_result";
            infoBase = "vimis.additionalinfo";
            attachmentsBase = "vimis.sms_mo_attachments";
            rrnBase = "vimis.report_rrn_onko";
            common_infoBase = "parsing.common_info_onkosms";
        }
        if (vmcl == 2) {
            smsBase = "vimis.preventionsms";
            logsBase = "vimis.preventionlogs";
            remdBase = "vimis.remd_prevention_sent_result";
            infoBase = "vimis.preventionadditionalinfo";
            attachmentsBase = "vimis.preventionsms_mo_attachments";
            rrnBase = "vimis.report_rrn_prevention";
            common_infoBase = "parsing.common_info_preventionsms";
        }
        if (vmcl == 3) {
            smsBase = "vimis.akineosms";
            logsBase = "vimis.akineologs";
            remdBase = "vimis.remd_akineo_sent_result";
            infoBase = "vimis.akineoadditionalinfo";
            attachmentsBase = "vimis.akineosms_mo_attachments";
            rrnBase = "vimis.report_rrn_akineo";
            common_infoBase = "parsing.common_info_akineosms";
        }
        if (vmcl == 4) {
            smsBase = "vimis.cvdsms";
            logsBase = "vimis.cvdlogs";
            remdBase = "vimis.remd_cvd_sent_result";
            infoBase = "vimis.cvdadditionalinfo";
            attachmentsBase = "vimis.cvdsms_mo_attachments";
            rrnBase = "vimis.report_rrn_cvd";
            common_infoBase = "parsing.common_info_cvdsms";
        }
        if (vmcl == 5) {
            smsBase = "vimis.infectionsms";
            logsBase = "vimis.infectionlogs";
            remdBase = "vimis.remd_infection_sent_result";
            infoBase = "vimis.infectionadditionalinfo";
            attachmentsBase = "vimis.infectionsms_mo_attachments";
            rrnBase = "vimis.report_rrn_infection";
            common_infoBase = "parsing.common_info_infectionsms";
        }
        if (vmcl == 99) {
            smsBase = "vimis.remd_sent_result";
            logsBase = "";
            remdBase = "vimis.remd_sent_result";
            infoBase = "vimis.remdadditionalinfo";
            attachmentsBase = "vimis.remdsms_mo_attachments";
            rrnBase = "vimis.report_rrn_remd";
            common_infoBase = "parsing.common_info_remdsms";
        }
    }
}