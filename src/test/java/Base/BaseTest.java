package Base;

import UI.TmTest.PageObject.Administration.MedOrganization;
import UI.TmTest.PageObject.Administration.Users;
import UI.TmTest.PageObject.AuthorizationObject;
import api.Before.Authorization;
import com.jcraft.jsch.JSch;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.apache.commons.io.output.TeeOutputStream;
import org.apache.hc.core5.util.TextUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.opentest4j.AssertionFailedError;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.jcraft.jsch.*;
import org.testng.asserts.SoftAssert;

import static Base.BaseAPI.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

abstract public class BaseTest {
    public static RemoteWebDriver driver;
    public static WebDriverWait wait;
    public static WebDriverWait waitThree;
    public static WebDriverWait waitOne;
    public static Actions actions;
    public static ChromeOptions chromeOptions;
    public static Date date;
    public static String Date;
    public SQL sql;
    public static Cookie Session;
    public static Cookie TelemedC1;
    public static Cookie TelemedC2;
    public static Cookie Telemed;
    public static String LOGIN_PAGE_URL;
    public static String LoginBase;
    public static String PasswordBase;
    static SecureRandom RND = new SecureRandom();
    public static final String GENERATE_SNILS = "https://ortex.github.io/snils-generator/";
    public static boolean MTBZ;
    public Boolean Crypto = false;
    public TestInfo testInfo;
    public SoftAssert softAssert;
    public static boolean NewContour;

    // Для SSH
    public Session session;

    // Нужно для записи лога из терминала в файл
    public static ByteArrayOutputStream buffer;

    // Нужно для перезапуска упавшего теста
    public static boolean RebaseTest = false;
    public String StrRebase;

    public static Integer SmdToMinio;
    public static String DocumentDto;
    public static Integer KingNumber;
    public static String remote_url_chrome;
    public static boolean OpenWindow;
    public static boolean Yandex;
    public static String RemdStatus;

    public static void setUp () throws MalformedURLException {
        remote_url_chrome = System.getProperty("UrlChrome");
        RemdStatus = System.getProperty("StatusRemd");
        chromeOptions = new ChromeOptions();

        if (TextUtils.isEmpty(RemdStatus)) {
            RemdStatus = "false";
        } else {
            RemdStatus = "true";
        }

        if (TextUtils.isEmpty(remote_url_chrome)) {
            KingNumber = 2;
            OpenWindow = false;
            Yandex = false;

            if (!OpenWindow) {
                chromeOptions.addArguments("--headless");
                chromeOptions.addArguments("window-size=1920,1080");
            }

            if (Yandex) {
                chromeOptions.setBinary("C:/Users/Timur/AppData/Local/Yandex/YandexBrowser/Application/browser.exe");
                ChromeDriverService service = new ChromeDriverService.Builder()
                        .usingDriverExecutable(new File("C:/Programs/Repositiry/GoogleChromePortable/yandexdriver.exe"))
                        .withSilent(true)
                        .build();
                driver = new ChromeDriver(service, chromeOptions);
            } else {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(chromeOptions);
            }

            if (OpenWindow) {
                driver.manage().window().maximize();
            }
        } else {
            KingNumber = Integer.valueOf(System.getProperty("Contour"));

            WebDriverManager.chromedriver().setup();
            chromeOptions.addArguments("--headless");
            chromeOptions.addArguments("window-size=1920,1080");
            driver = new RemoteWebDriver(new URL(remote_url_chrome), chromeOptions);
            driver.setFileDetector(new LocalFileDetector());
        }

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        actions = new Actions(driver);
        date = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");
        Date = formatForDateNow.format(date);
    }

    @BeforeEach
    public void init () throws Exception {
        setUp();

        System.out.println("Контур " + KingNumber);
        sql = new SQL();
        softAssert = new SoftAssert();

        InputClass();
        InputPropFile("IfCountListner", "web");

        //Переменная для перезапуска тестов (сразу же после теста, если тест упал)
        if (RebaseTest) {
            StrRebase = ReadPropFile(ReadPropFile("methodName"));
            if (TextUtils.isEmpty(StrRebase)) {
                //В my.properties нет переменной с названием метода, значит создаём и присваем значение 0
                InputPropFile(ReadPropFile("methodName"), "0");
            }
        }

        sql.ReplacementConnection();
        MINIOMethod();
        terminal();
        ParametersAdd();
    }

    @Attachment
    @Step("Создание файла с логом из консоли для Allure")
    public static byte[] LogConsole (String name) throws IOException {
        return Files.readAllBytes(Paths.get("src/test/resources", name));
    }

    public static String PName;
    public static String PNameGlobal;
    public static String PLastNameGlobal;
    public static String PMiddleNameGlobal;
    public static String PSnils;
    public static String PSnilsHak;
    public static String PSnils_;
    public static String PGuid;

    public static String PNameRezerv_Id;
    public static String PNameGlobal_FIO;
    public static String PNameGlobal_Rezerv;
    public static String PLastNameGlobal_Rezerv;
    public static String PMiddleNameGlobal_Rezerv;
    public static String PSnils_Rezerv;

    public static String PId;
    public static String PRole;

    public static String PMORequest;
    public static String PIdMoRequest;
    public static String POidMoRequest;
    public static String PIdmuMoRequest;

    public static String PMOTarget;
    public static String PIdMoTarget;
    public static String POidMoTarget;

    public static String PMOTarge2;
    public static String PIdMoTarget2;
    public static String POidMoTarget2;

    public static String PNameEquipment;
    public static String PidEquipment;
    public static String PNameEquipment2;
    public static String PidEquipment2;

    @Step("Метод установки нужных параметров")
    public static void ParametersAdd () {
        PName = "Тестировщик Тест Тестович";
        PNameGlobal = "Тест";
        PLastNameGlobal = "Тестировщик";
        PMiddleNameGlobal = "Тестович";
        PSnils = "159-790-257 20";
        PSnils_ = "15979025720";
        PId = "5885";
        PRole = "Тестировщик";
        PGuid = "e3c3323e-1e05-4f59-b733-9abe7dfc88ce";

        PSnilsHak = "136-307-230 32";

        PNameGlobal_FIO = "Пользователь Для Тестов";
        PNameGlobal_Rezerv = "Для";
        PLastNameGlobal_Rezerv = "Пользователь";
        PMiddleNameGlobal_Rezerv = "Тестов";
        PSnils_Rezerv = "849-041-434 11";
        PNameRezerv_Id = "23165";

        PMOTarge2 = "БУ ХМАО-Югры \"Белоярская районная больница\"";
        PIdMoTarget2 = "54";
        POidMoTarget2 = "1.2.643.5.1.13.13.12.2.86.8986";

        //Request
        PMORequest = "БУ ХМАО-Югры \"Окружная клиническая больница\"";
        PIdMoRequest = "12";
        POidMoRequest = "1.2.643.5.1.13.13.12.2.86.8902";
        PIdmuMoRequest = "21126";
        if (KingNumber == 1) {
            PNameEquipment = "Chorus 1.5T";
            PidEquipment = "2905";
        }
        if (KingNumber == 2) {
            PNameEquipment = "Vivid 7";
            PidEquipment = "87929";
        }

        //Target
        if (KingNumber == 1 || KingNumber == 2) {
            PMOTarget = "БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"";
            PIdMoTarget = "110";
            POidMoTarget = "1.2.643.5.1.13.13.12.2.86.9003";
            if (KingNumber == 1) {
                PNameEquipment2 = "X-OMAT";
                PidEquipment2 = "17478";
                PId = "4885";
            } else {
                PNameEquipment2 = "X-OMAT";
                PidEquipment2 = "17478";
                PId = "5885";
            }
        } else if (KingNumber == 4) {
            PIdMoRequest = "100";

            PId = "3057";
            PMOTarget = "АУ ХМАО-Югры \"Кондинская районная стоматологическая поликлиника\"";
            PIdMoTarget = "110";
            POidMoTarget = "1.2.643.5.1.13.13.12.2.86.8987";
            PidEquipment = "2750";
            PidEquipment2 = "48434";
            PNameEquipment = "Gyroscan ACS-NT томограф тест МосмедИИ";
            PNameEquipment2 = "CDR Kit";

        }
    }

    @Step("Метод подключения по SHH")
    public void connectAndSendRequest (String SSH_USER, String SSH_HOST, Integer SSH_PORT, String SSH_PASSWORD, String LOCAL_PORT, String REMOTE_HOST, Integer REMOTE_PORT) throws Exception {

        // Устанавливаем SSH-подключение
        JSch jsch = new JSch();
        session = jsch.getSession(SSH_USER, SSH_HOST, SSH_PORT);
        session.setPassword(SSH_PASSWORD);

        // Отключаем проверку ключа хоста
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // Настраиваем перенаправление порта
        int assignedPort = session.setPortForwardingL(Integer.parseInt(LOCAL_PORT), REMOTE_HOST, REMOTE_PORT);
        System.out.println("Local port " + assignedPort + " is forwarding to " + REMOTE_HOST + ":" + REMOTE_PORT);
    }

    @Step("Запись данных из консоли в файл")
    public void terminal () {
        buffer = new ByteArrayOutputStream();
        OutputStream teeStream = new TeeOutputStream(System.out, buffer);
        // После этой строки любой вывод будет сохраняться в buffer
        System.setOut(new PrintStream(teeStream));
    }

    @Step("Метод для перезапуска упавшего теста")
    public static void RebaseTests () throws IOException {
        String str = "";
        String Word = "";
        if (TextUtils.isEmpty(remote_url_chrome)) {
            str = "ignored/RebaseTestLocal.bat";
            Word = "mvn test -Dtest=\"" + ReadPropFile("className") + "#" + ReadPropFile("methodName") + "\"";
        } else {
            str = "ignored/RebaseTestRemote.sh";
            Word = "mvn test -Dtest=\"" + ReadPropFile("className") + "#" + ReadPropFile(
                    "methodName") + "\" -DUrlChrome=" + remote_url_chrome + " -DContour=" + KingNumber + "";
        }

        // Создаем экземпляр PrintWriter, указывающий на файл
        PrintWriter writers = new PrintWriter("src/test/resources/" + str + "");
        // Закрываем PrintWriter, чтобы удалить содержимое файла
        writers.close();

        //Записываем упавшый тест в файл, чтобы после запустить
        if (RebaseTest) {
            Charset charset = StandardCharsets.UTF_8;
            Path path = Paths.get("src/test/resources/" + str + "");
            byte[] bytes = Word.getBytes(charset);
            Files.write(path, bytes);

            // Добавляем код для запуска файла
            if (TextUtils.isEmpty(remote_url_chrome)) {
                Runtime.getRuntime().exec("src/test/resources/" + str + " /C start");
            } else {
                Runtime.getRuntime().exec("src/test/resources/" + str + " /C start");
            }
        }
    }

    @Step("Метод для определения отправки в МИНИО")
    public void MINIOMethod () throws SQLException {
        sql = new SQL();
        sql.PrintSQL = false;
        sql.StartConnection("SELECT * FROM telmed.features where key = 'SmdToMinio';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("enabled");
        }
        if (Integer.valueOf(sql.value) == 1) {
            SmdToMinio = 1;
            DocumentDto = "documentDto.";
        } else {
            SmdToMinio = 0;
            DocumentDto = "";
        }
        sql.PrintSQL = true;
    }

    @Step("Метод для определения синхронизации с МТБЗ")
    public void MTBZMethod () throws SQLException {
        sql = new SQL();
        sql.StartConnection(
                "SELECT * FROM telmed.accessesdirectory where name = 'Доступ к синхронизации с сервисом оборудования МТБЗ';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("visible");
        }
        if (Integer.valueOf(sql.value) == 1) {
            MTBZ = true;
        } else {
            MTBZ = false;
        }
    }

    /**
     Метод записи названия тестового метода в properties
     */
    @BeforeEach
    public void GetMethod (TestInfo testInfo) {
        this.testInfo = testInfo;
    }

    /**
     Метод записи названия класса выполняемого теста в properties
     */
    public void InputClass () throws IOException {
        //Запись класса
        String str = this.getClass().getSimpleName();

        //Запись метода
        String strNameTest = testInfo.getTestMethod().orElseThrow().getName();

        //Запись DisplayName
        String DisplayNameTest = testInfo.getDisplayName();

        InputPropFile("className", str);
        InputPropFile("methodName", strNameTest);
        InputPropFile("displayNameTest", DisplayNameTest);
    }

    /**
     Метод записи названия класса выполняемого теста из properties в Файл
     */
    public static void InputClassFile () throws IOException {
        String str = "";

        FileInputStream in = new FileInputStream("src/test/resources/ignored/my.properties");
        Properties props = new Properties();
        props.load(in);
        in.close();

        String className = props.getProperty("className");
        String methodName = props.getProperty("methodName");
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                str = "ignored/FiledTests.bat";
            } else {
                str = "ignored/FiledTests.sh";
            }
            FileWriter writer = new FileWriter("src/test/resources/" + str + "", true);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.write(", " + className + "#" + methodName);
            bufferWriter.close();
        }
    }

    /**
     метод для создания рандомных переменных
     @param length   длинна нужного слова
     @param alphabet набор символов из которых будет состоять слово
     */
    public static String getRandomWord (int length, String alphabet) {
        StringBuilder sb = new StringBuilder(Math.max(length, 16));
        for (int i = 0; i < length; i++) {
            int len = alphabet.length();
            int random = RND.nextInt(len);
            char c = alphabet.charAt(random);
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     метод для ожидания элемента
     locator - локатор со страницы
     */
    public static void WaitElement (By locator) {
        wait.until(visibilityOfElementLocated(locator));
    }

    /**
     метод для ожидания, когда пропадёт элемент
     locator - локатор со страницы
     */
    public static void WaitNotElement (By locator) {
        wait.until(invisibilityOfElementLocated(locator));
    }

    /**
     метод для ожидания, когда пропадёт элемент с выставлением времени
     @param locator локатор со страницы
     @param time    время ожидания исчезновения элемента
     */
    public static void WaitNotElement3 (By locator, Integer time) {
        waitThree = new WebDriverWait(driver, Duration.ofSeconds(time));
        waitThree.until(invisibilityOfElementLocated(locator));
    }

    /**
     метод для ожидания, когда появится элемент с выставлением времени
     @param locator локатор со страницы
     @param time    время ожидания исчезновения элемента
     */
    public static void WaitElementTime (By locator, Integer time) {
        waitThree = new WebDriverWait(driver, Duration.ofSeconds(time));
        waitThree.until(visibilityOfElementLocated(locator));
    }

    /**
     метод для условия видимости элемента
     locator - локатор со страницы
     */
    public static boolean isElementVisible (By locator) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException te) {
            return false;
        }
    }

    /**
     метод для условия видимости элемента с ожиданием 3 секунды
     locator - локатор со страницы
     */
    public static boolean isElementNotVisible (By locator) {
        try {
            waitThree = new WebDriverWait(driver, Duration.ofSeconds(3));
            waitThree.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException te) {
            return false;
        }
    }

    /**
     метод для условия видимости элемента с ожиданием
     locator - локатор со страницы
     */
    public static boolean isElementVisibleTime (By locator, Integer time) {
        try {
            waitOne = new WebDriverWait(driver, Duration.ofSeconds(time));
            waitOne.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException te) {
            return false;
        }
    }

    /**
     метод для ввода данных в shadow-root
     element - веб элемент, куда нужно ввести данные
     word - слово для ввода
     */
    public static void inputWord (WebElement element, String word) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value='" + word + "'", element);
        element.sendKeys(Keys.BACK_SPACE);
    }

    /**
     метод для того, чтобы взять значение из shadow-root
     element - веб элемент, откуда нужно взять данные
     */
    public static String getShadow (WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String shadow = (String) js.executeScript("return arguments[0].value", element);
        return shadow;
    }

    /**
     метод для проверки 2 значений
     @param element1
     @param element2 веб элементы со страницы
     */
    @Step("Метод сравнивает два значения")
    public static boolean Assertions (String element1, String element2) {
        try {
            Assertions.assertEquals(element1, element2);
            System.out.println(element1 + " и " + element2 + " совпадают");
            return true;
        } catch (AssertionFailedError te) {
            System.out.println(element1 + " и " + element2 + " не совпадают");
            return false;
        }
    }

    /**
     метод для проверки 2 значений и записи ошибок в файл
     @param element1 1 элемент
     @param element2 2 элемент
     */
    @Step("Метод сравнивает два значения")
    public static boolean AssertionsWriteFile (String element1, String element2) throws IOException {
        try {
            Assertions.assertEquals(element1, element2);
            System.out.println(element1 + " и " + element2 + " совпадают");
            return true;
        } catch (AssertionFailedError te) {
            System.out.println(element1 + " и " + element2 + " не совпадают");
            String str = "В тесте \" " + ReadPropFile(
                    "displayNameTest") + " \" не совпадают элементы " + element1 + " и " + element2 + "";

            FileWriter writer = new FileWriter("src/test/resources/ignored/FiledTests.txt", true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write("\n" + str);
            bufferedWriter.close();
            return false;
        }
    }

    @Step("Метод сравнивает два списка и выводит различия между ними")
    public static void assertListsEqualIgnoreOrder (List<String> list1, List<String> list2, String textError) {
        List<String> copyOfList2 = new ArrayList<>(list2); // Копия второго списка для модификации
        List<String> missingInSecond = new ArrayList<>();   // Элементы из list1, отсутствующие в list2
        List<String> extraInSecond = new ArrayList<>();    // Элементы из list2, отсутствующие в list1

        // Поиск элементов из list1, которых нет в list2 (с учётом дубликатов)
        for (String item : list1) {
            if (!copyOfList2.remove(item)) {
                missingInSecond.add(item);
            }
        }

        // Оставшиеся элементы в copyOfList2 — лишние в list2
        extraInSecond.addAll(copyOfList2);
        System.out.println("Количество в 1 списке - " + list1.size());
        System.out.println("Количество в 2 списке - " + list2.size());

        // Формируем сообщение об ошибке, если есть расхождения
        if (!missingInSecond.isEmpty() || !extraInSecond.isEmpty()) {
            String errorMessage = textError + "\n";
            if (!missingInSecond.isEmpty()) {
                errorMessage += "- Отсутствуют во втором списке: " + missingInSecond + "\n";
            }
            if (!extraInSecond.isEmpty()) {
                errorMessage += "\n- Лишние элементы во втором списке: " + extraInSecond;
            }
            Assertions.fail(errorMessage);
        }
    }

    @Step("Метод для прокрутки страницы (Скролл)")
    public void Scroll (String scroll) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window,scrollBy(0, " + scroll + ")");
    }

    /**
     метод Найти эелемент, показать его и нажать
     element - веб элемент со страницы
     */
    public static void actionElementAndClick (WebElement element) throws InterruptedException {
        actions.moveToElement(element);
        actions.perform();
        Thread.sleep(1000);
        element.click();
    }

    /**
     метод Найти эелемент, показать его и нажать
     locator - локатор со страницы
     */
    @Step("Нажимаем на элемент {0}")
    public static void ClickElement (By locator) throws InterruptedException {
        WaitElement(locator);
        WebElement element = driver.findElement(locator);
        actions.moveToElement(element);
        actions.perform();
        Thread.sleep(1200);
        element.click();
    }

    /**
     метод Найти эелемент, показать его и нажать
     locator - локатор со страницы
     */
    @Step("Нажимаем на элемент {0}")
    public static void ClickElementTry (By locator) throws InterruptedException {
        WebElement element = driver.findElement(locator);
        actions.moveToElement(element);
        actions.perform();
        Thread.sleep(1200);
        element.click();
    }

    @Step("Метод Авторизации через любого пользователя")
    public void AuthoUser (String snils) throws Exception {
        LOGIN_PAGE_URL = HostAddressWeb + "/auth/bysnils?snils=" + snils + "&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91";
    }

    /**
     Авторизация
     SelectName - выбираемое значение из селекта
     */
    public static void AuthorizationMethod (By SelectName) throws InterruptedException {

        if (KingNumber == 10 | KingNumber == 12 | KingNumber == 13) {
            driver.manage().deleteAllCookies();
            driver.get(HostStart);
            WaitElement(By.xpath("//h3[contains(.,'Вход в систему')]"));
            inputWord(driver.findElement(By.xpath("//input[@placeholder='Логин']")), LoginBase + "1");
            inputWord(driver.findElement(By.xpath("//input[@placeholder='Пароль']")), PasswordBase + "1");
            ClickElement(By.xpath("//button[contains(.,'Войти')]"));
        } else {
            driver.get(LOGIN_PAGE_URL);
        }
        wait.until(visibilityOfElementLocated(By.xpath("//span[@class='el-input__suffix']")));
        driver.findElement(By.xpath("//span[@class='el-input__suffix']")).click();
        /** Ожидание */
        wait.until(visibilityOfElementLocated(By.cssSelector(".el-scrollbar__view")));
        wait.until(visibilityOfElementLocated(SelectName));
        WebElement element = driver.findElement(SelectName);
        actionElementAndClick(element);
        driver.findElement(By.xpath("//button[contains(.,'Выбрать')]")).click();
    }

    /**
     Авторизация
     SelectName - выбираемое значение из селекта
     */
    public static void AuthorizationLoginPassword (String url, String login, String password, String MO) throws InterruptedException {
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);
        driver.get(url);

        if (isElementVisibleTime(By.xpath("//h3[contains(.,'Вход в систему')]"), 10)) {
            WaitElement(By.xpath("//h3[contains(.,'Вход в систему')]"));
            inputWord(driver.findElement(By.xpath("//input[@placeholder='Логин']")), login + "1");
            inputWord(driver.findElement(By.xpath("//input[@placeholder='Пароль']")), password + "1");
            ClickElement(By.xpath("//button[contains(.,'Войти')]"));

            wait.until(visibilityOfElementLocated(By.xpath("//span[@class='el-input__suffix']")));
            driver.findElement(By.xpath("//span[@class='el-input__suffix']")).click();
            /** Ожидание */
            wait.until(visibilityOfElementLocated(By.cssSelector(".el-scrollbar__view")));
            ClickElement(authorizationObject.Select(MO));
            driver.findElement(By.xpath("//button[contains(.,'Выбрать')]")).click();
        }

        if (isElementVisibleTime(By.xpath("//h2[contains(.,'Предупреждение')]"), 20)) {
            Thread.sleep(1500);
            if (isElementVisibleTime(By.xpath("//h2[contains(.,'Предупреждение')]"), 20)) {
                if (!isElementNotVisible(By.xpath("(//button/span[contains(.,'Ок')])[2]"))) {
                    ClickElement(By.xpath("//button/span[contains(.,'Ок')]"));
                } else {
                    ClickElement(By.xpath("(//button/span[contains(.,'Ок')])[2]"));
                }
            }
        }
    }

    /**
     Метод для добавления нового пользователя (AddUsers)
     MO - Выбранная Мед. Организация
     Subdivision - выбранное подразделение
     Role - Выбранная Роль
     mail - почта пользователя
     */
    public static void AddUsersMethod (By MO, By Subdivision, By Role, String mail) throws InterruptedException {
        Users users = new Users(driver);
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);

        System.out.println("Пользователя нет в ИЕЭМК, добавляю");
        WaitElement(users.LastName);
        inputWord(driver.findElement(users.LastName), "Тести");
        inputWord(driver.findElement(users.Name), "Тести");
        if (isElementNotVisible(users.Login)) {
            inputWord(driver.findElement(users.Login), "Тести");
            inputWord(driver.findElement(users.Password), "Тести");
        }

        ClickElement(users.Date);
        ClickElement(users.DateYToday);
        driver.findElement(users.Number).sendKeys("+7 (111) 111-11-11");
        inputWord(users.Email, mail);

        if (isElementVisible(users.FRMR)) {
            ClickElement(users.FRMR);
        }

        System.out.println("Добавление места работы");
        if (KingNumber == 4) {
            Thread.sleep(5000);
        }
        driver.findElement(By.xpath("//button[@tooltiptext='Редактировать']/span[contains(.,'Добавить')]")).click();
        SelectClickMethod(By.xpath("//input[@placeholder = 'Укажите мед. организацию']"), MO);
        Thread.sleep(1500);
        if (KingNumber == 4) {
            Thread.sleep(5000);
        }
        SelectClickMethod(By.xpath("//input[@placeholder = 'Укажите подразделение']"), Subdivision);
        Thread.sleep(1500);
        if (KingNumber == 4) {
            Thread.sleep(5000);
        }
        SelectClickMethod(By.xpath("//input[@placeholder = 'Укажите роль пользователя']"), Role);
        driver.findElement(By.xpath(
                "//div[@class='el-dialog__footer']//button[@class='el-button el-button--success el-button--medium']/span[contains(.,'Добавить')]")).click();
        wait.until(visibilityOfElementLocated(
                By.xpath("//footer[@class='margin-top-30 text-right']//span[contains(.,'Добавить')]")));

        System.out.println("Добавление профиля");
        WaitElement(By.xpath("(//table[@class='el-table__body']//span[contains(.,'Профили')])[1]"));
        driver.findElement(By.xpath("(//table[@class='el-table__body']//span[contains(.,'Профили')])[1]")).click();
        wait.until(visibilityOfElementLocated(By.xpath("//h3[contains(.,'Медицинские профили')]")));
        ClickElement(users.ProfileAddButton);
        wait.until(invisibilityOfElementLocated(By.xpath("//span[contains(.,'Нет данных')]")));
        ClickElement(users.ProfileChoose);
        ClickElement(authorizationObject.Select("авиационной и космической медицине"));
        wait.until(invisibilityOfElementLocated(By.xpath("//div[@x-placement='bottom-start']")));
        driver.findElement(By.xpath("//button[@tooltiptext='Применить']")).click();
        Thread.sleep(300);
        driver.findElement(By.xpath("(//button/span[contains(.,'Закрыть')])[3]")).click();
        ClickElement(By.xpath("//footer[@class='margin-top-30 text-right']//span[contains(.,'Добавить')]"));
        Thread.sleep(1500);
        if (KingNumber == 4) {
            Thread.sleep(1500);
        }
    }

    /**
     Метод для добавления МО в "Запись на оборудование" (AddEquipmentsTest)
     @Razdel - выбор раздела в настройках обордования
     @MO - Мед. Организация для которой будет доступено оборудование
     @MoSearch - Мед. Организация, которая должна быть добавлена в список
     */
    public static void AddEquipmentsMethod (By Razdel, By MO, By MoSearch) throws InterruptedException {
        MedOrganization medOrganization = new MedOrganization(driver);
        driver.findElement(Razdel).click();
        wait.until(visibilityOfElementLocated(medOrganization.MOReceiveWait));
        if (!isElementNotVisible(MoSearch)) {
            ClickElement(medOrganization.AddWait);
            wait.until(visibilityOfElementLocated(medOrganization.SearchValueWait));
            wait.until(visibilityOfElementLocated(By.xpath("//div[@x-placement]")));
            WebElement organization1 = driver.findElement(MO);
            actionElementAndClick(organization1);
            ClickElement(medOrganization.AddMOWait);
            driver.findElement(Razdel).click();
        } else {
            driver.findElement(Razdel).click();
        }
    }

    /**
     Метод для нажатия на селект и выбор нужного значения
     element - веб элемент со страницы
     SelectName - элемент в списке селекта
     */
    public static void SelectClickMethod (By element, By SelectName) throws InterruptedException {
        ClickElement(element);
        ClickElement(SelectName);
    }

    /**
     метод для проверки расстояния подписей полей фильтрации (SignaturesTest)
     locator - локатор со страницы
     */
    public static void AssertSignatures (By locator) {
        WaitElement(locator);
        String element = driver.findElement(locator).getCssValue("margin-bottom");
        Assertions.assertEquals("15px", element);
    }

    /**
     метод для проверки расстояния между кнопками справа 10px (SignaturesTest)
     locator - локатор со страницы
     */
    public static void AssertSignaturesButtonRight10 (By locator) {
        WaitElement(locator);
        String element = driver.findElement(locator).getCssValue("margin-right");
        Assertions.assertEquals("10px", element);
    }

    /**
     метод для проверки расстояния между кнопками справа 5px (SignaturesTest)
     locator - локатор со страницы
     */
    public static void AssertSignaturesButtonRight5 (By locator) {
        WaitElement(locator);
        String element = driver.findElement(locator).getCssValue("margin-right");
        Assertions.assertEquals("5px", element);
    }

    /**
     метод для проверки расстояния между кнопками слева 5px (SignaturesTest)
     locator - локатор со страницы
     */
    public static void AssertSignaturesButtonLeft5 (By locator) {
        WaitElement(locator);
        String element = driver.findElement(locator).getCssValue("margin-left");
        Assertions.assertEquals("5px", element);
    }

    /**
     метод для проверки расстояния между кнопками слева 5px (SignaturesTest)
     locator - локатор со страницы
     */
    public static void AssertSignaturesButtonRightAndLeft (By locator) {
        WaitElement(locator);
        String element = driver.findElement(locator).getCssValue("padding-right");
        String element1 = driver.findElement(locator).getCssValue("padding-left");
        Assertions.assertEquals("5px", element);
        Assertions.assertEquals("5px", element1);
    }
}
