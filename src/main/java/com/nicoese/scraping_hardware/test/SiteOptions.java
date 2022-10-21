package com.nicoese.scraping_hardware.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;

import java.util.List;

@Data
public class SiteOptions {
    private String baseUrl;
    private String query;
    private String param;
    private String selector;
    private String selectorPrice;
    private String selectorName;
    private Boolean isClassName;
    private By by;
    private List<String> chromeOptions;



    public SiteOptions(String baseUrl,
                       String query,
                       String selector,
                       String selectorPrice,
                       String selectorName,
                       Boolean isClassName,
                       List<String> chromeOptions) {
        this.baseUrl = baseUrl;
        this.query = query;
        this.selector = selector;
        this.selectorPrice = selectorPrice;
        this.selectorName = selectorName;
        this.chromeOptions = chromeOptions;
        this.by = isClassName ? By.className(selector) : By.cssSelector(selector);
    }

//    public String getParam() {
//        return param;
//    }
//
//    public void setParam(String param) {
//        this.param = param;
//    }
//
//    public void setClassName(Boolean className) {
//        isClassName = className;
//    }
//
//    public void setBy(By by) {
//        this.by = by;
//    }
//
//    public Boolean getClassName() {
//        return isClassName;
//    }
//
//    public By getBy() {
//        return by;
//    }
//
//    public String getBaseUrl() {
//        return baseUrl;
//    }
//
//    public void setBaseUrl(String baseUrl) {
//        this.baseUrl = baseUrl;
//    }
//
//    public String getQuery() {
//        return query;
//    }
//
//    public void setQuery(String query) {
//        this.query = query;
//    }
//
//    public String getSelector() {
//        return selector;
//    }
//
//    public void setSelector(String selector) {
//        this.selector = selector;
//    }
//
//    public List<String> getChromeOptions() {
//        return chromeOptions;
//    }
//
//    public void setChromeOptions(List<String> chromeOptions) {
//        this.chromeOptions = chromeOptions;
//    }
}
