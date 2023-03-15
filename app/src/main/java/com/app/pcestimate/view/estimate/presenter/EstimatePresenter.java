package com.app.pcestimate.view.estimate.presenter;

import android.util.Log;
import com.app.pcestimate.datamodel.PcDataModel;
import com.app.pcestimate.datamodel.PriceDataModel;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class EstimatePresenter {

    private final String baseUrl = "https://shop.danawa.com/mobile/?controller=estimateRecommend&methods=viewResult&path=1&priceRange={priceIndex}&priceType=1";
    private final String replaceString = "{priceIndex}";

    // 파싱 데이터
    private final String tagGoodsList = "content__list--goods";
//    private final String tagTotalPrice = "price_sum";

    public void getEstimateResult(int index, IEstimateResultCallback callback){

        ArrayList<PcDataModel> result = new ArrayList<>();

        Thread t = new Thread(() -> {

            String url = baseUrl.replace(replaceString, PriceDataModel.priceIndexList[index]);
            Connection connection = Jsoup.connect(url);

            try {
                Document doc = connection
                        .maxBodySize(0)
                        .timeout(10000)
                        .get();

                Elements classList = doc.getElementsByClass(tagGoodsList);

                if(classList.size() > 0){

                    // 해당 클래스가 5개가 존재함, 0번째 데이터만 사용
                    Elements liList = classList.get(0).getElementsByTag("li");

                    for(Element item : liList){

                        String category = item.getElementsByClass("cate").text();
                        String title = item.getElementsByClass("title").text();
                        String price = item.getElementsByClass("num").text();

                        Element imageTag = item.getElementsByTag("img").get(0);
                        String imageUrl = imageTag.attr("src");

                        PcDataModel addItem = new PcDataModel(category, "https:" + imageUrl, title, price);
                        result.add(addItem);
                    }

                    // total price값이 0이 나옴 -> 다른 클래스에서 가져오도록 변경
//                    Elements totalPrices = doc.getElementsByClass(tagTotalPrice);
//                    String totalPrice = totalPrices.text();

                    Elements totalPrices = doc.getElementsByClass("swiper-slide");
                    String totalPrice = totalPrices.get(0).getElementsByClass("price_wrap").text();

                    callback.onResult(result, totalPrice);

                }else {
                    callback.onError("데이터가 없습니다.");
                }

            } catch (IOException e) {
                // url 확인 필요
                callback.onError("접근 에러");
            }

        });
        t.start();

    }

    public interface IEstimateResultCallback {
        void onResult(ArrayList<PcDataModel> list, String totalPrice);
        void onError(String msg);
    }
}
