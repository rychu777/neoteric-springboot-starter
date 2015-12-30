import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class LocaleTest {

    private static final Logger LOG = LoggerFactory.getLogger(LocaleTest.class);

    @Test
    public void testName() throws Exception {

        Locale locale = Locale.forLanguageTag("en-EN");
        LOG.error("locale {}", locale.toLanguageTag());


        LOG.error("locale {}", locale.getUnicodeLocaleKeys());

    }
}
