package ru.netology.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class SubmitFormTest {

    private String dateGenerate(int days, String pattern) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    public void shouldFillTheFormWithCorrectData() {
        String planningDate = dateGenerate(3, "dd.MM.yyyy");

        Selenide.open("http://localhost:9999");
        $("[placeholder='Город'").setValue("Владимир");
        $("[data-test-id='date'] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[name='name']").setValue("Петров Николай");
        $("[name='phone']").setValue("+71234567898");
        $("[data-test-id='agreement']").click();
        $$("button").findBy(Condition.text("Забронировать")).click();

        $(Selectors.withText("Успешно")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='notification']").shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate));
    }

    @Test
    public void shouldFillTheFormWithCorrectData2() {
        String planningDate = dateGenerate(7, "dd.MM.yyyy");

        Selenide.open("http://localhost:9999");

        $("[data-test-id='city'] input").setValue("Вла");
        $$(".menu-item__control").findBy(Condition.text("Владимир")).click();

        $("[data-test-id='date'] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        if (!dateGenerate(3, "MM").equals(dateGenerate(7, "MM"))) $("[data-step='1']").click();
        $$(".calendar__day").findBy(Condition.text(dateGenerate(7,"d"))).click();

        $("[name='name']").setValue("Петров Николай");
        $("[name='phone']").setValue("+71234567898");
        $("[data-test-id='agreement']").click();
        $$("button").findBy(Condition.text("Забронировать")).click();

        $(".notification__content").shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate));
    }
}
