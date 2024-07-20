 const imageInput = document.getElementById('imageFile');
 const imagePreview = document.getElementById('imagePreview');
 const cropBtn = document.getElementById('crop-btn');
 const userForm = document.getElementById('userForm');
 const firstPreview = document.getElementById('firstPreview');
 let cropper;
 
 // 画像が選択される毎にトリミング枠と画像を表示する
 imageInput.addEventListener('change', () => {
	firstPreview.style.display= 'none';	
	
	// ユーザーが画像を選択している場合、画像に対してトリミング機能を提供する
    if (imageInput.files[0]) {
     let fileReader = new FileReader();
     
     // 読み込みが完了すると、画像をプレビュー表示する
     fileReader.onload = () => {
       imagePreview.innerHTML = `<img id="image" src="${fileReader.result}" class="mb-3">`;
       
       //画像をトリミングする
       var cropperImg = document.getElementById('image');
       cropper = new Cropper(cropperImg, {
		aspectRatio: 1,
        viewMode: 1,
        dragMode: 'move',
        autoCropArea: 1,
        guides: false,
        cropBoxMovable: false,
        cropBoxResizable: false,
        toggleDragModeOnDblclick: false,
	 });
	 
     }
     fileReader.readAsDataURL(imageInput.files[0]);
   } else {
     imagePreview.innerHTML = '';
   }
 })
 
// クリックされた際にトリミングした画像をフォームに追加して登録する
cropBtn.addEventListener('click', function (event) {
	
	// 画像が選択されているかどうか確認
	if (imageInput.files.length != 0) {
		event.preventDefault();// イベントをキャンセルする
		
		//トリミングされているか確認
	  	if (cropper) {
			
	    // トリミングされた画像データを取得
	    const canvas = cropper.getCroppedCanvas();
	    
	    // 生成されたBlobオブジェクトをもとにFileオブジェクトをimageInputに設定
	    canvas.toBlob((blob) => {
			const file = new File([blob], imageInput.files[0].name, { type: 'multipart/form-data'});
			
			const dataTransfer = new DataTransfer();
			dataTransfer.items.add(file);
			imageInput.files = dataTransfer.files;
			
			userForm.submit();
		}, 'multipart/form-data')
	   
	  }　
  } else {
	userForm.submit();
  }
});
