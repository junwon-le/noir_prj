(function($) {
    $.fn.extend({
        myFunction: function() {
            //Code Hear

            var _this = $(this);
            	_this.addClass('roomSlider');
            var $length = $(this).find('li').length;
            var $li = $(this).find('li');

            var setInt;

            $li.eq(0).addClass('active');

            $pager_wrap = $(document).find('.bx-pager');

			var $pager = "";
			for($i=0; $i < $length; $i++){
				$pager += "<a href data-slide-index=\"" + $i + "\">" + $i + "</a>";
			}
			$pager_wrap.append($pager).find('a').eq(0).addClass('active');

			$(document).on('click','.bx-pager a',function(){
				//code
				event.preventDefault();
				setInt = clearInterval(setInt);

				$pIdx = $('.bx-pager a').index($(this));

				$('.fullsizeImage li').eq($pIdx).addClass('active')
					.siblings('li').removeClass('active');

				$(this).addClass('active').siblings('a').removeClass('active');
				setInt = setInterval(function(){ autoPlay(); }, 5000);
			})

			function autoPlay(){

				//console.log('autoPlay active');

				var $active = $('.fullsizeImage').find('.active');
				var $activeIdx = $('.fullsizeImage li').index($active);

				if( $activeIdx < $length-1 ){
					$('.fullsizeImage li').eq($activeIdx+1).addClass('active')
						.siblings('li').removeClass('active');
					$('.bx-pager a').eq($activeIdx+1).addClass('active')
						.siblings('a').removeClass('active');
				}else{
					$('.fullsizeImage li').eq(0).addClass('active')
						.siblings('li').removeClass('active');
					$('.bx-pager a').eq(0).addClass('active')
						.siblings('a').removeClass('active');					
				}
			}

			setInt = setInterval(function(){ autoPlay(); }, 5000);

			//Code End
        }
    })
})(jQuery);