package UI.TmTest.PageObject.NSI;

import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;


public class DiagnosesOfConsultations extends BaseTest {
    /**
     * НСИ - Диагнозы для Консультаций
     */
    public DiagnosesOfConsultations(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * НСИ - Диагнозы для Консультаций
     */
    public By DiagnosesOfConsultationslink = By.xpath("//a[@href='/nsi/consultation-diagnoses']");

    /**
     * НСИ - Диагнозы для Консультаций - Редактировать последнее ограничение
     */
    public By EditLast = By.xpath("//tbody/tr[last()]/td//button[@tooltiptext='Редактировать']");

    /**
     * Диагнозы для Консультаций - Добавить ограничение
     */
    public By AddLimitation = By.xpath("//button[contains(.,' Добавить ограничение')]");

    /**
     * Диагнозы для Консультаций - Добавить ограничение
     */
    public By AddLimitationHeader = By.xpath("//span[contains(.,'Редактировать ограничение')]");

    /**
     * Диагнозы для Консультаций - Добавить ограничение - Селект
     */
    public By Select (String value) {
        By Select = By.xpath("//label[contains(.,'"+value+"')]/following-sibling::div//input");
        return Select;
    }

    /**
     * Диагнозы для Консультаций - Добавить ограничение - Добавить
     */
    public By ADD = By.xpath("//div[@class='el-dialog__footer']//button[contains(.,'Добавить')]");

    /**
     * Диагнозы для Консультаций - Добавить ограничение - Добавить
     */
    public By Update = By.xpath("//div[@class='el-dialog__footer']//button[contains(.,'Обновить')]");

    /**
     * Диагнозы для Консультаций - Всего записей
     */
    public By CountConsul = By.xpath("//span[@class='el-pagination__total']");

    @Step("Метод добавления ограничения для Удалённой консультации")
    public void AddConsul (String diagnose, String mo, String profile, String purpose) throws InterruptedException{

        AuthorizationObject authorizationObject = new AuthorizationObject(driver);

        ClickElement(AddLimitation);

        ClickElement(Select("Диагнозы"));
        ClickElement(authorizationObject.Select(diagnose));

        ClickElement(Select("МО"));
        ClickElement(authorizationObject.Select(mo));

        ClickElement(Select("Профиль"));
        ClickElement(authorizationObject.Select(profile));

        ClickElement(Select("Цель консультации"));
        ClickElement(authorizationObject.Select(purpose));

        ClickElement(ADD);
        Thread.sleep(2000);
    }
}
