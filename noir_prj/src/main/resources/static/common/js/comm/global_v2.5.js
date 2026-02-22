// Javascript Global Variables
/********************************************************************************
* �⑥닔紐� : gfn_ajax()
* ��  紐� : Ajax 泥섎━瑜� �쒕떎.
* ��  �� : [param] : Parameter Array, [rtnType] : Return Data Type, [sUrl] : Ajax call Url, [sFnc] : Callback Function (return result, msg, Data), [sErr] : Callback Error Function
* 由ы꽩媛� : 
* 踰�  �� : 1.0
********************************************************************************/
function gfn_ajax(param, rtnType, sUrl, sFnc, sErr){
	$.ajax({
		type : 'post',
//		cache : false,
		data : param,
//    	data : param + '&rnd=' + Math.random(),
    	dataType : rtnType,
    	processData : false,
    	timeout : 10000,	// 10珥�
    	url : sUrl,
//    	beforeSend : function(){
//    		//Loading Effect
//			_loading('on');
//    		//Loading Effect
//    	},
    	success : function(rtnValue){
//    		sFnc(rtnValue.rtnResult, rtnValue.rtnMsg, rtnValue.rtnData);
			sFnc(rtnValue.rtnResult, rtnValue.rtnMsg.replace(/\/#\//gi, '\n'), rtnValue.rtnData);	//�ㅺ뎅�� 援щ텇�먯쟻��.
    	},
    	//ajax Error
    	error : function(info, xhr){
			//if(typeof sErr == 'function') sErr(rtnValue.rtnMsg);
    		if(info.readyState == '4'){
				// 媛쒕컻紐⑤뱶�� �ъ슜.
    			alert('臾몄젣媛� 諛쒖깮�덉뒿�덈떎.\n\n�곹깭肄붾뱶 : ' + info.status+ '\n' + info.responseText);
				// �댁쁺紐⑤뱶�� �ъ슜.
//				alert('臾몄젣媛� 諛쒖깮�덉뒿�덈떎.\n\n�곹깭肄붾뱶 : ' + info.status);
    		}
    		else{
    			alert('臾몄젣媛� 諛쒖깮�덉뒿�덈떎. (' + info.readyState + ')\n�좎떆�� �ㅼ떆 �쒕룄�� 二쇱꽭��.');
    		}
			
			sErr(info);
    	}
    });
}

$.ajaxSetup({
	beforeSend : function(){
		$("#fade, #light").show();
//		if($('#nowLoadingDiv').size() == 0){
//			var bodyWidth = $(document).width();
//			var bodyHeight = $(document).height();
//			var locLeft = (bodyWidth - 200) / 2;
//			var locTop = (bodyHeight - 20) / 2;
//
////			$('<div id="nowLoadingDiv" style="position:absolute; display:none; width:200px; top:10px; right:10px; padding-top:7px; text-align:center; background:#000000"><img src="/common/img/pop-loader.gif" /><br />Now Loading..</div>')
////				.appendTo('body');
//		}
//
//		$('#nowLoadingDiv').fadeIn();
	},
	complete : function(){
		$("#fade, #light").hide();
//		$('#nowLoadingDiv').fadeOut();
	}
});

/********************************************************************************
* Jquery �ъ슜�� �뺤쓽 �뺤옣 �⑥닔
********************************************************************************/
$.extend({
	// * 湲곕낯�щ㎎(YYYY-MM-DD)�쇰줈 �꾩옱 �좎쭨 異쒕젰.
	getToDate: function(){
		var today = new Date();

		return today.getFullYear() + "-" + $.addZero(today.getMonth() + 1) + "-" + $.addZero(today.getDate());
	},
	// * �꾩옱 �좎쭨�먯꽌 �먰븯�� ����(��, ��, ��)�� 湲곌컙 留뚰겮�� 怨꾩궛�섏뿬 湲곕낯�щ㎎(YYYY-MM-DD)濡� 異쒕젰.
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
	// * �쇰떒�꾩쓽 �レ옄瑜� �먯옄由� 湲곕낯�щ㎎(00)�쇰줈 異쒕젰. (�좎쭨�� �ъ슜)
	addZero: function(number){
		return parseInt(number) < 10 ? "0" + number : number;
	},
	// * �レ옄 �щ�瑜� �먮떒�섏뿬 true/false 媛믪쑝濡� 諛섑솚.
	isNumber: function(inValue){
		inValue = String(inValue).replace(/^\s*|\s*$/g, '');	// 醫뚯슦 怨듬갚 �쒓굅
		return (inValue == '' || isNaN(inValue)) ? false : true;
	},
	// * �묒냽 �꾨찓�� 異쒕젰. (isPort : �ы듃媛� �덉쓣寃쎌슦 �ы듃異쒕젰 �좊Т)
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
* Jquery-validator 湲곕낯媛� �ㅼ젙.
********************************************************************************/
/*
 * Translated default messages for the jQuery validation plugin.
 * Locale: KO
 * Filename: messages_ko.js
 */
jQuery.extend(jQuery.validator.messages, {
	required: "諛섎뱶�� �낅젰�댁빞 �⑸땲��.",
	remote: "�섏젙 諛붾엻�덈떎.",
	email: "�대찓�� 二쇱냼瑜� �щ컮濡� �낅젰�섏꽭��.",
	url: "URL�� �щ컮濡� �낅젰�섏꽭��.",
	date: "�좎쭨媛� �섎せ �낅젰�먯뒿�덈떎.",
	dateISO: "ISO �뺤떇�� 留욌뒗 �좎쭨濡� �낅젰�섏꽭��.",
	number: "�レ옄留� �낅젰�섏꽭��.",
	digits: "�レ옄(digits)留� �낅젰�섏꽭��.",
	creditcard: "�щ컮瑜� �좎슜移대뱶 踰덊샇瑜� �낅젰�섏꽭��.",
	equalTo: "媛믪씠 �쒕줈 �ㅻ쫭�덈떎.",
	accept: "�밸굺�� 二쇱꽭��.",
	maxlength: jQuery.validator.format("{0}湲��� �댁긽�� �낅젰�� �� �놁뒿�덈떎."),
	minlength: jQuery.validator.format("�곸뼱�� {0}湲��먮뒗 �낅젰�댁빞 �⑸땲��."),
	rangelength: jQuery.validator.format("{0}湲��� �댁긽 {1}湲��� �댄븯濡� �낅젰�� 二쇱꽭��."),
	range: jQuery.validator.format("{0}�먯꽌 {1} �ъ씠�� 媛믪쓣 �낅젰�섏꽭��."),
	max: jQuery.validator.format("{0} �댄븯濡� �낅젰�� 二쇱꽭��."),
	min: jQuery.validator.format("{0} �댁긽�쇰줈 �낅젰�� 二쇱꽭��.")
});

jQuery.validator.setDefaults({
	debug: false
	,onsubmit: true	// �좏슚�깆껜�� �놁씠 臾댁“嫄� submit �щ�.
//	onfocusout: false,	// �붿냼�� blur�� �좏슚�� �뺤씤�щ�. (checkbox/radio 踰꾪듉�� �쒖쇅.)
//	onkeyup: false,		// keyup�� �좏슚�� �뺤씤�щ�.
	,onclick: false		// checkbox/radio 踰꾪듉 click�� �좏슚�� �뺤씤�щ�.
//	focusInvalid: true,	// �좏슚�� 寃��� �� �ъ빱�ㅻ� �대떦 臾댄슚�꾨뱶�� �� 寃껋씤媛� �щ�.
//	focusCleanup: true,	// �섎せ�� �꾨뱶�� �ъ빱�ㅺ� 媛�硫� �먮윭硫붿꽭吏�瑜� 吏��몄� �щ�.
//	errorElement: "div",
//	errorClass: "errors",
//	errorPlacement: function(error, element) {
//		error.appendTo( element.parent("td"));
//		$('div.errors').css('color','red');
//	},
//	showErrors:function(errorMap, errorList){
//		if(this.numberOfInvalids()) { // �먮윭媛� �덉쓣 �뚮쭔..
//			alert(errorList[0].message);
//			document.getElementById(errorList[0].element.id).focus();
//		}
//	}
//	showErrors:function(errorMap, errorList){
//		if(this.numberOfInvalids()) { // �먮윭媛� �덉쓣 �뚮쭔..
//			var caption = $(errorList[0].element).data('caption') || $("label[for='"+ $(errorList[0].element).attr('id') +"']").text() || $(errorList[0].element).attr('name');
//			alert('[' + caption + '] ' + errorList[0].message);
//			$(errorList[0].element).focus();
//		}
//	}
})
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

/*--------------------------------------------------------------------------------------------------------------------------------------------
* global function
*-------------------------------------------------------------------------------------------------------------------------------------------*/

/********************************************************************************
* �⑥닔紐� : gfn_setCookie()
* ��  紐� : 荑좏궎(cookie)媛� �ㅼ젙(����)�섍린.
* ��  �� : [*name] : 荑좏궎(cookie)紐�, [*value] : 荑좏궎(cookie)媛�, [expiredays] : �좎슂湲곌컙, [domain] : 荑좏궎(cookie)�ъ슜 �꾨찓��
* 由ы꽩媛� : 
* 踰�  �� : 1.0
********************************************************************************/
function gfn_setCookie(name,value,expiredays,domain){
	var todayDate=new Date();

	document.cookie = name + "=" + escape(value) + "; path=/; expires=" + (expiredays ? todayDate.setDate(todayDate.getDate()+expiredays).toGMTString() + ";" : "") + (domain ? "domain=" + domain + ";" : "");
}

/********************************************************************************
* �⑥닔紐� : gfn_getCookie()
* ��  紐� : 荑좏궎(cookie)媛� �살뼱�ㅺ린.
* ��  �� : [name] : 荑좏궎(cookie)紐�
* 由ы꽩媛� : 
* 踰�  �� : 1.0
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
* �⑥닔紐� : gfn_setCookieFrom()
* ��  紐� : 荑좏궎(cookie)媛� Form Tag�먯꽌 �살뼱�ㅺ린.
* ��  �� : [*_objId] : �퍲D, [*_cookieName] : 荑좏궎(cookie)援щ텇媛�, [_expiredays] : �좎슂湲곌컙, [_domain] : 荑좏궎(cookie)�ъ슜 �꾨찓��
* 由ы꽩媛� : 
* 踰�  �� : 1.0
********************************************************************************/
function gfn_setCookieFrom(_objId, _cookieName, _expiredays, _domain){
	var mValue = '';
	_cookieName = _cookieName ? _objId+'_'+_cookieName : _objId;

//	$("#"+_objId).find("input[type=text],select").each(function(index){
//		mValue += $(this).attr('name') +"="+ $(this).val() +"/#/";
//	});
	mValue = $('#'+_objId).serialize();

	gfn_setCookie(_cookieName, mValue, _expiredays, _domain);
}

/********************************************************************************
* �⑥닔紐� : gfn_getCookieFrom()
* ��  紐� : 荑좏궎(cookie)媛�  Form Tag�� �명똿�섍린.
* ��  �� : [*_objId] : �퍲D, [*_cookieName] : 荑좏궎(cookie)援щ텇媛�
* 由ы꽩媛� : 
* 踰�  �� : 1.0
********************************************************************************/
function gfn_getCookieFrom(_objId, _cookieName){
	var mValue;
	_cookieName = _cookieName ? _objId+'_'+_cookieName : _objId;

	mValue = gfn_getCookie(_cookieName);
	mValue = decodeURIComponent(mValue);	//�몄퐫�� (�쒓�源⑥쭚 �꾩긽.)

	if($.trim(mValue).length > 0 ){
		for (var i=0; i < $.trim(mValue).split('&').length; i++) {
			var data = $.trim(mValue).split('&')[i].split('=');
			$("#"+_objId).find('input[name='+data[0]+'], select[name='+data[0]+']').val(data[1]);
//			$('input[name='+data[0]+']').val(data[1]);
//			$('select[name='+data[0]+']').val(data[1]);
		}
	}
}

/********************************************************************************
* �⑥닔紐� : gfn_clearCookie()
* ��  紐� : 荑좏궎(cookie)媛� ��젣.
* ��  �� : 
* 由ы꽩媛� : 
* 踰�  �� : 1.0
********************************************************************************/
function gfn_clearCookie(){
	var expireDate = new Date();
	expireDate.setDate(expireDate.getDate()-1);

	if (document.cookie != '') {
		var thisCookie = document.cookie.split('; ');
		for (i=0; i < thisCookie.length; i++) {
			cookieName = thisCookie[i].split('=')[0];
			document.cookie = cookieName + '= ; expires=' + expireDate.toGMTString();
		}
	}
}

/********************************************************************************
* �⑥닔紐� : gfn_getDate()
* ��  紐� : �쇱옄瑜� Date ���낆쑝濡� 蹂���.
* ��  �� : [date] : �쇱옄 (ex. 2012-01-01)
* 由ы꽩媛� : Date ���� �좎쭨
* 踰�  �� : 1.0
********************************************************************************/
function gfn_getDate(date){
	var splitDate, _year, _month, _day;

	// �뚮씪誘명꽣 �좎쭨媛믪쓣 遺꾨━�섏뿬 �ъ슜�� 蹂��섏뿉 �좊떦.
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
* �⑥닔紐� : gfn_getLastDays()
* ��  紐� : �낅젰�좎쭨 �붿쓽 留덉�留� �쇱옄 異쒕젰.
* ��  �� : [date] : �쇱옄 (ex. 2012-01-01)
* 由ы꽩媛� : Date ���� �좎쭨
* 踰�  �� : 1.0
********************************************************************************/
function gfn_getLastDays(date){
	var splitDate, lastDate, _year, _month, _day;

	// �뚮씪誘명꽣 �좎쭨媛믪쓣 遺꾨━�섏뿬 �ъ슜�� 蹂��섏뿉 �좊떦.
	splitDate = String(date).split('-');
	switch (splitDate.length)
	{
	case 1:
		_year	= splitDate[0].substr(0,4);
		_month	= splitDate[0].substr(4,2);
//		_day	= splitDate[0].substr(6,2);
		break;
	case 3:
		_year	= splitDate[0];
		_month	= splitDate[1];
//		_day	= splitDate[2];
		break;
	default:
		return date;
	}
	lastDate = new Date(_year, _month, 0);

	return lastDate.getDate();
}


/********************************************************************************
* �⑥닔紐� : gfn_getLastDate()
* ��  紐� : �낅젰�좎쭨 �붿쓽 留덉�留� �쇱옄 異쒕젰.
* ��  �� :	[*date] : �쇱옄 (ex. 2012-01-01)
*			[rtnType] : 諛섑솚 ���� (ex. ST, DT) ST : String, DT : Date
* 由ы꽩媛� : Date ���� �좎쭨
* 踰�  �� : 1.5
********************************************************************************/
function gfn_getLastDate(date, rtnType){
	var thisDate;
	
	// �뚮씪誘명꽣媛� 珥덇린��.
	rtnType = (!rtnType ? "ST" : rtnType);

	thisDate = gfn_getDate(date);

	return rtnType == "ST" ? thisDate.getFullYear() + "-" + $.addZero(thisDate.getMonth() + 1) + "-" + gfn_getLastDays(date) : new Date(thisDate.getFullYear(), thisDate.getMonth(), gfn_getLastDays(date));
}

/********************************************************************************
* �⑥닔紐� : gfn_getDateDiff()
* ��  紐� : ����(��, ��, ��)�� 湲곌컙 留뚰겮�� 怨꾩궛�섏뿬 湲곕낯�щ㎎(YYYY-MM-DD)濡� 異쒕젰.
* ��  �� :	[*date] : �쇱옄 (ex. 2012-01-01)
*			[*number] : 怨꾩궛�� 湲곌컙 (ex. 7, -7)
*			[*dateType] : �쇱옄���� (ex. Y, M, D)
* 由ы꽩媛� : �щ㎎�� �좎쭨 臾몄옄��
* 踰�  �� : 1.1
********************************************************************************/
function gfn_getDateDiff(date, number, dateType){
	var thisDate, dateDiff = "";
//	var _year, _month, _day;

	// �뚮씪誘명꽣媛� 珥덇린��.
	dateType = (!dateType ? "D" : dateType);

	// Date ���낆쑝濡� 蹂���.
	thisDate = gfn_getDate(date);

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
* �⑥닔紐� : gfn_getMonthName()
* ��  紐� : �쇱옄�� �붿쓣 �⑥뼱濡� 異쒕젰
* ��  �� :	[*date] : �쇱옄 (ex. 2012-01-01, MM) MM : 01, 02..., 12
*			[isFullName] : �꾩껜紐� �щ� (ex. Y, N) 
*			[Language] : �몄뼱援щ텇 (ex. ko, kor, en, eng, ch, chn)
* 由ы꽩媛� : �� 臾몄옄��
* 踰�  �� : 1.1
********************************************************************************/
function gfn_getMonthName(date, isFullName, Language){
	var thisDate, arrMonth, posMonth;
	posMonth = 0;

	// �뚮씪誘명꽣媛� 珥덇린��.
	isFullName = (isFullName ? true : false);
	Language = (!Language ? "ko" : Language);

	// Date ���낆쑝濡� 蹂���.
	if (date.length <= 2 && $.isNumber(date)) {
		if (Number(date) <= 12) {
			posMonth = Number(date) -1;
		} else {return date;}
	} else { 
		thisDate = gfn_getDate(date);
		posMonth = thisDate.getMonth();
	}

	// �몄뼱援щ텇�� �곕씪 �곗씠�� �앹꽦.
	switch (Language.substr(0, 2)) {
	case "ko":
		arrMonth = new Array('1��', '2��', '3��', '4��', '5��', '6��', '7��', '8��', '9��', '10��', '11��', '12��');
		break;
	case "en" :
		if (isFullName) {
			arrMonth = new Array("January", "Febrary", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
		}else{
			arrMonth = new Array("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
		}
		break;
	case "ch":
		arrMonth = new Array("1��", "2��", "3��", "4��", "5��", "6��", "7��", "8��", "9��", "10��", "11��", "12��");
		break;
	default:
		return date;
	}

	return arrMonth[posMonth];
}

/********************************************************************************
* �⑥닔紐� : gfn_getWeekdayName()
* ��  紐� : �쇱옄�� �붿씪�� 異쒕젰
* ��  �� :	[*date] : �쇱옄 (ex. 2012-01-01), 
*			[isFullName] : �꾩껜紐� �щ� (ex. Y, N), 
*			[Language] : �몄뼱援щ텇 (ex. ko, kor, en, eng, ch, chn)
* 由ы꽩媛� : �붿씪 臾몄옄��
* 踰�  �� : 1.1
********************************************************************************/
function gfn_getWeekdayName(date, isFullName, Language){
	var thisDate, arrDays;
//	var _year, _month, _day;

	// �뚮씪誘명꽣媛� 珥덇린��.
	isFullName = (isFullName ? true : false);
	Language = (!Language ? "ko" : Language);

	// Date ���낆쑝濡� 蹂���.
	thisDate = gfn_getDate(date);

	// �몄뼱援щ텇�� �곕씪 �곗씠�� �앹꽦.
	switch (Language.substr(0, 2)) {
	case "ko":
		if (isFullName) {
			arrDays = new Array('�쇱슂��', '�붿슂��', '�붿슂��', '�섏슂��', '紐⑹슂��', '湲덉슂��', '�좎슂��');
		}else {
			arrDays = new Array('��', '��', '��', '��', '紐�', '湲�', '��');
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
		arrDays = new Array("��", "��", "��", "麗�", "��", "��", "��");
		break;
	default:
		return date;
	}

	return arrDays[thisDate.getDay()];
}

/********************************************************************************
* �⑥닔紐� : gfn_getDateFormat()
* ��  紐� : �쇱옄瑜� �쇱젙 �щ㎎�쇰줈 蹂���.
* ��  �� :	[*date] : �쇱옄 (ex. 2012-01-01), 
*			[dType] : �щ㎎���� (ex. A1[援щ텇媛믪쑝濡� 援щ텇�� �좎쭨], A2[援щ텇媛믪쑝濡� 援щ텇�� �쇱옄�녿뒗 �좎쭨], A3[援щ텇媛믪쑝濡� 援щ텇�� �좎쭨�� �붿씪], 
*									B1[�⑥뼱濡� 援щ텇�� �좎쭨], B2[�⑥뼱濡� 援щ텇�� �쇱옄�녿뒗 �좎쭨], B3[�⑥뼱濡� 援щ텇�� �좎쭨�� �붿씪], 
*									C1[dd-mm-yyyy�뺤떇�� �좎쭨], C2[mm-yyyy�뺤떇�� �좎쭨], C3[dd-mm-yyyy�뺤떇�� �좎쭨�� �붿씪], 
*									D1[�⑥뼱濡� 援щ텇�� dd-mm-yyyy�뺤떇�� �좎쭨], D2[�⑥뼱濡� 援щ텇�� mm-yyyy�뺤떇�� �좎쭨], D3[�⑥뼱濡� 援щ텇�� dd-mm-yyyy�뺤떇�� �좎쭨�� �붿씪]), 
*			[Separator] : 援щ텇�� (ex. -, /, &[�좎쭨 援щ텇�먯� �붿씪 援щ텇�먮� 援щ텇�섎뒗 援щ텇�� ex. (_), |_|... ] ...), 
*			[isFullName] : �꾩껜紐� �щ� (ex. true, false),
*			[Language] : �몄뼱援щ텇 (ex. ko, kor, en, eng, ch, chn)
* 由ы꽩媛� : �щ㎎�� �좎쭨 臾몄옄��
* 踰�  �� : 2.5
********************************************************************************/
function gfn_getDateFormat(date, dType, Separator, isFullName, Language){
	var thisDate, arrDays, splitSeparator;
	var _year, _month, _day, _yoil, _strYear, _strMonth, _strDay;

	// �뚮씪誘명꽣媛� 珥덇린��.
	dType = (!dType ? "A1" : dType);
	Separator = (!Separator ? "" : Separator);
	isFullName = (isFullName ? true : false);
	Language = (!Language ? "ko" : Language);
	
	// Date ���낆쑝濡� 蹂��섑븯怨�, �좎쭨媛� 蹂꾨줈 媛믫븷��.
	thisDate = gfn_getDate(date);
	_year = thisDate.getFullYear();
	_month = $.addZero(thisDate.getMonth() + 1);
	_day = $.addZero(thisDate.getDate());

	// �몄뼱援щ텇�� �곕씪 �곗씠�� �앹꽦.
	switch (Language.substr(0, 2)) {
	case "ko":
		_strYear = '�� ';
		_strMonth = '�� ';
		_strDay = '��';
		break;
	case "en":
		_strYear = 'year ';
		_strMonth = 'month ';
		_strDay = 'day';
		break;
	case "ch":
		_strYear = '亮� ';
		_strMonth = '�� ';
		_strDay = '��';
		break;
	default:
		return date;
	}

	// 援щ텇��(&)瑜� �듯빐 �좎쭨 援щ텇�먯� �붿씪 援щ텇�먮� 遺꾨━�섏뿬 �붿씪 �앹꽦.
	_yoil = gfn_getWeekdayName(date, isFullName, Language);

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

	// �щ㎎���낆뿉 �곕Ⅸ 理쒖쥌 �좎쭨 �щ㎎ �앹꽦.
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
		rtnDate =  _day + Separator + gfn_getMonthName(_month, isFullName, Language) + Separator + _year;
		break;
	case 'D2':
		rtnDate =  gfn_getMonthName(_month, isFullName, Language) + Separator + _year;
		break;
	case 'D3':
		rtnDate = _yoil + ', ';
		rtnDate +=  _day + Separator + gfn_getMonthName(_month, isFullName, Language) + Separator + _year;
		break;
	default:
		return date;
	}

	return rtnDate;
}

/********************************************************************************
* �⑥닔紐� : gfn_hhmm()
* ��  紐� : �쒓컙 �뺤떇 蹂�寃� 1630 -> 16:30
* ��  �� : [time] : hhmm���낆쓽 �レ옄 (�쒓컙)
* 由ы꽩媛� : �쒓컙 �щ㎎ 臾몄옄�� (HH:MM)
* 踰�  �� : 1.0
********************************************************************************/
function gfn_hhmm(time){
	if (time.length == 4){
		return time.substr(0,2) + ':' + time.substr(2,2);
	}else{
		return time;
	}
}

/********************************************************************************
* �⑥닔紐� : gfn_hhmm()
* ��  紐� : �쒓컙 �뺤떇 蹂�寃� 1630 -> 16:30
* ��  �� : [time] : hhmm���낆쓽 �レ옄 (�쒓컙)
* 由ы꽩媛� : �쒓컙 �щ㎎ 臾몄옄�� (HH:MM)
* 踰�  �� : 1.0
********************************************************************************/
function gfn_hhmm_kor(time){
	if (time.length == 4){
		return time.substr(0,2) + '�� ' + time.substr(2,2) + '遺�';
	}else{
		return time;
	}
}

/********************************************************************************
* �⑥닔紐� : gfn_number()
* ��  紐� : �レ옄�뺤떇蹂�寃� 1800 -> 1,800
* ��  �� : [input] : �レ옄
* 由ы꽩媛� : �щ㎎ 臾몄옄��
* 踰�  �� : 1.0
********************************************************************************/
function gfn_number(input){
	var input = String(input);
	var reg = /(\-?\d+)(\d{3})($|\.\d+)/;
	if(reg.test(input)){
		return input.replace(reg, function(str, p1,p2,p3){
				return gfn_number(p1) + "," + p2 + "" + p3;
			}
		);
	}else{
		return input;
	}
}

/********************************************************************************
* �⑥닔紐� : gfn_msgLen()
* ��  紐� : 臾몄옄 湲몄씠 byte �⑥쐞濡� 諛섑솚.
* ��  �� : [message] : 臾몄옄
* 由ы꽩媛� : 臾몄옄 湲몄씠(byte)
* 踰�  �� : 1.0
********************************************************************************/
function gfn_msgLen(message){
	var nbytes = 0;

	for (i=0; i<message.length; i++) {
		var ch = message.charAt(i);
		if(escape(ch).length > 4) {
			nbytes += 2;
		} else if (ch == '\n') {
			if (message.charAt(i-1) != '\r') {
				nbytes += 1;
			}
		} else if (ch == '<' || ch == '>') {
			nbytes += 4;
		} else {
			nbytes += 1;
		}
	}
	return nbytes;
}

/*-----------------------------------------------------------------------------------------
	鍮꾨�踰덊샇 蹂댁븞 blur �대깽��
	2012-01-12 kang

	* 2012-03-28 蹂댁븞議곌굔 蹂닿컯 �묒뾽.
-----------------------------------------------------------------------------------------*/
function fnc_pwdChk(sObj){
	var sPassword = sObj.value;
	var sRegexp1 = /[a-z]/;
	var sRegexp2 = /[A-Z]/;
	var sRegexp3 = /[0-9]/;
	var sRegexp4 = /[\~\!\@\#\$\%\^\&\*\(\)\_\+\{\}\[\]]/;
	var strPwdMsg = '鍮꾨�踰덊샇 蹂댁븞議곌굔�� 留욎� �딆뒿�덈떎.';

	if(sPassword.length == 0) return false;

	if(sPassword.length < 6){
		alert(strPwdMsg);
//		$(sObj).val('').focus();
//		sObj.val('').focus();
		sObj.value='';
		sObj.focus();
		return false;
	}
/*
	var strTempPwd = '';
	if (sRegexp1.test(sPassword)) {   // �뚮Ц�� �ъ슜
		strTempPwd += '1';
	}
	if (sRegexp2.test(sPassword)) {   // ��臾몄옄 �ъ슜
		strTempPwd += '2';
	}
	if (sRegexp3.test(sPassword)) {   // �レ옄 �ъ슜
		strTempPwd += '3';
	}
	if (sRegexp4.test(sPassword)) {   // �뱀닔 臾몄옄 �ъ슜
		strTempPwd += '4';
	}

	if(sPassword.length >= 8 && sPassword.length < 10){
		if(strTempPwd.length < 3){
			alert(strPwdMsg);
			$(sObj).val('').focus();
			return false;
		}
	}
	else if(sPassword.length >= 10){
		if(strTempPwd.length < 2){
			alert(strPwdMsg);
			$(sObj).val('').focus();
			return false;
		}
	}
*/
	var strTempPwd = '';
	if (sRegexp1.test(sPassword) || sRegexp2.test(sPassword)) {   // �뚮Ц�� �ъ슜 && ��臾몄옄
		strTempPwd += '1';
	}
	if (sRegexp3.test(sPassword)) {   // �レ옄 �ъ슜
		strTempPwd += '3';
	}

	if(sPassword.length >= 6 && sPassword.length < 9){
		if(strTempPwd.length < 2){
			alert(strPwdMsg);
//			$(sObj).val('').focus();
			sObj.value='';
			sObj.focus();
			return false;
		}
	}

	return true;
}

/*
	Input 臾몄옄 泥댄겕 (onblur �먯꽌 �몄텧)

	sObj : object
	minlength : 理쒖냼�먮┸��
	maxlength : 理쒕��먮┸��
	checkVal : 4�먮━ (ex. 111 = �곷Ц, �レ옄, �밸Ц 紐⑤몢 �ъ슜 / 100 = �곷Ц留� �ъ슜)
	(�욎뿉�쒕��� �곷Ц / �レ옄 / �뱀닔臾몄옄

*/
function fnc_InputCheck(sObj, minlength, maxlength, checkVal){
	var sString = sObj.value;	
	var sRegexp1 = /[a-z]/;
	var sRegexp2 = /[A-Z]/;
	var sRegexp3 = /[0-9]/;
	var sRegexp4 = /[\~\!\@\#\$\%\^\&\*\(\)\_\+\{\}\[\]]/;
	var strStringMsg = '�낅젰 �뺤떇�� 留욎� �딆뒿�덈떎.';
		
	if(sString.length == 0) return false;
	if(checkVal.length < 3) return false;

	if(sString.length < minlength){
		alert(strStringMsg);
		sObj.value='';
		sObj.focus();
		return false;
	}

	var strTempString = 0;
	
	if (sRegexp1.test(sString) || sRegexp2.test(sString)) {	//�뚮Ц��
		strTempString += 100;
	}	

	if (sRegexp3.test(sString)) {	//�レ옄
		strTempString += 10;
	}

	if (sRegexp4.test(sString)) {	//�밸Ц
		strTempString += 1;
	}

	//臾몄옄�� 蹂���
	strTempString = String('00' + strTempString).slice(-3);	

	if(sString.length >= minlength && sString.length <= maxlength){
		if (checkVal != strTempString) {
			alert(strStringMsg);
			sObj.value='';
			sObj.focus();
			return false;
		}
	} else {
		alert(strStringMsg);
		sObj.value='';
		sObj.focus();
		return false;
	}

	return true;
}

/*================================================================================
/-- outDate(y,m) --
/	-> y,m �� �꾧낵 �붾줈 �섏뼱�ㅻ뒗 媛믪씠 �놁쓣寃쎌슦 �대씪�댁뼵�� ��,�붿쓣 ����
/--this.lastDate()--
/	->�몄옄媛�(��,��)�� �대떦�섎뒗 留덉�留� �좉낵 �붿씪�� 諛곗뿴濡� 由ы꽩
/--this.fullDate()--
/	->�몄옄媛�(��,��)�� �대떦�섎뒗 �붿쓽 1~留먯씪 源뚯��� �쇱옄�� �붿씪�� �댁감�먮같�대줈 由ы꽩(�쇱슂�� 0 ~ �좎슂�� 6)
/--calendarArr()--
/	->�щ젰 �뺥깭�� 諛곗뿴濡� 肉뚮젮以�. 鍮덉뭏�� 0 ex) 2011�� 3�� �� 寃쎌슦 , �쇱슂��~�좎슂�� �쒖쑝濡� 
/		0,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,0,0
================================================================================*/
var outDate = function(y,m){
	var defDate = new Date();
	var y = (y)?y:defDate.getFullYear();
	var m = (m)?m:defDate.getMonth();
	this.lastDate = function(){
		var rtDate = new Date(y,m,1-1)
		return [rtDate.getDate(), rtDate.getDay()];
	}
	this.fullDate = function(){
		var dates = new Array();
		var day = this.lastDate()[1];
		for (var i=this.lastDate()[0]; i>=1; i--){
			dates[i-1] = [i,day];
			if(day == 0){
				day = 6;
			}else{
				day --;
			}
		};
		return dates;
	}
	this.calendarArr = function(){
		var numArr = new Array();
		numArr[0] = this.lastDate();
		numArr[1] = this.fullDate();
		var minNum = numArr[0][0] + numArr[1][0][1];
		var forLength = (minNum%7 == 0)?minNum : (parseInt(minNum/7)+1)*7;
		var Arr = new Array();
		for (var i=0; i<forLength;i++){
			if(i < numArr[1][0][1]){
				Arr[i] = 0;
			}else if(i - numArr[1][0][1]+1 > numArr[0][0]){
				Arr[i] = 0;
			}else{
				Arr[i] = i - numArr[1][0][1]+1;
			}
		}
		return Arr ;
	}
}

/*================================================================================
/-- popSnsShare(opt) --
/	opt : facebook or twitter
================================================================================*/
function popSnsShare(opt) {
	var HOST = location.protocol+"//"+location.host;
	var HREF = location.href;
	HREF=encodeURIComponent(HREF);

	var TITLE = encodeURIComponent(document.getElementsByTagName("title")[0].innerHTML);

	var FACEBOOK = "http://www.facebook.com/sharer.php?u="+HREF;
	var TWITTER = "http://twitter.com/share?text="+TITLE+"&url="+HREF;

	//console.log(HREF);

	var op='scrollbars=yes,menubar=no,toolbar=no,location=no,status=no,width=800,height=600,top=100,left=100,resizable=no';

	if(opt=='facebook'){
		window.open(FACEBOOK, "facebookWindow",op);
		return;
	}

	if(opt=='twitter'){
		window.open(TWITTER, "twitterWindow",op);
		return;
	}
}

/*================================================================================
/-- cl(val) --
/	val : html obj, text ....etc
================================================================================*/
function cl() {
	var _ary = new Array();

	for(var i = 0, _len = arguments.length; i < _len; i++){
		_ary[i] = arguments[i];
	}
	console.log(_ary.join());
}
function clog(val) {
	console.log(val);
//	cl(arguments);
}