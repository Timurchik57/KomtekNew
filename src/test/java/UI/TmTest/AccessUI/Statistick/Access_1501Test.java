package UI.TmTest.AccessUI.Statistick;

import Base.SQL;
import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@ExtendWith(TestListenerApi.class)
@Feature("Статистика")
@Tag("Аналитика_МО_ОМП")
@Tag("Отображение_документов_ЛК")
@Tag("РРП")
public class Access_1501Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;
    SQL sql;
    public Integer Count = 0;
    public String patient_guid;
    public List<String> listBD;

    @Test
    @Story("Проверка отображения документов в ЛК Врача")
    @Issue(value = "TEL-1497")
    @Issue(value = "TEL-1501")
    @Issue(value = "TEL-1502")
    @Issue(value = "TEL-1701")
    @Issue(value = "TEL-1747")
    @Issue(value = "TEL-1888")
    @Issue(value = "TEL-3175")
    @Link(name = "ТМС-1554", url = "https://team-1okm.testit.software/projects/5/tests/1554?isolatedSection=f07b5d61-7df7-4e90-9661-3fd312065912")
    @Link(name = "ТМС-1663", url = "https://team-1okm.testit.software/projects/5/tests/1663?isolatedSection=f07b5d61-7df7-4e90-9661-3fd312065912")
    @Link(name = "ТМС-1764", url = "https://team-1okm.testit.software/projects/5/tests/1764?isolatedSection=623e281e-2190-42e3-913b-8beea1fbc57d")
    @Link(name = "ТМС-1770", url = "https://team-1okm.testit.software/projects/5/tests/1770?isolatedSection=623e281e-2190-42e3-913b-8beea1fbc57d")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Отображение всех документов")
    @Description("Переходим в Аналитика Мо по ОМП, выбираем блок гдк есть маршруты, переходим к пациенту, у пациента смотрим отображение всех документов")
    public void Access_1501() throws SQLException, InterruptedException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);
        sql = new SQL();
        listBD = new ArrayList<>();

        System.out.println("Авторизуемся и переходим в Статистика - Аналитика МО по ОМП");
        AuthorizationMethod(authorizationObject.OKB);
        Thread.sleep(2500);
//        ClickElement(analyticsMO.Analytics);
//
//        System.out.println("Проверяем где есть маршруты, во всех блоках");
//        WaitElement(analyticsMO.Tall);
//        WaitElement(analyticsMO.Average);
//        WaitElement(analyticsMO.low);
//        analyticsMO.QuantityStackMethod();
//        System.out.println(AnalyticsMO.TallMO);
//        System.out.println(AnalyticsMO.AverageMO);
//        System.out.println(AnalyticsMO.lowMO);
//
//        System.out.println("Переходим в первый попавшийся блок, у которого есть мо с этапами");
//        if (AnalyticsMO.TallMO) {
//            ClickElement(analyticsMO.NameMOTallFirst);
//        } else {
//            if (AnalyticsMO.AverageMO) {
//                ClickElement(analyticsMO.NameMOAverageFirst);
//            } else {
//                ClickElement(analyticsMO.NameMOlowFirst);
//            }
//        }
//
//        System.out.println("Выбираем первого пациента и переходим к нему");
//        WaitNotElement3(analyticsMO.Loading, 30);
      //  ClickElement(analyticsMO.FirstPatient);
        driver.get(HostAddressWeb + "/registry/patient/7eca7459-af84-48c2-9fb9-b8bc2936b444/dashboard");
        WaitElement(analyticsMO.Snils);
        Thread.sleep(1500);
        String Snils = driver.findElement(analyticsMO.Snils).getText();
        WaitNotElement3(analyticsMO.LoadingDocsShablonAndDocs, 60);
        WaitElement(analyticsMO.Docs);
        List<WebElement> DocsWeb = driver.findElements(analyticsMO.Docs);

        System.out.println("Берём гуид пациента");
        GetRRP("", Snils);
        patient_guid = Response.getString("patients[0].guid");
        System.out.println(patient_guid);

        System.out.println("Берём все LocalUid с веба");
        GetDocs(patient_guid, Snils, DocsWeb.size());

        System.out.println("Ситаем количество документов для Онкологии");
        CountVimis(patient_guid, "vimis.sms", "vimis.additionalinfo");
        System.out.println("Ситаем количество документов для Профилактики");
        CountVimis(patient_guid, "vimis.preventionsms", "vimis.preventionadditionalinfo");
        System.out.println("Ситаем количество документов для Акинео");
        CountVimis(patient_guid, "vimis.akineosms", "vimis.akineoadditionalinfo");
        System.out.println("Ситаем количество документов для ССЗ");
        CountVimis(patient_guid, "vimis.cvdsms", "vimis.cvdadditionalinfo");
        System.out.println("Ситаем количество документов для Инфекции");
        CountVimis(patient_guid, "vimis.infectionsms", "vimis.infectionadditionalinfo");

        System.out.println("Ситаем количество документов для РЭМД без версии (ПДФ)");
        CountRemdVersion(patient_guid, "vimis.remd_sent_result", "vimis.remdadditionalinfo");

        System.out.println("Ситаем количество документов для РЭМД по уникальности local_uid и setid");
        CountRemdLocal_uid(patient_guid, "vimis.remd_sent_result", "vimis.remdadditionalinfo");

        System.out.println("Ситаем количество связанных документов МСЭ");
        CountRemdMSE();

        System.out.println("Ситаем количество документов в КРЭМД");
        CountKremd(Snils);

        System.out.println("Ситаем количество документов для Сертификат РЭМД");
        CountRemd(patient_guid, "vimis.certificate_remd_sent_result");

        Assertions.assertEquals(DocsWeb.size(), Count, "Количество отображаемых документов не совадает");
    }

    @Test
    @Story("Проверка отображения документов в ЛК Врача")
    @Issue(value = "TEL-1761")
    @Issue(value = "TEL-1888")
    @Link(name = "ТМС-1554", url = "https://team-1okm.testit.software/projects/5/tests/1554?isolatedSection=f07b5d61-7df7-4e90-9661-3fd312065912")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Отображение документов с чек боксом Документы есть на федеральном уровне")
    @Description("Переходим в Аналитика Мо по ОМП, выбираем блок гдк есть маршруты, переходим к пациенту, у пациента смотрим отображение документов с чек боксом Документы есть на федеральном уровне")
    public void Access_1761() throws SQLException, InterruptedException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);
        sql = new SQL();

        System.out.println("Авторизуемся и переходим в Статистика - Аналитика МО по ОМП");
        AuthorizationMethod(authorizationObject.OKB);
        Thread.sleep(2500);
        ClickElement(analyticsMO.Analytics);

        System.out.println("Проверяем где есть маршруты, во всех блоках");
        WaitElement(analyticsMO.Tall);
        WaitElement(analyticsMO.Average);
        WaitElement(analyticsMO.low);
        analyticsMO.QuantityStackMethod();
        System.out.println(AnalyticsMO.TallMO);
        System.out.println(AnalyticsMO.AverageMO);
        System.out.println(AnalyticsMO.lowMO);

        System.out.println("Переходим в первый попавшийся блок, у которого есть мо с этапами");
        if (AnalyticsMO.TallMO) {
            ClickElement(analyticsMO.NameMOTallFirst);
        } else {
            if (AnalyticsMO.AverageMO) {
                ClickElement(analyticsMO.NameMOAverageFirst);
            } else {
                ClickElement(analyticsMO.NameMOlowFirst);
            }
        }
        System.out.println("Выбираем первого пациента и переходим к нему");
        WaitNotElement3(analyticsMO.Loading, 30);
        driver.get(HostAddressWeb + "/registry/patient/7eca7459-af84-48c2-9fb9-b8bc2936b444/dashboard");
        WaitElement(analyticsMO.Snils);
        Thread.sleep(1500);
        String Snils = driver.findElement(analyticsMO.Snils).getText();
        WaitNotElement3(analyticsMO.LoadingDocsShablonAndDocs, 40);
        WaitElement(analyticsMO.Docs);
        ClickElement(analyticsMO.FederalTrue);
        WaitNotElement3(analyticsMO.LoadingDocsShablonAndDocs, 40);
        Thread.sleep(10000);
        if (KingNumber == 4) {
            Thread.sleep(10000);
        }
        List<WebElement> DocsWeb = driver.findElements(analyticsMO.Docs);

        System.out.println("Берём гуид пациента");
        GetRRP("", Snils);
        patient_guid = Response.getString("patients[0].guid");
        System.out.println(patient_guid);

        System.out.println("Ситаем количество документов для Онкологии");
        CountVimisTrue(patient_guid, "vimis.sms", "vimis.documentlogs", "vimis.additionalinfo");
        System.out.println("Ситаем количество документов для Профилактики");
        CountVimisTrue(patient_guid, "vimis.preventionsms", "vimis.preventionlogs", "vimis.preventionadditionalinfo");
        System.out.println("Ситаем количество документов для Акинео");
        CountVimisTrue(patient_guid, "vimis.akineosms", "vimis.akineologs", "vimis.akineoadditionalinfo");
        System.out.println("Ситаем количество документов для ССЗ");
        CountVimisTrue(patient_guid, "vimis.cvdsms", "vimis.cvdlogs", "vimis.cvdadditionalinfo");
        System.out.println("Ситаем количество документов для Инфекции");
        CountVimisTrue(patient_guid, "vimis.infectionsms", "vimis.infectionlogs", "vimis.infectionadditionalinfo");
        System.out.println("Ситаем количество документов для РЭМД");
        CountRemdTrue(patient_guid, "vimis.remd_sent_result");
        System.out.println("Ситаем количество документов в КРЭМД");
        CountKremd(Snils);
        Assertions.assertEquals(DocsWeb.size(), Count, "Количество отображаемых документов не совадает");
    }

    @Step("Проверка документов ВИМИС")
    public void CountVimis(String patient_guid, String sms, String info) throws SQLException {
        sql = new SQL();
        // 3175 - добавляем проверку по for_send, документы с одинаковым local_uid отображается только если for_send = true

        System.out.println(patient_guid);
        List<String> list = new ArrayList<>();
        sql.StartConnection("select count(m.*) from  (SELECT a.setid, max(s.version_doc) FROM " + sms + " s\n" +
                "join " + info + " a on s.id = a.smsid\n" +
                "join dpc.mis_sp_mu m on s.medicalidmu = m.medicalidmu\n" +
                "WHERE s.patient_guid = '" + patient_guid + "' and s.is_valid_xml is true and (a.is_contain_confidential_diagnoses is null or a.is_contain_confidential_diagnoses is false) and m.to_display is true and s.for_send is true group by a.setid) m;");
        while (sql.resultSet.next()) {
            Integer count = Integer.valueOf(sql.resultSet.getString("count"));
            System.out.println("Найдено в таблице - " + count);
            Count += count;
            System.out.println("Суммарное количество - " + Count);
        }
    }

    @Step("Проверка документов РЭМД сертификатов")
    public void CountRemd(String patient_guid, String remd) throws SQLException {
        sql = new SQL();

        sql.StartConnection("SELECT count(*) FROM " + remd + " s\n" +
                "  join dpc.mis_sp_mu m on s.medicalidmu = m.medicalidmu\n" +
                "where s.patient_guid = '" + patient_guid + "' and m.to_display is true;");
        while (sql.resultSet.next()) {
            Integer count = Integer.valueOf(sql.resultSet.getString("count"));
            System.out.println("Найдено в таблице - " + count);
            Count += count;
            System.out.println("Суммарное количество - " + Count);
        }
    }

    @Step("Проверка документов РЭМД пдф")
    public void CountRemdVersion(String patient_guid, String remd, String info) throws SQLException {
        sql = new SQL();

        sql.StartConnection("select count(m.*) from (SELECT * FROM " + remd + " s\n" +
                "join " + info + " r on s.id = r.smsid \n" +
                "join dpc.mis_sp_mu m on s.medicalidmu = m.medicalidmu\n" +
                "where r.\"version\"  is null and (r.is_contain_confidential_diagnoses is null or r.is_contain_confidential_diagnoses is false) and s.is_valid_xml is true and s.patient_guid = '" + patient_guid + "' and m.to_display is true) as m;");
        while (sql.resultSet.next()) {
            Integer count = Integer.valueOf(sql.resultSet.getString("count"));
            System.out.println("Найдено в таблице - " + count);
            Count += count;
            System.out.println("Суммарное количество - " + Count);
        }
    }

    @Step("Проверка документов РЭМД по уникальности Local_Uid")
    public void CountRemdLocal_uid(String patient_guid, String remd, String info) throws SQLException {
        sql = new SQL();

        Map<String, String> map = new HashMap<>();
        sql.StartConnection(
                "select m.setid, m.local_uid from (select  s.local_uid, r.setid, max(s.version_doc), s.medicalidmu FROM " + remd + " s\n" +
                        "join " + info + " r on s.id = r.smsid \n" +
                        "join dpc.mis_sp_mu m on s.medicalidmu = m.medicalidmu \n" +
                        "where s.patient_guid = '" + patient_guid + "' and (r.is_contain_confidential_diagnoses is null or r.is_contain_confidential_diagnoses is false) and m.to_display is true and s.is_valid_xml is true group by s.local_uid, r.setid , s.medicalidmu) as m group by m.setid, m.local_uid;");
        while (sql.resultSet.next()) {
            String setid = sql.resultSet.getString("setid");
            String local_uid = sql.resultSet.getString("local_uid");

            map.put(setid, local_uid);
        }

        /** Удаление дублей по ключу и значению (setid, local_uid)*/
        Set<String> existing = new HashSet<>();
        map = map.entrySet()
                .stream()
                .filter(entry -> existing.add(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println("Найдено в таблице - " +  map.size());
        Count += map.size();
        System.out.println("Суммарное количество - " + Count);
    }

    @Step("Считаем колличество связанных документов с МСЭ")
    public void CountRemdMSE() throws SQLException {
        List<WebElement> web = driver.findElements(By.xpath("//p[contains(.,'Обратный талон МСЭ')]"));

        System.out.println("Найдено на вебе - " + web.size());
        Count += web.size();
        System.out.println("Суммарное количество - " + Count);
    }

    @Step("Считаем колличество документов из КРЭМД")
    public void CountKremd(String snils) throws SQLException {
        String Host;

        if (KingNumber == 4) {
            Host = "http://192.168.2.21:34274";
        } else {
            Host = "http://192.168.2.126:5300";
        }

        JsonPath response = given()
                .filter(new AllureRestAssured())
                .header("apiKey", "1365500b-9c64-4e94-bc94-e8a15d7a9ec3")
                .contentType(ContentType.JSON)
                .when()
                .get(Host + "/api/documents?patientSnils=" + snils + "")
                .body()
                .jsonPath();
        try {
            Integer number = response.get("result.totalElements");
            System.out.println("Найдено из запроса - " + number);
            Count += number;
            System.out.println("Суммарное количество - " + Count);
        } catch (NullPointerException n) {
            System.out.println("У пациента со снилс = '" + snils + "' нет документов КРЭМД");
        }
    }

    @Step("Проверка документов ВИМИС принятых ФВИМИС")
    public void CountVimisTrue(String patient_guid, String sms, String logs, String info) throws SQLException {
        sql = new SQL();

        System.out.println(patient_guid);
        List<String> list = new ArrayList<>();
        sql.StartConnection("select count(m.*) from  (SELECT a.\"version\" FROM " + sms + " s\n" +
                "join " + info + " a on s.id = a.smsid\n" +
                "join " + logs + " d on s.id = d.sms_id  \n" +
                "WHERE s.patient_guid = '" + patient_guid + "' and s.is_valid_xml is true and (a.is_contain_confidential_diagnoses is null or a.is_contain_confidential_diagnoses is false) and d.status IN (1) group by a.\"version\") m;");
        while (sql.resultSet.next()) {
            Integer count = Integer.valueOf(sql.resultSet.getString("count"));
            System.out.println("Найдено в таблице - " + count);
            Count += count;
            System.out.println("Суммарное количество - " + Count);
        }
    }

    @Step("Проверка документов РЭМД принятых ФРЭМД")
    public void CountRemdTrue(String patient_guid, String remd) throws SQLException {
        sql = new SQL();
        String Sql = "";
        Sql = "SELECT count(*) FROM " + remd + " s " +
                "join vimis.remdadditionalinfo re on s.id = re.smsid " +
                "join dpc.mis_sp_mu m on s.medicalidmu = m.medicalidmu " +
                "where s.patient_guid = '" + patient_guid + "' and (re.is_contain_confidential_diagnoses is null or re.is_contain_confidential_diagnoses is false) and s.is_valid_xml is true and  s.fremd_status = 1 and m.to_display is true;";

        sql.StartConnection(Sql);
        while (sql.resultSet.next()) {
            Integer count = Integer.valueOf(sql.resultSet.getString("count"));
            System.out.println("Найдено в таблице - " + count);
            Count += count;
            System.out.println("Суммарное количество - " + Count);
        }
    }

    @Step("Проверка документов РЭМД принятых ФРЭМД")
    public Object GetDocs(String patient_guid, String snils, Integer value) throws InterruptedException, IOException {
        sql = new SQL();
        System.out.println(value);

        Thread.sleep(2000);
        Cookie Session = driver.manage().getCookieNamed(".AspNetCore.Session");
        Cookie TelemedC1 = driver.manage().getCookieNamed(".AspNet.Core.TelemedC1");
        Cookie TelemedC2 = driver.manage().getCookieNamed(".AspNet.Core.TelemedC2");
        Cookie Telemed = driver.manage().getCookieNamed(".AspNet.Core.Telemed");

        JsonPath respons = given()
                .filter(new AllureRestAssured())
                .log().uri()
                .queryParam("patientGuid", patient_guid)
                .queryParam("patientSnils", snils)
                .queryParam("sortOrder", "ascending")
                .queryParam("isSuccessStatus", "false")
                .cookie("Session", Session)
                .cookie("TelemedC1", TelemedC1)
                .cookie("TelemedC2", TelemedC2)
                .cookie("Telemed", Telemed)
                .contentType(ContentType.JSON)
                .when()
                .get(HostAddressWeb + "/vimis/sms/patient")
                .prettyPeek()
                .body()
                .jsonPath();

        List<String> list = new ArrayList<>();
        // Сначала очищаем файл, а после записываем
        new FileWriter("src/test/resources/ignored/1501.txt", false).close();

        for (int i = 0; i < value; i++) {
            list.add(respons.getString("result[" + i + "].localUid"));
            FileWriter writer = new FileWriter("src/test/resources/ignored/1501.txt", true);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            if (i == 0) {
                bufferWriter.write(respons.getString("result[" + i + "].localUid"));
            } else {
                bufferWriter.write(", " + respons.getString("result[" + i + "].localUid"));
            }
            bufferWriter.close();
        }
        return list;
    }
}
