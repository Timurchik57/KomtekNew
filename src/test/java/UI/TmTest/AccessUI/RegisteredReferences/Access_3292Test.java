package UI.TmTest.AccessUI.RegisteredReferences;

import Base.BaseAPI;
import Base.BaseTest;
import Base.SQL;
import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.RegisteredReferences.References;
import io.qameta.allure.*;
import org.apache.hc.core5.util.TextUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Заказанные справки")
@Tag("Медицинская_справка")
@Tag("Проверка_БД")
@Disabled
public class Access_3292Test extends BaseAPI {

    // #Доделать (Сейчас нет фильтров в мед справках, может когда нибудь будут)
    AuthorizationObject authorizationObject;
    References references;
    Integer number = 0;

    @Test
    @Issue(value = "TEL-3292")
    @Issue(value = "TEL-3296")
    @Issue(value = "TEL-3298")
    @Issue(value = "TEL-3291")
    @Issue(value = "TEL-3299")
    @Owner(value = "Галиакберов Тимур")
    @Link(name = "ТМС-2053", url = "https://team-1okm.testit.software/projects/5/tests/2053?isolatedSection=af882452-d09a-4051-af99-3d04bf599188")
    @Description("Переходим в Заказанные справки - Проверяем фильтр у новых и закрытых справок")
    @DisplayName("Фильтры в Заказанные справки")
    public void Access_3292() throws SQLException, InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        references = new References(driver);

        AuthorizationMethod(authorizationObject.OKB);

        for (int i = 0; i < 2; i++) {
            By Reference = null;
            String status = "";

            if(i == 0) {
                Reference = references.ReeferenceClosed;
                status = "'Отклонен', 'Выдан гражданину'";
            } else {
                Reference = references.ReeferenceNew;
                status = "'Отправлен', 'В работе', 'Готов'";
            }
            ClickElement(Reference);

            System.out.println("1 проверка - Проверяем количество всех записей");
            GetSql("SELECT count(*) FROM vimis.patient_requests_for_doc x\n" +
                    "join dpc.status_for_doc s on x.status_id = s.id\n" +
                    "where s.\"name\" in ("+status+");", "Количество всех записей не совпадает");

            System.out.println("2 проверка - Проверяем количество записей по 'ФИО пациента'");
            SetFilter("ФИО пациента",
                    "Тестировщик Тест Тестович ",
                    "",
                    "SELECT count(*) FROM vimis.patient_requests_for_doc x\n" +
                            "join dpc.status_for_doc s on x.status_id = s.id\n" +
                            "where x.patientlastname = 'Тестировщик' and x.patientfirstname = 'Тест' and x.patientmiddlename = 'Тестович' and s.\"name\" in ("+status+");",
                    "Количество записей по 'ФИО пациента' не совпадает");

            System.out.println("3 проверка - Проверяем количество записей по 'СНИЛС пациента'");
            SetFilter("СНИЛС пациента",
                    "15979025720 ",
                    "",
                    "SELECT count(*) FROM vimis.patient_requests_for_doc x \n" +
                            "join dpc.status_for_doc s on x.status_id = s.id \n" +
                            "where x.patient_snils = '15979025720' and s.\"name\" in ("+status+");",
                    "Количество записей по 'СНИЛС пациента' не совпадает");

            System.out.println("4 проверка - Проверяем количество записей по 'Наименование медицинского документа'");
            SetFilter("Наименование медицинского документа",
                    "Протокол лабораторного исследования ",
                    "Протокол лабораторного исследования",
                    "SELECT count(*) FROM vimis.patient_requests_for_doc x \n" +
                            "join dpc.status_for_doc s on x.status_id = s.id \n" +
                            "join dpc.emd_types e on x.doc_type = e.id \n" +
                            "join dpc.sms_types st on e.doctype = st.id \n" +
                            "join dpc.registered_emd r on e.emd_type = r.\"oid\" \n" +
                            "where st.full_name = 'Протокол лабораторного исследования' and s.\"name\" in ("+status+");",
                    "Количество записей по 'Наименование медицинского документа' не совпадает");

            System.out.println("5 проверка - Проверяем количество записей по 'Уникальный идентификатор запроса'");
            ClickElement(references.Filters);
            ClickElement(references.Reset);
            authorizationObject.LoadingTime(10);
            if (!isElementVisibleTime(references.NoData, 3)) {
                WaitElement(references.line("1", "2"));
                String request_id = driver.findElement(references.line("1", "2")).getText();
                number = 1;
                while (TextUtils.isEmpty(request_id) & number < 10) {
                    number++;
                    request_id = driver.findElement(references.line(""+number+"", "2")).getText();
                }
                SetFilter("Уникальный идентификатор запроса",
                        "" + request_id + " ",
                        "",
                        "SELECT count(*) FROM vimis.patient_requests_for_doc x \n" +
                                "join dpc.status_for_doc s on x.status_id = s.id \n" +
                                "where x.request_id = '" + request_id + "' and s.\"name\" in ("+status+");",
                        "Количество записей по 'Уникальный идентификатор запроса' не совпадает");
            }

            System.out.println("6 проверка - Проверяем количество записей по 'LocalUid'");
            ClickElement(references.Filters);
            ClickElement(references.Reset);
            authorizationObject.LoadingTime(10);
            if (!isElementVisibleTime(references.NoData, 3)) {
                WaitElement(references.line("1", "7"));
                String LocalUid = driver.findElement(references.line("1", "7")).getText();
                number = 1;
                while (TextUtils.isEmpty(LocalUid) & number < 10) {
                    number++;
                    LocalUid = driver.findElement(references.line(""+number+"", "2")).getText();
                }
                SetFilter("LocalUid",
                        "" + LocalUid + " ",
                        "",
                        "SELECT count(*) FROM vimis.patient_requests_for_doc x \n" +
                                "join dpc.status_for_doc s on x.status_id = s.id \n" +
                                "where x.local_uid = '" + LocalUid + "' and s.\"name\" in ("+status+");",
                        "Количество записей по 'LocalUid' не совпадает");
            }
        }
    }

    @Step("Метод для выбора фильтра в Регистр наблюдений и сравнения с БД")
    public void SetFilter(String filter, String input, String selectFilter, String SQL, String mistake) throws SQLException, InterruptedException {
        references = new References(driver);
        authorizationObject = new AuthorizationObject(driver);

        ClickElement(references.Filters);
        ClickElement(references.Reset);
        authorizationObject.LoadingTime(10);
        ClickElement(references.Filters);
        ClickElement(references.Field(filter));
        inputWord(driver.findElement(references.Field(filter)), input);
        authorizationObject.LoadingTime(10);
        if(filter.equals("Наименование медицинского документа")) {
            ClickElement(authorizationObject.Select(selectFilter));
        }
        ClickElement(references.Search);
        Thread.sleep(2000);
        authorizationObject.LoadingTime(10);
        Thread.sleep(2000);

        GetSql(SQL, mistake);
    }

    @Step("Метод для сравнения значений в Регистр наблюдений с БД")
    public void GetSql(String SQL, String str) throws SQLException, InterruptedException {
        references = new References(driver);
        authorizationObject = new AuthorizationObject(driver);
        sql = new SQL();

        authorizationObject.LoadingTime(10);
        Thread.sleep(2000);
        WaitElement(references.AllCount);
        String Count = driver.findElement(references.AllCount).getText().substring(6);

        sql.StartConnection(SQL);
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }
        Assertions.assertEquals(Count, sql.value, str);
    }
}