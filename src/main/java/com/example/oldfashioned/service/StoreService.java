package com.example.oldfashioned.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.oldfashioned.entity.Store;
import com.example.oldfashioned.form.PostRegisterForm;
import com.example.oldfashioned.repository.StoreRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.errors.RequestDeniedException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import jakarta.transaction.Transactional;

@Service
public class StoreService {
	StoreRepository storeRepository;
	@Value("${google.maps.api.key}")
	private String apiKey;
	
	public StoreService(StoreRepository storeRepository) {
		this.storeRepository = storeRepository;
	}
	
	// 緯度と経度を引数として受け取りその座標に対応する住所を返す
	public String getAddressFromCoordinates(double lat, double lon) throws RequestDeniedException {
		
		// LatLngオブジェクトの作成
		LatLng latlng = new LatLng(lat, lon);
		
		// APIキーを使用してGeoApiContextを作成する
		GeoApiContext context = new GeoApiContext.Builder()
									.apiKey(apiKey)
									.build();
		
		
		// 逆ジオコーディングリクエストを作成して、言語を日本語に設定
	        GeocodingApiRequest request = GeocodingApi.reverseGeocode(context, latlng).language("ja");
	        GeocodingResult[] results;
	        String address = null;
	        try {
	            results = request.await();
	            Gson gson = new GsonBuilder().setPrettyPrinting().create();
	            address = gson.toJson(results[0].formattedAddress);
	            
	            // JSON形式の文字列から「""」と「日本、」を取り除く
	            address = address.replaceAll("\"", "");
	            address = address.replaceFirst("^日本、", "");
	            
	            // 郵便番号の後に改行を追加
	            address = address.replaceFirst("(〒\\d{3}-\\d{4})", "$1<br/>");

	        } catch (ApiException | InterruptedException | IOException e) {
	            e.printStackTrace();
	        } finally {
	            context.shutdown();
	        }
	        return address;
	    }
	
	//storeテーブルに店舗情報を登録
	@Transactional
	public Store create(PostRegisterForm postRegisterForm) {
		Store store = new Store();
		
		 // Nullチェックを追加
        if (postRegisterForm.getLatitude() == null || postRegisterForm.getLongitude() == null) {
            throw new IllegalArgumentException("Latitude and Longitude must not be null");
        }
		
		store.setLatitude(postRegisterForm.getLatitude());
		store.setLongitude(postRegisterForm.getLongitude());
		store.setName(postRegisterForm.getStoreName());
		
		// 緯度と経度から住所を取得
		try {
            String address = getAddressFromCoordinates(postRegisterForm.getLatitude(), postRegisterForm.getLongitude());
            store.setAddress(address);
        } catch (RequestDeniedException e) {
            throw new IllegalStateException("Failed to retrieve address due to API key restrictions.", e);
        }
		return storeRepository.save(store);
	}
}
