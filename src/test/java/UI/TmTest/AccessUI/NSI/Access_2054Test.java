package UI.TmTest.AccessUI.NSI;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.NSI.DiagnosesOfConsultations;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("НСИ")
@Tag("Проверка_БД")
@Tag("Диагнозы_для_консультаций")
@Tag("Основные")
public class Access_2054Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    DiagnosesOfConsultations diagnosesOfCons;
    String MO;
    String ID;
    String Diagnoses;
    String Profile;
    String Purpose;

    @Issue(value = "TEL-2054")
    @Issue(value = "TEL-2055")
    @Link(name = "ТМС-1886", url = "https://team-1okm.testit.software/projects/5/tests/1886?isolatedSection=767caae3-1d53-4e9c-a277-e3122b6d6370")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Формирование записей в НСИ - Диагнозы для консультаций")
    @Description("Добавляем ограничения по консультациям")
    public void Access_2054() throws InterruptedException, SQLException {

        authorizationObject = new AuthorizationObject(driver);
        diagnosesOfCons = new DiagnosesOfConsultations(driver);
        String mo = "";
        if (KingNumber == 12) {
            mo = "ГБУЗ \"Чукотская окружная больница\" г. Анадырь";
        } else {
            mo = "БУ ХМАО-Югры \"Окружная клиническая больница\"";
        }

        System.out.println("Авторизуемся");
        AuthorizationMethod(authorizationObject.Select(mo));

        System.out.println("Первая проверка - проверяем количество всех записей в Диагнозы для консультаций");
        ClickElement(diagnosesOfCons.DiagnosesOfConsultationslink);
        Thread.sleep(1500);
        WaitElement(diagnosesOfCons.CountConsul);
        String Count = driver.findElement(diagnosesOfCons.CountConsul).getText();
        sql.StartConnection("select count(msm.namemu) from telmed.consultation_diagnoses cd\n" +
                "join dpc.mis_sp_mu msm on cd.medicalidmu = msm.medicalidmu\n" +
                "join telmed.profiledirectory p on cd.profileid = p.id\n" +
                "join telmed.consultationpurpose c on cd.consultationpurposeid = c.id ;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }
        Assertions.assertEquals(Count.substring(6), sql.value,
                "Количество всех ограничений по консультациям не совпадает");

        System.out.println("Вторая проверка - Создаём ограничение и проверяем данные в БД");
        diagnosesOfCons.AddConsul("Холера, вызванная холерным вибрионом 01, биовар cholerae",
                mo, "авиационной и космической медицине",
                "Подозрение на COVID-19");

        sql.StartConnection("SELECT * FROM dpc.mkb10 x\n" +
                "WHERE mkb_name = 'Холера, вызванная холерным вибрионом 01, биовар cholerae';");
        while (sql.resultSet.next()){
            sql.value = sql.resultSet.getString("mkb_code");
        }

        sql.StartConnection(
                "select cd.id, msm.namemu, cd.diagnoses, p.\"name\", c.purpose from telmed.consultation_diagnoses cd\n" +
                        "join dpc.mis_sp_mu msm on cd.medicalidmu = msm.medicalidmu\n" +
                        "join telmed.profiledirectory p on cd.profileid = p.id\n" +
                        "join telmed.consultationpurpose c on cd.consultationpurposeid = c.id order by cd.id desc limit 1;");
        while (sql.resultSet.next()) {
            ID = sql.resultSet.getString("id");
            MO = sql.resultSet.getString("namemu");
            Diagnoses = sql.resultSet.getString("diagnoses");
            Profile = sql.resultSet.getString("name");
            Purpose = sql.resultSet.getString("purpose");
        }
        Assertions.assertEquals(MO, mo, "МО не совпадает");
        Assertions.assertEquals(Diagnoses, "{" + sql.value + "}", "Диагноз не совпадает");
        Assertions.assertEquals(Profile, "авиационной и космической медицине", "Профиль не совпадает");
        Assertions.assertEquals(Purpose, "Подозрение на COVID-19", "Цель не совпадает");

        sql.UpdateConnection("Delete from telmed.consultation_diagnoses where id = " + ID + "");
    }
}
