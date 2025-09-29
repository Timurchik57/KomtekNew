package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Проверка_БД")
@Tag("Пакс")
@Tag("Цами")
@Tag("МосМед")
@Tag("Консультация_на_оборудование")
@Tag("Основные")
public class Access_3239Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    String id_direction;
    String study_uid;

    @Test
    @Issue(value = "TEL-3239")
    @Issue(value = "TEL-3856")
    @Owner(value = "Галиакберов Тимур")
    @Link(name = "ТМС-2148", url = "https://team-1okm.testit.software/projects/5/tests/2148?isolatedSection=af882452-d09a-4051-af99-3d04bf599188")
    @Description("Создаём направление - Проверяем создание ссылки ЦАМИ")
    @DisplayName("Отправка направления в МосМед")
    public void Access_3239() throws InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);

        AuthorizationMethod(authorizationObject.OKB);
        directionsForQuotas.CreateConsultationEquipment(true, "159 790 257 20",
                authorizationObject.Select("Женская консультация"),
                "Аорта",
                false,
                PMOTarget,
                authorizationObject.Select("A00.0 Холера, вызванная холерным вибрионом 01, биовар cholerae"),
                authorizationObject.Select("HMP01"),
                "40", false, true, false, "");

        sql.StartConnection("Select * from telmed.directions order by id limit 1");
        while(sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
        }

        System.out.println("Переходим в исходящие незавершённые");
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.SortDesc);
        ClickElement(directionsForQuotas.SearchID(sql.value));
        WaitElement(directionsForQuotas.NumberConsult);
        Thread.sleep(1500);
        WaitNotElement3(directionsForQuotas.LoadingAddPatient, 30);
        String Number = driver.findElement(directionsForQuotas.NumberConsult).getText().substring(19);

        System.out.println("\n1 проверка - Проверяем добавление ссылки ЦАМИ на веб");
        WaitElement(directionsForQuotas.CamiAndII("ЦАМИ","1", "1"));
        String data = driver.findElement(directionsForQuotas.CamiAndII("ЦАМИ","1", "1")).getText().substring(0, 10);

        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
        Date = formatForDateNow.format(date);
        Assertions.assertEquals(data, Date, "У ссылки цами не совпадает дата");

        System.out.println("\n2 проверка - Формируется accessionNumber в консультации");
        String accessionNumber = driver.findElement(directionsForQuotas.AccessionNumber).getText().substring(17);
        Assertions.assertEquals(accessionNumber, "TLMD"+Number);

        sql.StartConnection("SELECT * FROM telmed.cami_links order by id desc limit 1;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("accession_number");
        }
        Assertions.assertEquals(sql.value, accessionNumber);
    }
}