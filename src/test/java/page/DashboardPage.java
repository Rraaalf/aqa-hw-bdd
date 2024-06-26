package page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import data.DataHelper;
import lombok.val;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private final ElementsCollection cards = $$(".list__item div");

    public DashboardPage() {
        SelenideElement heading = $("[data-test-id=dashboard]");
        heading.shouldBe(visible);
    }

    public TransferPage transferToCard(DataHelper.CardInfo cardInfo) {
        cards.findBy(text(cardInfo.getCardNumber().substring(15, 19))).$("button").click();
        return new TransferPage();
    }

    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        val number = cards.findBy(text(cardInfo.getCardNumber().substring(15, 19))).getText();
        return extractBalance(number);
    }

    private int extractBalance(String text) {
        String balanceStart = "баланс: ";
        val start = text.indexOf(balanceStart);
        String balanceFinish = " р.";
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }
}
