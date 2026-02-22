// The framework core developed based on javascript & jQuery
/*--------------------------------------------------------------------------------------------------------------------------------------------
* global prototype
*-------------------------------------------------------------------------------------------------------------------------------------------*/
String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s$)/gi, '');
};
String.prototype.ltrim = function() {
	return this.replace(/(^\s*)/, '');
};
String.prototype.rtrim = function() {
	return this.replace(/(\s$)/, '');
};

String.prototype.format = function () {
  var args = arguments;
  return this.replace(/\{\{|\}\}|\{(\d+)\}/g, function (m, n) {
    if (m == "{{") { return "{"; }
    if (m == "}}") { return "}"; }
    return args[n];
  });
};
/********************************************************************************
* Jquery 사용자 정의 확장 함수
********************************************************************************/
$.extend({
	// * 접속 도메인 출력. (isPort : 포트가 있을경우 포트출력 유무)
//	getDomain: function(isPort){
//		var _dns;
//		isPort = isPort ? isPort : false;
//
//		_dns = location.href.split("//");
//		_dns = _dns[1].substr(0, _dns[1].indexOf("/"));
//		_dns = (_dns.indexOf(":") > -1 && isPort) ? _dns : _dns.substr(0, _dns.indexOf(":"));
//		return _dns;
//	},
	// * 기본포맷(YYYY-MM-DD)으로 현재 날짜 출력.
	fmtDate: function(strDate){
		return w_getDateFormat(strDate);
	},
	fmtNumber : function(number) {
		var input = String(number);
		var reg = /(\-?\d+)(\d{3})($|\.\d+)/;
		if(reg.test(input)){
			return input.replace(reg, function(str, p1,p2,p3){
					return $.fmtNumber(p1) + "," + p2 + "" + p3;
				}
			);
		}else{
			return input;
		}
	},
	// * 숫자 여부를 판단하여 true/false 값으로 반환.
	isNumber: function(inValue){
		inValue = String(inValue).replace(/^\s*|\s*$/g, '');	// 좌우 공백 제거
		return (inValue == '' || isNaN(inValue)) ? false : true;
	},
	// * 일단위의 숫자를 두자리 기본포맷(00)으로 출력. (날짜에 사용)
	addZero: function(number){
		return parseInt(number, 10) < 10 ? "0" + number : number;
	},
	isValidEmail : function(emailAddr) {
//		var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;

//		var emailRegex = new RegExp(/^([\w\.\-]+)@([\w\-]+)((\.(\w){2,3})+)$/i);
//		return emailRegex.test(emailAddr);

		var regEmail = /([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
		if (!emailAddr.match(regEmail)) {
			return false;
		}
		return true;
	}
});

/********************************************************************************
* Loading Setting Function
********************************************************************************/

//브라우저 버전 체크
function get_version_of_IE () { 

	 var word; 
	 var version = "N/A"; 

	 var agent = navigator.userAgent.toLowerCase(); 
	 var name = navigator.appName; 

	 // IE old version ( IE 10 or Lower ) 
	 if ( name == "Microsoft Internet Explorer" ) word = "msie "; 

	 else { 
		 // IE 11 
		 if ( agent.search("trident") > -1 ) word = "trident/.*rv:"; 

		 // Microsoft Edge  
		 else if ( agent.search("edge/") > -1 ) word = "edge/"; 
	 } 

	 var reg = new RegExp( word + "([0-9]{1,})(\\.{0,}[0-9]{0,1})" ); 

	 if (  reg.exec( agent ) != null  ) version = RegExp.$1 + RegExp.$2; 

	 return version; 
} 

//IE8 일때 로딩 처리
function loading_8() {
	$('div.loading .logo span').animate({width:'100%',opacity:'1'},2000).animate({width:'0%',opacity:'0'},2000, function() {loading_8();});
}

//$(['<div class="loading">', '<div class="ing"></div>', '</div>'].join('\n')).appendTo('body');
/*function loaderUI(sw,txt){
	
	if(sw=='on'){
		var loadingPopup = '';
		loadingPopup += '<div class="loading">';
		loadingPopup += '	<div class="logo"><span>loading...</span></div>';
		loadingPopup += '	<div class="bg"></div>';
		loadingPopup += '</div>';
		
		$('body').append(loadingPopup);	
		
		if (get_version_of_IE() == '8.0'){
			loading_8();			
		}
		
//		$('.loading').fadeIn(200, function() {
//		$('<div class="loading_overlay" tabindex="-1"></div>').appendTo('body');
//		});
//		$(['<div class="loadingWrap" style="display:none;">', '<div class="loading">', '<p>처리중 입니다.</p>', '</div>', '<div class="loadingBg"></div>'].join('\n')).appendTo('body');
//		$('.loadingWrap').show();

	}else{
		$('body > .loading').clearQueue().stop().hide();
		$('.loading').remove();
		
		//$('body > .loadingWrap').clearQueue().stop().hide();
		//$('.loadingWrap').remove();
	}
}*/
function loaderUI(sw,txt){
	if(sw=='on'){
//		$('.loading').fadeIn(200, function() {
//			$('<div class="loading_overlay" tabindex="-1"></div>').appendTo('body');
//		});
		
		$(['<div class="loadingWrap" style="display:none;">', '<div class="loading">', '<p>loading...</p>', '</div>', '<div class="loadingBg"></div>'].join('\n')).appendTo('body');
		$('.loadingWrap').show();
		
	}else{
		$('body > .loadingWrap').clearQueue().stop().hide();
		$('.loadingWrap').remove();
		//$('.loading_wrap').hide();
	}
}

/**********************************************************************Start*****
 * jquery ajax 기본값 설정. [!!!수정금지!!!]
 * 작성자		: 김광일
 * 작성일		: 2013.05
********************************************************************************/
$.ajaxSetup({
	cache : false,
	type: "POST",
	timeout : 30000,		// 30초
	dataType : "json"
});
/**********************************************************************End******/


/**********************************************************************Start*****
 * w_ajax 공통 비동기 통신 [!!!수정금지!!!]
 * >>> 추가 기능이 필요하다면, 추가 함수를 정의하고 그 함수 안에서 ajax 호출 부분을 cfn_ajax로 대체하세요.
 * 작성자		: 김광일
 * 작성일		: 2013.05
 * 
 * [options filed]
 * url			: request url address
 * data			: parameter values
 * success	: 요청 성공시, 수행 함수.
 * error		: 오류 발생시, 수행 함수. 
 * @returns void
 **********************************************************************/
$.extend({
	wajax : function(options){
		var rtn_ajax;

		if (options && options.context && options.context.trim().length > 0) {
			rtn_ajax = $.ajax({
				data : options.data,
		    	url : (options.url.charAt(0) === "/" ? options.context : "")+options.url,
		    	/*임시 적용*/
				beforeSend : function(jqXHR, settings){
					// Loading Effect Start
					if($.type(options.beforeSend) === "function") options.beforeSend(jqXHR, settings);
//					jqXHR.overrideMimeType("text/plain; charset=utf-8");
				},
				complete : function(jqXHR, txtStatus){
					// Loading Effect End
					if($.type(options.complete) === "function") options.complete(jqXHR, txtStatus);
				}
		    })
		    .then(
		    		function(data, textStatus, jqXHR) {
		    			if($.type(options.success) === "function") options.success(data);
		    		},
		    		function(jqXHR, textStatus, errorThrown) {
		    			if($.type(options.error) === "function") options.error(jqXHR.readyState);
		    		}
		    )
//		    .done(function(){
//		    	
//		    })
		    .fail(function(jqXHR, textStatus, errorThrown){
		    	// Loading Effect End
				loaderUI('off');
//		    	HttpCallback(jqXHR, textStatus, errorThrown);
		    });
			
		} else {
			rtn_ajax = $.ajax(options);
		}
		return rtn_ajax;
	}
});

function w_ajax(options){
	var defaults = {
			isProgress : true,
			context : (CONTEXT || ''),	// || '${CONTEXT}' || '${pageContext.request.contextPath}'
			beforeSend : function(jqXHR, settings){
				jqXHR.overrideMimeType("text/plain; charset=utf-8");
			}
	};
	
	var bLoadingShow = true;
	if (options.visible == false){
		bLoadingShow = false;
	}
	
	var defOptions = $.extend(true, {}, defaults, options);

	if (defOptions.isProgress) {
		var porgOption = {
				beforeSend : function(jqXHR, settings){
					// Loading Effect Start
					if (bLoadingShow){
						loaderUI('on');	
					}
					jqXHR.overrideMimeType("text/plain; charset=utf-8");
				},
				complete : function(jqXHR, txtStatus){
					// Loading Effect End
					loaderUI('off');
				}
		};
		$.extend(true, defOptions, porgOption);
	}
	
	return $.wajax(defOptions);
}

/**********************************************************************End******/

var WINGSLanguage = {
	arrWeek : ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
	arrWeek_s : ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
	arrMonth : ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
	arrMonth_s : ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],

//	arrWeek_En : ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
//	arrWeek_s_En : ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
//	arrWeek_ss_En : ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"],
//	arrMonth_En : ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
//	arrMonth_s_En : ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
//	
//	arrWeek_Ko : ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"],
//	arrWeek_s_Ko : ["일", "월", "화", "수", "목", "금", "토"],
//	arrMonth_Ko : ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
//	
//	arrWeek_Ch : ["日", "月", "火", "水", "木", "金", "土"],
	
	Night : "Nig."
};

/**
 * 기본포맷(YYYY-MM-DD)으로 현재 날짜 출력.
 * @returns {String}
 */
function w_Today() {
	var today = new Date();
	return today.getFullYear() +"-"+ $.addZero(today.getMonth() + 1) +"-"+ $.addZero(today.getDate());
}

/**
 * String 타입의 날짜값을 Date 타입으로 반환.
 * @param strDate
 * @param Separator
 * @returns {Date}
 */
function w_getDate(strDate, Separator) {
	var _splitDate, _year, _month, _day;

	// 파라미터 날짜값을 분리하여 사용할 변수에 할당.
	_splitDate = String(strDate && $.type(strDate) === 'string'? strDate : w_Today() ).split(Separator && $.type(Separator) === 'string' ? Separator : '-');

	switch (_splitDate.length)
	{
	case 1:
//		if (_splitDate[0].length == 4) {	// YYYY, mmDD
//			if (parseInt(_splitDate[0], 10) > 1231) {	// YYYY
//				_year	= _splitDate[0];
//				_month	= '01';
//				_day	= '01';
//			} else {	// mmDD
//				_year	= (new Date()).getFullYear();
//				_month	= _splitDate[0].substr(0,2);
//				_day	= _splitDate[0].substr(2,2);
//			}
//		} else if(_splitDate[0].length == 6) { // YYYYmm
//			_year	= _splitDate[0].substr(0,4);
//			_month	= _splitDate[0].substr(4,2);
//			_day	= '01';
//		} else {
			_year	= _splitDate[0].substr(0,4);
			_month	= _splitDate[0].substr(4,2);
			_day	= _splitDate[0].substr(6,2);	
//		}
		break;
		
//	case 2:
//		if (_splitDate[0].length == 4) {	// YYYY
//			_year	= _splitDate[0];
//			_month	= _splitDate[1];
//			_day	= '01';
//		} else {	// mm
//			_year	= (new Date()).getFullYear();
//			_month	= _splitDate[0];
//			_day	= _splitDate[1];
//		}
//		break;
		
	case 3:
		_year	= _splitDate[0];
		_month	= _splitDate[1];
		_day	= _splitDate[2];
		break;
		
	default:
		return strDate;
	}

	return new Date(_year, _month-1, _day);
}

/**
 * w_getDateFormat - String/Date 타입의 날짜를 지정된(ex.yyyy-MM-dd)포맷의 형태로 변환하여 반환.
 * @param date
 * @param options
 * @returns
 */
function w_getDateFormat(date, options) {
	// 인수 초기화.
//	console.log(date, $.type(date));
	if($.type(date) === 'object') {
		options = date;
		date = new Date();
	}
//	console.log(date, $.type(date));
	
	var _date, _options;
	_options = options ? options : {};
//	_date = date ? ( $.type(date) === 'string' ? w_getDate(date) : ($.type(date) === 'date' ? date : new Date()) ) : new Date();
	_date = date ? ( $.type(date) === 'string' ? w_getDate(date) : date ) : new Date();
	if($.type(_date) !== 'date') {
		console.log('첫번째 인자(Argument)의 타입을 확인하세요.');
		return date;
	}else if($.type(_options) !== 'object') {
		console.log('두번째 인자(Argument)의 타입을 확인하세요.');
		return options;
	}
	
	var _defOptions = {
			format : 'yyyy-MM-dd'
	};
//	console.log(_defOptions, _options);
	_options = $.extend(true, {}, _defOptions, _options);
//	console.log(_defOptions, _options);
	
	// 날짜 포맷 변경.
	var _year, _month, _day;//, _yoil, _strYear, _strMonth, _strDay;
	_year = _date.getFullYear();
	_month = _date.getMonth() + 1;
	_day = _date.getDate();
	
	var _fmtDate = _options.format;
	_fmtDate = _fmtDate.replace(/yyyy/gi, _year);
	_fmtDate = _fmtDate.replace(/MM/g, $.addZero(_month));
	_fmtDate = _fmtDate.replace(/dd/gi, $.addZero(_day));
	
//	console.log(_fmtDate);
	return _fmtDate;
}

/**
 * w_getDayOfWeek - 입력 받은 날짜의 요일을 반환.
 * @param date
 * @returns {String}
 */
function w_getDayOfWeek(date){
	var thisDate = $.type(date) === 'date' ? date : w_getDate(date);
	var strReturn = "";
	strReturn = WINGSLanguage.arrWeek_s[thisDate.getDay()];
	return strReturn;
}

/**
 * w_getDateDiff - 현재 날짜에서 원하는 타입(년, 월, 일)의 기간 만큼을 계산하여 기본포맷(YYYY-MM-DD)로 출력.
 */
function w_getDateDiff(options){
	if($.type(options) !== 'object') {
		console.log('wings.core Message', '인자(Argument)의 타입을 확인하세요.');
		return options;
	}
	
	// 인수 초기화.
	var _defOptions = {
			date : new Date(),
			type : 'D',
//			format : 'yyyy-MM-dd',
//			separator : '-',
			degree : 0
	};
//	console.log(_defOptions, _options);
	var _options = $.extend(true, {}, _defOptions, options);
//	console.log(_defOptions, _options);
	
	
	var _date = new Date( $.type(_options.date) === 'date' ? _options.date : ($.type(_options.date) === 'string' ? w_getDate(_options.date, _options.separator) : new Date()) );
	var _degree = $.type(_options.degree) === 'number' ? _options.degree : ($.isNumber(_options.degree) ? parseInt(_options.degree, 10) : 0 );
	
//	console.log(_date, _degree);
	
	// 날짜 변환
	var rsltDate;
	switch (_options.type.toUpperCase()) {
	case 'Y':
		rsltDate = new Date(_date.setYear(_date.getFullYear() + _degree));
		break;
		
	case 'M':
		rsltDate = new Date(_date.setMonth(_date.getMonth() + _degree, 1));
		break;
		
	case 'D':
		rsltDate = new Date(_date.setDate(_date.getDate() + _degree));
		break;

	default:
		rsltDate = new Date();
		break;
	}
	
	// 타입별 결과 반환.
	if(_options.format && $.type(_options.format) === 'string')
		return w_getDateFormat(rsltDate, {format : _options.format});
	else 
		return rsltDate;
}

/**
 * w_getDiffDays - 두 날짜 차이를 계산하여 일수로 반환한다.
 * @param date1
 * @param date2
 * @param type D : 일, M : 월, Y : 년
 * @returns days
 */
function w_getDiffDays(date1, date2, type){
	// 파라미터 초기화.
	if (date1 && date2) {
		date1 = $.type(date1) === 'date' ? date1 : w_getDate(date1);
		date2 = $.type(date2) === 'date' ? date2 : w_getDate(date2);
//		console.log(date1, date2);
	} else {
		return 0;
	}
	if (!type) {
		type = 'D';
	}
	
	var interval = date2 - date1;
	
	var day = 1000 * 60 * 60 * 24;
	var month = day * 30;
	var year = month * 12;

	var rtnValue = 0;
	switch (type.toUpperCase()) {
	case 'Y':
		rtnValue = parseInt(interval / year, 10);
		break;
	case 'M':
		rtnValue = parseInt(interval / month, 10);
		break;
	case 'D':
		rtnValue = parseInt(interval / day, 10);
		break;
	default:
		rtnValue = parseInt(interval, 10);
		break;
	}
	
	return rtnValue;
}

/**
 * w_getLastDate - 입력받은 날짜 월(달)의 마지막 일자를 반환.
 * @param date
 * @returns {Date}
 */
function w_getLastDate(date) {
	// 초기화.
	date = date ? ($.type(date) === 'date' ? date : w_getDate(date)) : new Date();

	return new Date(date.getFullYear(), date.getMonth()+1, 0);
}

//기존에 저장된 페이지를 가져옴
function checkForHash() {
	if (document.location.hash) {
		var str_hash = document.location.hash;
		var oldPage = str_hash.replace("#","");
		if (isNaN(oldPage)) oldPage = 1;
		return oldPage;
	}
}

//아이디 형식체크
function fn_id_valid_check(str){
	var chk1 = /^[a-z\d]{4,20}$/i;
	var chk2 = /[a-z]/i;
	var chk3 = /\d/;
	var chk4 = /^[a-zA-Z]{1}[a-zA-Z0-9]{3,11}$/;
	
	//return chk1.test(str) && chk2.test(str) && chk3.test(str);
	return chk4.test(str);
}

//비밀번호 형식체크
function fn_pwd_valid_check(pw){
	pw_passed = true;
	var pattern1 = /[0-9]/;
	var pattern2 = /[a-zA-Z]/;
	var pattern3 = /[~!@\#$%<>^&*]/;     // 원하는 특수문자 추가 제거
	var pw_msg = "비밀번호는 “영문+숫자+특수문자” 조합으로 2종류 이상은 10자리, 3종류 이상은 9자리 이상 입력해주십시오.";

	var b1 = pattern1.test(pw);
	var b2 = pattern2.test(pw);
	var b3 = pattern3.test(pw);

	var passCnt = 0;
	if (b1) passCnt++;
	if (b2) passCnt++;
	if (b3) passCnt++;

	if (passCnt == 0 || passCnt == 1){		
		alert(pw_msg);
		pw_passed=false;
	}else if (passCnt == 2){
		if (pw.length < 10){
			alert(pw_msg);
			pw_passed=false;
		}
	}else if (passCnt == 3){
		if (pw.length < 9){
			alert(pw_msg);
			pw_passed=false;
		}
	}

	return pw_passed;

}

/********************************************************************************
* 설  명 : 쿠키(cookie)값 설정(저장)하기.
* 인  자 : [*name] : 쿠키(cookie)명, [*value] : 쿠키(cookie)값, [expiredays] : 유요기간, [domain] : 쿠키(cookie)사용 도메인
********************************************************************************/
function gfn_setCookie(name,value,expiredays,domain){
	
	var exdate=new Date();
	exdate.setDate(exdate.getDate() + expiredays);
	var c_value=escape(value) + ((expiredays==null) ? "" : "; expires="+exdate.toUTCString());
	document.cookie=name + "=" + c_value;
	
//	var todayDate=new Date();
//	document.cookie = name + "=" + escape(value) + "; path=/; expires=" + (expiredays ? todayDate.setDate(todayDate.getDate()+expiredays).toUTCString() + ";" : "") + (domain ? "domain=" + domain + ";" : "");
}

/********************************************************************************
* 설  명 : 쿠키(cookie)값 얻어오기.
********************************************************************************/
function gfn_getCookie(name){
	var arg=name+"=";
	var alen=arg.length;
	var clen=document.cookie.length;
	var i=0;

	while(i<clen){
		var j=i+alen;
		if(document.cookie.substring(i,j)==arg){
			var end=document.cookie.indexOf(";",j);
			if(end==-1)
				end=document.cookie.length;
			return unescape(document.cookie.substring(j,end));
		}
		i=document.cookie.indexOf(" ",i)+1;
		if(i==0)break;
	}
	return null;
}

/********************************************************************************
* 설  명 : 쿠키(cookie)값 삭제.
********************************************************************************/
function gfn_clearCookie(){
	var expireDate = new Date();
	expireDate.setDate(expireDate.getDate()-1);

	if (document.cookie != '') {
		var thisCookie = document.cookie.split('; ');
		for (i=0; i < thisCookie.length; i++) {
			cookieName = thisCookie[i].split('=')[0];
			document.cookie = cookieName + '= ; expires=' + expireDate.toUTCString();
		}
	}
}

/********************************************************************************
* 함수명 : gfn_hhmm()
* 설  명 : 시간 형식 변경 1630 -> 16:30
* 인  자 : [time] : hhmm타입의 숫자 (시간)
* 리턴값 : 시간 포맷 문자열 (HH:MM)
* 버  전 : 1.0
********************************************************************************/
function gfn_hhmm(time){
	if (time.length == 4){
		return time.substr(0,2) + ':' + time.substr(2,2);
	}else{
		return time;
	}
}

/********************************************************************************
* 함수명 : gfn_hhmm()
* 설  명 : 시간 형식 변경 1630 -> 16:30
* 인  자 : [time] : hhmm타입의 숫자 (시간)
* 리턴값 : 시간 포맷 문자열 (HH:MM)
* 버  전 : 1.0
********************************************************************************/
function gfn_hhmm_kor(time){
	if (time.length == 4){
		return time.substr(0,2) + '시 ' + time.substr(2,2) + '분';
	}else{
		return time;
	}
}

/********************************************************************************
* 함수명 : gfn_getWeekdayName()
* 설  명 : 일자의 요일을 출력
* 인  자 :	[*date] : 일자 (ex. 2012-01-01), 
*			[isFullName] : 전체명 여부 (ex. Y, N), 
*			[Language] : 언어구분 (ex. ko, kor, en, eng, ch, chn)
* 리턴값 : 요일 문자열
* 버  전 : 1.1
********************************************************************************/
function gfn_getWeekdayName(date, isFullName, Language){
	var thisDate, arrDays;
//	var _year, _month, _day;

	// 파라미터값 초기화.
	isFullName = (isFullName ? true : false);
	Language = (!Language ? "ko" : Language);

	// Date 타입으로 변환.
	thisDate = gfn_getDate(date);

	// 언어구분에 따라 데이터 생성.
	switch (Language.substr(0, 2)) {
	case "ko":
		if (isFullName) {
			arrDays = new Array('일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일');
		}else {
			arrDays = new Array('일', '월', '화', '수', '목', '금', '토');
		}
		break;
	case "en" :
		if (isFullName) {
			arrDays = new Array("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
		}else{
			arrDays = new Array("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat");
		}
		break;
	case "ch":
		arrDays = new Array("日", "月", "火", "水", "木", "金", "土");
		break;
	default:
		return date;
	}

	return arrDays[thisDate.getDay()];
}

/********************************************************************************
* 함수명 : gfn_getDate()
* 설  명 : 일자를 Date 타입으로 변환.
* 인  자 : [date] : 일자 (ex. 2012-01-01)
* 리턴값 : Date 타입 날짜
* 버  전 : 1.0
********************************************************************************/
function gfn_getDate(date){
	var splitDate, _year, _month, _day;

	// 파라미터 날짜값을 분리하여 사용할 변수에 할당.
	splitDate = String(date).split('-');

	switch (splitDate.length)
	{
	case 1:
		_year	= splitDate[0].substr(0,4);
		_month	= splitDate[0].substr(4,2);
		_day	= splitDate[0].substr(6,2);
		break;
	case 3:
		_year	= splitDate[0];
		_month	= splitDate[1];
		_day	= splitDate[2];
		break;
	default:
		return date;
	}
//	thisDate = new Date(_year, _month-1, _day);

	return new Date(_year, _month-1, _day);
}

/********************************************************************************
* 함수명 : gfn_getDiffDays()
* 설  명 : 두 날짜 차이 계산 (일 반환)
* 인  자 : [date1, date2, type] : 일자 (date), 일자 (date), 타입(string = d : 일 / m : 월 : y : 년)
* 리턴값 : 일수
* 버  전 : 1.0
********************************************************************************/
function gfn_getDiffDays(date1, date2, type){
	
	if (date1 == null || date2 == null) return 0;
	
	var interval = date2 - date1;
	var rtnValue = 0;
	
	var day = 1000 * 60 * 60 * 24;
	var month = day * 30;
	var year = month * 12;
	
	if (type == 'd'){
		rtnValue = parseInt(interval / day, 10);
	}else if(type == 'm'){
		rtnValue = parseInt(interval / month, 10);
	}else if(type == 'y'){		
		rtnValue = parseInt(interval / year, 10);
	}
	
	return rtnValue;	
}