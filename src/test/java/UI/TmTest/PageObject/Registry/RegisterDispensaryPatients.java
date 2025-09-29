package UI.TmTest.PageObject.Registry;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class RegisterDispensaryPatients extends BaseTest {
    public RegisterDispensaryPatients(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Регистр диспансерных больных
     */
    @FindBy(xpath = "//a[@href='/registry/dispensary']")
    public WebElement RegistrDB;
    public By RegistrDBWait = By.xpath("//a[@href='/registry/dispensary']");

    /**
     * Регистр диспансерных больных - ССЗ
     */
    @FindBy(xpath = "//a[@href='/registry/semd-dispensary?registerTypeId=125&dataSource=4']")
    public WebElement RegistrDBSSZ;
    public By RegistrDBSSZWait = By.xpath("//a[@href='/registry/semd-dispensary?registerTypeId=125&dataSource=4']");
    public By RegistrDBZNO = By.xpath("//a[@href='/registry/semd-dispensary?registerTypeId=147&dataSource=1']");
    public By RegistrDBOnkoXmao = By.xpath("//a[@href='/registry/semd-dispensary?registerTypeId=13&dataSource=4']");
    public By RegistrSSZ = By.xpath("//span[contains(.,'Регистр ССЗ')]/preceding-sibling::i/parent::a");
    public By RegistrInfection = By.xpath("//span[contains(.,'Регистр Инфекции')]/preceding-sibling::i/parent::a");
    public By RegistrOther = By.xpath("//span[contains(.,'Регистр Иные Профили')]/preceding-sibling::i/parent::a");

    /**
     * Регистр диспансерных больных - Селект регистра
     */
    public By RegistrSelect(String value) {
        By RegistrSelect = By.xpath(
                "//div[@role='tab'][contains(.,'"+value+"')]");
        return RegistrSelect;
    }

    /**
     * Регистр диспансерных больных - Селект регистра - выбор Регистра
     */
    public By RegistrSelect2(String value) {
        By RegistrSelect = By.xpath(
                "//span[contains(.,'"+value+"')]/preceding-sibling::i/parent::a");
        return RegistrSelect;
    }

    /**
     * Регистр диспансерных больных - Регистр онкология
     */
    public By RegistrOnko = By.xpath("//a[@href='/registry/semd-dispensary?registerTypeId=13&dataSource=4']");

    /**
     * Регистр диспансерных больных - Регистр Профилактики
     */
    public By RegistrPrev = By.xpath("//span[contains(.,'Регистр Профилактики')]/preceding-sibling::i/parent::a");

    /**
     * Регистр диспансерных больных - Акинео
     */
    public By RegistrAkineo = By.xpath("//span[contains(.,'Регистр Акинео')]/preceding-sibling::i/parent::a");

    /**
     * Регистр диспансерных больных - Онко
     */
    @FindBy(xpath = "//a[@href='/registry/dispensary?registerTypeId=124']")
    public WebElement RegistrDBOnko;
    public By RegistrDBOnkoWait = By.xpath("//a[@href='/registry/dispensary?registerTypeId=124']");
    public By RegistrOnkoWait = By.xpath("//span[contains(.,'Регистр Онко')]/preceding-sibling::i/parent::a");

    /**
     * Регистр диспансерных больных - Загрузка
     */
    public By Loading = By.xpath("(//div[@class='el-loading-spinner'])[2]");

    /**
     * Регистр диспансерных больных - Следующая страница
     */
    public By Next = By.xpath("//div[@id='pane-quotas']//button[@class='btn-next']");

    /**
     * Регистр диспансерных больных - Следующая страница - Н активная
     */
    public By NextDisable = By.xpath("//div[@class='el-pagination is-background']//button[@class='btn-next'][@disabled='disabled']");

    /**
     * Регистр диспансерных больных - Onko - Заголовок
     */
    public By HeaderOnko = By.xpath("//h1[.='Регистр диспансерных больных / Онко']");

    /**
     * Регистр диспансерных больных - ССЗ - Заголовок
     */
    public By HeaderSSZ = By.xpath("//h1[.='Регистр диспансерных больных / ССЗ']");

    /**
     * Регистр диспансерных больных - Заголовок
     */
    public By Header = By.xpath("//h1[.='Регистр диспансерных больных']");

    /**
     * Регистр диспансерных больных - ССЗ - Колонка с диагнозами
     */
    public By RegistrDBDiagnosisSSZ = By.xpath("//tbody/tr/td[4]//span");

    /**
     * Регистр диспансерных больных - ФИО Пациента
     */
    public By FIO = By.xpath("//th/div[contains(.,'ФИО пациента')]");

    /**
     * Регистр диспансерных больных - Первый пациент - ФИО
     */
    public By FIOFirst = By.xpath("//table[@class='el-table__body']//tr[1]/td[1]//span");

    /**
     * Регистр диспансерных больных - Первый пациент - Врач обработавший КАС
     */
    public By FIOUpdateKASFirst = By.xpath("//table[@class='el-table__body']//tr[1]/td[9]//span");

    /**
     * Регистр диспансерных больных - Первый пациент - Врач создавший извещения КАС ФИО
     */
    public By FIOKASFirst = By.xpath("//table[@class='el-table__body']//tr[1]/td[11]//span");

    /**
     * Регистр диспансерных больных - Название колонки
     */
    public By RegisterColumn(String value) {
        By str = By.xpath(
                "//th[contains(.,'"+value+"')]");
        return str;
    }

    /**
     * Регистр диспансерных больных - Фильтры
     */
    @FindBy(xpath = "//span/span[contains(.,'Фильтры')]")
    public WebElement Filters;
    public By FiltersWait = By.xpath("//span/span[contains(.,'Фильтры')]");

    /**
     * Регистр диспансерных больных - Фильтр - Любой селектор
     */
    public By FiltersSelect(String value) {
        By str = By.xpath(
                "//div[@class='el-form-item__content']/div[contains(.,'"+value+"')]/following-sibling::div");
        return str;
    }

    /**
     * Регистр диспансерных больных - Фильтры - Окно Фильтров
     */
    public By WindowFilters = By.xpath("//div[@x-placement='left-end']");

    /** Регистр диспансерных больных - Фильтры - Любой фильтр */
    public By FilterAll (String value) {
        By All = By.xpath("//div[@class='form-item-label'][contains(.,'"+value+"')]/following-sibling::div//input");
        return All;
    }

    /**
     * Регистр диспансерных больных - Фильтры - Дополнительные фильтры
     */
    @FindBy(xpath = "//div[@role='button'][contains(.,'Дополнительные фильтры')]")
    public WebElement AdditionalFilters;
    public By AdditionalFiltersWait = By.xpath("//div[@role='button'][contains(.,'Дополнительные фильтры')]");

    /**
     * Регистр диспансерных больных - Фильтры - Укажите код диагноза
     */
    @FindBy(xpath = "//input[@placeholder='Укажите код диагноза']")
    public WebElement CodDiagnosis;
    public By CodDiagnosisWait = By.xpath("//input[@placeholder='Укажите код диагноза']");

    /**
     * Регистр диспансерных больных - Фильтры - Окно Кодов
     */
    public By WindowCode = By.xpath("(//div[@role='tooltip'])[2]");

    /**
     * Регистр диспансерных больных - Фильтры - Введите код регистра
     */
    @FindBy(xpath = "//input[@placeholder='Введите код регистра']")
    public WebElement InputCod;

    /**
     * Регистр диспансерных больных - Фильтры - Введите код регистра - Код С00
     */
    @FindBy(xpath = "//li[1]//div[contains(.,'C00')]/following-sibling::div/button[2]")
    public WebElement CodC00;
    public By CodC00wait = By.xpath("//li[1]//div[contains(.,'C00')]");

    /**
     * Регистр диспансерных больных - Фильтры - Введите код регистра - Код C96
     */
    @FindBy(xpath = "//li[2]//div[contains(.,'C96')]/following-sibling::div/button")
    public WebElement CodC96;
    public By CodC96wait = By.xpath("//li[2]//div[contains(.,'C96')]");

    /**
     * Регистр диспансерных больных - Фильтры - Введите код регистра - Код D00
     */
    @FindBy(xpath = "//li[1]//div[contains(.,'D00')]/following-sibling::div/button[2]")
    public WebElement CodD00;
    public By CodD00wait = By.xpath("//li[1]//div[contains(.,'D00')]");

    /**
     * Регистр диспансерных больных - Фильтры - Введите код регистра - D09.0
     */
    @FindBy(xpath = "//li[1]//div[contains(.,'D09.0')]/following-sibling::div/button")
    public WebElement CodD09;
    public By CodD09wait = By.xpath("//li[1]//div[contains(.,'D09.0')]");

    /**
     * Регистр диспансерных больных - Фильтры - Дополнительные фильтры - КАС
     */
    public By KAS = By.xpath("//label/span[contains(.,'КАС')]/preceding-sibling::span");

    /**
     * Регистр диспансерных больных - Фильтры - Дополнительные фильтры - ВРТ
     */
    public By BRT = By.xpath("//label/span[contains(.,'ВРТ')]/preceding-sibling::span");

    /**
     * Регистр диспансерных больных - Фильтры - Дополнительные фильтры - Врач создавший извещения КАС ФИО
     */
    public By DoctorAddKas = By.xpath(
            "//div[contains(.,'Врач создавший извещения КАС ФИО')]/following-sibling::div/input");

    /**
     * Регистр диспансерных больных - Фильтры - Дополнительные фильтры - Врач обработавший КАС
     */
    public By DoctorUpdateAddKas = By.xpath("//div[contains(.,'Врач обработавший КАС')]/following-sibling::div/input");

    /**
     * Регистр диспансерных больных - Фильтры - Сбросить
     */
    @FindBy(xpath = "(//div[@role='tooltip'])[2]//span[contains(.,'Сбросить')]")
    public WebElement Reset;
    public By ResetWait = By.xpath("//button[@type='button']/span[contains(.,'Сбросить')]");

    /**
     * Регистр диспансерных больных - Фильтры - Поиск
     */
    @FindBy(xpath = "//button[@type='submit']")
    public WebElement Search;
    public By SearchWait = By.xpath("//button[@type='submit']");

    /**
     * Регистр диспансерных больных - Список пациентов
     */
    public By Patients = By.xpath("//tbody/tr");

    /**
     * Регистр диспансерных больных - Количество записей
     */
    public By CountList = By.xpath("//i[@class='fal fa-file-alt']/ancestor::span");

    /**
     * Регистр диспансерных больных - Раскрытие нужной строки
     */
    public By SelectPatient (String value, boolean active) {
        By str = null;
        if (!active) {
            // Не нажата
            str = By.xpath(
                    "//table[@class='el-table__body']//tr[" + value + "]//div[@class='el-table__expand-icon']");
        } else {
            // Нажата
            str = By.xpath(
                    "//table[@class='el-table__body']//tr[" + value + "]//div[@class='el-table__expand-icon el-table__expand-icon--expanded']");
        }
        return str;
    }

    /**
     * Регистр диспансерных больных - Раскрытие нужной строки - Врач создавший извещения КАС ФИО
     */
    public By DoctorKas = By.xpath("//div[@class='el-table el-table--fit el-table--enable-row-hover el-table--enable-row-transition el-table--medium']//div[@class='el-table__body-wrapper is-scrolling-none']//td[4]//span");

    /**
     * Регистр диспансерных больных - Раскрытие нужной строки - ФИО Врач обработавший КАС
     */
    public By DoctorKasObr = By.xpath("(//div[@class='el-table el-table--fit el-table--enable-row-hover el-table--enable-row-transition el-table--medium']//div[@class='el-table__body-wrapper is-scrolling-none']//td[2]//span)[1]");

    /**
     * Регистр диспансерных больных - Количество пациентов
     */
    @FindBy(xpath = "(//div[@class='el-form-item__content']/span)[1]")
    public WebElement QuantityPatients;
    public By QuantityPatientsWait = By.xpath("//span[contains(.,'Пациентов:')]");

    /**
     * Регистр диспансерных больных - Первый пацинт - Лк Врача - тм тест
     */
    public By FirstPatientLK = By.xpath("//tr[1]/td[last()]//button");

    /**
     * Регистр диспансерных больных - Первый пацинт - Лк Врача - тм дев
     */
    public By FirstPatientLKDev = By.xpath("//tr[1]/td[last()]//button[2]");

    /**
     * Регистр диспансерных больных - Онко - Перейти на страницу 1 пациента
     */
    @FindBy(xpath = "//tbody/tr[1]/td[last()]//button[@tooltiptext='Перейти на страницу пациента']")
    public WebElement Cart;
    public By CartWait = By.xpath("//tbody/tr[1]/td[last()]//button[@tooltiptext='Перейти на страницу пациента']");

    /**
     * Страница 1 пациента - Мониторинг посещений
     */
    @FindBy(xpath = "//div[@class='el-tabs__item is-top'][contains(.,'Мониторинг посещений')]")
    public WebElement Monitoring;
    public By MonitoringWait = By.xpath("//div[@class='el-tabs__item is-top'][contains(.,'Мониторинг посещений')]");

    /**
     * Мониторинг посещений - Тип Периода
     */
    public By Type = By.xpath("//div[@class='form-item-label'][contains(.,'Тип периода')]");

    /**
     * Мониторинг посещений - Тип Периода
     */
    public By DistanceType = By.xpath("(//div[@class='el-col el-col-8'])[1]");

    /**
     * Мониторинг посещений - Тип Периода
     */
    public By Period = By.xpath("//div[@class='form-item-label'][contains(.,'Период')]");

    /**
     * Мониторинг посещений - Период
     */
    public By DistancePeriod = By.xpath("(//div[@class='el-col el-col-8'])[2]");

    /**
     * Мониторинг посещений - Поиск
     */
    @FindBy(xpath = "//button[contains(.,'Поиск')]")
    public WebElement SearchMonitoring;

    /**
     * Регистр диспансерных больных - Скачать
     */
    public By Download = By.xpath("//button/span[contains(.,'Скачать')]");

    /**
     * Регистр диспансерных больных - Скачать - Недоступно
     */
    public By DownloadDisabled = By.xpath("//button[@disabled='disabled'][contains(.,'Скачать')]");

    /**
     * Регистр диспансерных больных - Скачать - Ошибка
     */
    public By DownloadError = By.xpath("(//div[contains(.,'Ошибка')])[1]");
}
