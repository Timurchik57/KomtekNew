package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationArchived;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Удалённая_консультация")
@Tag("Проверка_интерфейса")
public class ConsultationArchivedTest extends BaseAPI {
    AuthorizationObject authorizationObject;
    ConsultationArchived consultationArchived;

    @Test
    @DisplayName("Проверка отображения консультаций за определённое время")
    @Description("Переходим в Направления - Консультации - Входящие - Архивные и смотрим, что консультации прогружаются менее чем за 20 секунд")
    public void ConsultationArchived() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        consultationArchived = new ConsultationArchived(driver);

        if (KingNumber != 4) {
            System.out.println("Проверка отображения консультаций за определённое время");
            /**  Переход в Направления - Консультации - Входящие - Архивные **/
            AuthorizationMethod(authorizationObject.OKB);
            WaitElement(consultationArchived.ArchivedWait);
            actionElementAndClick(consultationArchived.Archived);
            WaitElement(consultationArchived.HeaderWait);

            System.out.println("Проверка отображения 1 и последней записи");
            WaitElement(consultationArchived.FirstWait);
            WaitElement(consultationArchived.LastWait);
            System.out.println("Записи отображаются менее чем за 20 секунд");
        }
    }
}
