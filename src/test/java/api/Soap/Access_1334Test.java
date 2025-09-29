package api.Soap;

import Base.TestListener;
import api.Access_Notification.Access_1318_Type10_Test;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Запрос статуса Soap запроса")
@Tag("Soap")
@Tag("Проверка_БД")
public class Access_1334Test extends BaseAPI {
    Access_1318_Type10_Test access_1318Type10Test;

    @Issue(value = "TEL-1334")
    @Link(name = "ТМС-1496", url = "https://team-1okm.testit.software/projects/5/tests/1496?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Запрос статуса Soap запроса vmcl = 1")
    @Description("Отправляем смс soap запросом, проверить добавление в таблицы. Отправить запрос статуса, проверить ответ")
    public void Access_1334() throws IOException, SQLException, InterruptedException {
        if (KingNumber == 2 | KingNumber == 3) {
            System.out.println("Создаём подпись для Soap запроса");
            SoapSignatureMethod("SMS/SMS5.xml", SoapSystemId, 1, "5", 3);

            System.out.println("Отправляем Soap запрос");
            SoapBodyMethod("5", 1, 3, "vimis.sms");
            methodCheckSoap("vimis.documentlogs");
        }
    }

    @Test
    @DisplayName("Запрос статуса Soap запроса vmcl = 2")
    public void Access_1334_2() throws IOException, SQLException, InterruptedException {
        if (KingNumber == 2 | KingNumber == 3) {
            System.out.println("Создаём подпись для Soap запроса");
            SoapSignatureMethod("SMS/SMS5-vmcl=2.xml", SoapSystemId, 2, "5", 3);
            System.out.println("Отправляем Soap запрос");
            SoapBodyMethod("5", 2, 3, "vimis.preventionsms");

            methodCheckSoap("vimis.preventionlogs");
        }
    }

    @Test
    @DisplayName("Запрос статуса Soap запроса vmcl = 3")
    public void Access_1334_3() throws IOException, SQLException, InterruptedException {
        if (KingNumber == 2 | KingNumber == 3) {
            System.out.println("Создаём подпись для Soap запроса");
            SoapSignatureMethod("SMS/SMS5-vmcl=3(v1.2).xml", SoapSystemId, 3, "5", 2);
            System.out.println("Отправляем Soap запрос");
            SoapBodyMethod("5", 3, 2, "vimis.akineosms");

            methodCheckSoap("vimis.akineologs");
        }
    }

    @Test
    @DisplayName("Запрос статуса Soap запроса vmcl = 4")
    public void Access_1334_4() throws IOException, SQLException, InterruptedException {
        if (KingNumber == 2 | KingNumber == 3) {
            System.out.println("Создаём подпись для Soap запроса");
            SoapSignatureMethod("SMS/SMS5-vmcl=4.xml", SoapSystemId, 4, "6", 2);
            System.out.println("Отправляем Soap запрос");
            SoapBodyMethod("6", 4, 2, "vimis.cvdsms");

            methodCheckSoap("vimis.cvdlogs");
        }
    }

    @Test
    @DisplayName("Запрос статуса Soap запроса vmcl = 5")
    public void Access_1334_5() throws IOException, SQLException, InterruptedException {
        if (KingNumber == 2 | KingNumber == 3) {
            System.out.println("Создаём подпись для Soap запроса");
            SoapSignatureMethod("SMS/SMS5-vmcl=4.xml", SoapSystemId, 5, "5", 3);
            System.out.println("Отправляем Soap запрос");
            SoapBodyMethod("5", 5, 3, "vimis.infectionsms");

            methodCheckSoap("vimis.infectionlogs");
        }
    }

    @Step("Метод смены статуса и проверка статуса Soap запроса")
    public void methodCheckSoap(String logs) throws SQLException, IOException {
        System.out.println("Отправляем запрос статуса");
        String body = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:a=\"http://www.w3.org/2005/08/addressing\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
                "    <s:Header>\n" +
                "        <a:Action wsu:Id=\"id-action\">checkStatus</a:Action>\n" +
                "        <transportHeader xmlns=\"http://egisz.rosminzdrav.ru\">\n" +
                "            <authInfo>\n" +
                "                <clientEntityId>19723f16-2f55-a5d0-9a9d-593199a22ef3</clientEntityId>\n" +
                "            </authInfo>\n" +
                "        </transportHeader>\n" +
                "        <a:MessageID>43afe69d-b21a-428b-a624-f113f95ec743</a:MessageID>\n" +
                "\t\t<a:ReplyTo wsu:Id=\"id-replyto\">\n" +
                "\t\t\t<a:Address>https://ips.rosminzdrav.ru/79f5771afb36b</a:Address>\n" +
                "\t\t</a:ReplyTo>\n" +
                "\t\t<a:FaultTo>\n" +
                "\t\t\t<a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address>\n" +
                "\t\t</a:FaultTo>\n" +
                "\t\t<a:To>https://ips.rosminzdrav.ru/8b02e1e4e03c7</a:To>\n" +
                "        <wsse:Security>\n" +
                "            <ds:Signature Id=\"SIG-a450f89e-67f4-497a-8f28-c11fad05e0d2\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
                "                <SignedInfo xmlns=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
                "                    <CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />\n" +
                "                    <SignatureMethod Algorithm=\"urn:ietf:params:xml:ns:cpxmlsec:algorithms:gostr34102012-gostr34112012-256\" />\n" +
                "                    <Reference URI=\"#body\">\n" +
                "                        <Transforms>\n" +
                "                            <Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\" />\n" +
                "                            <Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" />\n" +
                "                        </Transforms>\n" +
                "                        <DigestMethod Algorithm=\"urn:ietf:params:xml:ns:cpxmlsec:algorithms:gostr34112012-256\" />\n" +
                "                        <DigestValue>ITBagGDt7tSxjhgxGOng+Sh7Lhu4pwpKvwrZ800jjek=</DigestValue>\n" +
                "                    </Reference>\n" +
                "                </SignedInfo>\n" +
                "                <SignatureValue xmlns=\"http://www.w3.org/2000/09/xmldsig#\">tW+AJcFAaeq8vJ8YtFu//jZq9sUVKDCem3GC6VFwuhtX1W9BI++/o8b0It7zfWTOmcDS3z+KGYg17bs07f5JgA==</SignatureValue>\n" +
                "                <ds:KeyInfo Id=\"KI-88624081-8512-4a77-9526-040f5b8252db\">\n" +
                "                    <wsse:SecurityTokenReference Id=\"STR-721f13eb-32f4-42ca-961b-bc888b76519c\">\n" +
                "                        <wsse:Reference ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" URI=\"#565bbb1d-dd7f-41fa-bb94-591631ce1fb9\" />\n" +
                "                    </wsse:SecurityTokenReference>\n" +
                "                </ds:KeyInfo>\n" +
                "            </ds:Signature>\n" +
                "            <wsse:BinarySecurityToken EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" wsu:Id=\"565bbb1d-dd7f-41fa-bb94-591631ce1fb9\">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</wsse:BinarySecurityToken>\n" +
                "        </wsse:Security>\n" +
                "    </s:Header>\n" +
                "    <s:Body xmlns:d2p1=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" d2p1:Id=\"body\">\n" +
                "        <checkStatus xmlns=\"http://vimis.rosminzdrav.ru/\">\n" +
                "            <msg_id xmlns=\"\">" + local_uidSoap + "</msg_id>\n" +
                "        </checkStatus>\n" +
                "    </s:Body>\n" +
                "</s:Envelope>";


        Api(HostSoap, "post", null, null, body, 200, false);
        String xml = Responses.getBody().asString();
        System.out.println("Запишем ответ");
        System.out.println(xml);
        Assertions.assertTrue(xml.contains("<status xmlns=\"\">2</status>"), "Статус не сменился на 2");
        Assertions.assertTrue(xml.contains(
                "<description xmlns=\"\">Документ находится в обработке</description>"), "Статус не сменился на - Документ находится в обработке");

        System.out.println("Устанавливаем status = 1 в vimis.documentlogs");
        sql.UpdateConnection(
                "insert into " + logs + " (create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'Статус сменился по заявке 1334', '" + IdSoap + "', '" + UUID.randomUUID() + "')");

        Api(HostSoap, "post", null, null, body, 200, false);
        xml = Responses.getBody().asString();
        Assertions.assertTrue(
                xml.contains("<status xmlns=\"\">1</status>"),
                "Статус не сменился на 1"
        );
        Assertions.assertTrue(
                xml.contains("<description xmlns=\"\">Статус сменился по заявке 1334</description>"),
                "Статус не сменился на - Статус сменился по заявке 1334");
    }
}
