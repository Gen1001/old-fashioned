 const imageInput = document.getElementById('imageFile');
 const imagePreview = document.getElementById('imagePreview');
 const cropBtn = document.getElementById('crop-btn');
 const userForm = document.getElementById('userForm');
 
 let cropper;
 
 imageInput.addEventListener('change', () => {
   if (imageInput.files[0]) {
     let fileReader = new FileReader();
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
 
 //トリミングした画像を追加する
cropBtn.addEventListener('click', function (event) {
	event.preventDefault();
  	if (cropper) {
    // トリミングされた画像データを取得
    const canvas = cropper.getCroppedCanvas();
    canvas.toBlob((blob) => {
		const file = new File([blob], imageInput.files[0].name, { type: 'image/jpeg'});
		
		//FileオブジェクトをimageInputに設定
		const dataTransfer = new DataTransfer();
		dataTransfer.items.add(file);
		imageInput.files = dataTransfer.files;
		
		userForm.submit();
	}, 'image/jpeg')
   
  }　else {
	userForm.submit();
  }
});