package UI.TmTest.AccessUI.NSI;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.NSI.Equipments;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("НСИ")
@Tag("Проверка_БД")
@Tag("Оборудование")
public class Access_1526Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    Equipments equipments;

    public String name;
    public String MO;

    @Test
    @Issue(value = "TEL-1526")
    @Link(name = "ТМС-1187", url = "https://team-1okm.testit.software/projects/5/tests/1187?isolatedSection=ccb1fcf9-9e3b-44d1-9bad-7959d251a43d")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Проверка Отображения подразделении c БД")
    @Description("Открыть ВИМИС - Оборудование - Редактировать оборудование - Сверка значений с БД")
    public void Access_1526() throws SQLException, InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        equipments = new Equipments(driver);

        System.out.println("Определяем параметр по МТБЗ");
        MTBZMethod();

        if (!MTBZ) {
            System.out.println("Переход в НСИ - Оборудование");
            AuthorizationMethod(authorizationObject.OKB);
            WaitElement(equipments.EquipmentWait);
            actionElementAndClick(equipments.Equipment);
            WaitElement(equipments.HeaderEquipmentWait);

            System.out.println("Оборудование - выбор МО");
            if (KingNumber == 4) {
                SelectClickMethod(equipments.InputMoWait, equipments.SelectMOOKB);
                inputWord(equipments.Description, "1233");
                ClickElement(equipments.CheckIs);
            } else {
                SelectClickMethod(equipments.InputMoWait, equipments.SelectMO);
                inputWord(equipments.Name, "X-OMATT");
                inputWord(equipments.Description, "1233");
            }
            Thread.sleep(1500);
            WaitNotElement3(equipments.SearchLoading, 20);
            ClickElement(equipments.SearchWait);

            System.out.println("Оборудование - выбор 1 из списка");
            WaitElement(equipments.NameColumnWait);
            Thread.sleep(1500);
            WaitNotElement3(equipments.Loading, 30);
            equipments.Edit1.click();

            System.out.println("Редактировать - Подразделение МО");
            WaitElement(equipments.HeaderEditWait);
            ClickElement(equipments.DivisionWait);
            Thread.sleep(2000);
            List<String> getDivision = new ArrayList<String>();
            List<WebElement> office = driver.findElements(authorizationObject.SelectALL);
            for (int i = 0; i < office.size(); i++) {
                getDivision.add(office.get(i).getText());
            }
            String depart_name;
            List<String> SQLOffice = new ArrayList<String>();
            if (KingNumber != 4) {
                name = "Инфекционное отделение";
                MO = "БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"";
            } else {
                name = "Женская консультация";
                MO = "БУ ХМАО-Югры \"Окружная клиническая больница\"";
            }
            sql.StartConnection("select ms.namemu, m.depart_name from dpc.mo_subdivision_structure m\n" +
                    "left join dpc.mis_sp_mu ms on m.mo_oid = ms.oid \n" +
                    "where ms.namemu = '" + MO + "' order by m.depart_name ASC;");
            while (sql.resultSet.next()) {
                depart_name = sql.resultSet.getString("depart_name");
                if (KingNumber != 4) {
                    SQLOffice.add(depart_name);
                } else {
                    SQLOffice.add(depart_name);
                }
            }
            Assertions.assertEquals(getDivision, SQLOffice, "Значения не совпадают");
        }
    }
}
