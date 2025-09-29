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
import java.time.LocalDate;
import java.time.Period;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Консультация_на_оборудование")
@Tag("Заполнение_карточки_пациента")
@Tag("Проверка_БД")
public class Access_1400Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    EquipmentSchedule equipmentSchedule;
    Access_1445Test access_1445Test;
    ConsultationOutgoingUnfinished consultationOutgoingUn;
    ConsultationUnfinished consultationUnfinished;
    public String DateSql;

    @Issue(value = "TEL-1400")
    @Link(name = "ТМС-1533", url = "https://team-1okm.testit.software/projects/5/tests/1533?isolatedSection=aee82730-5a5f-42aa-a904-10b3057df4c4")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Добавить отображение возраста пациента в списке консультаций")
    @Description("Создаём удалённую консультацию/на оборудование - переходим в отправленные и проверяем возраст рядом с фамилией. Меняем в бд значение iemk.op_patient_reg.birthdate в большую/меньшую сторону и проверяем изменения")
    public void Access_1400() throws SQLException, InterruptedException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        access_1445Test = new Access_1445Test();
        consultationOutgoingUn = new ConsultationOutgoingUnfinished(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);

        AuthorizationMethod(authorizationObject.OKB);
        access_1445Test.AddConsultationMethod(false);

        System.out.println("Переходим в Исходящие - Незавершённые - Созданное направление и берём значение ФИО");
        ClickElement(consultationOutgoingUn.Consultation);
        WaitElement(consultationOutgoingUn.LastName);
        String FIO = driver.findElement(consultationOutgoingUn.LastName).getText().substring(27);

        System.out.println("Берём значение даты в БД");
        sql.StartConnection("Select * from iemk.op_patient_reg WHERE snils  = '15979025720';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("birthdate");
        }
        DateSql = sql.value.substring(0, sql.value.length() - 9);
        System.out.println(DateSql);
        System.out.println(LocalDate.now());
        int actual = calculateAge(LocalDate.parse(DateSql), LocalDate.now());
        System.out.println(actual);
        Assertions.assertEquals(Integer.valueOf(FIO), actual, "Возраст не сходится");
        Access1400Method("большую", "2022");
        Access1400Method("меньшую", "2020");

        System.out.println("Меняем дату рождения на прежнюю");
        sql.UpdateConnection("update iemk.op_patient_reg set birthdate = '2021-11-22 00:00:00.000' WHERE snils  = '15979025720';");
    }

    @Step("Метод для определения разницы между датами в годах")
    public static int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }

    @Step("Метод для определения разницы между датами в годах")
    public void Access1400Method(String name, String date) throws SQLException {
        consultationOutgoingUn = new ConsultationOutgoingUnfinished(driver);

        System.out.println("Меняем дату рождения в " + name + " сторону на один год");
        sql.UpdateConnection("update iemk.op_patient_reg set birthdate = '" + date + "-11-22 00:00:00.000' WHERE snils  = '15979025720';");

        System.out.println("Обновляем страницу и сравниваем значения");
        driver.navigate().refresh();
        WaitElement(consultationOutgoingUn.LastName);
        String FIO = driver.findElement(consultationOutgoingUn.LastName).getText().substring(27);

        System.out.println("Берём значение даты в БД");
        sql.StartConnection("Select * from iemk.op_patient_reg x\n" +
                "WHERE snils  = '15979025720';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("birthdate");
        }
        DateSql = sql.value.substring(0, sql.value.length() - 9);
        System.out.println(DateSql);
        System.out.println(LocalDate.now());
        int actual = calculateAge(LocalDate.parse(DateSql), LocalDate.now());
        System.out.println(actual);
        Assertions.assertEquals(Integer.valueOf(FIO), actual, "Возраст не сходится");
    }
}
