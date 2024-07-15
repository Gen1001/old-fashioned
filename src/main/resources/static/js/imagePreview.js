const imageInput = document.getElementById('imageFiles');
const thumbnailContainer = document.getElementById('thumbnailContainer');
const imagePreview = document.getElementById('imagePreview');

imageInput.addEventListener('change', () => {
	thumbnailContainer.innerHTML = '';
	imagePreview.innerHTML = ''; // 以前のプレビューをクリア
	
	const files = imageInput.files;
	for (let i = 0; i < files.length; i++) {
	  const file = files[i];
	  const fileReader = new FileReader();
	
	  fileReader.onload = (e) => {
	    const imgElement = document.createElement('img');
	    imgElement.src = e.target.result;
	    imgElement.classList.add('thumbnail');
	    thumbnailContainer.appendChild(imgElement);
	    
	    if (i === 0) {
			const previewElement = document.createElement('img');
			previewElement.src = e.target.result;
			imagePreview.appendChild(previewElement);
		}
	    
	    imgElement.addEventListener('mouseover', () => {
			imagePreview.innerHTML = '';
			
			const previewElement = document.createElement('img');
			previewElement.src = e.target.result;
			imagePreview.appendChild(previewElement); 
		});
    
	    };
	
	    fileReader.readAsDataURL(file);
	  }
});
