package com.nicoese.scraping_hardware.test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.tomcat.util.modeler.Registry;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.json.Json;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RestController
public class ControllerTest {

    private final List<SiteOptions> siteOptions = List.of(
            new SiteOptions(
                    "https://compragamer.com/",
                    "?seccion=3&criterio=",
                    "card-product",
                    "h1 span",
                    "a span",
                    true,
                    List.of("--headless")
            ),
            new SiteOptions(
                    "https://www.maximus.com.ar/",
                    "Productos/OR=1/maximus.aspx/BUS=",
                    "col-md-prod",
                    "div.price",
                    "div.description h4",
                    true,
                    List.of("--headless")
            ),
            new SiteOptions(
                    "https://www.gezatek.com.ar/",
                    "tienda/?busqueda=",
                    "div.w-box.product",
                    "h3.price",
                    "h2 a.click",
                    false,
                    List.of("--headless")
            ),
            new SiteOptions(
                    "https://www.venex.com.ar/",
                    "resultado-busqueda.htm?keywords=",
                    "product-box",
                    "span.current-price",
                    "h3.product-box-title",
                    true,
                    List.of("--headless")
            ),
            new SiteOptions(
                    "https://www.fullh4rd.com.ar/",
                    "cat/search/",
                    "div.item.product-list",
                    "div.price",
                    "div.info h3",
                    false,
                    List.of("--headless")
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
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            ChromeDriver driver = new ChromeDriver(options);
            driver.get("https://nicoese.com");


            return ResponseEntity.ok("weno");
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(e.getMessage());
        }
    }

    @GetMapping("/scraptest")

    public ResponseEntity<List<Item>> scraptest(@RequestParam("query") String queryParam) {


        long start = System.nanoTime();


        final List<List<Item>> webSiteItemsContainer = siteOptions.stream()
                .parallel()
                .map(siteOptionsElement -> {
                    //selenium driver bootstrap:

                    List<WebElement> webElements = new ArrayList<>();
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments(siteOptionsElement.getChromeOptions());
                    options.setPageLoadStrategy(PageLoadStrategy.NONE);
                    ChromeDriver driver = new ChromeDriver(options);

                    try {
                        driver.manage().timeouts().pageLoadTimeout(Duration.ofMillis(15000));
                        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(15000));
                        driver.get(siteOptionsElement.getBaseUrl() + siteOptionsElement.getQuery() + queryParam);

                        //mapping web elements from website (item containers)

                        webElements = driver.findElements(siteOptionsElement.getBy()).stream().limit(3).collect(toList());
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
                                    elem.findElement(By.cssSelector(siteOptionsElement.getSelectorName())).getText()
                            );
                            items.add(item);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    });
                    try {
                        driver.wait();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
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
