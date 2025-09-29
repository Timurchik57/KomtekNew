package api;

import Base.BaseAPI;
import Base.SQL;
import Base.TestListener;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Сохранение значение структурного подразделения из СЭМД")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("Проверка_info")
@Tag("Основные")
public class Access_613Test extends BaseAPI {
    SQL sql;
    public String value613;
    public String department_oid;
    public String DateDeathsAll;

    @Issue(value = "TEL-613")
    @Link(name = "ТМС-1179", url = "https://team-1okm.testit.software/projects/5/tests/1179?isolatedSection=ccb1fcf9-9e3b-44d1-9bad-7959d251a43d")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Сохранение значение структурного подразделения из СЭМД для vmcl = 1")
    @Description("Отправить СЭМД с заполненным полем providerOrganization/id.extension. После перейти в таблицу info и проверить заполнение поля department_oid")
    public void Access_613Vmcl_1() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_613Method(
                "SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "", "vimis.additionalinfo");
    }

    @Test
    @DisplayName("Сохранение значение структурного подразделения из СЭМД для vmcl = 2")
    public void Access_613Vmcl_2() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_613Method(
                "SMS/SMS3.xml", "3", 2, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", "",
                "vimis.preventionadditionalinfo"
        );
    }

    @Test
    @DisplayName("Сохранение значение структурного подразделения из СЭМД для vmcl = 3")
    public void Access_613Vmcl_3() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_613Method(
                "SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", "",
                "vimis.akineoadditionalinfo"
        );
    }

    @Test
    @DisplayName("Сохранение значение структурного подразделения из СЭМД для vmcl = 4")
    public void Access_613Vmcl_4() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_613Method(
                "SMS/SMS3.xml", "3", 4, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "", "vimis.cvdadditionalinfo");
    }

    @Test
    @DisplayName("Сохранение значение структурного подразделения из СЭМД для vmcl = 5")
    public void Access_613Vmcl_5() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_613Method(
                "SMS/SMS3.xml", "3", 5, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.infectionsms", "",
                "vimis.infectionadditionalinfo");
    }

    @Test
    @DisplayName("Сохранение значение структурного подразделения из СЭМД для vmcl = 99")
    public void Access_613Vmcl_99() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_613Method(
                "SMS/SMS3.xml", "3", 99, 1, true, 2, 1, 9, 18, 1, 57, 21, "", "vimis.remd_sent_result",
                "vimis.remdadditionalinfo"
        );
    }

    @Test
    @Issue(value = "TEL-2927")
    @DisplayName("Сохранение даты смерти из СЭМД для смс c id = 13")
    public void Access_2927Id_13() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_613Method(
                "SMS/SMS13-id=13.xml", "13", 1, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.sms", "",
                "vimis.additionalinfo"
        );
    }

    @Test
    @DisplayName("Сохранение даты смерти из СЭМД для смс c id = 14")
    public void Access_2927Id_14() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_613Method(
                "SMS/SMS13-id=14.xml", "14", 1, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.sms", "",
                "vimis.additionalinfo"
        );
    }

    @Test
    @DisplayName("Сохранение даты смерти из СЭМД для смс c id = 21")
    public void Access_2927Id_21() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_613Method(
                "SMS/SMS19-id=21.xml", "21", 3, 1, true, 3, 1, 11, 18, 1, 57, 21, "vimis.akineosms", "",
                "vimis.akineoadditionalinfo"
        );
    }

    @Test
    @DisplayName("Сохранение даты смерти из СЭМД для смс c id = 15")
    public void Access_2927Id_15() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_613Method(
                "SMS/id=15-vmcl=99.xml", "15", 1, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.sms", "",
                "vimis.additionalinfo"
        );
    }

    @Test
    @DisplayName("Сохранение даты смерти из СЭМД для смс c id = 33")
    public void Access_2927Id_33() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_613Method(
                "SMS/id=33-vmcl=1.xml", "33", 1, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.sms", "",
                "vimis.additionalinfo"
        );
    }

    @Test
    @DisplayName("Сохранение даты смерти из СЭМД для смс c id = 34")
    public void Access_2927Id_34() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_613Method(
                "SMS/SMSV19-id=34.xml", "34", 3, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.akineosms", "",
                "vimis.akineoadditionalinfo"
        );
    }

    public void Access_613Method(String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String sms, String remd, String info) throws IOException, InterruptedException, SQLException, XPathExpressionException, ParserConfigurationException, SAXException {
        sql = new SQL();

        System.out.println("Отправляем смс с Doctype = " + DocType + " и vmcl = " + vmcl + "");
        xml.ApiSmd(FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                position1, speciality1);
        if (vmcl == 99) {
            sql.StartConnection(
                    "Select * from " + remd + " where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                value613 = sql.resultSet.getString("id");
                System.out.println(value613);
            }
        } else {
            sql.StartConnection(
                    "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                value613 = sql.resultSet.getString("id");
                System.out.println(value613);
            }
        }

        if (FileName != "SMS/SMS3.xml") {
            DateDeathsAll = DateDeath(FileName);
        }

        System.out.println("Проверяем добавление значений в таблице " + info + "");
        sql.StartConnection("Select * from " + info + " where smsid = '" + value613 + "';");
        while (sql.resultSet.next()) {
            department_oid = sql.resultSet.getString("department_oid");
            sql.value = sql.resultSet.getString("deathdate");
        }

        if (FileName != "SMS/SMS3.xml") {
            /** Проверяем дату смерти в XML (2927) */
            Assertions.assertEquals(DateDeathsAll, sql.value.substring(0, 10), "Дата смерти не совпадает");
        } else {
            Assertions.assertEquals(department_oid, "1.2.643.5.1.13.13.12.2.77.8312.0.166444",
                    "Значение в поле department_oid не совпадаетс с providerOrganization/id.extension");
        }
    }

    public String DateDeath (String FileName) throws XPathExpressionException, ParserConfigurationException, IOException, SAXException {
        /** Определяем дату смерти в XML (2927) */
        String DateDeaths = getXml(FileName,
                "//code[@displayName='Дата и время смерти']/following-sibling::value/@value");
        String year = DateDeaths.substring(0, 4);
        String day = DateDeaths.substring(0, DateDeaths.length() - 9).substring(6);
        String month = DateDeaths.substring(0, DateDeaths.length() - 11).substring(4);
        DateDeathsAll = year + "-" + month + "-" + day;
        return DateDeathsAll;
    }
}
