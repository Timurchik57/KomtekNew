package UI.TmTest.PageObject.VIMIS;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


import java.text.SimpleDateFormat;
import java.util.Date;

public class SMS extends BaseTest {
    /**
     * Вимис - Структурированные медицинские сведения
     */
    public SMS(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Вимис - Структурированные медицинские сведения
     */
    @FindBy(xpath = "//a[@href='/vimis/medical-documents']")
    public WebElement SMS;
    public By SMSWait = By.xpath("//a[@href='/vimis/medical-documents']");

    /**
     * Назад к выбору направления
     */
    public By Back = By.xpath("//i[@class='el-icon-arrow-left']");

    /**
     * Выбирете направление
     */
    public By DistrictWait = By.xpath("//h2[contains(.,'Выберите направление оказания медицинской помощи')]");

    /**
     * Направление - Любое направление
     */
    public By Direction (String value) {
        By str = By.xpath("//div[@class='sms-type-button'][contains(.,'"+value+"')]");
        return str;
    }

    /**
     * Направление - Онкологния
     */
    @FindBy(xpath = "//div[@class='sms-type-button'][contains(.,'Онкология')]")
    public WebElement Oncology;
    public By OncologyWait = By.xpath("//div[@class='sms-type-button'][contains(.,'Онкология')]");

    /**
     * Направление - Профилактика
     */
    @FindBy(xpath = "//div[@class='sms-type-button'][contains(.,'Профилактика')]")
    public WebElement Prevention;
    public By PreventionWait = By.xpath("//div[@class='sms-type-button'][contains(.,'Профилактика')]");

    /**
     * Направление - Акушерство и неонатология
     */
    @FindBy(xpath = "//div[@class='sms-type-button'][contains(.,'Акушерство и неонатология')]")
    public WebElement Akineo;
    public By AkineoWait = By.xpath("//div[@class='sms-type-button'][contains(.,'Акушерство и неонатология')]");

    /**
     * Направление - Сердечно-сосудистые заболевания
     */
    @FindBy(xpath = "//div[@class='sms-type-button'][contains(.,'Сердечно-сосудистые заболевания')]")
    public WebElement SSZ;
    public By SSZWait = By.xpath("//div[@class='sms-type-button'][contains(.,'Сердечно-сосудистые заболевания')]");

    /**
     * Направление - Инфекция
     */
    @FindBy(xpath = "//div[@class='sms-type-button'][contains(.,'Инфекционные болезни')]")
    public WebElement Infection;
    public By InfectionWait = By.xpath("//div[@class='sms-type-button'][contains(.,'Инфекционные болезни')]");

    /**
     * Направление - Онкологния
     */
    @FindBy(xpath = "//div[@class='sms-type-button'][contains(.,'Иные профили')]")
    public WebElement Other;
    public By OtherWait = By.xpath("//div[@class='sms-type-button'][contains(.,'Иные профили')]");

    /**
     * Фильтр
     */
    @FindBy(xpath = "//span[@class='el-popover__reference-wrapper']")
    public WebElement Filter;
    public By FilterWaitAdd = By.xpath("//span[@class='el-popover__reference-wrapper']");
    public By FilterWait = By.xpath("//div[@role='tooltip']");

    /**
     * Мед. организация
     */
    @FindBy(xpath = "//input[@placeholder='Введите название мед. организации']")
    public WebElement MO;

    /**
     * Мед. организация - Организация
     */
    @FindBy(xpath = "//li/span[contains(.,'БУ ХМАО-Югры \"Белоярская районная больница\"')]")
    public WebElement Organization;
    public By OrganizationWait = By.xpath("//li/span[contains(.,'БУ ХМАО-Югры \"Белоярская районная больница\"')]");

    /**
     * Тип СМС
     */
    @FindBy(xpath = "//input[@placeholder='Введите тип СМС']")
    public WebElement TypeSms;

    /**
     * Тип СМС - СМС
     */
    @FindBy(xpath = "//li/span[contains(.,'SMSV5 - Осмотр (консультация)')]")
    public WebElement SelectSms;
    public By SelectSmsWait = By.xpath("//li/span[contains(.,'SMSV5 - Осмотр (консультация)')]");

    /**
     * Тип СМС - СМС 110
     */
    public By SelectSms110ait = By.xpath(
            "//li/span[contains(.,'110 - Протокол инструментального исследования (CDA) Редакция 3')]");

    /**
     * Начало периода
     */
    @FindBy(xpath = "//input[@placeholder='Начало периода']")
    public WebElement BeforeTime;
    public By BeforeTimeWait = By.xpath("//input[@placeholder='Начало периода']");

    /** Конец периода */
    public By AfterTimeWait = By.xpath("//input[@placeholder='Конец периода']");

    /** Начало периода - 1 число текщего месяца */
    public By DataStart = By.xpath("(//div[@x-placement][2]//td[@class='available'][contains(.,'1')])[1]");

    /** Начало периода - текущее число текщего месяца */
    public By DataToDay = By.xpath("(//div[@x-placement][2]//td[@class='available today'])[1]");

    /**
     * Конец периода
     */
    @FindBy(xpath = "//input[@placeholder='Конец периода']")
    public WebElement AfterTime;

    /**
     * Фильтр - Идентификаторы
     */
    public By Ident = By.xpath("//div[@role='button'][contains(.,'Идентификаторы')]");

    /**
     * Фильтр - Принятые
     */
    public By Accepted = By.xpath("//label/span[contains(.,'Принятые')]/preceding::span[1]");

    /**
     * Фильтр - Идентификаторы - localUid
     */
    public By localUid = By.xpath("//div[contains(.,'localUid')]/following-sibling::div/input");

    /**
     * Фильтр - Идентификаторы - PatientGuid
     */
    public By PatientGuid = By.xpath("//div[contains(.,'patientGuid')]/following-sibling::div/input");

    /**
     * Поиск
     */
    @FindBy(xpath = "//span[contains(.,'Поиск')]")
    public WebElement Search;
    public By SearchWait = By.xpath("//span[contains(.,'Поиск')]");

    /**
     * Фильтр - Сбросить
     */
    public By Reset = By.xpath("//button[contains(.,' Сбросить ')]");

    /**
     * Excel
     */
    public By AddExcel = By.xpath("//button[contains(.,'Excel')]");

    /**
     * Excel - Загрузка
     */
    public By ExcelLoading = By.xpath("(//div[@class='el-loading-spinner'])[3]");

    /**
     * Отправить принудительно
     */
    public By Send = By.xpath("//button//span[contains(.,'Отправить принудительно')]");

    /**
     * Отправить принудительно - Загрузка
     */
    public By LoadingSend(String value) {
        By LoadingSend = By.xpath("(//div[@class='el-loading-spinner'])[" + value + "]");
        return LoadingSend;
    }

    /**
     * Отправить принудительно
     */
    public By Success = By.xpath("//h2[contains(.,'Успешно')]");

    /**
     * Результат поиска
     */
    public By ResultSearch = By.xpath(
            "//div[@class='el-table__body-wrapper is-scrolling-none']/table//tr//td[7]//div[@class='popover-info']");

    /**
     * Результат поиска - Загрузка
     */
    public By Loading = By.xpath("(//div[@class='el-loading-spinner'])[2]");

    /** Результат поиска - Дата создания - сортировка по возрастанию */
    public By SortAp = By.xpath(
            "//div[contains(.,'Дата создания')]/span[@class='caret-wrapper']/i[@class='sort-caret ascending']");

    /**
     * Результат поиска - Дата создания - сортировка по убыванию
     */
    public By SortDesk = By.xpath(
            "//div[contains(.,'Дата создания')]/span[@class='caret-wrapper']/i[@class='sort-caret descending']");

    /** Результат поиска - Первая строка - Дата создания */
    public By FirstSearchData = By.xpath("//table[@class='el-table__body']//tr[1]/td[3]//span/div");

    /**
     * Результат поиска - Статус отправки
     */
    public By ResultSearchStatus = By.xpath("//tr/td[2]//span/div");

    /**
     * Результат поиска - LocalUid
     */
    public By ResultSearchLocalUid = By.xpath("//tr/td[7]//span/div");

    /**
     * Результат поиска - первая запись - Три точки
     */
    public By FirstLine = By.xpath("//tr[1]/td[last()]//button");

    /**
     * Результат поиска - Три точки - Лк Врача
     */
    public By FirstLineLK = By.xpath("//ul[@x-placement='bottom-end']/li[contains(.,'ЛК врача')]");

    /**
     * Результат поиска - Три точки - Ссылка на Лк Врача
     */
    public By FirstLineLKLink = By.xpath("//ul[@x-placement='bottom-end']/li/a[contains(.,'ЛК врача')]");

    /**
     * Результат поиска - Нет данных
     */
    public By NotResultSearch = By.xpath("//div/span[contains(.,'Нет данных')]");

    /**
     * Конец периода
     */
    public By Page = By.xpath("//div[@class='grid-table__pagination']//input[@readonly='readonly']");

    public By SelectPage = By.xpath("//ul/li/span[contains(.,'30 на странице')]");
    /**
     * Количество страниц
     */
    public By QuantityPage = By.xpath("//ul[@class='el-pager']/li");

    /**
     * Следующая страница после активной
     */
    public By NextPage = By.xpath("//ul[@class='el-pager']/li[@class='number active']/following-sibling::li[1]");

    /**
     * Метод для выбора направления, заполниния фильтра, нажатие поиска
     * TypeSms - Локатор направления (WebElement)
     * TypeSmsWait - Локатор направления
     * District - Наименование направления
     * EditMO - условие нужно ли выбирать Мед. организацию
     */
    public void ChoosingDirection(
            WebElement TypeSms, By TypeSmsWait, String District, Boolean EditMO
    ) throws InterruptedException {
        SMS sms = new SMS(driver);
        Date date1 = new Date();
        SimpleDateFormat formatForDateNow1 = new SimpleDateFormat("dd-MM-yyyy");
        String Date1 = formatForDateNow1.format(date1);
        WaitElement(sms.SMSWait);
        actionElementAndClick(sms.SMS);

        System.out.println("Выбор Направления - " + District + "");
        WaitElement(sms.DistrictWait);
        WaitElement(TypeSmsWait);
        TypeSms.click();
        Thread.sleep(1000);

        System.out.println("Выбор Фильтров");
        sms.Filter.click();
        WaitElement(sms.FilterWait);

        System.out.println("Выбор Мед. организации");
        if (EditMO) {
            sms.MO.sendKeys("БУ ХМАО-Югры \"Белоярская районная больница\"");
            WaitElement(By.xpath("//div[@x-placement]"));
            WaitElement(sms.OrganizationWait);
            actionElementAndClick(sms.Organization);
        }

        System.out.println("Выбор Типа смс");
        sms.TypeSms.click();
        if (District != "Иные профили") {
            ClickElement(sms.SelectSmsWait);
        } else {
            ClickElement(sms.SelectSms110ait);
        }

        System.out.println("Ввод периода");
        sms.BeforeTime.sendKeys(Date1 + "0000");
        sms.AfterTime.sendKeys(Date1 + "2359");

        System.out.println("Поиск");
        sms.Search.click();
        Thread.sleep(1000);
    }
}
