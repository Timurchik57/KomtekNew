package api;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Проверка метода api/smd с параметром status")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
@Tag("Поиск_api/smd")
@Tag("Основные")
public class Access_1595Test extends BaseAPI {
    public String URLRemd;
    public String TransferId;
    String params[];

    @Issue(value = "TEL-1595")
    @Issue(value = "TEL-3389")
    @Link(name = "ТМС-1568", url = "https://team-1okm.testit.software/projects/5/tests/1568?isolatedSection=f07b5d61-7df7-4e90-9661-3fd312065912")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка метода api/smd с параметром status для vmcl = 1")
    @Description("Отправляем смс для ВИМИС добавляем статус в таблицы logs, для РЭМД в таблицы remd, после ищем данные смс с дополнителным параметром status")
    public void Access_1595Vmcl_1() throws IOException, SQLException, InterruptedException {
        Access_1595Method("SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_onko_sent_result");
    }

    @Test
    @DisplayName("Проверка метода api/smd с параметром status для vmcl = 2")
    public void Access_1595Vmcl_2() throws IOException, SQLException, InterruptedException {
        Access_1595Method("SMS/SMS3.xml", "3", 2, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result");
    }

    @Test
    @DisplayName("Проверка метода api/smd с параметром status для vmcl = 3")
    public void Access_1595Vmcl_3() throws IOException, SQLException, InterruptedException {
        Access_1595Method("SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result");
    }

    @Test
    @DisplayName("Проверка метода api/smd с параметром status для vmcl = 4")
    public void Access_1595Vmcl_4() throws IOException, SQLException, InterruptedException {
        Access_1595Method("SMS/SMS3.xml", "3", 4, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "vimis.cvdlogs",
                "vimis.remd_cvd_sent_result");
    }

    @Test
    @DisplayName("Проверка метода api/smd с параметром status для vmcl = 5")
    public void Access_1595Vmcl_5() throws IOException, SQLException, InterruptedException {
        Access_1595Method("SMS/SMS3.xml", "3", 5, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.infectionsms", "vimis.infectionlogs",
                "vimis.remd_infection_sent_result");
    }

    @Test
    @DisplayName("Проверка метода api/smd с параметром status для vmcl = 99")
    public void Access_1595Vmcl_99() throws IOException, SQLException, InterruptedException {
        Access_1595Method("SMS/SMS3.xml", "3", 99, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.remd_sent_result", "", "vimis.remd_sent_result");
    }

    @Step("Метод для смены статуса смс и проверки по статусам spi/smd statusvimis и statusremd")
    public void Access_1595Method(String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1, String sms, String logs, String remd) throws IOException, InterruptedException, SQLException {

        System.out.println("Отправляем запрос смс 3 с vmlc = " + vmcl + "");
        xml.ApiSmd(FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1, position1, speciality1);

        sql.StartConnection("Select * from " + sms + " where local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
            TransferId = sql.resultSet.getString("transfer_id");
            System.out.println(sql.value);
        }
        xml.ReplacementWordInFileBack(FileName);

        if (vmcl != 99) {
            /** Проверки идут в следующей последовательности
             * statusvimis = 1
             * statusvimis = 0
             * statusremd = 1
             * statusremd = 0
             * statusvimis = 1 и statusremd = 0
             * statusvimis = 1 и statusremd = 1
             * statusvimis = 0 и statusremd = 0 */
            System.out.println("\n 1 проверка - statusvimis = 0, statusremd = null");
            CollbekVimis("" + xml.uuid + "", "0", "Проверка 3389", sms, vmcl);
            AssertMethod(false, true, false, false, false, false, false);

            System.out.println("\n 2 проверка - statusvimis = 1, statusremd = null");
            xml.ApiSmd(FileName, DocType, vmcl, 2, RanLoc, docTypeVersion, Role, position, speciality, Role1, position1, speciality1);

            sql.StartConnection("Select * from " + sms + " where local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                TransferId = sql.resultSet.getString("transfer_id");
                System.out.println(sql.value);
            }
            CollbekVimis("" + xml.uuid + "", "1", "Проверка 3389", sms, vmcl);
            WaitStatusKremd(remd, "" + xml.uuid + "");
            AssertMethod(true, false, false, false, false, false, false);

            System.out.println("\n 3 проверка - statusvimis = 1, statusremd = 0");
            CollbekKremd("" + xml.uuid + "", "error", "Проверка 3389", remd);
            AssertMethod(true, false, false, true, true, false, false);

            System.out.println("\n 4 проверка - statusvimis = 1, statusremd = 1");
            CollbekKremd("" + xml.uuid + "", "success", "Проверка 3389", remd);
            AssertMethod(true, false, true, false, false, true, false);

        } else {
            System.out.println("\n 5 проверка - statusremd = null для vmcl = 99");
            WaitStatusKremd(remd, "" + xml.uuid + "");
            AssertMethod(false, false, false, false, false, false, false);

            System.out.println("\n 6 проверка - statusremd = 0 для vmcl = 99");
            CollbekKremd("" + xml.uuid + "", "error", "Проверка 3389", remd);
            AssertMethod(false, false, false, true, false, false, false);

            System.out.println("\n 7 проверка - statusremd = 1 для vmcl = 99");
            CollbekKremd("" + xml.uuid + "", "success", "Проверка 3389", remd);
            AssertMethod(false, false, true, false, false, false, false);
        }
    }

    /** Проверки идут в следующей последовательности
     * statusvimis = 1
     * statusvimis = 0
     * statusremd = 1
     * statusremd = 0
     * statusvimis = 1 и statusremd = 0
     * statusvimis = 1 и statusremd = 1
     * statusvimis = 0 и statusremd = 0 */
    @Step("Проверки api/smd при разных статусах statusvimis и statusremd")
    public void AssertMethod (boolean value1, boolean value2, boolean value3, boolean value4, boolean value5, boolean value6, boolean value7) throws IOException {

        System.out.println("statusvimis = 1");
        params = new String[]{
                "transferId", TransferId,
                "statusvimis", "1"};
        AssertBoolean(value1);

        System.out.println("statusvimis = 0");
        params = new String[]{
                "transferId", TransferId,
                "statusvimis", "0"};
        AssertBoolean(value2);

        System.out.println("statusremd = 1");
        params = new String[]{
                "transferId", TransferId,
                "statusremd", "1"};
        AssertBoolean(value3);

        System.out.println("statusremd = 0");
        params = new String[]{
                "transferId", TransferId,
                "statusremd", "0"};
        AssertBoolean(value4);

        System.out.println("statusvimis = 1 statusremd = 0");
        params = new String[]{
                "transferId", TransferId,
                "statusremd", "0",
                "statusvimis", "1"};
        AssertBoolean(value5);

        System.out.println("statusvimis = 1 statusremd = 1");
        params = new String[]{
                "transferId", TransferId,
                "statusremd", "1",
                "statusvimis", "1"};
        AssertBoolean(value6);

        System.out.println("statusvimis = 0 statusremd = 1");
        params = new String[]{
                "transferId", TransferId,
                "statusremd", "1",
                "statusvimis", "0"};
        AssertBoolean(value7);
    }

    @Step("Проверки тела ответа на api/smd")
    public void AssertBoolean (boolean error) throws IOException {
        if (error) {
            Api(HostAddress + "/api/smd/", "get", params, null, "", 200, true);
            Assertions.assertEquals(Response.get("result[0].localUid"), "" + xml.uuid + "", "localUid не совпадет");
        } else {
            Api(HostAddress + "/api/smd/", "get", params, null, "", 400, true);
            Assertions.assertEquals(Response.get("errorMessage"), "СМС с указанными параметрами не найдены", "Не выходит ошибка 'СМС с указанными параметрами не найдены'");
        }
    }
}
