package UI.TmTest.AccessUI.Administration;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.Administration.TypeRegistr;
import UI.TmTest.PageObject.AuthorizationObject;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Администрирование")
@Tag("Типы_регистров")
@Tag("Регистр_ССЗ")
public class Access_142Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    TypeRegistr typeRegistr;

    @Test
    @Issue(value = "TEL-142")
    @Link(name = "ТМС-1528", url = "https://team-1okm.testit.software/projects/5/tests/1528?isolatedSection=aee82730-5a5f-42aa-a904-10b3057df4c4")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("В Услуги убирать значения из списка")
    @Description("Переходим в администрирование - типы регистров - Добавить регистр - в \"Источник данных\" выбрать 4 - Сердечно-сосудистые заболевания -Отметить чек-бокс \"Признак отбора\" и выбрать \"По дате оперативного вмешательства\" - добавить услугу в операции и проверить, что больше нельзя её выбрать")
    public void Access_142() throws InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        typeRegistr = new TypeRegistr(driver);

        AuthorizationMethod(authorizationObject.OKB);

        /** Переход в Типы регистров */
        WaitElement(typeRegistr.TypeRegistrWait);
        ClickElement(typeRegistr.TypeRegistrWait);

        System.out.println("Добавление регистра");
        ClickElement(typeRegistr.AddRegistrWait);
        WaitElement(typeRegistr.HeaderAddRegistrWait);
        inputWord(typeRegistr.InputNameRegistr, "ТЕСТТ");
        inputWord(typeRegistr.InputShortNameRegistr, "ТЕСТОВИЧЧ");
        SelectClickMethod(typeRegistr.SourceData, typeRegistr.SelectSSZ);
        ClickElement(typeRegistr.Sign);
        ClickElement(typeRegistr.OP);
        ClickElement(typeRegistr.Operations);
        ClickElement(typeRegistr.AddService);

        System.out.println("Ищем код услуги и добавляем его");
        inputWord(typeRegistr.CodeService, "A26.08.028 ");
        Thread.sleep(1000);
        if (KingNumber == 4) {
            Thread.sleep(4000);
        }
        actionElementAndClick(typeRegistr.AddCodeService);
        ClickElement(typeRegistr.AddService);

        System.out.println("Ищем код услуги и проверяем, что не отображается");
        inputWord(typeRegistr.CodeService, "A26.08.028 ");
        Thread.sleep(1000);
        if (KingNumber == 4) {
            Thread.sleep(4000);
        }
        WaitNotElement(typeRegistr.AddCodeServiceWait);
    }
}
