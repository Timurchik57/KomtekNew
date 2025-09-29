package UI.TmTest.PageObject.Directions.Consultation;

import Base.BaseTest;
import Base.SQL;
import io.qameta.allure.Step;
import org.apache.hc.core5.util.TextUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConsultationOutgoingUnfinished extends BaseTest {
    /**
     * Консультации / Исходящие / Незавершенные
     */
    public ConsultationOutgoingUnfinished(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Консультации / Исходящие / Незавершенные
     */
    public By Consultation = By.xpath(
            "//a[@href='/direction/requestConsultation/uncompleted?directionType=2&directionTarget=1&directionPageType=1']");

    /**
     * Разворачиваем селекты
     */
    public By SelectAllClick = By.xpath("//button[@class='el-button el-tooltip m-0 el-button--default el-button--small']");

    /**
     * Консультации / Исходящие / Незавершенные - Статус
     */
    public By Status (String value) {
        By Add = By.xpath("//div[@class='form-item-label'][contains(.,'"+value+"')]/following-sibling::div");
        return Add;
    }

    /**
     * Консультации / Исходящие / Незавершенные - Статус
     */
    public By Search = By.xpath("//button[contains(.,'Поиск')]");

    /**
     * Исходящие / Незавершенные - Сортировка по id в обратном порядке
     */
    public By SortDesc = By.xpath("(//span/i[@class='sort-caret descending'])[1]");

    /**
     * Консультации / Исходящие / Незавершенные - 1 запись в таблице
     */
    public By ConsultationFirst = By.xpath("//tbody//tr[1]");

    /**
     * Консультации / Исходящие / Незавершенные - 1 запись в таблице - берём её цвет
     */
    public By ConsultationFirstColor = By.xpath("//tbody//tr[1]/td[1]");

    /**
     * Консультации / Исходящие / Незавершенные - Записи на странице - 1 запись - Отправлен - Фамилия
     **/
    public By LastName = By.xpath("(//tbody/tr/td/div/span[contains(.,'Отправлен')])[1]/preceding::td[2]");
    public String Class = "el-button el-button--primary el-button--small";

    /**
     * Консультации / Исходящие / Незавершенные - Записи на странице - с нужным статусом
     */
    public By Line (String status) {
        By Add = By.xpath("//tbody/tr/td/div/span[contains(.,'"+status+"')]");
        return Add;
    }

    /**
     * Консультации / Исходящие / Незавершенные - Записи на странице - всё количество записей
     */
    public By ConsultationCountList = By.xpath("//tbody/tr");

    /**
     * Консультации / Исходящие / Незавершенные - Следующая страница
     */
    public By NextWait = By.xpath("//button[@class='btn-next']");

    /**
     * Консультации / Исходящие / Незавершенные - Всего консультаций
     */
    public By ConsultationCount = By.xpath("//span[@class='el-pagination__total']");

    /**
     * Консультация - Кнопки
     */
    public By Button (String value) {
        By Add = By.xpath("//ul[@class='list-inline margin-0']/li[contains(.,'"+value+"')]");
        return Add;
    }

    /**
     * Консультация - Статус
     **/
    public By StatusConsultation = By.xpath("//span[contains(.,'Номер консультации')]/following-sibling::span/span");

    /**
     * Консультация - Отменить запрос - Причина отмены
     */
    public By ReasonCancellation = By.xpath("//label[contains(.,'Причина отмены')]/following-sibling::div//textarea");

    /**
     * Консультация - Отменить запрос - Отменить консультацию
     */
    public By ButtonCancellation = By.xpath("//button[contains(.,'Отменить консультацию')]");

    /**
     * Консультация - Запись на приём
     */
    public By AddReception = By.xpath("//button[contains(.,'Записать на прием')]");

    /**
     * Консультация - id записи
     */
    public By NumberConsultation = By.xpath("//span[contains(.,'Номер консультации')]");

    /**
     * Консультация - Дата отправки консультации
     */
    public By DateConsultation = By.xpath("//td[contains(.,'Дата отправки консультации:')]/following-sibling::td");

    /**
     * Консультация - Данные консультации
     */
    public By Consul (String value) {
        By Consultation = By.xpath("(//tr/td[contains(.,'"+value+"')]/following-sibling::td//span)[1]");
        return Consultation;
    }

    /**
     * Консультация - Данные консультации - Кнопки
     */
    public By ConsulButton (String value) {
        By Consultation = By.xpath("//tr/td[contains(.,'"+value+"')]/following-sibling::td//button");
        return Consultation;
    }

    /**
     * Консультация - Данные консультации - Кнопки - Селект после нажатия кнопки
     */
    public By ClickSelect = By.xpath("//div[@class='el-input el-input--medium el-input--suffix']");

    /**
     * Консультация - Добавить ссылку
     */
    @FindBy(xpath = "//button[contains(.,'Добавить ссылку')]")
    public WebElement AddLink;
    public By AddLinkWait = By.xpath("//button[contains(.,'Добавить ссылку')]");

    /**
     * Консультация - Добавить файлы
     */
    @FindBy(xpath = "//button[contains(.,'Добавить файлы')]")
    public WebElement AddFile;
    public By AddFileWait = By.xpath("//button[contains(.,'Добавить файлы')]");

    /**
     * Консультация - Добавить файлы - Прикрепление файла
     */
    @FindBy(xpath = "(//input[@type='file'])[4]")
    public WebElement File;

    /**
     * Консультация - Добавить файлы - Закрыть
     */
    public By AddFileClose = By.xpath(
            "(//button[@class='el-button el-button--primary el-button--medium is-plain'][contains(.,'Закрыть')])[3]");

    /**
     * Консультация - Добавить файлы - 2 добавленный файл
     */
    public By AddFileDownload2 = By.xpath("(//tbody/tr[contains(.,'test.txt')])[4]/td[contains(.,'Скачать')]");

    /**
     * Консультация - Скачать архивом
     */
    @FindBy(xpath = "//button[contains(.,'Скачать архивом')]")
    public WebElement Download;
    public By DownloadWait = By.xpath("//button[contains(.,'Скачать архивом')]");

    /**
     * Консультация - Сформировать протокол
     */
    @FindBy(xpath = "//button[contains(.,'Сформировать протокол')]")
    public WebElement AddProt;
    public By AddProtWait = By.xpath("//button[contains(.,'Сформировать протокол')]");

    /**
     * Консультация - Сформировать протокол - Сформированный протокол
     */
    public By CreateDirectionAfter = By.xpath("//td[contains(.,'Протокол_Тестировщик_Т.Т.docx')]");

    @Step("Метод отмены созданной консультации")
    public void CancelConsultation (String patientGuid) throws SQLException {
        SQL sql = new SQL();
        List list = new ArrayList();

        sql.StartConnection(
                "Select d.id, d.status, d.directionguid, d.diagnos, d.consultationtype, op.guid  from telmed.directions d \n" +
                        "join iemk.op_patient_reg op on d.patient_id = op.patient_id\n" +
                        "where op.guid = '" + patientGuid + "' and d.consultationtype = '1' and d.status not in ('99', '5', '4')");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
            list.add(sql.value);
        }

        if (!TextUtils.isEmpty(sql.value)) {
            for (int i = 0; i < list.size(); i++) {
                sql.UpdateConnection("update telmed.directions set status = '99' where id = '" + list.get(i) + "';");
            }
        }
    }
}
