package UI.TmTest.AccessUI.VIMIS;

import Base.BaseAPI;
import Base.SQL;
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
import org.openqa.selenium.By;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Вимис")
@Tag("Основные")
@Tag("Клинические_рекомендации")
public class ClinicalRecommendationsTest extends BaseAPI {

    AuthorizationObject authorizationObject;
    UI.TmTest.PageObject.VIMIS.ClinicalRecommendations clinicalRec;
    SQL sql;
    String count;
    Integer countInt;
    Integer countBD;

    @Issue(value = "TEL-569")
    @Issue(value = "TEL-3405")
    @Owner(value = "Галиакберов Тимур")
    @Description("Авторизация и переход в ВИМИС - Клинические рекомендации. Проверяем отображение у выбранного направления в связке с БД")
    @DisplayName("Проверка работы фильтра по Направлению в разделе ВИМИС - Клинические рекомендации")
    @Test
    public void ClinicalRecommendations() throws InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        clinicalRec = new ClinicalRecommendations(driver);
        sql = new SQL();
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Переход в ВИМИС - Клинические рекомендации");
        ClickElement(clinicalRec.ClinicalRecommendationsWait);
        WaitElement(clinicalRec.Header);
        ChooseDirectionsMethod(authorizationObject.Select("1 - Онкология"), "Онкология", 1);
        ChooseDirectionsMethod(authorizationObject.Select("2 - Профилактика"), "Профилактика", 2);
        ChooseDirectionsMethod(authorizationObject.Select("3 - Акушерство и неонатология"), "Акушерство и неонатология", 3);
        ChooseDirectionsMethod(authorizationObject.Select("4 - Сердечно-сосудистые заболевания"), "Сердечно-сосудистые заболевания", 4);
        ChooseDirectionsMethod(authorizationObject.Select("5 - Инфекционные болезни"), "Инфекционные болезни", 5);
        ChooseDirectionsMethod(authorizationObject.Select("99 - Иные профили"), "Иные профили", 99);
    }

    /**
     * Метод для выбора направления и сравнение значений с БД
     * @param Diagnosis Локатор диагноза
     * @param NameDiagnosis Название направления
     * @param NumberDiagnosis Нумерация направления
     */
    @Step("Метод для выбора направления и сравнение значений с БД")
    public void ChooseDirectionsMethod(By Diagnosis, String NameDiagnosis, int NumberDiagnosis) throws InterruptedException, SQLException {
        SQL sql = new SQL();
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);
        clinicalRec = new ClinicalRecommendations(driver);

        System.out.println("Выбор направления медицинской помощи");
        SelectClickMethod(clinicalRec.GetField("Направление"), Diagnosis);
        Thread.sleep(1500);
        ClickElement(clinicalRec.SearchWait);

        System.out.println("Обработка данных таблицы. Проверка Наименования - " + NameDiagnosis + "");
        Thread.sleep(1500);
        authorizationObject.LoadingTime(20);
        if (isElementVisibleTime(clinicalRec.AllCount, 20)) {
            countInt = Integer.valueOf(driver.findElement(clinicalRec.AllCount).getText().substring(6));
        } else {
            countInt = 0;
        }

        List<String> BD = new ArrayList<>();
        if (NameDiagnosis.equals("Иные профили")) {
            System.out.println("зашли");    sql.StartConnection("select count(*) from vimis.clinrec;");
            while (sql.resultSet.next()) {
                countBD = Integer.valueOf(sql.resultSet.getString("count"));
            }
            System.out.println("Проверка значений");
            Assertions.assertEquals(countInt, countBD);
            System.out.println("-----Значения для " + NameDiagnosis + " совпадают");
        } else {
            sql.StartConnection("select count(*) from vimis.clinrec c \n" +
                    "join vimis.clinrec_diagnoses cd on c.id = cd.clinical_recommendation_id\n" +
                    "join dpc.mkb10 m on cd.mkb_code = m.mkb_code \n" +
                    "join vimis.mkb_vmcl mv on m.id = mv.mkb_id\n" +
                    "where mv.vmcl = '" + NumberDiagnosis + "' group by c.\"name\", c.begin_date;");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("count");
                BD.add(sql.value);
            }
            System.out.println("Проверка значений");
            Assertions.assertEquals(countInt, BD.size());
            System.out.println("-----Значения для " + NameDiagnosis + " совпадают");
        }
    }
}
