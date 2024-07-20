// ブラウザがGeolocation APIをサポートしているかどうかを確認する
function getLocation() {
	
	if (navigator.geolocation) {
		
		// 現在の位置情報を取得する
		navigator.geolocation.getCurrentPosition(showPosition);
	} else {
		alert("Geolocation is not supported by this browser.");
	}
}

// 緯度と経度をフィールドに紐づける
function showPosition(position) {
	document.querySelector('input[name="latitude"]').value = position.coords.latitude;
	document.querySelector('input[name="longitude"]').value = position.coords.longitude;
}

// storeNameが変更された時にgetLocationを呼び出す
document.addEventListener("DOMContentLoaded", function() {
	document.getElementById("storeName").addEventListener("change", function() {
		getLocation();
	});
});