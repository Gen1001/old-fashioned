
//===============================================================
// debounce関数
//===============================================================
function debounce(func, wait) {
    var timeout;
    return function() {
        var context = this, args = arguments;
        var later = function() {
            timeout = null;
            func.apply(context, args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}


//===============================================================
// メニュー関連
//===============================================================

// 変数でセレクタを管理
var $menubar = $('#menubar');

// menu
$(window).on("load resize", debounce(function() {
    if(window.innerWidth <= 600) {	// ここがブレイクポイント指定箇所です
        // 小さな端末用の処理
        $('body').addClass('small-screen').removeClass('large-screen');
        $menubar.addClass('display-none').removeClass('display-block');
        $menubarHdr.removeClass('display-none ham').addClass('display-block');
    } else {
        // 大きな端末用の処理
        $('body').addClass('large-screen').removeClass('small-screen');
        $menubar.addClass('display-block').removeClass('display-none');
        $menubarHdr.removeClass('display-block').addClass('display-none');
    }
}, 10));

//===============================================================
// スムーススクロール（※バージョン2024-1）※通常タイプ
//===============================================================
$(function() {
    // ページ上部へ戻るボタンのセレクター
    var topButton = $('.pagetop');
    // ページトップボタン表示用のクラス名
    var scrollShow = 'pagetop-show';

    // スムーススクロールを実行する関数
    // targetにはスクロール先の要素のセレクターまたは'#'（ページトップ）を指定
    function smoothScroll(target) {
        // スクロール先の位置を計算（ページトップの場合は0、それ以外は要素の位置）
        var scrollTo = target === '#' ? 0 : $(target).offset().top;
        // アニメーションでスムーススクロールを実行
        $('html, body').animate({scrollTop: scrollTo}, 500);
    }

    // ページ内リンクとページトップへ戻るボタンにクリックイベントを設定
    $('a[href^="#"], .pagetop').click(function(e) {
        e.preventDefault(); // デフォルトのアンカー動作をキャンセル
        var id = $(this).attr('href') || '#'; // クリックされた要素のhref属性を取得、なければ'#'
        smoothScroll(id); // スムーススクロールを実行
    });

    // スクロールに応じてページトップボタンの表示/非表示を切り替え
    $(topButton).hide(); // 初期状態ではボタンを隠す
    $(window).scroll(function() {
        if($(this).scrollTop() >= 300) { // スクロール位置が300pxを超えたら
            $(topButton).fadeIn().addClass(scrollShow); // ボタンを表示
        } else {
            $(topButton).fadeOut().removeClass(scrollShow); // それ以外では非表示
        }
    });

    // ページロード時にURLのハッシュが存在する場合の処理
    if(window.location.hash) {
        // ページの最上部に即時スクロールする
        $('html, body').scrollTop(0);
        // 少し遅延させてからスムーススクロールを実行
        setTimeout(function() {
            smoothScroll(window.location.hash);
        }, 10);
    }
});


//===============================================================
// スライドショー
//===============================================================
$(function() {
	var slides = $('#mainimg .slide');
	var slideCount = slides.length;
	var currentIndex = 0;

	slides.eq(currentIndex).css('opacity', 1).addClass('active');

	setInterval(function() {
		var nextIndex = (currentIndex + 1) % slideCount;
		slides.eq(currentIndex).css('opacity', 0).removeClass('active');
		slides.eq(nextIndex).css('opacity', 1).addClass('active');
		currentIndex = nextIndex;
	}, 4000); // 4秒ごとにスライドを切り替える
});


//===============================================================
// 静止画から動画に切り替え
//===============================================================
$(document).ready(function() {
    $('.image-container').on('mouseover', function() {
        var $video = $(this).find('video');
        var $image = $(this).find('img');
        $image.hide();
        $video.show().get(0).play();
    }).on('mouseout', function() {
        var $video = $(this).find('video');
        var $image = $(this).find('img');
        $video.get(0).pause();
        $video.get(0).currentTime = 0; // 再生位置をリセット
        $video.hide();
        $image.show();
    });
});
