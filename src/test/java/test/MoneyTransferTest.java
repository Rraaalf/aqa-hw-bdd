package test;

import com.codeborne.selenide.Configuration;
import data.DataHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import page.LoginPage;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static data.DataHelper.getFirstCardInfo;
import static data.DataHelper.getSecondCardInfo;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("password_manager_enable", false);
        options.setExperimentalOption("prefs", prefs);
        Configuration.browserCapabilities = options;
    }

    @Test
    void shouldTransferMoneyFromFirstToSecondCard() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        int amount = 10000;
        var expectedBalance1 = dashboardPage.getCardBalance(firstCardInfo) - amount;
        var expectedBalance2 = dashboardPage.getCardBalance(secondCardInfo) + amount;
        var transferPage = dashboardPage.transferToCard(secondCardInfo);
        dashboardPage = transferPage.addMoneyToCard(String.valueOf(amount), firstCardInfo);
        var actualBalance1 = dashboardPage.getCardBalance(firstCardInfo);
        var actualBalance2 = dashboardPage.getCardBalance(secondCardInfo);
        assertEquals(expectedBalance1, actualBalance1);
        assertEquals(expectedBalance2, actualBalance2);
        closeWebDriver();
    }

    @Test
    void shouldTransferMoneyFromSecondToFirstCard() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var secondCardInfo = getSecondCardInfo();
        var firstCardInfo = getFirstCardInfo();
        int amount = 10000;
        var expectedBalance2 = dashboardPage.getCardBalance(secondCardInfo) + amount;
        var expectedBalance1 = dashboardPage.getCardBalance(firstCardInfo) - amount;
        var transferPage = dashboardPage.transferToCard(firstCardInfo);
        dashboardPage = transferPage.addMoneyToCard(String.valueOf(amount), secondCardInfo);
        var actualBalance1 = dashboardPage.getCardBalance(secondCardInfo);
        var actualBalance2 = dashboardPage.getCardBalance(firstCardInfo);
        assertEquals(expectedBalance1, actualBalance1);
        assertEquals(expectedBalance2, actualBalance2);
        closeWebDriver();
    }
}
