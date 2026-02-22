
/********************************************************************************
* Jquery 사용자 정의 확장 함수
********************************************************************************/
$.extend({
	// * 기본포맷(YYYY-MM-DD)으로 현재 날짜 출력.
	getToDate: function(strDate){
		var today = strDate || new Date();
		
		return today.getFullYear() + "-" + $.addZero(today.getMonth() + 1) + "-" + $.addZero(today.getDate());
	},
	// * 기본포맷(YYYY-MM-DD)으로 현재 날짜 출력.
	getDateFormat: function(strDate){
		var today = (strDate ? ($.isNumber(strDate) && strDate.length === 8 && new Date(strDate.substr(0,4), strDate.substr(4,2)-1, strDate.substr(6,2)) ) : new Date() );
		
		return today.getFullYear() + "-" + $.addZero(today.getMonth() + 1) + "-" + $.addZero(today.getDate());
	},
	// * 현재 날짜에서 원하는 타입(년, 월, 일)의 기간 만큼을 계산하여 기본포맷(YYYY-MM-DD)로 출력.
	getDateDiff: function(number, dateType){
		var today = new Date();
		var dateDiff = "";

		number = parseInt(number);

		switch (dateType){
			case "Y" :
				dateDiff = new Date(today.setYear(today.getFullYear() + number));
				break;
			case "M" :
				dateDiff = new Date(today.setMonth(today.getMonth() + number));
				break;
			case "D" :
				dateDiff = new Date(today.setDate(today.getDate() + number));
				break;
		}

		return dateDiff.getFullYear() + "-" + $.addZero(dateDiff.getMonth() + 1) + "-" + $.addZero(dateDiff.getDate());
	},
	// * 일단위의 숫자를 두자리 기본포맷(00)으로 출력. (날짜에 사용)
	addZero: function(number){
		return parseInt(number) < 10 ? "0" + number : number;
	},
	// * 숫자 여부를 판단하여 true/false 값으로 반환.
	isNumber: function(inValue){
		inValue = String(inValue).replace(/^\s*|\s*$/g, '');	// 좌우 공백 제거
		return (inValue == '' || isNaN(inValue)) ? false : true;
	},
	format : function(source, params){
		return $.validator.format(source, params);
	},
	// * 접속 도메인 출력. (isPort : 포트가 있을경우 포트출력 유무)
	getDomain: function(isPort){
		var _dns;
		isPort = isPort ? isPort : false;

		_dns = location.href.split("//");
		_dns = _dns[1].substr(0, _dns[1].indexOf("/"));
		_dns = (_dns.indexOf(":") > -1 && isPort) ? _dns : _dns.substr(0, _dns.indexOf(":"));
		return _dns;
	}
});

/********************************************************************************
* 함수명 : cfn_getDate()
* 설  명 : 일자를 Date 타입으로 변환.
* 인  자 : [date] : 일자 (ex. 2012-01-01)
* 리턴값 : Date 타입 날짜
* 버  전 : 1.0
********************************************************************************/
function cfn_getDate(date){
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
* 함수명 : cfn_getDateDiff()
* 설  명 : 타입(년, 월, 일)의 기간 만큼을 계산하여 기본포맷(YYYY-MM-DD)로 출력.
* 인  자 :	[*date] : 일자 (ex. 2012-01-01)
*			[*number] : 계산될 기간 (ex. 7, -7)
*			[*dateType] : 일자타입 (ex. Y, M, D)
* 리턴값 : 포맷된 날짜 문자열
* 버  전 : 1.1
********************************************************************************/
function cfn_getDateDiff(date, number, dateType){
	var thisDate, dateDiff = "";
//	var _year, _month, _day;

	// 파라미터값 초기화.
	dateType = (!dateType ? "D" : dateType);

	// Date 타입으로 변환.
	thisDate = cfn_getDate(date);

	number = parseInt(number);

	switch (dateType){
		case "Y" :
			dateDiff = new Date(thisDate.setYear(thisDate.getFullYear() + number));
			break;
		case "M" :
			dateDiff = new Date(thisDate.setMonth(thisDate.getMonth() + number));
			break;
		case "D" :
			dateDiff = new Date(thisDate.setDate(thisDate.getDate() + number));
			break;
	}

	return dateDiff.getFullYear() + "-" + $.addZero(dateDiff.getMonth() + 1) + "-" + $.addZero(dateDiff.getDate());
}

/********************************************************************************
* 함수명 : cfn_dateDiff(d2, d1)
* 설  명 : 두 날짜사이의 일자 차이를 리턴
* 인  자 :	[*date] : 일자 (ex. 2012-01-01)
* 버  전 : 1.1
********************************************************************************/
function cfn_dateDiff(d2, d1){
	return (cfn_getDate(d2) - cfn_getDate(d1)) / (1000 * 60 * 60 * 24);
}

/********************************************************************************
* 함수명 : cfn_getMonthName()
* 설  명 : 일자의 월을 단어로 출력
* 인  자 :	[*date] : 일자 (ex. 2012-01-01, MM) MM : 01, 02..., 12
*			[isFullName] : 전체명 여부 (ex. Y, N) 
*			[Language] : 언어구분 (ex. ko, kor, en, eng, ch, chn)
* 리턴값 : 월 문자열
* 버  전 : 1.1
********************************************************************************/
function cfn_getMonthName(date, isFullName, Language){
	var thisDate, arrMonth, posMonth;
	posMonth = 0;

	// 파라미터값 초기화.
	isFullName = (isFullName ? true : false);
	Language = (!Language ? "ko" : Language);

	// Date 타입으로 변환.
	if (date.length <= 2 && $.isNumber(date)) {
		if (Number(date) <= 12) {
			posMonth = Number(date) -1;
		} else {return date;}
	} else { 
		thisDate = cfn_getDate(date);
		posMonth = thisDate.getMonth();
	}

	// 언어구분에 따라 데이터 생성.
	switch (Language.substr(0, 2)) {
	case "ko":
		arrMonth = new Array('1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월');
		break;
	case "en" :
		if (isFullName) {
			arrMonth = new Array("January", "Febrary", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
		}else{
			arrMonth = new Array("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
		}
		break;
	case "ch":
		arrMonth = new Array("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月");
		break;
	default:
		return date;
	}

	return arrMonth[posMonth];
}

/********************************************************************************
* 함수명 : cfn_getWeekdayName()
* 설  명 : 일자의 요일을 출력
* 인  자 :	[*date] : 일자 (ex. 2012-01-01), 
*			[isFullName] : 전체명 여부 (ex. Y, N), 
*			[Language] : 언어구분 (ex. ko, kor, en, eng, ch, chn)
* 리턴값 : 요일 문자열
* 버  전 : 1.1
********************************************************************************/
function cfn_getWeekdayName(date, isFullName, Language){
	var thisDate, arrDays;
//	var _year, _month, _day;

	// 파라미터값 초기화.
	isFullName = (isFullName ? true : false);
	Language = (!Language ? "ko" : Language);

	// Date 타입으로 변환.
	thisDate = cfn_getDate(date);

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
* 함수명 : cfn_getDateFormat()
* 설  명 : 일자를 일정 포맷으로 변환.
* 인  자 :	[*date] : 일자 (ex. 2012-01-01), 
*			[dType] : 포맷타입 (ex. A1[구분값으로 구분된 날짜], A2[구분값으로 구분된 일자없는 날짜], A3[구분값으로 구분된 날짜와 요일], 
*									B1[단어로 구분된 날짜], B2[단어로 구분된 일자없는 날짜], B3[단어로 구분된 날짜와 요일], 
*									C1[dd-mm-yyyy형식의 날짜], C2[mm-yyyy형식의 날짜], C3[dd-mm-yyyy형식의 날짜와 요일], 
*									D1[단어로 구분된 dd-mm-yyyy형식의 날짜], D2[단어로 구분된 mm-yyyy형식의 날짜], D3[단어로 구분된 dd-mm-yyyy형식의 날짜와 요일]), 
*			[Separator] : 구분자 (ex. -, /, &[날짜 구분자와 요일 구분자를 구분하는 구분자 ex. (_), |_|... ] ...), 
*			[isFullName] : 전체명 여부 (ex. true, false),
*			[Language] : 언어구분 (ex. ko, kor, en, eng, ch, chn)
* 리턴값 : 포맷된 날짜 문자열
* 버  전 : 2.5
********************************************************************************/
function cfn_getDateFormat(date, dType, Separator, isFullName, Language){
	var thisDate, arrDays, splitSeparator;
	var _year, _month, _day, _yoil, _strYear, _strMonth, _strDay;

	// 파라미터값 초기화.
	dType = (!dType ? "A1" : dType);
	Separator = (!Separator ? "" : Separator);
	isFullName = (isFullName ? true : false);
	Language = (!Language ? "ko" : Language);
	
	// Date 타입으로 변환하고, 날짜값 별로 값할당.
	thisDate = cfn_getDate(date);
	_year = thisDate.getFullYear();
	_month = $.addZero(thisDate.getMonth() + 1);
	_day = $.addZero(thisDate.getDate());

	// 언어구분에 따라 데이터 생성.
	switch (Language.substr(0, 2)) {
	case "ko":
		_strYear = '년 ';
		_strMonth = '월 ';
		_strDay = '일';
		break;
	case "en":
		_strYear = 'year ';
		_strMonth = 'month ';
		_strDay = 'day';
		break;
	case "ch":
		_strYear = '年 ';
		_strMonth = '月 ';
		_strDay = '日';
		break;
	default:
		return date;
	}

	// 구분자(&)를 통해 날짜 구분자와 요일 구분자를 분리하여 요일 생성.
	_yoil = cfn_getWeekdayName(date, isFullName, Language);

	splitSeparator = Separator.split('&');
	switch (splitSeparator.length)
	{
	case 1:
		Separator = (!splitSeparator[0] ? "" : splitSeparator[0]);
		break;
	case 2:
		Separator = (!splitSeparator[0] ? "" : splitSeparator[0]);

		if (splitSeparator[1].indexOf('_') != -1)
			_yoil = splitSeparator[1].replace('_', _yoil);
		break;
	default:
		return date;
	}

	// 포맷타입에 따른 최종 날짜 포맷 생성.
	switch (dType) {
	case 'A1':
		rtnDate = _year + Separator + _month + Separator + _day;
		break;
	case 'A2':
		rtnDate = _year + Separator + _month;
		break;
	case 'A3':
		rtnDate = _year + Separator + _month + Separator + _day;
		rtnDate += ' ' + _yoil;
		break;
	case 'B1':
		rtnDate = _year + _strYear + _month + _strMonth + _day + _strDay;
		break;	
	case 'B2':
		rtnDate = _year + _strYear + _month + _strMonth;
		break;	
	case 'B3':
		rtnDate = _year + _strYear + _month + _strMonth + _day + _strDay;
		rtnDate += ' ' + _yoil;
		break;
	case 'C1':
		rtnDate =  _day + Separator + _month + Separator + _year;
		break;
	case 'C2':
		rtnDate =  _month + Separator + _year;
		break;
	case 'C3':
		rtnDate = _yoil + ', ';
		rtnDate +=  _day + Separator + _month + Separator + _year;
		break;
	case 'D1':
		rtnDate =  _day + Separator + cfn_getMonthName(_month, isFullName, Language) + Separator + _year;
		break;
	case 'D2':
		rtnDate =  cfn_getMonthName(_month, isFullName, Language) + Separator + _year;
		break;
	case 'D3':
		rtnDate = _yoil + ', ';
		rtnDate +=  _day + Separator + cfn_getMonthName(_month, isFullName, Language) + Separator + _year;
		break;
	default:
		return date;
	}

	return rtnDate;
}

/********************************************************************************
* 함수명 : cfn_getDateMMdd()
* 설  명 : 일자를 MM-dd 포맷으로 반환.
* 인  자 :	[*date] : 일자 (ex. 2012-01-01)
* 리턴값 : 포맷된 날짜 문자열
* 버  전 : 1.0
********************************************************************************/
function cfn_getDateMMdd(date){
	var thisDate, rtnDate;
	
	// Date 타입으로 변환하고, 날짜값 별로 값할당.
	thisDate = cfn_getDate(date);
	_year = thisDate.getFullYear();
	_month = $.addZero(thisDate.getMonth() + 1);
	_day = $.addZero(thisDate.getDate());
	
	return _month+"-"+_day;
}
/**
 * json to string 출력 [!!!수정금지!!!]
 * 작성자		: 김광일
 * 작성일		: 2013.05
 */
var printObj = typeof JSON != "undefined" ? JSON.stringify : function(obj) {
	var arr = [];
	$.each(obj, function(key, val) {
		var next = key + ": ";
		next += $.isPlainObject(val) ? printObj(val) : val;
		arr.push( next );
	});
	return "{ " +  arr.join(", ") + " }";
};

/********************************************************************************
* 함수명 : cfn_number()
* 설  명 : 숫자형식변경 1800 -> 1,800
* 인  자 : [input] : 숫자
* 리턴값 : 포맷 문자열
* 버  전 : 1.0
********************************************************************************/
function cfn_number(input){	
	var input = String(input);
	var reg = /(\-?\d+)(\d{3})($|\.\d+)/;
	if(reg.test(input)){
		return input.replace(reg, function(str, p1,p2,p3){
				return cfn_number(p1) + "," + p2 + "" + p3;
			}
		);
	}else{
		return input;
	}
}

/********************************************************************************
* 함수명 : cfn_formtdate()
* 설  명 : 20130125 -> 2013-01-25
* 인  자 : 
* 리턴값 :Baotv
* 버  전 : 1.0
********************************************************************************/
function cfn_formtdate (input){	
	var date = kendo.parseDate(input, "yyyyMMdd");
	return kendo.toString(date, "yyyy-MM-dd");	
}


/********************************************************************************
* 함수명 : cfn_openLoading()
* 설  명 : 20130125 -> 2013-01-25
* 인  자 : 
* 리턴값 :Baotv
* 버  전 : 1.0
********************************************************************************/
function cfn_openLoading(message){
	$.loader({
		className:"blue-with-image-2",
		content:message
	});
}
/********************************************************************************
* 함수명 : cfn_closeLoading()
* 설  명 : 
* 인  자 : 
* 리턴값 :Baotv
* 버  전 : 1.0
********************************************************************************/

function cfn_closeLoading(){
	$.loader('close');
}
 /*==================================================================End==== */
 
/*==================================================================Start====
 * jquery ajax 기본값 설정. [!!!수정금지!!!]
 * 작성자		: 김광일
 * 작성일		: 2013.05
 ==========================================================================*/
$.ajaxSetup({
//	global: false,
//	processData : false,
	cache : false,
	type: "POST",
	timeout : 30000,		// 30초
	dataType : "json",
	beforeSend : function( xhr, settings){
		$("#fade, #light").show();
//		console.log("iTiSt log : ", "beforeSend in Setup");
//		settings = settings || {};
		xhr.overrideMimeType("text/plain; charset=utf-8");
		//settings.fncName === "cfn_ajax" && !settings.loadInvisible && cfn_openLoading(null);
	},
	complete : function(xhr, status ){
//		console.log("iTiSt log : ", "complete");
		// 로딩 애니메이션 닫기.
		//cfn_closeLoading();
		// 세션 타이머 리셋.
//		cfn_resetSessionTimer();
		$("#fade, #light").hide();
	}
});
/*==================================================================End==== */

/*==================================================================Start====
 * cfn_ajax 공통 비동기 통신 [!!!수정금지!!!]
 * >>> 추가 기능이 필요하다면, 추가 함수를 정의하고 그 함수 안에서 ajax 호출 부분을 cfn_ajax로 대체하세요.
 * 작성자		: 김광일
 * 작성일		: 2013.05
 * 
 * [options filed]
 * url			: request url address
 * data			: parameter values
 * success	: 요청 성공시, 수행 함수.
 * error		: 오류 발생시, 수행 함수. 
 ==========================================================================*/
$(['<div class="loadingWrap">', '<div class="loadingBg"></div>', '<div id="pLoading" class="loading" style="margin-top: -52px;">', '<p>Loading</p>', '</div>', '</div>'].join('\n')).appendTo('body');
function cfn_ajax(options, sLoadingView){
	
	var _loadingWrap = $('.loadingWrap')
	, _loadingBox = $('#pLoading')
	, _loadingDelay = 300;
	
	if (options && options.url) {
		//초기화
		if (sLoadingView == 'undefined' || $.trim(sLoadingView) == ''){
			sLoadingView = true;
		}

		$.ajax({
			// custom function settings
			fncName : "cfn_ajax",
			loadInvisible : options.loadInvisible,	// add custom function to be invisible on loading animation.
			// Jquery ajax settings
			data : options.data,
	    	//url : (options.url.charAt(0) === "/" ? (CONTEXT || context) : "")+options.url,
	    	url : options.url,
	    	beforeSend : function( xhr, settings){
	    		//cfn_openLoading();
	    		$("#fade, #light").show();
	    		
/*	    		_loadingWrap
					.stop().fadeIn();
				_loadingBox
					.stop().animate({
						'margin-top' : '0px'
					}, 100);*/
				if (sLoadingView){
					_loadingWrap
						.stop().fadeIn();
					_loadingBox
						.stop().animate({
							'margin-top' : '0px'
						}, 100);
				}
	    	},
	    	success : function(data, status, xhr){	    		
	    		if(defaultCallback(data, options)){
	    			if(typeof(options.success) === "function") options.success(data);
	    		}
	    		
	    		if (sLoadingView){
		    		_loadingWrap
						.delay(_loadingDelay - 200).fadeOut();
					_loadingBox
						.delay(_loadingDelay).animate({
							'margin-top' : '-52px'
						}, _loadingDelay);	    			
	    		}
/*	    		$("#fade, #light").hide();
	    		_loadingWrap
					.delay(_loadingDelay - 200).fadeOut();
				_loadingBox
					.delay(_loadingDelay).animate({
						'margin-top' : '-52px'
					}, _loadingDelay);*/
	    		
	    	},
	    	error : function(xhr, status, strError){	//ajax Error
	    		if (sLoadingView){
		    		_loadingWrap
						.delay(_loadingDelay - 200).fadeOut();
					_loadingBox
						.delay(_loadingDelay).animate({
							'margin-top' : '-52px'
						}, _loadingDelay);	    			
	    		}
	    		
				if(typeof(options.error) === "function") options.error(xhr.readyState);
	    		if(xhr.readyState == "4"){
	    			//cfn_errorBox({ title: status, message: "<span class='hlight_r'>Problem has occurred. ("+ xhr.readyState +")</span><br />Please contact ITS.", traceInfo: strError });
	    		}
	    		else{
	    			//cfn_errorBox({ title: status, message: "<span class='hlight_r'>The network is unstable. ("+ xhr.readyState +")</span><br />Please try again.", traceInfo: strError });
	    		}
	    	}
	    });
	}
}


function fnLoading(flag){

	var _loadingWrap = $('.loadingWrap')
		, _loadingBox = $('#pLoading')
		, _loadingDelay = 300;

	if (flag == 'show'){
		_loadingWrap
			.stop().fadeIn();
		_loadingBox
			.stop().animate({
				'margin-top' : '0px'
			}, 100);
	}else{
		_loadingWrap
			.delay(_loadingDelay - 200).fadeOut();
		_loadingBox
			.delay(_loadingDelay).animate({
				'margin-top' : '-52px'
			}, _loadingDelay);
	}
}
/*==================================================================Start====
 * cfn_ajax 공통 비동기 통신 [!!!수정금지!!!]
 * >>> 추가 기능이 필요하다면, 추가 함수를 정의하고 그 함수 안에서 ajax 호출 부분을 cfn_ajax로 대체하세요.
 * 작성자		: 김광일
 * 작성일		: 2013.05
 * 
 * [options filed]
 * url			: request url address
 * data			: parameter values
 * success	: 요청 성공시, 수행 함수.
 * error		: 오류 발생시, 수행 함수. 
 ==========================================================================*/
jQuery.validator.setDefaults({
	debug: true,
	ignore: ".ignore",
	onkeyup:false,
	onclick:false,
	onfocusout:false,
//	showErrors:function(errorMap, errorList){
//		var caption = $(errorList[0].element).attr('caption') || $(errorList[0].element).attr('name');
//		alert('[' + caption + ']' + errorList[0].message);
//	}
	submitHandler : function(frm){
		frm.submit();
	}
});

/*
* Translated default messages for the jQuery validation plugin.
* Locale: KO
*/
jQuery.extend(jQuery.validator.messages, {
	required: "반드시 입력해야 합니다.",
	remote: "수정 바랍니다.",
	email: "이메일 주소를 올바로 입력하세요.",
	url: "URL을 올바로 입력하세요.",
	date: "날짜가 잘못 입력됐습니다.",
	dateISO: "ISO 형식에 맞는 날짜로 입력하세요.",
	number: "숫자만 입력하세요.",
	digits: "숫자(digits)만 입력하세요.",
	creditcard: "올바른 신용카드 번호를 입력하세요.",
	equalTo: "값이 서로 다릅니다.",
	accept: "승낙해 주세요.",
	maxlength: $.validator.format("{0}글자 이상은 입력할 수 없습니다."),
	minlength: $.validator.format("적어도 {0}글자는 입력해야 합니다."),
	rangelength: $.validator.format("{0}글자 이상 {1}글자 이하로 입력해 주세요."),
	range: $.validator.format("{0}에서 {1} 사이의 값을 입력하세요."),
	max: $.validator.format("{0} 이하로 입력해 주세요."),
	min: $.validator.format("{0} 이상으로 입력해 주세요.")
});
/*================================================================================
/-- cl(val) --
/	val : html obj, text ....etc
================================================================================*/
function cl() {
	clog.apply(this, arguments);
}
function clog(val) {
	var _ary = new Array()
		, console = window.console || {log:function(){}};

	for(var i = 0, _len = arguments.length; i < _len; i++){
		_ary[i] = arguments[i];
	}

	console.log(_ary.join());
}

/*==================================================================End==== */