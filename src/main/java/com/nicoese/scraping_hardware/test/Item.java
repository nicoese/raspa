package com.nicoese.scraping_hardware.test;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Item {

    private String href;
    private String img;
    private String price;
    private String name;
    private String site;

    public Item(String href,
                String img,
                String price,
                String name,
                String site) {
        this.href = href;
        this.img = img;
        this.price = price;
        this.name = name;
        this.site = site;
    }
}
