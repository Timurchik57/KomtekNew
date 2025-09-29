package UI.TmTest.AccessUI.Statistick;

import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import Base.BaseAPI;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Статистика")
@Tag("Аналитика_МО_ОМП")
public class Access_1559Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;

    @Test
    @Issue(value = "TEL-1559")
    @Issue(value = "TEL-1603")
    @Issue(value = "TEL-1685")
    @Link(name = "ТМС-1724", url = "https://team-1okm.testit.software/projects/5/tests/1724?isolatedSection=a612d4c1-8453-43f3-b70f-551c6e8f4cd3")
    @Link(name = "ТМС-1739", url = "https://team-1okm.testit.software/projects/5/tests/1739?isolatedSection=a612d4c1-8453-43f3-b70f-551c6e8f4cd3")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Проверка отображения блоков в ЛК Врача")
    @Description("Переходим в Аналитика Мо по ОМП, выбираем блок где есть маршруты, переходим к пациенту и проверяем отсутствие блоков")
    public void Access_1559() throws SQLException, InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);

        System.out.println("Проверяем в БД, что не включена видимсть блоков");
        sql.UpdateConnection(
                "update telmed.setting_visibility_blocks set visibility = false where name = 'Диспансеризация';");
        sql.UpdateConnection(
                "update telmed.setting_visibility_blocks set visibility = false where name = 'Иммунизация';");
        sql.UpdateConnection("update telmed.setting_visibility_blocks set visibility = true where name = 'РРЭМД';");
        sql.UpdateConnection(
                "update telmed.setting_visibility_blocks set visibility = false where name = 'Вакцинация COVID-19';");

        System.out.println("Авторизуемся и переходим в Статистика - Аналитика МО по ОМП");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(analyticsMO.Analytics);

        System.out.println("Проверяем где есть маршруты, во всех блоках");
        WaitElement(analyticsMO.Tall);
        WaitElement(analyticsMO.Average);
        WaitElement(analyticsMO.low);
        analyticsMO.QuantityStackMethod();
        System.out.println(AnalyticsMO.TallMO);
        System.out.println(AnalyticsMO.AverageMO);
        System.out.println(AnalyticsMO.lowMO);

        System.out.println("Переходим в первый попавшийся блок, у которого есть мо с этапами");
        if (AnalyticsMO.TallMO) {
            ClickElement(analyticsMO.NameMOTallFirst);
        } else {
            if (AnalyticsMO.AverageMO) {
                ClickElement(analyticsMO.NameMOAverageFirst);
            } else {
                ClickElement(analyticsMO.NameMOlowFirst);
            }
        }
        System.out.println("Выбираем первого пациента и переходим к нему");
        ClickElement(analyticsMO.FirstPatient);
        Thread.sleep(1500);
        WaitElement(analyticsMO.Snils);
        WaitNotElement(analyticsMO.MedicalExamination);
        WaitNotElement(analyticsMO.Immunization);
        WaitElement(analyticsMO.RREMD);
        WaitNotElement(analyticsMO.Vaccination);
    }
}
