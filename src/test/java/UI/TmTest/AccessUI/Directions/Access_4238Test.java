package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.BaseTest;
import Base.TestListener;
import Base.TestListenerApi;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationOutgoingUnfinished;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Удалённая_консультация")
@Tag("Карточка_консультации")
@Tag("Проверка_БД")
public class Access_4238Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    ConsultationOutgoingUnfinished consultationOU;

    @Issue(value = "TEL-4238")
    @Issue(value = "TEL-4242")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Link(name = "ТМС-2348", url = "https://team-1okm.testit.software/projects/5/tests/2348?isolatedSection=f7355014-59a9-48e1-9277-647a55510677")
    @DisplayName("Создание консультации с полями Вид консультации, Требуется консилиум, Требуется видео-консультация, Проводится консилиум")
    @Description("Создаём консультлацию выбираем нужные поля и проверяем отображение в карточке консультации и в бд")
    public void Access_4238 () throws Exception {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        consultationOU = new ConsultationOutgoingUnfinished(driver);

        System.out.println("Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Переходм в создание удалённой консультации");
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.CreateWait);
        ClickElement(directionsForQuotas.RemoteConsultationWait);
        ClickElement(directionsForQuotas.NextWait);
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys(PSnils_);
        WaitElement(directionsForQuotas.PatientDataWait);
        if (isElementVisibleTime(directionsForQuotas.CitizenshipError, 2)) {
            ClickElement(directionsForQuotas.Citizenship);
            ClickElement(authorizationObject.Select("РОССИЯ Российская Федерация"));
        }
        WaitElement(directionsForQuotas.NextWait);
        Thread.sleep(1000);
        actionElementAndClick(directionsForQuotas.Next);

        System.out.println("Заполняем данные");
        SelectClickMethod(directionsForQuotas.DoctorWait, directionsForQuotas.DoctorSearch("Хакимова"));
        ClickElement(directionsForQuotas.MyMO);
        SelectClickMethod(directionsForQuotas.MyDivision, authorizationObject.Select("Женская консультация"));
        SelectClickMethod(directionsForQuotas.ProfileWait, authorizationObject.Select("детской урологии-андрологии"));
        ClickElement(directionsForQuotas.FormConsul("Форма консультации", "плановая")); // Форма консультации
        SelectClickMethod(directionsForQuotas.Goal,
                authorizationObject.Select("Подозрение на COVID-19")); // Цель консультации
        inputWord(driver.findElement(directionsForQuotas.TypeConsul), "конс");
        ClickElement(authorizationObject.Select("Консультация детского уролога-андролога"));
        inputWord(directionsForQuotas.Diagnos, "Паратиф A");
        Thread.sleep(1000);
        ClickElement(authorizationObject.SelectFirst);
        ClickElement(directionsForQuotas.Hospital("Да"));

        System.out.println("Устанавливаем нужные параметры");
        // 1 - "Врач-врач", 2 - "Врач -Врач - Пациент", 3 - "Врач - Пациент"
        ClickElement(directionsForQuotas.FormConsul("Вид консультации", "Врач - Врач - Пациент"));
        ClickElement(directionsForQuotas.FormConsul("Требуется консилиум", "Да"));
        ClickElement(directionsForQuotas.FormConsul("Требуется видео-консультация", "Да"));
        ClickElement(directionsForQuotas.FormConsul("Пациент в стационаре", "Да"));

        System.out.println("Проверяем заполненные данные");
        ClickElement(directionsForQuotas.NextConsul);
        WaitElement(directionsForQuotas.TextWait("Вид консультации"));
        String vid = driver.findElement(directionsForQuotas.TextWait("Вид консультации")).getText();
        String consilium = driver.findElement(directionsForQuotas.TextWait("Требуется консилиум")).getText();
        String video = driver.findElement(directionsForQuotas.TextWait("Требуется видео-консультация")).getText();
        String inhospital = driver.findElement(directionsForQuotas.TextWait("Пациент в стационаре")).getText();

        Assertions.assertEquals(vid, "Врач - Врач - Пациент", "Вид консультации не совпадает");
        Assertions.assertEquals(consilium, "Да", "Требуется консилиум не совпадает");
        Assertions.assertEquals(video, "Да", "Требуется видео-консультация не совпадает");
        Assertions.assertEquals(inhospital, "Да", "Требуется видео-консультация не совпадает");

        ClickElement(directionsForQuotas.CreateConsul);
        System.out.println("Прикрепление  файла");
        WaitElement(directionsForQuotas.AddFilesWait);
        Thread.sleep(1000);
        File file = new File("src/test/resources/test.txt");
        directionsForQuotas.File.sendKeys(file.getAbsolutePath());

        System.out.println("Берём id записи");
        sql.StartConnection("Select * from telmed.directions order by id desc limit 1;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
        }

        System.out.println("Закрываем создание и переходим в исходящие незавершённые");
        ClickElement(directionsForQuotas.CloseReception);
        ClickElement(consultationOU.Consultation);
        ClickElement(consultationOU.SortDesc);
        ClickElement(consultationOU.Line(sql.value));

        Sql_4238("2", "1", "1", "2", "0");

        System.out.println("Проверяем карточку консультации");
        WaitElement(consultationOU.Consul("Вид консультации"));

        vid = driver.findElement(consultationOU.Consul("Вид консультации")).getText();
        consilium = driver.findElement(consultationOU.Consul("Требуется консилиум")).getText();
        String consiliumGo = driver.findElement(consultationOU.Consul("Проводится консилиум")).getText();
        inhospital = driver.findElement(consultationOU.Consul("Пациент в стационаре")).getText();

        Assertions.assertEquals(vid, "Врач - Врач - Пациент", "Вид консультации не совпадает");
        Assertions.assertEquals(consilium, "Да", "Требуется консилиум не совпадает");
        Assertions.assertEquals(consiliumGo, "Нет", "Проводится консилиум не совпадает");
        Assertions.assertEquals(inhospital, "Да", "Требуется видео-консультация не совпадает");

        System.out.println("Изменяем данные в карточке консультации");
        ClickElement(consultationOU.ConsulButton("Вид консультации"));
        SelectClickMethod(consultationOU.ClickSelect, authorizationObject.Select("Врач - врач"));

        ClickElement(consultationOU.ConsulButton("Проводится консилиум"));
        SelectClickMethod(consultationOU.ClickSelect, authorizationObject.Select("Да"));

        Sql_4238("1", "1", "1", "2", "1");
    }

    @Step("метод проверки консультации в бд с новыми полями")
    public void Sql_4238 (String vid, String cons, String video, String hospital, String nowconsilium) throws SQLException {
        String vidSql = null;
        String consiliumSql = null;
        String videoSql = null;
        String inhospitalSql = null;
        String nowconsiliumSql = null;

        sql.StartConnection(
                "select id, d.directionguid, d.typedirection, d.needconsilium, d.requirevideoconsult, d.nowconsilium, d.inhospital from telmed.directions d\n" +
                        "where id = '" + sql.value + "';");
        while(sql.resultSet.next()) {
            vidSql = sql.resultSet.getString("typedirection");
            consiliumSql = sql.resultSet.getString("needconsilium");
            videoSql = sql.resultSet.getString("requirevideoconsult");
            inhospitalSql = sql.resultSet.getString("inhospital");
            nowconsiliumSql = sql.resultSet.getString("nowconsilium");
        }

        Assertions.assertEquals(vid, vidSql, "Вид консультации не совпадает");
        Assertions.assertEquals(cons, consiliumSql, "Требуется консилиум не совпадает");
        Assertions.assertEquals(video, videoSql, "Требуется видео-консультация не совпадает");
        Assertions.assertEquals(hospital, inhospitalSql, "Требуется видео-консультация не совпадает");
        Assertions.assertEquals(nowconsilium, nowconsiliumSql, "Проводится консилиум не совпадает");
    }
}