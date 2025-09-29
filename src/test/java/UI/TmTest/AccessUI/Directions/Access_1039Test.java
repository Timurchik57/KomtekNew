package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import UI.TmTest.PageObject.Administration.MedOrganization;
import UI.TmTest.PageObject.Administration.Users;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.EquipmentSchedule;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.RRP;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Консультация_на_оборудование")
@Tag("Заполнение_карточки_пациента")
@Tag("Удалённая_консультация")
@Tag("РРП")
public class Access_1039Test extends BaseAPI {

    MedOrganization medOrganization;
    AuthorizationObject authorizationObject;
    Users users;
    DirectionsForQuotas directionsForQuotas;
    EquipmentSchedule equipmentSchedule;
    RRP rrp;
    Integer ToDay;

    @Issue(value = "TEL-1039")
    @Link(name = "ТМС-1381", url = "https://team-1okm.testit.software/projects/5/tests/1381?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Правки в интерфейс на странице создания пациента")
    @Description("Проверяем блок Данные о страховом полисе, Социальный статус, Место работы на обязательность в различных условиях")
    public void Access_1039() throws SQLException, InterruptedException, IOException {

        Calendar calendar = new GregorianCalendar();
        equipmentSchedule = new EquipmentSchedule(driver);
        medOrganization = new MedOrganization(driver);
        authorizationObject = new AuthorizationObject(driver);
        users = new Users(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        rrp = new RRP(driver);
        RRPConnect();

        System.out.println("Генерация снилса для добавления пользователя");
        authorizationObject.GenerateSnilsMethod();

        System.out.println("Авторизация и переход в пользователи");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(directionsForQuotas.Unfinished);
        actionElementAndClick(directionsForQuotas.Consultation);

        /** Переход в создание консультации  */
        WaitElement(directionsForQuotas.Unfinished);
        actionElementAndClick(directionsForQuotas.Consultation);

        System.out.println("Создать консультацию - добавить пациента");
        WaitElement(directionsForQuotas.Heading);
        WaitElement(directionsForQuotas.CreateWait);
        directionsForQuotas.Create.click();
        WaitElement(directionsForQuotas.TypeConsultationWait);
        directionsForQuotas.DistrictDiagnostic.click();
        directionsForQuotas.Next.click();
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys(authorizationObject.GenerationSnils);
        Thread.sleep(2000);
        ClickElement(directionsForQuotas.BigSearchWait);

        System.out.println("Создание пациента для консультации");
        WaitElement(directionsForQuotas.CreatePatientWait);
        directionsForQuotas.CreatePatient.click();
        WaitElement(directionsForQuotas.RemoteConsultationForDiagnostics);
        directionsForQuotas.AddSnils.sendKeys(authorizationObject.GenerationSnils);
        inputWord(directionsForQuotas.LastName, getRandomWord(6, "абвгдеёжзийклмнопрстуфхцщшьъыэюя"));
        inputWord(directionsForQuotas.Name, getRandomWord(6, "абвгдеёжзийклмнопрстуфхцщшьъыэюя"));
        inputWord(directionsForQuotas.MiddleName, getRandomWord(6, "абвгдеёжзийклмнопрстуфхцщшьъыэюя"));

        System.out.println("Проверка отображения Данные о страховом полисе");
        ClickElement(directionsForQuotas.Polis);
        WaitNotElement3(directionsForQuotas.PolisTypeError, 3);

        System.out.println("Проверка отображения - Тип полиса обязателен для заполнения");
        inputWord(directionsForQuotas.PolisENP, "12332145678945612");
        WaitElement(directionsForQuotas.PolisTypeError);

        System.out.println("Проверка значения Тип полиса с бд");
        ClickElement(directionsForQuotas.PolisTypeWait);
        Thread.sleep(1000);
        List<String> Type = new ArrayList<String>();
        List<WebElement> TypeWeb = driver.findElements(directionsForQuotas.StatusSelectT);
        for (int i = 0; i < TypeWeb.size(); i++) {
            Type.add(TypeWeb.get(i).getText());
        }
        List<String> TypeSql = new ArrayList<String>();
        sql.StartConnection("Select * from telmed.hst0065;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("namepolicytype");
            TypeSql.add(sql.value);
        }
        Assertions.assertEquals(Type, TypeSql, "Не совпадает Тип полиса с значениями в БД");

        System.out.println("Указываем дату рождения с которой прошло менее 14 лет");
        ToDay = calendar.get(Calendar.DAY_OF_MONTH);

        System.out.println("Высчитываем нужный год");
        Integer Year = calendar.get(Calendar.YEAR);
        Integer IntYear = Year - 14;

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Integer IntDay = calendar.get(Calendar.DAY_OF_MONTH);
        Integer IntDayOld = IntDay;

        ClickElement(directionsForQuotas.DateWait);
        String YearNow = directionsForQuotas.Year.getText();
        while (!YearNow.contains(String.valueOf(IntYear))) {
            ClickElementTry(directionsForQuotas.BeforeYearWait);
            YearNow = directionsForQuotas.Year.getText();
        }

        if (ToDay == 31 & IntDayOld == 1 | ToDay == 30 & IntDayOld == 1 | ToDay == 29 & IntDayOld == 1) {
            ClickElement(By.xpath("//table[@class='el-date-table']//tr/td[@class='next-month']//span[text()=" + IntDay + "]"));
        } else {
            ClickElement(By.xpath("//table[@class='el-date-table']//tr/td[@class='available']//span[text()=" + IntDay + "]"));
        }

        System.out.println("Проверяем, что поле Гражданство не обязательно");
        WaitNotElement3(directionsForQuotas.CitizenshipError, 3);

        System.out.println("Проверяем, что поле Социальный статус обязательно");
        WaitElement(directionsForQuotas.StatusError);

        System.out.println("Указываем дату рождения с которой прошло ровно 14 лет");
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        IntDay = calendar.get(Calendar.DAY_OF_MONTH);
        ClickElement(directionsForQuotas.DateWait);

        if (ToDay == 31 & IntDayOld == 1 | ToDay == 30 & IntDayOld == 1 | ToDay == 29 & IntDayOld == 1) {
            ClickElement(directionsForQuotas.BeforeMonth);
            ClickElement(By.xpath("//table[@class='el-date-table']//tr/td[@class='available']//span[text()=" + IntDay + "]"));
        } else {
            ClickElement(By.xpath("//table[@class='el-date-table']//tr/td[@class='available']//span[text()=" + IntDay + "]"));
        }

        System.out.println("Проверяем, что поле Гражданство обязательно");
        WaitElement(directionsForQuotas.CitizenshipError);

        System.out.println("Указываем дату рождения с которой прошло более 14 лет");
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        IntDay = calendar.get(Calendar.DAY_OF_MONTH);
        ClickElement(directionsForQuotas.DateWait);
        ClickElement(By.xpath("//table[@class='el-date-table']//tr/td[@class='available']//span[text()=" + IntDay + "]"));
        System.out.println("Проверяем, что поле Гражданство обязательно");

        WaitElement(directionsForQuotas.CitizenshipError);

        System.out.println("Указываем дату рождения с которой прошло менее 8 лет");
        IntYear = Integer.valueOf(Year) - 8;
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        IntDay = calendar.get(Calendar.DAY_OF_MONTH);
        ClickElement(directionsForQuotas.DateWait);
        YearNow = directionsForQuotas.Year.getText();
        while (!YearNow.contains(String.valueOf(IntYear))) {
            Thread.sleep(1000);
            directionsForQuotas.AfterYear.click();
            YearNow = directionsForQuotas.Year.getText();
        }
        if (ToDay == 31 & IntDayOld == 1 | ToDay == 30 & IntDayOld == 1 | ToDay == 29 & IntDayOld == 1) {
            ClickElement(By.xpath("//table[@class='el-date-table']//tr/td[@class='next-month']//span[text()=" + IntDay + "]"));
        } else {
            ClickElement(By.xpath("//table[@class='el-date-table']//tr/td[@class='available']//span[text()=" + IntDay + "]"));
        }
        WaitNotElement3(directionsForQuotas.StatusError, 3);

        System.out.println("Указываем дату рождения с которой прошло ровно 8 лет");
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        IntDay = calendar.get(Calendar.DAY_OF_MONTH);
        ClickElement(directionsForQuotas.DateWait);
        if (ToDay == 31 & IntDayOld == 1 | ToDay == 30 & IntDayOld == 1 | ToDay == 29 & IntDayOld == 1) {
            ClickElement(directionsForQuotas.BeforeMonth);
            ClickElement(By.xpath("//table[@class='el-date-table']//tr/td[@class='available']//span[text()=" + IntDay + "]"));
        } else {
            ClickElement(By.xpath("//table[@class='el-date-table']//tr/td[@class='available']//span[text()=" + IntDay + "]"));
        }
        WaitElement(directionsForQuotas.StatusError);

        System.out.println("Указываем дату рождения с которой прошло более 8 лет");
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        IntDay = calendar.get(Calendar.DAY_OF_MONTH);
        ClickElement(directionsForQuotas.DateWait);
        ClickElement(By.xpath("//table[@class='el-date-table']//tr/td[@class='available']//span[text()=" + IntDay + "]"));
        WaitElement(directionsForQuotas.StatusError);

        System.out.println("Указываем Социальный статус Работающий и проверяем обязательность места работы");
        ClickElement(directionsForQuotas.Jobinfo);
        WaitNotElement3(directionsForQuotas.JobError, 3);
        ClickElement(directionsForQuotas.Status);
        ClickElement(directionsForQuotas.StatusSelectJob);
        WaitNotElement3(directionsForQuotas.JobPost, 3);

        System.out.println("Заполняем данные о пациенте для консультации");
        ClickElement(directionsForQuotas.ManWait);
        SelectClickMethod(directionsForQuotas.TypeDocument, directionsForQuotas.SelectTypeDocument);
        inputWord(directionsForQuotas.Serial, "12344");
        inputWord(directionsForQuotas.Number, "1234566");
        actionElementAndClick(directionsForQuotas.Address);
        inputWord(directionsForQuotas.Address, "г Москва, ул Арбат, д 9АФ");
        ClickElement(directionsForQuotas.Select);
        actionElementAndClick(directionsForQuotas.AddressHome);
        ClickElement(directionsForQuotas.Select);
        inputWord(directionsForQuotas.Job, "KOMTEKK");
        inputWord(directionsForQuotas.PostJob, "TESTT");
        SelectClickMethod(directionsForQuotas.PolisTypeWait, directionsForQuotas.PolisTypeSelectEdin);
        actionElementAndClick(directionsForQuotas.NextPatient);

        System.out.println("Заполнение информации о направившем враче");
        directionsForQuotas.DoctorMethod();
        Thread.sleep(3000);

        System.out.println("Переходим в РРП и вводим снилс");
        driver.get(UrlRRPWeb);
        WaitElement(rrp.LoginWait);
        rrp.Login.sendKeys(LoginRRPWeb);
        rrp.Password.sendKeys(PasswordRRPWeb);
        rrp.Enter.click();
        WaitElement(rrp.SnilsWait);
        rrp.Snils.sendKeys(authorizationObject.GenerationSnils);
        rrp.Search.click();

        System.out.println("Берём значение Снилс");
        WaitElement(rrp.SearchSnilsWait);
        System.out.println("Убираем пробелы из Снилс");
        String charsToRemove = " !";
        for (char c : charsToRemove.toCharArray()) {
            authorizationObject.GenerationSnils = authorizationObject.GenerationSnils.replace(String.valueOf(c), "");
        }

        System.out.println("Авторизуемся в РРП через АПИ");
        GetRRP("", authorizationObject.GenerationSnils);
        assertThat(Response.get("size"), equalTo(1));
        assertThat(Response.get("patients[0].snils"), equalTo("" + authorizationObject.GenerationSnils + ""));

    }
}
