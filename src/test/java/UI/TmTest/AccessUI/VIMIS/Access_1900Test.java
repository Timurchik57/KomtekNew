package UI.TmTest.AccessUI.VIMIS;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.VIMIS.ClinicalRecommendations;
import io.qameta.allure.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Вимис")
@Tag("Клинические_рекомендации")
@Tag("Тезис_Клинические_рекомендации")
@Disabled
public class Access_1900Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    ClinicalRecommendations clinicalRec;

    @Issue(value = "TEL-1900")
    @Link(name = "ТМС-1795", url = "https://team-1okm.testit.software/projects/5/tests/1795?isolatedSection=1f9b0804-847c-4b2c-8be6-2d2472e56a75")
    @Owner(value = "Галиакберов Тимур")
    @Description("Авторизация и переход в ВИМИС - Клинические рекомендации. Проверяем отображение данных по всем фильтрам")
    @DisplayName("Поля фильтрации в Клинических рекомендациях")
    @Test
    public void Access_1900() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        clinicalRec = new ClinicalRecommendations(driver);

        System.out.println("Переход в ВИМИС - Клинические рекомендации");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(clinicalRec.ClinicalRecommendationsWait);
        ClickElement(clinicalRec.GetField("Направление"));
        ClickElement(authorizationObject.Select("1 - Онкология"));
        SelectClickMethod(clinicalRec.GetField("Тип клинической рекомендации"), authorizationObject.Select("Федеральный"));
        ClickElement(clinicalRec.SearchWait);

        System.out.println("Переходим по первому наименованию");
        ClickElement(clinicalRec.TableWaitName);

        System.out.println("Проверяем Уровень убедительности - '-'");
        Thread.sleep(1500);
        authorizationObject.LoadingTime(20);
        WaitElement(clinicalRec.LevelPersuasion);
        SelectClickMethod(clinicalRec.LevelPersuasion, authorizationObject.Select("-"));
        if (!isElementNotVisible(clinicalRec.LevelPersuasionTable(" ,"))) {
            System.out.println("Нет записи с -");
        }
        WaitNotElement(clinicalRec.LevelPersuasionTable(" A"));
        WaitNotElement(clinicalRec.LevelPersuasionTable(" B"));
        WaitNotElement(clinicalRec.LevelPersuasionTable(" C"));

        System.out.println("Проверяем Уровень убедительности - A");
        SelectClickMethod(clinicalRec.LevelPersuasion, authorizationObject.Select("А"));
        WaitNotElement(clinicalRec.LevelPersuasionTable(" ,"));
        if (!isElementNotVisible(clinicalRec.LevelPersuasionTable(" A"))) {
            System.out.println("Нет записи с A");
        }
        WaitNotElement(clinicalRec.LevelPersuasionTable(" B"));
        WaitNotElement(clinicalRec.LevelPersuasionTable(" C"));

        System.out.println("Проверяем Уровень убедительности - B");
        SelectClickMethod(clinicalRec.LevelPersuasion, authorizationObject.Select("В"));
        WaitNotElement(clinicalRec.LevelPersuasionTable(" ,"));
        WaitNotElement(clinicalRec.LevelPersuasionTable(" A"));
        if (!isElementNotVisible(clinicalRec.LevelPersuasionTable(" В"))) {
            System.out.println("Нет записи с В");
        }
        WaitNotElement(clinicalRec.LevelPersuasionTable(" C"));

        System.out.println("Проверяем Уровень убедительности - C");
        SelectClickMethod(clinicalRec.LevelPersuasion, authorizationObject.Select("С"));
        WaitNotElement(clinicalRec.LevelPersuasionTable(" ,"));
        WaitNotElement(clinicalRec.LevelPersuasionTable(" A"));
        WaitNotElement(clinicalRec.LevelPersuasionTable(" B"));
        if (!isElementNotVisible(clinicalRec.LevelPersuasionTable(" C"))) {
            System.out.println("Нет записи с C");
        }

        driver.navigate().refresh();
        Thread.sleep(1500);
        authorizationObject.LoadingTime(20);
        authorizationObject.LoadingTime(20);

        System.out.println("Проверяем Уровень доказательсности - '-'");
        SelectClickMethod(clinicalRec.LevelProof, authorizationObject.Select("-"));
        WaitNotElement(clinicalRec.LevelProofTable(" 1"));
        WaitNotElement(clinicalRec.LevelProofTable(" 2"));
        WaitNotElement(clinicalRec.LevelProofTable(" 3"));
        WaitNotElement(clinicalRec.LevelProofTable(" 4"));
        WaitNotElement(clinicalRec.LevelProofTable(" 5"));
        if (!isElementNotVisible(clinicalRec.LevelProofTable(""))) {
            System.out.println("Нет записи с -");
        }

        System.out.println("Проверяем Уровень доказательсности - 1");
        SelectClickMethod(clinicalRec.LevelProof, authorizationObject.Select("1"));
        WaitNotElement(clinicalRec.LevelProofTable(" 2"));
        WaitNotElement(clinicalRec.LevelProofTable(" 3"));
        WaitNotElement(clinicalRec.LevelProofTable(" 4"));
        WaitNotElement(clinicalRec.LevelProofTable(" 5"));
        if (!isElementNotVisible(clinicalRec.LevelProofTable(" 1"))) {
            System.out.println("Нет записи с 1");
        }

        System.out.println("Проверяем Уровень доказательсности - 2");
        SelectClickMethod(clinicalRec.LevelProof, authorizationObject.Select("2"));
        WaitNotElement(clinicalRec.LevelProofTable(" 1"));
        WaitNotElement(clinicalRec.LevelProofTable(" 3"));
        WaitNotElement(clinicalRec.LevelProofTable(" 4"));
        WaitNotElement(clinicalRec.LevelProofTable(" 5"));
        if (!isElementNotVisible(clinicalRec.LevelProofTable(" 2"))) {
            System.out.println("Нет записи с 2");
        }

        System.out.println("Проверяем Уровень доказательсности - 3");
        SelectClickMethod(clinicalRec.LevelProof, authorizationObject.Select("3"));
        WaitNotElement(clinicalRec.LevelProofTable(" 1"));
        WaitNotElement(clinicalRec.LevelProofTable(" 2"));
        WaitNotElement(clinicalRec.LevelProofTable(" 4"));
        WaitNotElement(clinicalRec.LevelProofTable(" 5"));
        if (!isElementNotVisible(clinicalRec.LevelProofTable(" 3"))) {
            System.out.println("Нет записи с 3");
        }

        System.out.println("Проверяем Уровень доказательсности - 4");
        SelectClickMethod(clinicalRec.LevelProof, authorizationObject.Select("4"));
        WaitNotElement(clinicalRec.LevelProofTable(" 1"));
        WaitNotElement(clinicalRec.LevelProofTable(" 2"));
        WaitNotElement(clinicalRec.LevelProofTable(" 3"));
        WaitNotElement(clinicalRec.LevelProofTable(" 5"));
        if (!isElementNotVisible(clinicalRec.LevelProofTable(" 4"))) {
            System.out.println("Нет записи с 4");
        }

        System.out.println("Проверяем Уровень доказательсности - 5");
        SelectClickMethod(clinicalRec.LevelProof, authorizationObject.Select("5"));
        WaitNotElement(clinicalRec.LevelProofTable(" 1"));
        WaitNotElement(clinicalRec.LevelProofTable(" 2"));
        WaitNotElement(clinicalRec.LevelProofTable(" 3"));
        WaitNotElement(clinicalRec.LevelProofTable(" 4"));
        if (!isElementNotVisible(clinicalRec.LevelProofTable(" 5"))) {
            System.out.println("Нет записи с 5");
        }

        driver.navigate().refresh();
        Thread.sleep(1500);
        authorizationObject.LoadingTime(20);
        authorizationObject.LoadingTime(20);

        System.out.println("Проверяем Группировка - По этапу");
        SelectClickMethod(clinicalRec.Group, authorizationObject.Select("по этапу"));
        if (!isElementNotVisible(clinicalRec.GroupTable("Нет этапа"))) {
            WaitElement(clinicalRec.GroupTable("Лечение"));
            WaitElement(clinicalRec.GroupTable("Диагностика"));
            WaitElement(clinicalRec.GroupTable("Диспансерное наблюдение"));
            WaitElement(clinicalRec.GroupTable("Реабилитация"));
        }

        System.out.println("Проверяем Группировка - По уровню убедительности");
        SelectClickMethod(clinicalRec.Group, authorizationObject.Select("по уровню убедительности"));
        WaitElement(clinicalRec.GroupTable(""));
        WaitElement(clinicalRec.GroupTable("A"));
        WaitElement(clinicalRec.GroupTable("B"));
        WaitElement(clinicalRec.GroupTable("C"));

        System.out.println("Проверяем Группировка - По уровню докаказтельности");
        SelectClickMethod(clinicalRec.Group, authorizationObject.Select("по уровню докаказтельности"));
        WaitElement(clinicalRec.GroupTable("1"));
        WaitElement(clinicalRec.GroupTable("2"));
        WaitElement(clinicalRec.GroupTable("3"));
        WaitElement(clinicalRec.GroupTable("4"));
        WaitElement(clinicalRec.GroupTable("5"));
        WaitElement(clinicalRec.GroupTable(""));
    }
}
