package UI.TmTest.AccessUI.VIMIS;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.VIMIS.RestrictionsIS;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;

import java.sql.SQLException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Вимис")
@Tag("Ограничения_ИС")
public class Access_3338Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    RestrictionsIS restrictionsIS;

    @Test
    @Order(1)
    @Issue(value = "TEL-3338")
    @Issue(value = "TEL-3388")
    @Owner(value = "Галиакберов Тимур")
    @Link(name = "ТМС-2050", url = "https://team-1okm.testit.software/projects/5/tests/2050?isolatedSection=af882452-d09a-4051-af99-3d04bf599188")
    @Description("Переходим в Вимис - Список ограничений на назначение инструментальных исследований и проверяем Поиск, создание, удаление, изменение")
    @DisplayName("Поиск, создание, удаление, изменение в Список ограничений на назначение инструментальных исследований")
    public void Access_3338() throws SQLException, InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        restrictionsIS = new RestrictionsIS(driver);

        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(restrictionsIS.RestrictionIS);

        System.out.println("1 проверка - Проверяем количество всех записей");
        GetSql("select count (*) from telmed.service_restrictions sr;", "Количество всех записей не совпадает");

        System.out.println("2 проверка - Проверяем количество записей по 'Медицинская организация'");
        SetFilter("Медицинская организация",
                "Яцкив ",
                "БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"",
                "select count (*) from telmed.service_restrictions sr\n" +
                        "join dpc.mis_sp_mu m on sr.targetmedicalidmu = m.idmu where m.namemu = 'БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"';",
                "Количество записей по 'Медицинская организация' не совпадает");

        System.out.println("3 проверка - Проверяем количество записей по 'Диагноз'");
        SetFilter("Диагноз",
                "J62 ",
                "J62 Пневмокониоз, вызванный пылью, содержащей кремний",
                "select count (*) from telmed.service_restrictions sr where diagnosiscode = 'J62';",
                "Количество записей по 'Диагноз' не совпадает");

        System.out.println("4 проверка - Проверяем количество записей по 'Назначаемая услуга'");
        SetFilter("Назначаемая услуга",
                "A.06.10.010.01 ",
                "A.06.10.010.01 HMP30",
                "select count (*) from telmed.service_restrictions sr where servicecode = 'A.06.10.010.01';",
                "Количество записей по 'Назначаемая услуга' не совпадает");

        System.out.println("5 проверка - Проверяем количество записей по 'Анатомическая область'");
        SetFilter("Анатомическая область",
                "Бедренная кость ",
                "Бедренная кость",
                "select count (*) from telmed.service_restrictions sr \n" +
                        "join dpc.anatom_localation a on sr.anatomicallocationid = a.id where a.\"name\" = 'Бедренная кость';",
                "Количество записей по 'Анатомическая область' не совпадает");

        System.out.println("6 проверка - Проверяем количество записей по 'Лабораторный тест'");
        SetFilter("Лабораторный тест",
                "Тканевый активатор ",
                "1000959 Тканевый активатор плазминогена, массовая концентрация в бедной тромбоцитами плазме через 10 минут после веностаза иммунологическим методом",
                "select count (*) from telmed.service_restrictions sr \n" +
                        "join dpc.fed_lab_tests f on sr.labtestid = f.id where f.fullname = 'Тканевый активатор плазминогена, массовая концентрация в бедной тромбоцитами плазме через 10 минут после веностаза иммунологическим методом';",
                "Количество записей по 'Лабораторный тест' не совпадает");

        ClickElement(restrictionsIS.Reset);
        authorizationObject.LoadingTime(10);

        System.out.println("\n7 проверка - Проверяем Создание записи только с обязательными палями");
        ClickElement(restrictionsIS.Add("1"));
        WaitElement(restrictionsIS.SetField("Медицинская организация"));
        driver.findElement(restrictionsIS.SetField("Медицинская организация")).sendKeys("Яцкив");
        ClickElement(authorizationObject.Select("БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\""));

        WaitElement(restrictionsIS.SetField("Назначаемая услуга"));
        driver.findElement(restrictionsIS.SetField("Назначаемая услуга")).sendKeys("A07.16.006");
        ClickElement(authorizationObject.Select("A07.16.006 13С-уреазный дыхательный тест на Helicobacter Pylori"));

        WaitElement(restrictionsIS.SetField("Анатомическая область"));
        driver.findElement(restrictionsIS.SetField("Анатомическая область")).sendKeys("Артерии шеи и головы");
        ClickElement(authorizationObject.Select("Артерии шеи и головы"));

        ClickElement(restrictionsIS.Add("2"));
        Thread.sleep(1500);
        authorizationObject.LoadingTime(10);

        sql.StartConnection("select sr.id, m.namemu, sr.servicecode, a.\"name\", sr.documenttypesoids, sr.diagnosiscode, sr.labtestid, f.fullname, sr.thresholdvalue from telmed.service_restrictions sr\n" +
                "join dpc.mis_sp_mu m on sr.targetmedicalidmu = m.idmu \n" +
                "join dpc.anatom_localation a on sr.anatomicallocationid = a.id \n" +
                "left join dpc.fed_lab_tests f on sr.labtestid = f.id where m.namemu = 'БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"' and a.\"name\" = 'Артерии шеи и головы' and servicecode = 'A07.16.006';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
        }
        Assertions.assertNotNull(sql.value);

        NextButton();
        ClickElement(restrictionsIS.DeleteLast);
        ClickElement(restrictionsIS.YesDelete);
    }

    @Test
    @Order(2)
    @DisplayName("Продолжаем проверять валидацию при создании/обновлении записи")
    public void Access_3338_20() throws InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        restrictionsIS = new RestrictionsIS(driver);

        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(restrictionsIS.RestrictionIS);

        System.out.println("\n8 проверка - Проверяем Создание записи с Диагноз, но без Лабораторный тест - Будет ошибка");
        ClickElement(restrictionsIS.Add("1"));
        WaitElement(restrictionsIS.SetField("Медицинская организация"));
        driver.findElement(restrictionsIS.SetField("Медицинская организация")).sendKeys("Яцкив");
        ClickElement(authorizationObject.Select("БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\""));

        WaitElement(restrictionsIS.SetField("Назначаемая услуга"));
        driver.findElement(restrictionsIS.SetField("Назначаемая услуга")).sendKeys("A07.16.006");
        ClickElement(authorizationObject.Select("A07.16.006 13С-уреазный дыхательный тест на Helicobacter Pylori"));

        WaitElement(restrictionsIS.SetField("Анатомическая область"));
        driver.findElement(restrictionsIS.SetField("Анатомическая область")).sendKeys("Артерии шеи и головы");
        ClickElement(authorizationObject.Select("Артерии шеи и головы"));

        WaitElement(restrictionsIS.SetField("Диагноз"));
        driver.findElement(restrictionsIS.SetField("Диагноз")).sendKeys("F01");
        ClickElement(authorizationObject.Select("F01 Сосудистая деменция"));

        ClickElement(restrictionsIS.Add("2"));
        Thread.sleep(1500);
        WaitElement(restrictionsIS.AddFiled("Лабораторный тест"));

        System.out.println("\n9 проверка - Проверяем Создание записи с Лабораторный тест, но без Пороговое значение теста - Будет ошибка");
        WaitElement(restrictionsIS.SetField("Лабораторный тест"));
        driver.findElement(restrictionsIS.SetField("Лабораторный тест")).sendKeys("Тканевый");
        ClickElement(authorizationObject.Select("Тканевый активатор плазминогена, массовая концентрация хромогенным методом в бедной тромбоцитами плазме"));

        ClickElement(restrictionsIS.Add("2"));
        Thread.sleep(1500);

        WaitElement(restrictionsIS.AddFiled("Пороговое значение теста"));

        System.out.println("\n10 проверка - Проверяем Создание записи без Перечень документов - Ошибки нет");
        WaitElement(restrictionsIS.SetField("Пороговое значение теста"));
        driver.findElement(restrictionsIS.SetField("Пороговое значение теста")).sendKeys("12345");

        ClickElement(restrictionsIS.Add("2"));
        Thread.sleep(1500);
        authorizationObject.LoadingTime(10);
        /** Создастся 1 запись
         * МО - Яцкив
         * Диагноз - F01
         * Услуга - A07.16.006
         * Документы -
         * Анотом Область - Артерии шеи и головы
         * Лаб Тест - Тканевый активатор плазминогена, массовая концентрация хромогенным методом в бедной тромбоцитами плазме
         * Пороговое значение - 12345 */

        sql.StartConnection("select sr.id, m.namemu, sr.servicecode, a.\"name\", sr.documenttypesoids, sr.diagnosiscode, sr.labtestid, f.fullname, sr.thresholdvalue from telmed.service_restrictions sr\n" +
                "join dpc.mis_sp_mu m on sr.targetmedicalidmu = m.idmu \n" +
                "join dpc.anatom_localation a on sr.anatomicallocationid = a.id \n" +
                "left join dpc.fed_lab_tests f on sr.labtestid = f.id where " +
                "m.namemu = 'БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"' and " +
                "a.\"name\" = 'Артерии шеи и головы' and " +
                "servicecode = 'A07.16.006' and " +
                "sr.diagnosiscode = 'F01' and " +
                "f.fullname = 'Тканевый активатор плазминогена, массовая концентрация хромогенным методом в бедной тромбоцитами плазме' and sr.thresholdvalue = '12345';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
        }
        Assertions.assertNotNull(sql.value);

        System.out.println("\n11 проверка - Добавляем Перечень документов прошлой записи и пробуем добавить новую запись по уникальному сочетанию Медицинская организация + Анатомическая область + Перечень документов - Будет ошибка");

        NextButton();
        try {
            ClickElement(restrictionsIS.EditLast);
        } catch (ElementClickInterceptedException e) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,250)");
            ClickElement(restrictionsIS.EditLast);
        }
        WaitElement(restrictionsIS.SetField("Перечень документов"));
        driver.findElement(restrictionsIS.SetField("Перечень документов")).sendKeys("Справка об оплате");
        ClickElement(authorizationObject.Select("Справка об оплате медицинских услуг для предоставления в налоговые органы Российской Федерации (CDA) Редакция 1 100"));
        ClickElement(restrictionsIS.SetField("Диагноз"));

        ClickElement(restrictionsIS.Update);
        Thread.sleep(1500);
        authorizationObject.LoadingTime(10);
        /** Созданная 1 запись
         * МО - Яцкив
         * Диагноз - F01
         * Услуга - A07.16.006
         * Документы - Справка об оплате медицинских услуг для предоставления в налоговые органы Российской Федерации (CDA) Редакция 1 100
         * Анотом Область - Артерии шеи и головы
         * Лаб Тест - Тканевый активатор плазминогена, массовая концентрация хромогенным методом в бедной тромбоцитами плазме
         * Пороговое значение - 12345 */

        /** Добавлем новоую запись*/
        ClickElement(restrictionsIS.Add("1"));
        WaitElement(restrictionsIS.SetField("Медицинская организация"));
        driver.findElement(restrictionsIS.SetField("Медицинская организация")).sendKeys("Яцкив");
        ClickElement(authorizationObject.Select("БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\""));

        //Меняем на другой
        WaitElement(restrictionsIS.SetField("Диагноз"));
        driver.findElement(restrictionsIS.SetField("Диагноз")).sendKeys("A00");
        ClickElement(authorizationObject.Select("A00 Холера"));

        WaitElement(restrictionsIS.SetField("Назначаемая услуга"));
        driver.findElement(restrictionsIS.SetField("Назначаемая услуга")).sendKeys("A07.16.006");
        ClickElement(authorizationObject.Select("A07.16.006 13С-уреазный дыхательный тест на Helicobacter Pylori"));

        //Оставляем как в прошлой добавленной записи
        WaitElement(restrictionsIS.SetField("Анатомическая область"));
        driver.findElement(restrictionsIS.SetField("Анатомическая область")).sendKeys("Артерии шеи и головы");
        ClickElement(authorizationObject.Select("Артерии шеи и головы"));

        //Оставляем как в прошлой добавленной записи
        WaitElement(restrictionsIS.SetField("Перечень документов"));
        driver.findElement(restrictionsIS.SetField("Перечень документов")).sendKeys("Справка об оплате");
        ClickElement(authorizationObject.Select("Справка об оплате медицинских услуг для предоставления в налоговые органы Российской Федерации (CDA) Редакция 1 100"));

        //Меняем на другой
        WaitElement(restrictionsIS.SetField("Лабораторный тест"));
        driver.findElement(restrictionsIS.SetField("Лабораторный тест")).sendKeys("Функция тромбоцитов");
        ClickElement(authorizationObject.Select("A12.05.017 Функция тромбоцитов, стимулированная аденозиндифосфатом и коллагеном в условиях активного кровотока в микрокапилляре"));

        WaitElement(restrictionsIS.SetField("Пороговое значение теста"));
        driver.findElement(restrictionsIS.SetField("Пороговое значение теста")).sendKeys("12345");

        ClickElement(restrictionsIS.Add("2"));
        Thread.sleep(1500);
        WaitElement(restrictionsIS.Error);

        System.out.println("\n12 проверка - Пробуем добавить новую запись по другому уникальному сочетанию Медицинская организация + Диагноз + Лабораторный тест - Будет ошибка");

        ClickElement(restrictionsIS.Add("1"));
        Thread.sleep(1500);
        authorizationObject.LoadingTime(10);

        WaitElement(restrictionsIS.SetField("Медицинская организация"));
        driver.findElement(restrictionsIS.SetField("Медицинская организация")).sendKeys("Яцкив");
        ClickElement(authorizationObject.Select("БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\""));

        //Оставляем как в прошлой добавленной записи
        WaitElement(restrictionsIS.SetField("Диагноз"));
        driver.findElement(restrictionsIS.SetField("Диагноз")).sendKeys("F01");
        ClickElement(authorizationObject.Select("F01 Сосудистая деменция"));

        WaitElement(restrictionsIS.SetField("Назначаемая услуга"));
        driver.findElement(restrictionsIS.SetField("Назначаемая услуга")).sendKeys("A07.16.006");
        ClickElement(authorizationObject.Select("A07.16.006 13С-уреазный дыхательный тест на Helicobacter Pylori"));

        //Меняем на другой
        WaitElement(restrictionsIS.SetField("Анатомическая область"));
        driver.findElement(restrictionsIS.SetField("Анатомическая область")).sendKeys("Грудной отдел аорты");
        ClickElement(authorizationObject.Select("Грудной отдел аорты"));

        //Меняем на другой
        WaitElement(restrictionsIS.SetField("Перечень документов"));
        driver.findElement(restrictionsIS.SetField("Перечень документов")).sendKeys("Справка об отказе");
        ClickElement(authorizationObject.Select("Справка об отказе в направлении на медико-социальную экспертизу (CDA) Редакция 1 102"));

        //Оставляем как в прошлой добавленной записи
        WaitElement(restrictionsIS.SetField("Лабораторный тест"));
        driver.findElement(restrictionsIS.SetField("Лабораторный тест")).sendKeys("Тканевый");
        ClickElement(authorizationObject.Select("Тканевый активатор плазминогена, массовая концентрация хромогенным методом в бедной тромбоцитами плазме"));

        WaitElement(restrictionsIS.SetField("Пороговое значение теста"));
        driver.findElement(restrictionsIS.SetField("Пороговое значение теста")).sendKeys("12345");

        ClickElement(restrictionsIS.Add("2"));
        Thread.sleep(1500);
        WaitElement(restrictionsIS.Error);

        System.out.println("\n13 проверка - Добавляем запись с той же МО, но с другими уникальными значениями. После проверяем уникальное сочетание параметров Медицинская организация + Анатомическая область + Перечень документов через редактирование - Будет ошибка");
        /** 1 запись
         * МО - Яцкив
         * Диагноз - F01
         * Услуга - A07.16.006
         * Документы - Справка об оплате медицинских услуг для предоставления в налоговые органы Российской Федерации (CDA) Редакция 1 100
         * Анотом Область - Артерии шеи и головы
         * Лаб Тест - Тканевый активатор плазминогена, массовая концентрация хромогенным методом в бедной тромбоцитами плазме
         * Пороговое значение - 12345 */

        ClickElement(restrictionsIS.Add("1"));
        Thread.sleep(1500);
        authorizationObject.LoadingTime(10);

        WaitElement(restrictionsIS.SetField("Медицинская организация"));
        driver.findElement(restrictionsIS.SetField("Медицинская организация")).sendKeys("Яцкив");
        ClickElement(authorizationObject.Select("БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\""));

        //Меняем на другой
        WaitElement(restrictionsIS.SetField("Диагноз"));
        driver.findElement(restrictionsIS.SetField("Диагноз")).sendKeys("A01.1");
        ClickElement(authorizationObject.Select("A01.1 Паратиф A"));

        WaitElement(restrictionsIS.SetField("Назначаемая услуга"));
        driver.findElement(restrictionsIS.SetField("Назначаемая услуга")).sendKeys("A07.16.006");
        ClickElement(authorizationObject.Select("A07.16.006 13С-уреазный дыхательный тест на Helicobacter Pylori"));

        //Меняем на другой
        WaitElement(restrictionsIS.SetField("Анатомическая область"));
        driver.findElement(restrictionsIS.SetField("Анатомическая область")).sendKeys("Грудной отдел аорты");
        ClickElement(authorizationObject.Select("Грудной отдел аорты"));

        //Меняем на другой
        WaitElement(restrictionsIS.SetField("Перечень документов"));
        driver.findElement(restrictionsIS.SetField("Перечень документов")).sendKeys("Справка об отказе");
        ClickElement(authorizationObject.Select("Справка об отказе в направлении на медико-социальную экспертизу (CDA) Редакция 1 102"));

        //Меняем на другой
        WaitElement(restrictionsIS.SetField("Лабораторный тест"));
        driver.findElement(restrictionsIS.SetField("Лабораторный тест")).sendKeys("Индекс активированного");
        ClickElement(authorizationObject.Select("A12.05.039 Индекс активированного частичного тромбопластинового времени в бедной тромбоцитами плазме"));

        WaitElement(restrictionsIS.SetField("Пороговое значение теста"));
        driver.findElement(restrictionsIS.SetField("Пороговое значение теста")).sendKeys("12345");

        ClickElement(restrictionsIS.Add("2"));
        Thread.sleep(1500);
        authorizationObject.LoadingTime(10);

        /** 2 запись
         * МО - Яцкив
         * Диагноз - A01.1
         * Услуга - A07.16.006
         * Документы - Справка об отказе в направлении на медико-социальную экспертизу (CDA) Редакция 1 102
         * Анотом Область - Грудной отдел аорты
         * Лаб Тест - A12.05.039 Индекс активированного частичного тромбопластинового времени в бедной тромбоцитами плазме
         * Пороговое значение - 12345 */

//       // Редактируем по заявке, должны исправить появление ошибки (3388)
//        NextButton();
//       // Оставляем как в прошлой добавленной записи
//        ClickElement(restrictionsIS.EditLast);
//        WaitElement(restrictionsIS.SetField("Анатомическая область"));
//        driver.findElement(restrictionsIS.SetField("Анатомическая область")).sendKeys("Артерии шеи и головы");
//        ClickElement(authorizationObject.Select("Артерии шеи и головы"));
//
//        //Оставляем как в прошлой добавленной записи
//        WaitElement(restrictionsIS.SetField("Перечень документов"));
//        driver.findElement(restrictionsIS.SetField("Перечень документов")).sendKeys("Справка об оплате");
//        ClickElement(authorizationObject.Select("Справка об оплате медицинских услуг для предоставления в налоговые органы Российской Федерации (CDA) Редакция 1 100"));
//
//        ClickElement(restrictionsIS.Update);
//        Thread.sleep(1500);
//        WaitElement(restrictionsIS.Error);

        System.out.println("\n14 проверка - Проверяем уникальное сочетание параметров Медицинская организация + Диагноз + Лабораторный тест - Будет ошибка");

        /** 1 запись
         * МО - Яцкив
         * Диагноз - F01
         * Услуга - A07.16.006
         * Документы - Справка об оплате медицинских услуг для предоставления в налоговые органы Российской Федерации (CDA) Редакция 1 100
         * Анотом Область - Артерии шеи и головы
         * Лаб Тест - Тканевый активатор плазминогена, массовая концентрация хромогенным методом в бедной тромбоцитами плазме
         * Пороговое значение - 12345 */

        NextButton();
        //Оставляем как в прошлой добавленной записи
        ClickElement(restrictionsIS.EditLast);
        WaitElement(restrictionsIS.SetField("Диагноз"));
        driver.findElement(restrictionsIS.SetField("Диагноз")).sendKeys("F01");
        ClickElement(authorizationObject.Select("F01 Сосудистая деменция"));

        //Оставляем как в прошлой добавленной записи
        WaitElement(restrictionsIS.SetField("Лабораторный тест"));
        driver.findElement(restrictionsIS.SetField("Лабораторный тест")).sendKeys("Тканевый активатор ");
        ClickElement(authorizationObject.Select("Тканевый активатор плазминогена, массовая концентрация хромогенным методом в бедной тромбоцитами плазме"));

        ClickElement(restrictionsIS.Update);
        Thread.sleep(1500);
        WaitElement(restrictionsIS.Error);

        // Удаляем последнии 2 записи
        NextButton();
        ClickElement(restrictionsIS.DeleteLast);
        ClickElement(restrictionsIS.YesDelete);
        Thread.sleep(1500);
        authorizationObject.LoadingTime(10);

        NextButton();
        ClickElement(restrictionsIS.DeleteLast);
        ClickElement(restrictionsIS.YesDelete);
        Thread.sleep(1500);
        authorizationObject.LoadingTime(10);
    }

    @Step("Метод для переключения до последней страницы")
    public void NextButton() throws InterruptedException {
        int number = 0;
        while (!isElementNotVisible(restrictionsIS.NextDisable) & number < 5) {
            number++;
            ClickElement(restrictionsIS.Next);
        }
    }

    @Step("Метод для выбора фильтра в Регистр наблюдений и равнения с БД")
    public void SetFilter(String filter, String input, String selectFilter, String SQL, String mistake) throws SQLException, InterruptedException {
        ClickElement(restrictionsIS.Reset);
        authorizationObject.LoadingTime(10);
        ClickElement(restrictionsIS.GetField(filter));
        inputWord(driver.findElement(restrictionsIS.GetField(filter)), input);
        authorizationObject.LoadingTime(10);
        ClickElement(authorizationObject.Select(selectFilter));
        ClickElement(restrictionsIS.Search);
        authorizationObject.LoadingTime(10);
        Thread.sleep(2000);

        GetSql(SQL, mistake);
    }

    @Step("Метод для сравнения значений в Регистр наблюдений с БД")
    public void GetSql(String SQL, String str) throws SQLException, InterruptedException {
        authorizationObject.LoadingTime(10);
        Thread.sleep(2000);
        WaitElement(restrictionsIS.AllCount);
        String Count = driver.findElement(restrictionsIS.AllCount).getText().substring(6);

        sql.StartConnection(SQL);
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }
        Assertions.assertEquals(Count, sql.value, str);
    }
}