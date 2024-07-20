const imageInput = document.getElementById('imageFiles');
const thumbnailContainer = document.getElementById('thumbnailContainer');
const imagePreview = document.getElementById('imagePreview');

// 画像が選択される毎に画像を表示する
imageInput.addEventListener('change', () => {
	
	// 以前のプレビューをクリア
	thumbnailContainer.innerHTML = '';
	imagePreview.innerHTML = ''; 
	
	// 選択されたファイルを取得し、各ファイルに対して画像データを読み込む
	const files = imageInput.files;
	for (let i = 0; i < files.length; i++) {
	    const file = files[i];
	    const fileReader = new FileReader();
	
		// 画像ファイルが読み込まれた際にサムネイルとして小さい画像を表示する
		fileReader.onload = (e) => {
		    const imgElement = document.createElement('img');
		    imgElement.src = e.target.result;
		    imgElement.classList.add('thumbnail');
		    thumbnailContainer.appendChild(imgElement);
		    
		    // 最初に画像が表示された際、その画像を大きい画像として表示する
		    if (i === 0) {
				const previewElement = document.createElement('img');
				previewElement.src = e.target.result;
				imagePreview.appendChild(previewElement);
			}
		    
		    // サムネイル画像にマウスをホバーすると、大きな画像を表示する
		    imgElement.addEventListener('mouseover', () => {
				imagePreview.innerHTML = '';
				
				const previewElement = document.createElement('img');
				previewElement.src = e.target.result;
				imagePreview.appendChild(previewElement); 
			});
	    
		 };
		
		 //画像データをデータURLとして読み込む
		 fileReader.readAsDataURL(file);
	 } 
});
