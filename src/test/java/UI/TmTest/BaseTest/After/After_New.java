package UI.TmTest.BaseTest.After;

import Base.TestListener;
import Base.TestListenerApi;
import UI.TmTest.BaseTest.BaseNew;
import UI.TmTest.BaseTest.Tests.Access_1100_NewTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Проверка бд после тестов")
@Tag("Новый_контур")
public class After_New extends BaseNew {


    @Order(1)
    @Issue(value = "TEL-1100")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка отправки документа в РЭМД с отправкой только в ВИМИС")
    @Description("После прохождения тестов смотрим, что документ, который только в ВИМИС не добавился, а документ в ВИМИС и РЭМД добавился в таблицы vimis.remd_onko_sent_result/ vimis.remd_cvd_sent_result/ vimis.remd_akineo_sent_result/ vimis.remd_prevention_sent_result")
    public void AfterTests1100 () throws SQLException, IOException {
        Access_1100_NewTest access_1100Test = new Access_1100_NewTest();
        access_1100Test.Access_1100AfterMethod("value_1100_vmcl_1", "vimis.remd_onko_sent_result", false);
        access_1100Test.Access_1100AfterMethod("value_1100_vmcl_2", "vimis.remd_prevention_sent_result", false);
        access_1100Test.Access_1100AfterMethod("value_1100_vmcl_3", "vimis.remd_akineo_sent_result", false);
        access_1100Test.Access_1100AfterMethod("value_1100_vmcl_4", "vimis.remd_cvd_sent_result", false);
        access_1100Test.Access_1100AfterMethod("value_1100_vmcl_5", "vimis.remd_infection_sent_result", false);

        access_1100Test.Access_1100AfterMethod("value_1100_remd_1", "vimis.remd_onko_sent_result", true);
        access_1100Test.Access_1100AfterMethod("value_1100_remd_2", "vimis.remd_prevention_sent_result", true);
        access_1100Test.Access_1100AfterMethod("value_1100_remd_3", "vimis.remd_akineo_sent_result", true);
        access_1100Test.Access_1100AfterMethod("value_1100_remd_4", "vimis.remd_cvd_sent_result", true);
        access_1100Test.Access_1100AfterMethod("value_1100_remd_5", "vimis.remd_infection_sent_result", true);
        access_1100Test.Access_1100AfterMethod("value_1100_remd_99", "vimis.remd_sent_result", true);
    }
}
