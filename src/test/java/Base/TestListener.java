package Base;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.logging.LogType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static Base.BaseTest.*;
import static Base.BaseAPI.*;

public class TestListener implements TestWatcher {

    @SneakyThrows
    @Override
    public void testFailed (ExtensionContext context, Throwable cause) {
        try {
            Allure.getLifecycle().addAttachment("Скриншот на месте падания теста", "image/png", "png",
                    ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
//            Allure.addAttachment("Логи после падения теста: ",
//                    String.valueOf(driver.manage().logs().get(LogType.BROWSER).getAll()));
            WebDriverManager.chromedriver().quit();
            if (!OpenWindow) {
                driver.quit();
            }
            // После каждого теста, сохраняем название его класса, чтобы в дальнейшем перезапустить его

            if (ReadPropFile("IfCountListner").contains("web") == true) {
                InputClassFile();
            }

            // Сохраняем лог консоли в файл
            OutputStream fileStream = new FileOutputStream("src/test/resources/ignored/console.txt");
            buffer.writeTo(fileStream);

            Allure.addAttachment("Данные из консоли: ",
                    new String(Files.readAllBytes(Paths.get("src/test/resources/ignored/console.txt"))));

            //Если тест упал то пробуем запустить его ещё раз
            if (RebaseTest) {
                if (ReadPropFile(ReadPropFile("methodName")).equals("0")) {
                    InputPropFile(ReadPropFile("methodName"), "1");
                    RebaseTests();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    @Override
    public void testSuccessful (ExtensionContext context) {
        try {
            Allure.getLifecycle().addAttachment("Скриншот после успешного прохождения теста", "image/png", "png",
                    ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
//            Allure.addAttachment("Логи после успешного прохождения теста: ",
//                    String.valueOf(driver.manage().logs().get(LogType.BROWSER).getAll()));
            WebDriverManager.chromedriver().quit();
            driver.quit();

            // Сохраняем лог консоли в файл
            OutputStream fileStream = new FileOutputStream("src/test/resources/ignored/console.txt");
            buffer.writeTo(fileStream);

            Allure.addAttachment("Данные из консоли: ",
                    new String(Files.readAllBytes(Paths.get("src/test/resources/ignored/console.txt"))));

            if (RebaseTest) {
                InputPropFile("CountRebaseTest", "0");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
