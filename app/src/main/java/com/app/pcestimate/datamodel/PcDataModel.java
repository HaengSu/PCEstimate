package com.app.pcestimate.datamodel;

/**
 * 견적 결과 데이터모델
 */
public class PcDataModel {

    public String category;
    public String thumb;
    public String title;
    public String price;

    public PcDataModel(String category, String thumb, String title, String price) {
        this.category = category;
        this.thumb = thumb;
        this.title = title;
        this.price = price;
    }

    @Override
    public String toString() {
        return "PcDataModel{" +
                "category='" + category + '\'' +
                ", thumb='" + thumb + '\'' +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
