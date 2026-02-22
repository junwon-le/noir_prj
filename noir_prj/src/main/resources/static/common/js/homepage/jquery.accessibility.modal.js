/*
* jQuery Modal Layer- 0.9.2
* Copyright (c) 2013 nickname stryper http://gotoplay.tistory.com/
* Dual licensed under the MIT and GPL licenses:
* http://www.opensource.org/licenses/mit-license.php
* http://www.gnu.org/licenses/gpl.html
*
* 0.9.2 Updates - Windows resize the layer position fix
*
*/
(function($){
	$.fn.modalLayer = function(){
		var $modals = this;
		var $focus ='a[href], area[href], input:not([disabled]), input:not([readonly]), select:not([disabled]), textarea:not([disabled]), button:not([disabled]), iframe, object, embed, *[tabindex], *[contenteditable]';
		var $radioCheck = "input[type='checkbox'], input[type='radio']";
		var $location = window.location.pathname;
		$modals.click(function(e){
			//$('body').addClass('noScroll');
			
			e.preventDefault();
			var $this = $(this);
			var $select_id = $($(this).attr('href'));
			var $sel_id_focus = $select_id.find($focus);
			var $focus_num = $select_id.find($focus).length;
			var $closBtn = $select_id.find('.btn_pop_close');
			var $checkLabel = $select_id.find($radioCheck);
			var clickAnchor = $this.attr('href');
			var hrefFocus = this;
			$.widthHeight = function(){
				var $leftP = ( $(window).scrollLeft() + ($(window).width() - $select_id.outerWidth(true)) / 2 );
				var $topP = ( $(window).scrollTop() + ($(window).height() - $select_id.outerHeight(true)) / 2 );
				$select_id.css({ 'top':$topP ,'left':$leftP });
			};
			$.widthHeight ();
			$(window).on("resize", function () { $.widthHeight (); }).resize();
			$('body').append('<div class="overlay" tabindex="-1"></div>');
			$(clickAnchor).siblings().find($focus).attr('tabindex','-1');
			$(clickAnchor).siblings().attr('aria-hidden','true');
			window.location.hash = clickAnchor;
			$select_id.attr('tabindex', '0').attr({'aria-hidden':'false','aria-live':'polit'}).fadeIn(100).focus();
			$select_id.on('blur', function(){ $(this).removeAttr('tabindex'); });
			$('div.overlay').fadeIn(300);
			setTimeout(function() {	$('div.overlay').css("height",$(document).height()); }, 10);
			$(window).on("resize", function () { $('div.overlay').css("height",$(document).height()); }).resize();
			$($select_id).find($focus).last().on("keydown", function(e){
				if (e.which == 9) {
					if(e.shiftKey) {
						$($select_id).find($focus).eq($focus_num - 1).focus();
						e.stopPropagation();
					} else {
						$($select_id).find($focus).eq(0).focus();
						e.preventDefault();
					};
				};
			});
			$($select_id).find($focus).first().on("keydown", function(e){
				if(e.keyCode == 9) {
					if(e.shiftKey) {
						$($select_id).find($focus).eq($focus_num - 1).focus();
						e.preventDefault();
					};
				};
			});
			$($select_id).on("keydown", {msg:clickAnchor,msg2:hrefFocus}, function(e){
				if ( e.which == 27 ) {
					e.preventDefault();
					$.fn.hide_modal (e.data.msg,e.data.msg2 );
				};
				if( $(this).is(":focus") ){
					if(e.keyCode == 9) {
						if(e.shiftKey) {
							$($select_id).find($focus).eq($focus_num - 1).focus();
							e.preventDefault();
						};
					};
				};
			});
			$($checkLabel).on("click", function(){  $(this).focus();  });
			$closBtn.on("click", {msg:clickAnchor,msg2:hrefFocus},function(e){
				//$('body').removeClass('noScroll');
				e.preventDefault();
				$.fn.hide_modal (e.data.msg,e.data.msg2 );
			});
		});
		$.fn.hide_modal = function (info, hrefFocus){
			if ( window.CSSBS_ie6789 ){
				window.location.hash = '';
			} else {
//				window.history.pushState('', document.title, window.location.pathname);
			};
			$(info).attr('aria-hidden','true').fadeOut(300);
			$(info).siblings().removeAttr('aria-hidden aria-live');
			$(info).siblings().find($focus).removeAttr('tabindex');
			$('div.overlay').fadeOut(100);
			setTimeout(function() { $('div.overlay').remove(); }, 400);
			$(info).find($radioCheck).prop('checked', false);
			$(info).find("input[type='text']").val('');
			setTimeout(function() { $(hrefFocus).focus(); }, 100);
		};
	};
})(jQuery);