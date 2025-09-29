package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationScheduleRemote;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Расписание_консультаций")
@Tag("Удалённая_консультация")
public class Access_2274Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    ConsultationScheduleRemote consultationSR;

    @Issue(value = "TEL-2274")
    @Link(name = "ТМС-1899", url = "https://team-1okm.testit.software/projects/5/tests/1899?isolatedSection=767caae3-1d53-4e9c-a277-e3122b6d6370")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Валидации при создании Расписания консультаций")
    @Description("Переходим в Расписание консультаций - при создании консультации, проверяем валидацию")
    public void Access_2274() throws InterruptedException{

        authorizationObject = new AuthorizationObject(driver);
        consultationSR = new ConsultationScheduleRemote(driver);

        System.out.println("Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Открываем Расписание консультаций - График консультаций");
        ClickElement(consultationSR.ConsultationScheduleremote);
        ClickElement(consultationSR.ConsultationSchedule);

        System.out.println("Создаём новый график консультаций");
        ClickElement(consultationSR.ConsultationScheduleAdd);
        ClickElement(consultationSR.Select("Консультант"));
        ClickElement(authorizationObject.Select("Зотин Андрей Владимирович"));
        ClickElement(consultationSR.Select("Профиль"));
        ClickElement(authorizationObject.Select("детской урологии-андрологии"));
        SelectClickMethod(consultationSR.Select("Цель консультаций"), authorizationObject.Select("Подозрение на COVID-19"));
        ClickElement(consultationSR.DateStart);
        if(isElementNotVisible(consultationSR.Tomorrow)) {
            ClickElement(consultationSR.Tomorrow);
        } else {
            ClickElement(consultationSR.TomorrowWeek);
        }
        if (!isElementNotVisible(consultationSR.UsedDate)) {
            ClickElement(consultationSR.UsedDateToday);
        } else {
            ClickElement(consultationSR.UsedDate);
        }
        SelectClickMethod(consultationSR.Select("Время начала"), consultationSR.Time("6:00"));
        SelectClickMethod(consultationSR.Select("Время окончания"), consultationSR.Time("21:00"));
        inputWord(driver.findElement(consultationSR.Select("Отображение расписания")), "00");

        System.out.println("\n 1 проверка - Количество дней не может быть меньше 1");
        WaitElement(consultationSR.ErrorDay);
        inputWord(driver.findElement(consultationSR.Select("Отображение расписания")), "");
        inputWord(driver.findElement(consultationSR.Select("Отображение расписания")), "70");

        for (int i = 1; i < 8; i++) {
            ClickElement(consultationSR.DayActivity("" + i + ""));
        }

        ClickElement(consultationSR.Exception);
        ClickElement(consultationSR.SearchDate);
        if (!isElementNotVisible(consultationSR.SearchDateTrue)) {
            ClickElement(consultationSR.SearchDateTrueToday);
        } else {
            ClickElement(consultationSR.SearchDateTrue);
        }
        ClickElement(consultationSR.Add);
        isElementVisibleTime(consultationSR.Error(""), 30);
    }
}