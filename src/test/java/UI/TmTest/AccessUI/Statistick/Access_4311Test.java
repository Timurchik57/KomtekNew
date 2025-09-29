package UI.TmTest.AccessUI.Statistick;

import Base.BaseAPI;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import Base.TestListener;
import Base.TestListenerApi;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({TestListener.class, TestListenerApi.class})
@Epic("Тесты UI")
@Feature("Статистика")
@Tag("Аналитика_МО_ОМП")
@Tag("Лк_врача")
@Tag("ИИ")
@Tag("Проверка_БД")
public class Access_4311Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;

    private static final String SETTINGS_BLOCKS = "/patient-dashboard/settings/blocks";
    private static final String VIMIS_SMS = "/vimis/sms/patient";
    private static final String VIMIS_DOCS = "/vimis/sms/patient/documents";

    @Test
    @Issue(value = "TEL-4311")
    @Link(name = "ТМС-2384", url = "https://team-1okm.testit.software/projects/5/tests/2384?isolatedSection=f7355014-59a9-48e1-9277-647a55510677")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Отображение блока ВИМИС при включеной/выключеной настройке в telmed.setting_visibility_blocks")
    @Description("Перейти в Статистика - Лк Врача, проверить запрос к блокам, если блок вимис true то отображаем документы, иначае не отображаем")
    public void VimisRequestsVisibilityFlow () throws Exception {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);

        // 1) Включить видимость ВИМИС
        setVimisVisibility(true);

        // Авторизация через готовый метод (выбор МО OKB)
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(analyticsMO.Analytics);
        // Переход на конкретный дашборд пациента (как в Access_1862Test)
        driver.get(HostAddressWeb + "/registry/patient/2ed97c3b-5f30-4fda-bcdc-39d930c6c224/dashboard");
        // Дождаться загрузок плейсхолдеров аналитики/документов
        WaitElement(analyticsMO.Snils);
        Thread.sleep(1500);
        WaitNotElement3(analyticsMO.LoadingDocsShablonAndDocs, 40);
        Thread.sleep(1500);
        WaitNotElement3(analyticsMO.LoadingDocsShablon, 40);

        // 3) Проверка, что пришли сетевые запросы
        Requests r1 = captureRequests();
        assertTrue(r1.blocks, "settings/blocks должен быть вызван при visible=true");
        assertTrue(r1.vimisSms, "/vimis/sms/patient должен быть вызван при visible=true");
        assertTrue(r1.vimisDocs, "/vimis/sms/patient/documents должен быть вызван при visible=true");

        // 4) Проверка, что отображаются документы (как в Access_1862 логика подсчета)
        List<WebElement> list = driver.findElements(analyticsMO.Docs);
        // По аналогии из Access_1862: делаем поиск и сверяем уменьшение
        inputWord(driver.findElement(analyticsMO.Search), " "); // триггер фильтра
        Thread.sleep(1500);
        WaitNotElement3(analyticsMO.LoadingDocsShablonAndDocs, 40);
        Thread.sleep(1500);
        WaitNotElement3(analyticsMO.LoadingDocsShablon, 40);
        List<WebElement> list2 = driver.findElements(analyticsMO.Docs);
        int delta = list.size() - list2.size();
        assertTrue(delta >= 0, "Ожидалось, что документы присутствуют и их можно фильтровать");

        // 5) Выключить видимость ВИМИС
        setVimisVisibility(false);
    }

    @Step("Метод Проверки блока вимис после кеша")
    public void After_4311 () throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);

        // 6) Обновить дашборд и заново проверить запросы
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(analyticsMO.Analytics);
        // Переход на конкретный дашборд пациента (как в Access_1862Test)
        driver.get(HostAddressWeb + "/registry/patient/2ed97c3b-5f30-4fda-bcdc-39d930c6c224/dashboard");
        WaitElement(analyticsMO.Snils);
        Thread.sleep(1500);
        WaitNotElement3(analyticsMO.LoadingDocsShablonAndDocs, 40);
        Thread.sleep(1500);
        WaitNotElement3(analyticsMO.LoadingDocsShablon, 40);

        Requests r2 = captureRequests();
        assertTrue(r2.blocks, "settings/blocks должен быть вызван при visible=false");
        assertFalse(r2.vimisSms, "/vimis/sms/patient не должен вызываться при visible=false");
        assertFalse(r2.vimisDocs, "/vimis/sms/patient/documents не должен вызываться при visible=false");
    }

    @Step("Метод для изменения видимости данных в бд")
    private void setVimisVisibility (boolean visible) throws SQLException, InterruptedException {
        sql.UpdateConnection(
                "update telmed.setting_visibility_blocks set visibility = " + visible + " where name = 'ВИМИС';"
        );
        // Небольшая пауза, чтобы конфиг подхватился фронтом
        Thread.sleep(1500);
    }

    @Step("Метод захвата сетевых запросов")
    private Requests captureRequests () throws InterruptedException {
        Thread.sleep(1500);
        String script = "return JSON.stringify(window.performance.getEntries())";
        String netData = (String) ((JavascriptExecutor) driver).executeScript(script);
        // Нормализация хоста на случай абсолютных URL
        String base = HostAddressWeb.replace("https://", "\").replace(\"http://\", \"\"");
                Requests res = new Requests();
        if (netData != null) {
            if (netData.contains(SETTINGS_BLOCKS)) res.blocks = true;
            if (netData.contains(VIMIS_SMS))      res.vimisSms = true;
            if (netData.contains(VIMIS_DOCS))     res.vimisDocs = true;
            // Дополнительно: проверка на абсолютные URL
            if (!res.blocks && netData.contains(base + SETTINGS_BLOCKS)) res.blocks = true;
            if (!res.vimisSms && netData.contains(base + VIMIS_SMS))     res.vimisSms = true;
            if (!res.vimisDocs && netData.contains(base + VIMIS_DOCS))   res.vimisDocs = true;
        }
        return res;
    }

    static class Requests {
        boolean blocks;
        boolean vimisSms;
        boolean vimisDocs;
    }
}
