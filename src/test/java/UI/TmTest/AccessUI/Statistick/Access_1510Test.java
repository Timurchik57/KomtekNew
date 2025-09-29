package UI.TmTest.AccessUI.Statistick;

import Base.SQL;
import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.EquipmentSchedule;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import Base.BaseAPI;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Статистика")
@Tag("Аналитика_МО_ОМП")
@Tag("Удалённая_консультация")
@Tag("Консультация_на_оборудование")
@Tag("Основные")
public class Access_1510Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;
    SQL sql;
    DirectionsForQuotas directionsForQuotas;
    EquipmentSchedule equipmentSchedule;

    @Step("Метод для выбора направления")
    public void ChoiceDirections(By Research, Boolean Direction) throws InterruptedException, IOException, SQLException {
        analyticsMO = new AnalyticsMO(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);

        String mo = null;
        String equipment = null;

        if (KingNumber == 1 || KingNumber == 2) {
            mo = "БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"";
            equipment = "X-OMAT";
        }
        if (KingNumber == 4) {
            mo = "АУ ХМАО-Югры \"Кондинская районная стоматологическая поликлиника\"";
            equipment = "CDR Kit";
        }

        WaitElement(analyticsMO.Analytics);
        //Специальный пациент для проверки ЛК, небольшое количество СЭМД
        driver.get(HostAddressWeb + "/registry/patient/2ed97c3b-5f30-4fda-bcdc-39d930c6c224/dashboard");
        WaitElement(analyticsMO.Snils);
        ClickElement(analyticsMO.Action);

        System.out.println("Выбираем создание направления на диагностику");
        ClickElement(Research);
        Thread.sleep(1500);
        if (Direction) {
            directionsForQuotas.CreateConsultationEquipment(false, Snils,
                    authorizationObject.Select("Женская консультация"), "Аорта", false, mo,
                    authorizationObject.Select("A00.0 Холера, вызванная холерным вибрионом 01, биовар cholerae"),
                    authorizationObject.Select("HMP01"), "50", false, true, true, equipment);
        } else {
            directionsForQuotas.CreateRemoteConsul(true, "", "159 790 257 20", "Зотин Андрей Владимирович", "Женская консультация", "детской урологии-андрологии", "плановая", "Подозрение на COVID-19", "Паратиф A");
            Thread.sleep(500);
            ClickElement(directionsForQuotas.SendConsul);
            Thread.sleep(2000);
        }
    }

    @Test
    @Issue(value = "TEL-1510")
    @Issue(value = "TEL-1836")
    @Link(name = "ТМС-1564", url = "https://team-1okm.testit.software/projects/5/tests/1564?isolatedSection=f07b5d61-7df7-4e90-9661-3fd312065912")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Создание направления на диагностику/консультацию через ЛК Врача")
    @Description("Переходим в Аналитика Мо по ОМП, выбираем блок гдк есть маршруты, переходим к пациенту, нажимаем действия и создаём направление на диагностику, консультацию")
    public void Access_1510() throws InterruptedException, IOException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);
        sql = new SQL();
        directionsForQuotas = new DirectionsForQuotas(driver);

        System.out.println("Авторизуемся и переходим в Статистика - Аналитика МО по ОМП");
        AuthorizationMethod(authorizationObject.OKB);
        ChoiceDirections(analyticsMO.ActionAddDiagnostic, true);
        ChoiceDirections(analyticsMO.ActionAddKonsul, false);
    }
}
