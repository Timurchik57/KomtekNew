package UI.TmTest.PageObject.Statistics;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class StatisticsConsultation extends BaseTest {
    public StatisticsConsultation(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Статистика
     */
    public By Statistics = By.xpath("(//span[contains(.,'по консультациям')])[2]");
    /**
     * Статистика по консультациям
     */
    @FindBy(xpath = "//a[@href='/stats/consultations']")
    public WebElement Consultation;
    public By ConsultationWait = By.xpath("//a[@href='/stats/consultations']");
    /**
     * Поиск
     */
    public By SearchWait1 = By.xpath("(//button//span[@class='hidden-md'][contains(.,'Поиск')])[1]");
    /**
     * Excel
     */
    public By AddExcel = By.xpath("(//button[contains(.,'Excel')])[1]");
    /**
     * Статистика по консультациям - Детальный отчёт
     */
    @FindBy(xpath = "//div[@role]//div[contains(.,'Детальный отчёт')]")
    public WebElement HeaderReport;
    public By HeaderReportWait = By.xpath("//div[@role]//div[contains(.,'Детальный отчёт')]");
    /**
     * Детальный отчёт - C какого числа
     */
    @FindBy(xpath = "(//input[@placeholder='C какого числа'])[2]")
    public WebElement DateStart;
    public By DateStartWait = By.xpath("(//input[@placeholder='C какого числа'])[2]");
    public By DateStartWait2 = By.xpath(
            "(//div[@class='form-item-label'][contains(.,'Период')])[1]/following-sibling::div/input[1]");

    /**
     * Детальный отчёт - Нужная дата
     */
    public By TrueDateWait = By.xpath(
            "(//div[@class='el-picker-panel__content el-date-range-picker__content is-left']//div[contains(.,'2023  Май')])[2]");
    /**
     * Детальный отчёт - 1 число текущего месяца
     */
    public By FirstMonth = By.xpath("(//tbody//td[@class='available'])[1]");

    /**
     * Детальный отчёт - текущая дата
     */
    public By ToDay = By.xpath("//tbody//td[@class='available today']");

    /**
     * Детальный отчёт - Нужная дата - Перекулючатель назад
     */
    @FindBy(xpath = "//div[@class='el-picker-panel__content el-date-range-picker__content is-left']//button[@class='el-picker-panel__icon-btn el-icon-arrow-left']")
    public WebElement ButtonBack;
    /**
     * Детальный отчёт - Нужная дата - 5 число
     */
    @FindBy(xpath = "(//div[@class='el-picker-panel__content el-date-range-picker__content is-left']//table//span[contains(.,' 5')])[1]")
    public WebElement Day5;
    public By Day5Wait = By.xpath(
            "(//div[@class='el-picker-panel__content el-date-range-picker__content is-left']//table//span[contains(.,' 5')])[1]");

    /**
     * Детальный отчёт - Нужная дата - 19 число
     */
    @FindBy(xpath = "//div[@class='el-picker-panel__content el-date-range-picker__content is-left']//table//span[contains(.,'19')]")
    public WebElement Day19;

    /**
     * Детальный отчёт - Введите название мед. организации
     */
    public By MO = By.xpath("(//input[@placeholder='Введите название мед. организации'])[2]");
    public By SelectMO = By.xpath("(//li//span[contains(.,'БУ ХМАО-Югры \"Окружная клиническая больница\"')])[3]");

    /**
     * Детальный отчёт - Поиск
     */
    @FindBy(xpath = "(//button//span[@class='hidden-md'][contains(.,'Поиск')])[2]")
    public WebElement Search;
    public By SearchWait = By.xpath("(//button//span[@class='hidden-md'][contains(.,'Поиск')])[2]");
    /**
     * Excel
     */
    public By AddExcel2 = By.xpath("(//button[contains(.,'Excel')])[2]");
    /**
     * Excel - ДА
     */
    public By AddExcelYes = By.xpath("//div[@class='el-message-box__btns']/button[contains(.,'Да')]");
    /**
     * Поиск - За указанный период с 05.05.2022 по 19.05.2022
     */
    public By Period = By.xpath("//td[contains(.,'За указанный период с 05.05.2022 по 19.05.2022')]");
    /**
     * Поиск -  05.05.2022
     */
    public By Period5 = By.xpath("//td[contains(.,'05.05.2022')]");
    /**
     * Поиск -  06.05.2022
     */
    public By Period6 = By.xpath("//td[contains(.,'06.05.2022')]");
    /**
     * Поиск -  19.05.2022
     */
    public By Period19 = By.xpath("//td[contains(.,'19.05.2022')]");
    /**
     * Поиск -  05.12.2022
     */
    public By Period05 = By.xpath("//td[contains(.,'05.12.2022')]");

    /**
     * Поиск - Отображаемая таблица
     */
    public By Table = By.xpath("//div[@class='stats-wrap']//tbody");
}
