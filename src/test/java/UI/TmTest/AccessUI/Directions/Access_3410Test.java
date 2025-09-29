package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.Directions.Kvots.EquipmentSchedule;
import api.Before.Authorization;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Консультация_на_оборудование")
@Tag("Расписание_оборудования")
@Tag("Проверка_БД")
@Tag("Api_Направление_на_диагностику")
@Tag("schedule")
public class Access_3410Test extends BaseAPI {

    Authorization authorization;
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    EquipmentSchedule equipmentSchedule;
    String equipmentId;
    String directionGuid;
    String DirectionId;
    String Research;
    String OldDate;
    String Tomorrow;

    @Test
    @Issue(value = "TEL-3410")
    @Link(name = "ТМС-2054", url = "https://team-1okm.testit.software/projects/5/tests/2054?isolatedSection=af882452-d09a-4051-af99-3d04bf599188")
    @Owner(value = "Галиакберов Тимур")
    @Description("Создаём напрвление, но не записываем на диагностику. После используем гуид данного направления в методе, который сразу создает расписание и записывает на слот")
    @DisplayName("Создание направления на диагностику через метод который сразу создает расписание и записывает на слот")
    public void Access_3410() throws SQLException, InterruptedException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        authorization = new Authorization();
        Research = "HMP03";
        String NameRes = null;

        AuthorizationMethod(authorizationObject.OKB);

        if (KingNumber == 1) {
            equipmentId = "14037";
            NameRes = "Axiom Iconos R200";
        }
        if (KingNumber == 2) {
            equipmentId = "17478";
            NameRes = "Axiom Iconos R200";
        }
        if (KingNumber == 4) {
            equipmentId = "48434";
            NameRes = "Orthoceph JC 100";
        }

        System.out.println("Добавляем исследование оборудованию");
        String[] str = {Research};
        equipmentSchedule.AddResearch(PMOTarget, NameRes, "", str);

        System.out.println("Создаём направление, берём его directionGuid, enddate");
        AddConsul("HMP01", "40");

        System.out.println("Прибавляем 1 день к последней дате");
        String datePlus = String.valueOf(addDay(Tomorrow, 1));

        System.out.println("\n 1 проверка - Пробуем записать на слот через авторизацию другой МО");
        Api(HostAddress + "/api/schedule/slot/occupy-and-create-missing-slot",
                "post",
                null,
                null,
                "{\n" +
                        "    \"equipmentId\": "+equipmentId+",\n" +
                        "    \"equipmentDate\": \""+datePlus+"T10:30:00\",\n" +
                        "    \"timeInterval\" : 15,\n" +
                        "    \"directionGuid\": \""+directionGuid+"\"         \n" +
                        "}",
                200, true);
        Assertions.assertEquals(Response.getString("ErrorMessage"), "Записывать на слот может только МО создавшее направление.", "Нет ошибки на авторизацию дрогой МО");

        System.out.println("\n 2 проверка - Авторизуемся через валидную МО");
        OldDate = datePlus;
        Token = authorization.AuthorizationsAdd("1.2.643.5.1.13.13.12.2.86.8902", "21");

        Api(HostAddress + "/api/schedule/slot/occupy-and-create-missing-slot",
                "post",
                null,
                null,
                "{\n" +
                        "    \"equipmentId\": "+equipmentId+",\n" +
                        "    \"equipmentDate\": \""+datePlus+"T10:30:00\",\n" +
                        "    \"timeInterval\" : 30,\n" +
                        "    \"directionGuid\": \""+directionGuid+"\"         \n" +
                        "}",
                200,
                false);
        Assertions.assertEquals(Response.getString("Result.DirectionId"), DirectionId, "Не создалось расписание и не записался слот");
        // Проверяем 3856
        sql.StartConnection("SELECT * FROM telmed.cami_links order by id desc limit 1;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("accession_number");
        }
        Assertions.assertEquals(Response.getString("Result.AccessionNumber"), sql.value, "Не сгенерировался AccessionNumber");

        System.out.println("\n 3 проверка - Используем направление, которое уже записали на слот");
        System.out.println("Прибавляем 2 дня к последней дате");
        datePlus = String.valueOf(addDay(Tomorrow, 2));
        Api(HostAddress + "/api/schedule/slot/occupy-and-create-missing-slot",
                "post",
                null,
                null,
                "{\n" +
                        "    \"equipmentId\": "+equipmentId+",\n" +
                        "    \"equipmentDate\": \""+datePlus+"T13:30:00\",\n" +
                        "    \"timeInterval\" : 30,\n" +
                        "    \"directionGuid\": \""+directionGuid+"\"         \n" +
                        "}",
                200,
                false);
        Assertions.assertEquals(Response.getString("ErrorMessage"), "Запись направлений возможна только в статусе \"Создано\"", "Направление со статусом отличным от Создано, не должно записываться на новый слот");

        System.out.println("\n 4 проверка - Записываемся на занятое время");
        System.out.println("Создаём новое направление, берём его directionGuid, enddate");
        AddConsul("HMP01", "40");

        Api(HostAddress + "/api/schedule/slot/occupy-and-create-missing-slot",
                "post",
                null,
                null,
                "{\n" +
                        "    \"equipmentId\": "+equipmentId+",\n" +
                        "    \"equipmentDate\": \""+OldDate+"T10:30:00\",\n" +
                        "    \"timeInterval\" : 30,\n" +
                        "    \"directionGuid\": \""+directionGuid+"\"         \n" +
                        "}",
                200,
                false);
        Assertions.assertEquals(Response.getString("ErrorMessage"), "Слот уже занят", "Направление, не должно записываться на занятый слот");

        System.out.println("\n 5 проверка - Записываемся на время 23:45");
        datePlus = String.valueOf(addDay(Tomorrow, 1));
        Api(HostAddress + "/api/schedule/slot/occupy-and-create-missing-slot",
                "post",
                null,
                null,
                "{\n" +
                        "    \"equipmentId\": "+equipmentId+",\n" +
                        "    \"equipmentDate\": \""+datePlus+"T23:45:00\",\n" +
                        "    \"timeInterval\" : 30,\n" +
                        "    \"directionGuid\": \""+directionGuid+"\"         \n" +
                        "}",
                200,
                false);
        Assertions.assertEquals(Response.getString("ErrorMessage"), "Не удалось найти или создать слот. Обратитесь к администратору.", "Нельзя записаться на такое позднее время, так как длительность консультации превышает оставшееся время");

        System.out.println("\n 6 проверка - Записываемся c исследованием, которого нет у оборудования");
        System.out.println("Создаём новое направление, и указываем другое исследование, которого нет у той МО, в которую отправляем направление");
        AddConsul(Research, "40");

        Api(HostAddress + "/api/schedule/slot/occupy-and-create-missing-slot",
                "post",
                null,
                null,
                "{\n" +
                        "    \"equipmentId\": "+equipmentId+",\n" +
                        "    \"equipmentDate\": \""+datePlus+"T10:30:00\",\n" +
                        "    \"timeInterval\" : 30,\n" +
                        "    \"directionGuid\": \""+directionGuid+"\"         \n" +
                        "}",
                200,
                false);
        Assertions.assertEquals(Response.getString("ErrorMessage"), "Невозможно создать запись на слот, т.к. исследование по направлению, недоступно на данном оборудовании", "Нельзя записаться на исследование, которого нет у оборудования");

        System.out.println("\n 7 проверка - Записываемся c направлением превышающим вес на оборудовании");
        System.out.println("Создаём новое направление, и указываем другое исследование, которого нет у той МО, в которую отправляем направление");
        AddConsul("HMP01", "80");

        Api(HostAddress + "/api/schedule/slot/occupy-and-create-missing-slot",
                "post",
                null,
                null,
                "{\n" +
                        "    \"equipmentId\": "+equipmentId+",\n" +
                        "    \"equipmentDate\": \""+datePlus+"T13:30:00\",\n" +
                        "    \"timeInterval\" : 30,\n" +
                        "    \"directionGuid\": \""+directionGuid+"\"         \n" +
                        "}",
                200,
                false);
        Assertions.assertEquals(Response.getString("ErrorMessage"), "Вес пациента превышает максимальный вес, предусмотренный для аппарата.", "Нельзя записаться с весом превышающим обородование");
    }

    @Step("Создаём направление, берём его directionGuid, enddate")
    public void AddConsul (String research, String mass) throws InterruptedException, SQLException {
        directionsForQuotas = new DirectionsForQuotas(driver);

        directionsForQuotas.CreateConsultationEquipment(true, "159 790 257 20",
                authorizationObject.Select("Женская консультация"),
                "Аорта",
                false,
                PMOTarget,
                authorizationObject.Select("A00.0 Холера, вызванная холерным вибрионом 01, биовар cholerae"),
                authorizationObject.Select(research), mass, false, true, false,"");

        Thread.sleep(1500);
        sql.StartConnection("select * from telmed.directions order by id desc limit 1;");
        while (sql.resultSet.next()) {
            directionGuid = sql.resultSet.getString("directionguid");
            DirectionId = sql.resultSet.getString("id");
        }
        System.out.println(directionGuid);

        sql.StartConnection("SELECT s.idschedule, sc.equipmentid, s.begindate, s.enddate, m.namemu MoDirection, m2.namemu mokvots, eq.moname morasp, s.iddirection FROM telmed.slots s\n" +
                "left join telmed.directions d on s.iddirection = d.id\n" +
                "left join dpc.mis_sp_mu m on d.requestermoid = m.idmu\n" +
                "left join dpc.mis_sp_mu m2 on s.distributeto_medicalidmu = m2.medicalidmu\n" +
                "join telmed.schedule sc on s.idschedule = sc.id\n" +
                "join telmed.equipmentregistry eq on sc.equipmentid = eq.id\n" +
                "where sc.equipmentid = "+equipmentId+" order by s.enddate desc limit 1;");
        while (sql.resultSet.next()) {
            Tomorrow = sql.resultSet.getString("enddate").substring(0,10);
        }
        System.out.println(Tomorrow);
    }
}
