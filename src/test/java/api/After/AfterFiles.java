package api.After;

import Base.TestListener;
import Base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.apache.hc.core5.util.TextUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Запуск упавших тестов чере файл")
public class AfterFiles extends BaseTest {

    @Test
    @DisplayName("Запускаем тесты, которые упали")
    public void TestFiled() throws IOException {
        String str = "";
        if (TextUtils.isEmpty(remote_url_chrome)) {
            str = "ignored/FiledTests.bat";
        } else {
            str = "ignored/FiledTests.sh";
        }
        String text1 = new String(Files.readAllBytes(Paths.get("src/test/resources/"+str+"")));
        System.out.println("Данные из файла с упавшими тестами - " + text1);
        Runtime.getRuntime().exec("src/test/resources/" + str + " /C start");
    }
}