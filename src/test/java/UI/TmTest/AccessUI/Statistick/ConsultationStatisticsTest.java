package UI.TmTest.AccessUI.Statistick;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Statistics.StatisticsConsultation;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Статистика")
public class ConsultationStatisticsTest extends BaseAPI {
    AuthorizationObject authorizationObject;
    StatisticsConsultation statisticsConsultation;

    @Description("Авторизация и переход в Детальный отчёт по консультациям. Проверяем корректное отображение, за выбранный период")
    @DisplayName("Проверка отображения детального отчёта по консультации")
    @Test
    public void ConsultationStatistic() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        statisticsConsultation = new StatisticsConsultation(driver);
        System.out.println("Проверка отображения детального отчёта по консультации");

        /** Авторизация */
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(statisticsConsultation.ConsultationWait);
        ClickElement(statisticsConsultation.HeaderReportWait);
        ClickElement(statisticsConsultation.DateStartWait);

        System.out.println("Вводим период");
        ClickElement(statisticsConsultation.FirstMonth);
        ClickElement(statisticsConsultation.ToDay);
        SelectClickMethod(statisticsConsultation.MO, statisticsConsultation.SelectMO);
        statisticsConsultation.Search.click();

        /** Проверка */
        WaitElement(statisticsConsultation.Table);
    }
}
