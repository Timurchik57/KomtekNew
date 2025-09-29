package Base;

import lombok.SneakyThrows;
import org.apache.hc.core5.util.TextUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.IOException;

import static Base.BaseTest.InputClassFile;
import static Base.BaseAPI.ReadPropFile;
import static Base.SQL.session;
import static Base.SQL.sshHost;

public class TestListenerApi implements TestWatcher {
    XML xml;
    public String name;

    @SneakyThrows
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        xml = new XML();
        System.out.println(XML.Type);

        try {
            // После каждого теста, сохраняем название его класса, чтобы в дальнейшем перезапустить его
            String str = ReadPropFile("IfCountListner");
            System.out.println(str);
            if (ReadPropFile("IfCountListner").contains("api") == true) {
                InputClassFile();
            }
            xml.ReplacementWordInFileBack(XML.Type);

            // Закрываем SSH-сессию
            if (!TextUtils.isEmpty(sshHost)) {
                session.disconnect();
            }
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException("Ошибочка - " + e.getMessage());
        }
    }

    @SneakyThrows
    @Override
    public void testSuccessful(ExtensionContext context) {
        xml = new XML();
        System.out.println(XML.Type);
        try {

            xml.ReplacementWordInFileBack(XML.Type);

            // Закрываем SSH-сессию
            if (!TextUtils.isEmpty(sshHost)) {
                session.disconnect();
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
