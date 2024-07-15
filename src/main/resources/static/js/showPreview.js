document.addEventListener('DOMContentLoaded', () => {
    const thumbnailContainer = document.querySelector('.thumbnail-container');
    const imagePreview = document.getElementById('imagePreview');
    const thumbnails = thumbnailContainer.getElementsByClassName('thumbnail');

    // 最初の画像をプレビューに表示
    if (thumbnails.length > 0) {
        const firstThumbnail = thumbnails[0];
        const firstImageElement = document.createElement('img');
        firstImageElement.src = firstThumbnail.src;
        firstImageElement.classList.add('preview-image');
        imagePreview.appendChild(firstImageElement);
    }

    // マウスオーバーイベントを追加
    Array.from(thumbnails).forEach(thumbnail => {
        thumbnail.addEventListener('mouseover', (event) => {
            imagePreview.innerHTML = '';

            const previewElement = document.createElement('img');
            previewElement.src = event.target.src;
            previewElement.classList.add('preview-image');
            imagePreview.appendChild(previewElement);
        });
    });
});
