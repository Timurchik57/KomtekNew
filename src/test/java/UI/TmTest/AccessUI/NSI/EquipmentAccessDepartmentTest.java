package UI.TmTest.AccessUI.NSI;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.NSI.Equipments;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("НСИ")
public class EquipmentAccessDepartmentTest extends BaseAPI {
    AuthorizationObject authorizationObject;
    Equipments equipments;
    public String name;
    public String MO;

    @Test
    @Issue(value = "TEL-668")
    @Link(name = "ТМС-1187", url = "https://team-1okm.testit.software/projects/5/tests/1187?isolatedSection=ccb1fcf9-9e3b-44d1-9bad-7959d251a43d")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Проверка Отображения отделения при выбранном подразделении")
    @Description("Открыть ВИМИС - Оборудование - Редактировать оборудование - Сверка значений с БД")
    public void EquipmentAccessDepartment() throws SQLException, InterruptedException {
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
            } else {
                SelectClickMethod(equipments.InputMoWait, equipments.SelectMO);
                inputWord(equipments.Name, "X-OMATT");
            }
            inputWord(equipments.Description, "1233");
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
            WaitElement(equipments.DivisionWait);
            if (KingNumber == 4) {
                SelectClickMethod(equipments.DivisionWait, equipments.SelectDivision);
            } else {
                SelectClickMethod(equipments.DivisionWait, equipments.SelectDivisionInf);
            }

            System.out.println("Редактировать - Выбор Отделения");
            ClickElement(equipments.OfficeWait);
            Thread.sleep(1000);
            List<String> getOffice = new ArrayList<String>();
            List<WebElement> office = driver.findElements(equipments.SelectOfficeWaitt);
            for (int i = 0; i < office.size(); i++) {
                getOffice.add(office.get(i).getText());
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
            sql.StartConnection(
                    "select ms.namemu, m.depart_name, b.oid, b.hospital_name from dpc.mo_subdivision_structure m\n" +
                            "left join dpc.mis_sp_mu ms on m.mo_oid = ms.oid \n" +
                            "left join dpc.mo_branches b on m.depart_oid = b.depart_oid\n" +
                            "where ms.namemu = '" + MO + "'\n" +
                            "and m.depart_name = '" + name + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("oid");
                depart_name = sql.resultSet.getString("depart_name");
                if (KingNumber != 4) {
                    SQLOffice.add("" + depart_name + " (" + sql.value + ")");
                } else {
                    SQLOffice.add("- (" + sql.value + ")");
                }
            }
            Collections.sort(SQLOffice);
            Assertions.assertEquals(getOffice, SQLOffice);
            System.out.println("Значения " + getOffice + " и " + SQLOffice + " совпадают");
        }
    }
}
