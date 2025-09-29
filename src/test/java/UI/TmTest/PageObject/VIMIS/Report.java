package UI.TmTest.PageObject.VIMIS;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class Report extends BaseTest {
    /**
     * Вимис - Отчёты
     */
    public Report(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Метод для выбора направления и поиска
     * direction - Локатор направления
     */
    public void ChoosingDirection(By direction) throws InterruptedException {
        Report vimisReport;
        vimisReport = new Report(driver);
        wait.until(visibilityOfElementLocated(vimisReport.IntelligenceWait));
        SelectClickMethod(vimisReport.Direction, direction);
        /** Поиск */
        System.out.println("Поиск");
        vimisReport.Search.click();
        Thread.sleep(1000);

    }

    /**
     * Вимис - Отчёты
     */
    @FindBy(xpath = "//a[@href='/vimis/sms-statistics']")
    public WebElement Report;
    public By ReportWait = By.xpath("//a[@href='/vimis/sms-statistics']");

    /**
     * Сведения о количестве переданных запросов в ВИМИС
     */
    public By IntelligenceWait = By.xpath(
            "//div[@id][contains(.,'Сведения о количестве переданных запросов в ВИМИС')]");

    /**
     * Направление
     */
    public By Direction = By.xpath("(//input[@placeholder='Выберите направление СМС'])[1]");

    /**
     * Направление - Любое направление
     */
    public By DirectionBy (String value) {
        By str = By.xpath("(//li/span[contains(.,'"+value+"')])[2]");
        return str;
    }

    /**
     * Направление - Онкология
     */
    public By Oncology = By.xpath("(//li/span[contains(.,'1 - Онкология')])[2]");

    /**
     * Направление - Профилактика
     */
    public By Prevention = By.xpath("(//li/span[contains(.,'2 - Профилактика')])[2]");

    /**
     * Направление - Акушерство и неонатология
     */
    public By Akineo = By.xpath("(//li/span[contains(.,'3 - Акушерство и неонатология')])[2]");

    /**
     * Направление - Сердечно-сосудистые заболевания
     */
    public By SSZ = By.xpath("(//li/span[contains(.,'4 - Сердечно-сосудистые заболевания')])[2]");

    /**
     * Направление - Инфекционные болезни
     */
    public By Infection = By.xpath("(//li/span[contains(.,'5 - Инфекционные болезни')])[2]");

    /**
     * Направление - Иные профили
     */
    public By Other = By.xpath("(//li/span[contains(.,'99 - Иные профили')])[2]");

    /**
     * Период "С какого числа"
     */
    @FindBy(xpath = "(//input[@placeholder='C какого числа '])[1]")
    public WebElement Period;
    public By PeriodWait = By.xpath("(//input[@placeholder='C какого числа '])[1]");

    /**
     * Выбор дат
     */
    public By DateWait = By.xpath("//div[@class='el-picker-panel__content el-date-range-picker__content is-left']");

    /**
     * Выбор текущей даты
     */
    @FindBy(xpath = "//td[@class='available today in-range end-date']")
    public WebElement DateOne;
    public By DateOneWait = By.xpath("//td[@class='available today in-range end-date']");

    /**
     * Выбор этого же дня
     */
    @FindBy(xpath = "//td[@class='available today in-range start-date end-date']")
    public WebElement DateTwo;
    public By DateTwoWait = By.xpath("//td[@class='available today in-range start-date end-date']");

    /**
     * Мед организации
     */
    @FindBy(xpath = "//input[@class='el-select__input is-small']")
    public WebElement MO;
    public By MOWait = By.xpath("//input[@class='el-select__input is-small']");

    /**
     * Мед организации - МО
     */
    @FindBy(xpath = "//li/span[contains(.,'БУ ХМАО-Югры \"Белоярская районная больница\"')]")
    public WebElement Organization;
    public By OrganizationWait = By.xpath("//li/span[contains(.,'БУ ХМАО-Югры \"Белоярская районная больница\"')]");

    /**
     * Информационная система
     */
    @FindBy(xpath = "(//input[@placeholder='Выберите информационную систему'])[1]")
    public By IsWait = By.xpath("(//input[@placeholder='Выберите информационную систему'])[1]");
    public By IsWaitTwo = By.xpath("(//input[@placeholder='Выберите информационную систему'])[2]");
    public By SelectIs = By.xpath("//div[@x-placement]//ul/li/span");
    public By SelectMIS = By.xpath("//div[@x-placement]//ul/li/span[contains(.,'МИС \\\"Пациент\\\"')]");

    /**
     * Тип Документа
     */
    public By SMS = By.xpath("//input[@placeholder='Введите тип документа']");

    /**
     * Тип СМС - СМС
     */
    public By SelectSms = By.xpath("//div[@x-placement]//ul/li/span[contains(.,'SMSV3 - Лабораторное исследование')]");

    /**
     * Тип СМС - СМС
     */
    public By SelectSms75 = By.xpath(
            "//div[@x-placement]//ul/li/span[contains(.,'75 - Протокол лабораторного исследования (CDA) Редакция 4')]");
    public By Select (String value) {
        By Select = By.xpath(
                "//div[@x-placement]//ul/li/span[contains(.,'"+value+"')]");
        return Select;
    }

    /**
     * Тип СМС - СМС - 53
     */
    public By SelectSms53 = By.xpath(
            "//div[@x-placement]//ul/li/span[contains(.,'53 - Контрольная карта диспансерного наблюдения (PDF/A-1)')]");

    /**
     * Сброс
     */
    public By Delete = By.xpath("(//button[@class='el-button el-button--default el-button--small'])[1]");

    /**
     * Поиск
     */
    @FindBy(xpath = "(//button//span[contains(.,'Поиск')])[1]")
    public WebElement Search;
    public By SearchWait = By.xpath("(//button//span[contains(.,'Поиск')])[1]");

    /** Нет Данных */
    public By NoData (String value) {
        By data = By.xpath("(//td[contains(.,'Нет данных')])["+value+"]");
        return data;
    }

    /**
     * Excel
     */
    public By AddExcel = By.xpath("(//button[contains(.,'Excel')])[1]");

    /**
     * Excel - Недоступно
     */
    public By AddExcelDisabled = By.xpath("(//button[@disabled='disabled'][contains(.,'Excel')])[1]");

    /**
     * Поиск - Загрузка
     */
    public By Loading = By.xpath("(//div[@class='el-loading-spinner'])[2]");

    /**
     * Ожидание таблицы
     */
    @FindBy(xpath = "(//div[@class='el-card__body']//table[@border='1'])[2]//td[5]")
    public WebElement TableSearchWait;
    public By TableSearch = By.xpath("(//div[@class='el-card__body']//table[@border='1'])[2]//td[5]");

    /**
     * Первое значение Doctype в таблице
     */
    @FindBy(xpath = "//table[@id='table_diagnostics']//tr[1]/td[4]")
    public By OneDoctype = By.xpath("//table[@id='table_diagnostics']//tr[1]/td[4]");

    /**
     * Все остальные значения Doctype в таблице
     */
    @FindBy(xpath = "//table[@id='table_diagnostics']//tr[1]/td[4]")
    public By Doctype = By.xpath("//table[@id='table_diagnostics']//tr[1]/following-sibling::tr/td[1]");

    /**
     * Первое название ИС в таблице
     */
    public By IS = By.xpath("(//table//tbody/tr)[1]/td[3]");

    /**
     * Успешно загруженные в регион - все
     */
    @FindBy(xpath = "//table[@border='1']/tbody//td[@class='font-weight-500 color-blue-grey-500'][1]")
    public WebElement SuccessfullyUploaded;
    public By SuccessfullyUploadedWait = By.xpath(
            "//table[@border='1']/tbody//td[@class='font-weight-500 color-blue-grey-500'][1]");

    /**
     * В очереди КРЭМД - все
     */
    @FindBy(xpath = "//table[@border='1']/tbody//td[@class='font-weight-500 color-blue-grey-500'][2]")
    public WebElement Regional;

    /**
     * Успешно приняты РРЭМД - все
     */
    @FindBy(xpath = "//table[@border='1']/tbody//td[@class='font-weight-500 color-blue-grey-500'][3]")
    public WebElement SuccessfullyRegional;

    /**
     * Успешно приняты на федеральном уровне - по конкретной ИС
     */
    public By SuccessfullyFederalMO = By.xpath(
            "(//table[@border='1']/tbody)[1]//td[contains(.,'МИС \"Пациент\"')]/following-sibling::td[4]/a");

    /**
     * Не прошли ФЛК РРЭМД - по конкретной ИС
     */
    public By ErrorFlkKremdMO = By.xpath(
            "(//table[@border='1']/tbody)[1]//td[contains(.,'МИС \"Пациент\"')]/following-sibling::td[5]/a");

    /**
     * Не прошли ФЛК РРЭМД - все
     */
    @FindBy(xpath = "//table[@border='1']/tbody//td[@class='font-weight-500 color-blue-grey-500'][4]")
    public WebElement FLK;

    /**
     * Отправленные в ФРЭМД - все
     */
    @FindBy(xpath = "//table[@border='1']/tbody//td[@class='font-weight-500 color-blue-grey-500'][5]")
    public WebElement Queue;

    /**
     * Успешно принятые в ФРЭМД - все
     */
    @FindBy(xpath = "//table[@border='1']/tbody//td[@class='font-weight-500 color-blue-grey-500'][6]")
    public WebElement FederalTrue;

    /**
     * Не принятые в ФРЭМД - все
     */
    @FindBy(xpath = "//table[@border='1']/tbody//td[@class='font-weight-500 color-blue-grey-500'][7]")
    public WebElement FederalFalse;


    public By FederalFalseIS = By.xpath("(//table[@border='1']/tbody)[1]//td[contains(.,'МИС \"Пациент\"')]/following-sibling::td[8]/a");

    /**
     * Детализация по СМС - первая группировка по ошибке
     */
    public By Error (String value) {
        By GroupError = By.xpath("//tr/td[2]/div/span[contains(.,'"+value+"')]");
        return GroupError;
    }


    /**
     * Детализация по СМС - Успешно приняты на федеральном уровне - МО
     */
    @FindBy(xpath = "(//span[@role='link'])[1]")
    public WebElement DetalMO;
    public By DetalMOWait = By.xpath("(//span[@role='link'])[1]");

    /**
     * Детализация по СМС - следующая страница -  активна
     */
    public By ActiveRight = By.xpath("//button[@class='btn-next']/i[@class='el-icon el-icon-arrow-right']");

    /**
     * Детализация по СМС - следующая страница - не активна
     */
    public By DisableRight = By.xpath("//button[@disabled]/i[@class='el-icon el-icon-arrow-right']");

    /**
     * Детализация по СМС - Успешно приняты на федеральном уровне - Тело документа
     */
    public By DocumentWait = By.xpath("//code[@class='html hljs xml']");

    /**
     * Детализация по СМС - Успешно приняты на федеральном уровне - Тело документа - PDF
     */
    public By DocumentWaitPDF = By.xpath("//div[@class='v-modal']");

    /**
     * Детализация по СМС - Успешно приняты на федеральном уровне - Тело документа - Закрыть (Тм-дев)
     */
    public By Closes (String value) {
        By Close = By.xpath("(//button[@aria-label='Close'])["+value+"]");
        return Close;
    }

    /**
     * Детализация по СМС - Успешно приняты на федеральном уровне - Тело документа - Закрыть (ТМ-тест)
     */
    public By CloseTm = By.xpath("(//button[@aria-label='Close'])[1]");

    /**---------------------- Отчёт по статусам отправки СМС-------------------- */
    /**
     * Отчёт по статусам отправки СМС
     */
    @FindBy(xpath = "//div[@role='tablist']/div[contains(.,'Отчёт по статусам отправки СМС')]")
    public WebElement ReportInStatus;
    public By ReportInStatusWait = By.xpath("//div[@role='tablist']/div[contains(.,'Отчёт по статусам отправки СМС')]");

    /**
     * Отчёт по статусам отправки СМС - Направление
     */
    public By StatusDirection = By.xpath("(//input[@placeholder='Выберите направление СМС'])[2]");

    /**
     * Отчёт по статусам отправки СМС - Направление - Недоступный
     */
    public By StatusDirectionDisabled = By.xpath("//input[@disabled='disabled'][@placeholder='Выберите направление СМС']");

    /**
     * Отчёт по статусам отправки СМС - Направление - Любое направление
     */
    public By Directions (String value) {
        By str = By.xpath("(//li/span[contains(.,'"+value+"')])[2]");
        return str;
    }

    /**
     * Отчёт по статусам отправки СМС - Направление - Онкология
     */
    public By Onko = By.xpath("(//li/span[contains(.,'1 - Онкология')])[2]");

    /**
     * Отчёт по статусам отправки СМС - Направление - Профилактика
     */
    public By Prev = By.xpath("(//li/span[contains(.,'2 - Профилактика')])[2]");

    /**
     * Отчёт по статусам отправки СМС - Направление - Акушерство и неонатология
     */
    public By akineo = By.xpath("(//li/span[contains(.,'3 - Акушерство и неонатология')])[2]");

    /**
     * Отчёт по статусам отправки СМС - Направление - Сердечно-сосудистые заболевания
     */
    public By ssz = By.xpath("(//li/span[contains(.,'4 - Сердечно-сосудистые заболевания')])[2]");

    /**
     * Отчёт по статусам отправки СМС - Направление - Сердечно-сосудистые заболевания
     */
    public By infection = By.xpath("(//li/span[contains(.,'5 - Инфекционные болезни')])[2]");

    /**
     * Отчёт по статусам отправки СМС - Направление - Сердечно-сосудистые заболевания
     */
    public By other = By.xpath("(//li/span[contains(.,'99 - Иные профили')])[2]");

    /**
     * Отчёт по статусам отправки СМС - Период "С какого числа"
     */
    @FindBy(xpath = "(//input[@placeholder='C какого числа '])[2]")
    public WebElement StatusPeriod;
    public By StatusPeriodWait = By.xpath("(//input[@placeholder='C какого числа '])[2]");

    /**
     * Отчёт по статусам отправки СМС - Период - С какого числа
     */
    public By PeriodToday = By.xpath("//td[@class='available today']");

    /**
     * Отчёт по статусам отправки СМС - Период - По какое число
     */
    public By PeriodStartEnd = By.xpath("//td[@class='available today in-range start-date end-date']");
    public By PeriodStart = By.xpath("//td[@class='available today in-range start-date']");
    public By PeriodEnd = By.xpath("//td[@class='available today in-range end-date']");

    /**
     * Отчёт по статусам отправки СМС - Статус
     */
    public By Status = By.xpath("//input[@placeholder='Выберите статус отправки']");

    /**
     * Отчёт по статусам отправки СМС - Статус - Успешно принятые в ВИМИС (1)
     */
    public By SelectStatusSuccess = By.xpath("//li/span[contains(.,'Успешно принятые в ВИМИС')]");

    /**
     * Отчёт по статусам отправки СМС - Статус - Принято на региональном уровне
     */
    public By SelectStatusTrue = By.xpath("//li/span[contains(.,'Принято на региональном уровне')]");

    /**
     * Отчёт по статусам отправки СМС - Статус - Не прошли проверку ФЛК ВИМИС
     */
    public By SelectStatusFalse = By.xpath("//li/span[contains(.,'Не прошли проверку ФЛК ВИМИС')]");

    /**
     * Отчёт по статусам отправки СМС - Поиск
     */
    @FindBy(xpath = "(//button//span[contains(.,'Поиск')])[2]")
    public WebElement StatusSearch;
    public By StatusSearchWait = By.xpath("(//button//span[contains(.,'Поиск')])[2]");

    /**
     * Отчёт по статусам отправки СМС - Excel
     */
    public By AddExcel2 = By.xpath("(//button[contains(.,'Excel')])[2]");

    /**
     * Отчёт по статусам отправки СМС - Excel - Недоступный
     */
    public By AddExcel2Disabled = By.xpath("(//button[@disabled='disabled'][contains(.,'Excel')])[2]");

    /**
     * Отчёт по статусам отправки СМС - Загрузка данных
     */
    public By LoadingSMS = By.xpath("//div[@class='el-card is-always-shadow el-loading-parent--relative']//div[@class='el-loading-spinner']");

    /**
     * Отчёт по статусам отправки СМС - Нет данных
     */
    public By NotData = By.xpath("//td[contains(.,'Нет данных')]");

    /**
     * Отчёт по статусам отправки СМС - Колонка Структурное подразделение
     */
    @FindBy(xpath = "//table/thead/tr/th[@class='mo-name-column sticky'][2]")
    public WebElement ColumnStructure;

    /**
     * Отчёт по статусам отправки СМС - Строка Итого - Все СМС
     */
    public By Sum = By.xpath("//table[@class='main-table']//tbody/tr[last()]/td");

    /**
     * Отчёт по статусам отправки СМС - Количество строк
     */
    public By SizeLine = By.xpath("(//tbody)[1]/tr");

    /**
     * Отчёт по статусам отправки СМС - Столбец CMCV27
     */
    @FindBy(xpath = "(//tbody)[1]/tr[last()]/td[20]")
    public WebElement SMS27;
    public By SMS27Wait = By.xpath("(//tbody)[1]/tr[last()]/td[20]");
}
