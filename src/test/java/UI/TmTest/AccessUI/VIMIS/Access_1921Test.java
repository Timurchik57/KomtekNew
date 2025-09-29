package UI.TmTest.AccessUI.VIMIS;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.VIMIS.ClinicalRecommendations;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Вимис")
@Tag("Клинические_рекомендации")
public class Access_1921Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    ClinicalRecommendations clinicalRec;
    String count;

    @Issue(value = "TEL-1921")
    @Issue(value = "TEL-3354")
    @Link(name = "ТМС-1805", url = "https://team-1okm.testit.software/projects/5/tests/1805?isolatedSection=1f9b0804-847c-4b2c-8be6-2d2472e56a75")
    @Link(name = "ТМС-2051", url = "https://team-1okm.testit.software/projects/5/tests/2051?isolatedSection=af882452-d09a-4051-af99-3d04bf599188")
    @Owner(value = "Галиакберов Тимур")
    @Description("Переходим в ВИМИС - Клинические рекомендации - проверяем работу фильтров")
    @Test
    @DisplayName("Проверка фильров в Клинические рекомендации")
    public void Access_1921() throws InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        clinicalRec = new ClinicalRecommendations(driver);
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Переход в ВИМИС - Клинические рекомендации");
        ClickElement(clinicalRec.ClinicalRecommendationsWait);

        System.out.println("1 проверка - Наименование");
        SelectClickMethod(clinicalRec.GetField("Направление"), authorizationObject.Select("1 - Онкология"));
        SelectClickMethod(clinicalRec.GetField("Название документа"), authorizationObject.Select("Гепатобластома"));
        MoveTableMethod("where mv.vmcl = '1' and c.\"name\" = 'Гепатобластома'");

        System.out.println("2 проверка - Возрастная группа");
        SelectClickMethod(clinicalRec.GetField("Направление"), authorizationObject.Select("2 - Профилактика"));
        SelectClickMethod(clinicalRec.GetField("Возрастная группа"), authorizationObject.Select("Дети"));
        /** vimis.clinrec.age_group = 1 - Дети
         *  vimis.clinrec.age_group = 0 - Взрослые */
        MoveTableMethod("where mv.vmcl = '2' and c.age_group = '1'");

        System.out.println("3 проверка - Тип клинической рекомендации");
        SelectClickMethod(clinicalRec.GetField("Направление"), authorizationObject.Select("1 - Онкология"));
        SelectClickMethod(clinicalRec.GetField("Тип клинической рекомендации"), authorizationObject.Select("Федеральный"));
        /** vimis.clinrec.standard_type = 2 - Региональный
         *  vimis.clinrec.standard_type = 1 - Федеальный */
        MoveTableMethod("where mv.vmcl = '1' and c.standard_type = '1'");

        System.out.println("4 проверка - Диагнозы");
        SelectClickMethod(clinicalRec.GetField("Направление"), authorizationObject.Select("4 - Сердечно-сосудистые заболевания"));
        ClickElement(clinicalRec.CodeService);
        WaitElement(clinicalRec.CodeServiceInput);
        inputWord(driver.findElement(clinicalRec.CodeServiceInput), "I20.0 ");
        Thread.sleep(1500);
        authorizationObject.LoadingTime(10);
        ClickElement(authorizationObject.Select("I20.0"));
        ClickElement(authorizationObject.ButtonTrue("Сохранить", "1"));
        MoveTableMethod("where mv.vmcl = '4' and mv.mkb_code = 'I20.0'");

        System.out.println("5  проверка - дата начала");
        SelectClickMethod(clinicalRec.GetField("Направление"), authorizationObject.Select("3 - Акушерство и неонатология"));
        ClickElement(clinicalRec.GetField("Дата начала действия"));
        WaitElement(clinicalRec.Year);
        String year = driver.findElement(clinicalRec.Year).getText();
        while (year.contains("2020") == false) {
            ClickElement(clinicalRec.YearAgo);
            year = driver.findElement(clinicalRec.Year).getText();
        }
        while (year.contains("Январь") == false) {
            ClickElement(clinicalRec.MonthAgo);
            year = driver.findElement(clinicalRec.Year).getText();
        }
        ClickElement(clinicalRec.First);
        ClickElement(clinicalRec.First);
        MoveTableMethod("where c.begin_date between '01.01.2020' and '01.01.2020' and mv.vmcl = 3 group by c.id, c.\"name\"");
    }

    @Step("Метод для проверки фильтра в Клинических рекомендациях")
    public void MoveTableMethod(String SQL) throws InterruptedException, SQLException {
        ClickElement(clinicalRec.SearchWait);
        Thread.sleep(1500);
        authorizationObject.LoadingTime(20);
        if (isElementVisibleTime(clinicalRec.AllCount, 30)) {
            count = driver.findElement(clinicalRec.AllCount).getText().substring(6);
        } else {
            count = "0";
        }

        List<String> BD = new ArrayList<>();
        sql.StartConnection("select c.id, c.\"name\" from vimis.clinrec c \n" +
                "join vimis.clinrec_diagnoses cd on c.id = cd.clinical_recommendation_id\n" +
                "join vimis.mkb_vmcl mv on cd.mkb_code = mv.mkb_code\n" +
                ""+SQL+";");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
            if (!BD.contains(sql.value)) {
                BD.add(sql.value);
            }
        }

        System.out.println("Проверка значений");
        Assertions.assertEquals(count, String.valueOf(BD.size()), "Количество записей не совпадает с БД");
        ClickElement(clinicalRec.Reset);
    }
}