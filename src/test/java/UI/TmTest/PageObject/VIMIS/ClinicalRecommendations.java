package UI.TmTest.PageObject.VIMIS;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class ClinicalRecommendations extends BaseTest {

    public ClinicalRecommendations(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * ВИМИС - Клинические рекомендации
     */
    @FindBy(xpath = "//a[@href='/vimis/clinical-recommendations']")
    public WebElement ClinicalRecommendations;
    public By ClinicalRecommendationsWait = By.xpath("//a[@href='/vimis/clinical-recommendations']");

    /**
     * Клинические рекомендации - Заголовок
     */
    public By Header = By.xpath("//h1[.='Клинические рекомендации']");

    /**
     * Клинические рекомендации - Фильтры
     */
    public By GetField (String value) {
        By field = By.xpath("//div[@class='form-item-label'][contains(.,'"+value+"')]/following-sibling::div//input");
        return field;
    }

    /**
     * Клинические рекомендации - Диагноз
     */
    public By CodeService = By.xpath("//div[@class='form-item-label'][contains(.,'Диагнозы')]/following-sibling::span//input");

    /**
     * Клинические рекомендации - Диагноз - Поле ввода
     */
    public By CodeServiceInput = By.xpath("//input[@placeholder='Введите код регистра']");

    /**
     * Клинические рекомендации - Дата начала - год назад
     */
    public By YearAgo = By.xpath("(//button[@class='el-picker-panel__icon-btn el-icon-d-arrow-left'])[1]");

    /**
     * Клинические рекомендации - Дата начала - месяц назад
     */
    public By MonthAgo = By.xpath("(//button[@class='el-picker-panel__icon-btn el-icon-arrow-left'])[1]");

    /**
     * Клинические рекомендации - Дата начала - Текст
     */
    public By Year = By.xpath("(//div[@class='el-date-range-picker__header'])[1]/div");

    /**
     * Клинические рекомендации - Дата начала - Январь
     */
    public By Year2020 = By.xpath("//div[@class='el-date-range-picker__header']/div[contains(.,'2020')]");

    /**
     * Клинические рекомендации - Дата начала - 1 число
     */
    public By First = By.xpath("(//td[@class='available']//span[contains(.,'1')])[1]");

    /**
     * Клинические рекомендации - Поиск
     */
    public By SearchWait = By.xpath("//button/span[contains(.,'Поиск')]");

    /**
     * Клинические рекомендации - Сбросить
     */
    public By Reset = By.xpath("//button/i[@class='fa fa-sync']");

    /**
     * Клинические рекомендации - Таблица - наименование
     */
    public By TableWaitName = By.xpath("//tbody/tr/td[2]");

    /**
     * Клинические рекомендации - Таблица - Возрастная группа
     */
    public By Tableold = By.xpath("//tbody/tr/td[3]");

    /**
     * Клинические рекомендации - Таблица - Диагноз
     */
    @FindBy(xpath = "//*[@id='table_diagnostics']/tbody/tr/td[4]")
    public WebElement TableDiagnosis;
    public By TableWaitDiagnosis = By.xpath("//*[@id='table_diagnostics']/tbody/tr/td[4]");

    /**
     * Клинические рекомендации - Таблица - Дата начала
     */
    public By TableDataStart = By.xpath("//table[@id='table_diagnostics']//tbody/tr/td[5]");

    /**
     * Клиническая рекомендация - Уровень убедительности
     */
    public By LevelPersuasion = By.xpath("//input[@placeholder='Уровень убедительности']");

    /**
     * Клиническая рекомендация - Уровень убедительности -  отображение в таблице
     */
    public By LevelPersuasionTable(String Doc) {
        By LevelPersuasion_Table = By.xpath(
                "//div[@class='el-card is-always-shadow']//h3[contains(.,'уровень убедительности -" + Doc + "')]");
        return LevelPersuasion_Table;
    }

    /**
     * Клиническая рекомендация - Уровень доказательсности
     */
    public By LevelProof = By.xpath("//input[@placeholder='Уровень доказательсности']");

    /**
     * Клиническая рекомендация - Уровень доказательсности - отображение в таблице
     */
    public By LevelProofTable(String Doc) {
        By LevelProof_Table = By.xpath(
                "//div[@class='el-card is-always-shadow']//h3[contains(.,'уровень доказательности -" + Doc + "')]");
        return LevelProof_Table;
    }

    /**
     * Клиническая рекомендация - Тезис-рекомендация
     */
    public By Tezis = By.xpath("//input[@placeholder='Тезис-рекомендация']");

    /**
     * Клиническая рекомендация - Уровень убедительности
     */
    public By TNM = By.xpath("//input[@placeholder='TNM']");

    /**
     * Клиническая рекомендация - Стадия опухолевого процесса
     */
    public By Stage = By.xpath("//input[@placeholder='Стадия опухолевого процесса']");

    /**
     * Клиническая рекомендация - Группировка
     */
    public By Group = By.xpath("//input[@placeholder='Группировка']");

    /**
     * Клиническая рекомендация - Группировка - отображение в таблице
     */
    public By GroupTable(String Doc) {
        By Group_Table = By.xpath("//div[@class='el-card is-always-shadow']//h3[text()='" + Doc + "']");
        return Group_Table;
    }

    /**
     * Клиническая рекомендация - Отображение на странице
     */
    public By CountList = By.xpath("//input[@placeholder='Выбрать']");

    /**
     * Клиническая рекомендация - Отображение на странице - 30 страниц
     */
    public By CountList30 = By.xpath("//div[@x-placement]//li[contains(.,'30 на странице')]");

    /**
     * Клиническая рекомендация - Следующая страница
     */
    public By NextPage = By.xpath("//ul[@class='el-pager']/li[@class='number active']/following-sibling::li[1]");

    /**
     * Клиническая рекомендация - Всего записей
     */
    public By AllCount = By.xpath("//span[@class='el-pagination__total']");
}
