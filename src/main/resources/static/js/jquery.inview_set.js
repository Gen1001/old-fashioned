//upスタイルが画面内にきたら、スタイルupstyleを適用する
$('.up').on('inview', function() {
	$(this).addClass('upstyle');
});

//blurスタイルが画面内にきたら、スタイルblurstyleを適用する
$('.blur').on('inview', function() {
	$(this).addClass('blurstyle');
});
