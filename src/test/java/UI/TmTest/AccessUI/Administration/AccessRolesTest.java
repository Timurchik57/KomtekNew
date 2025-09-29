package UI.TmTest.AccessUI.Administration;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.Administration.AcceessRoles;
import UI.TmTest.PageObject.AuthorizationObject;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Администрирование")
@Tag("Роли_доступа")
public class AccessRolesTest extends BaseAPI {
    AuthorizationObject authorizationObject;
    AcceessRoles acceessRoles;

    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка поиска в роли доступа")
    @Description("Пробуем писать в поиске ролей доступа различными регистрами")
    public void AccessRole() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        acceessRoles = new AcceessRoles(driver);

        AuthorizationMethod(authorizationObject.OKB);

        /** Переход в Роли Доступа */
        WaitElement(acceessRoles.RolesWait);
        actionElementAndClick(acceessRoles.Roles);

        /** Редактирование роли */
        System.out.println("Редактирование роли");
        WaitElement(acceessRoles.HeaderRoles);
        while (!isElementNotVisible(acceessRoles.RoleAdministrator)) {
            acceessRoles.Next.click();
        }
        actionElementAndClick(acceessRoles.Edit);
        WaitElement(acceessRoles.EditRole);
        WaitElement(acceessRoles.InputWordWait);

        System.out.println("Ввод прописных букв");
        if (KingNumber == 4) {
            inputWord(acceessRoles.InputWord, "Типы регистров");
            WaitElement(acceessRoles.Check("15. Доступ к разделу \"Типы регистров\""));
        } else {
            inputWord(acceessRoles.InputWord, "процедурныйй");
            WaitElement(acceessRoles.Check("16. Процедурный кабинет"));
        }
        inputWord(acceessRoles.InputWord, "реестрыы");
        Thread.sleep(1000);

        System.out.println("Ввод заглавных букв");
        if (KingNumber == 4) {
            inputWord(acceessRoles.InputWord, "ТИПЫ РЕГИСТРОВ");
            WaitElement(acceessRoles.Check("15. Доступ к разделу \"Типы регистров\""));
        } else {
            inputWord(acceessRoles.InputWord, "ПРОЦЕДУРНЫЙЙ");
            WaitElement(acceessRoles.Check("16. Процедурный кабинет"));
        }
        inputWord(acceessRoles.InputWord, "реестрыы");
        Thread.sleep(1000);

        System.out.println("Ввод заглавных и прописных букв");
        if (KingNumber == 4) {
            inputWord(acceessRoles.InputWord, "ТиПы РЕГиСТРоВ");
            WaitElement(acceessRoles.Check("15. Доступ к разделу \"Типы регистров\""));
        } else {
            inputWord(acceessRoles.InputWord, "ПрОцЕдУрНыЙй");
            WaitElement(acceessRoles.Check("16. Процедурный кабинет"));
        }
        Thread.sleep(1000);
        System.out.println("Поиск работает корректно!");
    }
}
