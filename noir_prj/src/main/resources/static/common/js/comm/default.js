// �꾩옱 �섏씠吏� 媛�
function Rsvn(depth1){
	$('#rsvn > li:eq('+depth1+') > a').addClass("active");
}
function Member(depth1){
	$('#member > li:eq('+depth1+') > a').addClass("active");
}

// Select Skin
(function ($) {
    $.fn.select_skin = function (w) {
        return $(this).each(function(i) {
            s = $(this);
            if (!s.attr('multiple')) {
                s.wrap('<div class="cmf-skinned-select"></div>');
                c = s.parent();
                c.children().before('<div class="cmf-skinned-text">&nbsp;</div>').each(function() {
                    if (this.selectedIndex >= 0) $(this).prev().text(this.options[this.selectedIndex].innerHTML)
                });
                c.width(s.outerWidth()-2);
                c.height(s.outerHeight()-2);
                c.css('background-color', s.css('background-color'));
                c.css('color', s.css('color'));
                c.css('font-size', s.css('font-size'));
                c.css('font-family', s.css('font-family'));
                c.css('font-style', s.css('font-style'));
                c.css('position', 'relative');
                s.css( { 'opacity': 0,  'position': 'relative', 'z-index': 100 } );
                var t = c.children().prev();
                t.height(c.outerHeight()-s.css('padding-top').replace(/px,*\)*/g,"")-s.css('padding-bottom').replace(/px,*\)*/g,"")-t.css('padding-top').replace(/px,*\)*/g,"")-t.css('padding-bottom').replace(/px,*\)*/g,"")-2);
                t.width(c.innerWidth()-s.css('padding-right').replace(/px,*\)*/g,"")-s.css('padding-left').replace(/px,*\)*/g,"")-t.css('padding-right').replace(/px,*\)*/g,"")-t.css('padding-left').replace(/px,*\)*/g,"")-c.innerHeight());
                t.css( { 'opacity': 100, 'overflow': 'hidden', 'position': 'absolute', 'text-indent': '0px', 'z-index': 1, 'top': 0, 'left': 0 } );
                c.children().click(function() {
                    t.html( (this.options.length > 0 && this.selectedIndex >= 0 ? this.options[this.selectedIndex].innerHTML : '') );
                });
                c.children().change(function() {
                    t.html( (this.options.length > 0 && this.selectedIndex >= 0 ? this.options[this.selectedIndex].innerHTML : '') );
                });
             }
        });
    }
    $.fn.select_unskin = function (w) {
        return $(this).each(function(i) {
            s = $(this);
            if (!s.attr('multiple') && s.parent().hasClass('cmf-skinned-select')) {
                s.siblings('.cmf-skinned-text').remove();
                s.css( { 'opacity': 100, 'z-index': 0 } ).unwrap();
             }
        });
    }
}(jQuery));

/*
 * In-Field Label jQuery Plugin
 */
(function($){$.InFieldLabels=function(b,c,d){var f=this;f.$label=$(b);f.label=b;f.$field=$(c);f.field=c;f.$label.data("InFieldLabels",f);f.showing=true;f.init=function(){f.options=$.extend({},$.InFieldLabels.defaultOptions,d);if(f.$field.val()!=""){f.$label.hide();f.showing=false};f.$field.focus(function(){f.fadeOnFocus()}).blur(function(){f.checkForEmpty(true)}).bind('keydown.infieldlabel',function(e){f.hideOnChange(e)}).change(function(e){f.checkForEmpty()}).bind('onPropertyChange',function(){f.checkForEmpty()})};f.fadeOnFocus=function(){if(f.showing){f.setOpacity(f.options.fadeOpacity)}};f.setOpacity=function(a){f.$label.stop().animate({opacity:a},f.options.fadeDuration);f.showing=(a>0.0)};f.checkForEmpty=function(a){if(f.$field.val()==""){f.prepForShow();f.setOpacity(a?1.0:f.options.fadeOpacity)}else{f.setOpacity(0.0)}};f.prepForShow=function(e){if(!f.showing){f.$label.css({opacity:0.0}).show();f.$field.bind('keydown.infieldlabel',function(e){f.hideOnChange(e)})}};f.hideOnChange=function(e){if((e.keyCode==16)||(e.keyCode==9))return;if(f.showing){f.$label.hide();f.showing=false};f.$field.unbind('keydown.infieldlabel')};f.init()};$.InFieldLabels.defaultOptions={fadeOpacity:0.5,fadeDuration:300};$.fn.inFieldLabels=function(c){return this.each(function(){var a=$(this).attr('for');if(!a)return;var b=$("input#"+a+"[type='text'],"+"input#"+a+"[type='password'],"+"textarea#"+a);if(b.length==0)return;(new $.InFieldLabels(this,b[0],c))})}})(jQuery);

$(function(){ $(".placeholder").inFieldLabels(); });
 

jQuery(document).ready(function() {
	jQuery(".quick_rsvn_form select, .input_item select").select_skin();
});

// �묎렐�� �덉씠�� �앹뾽
jQuery(document).ready(function($) {
	//$('.open_modal').modalLayer();
	$('.open_modal_pop').modalLayer();
	
});