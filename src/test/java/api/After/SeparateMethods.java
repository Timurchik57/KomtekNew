package api.After;

import Base.BaseAPI;
import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationUnfinished;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.Directions.Kvots.EquipmentSchedule;
import api.Before.Authorization;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import ru.testit.client.invoker.ApiClient;
import ru.testit.client.invoker.ApiException;
import ru.testit.client.invoker.Configuration;
import ru.testit.client.invoker.auth.*;
import ru.testit.client.api.AutoTestsApi;
import ru.testit.client.model.AutoTestModel;
import ru.testit.client.model.AutoTestPostModel;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

@Epic("Тесты UI")
@ExtendWith(TestListener.class)
@Feature("Запуск отдельных методов руками")
@Disabled
public class SeparateMethods extends BaseAPI {


    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://192.168.1.109");

        // Configure API key authorization: Bearer or PrivateToken
        ApiKeyAuth PrivateToken = (ApiKeyAuth) defaultClient.getAuthentication("Bearer or PrivateToken");
        PrivateToken.setApiKey("PrivateToken aWt6NkJpVkZpVTRiZW5paTlQ");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //Bearer or PrivateToken.setApiKeyPrefix("Token");

        AutoTestsApi apiInstance = new AutoTestsApi(defaultClient);
        AutoTestPostModel autoTestPostModel = new AutoTestPostModel(); // AutoTestPostModel |
        try {
            AutoTestModel result = apiInstance.createAutoTest(autoTestPostModel);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AutoTestsApi#createAutoTest");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Создание направления на диагностику без отправки")
    public void DirectionFalse() throws SQLException, InterruptedException {
        DirectionsForQuotas directionsForQuotas = new DirectionsForQuotas(driver);
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);
        EquipmentSchedule equipmentSchedule = new EquipmentSchedule(driver);

        String str[] = {"HMP01", "Компьютерная томография органов брюшной полости", "Магнитно-резонансная томография головного мозга"};
        /**
         * БУ ХМАО-Югры "Окружная клиническая больница - Gyroscan ACS-NT томограф тест МосмедИИ, Vivid 7"
         * БУ ХМАО-Югры "Нефтеюганская окружная клиническая больница имени В.И. Яцкив - X-OMAT"
         * АУ ХМАО-Югры "Кондинская районная стоматологическая поликлиника - CDR Kit"
         * */

        String id_direction = null;

        AuthorizationMethod(authorizationObject.OKB);
        directionsForQuotas.CreateConsultationEquipment(true, "159 790 257 20",
                authorizationObject.Select("Женская консультация"), "Аорта", false, "БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив",
                authorizationObject.Select("A00.0 Холера, вызванная холерным вибрионом 01, биовар cholerae"),
                authorizationObject.Select("HMP01"), "1", false, true, false, "");

        Thread.sleep(1500);
        sql.StartConnection("select * from telmed.directions order by id desc limit 1;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("directionguid");
            id_direction = sql.resultSet.getString("id");
        }
        System.out.println(sql.value);
        System.out.println(id_direction);
    }

    @Test
    @DisplayName("Создание направления на диагностику с отправкой")
    public void Direction() throws SQLException, InterruptedException {
        DirectionsForQuotas directionsForQuotas = new DirectionsForQuotas(driver);
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);
        EquipmentSchedule equipmentSchedule = new EquipmentSchedule(driver);

        String id_direction = null;

        AuthorizationMethod(authorizationObject.OKB);
        directionsForQuotas.CreateConsultationEquipment(true, "159 790 257 20",
                authorizationObject.Select("Женская консультация"), "Аорта", true, "",
                authorizationObject.Select("A00.0 Холера, вызванная холерным вибрионом 01, биовар cholerae"),
                authorizationObject.Select("Маммография"), "1", false, true, true, "Gyroscan ACS-NT томограф тест МосмедИИ");

        Thread.sleep(2500);
        sql.StartConnection("select * from telmed.directions order by id desc limit 1;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("directionguid");
            id_direction = sql.resultSet.getString("id");
        }
        System.out.println(sql.value);

       // sql.UpdateConnection("update telmed.cami_links set accession_number = '2014453' WHERE id_direction  = '"+id_direction+"';");
    }

    @Test
    @DisplayName("Создание направления на диагностику с отправкой через api метод")
    public void DirectionApi() throws SQLException, InterruptedException, IOException {
        DirectionsForQuotas directionsForQuotas = new DirectionsForQuotas(driver);
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);
        EquipmentSchedule equipmentSchedule = new EquipmentSchedule(driver);
        Authorization authorization = new Authorization();

        boolean otpr = false;

        /**
         * БУ ХМАО-Югры "Окружная клиническая больница - Gyroscan ACS-NT томограф тест МосмедИИ, Vivid 7"
         * БУ ХМАО-Югры "Нефтеюганская окружная клиническая больница имени В.И. Яцкив - X-OMAT"
         * АУ ХМАО-Югры "Кондинская районная стоматологическая поликлиника - CDR Kit"
         * 1.2.643.5.1.13.13.12.2.86.8902
         * 1.2.643.5.1.13.13.12.2.86.9003
         * */
        Token = authorization.AuthorizationsAdd("1.2.643.5.1.13.13.12.2.86.8902", "21");

        xml.changes.put("$.TargetMedicalOid", "1.2.643.5.1.13.13.12.2.86.8902");
        xml.changes.put("$.ResearchCode", "HMP01");
        xml.changes.put("$.PatientGuid", PatientGuid);
        String modifiedJson = JsonMethod("SMS/Body/api_diagnostic.json", xml.changes, false, null);
        Api(HostAddress + "/api/diagnostic", "post", null, null, modifiedJson, 200, false);
        String id = Response.getString("Result.DirectionId");
        System.out.println(id);

        if (otpr) {
            AuthorizationMethod(authorizationObject.OKB);
            ClickElement(directionsForQuotas.ConsultationWait);
            ClickElement(directionsForQuotas.SortDesc);
            Thread.sleep(1500);
            authorizationObject.LoadingTime(20);
            ClickElement(directionsForQuotas.GetDirection(id, "1"));
            System.out.println("Прикрепление  файла");
            ClickElement(directionsForQuotas.Addfiles);
            Thread.sleep(1000);
            java.io.File file = new File("src/test/resources/test.txt");
            directionsForQuotas.File.sendKeys(file.getAbsolutePath());
            Thread.sleep(500);
            ClickElement(directionsForQuotas.Clouses);
            ClickElement(directionsForQuotas.AddReception);
            Thread.sleep(1500);
            authorizationObject.LoadingTime(40);
            equipmentSchedule.CheckDirection("X-OMAT");
            ClickElement(equipmentSchedule.SlotsFreeWait);
            if (isElementNotVisible(equipmentSchedule.AddFileWait)) {
                file = new File("src/test/resources/test.txt");
                equipmentSchedule.AddFile.sendKeys(file.getAbsolutePath());
                Thread.sleep(1500);
            }
            ClickElement(equipmentSchedule.WriteTwo);
            Thread.sleep(1500);
            authorizationObject.LoadingTime(30);
            ClickElement(equipmentSchedule.AlertCloseWait);
            System.out.println("Запись на прием успешна создана!");
        }

//        Thread.sleep(2500);
//        sql.StartConnection("select * from telmed.directions order by id desc limit 1;");
//        while (sql.resultSet.next()) {
//            sql.value = sql.resultSet.getString("directionguid");
//            id_direction = sql.resultSet.getString("id");
//        }
    }

    @Test
    @DisplayName("Создание консультаций и завешение их")
    public void Consultation() throws SQLException, InterruptedException, IOException {
        DirectionsForQuotas directionsForQuotas = new DirectionsForQuotas(driver);
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);
        ConsultationUnfinished consultationUnfinished = new ConsultationUnfinished(driver);
        String uuid = null;

        AuthorizationMethod(authorizationObject.OKB);
        for (int i = 0; i < 1; i++) {
            ClickElement(directionsForQuotas.ConsultationWait);
            ClickElement(directionsForQuotas.CreateWait);
            if (i == 0) {
                directionsForQuotas.CreateRemoteConsul(true, "БУ ХМАО-Югры \"Окружная клиническая больница\"",
                        "159 790 257 20", PName, "Женская консультация",
                        "детской урологии-андрологии", "плановая", "Подозрение на COVID-19", "Паратиф A");
            } else {
                directionsForQuotas.CreateRemoteConsul(true, "БУ ХМАО-Югры \"Окружная клиническая больница\"",
                        "159 790 257 20", PName, "Женская консультация", "гематологии", "плановая",
                        "Подозрение на COVID-19", "Паратиф A");
            }
           // ClickElement(directionsForQuotas.SendConsul);
            ClickElement(directionsForQuotas.CloseReception);

            sql.StartConnection("Select * from telmed.directions order by id desc limit 1");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
            }
            System.out.println(sql.value);

//            AuthorizationMethod(authorizationObject.OKB);
//            ClickElement(consultationUnfinished.UnfinishedWait);
//            ClickElement(consultationUnfinished.DESK);
//            Thread.sleep(1500);
//            ClickElement(consultationUnfinished.Consuls(sql.value));
//            ClickElement(consultationUnfinished.Closed);
//            WaitElement(consultationUnfinished.ClosedText);
//            uuid = String.valueOf(UUID.randomUUID());
//            InputPropFile("Test_1219_" + i + "_uuid", uuid);
//            System.out.println(uuid);
//            inputWord(driver.findElement(consultationUnfinished.ClosedText), uuid + "j");
//            SelectClickMethod(consultationUnfinished.DataDay, consultationUnfinished.NextMonth);
//            ClickElement(consultationUnfinished.Closed2);
//            Thread.sleep(3000);
        }
    }

    @Test
    public void qwee() throws SQLException, IOException, InterruptedException {
        AddXml("SMS/SMS3.xml", "3", 1, 3, 1, 9, 18, 1, 57, 21);
    }

    @Step("Метод для отправки 2 версии документа ВИМИС")
    public void AddXml(String FileName, String DocType, Integer vmcl, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1) throws IOException, SQLException, InterruptedException {
        TableVmcl(vmcl);

        xml.ApiSmd(FileName, DocType, vmcl, 1, true, docTypeVersion, Role, position, speciality, Role1, position1,
                speciality1);
        xml.ReplacementWordInFileBack(FileName);
        CollbekVimis("" + xml.uuid + "", "0", "Проверка 3692", smsBase, vmcl);
        xml.ApiSmd("SMS/SMS3_1.xml", DocType, vmcl, 0, false, docTypeVersion, Role, position, speciality, Role1,
                position1, speciality1);
        xml.ReplacementWordInFileBack("SMS/SMS3_1.xml");
    }

    @Test
    @Step("Метод подстановки значений для проверки данных подписи Access_1256")
    public void addSignature() throws SQLException, IOException, InterruptedException {
        System.out.println("Сверяем тело запроса в РЭМД");

        String localuid = "edce964a-b31b-4cc1-99de-e428dfc0e63a";

        Token = TokenInit();
        JsonPath response = given()
                .filter(new AllureRestAssured())
                .header("Authorization", "Bearer " + Token)
                .param("LocalUid", localuid)
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .get(ApiVimis + "/api/rremd")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract().jsonPath();

        String PereResult = "";
        PereResult = "result[0]";

        assertThat(response.get("" + PereResult + "." + DocumentDto + "localUid"), equalTo("" + localuid + ""));

//        /** Узнаем формат отправки протокола (2446))*/
//        if (GetSettings().get("result.formatForTelemedProtocol") == Integer.valueOf("1")) {
//            assertThat(response.get("" + PereResult + "." + DocumentDto + "kind.code"), equalTo("8"));
//        } else {
//            assertThat(response.get("" + PereResult + "." + DocumentDto + "kind.code"), equalTo("40"));
//        }
        assertThat(response.get("" + PereResult + "." + DocumentDto + "patient.surname"), equalTo("Тестировщик"));
        assertThat(response.get("" + PereResult + "." + DocumentDto + "patient.name"), equalTo("Тест"));
        assertThat(response.get("" + PereResult + "." + DocumentDto + "patient.patrName"), equalTo("Тестович"));
        assertThat(response.get("" + PereResult + "." + DocumentDto + "patient.snils"), equalTo("15979025720"));
        String orgSignatureSum = response.get("" + PereResult + "." + DocumentDto + "orgSignature.checksum");
        String personalSignaturesSum = response.get(
                "" + PereResult + "." + DocumentDto + "personalSignatures[0].signature.checksum");

        sql.StartConnection("select * from vimis.certificate_remd_sent_result where local_uid = '" + localuid + "'");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("org_signature_id");
        }

        InputPropFile("Test_1219_org_signature_id_0", sql.value);
        InputPropFile("Test_1219_orgSignatureSum_0", orgSignatureSum);
        InputPropFile("Test_1219_personalSignaturesSum_0", personalSignaturesSum);
    }

    @Test
    @Step("Метод для проверки нужных данных в XML")
    public void GetXML () {
        String pathFile = "SMS/file.xml";
       // String foetus_numberXml = getXml2(pathFile, "//code[@codeSystem='1.2.643.5.1.13.13.11.1070']/@code", "//code[@codeSystem='1.2.643.5.1.13.13.11.1070']/@displayName");
        setXml(pathFile, "//code[@codeSystem='1.2.643.5.1.13.13.11.1070']/@code", "B01.015.001");
        setXml(pathFile, "//code[@codeSystem='1.2.643.5.1.13.13.11.1070']/@displayName", "Прием (осмотр, консультация) врача-кардиолога первичный");
    }

    @Test
    @Step("Запись XML в файл")
    public void PrintXMLFile () throws IOException {
        /** Проверяем заполнение данных автора в документе (2741) */
        Token = TokenInit();
        JsonPath responses = given()
                .filter(new AllureRestAssured())
                .header("Authorization", "Bearer " + Token)
                .param("localUid", "8daf3e6c-3ec7-4dc6-938a-be4e164587c4")
                .contentType(ContentType.JSON)
                .when()
                .get(HostAddress + "/api/smd/document")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract().jsonPath();
        String Base64 = responses.getString("result[0].document");
        byte[] decoded = java.util.Base64.getDecoder().decode(Base64);
        String xml = new String(decoded, StandardCharsets.UTF_8);

        System.out.println("Запсываем полученный xml в файл");
        PrintWriter out = new PrintWriter("src/test/resources/ignored/1219_test.xml");
        out.println(xml);
        out.close();
    }

    /**
     * Метод для поиска текста в XML
     */
    @Step("Метод поиска текста в XML с 2 значениями")
    public String getXml2(String File, String path, String path2) {
        String str = null;
        String bd = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(File);
            List<String> list = new ArrayList<>();
            List<String> list2 = new ArrayList<>();

            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList nl = (NodeList) xpath.evaluate(path, doc, XPathConstants.NODESET);
            NodeList nl2 = (NodeList) xpath.evaluate(path2, doc, XPathConstants.NODESET);
            for (int i = 0; i < nl.getLength(); i++) {
               // System.out.println(nl.item(i).getNodeValue());
                str = nl.item(i).getNodeValue();
                list.add(i, str);
            }

            for (int q = 0; q < nl2.getLength(); q++) {
               // System.out.println(nl2.item(q).getNodeValue());
                str = nl2.item(q).getNodeValue();
                list2.add(q, str);
            }

            for (int w = 0; w < nl.getLength(); w++) {
                System.out.println(list.get(w) + " - " + list2.get(w));
            }

            System.out.println("Значения из БД");
            for (int t = 0; t < nl.getLength(); t++) {
                sql.PrintSQL = false;
                sql.StartConnection("Select * from dpc.medical_services x WHERE s_code = '"+list.get(t)+"'");
                while(sql.resultSet.next()){
                    bd = sql.resultSet.getString("name");
                }
                System.out.println(list.get(t) + " - " + bd);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Ошибочка - " + e.getMessage());
        }
        return str;
    }


}
