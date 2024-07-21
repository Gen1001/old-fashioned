package com.example.oldfashioned.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.oldfashioned.entity.Store;
import com.example.oldfashioned.form.PostRegisterForm;
import com.example.oldfashioned.repository.StoreRepository;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
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
        GeocodingResult[] results;
        String address = null;
        try {
        	results = GeocodingApi.reverseGeocode(context, latlng).language("ja").await();
            if (results != null && results.length > 0) {
                address = formatAddress(results[0]);
            }
        } catch (ApiException | InterruptedException | IOException e) {
        	 if (e instanceof RequestDeniedException) {
                 throw (RequestDeniedException) e;
             }
             e.printStackTrace();
        } finally {
            context.shutdown();
        }
        return address;
	}
	
	// 住所をフォーマットするメソッド
    private String formatAddress(GeocodingResult result) {
        String[] addressComponents = new String[5];
        
        for (int i = 0; i < result.addressComponents.length; i++) {
            String type = result.addressComponents[i].types[0].toString();
            switch (type) {
                case "administrative_area_level_1":
                    addressComponents[0] = result.addressComponents[i].longName; // 都道府県
                    break;
                case "locality":
                    addressComponents[1] = result.addressComponents[i].longName; // 市区町村
                    break;
                case "sublocality_level_1":
                    addressComponents[2] = result.addressComponents[i].longName; // 区
                    break;
                case "neighborhood":
                    addressComponents[3] = result.addressComponents[i].longName; // 町名
                    break;
                case "premise":
                    addressComponents[4] = result.addressComponents[i].longName; // 建物名など
                    break;
                default:
                    break;
            }
        }
        StringBuilder formattedAddress = new StringBuilder();
        for (String component : addressComponents) {
            if (component != null) {
                if (formattedAddress.length() > 0) {
                    formattedAddress.append(" ");
                }
                formattedAddress.append(component);
            }
        }
        return formattedAddress.toString();
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
            System.out.println("API key restrictions are not properly set: " + e.getMessage());
            throw new IllegalStateException("Failed to retrieve address due to API key restrictions.", e);
        }
		return storeRepository.save(store);
	}
}
