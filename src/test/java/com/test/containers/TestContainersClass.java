package com.test.containers;


import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.DefaultRecordingFileFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Condition.appears;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static java.util.stream.Collectors.toList;


public class TestContainersClass {


    static BrowserWebDriverContainer<?> chrome =  new BrowserWebDriverContainer()
            .withCapabilities(new ChromeOptions())
            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, new File("./target/"))
            .withRecordingFileFactory(new DefaultRecordingFileFactory());

    @BeforeClass
    public static void setUp() {
        chrome.start();
        //RemoteWebDriver driver = chrome.getWebDriver();
        RemoteWebDriver driver = new RemoteWebDriver(chrome.getSeleniumAddress(), new ChromeOptions());
        WebDriverRunner.setWebDriver(driver);
    }

    @Test
    public void test() {
        Selenide.open("https://www.google.com/");
        $(By.name("q")).val("TestContainers");
        $(By.name("q")).pressEnter();


        $(By.xpath("//div[@data-base-lens-url='https://lens.google.com']")).should(appears, Duration.ofSeconds(9));


        List<SelenideElement> collectH3 = $$(By.xpath("//h3")).stream()
                .limit(1)
                .collect(toList());

        Assert.assertEquals(collectH3.get(0).getText(), "Testcontainers");


        List<SelenideElement> collectH3Urls = $$(By.xpath("//div/cite")).stream()
                .limit(1)
                .collect(toList());

        Assert.assertEquals(collectH3Urls.get(0).getText(), "https://testcontainers.com");

    }


    @AfterClass
    public static void tearDown() {
        if (chrome.isRunning()) {
            chrome.stop();
        }
    }
}
