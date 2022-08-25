package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $x("//input[@placeholder='Город']").setValue(validUser.getCity());
        $x("//input[@placeholder='Дата встречи']").sendKeys(Keys.CONTROL + "a");
        $x("//input[@placeholder='Дата встречи']").sendKeys(Keys.BACK_SPACE);
        $x("//input[@placeholder='Дата встречи']").setValue(firstMeetingDate);
        $x("//input[@name='name']").setValue(validUser.getName());
        $x("//input[@name='phone']").setValue(validUser.getPhone());
        $x("//span[@class='checkbox__text']").click();
        $x("//*[text()='Запланировать']").click();
        $(withText("Успешно!")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        $x("//div[@class='notification__content']").shouldHave(text("Встреча успешно запланирована на "
                + firstMeetingDate));
        $x("//input[@placeholder='Дата встречи']").sendKeys(Keys.CONTROL + "a");
        $x("//input[@placeholder='Дата встречи']").sendKeys(Keys.BACK_SPACE);
        $x("//input[@placeholder='Дата встречи']").setValue(secondMeetingDate);
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id=replan-notification]").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id=replan-notification] [class = notification__content]")
                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $x("//*[text()='Перепланировать']").click();
        $("[data-test-id=success-notification] [class = notification__content]")
                .shouldHave(text("Встреча успешно запланирована на " + secondMeetingDate));
    }
}
