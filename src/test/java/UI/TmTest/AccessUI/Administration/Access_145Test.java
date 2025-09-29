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
public class Access_145Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    TypeRegistr typeRegistr;

    @Test
    @Issue(value = "TEL-145")
    @Issue(value = "TEL-1982")
    @Link(name = "ТМС-1530", url = "https://team-1okm.testit.software/projects/5/tests/1530?isolatedSection=aee82730-5a5f-42aa-a904-10b3057df4c4")
    @Link(name = "ТМС-1882", url = "https://team-1okm.testit.software/projects/5/tests/1882?isolatedSection=767caae3-1d53-4e9c-a277-e3122b6d6370")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("В \"Диагноз(ы)\" убирать значения из списка")
    @Description("Переходим в администрирование - типы регистров - Добавить регистр - добавить диагноз и потом попробовать добавить его снова")
    public void Access_145() throws InterruptedException {

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
        SelectClickMethod(typeRegistr.SourceData, typeRegistr.SelectSourceDataOnko);
        typeRegistr.AddDiagnosis.click();
        WaitElement(typeRegistr.SelectAddDiagnosisWait);
        System.out.println("Добавляем код регистра");

        /** Проверяем заявку 1982 */
        inputWord(typeRegistr.CodRegistr, "Холераа");
        WaitElement(typeRegistr.CodRegistr("Холера"));

        inputWord(typeRegistr.CodRegistr, "A00 ");
        Thread.sleep(1000);
        if (KingNumber == 4) {
            Thread.sleep(2500);
        }
        ClickElement(typeRegistr.CodRegistrA00);
        ClickElement(typeRegistr.Save);
        Thread.sleep(1000);
        if (KingNumber == 4) {
            Thread.sleep(2500);
        }
        typeRegistr.AddDiagnosis.click();
        WaitElement(typeRegistr.SelectAddDiagnosisWait);
        System.out.println("Проверяем, что выбранный код не отображается");
        inputWord(typeRegistr.CodRegistr, "A00 ");
        WaitNotElement(typeRegistr.CodRegistrA00);
        if (KingNumber == 4) {
            Thread.sleep(2500);
        }
        System.out.println("Добавляем диапазон значений и проверяем, что все значения из диапазона отсутствуют");
        actionElementAndClick(typeRegistr.FirstCodRegistr2);
        actionElementAndClick(typeRegistr.LastCodRegistr);
        Thread.sleep(1000);
        if (KingNumber == 4) {
            Thread.sleep(2500);
        }
        typeRegistr.AddDiagnosis.click();
        WaitElement(typeRegistr.SelectAddDiagnosisWait);
        System.out.println("Проверяем код из диапазона, что он не отображается");
        inputWord(typeRegistr.CodRegistr, "A00.1 ");
        Thread.sleep(1000);
        if (KingNumber == 4) {
            Thread.sleep(2500);
        }
        WaitNotElement(typeRegistr.CodRegistrA001);
    }
}
