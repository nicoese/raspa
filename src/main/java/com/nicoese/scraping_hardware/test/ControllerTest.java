package com.nicoese.scraping_hardware.test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@RestController
public class ControllerTest {

    //    private final List<SiteOptions> siteOptions = List.of(
    private final List<SiteOptions> siteOptions = List.of(
            new SiteOptions(
                    "https://compragamer.com/",
                    "?seccion=3&criterio=",
                    "card-product",
                    "h1 span",
                    "a span",
                    true,
                    List.of("--headless"),
                    true,
                    "Compragamer"
            ),
            new SiteOptions(
                    "https://www.maximus.com.ar/",
                    "Productos/OR=1/maximus.aspx/BUS=",
                    "col-md-prod",
                    "div.price",
                    "div.description h4",
                    true,
                    List.of("--headless"),
                    false,
                    "Maximus"
            ),
            new SiteOptions(
                    "https://listado.mercadolibre.com.ar/",
                    "computacion/componentes-pc-",
                    "andes-card",
                    "span.price-tag",
                    "h2.shops__item-title",
                    true,
                    List.of("--headless"),
                    true,
                    "Mercado Libre"
            ),
            new SiteOptions(
                    "https://www.gezatek.com.ar/",
                    "tienda/?busqueda=",
                    "div.w-box.product",
                    "h3.price",
                    "h2 a.click",
                    false,
                    List.of("--headless"),
                    true,
                    "Gezatek"
            ),
            new SiteOptions(
                    "https://www.venex.com.ar/",
                    "resultado-busqueda.htm?keywords=",
                    "product-box",
                    "span.current-price",
                    "h3.product-box-title",
                    true,
                    List.of("--headless"),
                    true,
                    "Venex"
            ),
            new SiteOptions(
                    "https://www.fullh4rd.com.ar/",
                    "cat/search/",
                    "div.item.product-list",
                    "div.price",
                    "div.info h3",
                    false,
                    List.of("--headless"),
                    true,
                    "Full H4rd"
            )

    );

    @GetMapping("/")
    public ResponseEntity<HashMap<String, String>> test() {
        HashMap<String, String> map = new HashMap<>();
        map.put("message", "hola amiguero");
        map.put("los mapas", "son la mejor opcion para mapear objetos de java a js");
        WebDriverManager.chromedriver().setup();
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(map);
    }

    @GetMapping("/scraping")
    public ResponseEntity<String> scrap() {


        try {
//
            System.setProperty("webdriver.gecko.driver", "C:/Users/Nico/Documents/nicoese/scraping_hardware/geckodriver.exe");
            FirefoxOptions op = new FirefoxOptions();
            op.setHeadless(true);


            WebDriver driver = new FirefoxDriver(op);


            driver.get("https://www.maximus.com.ar/");
//            String title = driver.getCurrentUrl();
            List<WebElement> elements = driver.findElements(By.cssSelector("a"));


            driver.close();
            driver.quit();

            return ResponseEntity.ok(elements.toString());
        } catch (Exception e) {
            System.out.println("--------------------------------------------");
            System.out.println(e.getMessage());
            System.out.println("--------------------------------------------");
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(e.getMessage());
        }
    }

    @GetMapping("/search")

    public ResponseEntity<List<Item>> scraptest(
            @RequestParam("query") String queryParam,
            @RequestParam("items") Integer itemsQuantity
            ) {


        long start = System.nanoTime();


        final List<List<Item>> webSiteItemsContainer = siteOptions.stream()
                .parallel()
                .map(siteOptionsElement -> {
                    //selenium driver bootstrap:

                    List<WebElement> webElements = new ArrayList<>();
//                    WebDriverManager.chromedriver().setup();
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments(siteOptionsElement.getChromeOptions());

                    if (siteOptionsElement.getIsPageLoadStrategyNone()) {
                        options.setPageLoadStrategy(PageLoadStrategy.NONE);
                    }
                    options.setImplicitWaitTimeout(Duration.ofMillis(15000));
                    options.setPageLoadTimeout(Duration.ofMillis(15000));
                    options.setScriptTimeout(Duration.ofMillis(15000));
                    options.addArguments("--no-sandbox");
                    options.addArguments("--disable-infobars");
                    options.addArguments("--disable-extensions");
//                    options.addArguments("--disable-gpu");
//                    ChromeDriver driver = new ChromeDriver(options);


                    WebDriver driver = null;
                    try {
//                        driver = new RemoteWebDriver(new URL("http://localhost:4444"), options);
                        driver = new RemoteWebDriver(new URL("http://172.17.0.2:4444"), options);
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }


                    try {
                        driver.manage().timeouts().pageLoadTimeout(Duration.ofMillis(15000));
                        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(15000));

                        driver.get(siteOptionsElement.getBaseUrl() + siteOptionsElement.getQuery() + queryParam);

                        //mapping web elements from website (item containers)

                        webElements = driver.findElements(siteOptionsElement.getBy()).stream().limit(itemsQuantity).collect(toList());


                        System.out.println("----------------------------------------------");
                        webElements.forEach(e -> System.out.println(e.getText()));
                        System.out.println("----------------------------------------------");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    List<Item> items = new ArrayList<>();

                    //script para formatear el precio
//                    driver.executeScript(
//                            "var price = document.querySelector('h3.price');" +
//                                    "if(price) price.removeChild(price.children[0]);"
//                    );

                    webElements.forEach(elem -> {
                        try {
                            Item item = new Item(
                                    elem.findElement(By.tagName("a")).getAttribute("href"),
                                    elem.findElement(By.tagName("img")).getAttribute("src"),
                                    elem.findElement(By.cssSelector(siteOptionsElement.getSelectorPrice())).getText(),
                                    elem.findElement(By.cssSelector(siteOptionsElement.getSelectorName())).getText(),
                                    siteOptionsElement.getSiteName()
                            );
                            items.add(item);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    });
                    driver.close();
                    driver.quit();
                    return items;
                })
                .collect(toList());

        long end = System.nanoTime();

        List<Item> lista = new ArrayList<>();

        //method reference
        webSiteItemsContainer.forEach(lista::addAll);

        //instead of
//        webSiteItemsContainer.forEach(list -> {
//            lista.addAll(list);
//        });

        System.out.println(webSiteItemsContainer.size());
        System.out.println(lista.size());
        System.out.println(Duration.ofNanos(end - start).toMillis());


        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(lista);
    }

}
