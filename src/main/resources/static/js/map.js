(g=>{var h,a,k,p="The Google Maps JavaScript API",c="google",l="importLibrary",q="__ib__",m=document,b=window;b=b[c]||(b[c]={});var d=b.maps||(b.maps={}),r=new Set,e=new URLSearchParams,u=()=>h||(h=new Promise(async(f,n)=>{await (a=m.createElement("script"));e.set("libraries",[...r]+"");for(k in g)e.set(k.replace(/[A-Z]/g,t=>"_"+t[0].toLowerCase()),g[k]);e.set("callback",c+".maps."+q);a.src=`https://maps.${c}apis.com/maps/api/js?`+e;d[q]=f;a.onerror=()=>h=n(Error(p+" could not load."));a.nonce=m.querySelector("script[nonce]")?.nonce||"";m.head.append(a)}));d[l]?console.warn(p+" only loads once. Ignoring:",g):d[l]=(f,...n)=>r.add(f)&&u().then(()=>d[l](f,...n))})({
    key: "AIzaSyBBG4iN0PxgFOWR1HxwPNNrbUzlErSQke0",
    v: "week",
});

let map;

async function initMap() {
//	//マーカーを指定した画像で描画
//    const mapLogo = document.createElement("img");
//    mapLogo.src = "/image/map_logo.png";
//    
	
	const { Map } = await google.maps.importLibrary("maps");
	const { AdvancedMarkerElement } = await google.maps.importLibrary("marker");

	//座標
    let coordinate = { lat: 35.16933225736418, lng: 136.8946049833043 };

	//地図の描画
	map = new Map(document.getElementById("map"), {
    center: coordinate,
    zoom: 8,
    mapId: "95c5f36ea121d61",
 	});
 	
 	var stores = /*[[${store}]]*/ '[]';
 	stores = JSON.parse(stores);
 	//storesは出力されるけど、storeは出力されないからnullの可能性が高い
 	stores.forEach(function(store) {
 		console.log(store.name);
 	});
 	
 	stores.forEach(function(store) {
		//店舗名を表示する
	    const storeName = document.createElement("div");
	    storeName.className = "store-name";
	    storeName.textContent = store.name;
	    
	    //店舗の座標
	    let storePosition = { lat: store.latitude, lng: store.longitude };
		
	 	//マーカーの設置
	 	new AdvancedMarkerElement({
		map,
		position: storePosition,
		content: storeName,
		title: store.name,
	//	content: mapLogo,
		});
		
	});
 	
}

initMap();