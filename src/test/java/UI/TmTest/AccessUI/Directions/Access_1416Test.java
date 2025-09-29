package UI.TmTest.AccessUI.Directions;

import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationOutgoingUnfinished;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationUnfinished;
import UI.TmTest.PageObject.Directions.Kvots.EquipmentSchedule;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import Base.BaseAPI;
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
@Tag("Удалённая_консультация")
@Tag("Проверка_БД")
public class Access_1416Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    EquipmentSchedule equipmentSchedule;
    Access_1445Test access_1445Test;
    ConsultationOutgoingUnfinished consultationOutgoingUn;
    ConsultationUnfinished consultationUnfinished;
    public String DateSql;

    @Issue(value = "TEL-1416")
    @Issue(value = "TEL-3019")
    @Link(name = "ТМС-1534", url = "https://team-1okm.testit.software/projects/5/tests/1534?isolatedSection=aee82730-5a5f-42aa-a904-10b3057df4c4")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Дата отправки консультации в МО")
    @Description("Создаём удалённую консультацию - переходим в отправленные и проверяем Дата отправки консультации. Переходим в МО, куда отправили - входящие и проверяем Дата отправки консультации")
    public void Access_1416() throws SQLException, InterruptedException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        access_1445Test = new Access_1445Test();
        consultationOutgoingUn = new ConsultationOutgoingUnfinished(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);

        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
        Date = formatForDateNow.format(date);
        AuthorizationMethod(authorizationObject.OKB);
        access_1445Test.AddConsultationMethod(false);

        System.out.println("Переходим в Исходящие - Незавершённые - Созданное направление");
        ClickElement(consultationOutgoingUn.Consultation);
        ClickElement(consultationOutgoingUn.SortDesc);
        ClickElement(consultationOutgoingUn.ConsultationFirst);

        System.out.println("Берём id записи и дату отправки консультации");
        WaitElement(consultationOutgoingUn.NumberConsultation);
        WaitElement(consultationOutgoingUn.DateConsultation);
        String number = driver.findElement(consultationOutgoingUn.NumberConsultation).getText().substring(20);
        String dateUi = driver.findElement(consultationOutgoingUn.DateConsultation).getText();
        String complaints = driver.findElement(consultationOutgoingUn.Consul("Жалобы")).getText();
        String anemnes = driver.findElement(consultationOutgoingUn.Consul("Анамнез")).getText();
        String status = driver.findElement(consultationOutgoingUn.Consul("Объективный статус")).getText();

        System.out.println("Заменяем в дате точки на тире");
        String remove = ".!";
        for (char c : remove.toCharArray()) {
            dateUi = dateUi.replace(String.valueOf(c), "-");
        }
        String dateUi1 = dateUi.substring(0, dateUi.length() - 6);

        System.out.println("Берём значение даты в БД");
        sql.StartConnection("Select * from telmed.directionstatuses where directionid = '" + number + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("statusdate");
            System.out.println(sql.value);
        }
        DateSql = sql.value.substring(0, sql.value.length() - 16);
        String dd = DateSql.substring(8);
        String mm = DateSql.substring(0, DateSql.length() - 3);
        String mm1 = mm.substring(5);
        String yyyy = DateSql.substring(0, DateSql.length() - 6);
        Assertions.assertEquals(dateUi1, dd + "-" + mm1 + "-" + yyyy, "Дата создания консультации не совпадает");

        /** Берём значения из БД для 3019*/
        sql.StartConnection("Select * from telmed.directions where id = '" + number + "';");
        String complaintsBd = null;
        String anemnesBd = null;
        String statusBd = null;
        while (sql.resultSet.next()) {
            complaintsBd = sql.resultSet.getString("complaints");
            anemnesBd = sql.resultSet.getString("anamnesis");
            statusBd = sql.resultSet.getString("objectivestate");
        }

        System.out.println("Проверяем заполнение данных по 3019");
        Assertions.assertEquals(complaints, complaintsBd, "Жалобы не совпадают с БД");
        Assertions.assertEquals(anemnes, anemnesBd, "Анамнез не совпадают с БД");
        Assertions.assertEquals(status, statusBd, "Объективный статус не совпадают с БД");

        System.out.println("Авторизуемся в МО, куда отправили консультацию, Переходим в входящие - Незавершённые - Созданное направление");
        if (KingNumber == 4) {
            AuthorizationMethod(authorizationObject.Kondinsk);
        } else {
            AuthorizationMethod(authorizationObject.YATCKIV);
        }
        ClickElement(consultationUnfinished.UnfinishedWait);
        ClickElement(consultationUnfinished.FirstWait);
        if (KingNumber == 4) {
            Thread.sleep(5000);
        }
        WaitElement(consultationUnfinished.DateConsultation);
        String dateUi2 = driver.findElement(consultationUnfinished.DateConsultation).getText();
        for (char c : remove.toCharArray()) {
            dateUi2 = dateUi2.replace(String.valueOf(c), "-");
        }
        String dateUi3 = dateUi2.substring(0, dateUi2.length() - 6);
        Assertions.assertEquals(dateUi3, dd + "-" + mm1 + "-" + yyyy, "Дата создания консультации не совпадает");
    }
}
