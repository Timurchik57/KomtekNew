package UI.TmTest.AccessUI.VIMIS.Report;

import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.VIMIS.Report;
import UI.TmTest.PageObject.VIMIS.SMS;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Вимис")
@Tag("СМС")
@Tag("Проверка_БД")
@Tag("api/smd")
public class VimisReportFilterTest extends BaseAPI {

    AuthorizationObject authorizationObject;
    Report vimisReport;
    SMS sms;

    @Issue(value = "TEL-833")
    @Link(name = "ТМС-1265", url = "https://team-1okm.testit.software/projects/5/tests/1265?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @Description("Авторизация и переход в Вимис - Структурированные медицинские сведения. Проверяем корректное отображение, за выбранный период, применяем фильтр по принятым, проверяем соответствие с БД")
    @DisplayName("Проверка отображения принятых документов в соответствии с бд для vmcl =1")
    @Test
    @Order(1)
    @Story("Структурированные медицинские сведения")
    public void VimisReportAcceptedVmcl_1() throws InterruptedException, SQLException, IOException {
        AddRole(PRole, "Доступ до аналитики ВИМИС в рамках разработчика МИС", false);
        VimisReportAccepteAddMethod(1, "Онкология", 3);
    }

    @DisplayName("Проверка отображения принятых документов в соответствии с бд для vmcl =2")
    @Test
    @Story("Структурированные медицинские сведения")
    public void VimisReportAcceptedVmcl_2() throws InterruptedException, SQLException, IOException {
        VimisReportAccepteAddMethod(2, "Профилактика", 3);
    }

    @DisplayName("Проверка отображения принятых документов в соответствии с бд для vmcl =3")
    @Test
    @Story("Структурированные медицинские сведения")
    public void VimisReportAcceptedVmcl_3() throws InterruptedException, SQLException, IOException {
        VimisReportAccepteAddMethod(3, "Акушерство и неонатология", 2);
    }

    @DisplayName("Проверка отображения принятых документов в соответствии с бд для vmcl =4")
    @Test
    @Story("Структурированные медицинские сведения")
    public void VimisReportAcceptedVmcl_4() throws InterruptedException, SQLException, IOException {
        VimisReportAccepteAddMethod(4, "Сердечно-сосудистые заболевания", 2);
    }

    @DisplayName("Проверка отображения принятых документов в соответствии с бд для vmcl =5")
    @Test
    @Story("Структурированные медицинские сведения")
    public void VimisReportAcceptedVmcl_5() throws InterruptedException, SQLException, IOException {
        VimisReportAccepteAddMethod(5, "Инфекционные болезни", 3);
    }

    @DisplayName("Проверка отображения принятых документов в соответствии с бд для vmcl =99")
    @Test
    @Story("Структурированные медицинские сведения")
    public void VimisReportAcceptedVmcl_99() throws InterruptedException, SQLException, IOException {
        VimisReportAccepteAddMethod(99, "Иные профили", 0);
    }

    @Step("Отправялем СМС с vmcl = {0} и устанавливаем статус 1 в таблице {2}")
    private void VimisReportAccepteMethod(Integer vmcl, Integer doctype) throws IOException, InterruptedException, SQLException {

        String Value = "";
        /** Узнаём текущую дату */
        GetDate();

        System.out.println("Отправляем смс3 для vmcl = " + vmcl + "");
        xml.ApiSmd("SMS/SMS3.xml", "3", vmcl, 1, true, doctype, 1, 9, 18, 1, 57, 21);
        sql.StartConnection(
                "Select * from " + smsBase + " where  local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            Value = sql.resultSet.getString("id");
            System.out.println(Value);
        }
        if (vmcl != 99) {
            sql.UpdateConnection(
                    "insert into " + logsBase + " (create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', '" + Value + "', '" + UUID.randomUUID() + "')");
        } else {
            CollbekKremd("" + xml.uuid + "", "success", "Проверка 1444", remdBase);
        }
    }

    @Step("Выбираем направление {3} берём значения на ui и сравниваем с БД")
    private void VimisReportAccepteAddMethod(Integer vmcl, String Name, Integer doctype) throws SQLException, IOException, InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        vimisReport = new Report(driver);
        sms = new SMS(driver);

        TableVmcl(vmcl);

        VimisReportAccepteMethod(vmcl, doctype);
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(sms.SMSWait);
        ClickElement(sms.SMSWait);

        System.out.println("Выбор Направления - " + Name + "");
        WaitElement(sms.DistrictWait);
        ClickElement(sms.Direction(Name));
        Thread.sleep(1000);

        System.out.println("Выбор Фильтров");
        sms.Filter.click();
        WaitElement(sms.FilterWait);

        System.out.println("Ввод периода");
        SelectClickMethod(sms.BeforeTimeWait, sms.DataToDay);
        SelectClickMethod(sms.AfterTimeWait, sms.DataToDay);
        System.out.println("Принятые");
        ClickElement(sms.Accepted);

        System.out.println("Поиск");
        sms.Search.click();
        Thread.sleep(1000);
        if (KingNumber == 4) {
            Thread.sleep(12000);
        }

        /** Результаты поиска */
        SelectClickMethod(sms.Page, sms.SelectPage);
        Thread.sleep(1000);
        authorizationObject.LoadingTime(20);
        List<String> getResultSearch = new ArrayList<String>();

        System.out.println("Берём все смс на 1 странице");
        if (!isElementNotVisible(sms.ResultSearch)) {
            System.out.println("Количество успешно загруженных в регион для " + Name + ": " + getResultSearch);
        } else {
            WaitElement(sms.ResultSearch);
            List<WebElement> list = driver.findElements(sms.ResultSearch);
            for (int i = 0; i < list.size(); i++) {
                getResultSearch.add(list.get(i).getText());
            }
            Collections.sort(getResultSearch);
        }

        System.out.println("Считаем сколько страниц");
        List<WebElement> Quantity = driver.findElements(sms.QuantityPage);
        Integer Count = 0;
        for (int i = 0; i < Quantity.size(); i++) {
            Count += 1;
        }
        System.out.println(Count);
        if (Count > 1) {
            while ((isElementNotVisible(sms.NextPage))) {
                ClickElement(sms.NextPage);
                if (KingNumber == 4) {
                    WaitNotElement3(sms.Loading, 20);
                }
                if (!isElementNotVisible(sms.ResultSearch)) {
                    Thread.sleep(1000);
                    System.out.println(
                            "Количество успешно загруженных в регион для " + Name + ": " + getResultSearch);
                } else {
                    Thread.sleep(1000);
                    WaitElement(sms.ResultSearch);
                    List<WebElement> list = driver.findElements(sms.ResultSearch);
                    for (int i = 0; i < list.size(); i++) {
                        getResultSearch.add(list.get(i).getText());
                    }
                    Collections.sort(getResultSearch);
                }
            }
        }
        List<String> getResultBd = new ArrayList<String>();
        System.out.println("Запрос в БД для " + Name + "");
        if (vmcl != 99) {
            sql.StartConnection("select p.id, p.local_uid  from " + smsBase + " p \n" +
                    "join " + logsBase + " p2 on p.id = p2.sms_id \n" +
                    "where  create_date BETWEEN '" + Date + " 00:00:00.346 +0500' and '" + Date + " 23:59:59.346 +0500' and p2.status = '1' group by p.id;");
        } else {
            sql.StartConnection("select p.id, p.local_uid from " + smsBase + " p \n" +
                    "where created_datetime BETWEEN '" + Date + " 00:00:00.346 +0500' and '" + Date + " 23:59:59.346 +0500' and fremd_status = '1' group by p.id;");
        }

        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("local_uid");
            getResultBd.add(sql.value);
        }
        Collections.sort(getResultBd);

        System.out.println("Проверка на соответствие для " + Name + "");
        Assertions.assertEquals(getResultSearch, getResultBd);
        System.out.println(
                "Значение на интерфейсе для " + Name + ": " + getResultSearch + " и значение в БД: " + getResultBd + " совпадают");
    }
}
