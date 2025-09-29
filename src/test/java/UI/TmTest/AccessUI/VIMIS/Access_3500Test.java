package UI.TmTest.AccessUI.VIMIS;

import Base.BaseAPI;
import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.Directions.Kvots.EquipmentSchedule;
import UI.TmTest.PageObject.Directions.Kvots.IncomingUnfinished;
import UI.TmTest.PageObject.VIMIS.StandardsProvision;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Вимис")
@Tag("Стандарты_оказания_МП")
@Tag("Консультация_на_оборудование")
@Tag("Оборудование")
public class Access_3500Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    StandardsProvision standardsProvision;
    DirectionsForQuotas directionsForQuotas;
    IncomingUnfinished incomingUnfinished;
    EquipmentSchedule equipmentSchedule;

    @Test
    @Issue(value = "TEL-3500")
    @Issue(value = "TEL-3501")
    @Owner(value = "Галиакберов Тимур")
    @Description("Переходим в Вимис - Стандарты оказания МП и создаём записи, чтобы появилось ограничение при создании направления, поосле создаём направление и проверяем валидную ошибку - отправляем на согласование - проверяем статус")
    @DisplayName("Создание направления на диагностику с ограничениями по стандартам МП")
    public void Access_3500() throws SQLException, InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        standardsProvision = new StandardsProvision(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        equipmentSchedule = new EquipmentSchedule(driver);

        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(standardsProvision.StandardsProv);

        sql.StartConnection("select count(*) from telmed.medical_care_standards mcs \n" +
                "where diagnosiscode = 'A00.0' and age = '1' and sex = '1' and servicecode = 'A06.30.005' and frequency = '15' and multiplicity = '15';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }
        if (sql.value.equals("0")) {
            System.out.println("Создаём новую запись");
            ClickElement(standardsProvision.Add("1"));
            WaitElement(standardsProvision.SetField("Перечень диагнозов"));
            driver.findElement(standardsProvision.SetField("Перечень диагнозов")).sendKeys("A00.0");
            ClickElement(authorizationObject.Select("A00.0 Холера, вызванная холерным вибрионом 01, биовар cholerae"));
            SelectClickMethod(standardsProvision.SetField("Возраст пациента"), authorizationObject.Select("Взрослые"));
            SelectClickMethod(standardsProvision.SetField("Пол пациента"), authorizationObject.Select("Мужской"));
            driver.findElement(standardsProvision.SetField("Назначаемая услуга")).sendKeys("A06.30.005");
            Thread.sleep(2000);
            ClickElement(authorizationObject.SelectFirst);
            inputWord(driver.findElement(standardsProvision.SetField("Частота услуги")), "15 ");
            inputWord(driver.findElement(standardsProvision.SetField("Кратность услуги")), "15 ");
            ClickElement(standardsProvision.Add("2"));
            authorizationObject.LoadingTime(10);
            Thread.sleep(2000);
        }
        sql.value = "0";

        sql.StartConnection("select count(*) from telmed.medical_care_standards mcs \n" +
                "where diagnosiscode = 'A00.0' and age = '1' and sex = '1' and servicecode = 'A05.23.009.008' and frequency = '15' and multiplicity = '15';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }
        System.out.println(sql.value);
        if (sql.value.equals("0")) {
            System.out.println("Создаём новую запись");
            ClickElement(standardsProvision.Add("1"));
            WaitElement(standardsProvision.SetField("Перечень диагнозов"));
            driver.findElement(standardsProvision.SetField("Перечень диагнозов")).sendKeys("A00.0");
            ClickElement(authorizationObject.Select("A00.0 Холера, вызванная холерным вибрионом 01, биовар cholerae"));
            SelectClickMethod(standardsProvision.SetField("Возраст пациента"), authorizationObject.Select("Взрослые"));
            SelectClickMethod(standardsProvision.SetField("Пол пациента"), authorizationObject.Select("Мужской"));
            driver.findElement(standardsProvision.SetField("Назначаемая услуга")).sendKeys("A05.23.009.008");
            Thread.sleep(2000);
            ClickElement(authorizationObject.SelectFirst);
            inputWord(driver.findElement(standardsProvision.SetField("Частота услуги")), "15 ");
            inputWord(driver.findElement(standardsProvision.SetField("Кратность услуги")), "15 ");
            ClickElement(standardsProvision.Add("2"));
            authorizationObject.LoadingTime(10);
            Thread.sleep(2000);
        }

        directionsForQuotas.CreateConsultationEquipment(true,
                "159 790 257 20",
                authorizationObject.Select("Женская консультация"),
                "Аорта",
                false,
                PMOTarget,
                authorizationObject.Select("A00.0 Холера, вызванная холерным вибрионом 01, биовар cholerae"),
                authorizationObject.Select("Компьютерная томография органов брюшной полости"),
                "50",
                false,
                false,
                false, "");

        ClickElement(directionsForQuotas.Agreement("Отправить на согласование"));
        Thread.sleep(4000);
        authorizationObject.LoadingTime(10);

        /** Нет добавления файлов (3501) */
        WaitNotElement3(directionsForQuotas.Close, 3);

        AuthorizationMethod(authorizationObject.YATCKIV);
        ClickElement(incomingUnfinished.ConsultationWait);
        Thread.sleep(1500);
        WaitNotElement3(authorizationObject.Loading, 30);
        ClickElement(incomingUnfinished.DESK);
        Thread.sleep(1500);
        ClickElement(incomingUnfinished.SearchMO("БУ ХМАО-Югры \"Окружная клиническая больница\"", "На согласовании"));

        System.out.println("Берём номер конультации, для проверки, что он добавляется в бд");
        Thread.sleep(2500);
        WaitElement(incomingUnfinished.NumberConsultation);
        String NumberConsul = driver.findElement(incomingUnfinished.NumberConsultation).getText().substring(19);

        System.out.println("Проверяем статус 25 На согласовании");
        sql.StartConnection("Select * from telmed.directions where id = '" + NumberConsul + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("status");
        }
        Assertions.assertEquals(sql.value, "25", "После нажатия кнопки Отправить на согласование, статус должен быть 25 (На согласовании)");
    }
}