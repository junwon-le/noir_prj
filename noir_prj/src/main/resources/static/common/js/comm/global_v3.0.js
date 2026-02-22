$.extend({
	getToDateSeparator: function(Separator){
		var today = new Date(),
			Separator = Separator || "-";//(!Separator ? "" : Separator);

		return today.getFullYear() + Separator + $.addZero(today.getMonth() + 1) + Separator + $.addZero(today.getDate());
	}
});

/*================================================================================
/-- gfnPwdCheck(arg){ --
/	arg : value
================================================================================*/
function gfnPwdCheck(arg){
	var chk1 = /^[a-zA-Z0-9]{8,13}$/;	//臾몄옄�� �쒖옉�� �곷�/�곸냼/�レ옄濡� 援ъ꽦�섏뼱 �덇퀬, 8-13�먮━	
	var chk2 = /[a-z]/;					//�곸뼱 �뚮Ц�먮줈 援ъ꽦
	var chk3 = /[A-Z]/;					//�곸뼱 ��臾몄옄濡� 援ъ꽦
	var chk4 = /\d/;					//�レ옄濡� 援ъ꽦
	
	var minLength = 8;
	var maxLength = 13;
	var rtnValue = false;
	
	if ((chk2.test(arg) || chk3.test(arg)) && chk4.test(arg)){
		if ((arg.length < minLength) || (arg.length > maxLength)){
			rtnValue = false;
		}else{
			rtnValue = true;
		}
	}else{
		rtnValue = false;
	}
	
	return rtnValue;	
}

//鍮꾨�踰덊샇 �뺤떇泥댄겕 (�ъ슜以�)
function fn_pwd_valid_check(pw){
	pw_passed = true;
	var pattern1 = /[0-9]/;
	var pattern2 = /[a-zA-Z]/;
	var pattern3 = /[~!@\#$%<>^&*]/;     // �먰븯�� �뱀닔臾몄옄 異붽� �쒓굅
	var pw_msg = "鍮꾨�踰덊샇�� �쒖쁺臾�+�レ옄+�뱀닔臾몄옄�� 議고빀�쇰줈 2醫낅쪟 �댁긽�� 10�먮━, 3醫낅쪟 �댁긽�� 9�먮━ �댁긽 �낅젰�댁＜��떆��.";

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

//�꾩씠�� �뺤떇泥댄겕
function fn_id_valid_check(str){
	var chk1 = /^[a-z\d]{4,20}$/i;
	var chk2 = /[a-z]/i;
	var chk3 = /\d/;
	var chk4 = /^[a-zA-Z]{1}[a-zA-Z0-9]{3,11}$/;
	
	//return chk1.test(str) && chk2.test(str) && chk3.test(str);
	return chk4.test(str);

}

/********************************************************************************
* �⑥닔紐� : w_getDate()
* ��  紐� : �쇱옄瑜� Date ���낆쑝濡� 蹂���.
* ��  �� : [date] : �쇱옄 (ex. 2012-01-01)
* 由ы꽩媛� : Date ���� �좎쭨
* 踰�  �� : 1.0
********************************************************************************/
function w_getDate(date){
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

	return new Date(_year, _month-1, _day);
}

///********************************************************************************
//* �⑥닔紐� : w_getDateDiff()
//* ��  紐� : ����(��, ��, ��)�� 湲곌컙 留뚰겮�� 怨꾩궛�섏뿬 湲곕낯�щ㎎(YYYY-MM-DD)濡� 異쒕젰.
//* ��  �� :	[*date] : �쇱옄 (ex. 2012-01-01)
//*			[*number] : 怨꾩궛�� 湲곌컙 (ex. 7, -7)
//*			[*dateType] : �쇱옄���� (ex. Y, M, D)
//*			[Separator] : 援щ텇��
//* 由ы꽩媛� : �щ㎎�� �좎쭨 臾몄옄��
//* 踰�  �� : 2.0
//********************************************************************************/
//function w_getDateDiff(date, number, dateType, Separator){
//	var thisDate,
//		dateDiff = "",
//		Separator = Separator || "-";
////	var _year, _month, _day;
//
//	// �뚮씪誘명꽣媛� 珥덇린��.
//	dateType = (!dateType ? "D" : dateType);
//
//	// Date ���낆쑝濡� 蹂���.
//	thisDate = w_getDate(date.replace(/-|\.|\//g, ''));
//	number = parseInt(number, 10);
//
//	switch (dateType){
//		case "Y" :
//			dateDiff = new Date(thisDate.setYear(thisDate.getFullYear() + number));
//			break;
//		case "M" :
//			dateDiff = new Date(thisDate.setMonth(thisDate.getMonth() + number));
//			break;
//		case "D" :
//			dateDiff = new Date(thisDate.setDate(thisDate.getDate() + number));
//			break;
//	}
//
//	return dateDiff.getFullYear() +Separator+ $.addZero(dateDiff.getMonth() + 1) +Separator+ $.addZero(dateDiff.getDate());
//}

/**
 * w_getDateDiff - �꾩옱 �좎쭨�먯꽌 �먰븯�� ����(��, ��, ��)�� 湲곌컙 留뚰겮�� 怨꾩궛�섏뿬 湲곕낯�щ㎎(YYYY-MM-DD)濡� 異쒕젰.
 */
function w_getDateDiff(options){
	if($.type(options) !== 'object') {
		console.log('�몄옄(Argument)�� ���낆쓣 �뺤씤�섏꽭��.');
		return options;
	}
	
	// �몄닔 珥덇린��.
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
	
	// �좎쭨 蹂���
	var rsltDate;
	switch (_options.type.toUpperCase()) {
	case 'Y':
		rsltDate = new Date(_date.setYear(_date.getFullYear() + _degree));
		break;
		
	case 'M':
		rsltDate = new Date(_date.setMonth(_date.getMonth() + _degree));
		break;
		
	case 'D':
		rsltDate = new Date(_date.setDate(_date.getDate() + _degree));
		break;

	default:
		rsltDate = new Date();
		break;
	}
	
	// ���낅퀎 寃곌낵 諛섑솚.
	if(_options.format && $.type(_options.format) === 'string')
		return w_getDateFormat(rsltDate, {format : _options.format});
	else 
		return rsltDate;
}

/**
 * w_getDateFormat - String/Date ���낆쓽 �좎쭨瑜� 吏��뺣맂(ex.yyyy-MM-dd)�щ㎎�� �뺥깭濡� 蹂��섑븯�� 諛섑솚.
 * @param date
 * @param options
 * @returns
 */
function w_getDateFormat(date, options) {

	// �몄닔 珥덇린��.
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
		console.log('泥ル쾲吏� �몄옄(Argument)�� ���낆쓣 �뺤씤�섏꽭��.');
		return date;
	}else if($.type(_options) !== 'object') {
		console.log('�먮쾲吏� �몄옄(Argument)�� ���낆쓣 �뺤씤�섏꽭��.');
		return options;
	}
	
	var _defOptions = {
			format : 'yyyy-MM-dd'
	};
//	console.log(_defOptions, _options);
	_options = $.extend(true, {}, _defOptions, _options);
//	console.log(_defOptions, _options);
	
	// �좎쭨 �щ㎎ 蹂�寃�.
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
* �⑥닔紐� : gfn_getDiffDays()
* ��  紐� : �� �좎쭨 李⑥씠 怨꾩궛 (�� 諛섑솚)
* ��  �� : [date1, date2, type] : �쇱옄 (date), �쇱옄 (date), ����(string = d : �� / m : �� : y : ��)
* 由ы꽩媛� : �쇱닔
* 踰�  �� : 1.0
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

/**
 * w_getLastDate - �낅젰諛쏆� �좎쭨 ��(��)�� 留덉�留� �쇱옄瑜� 諛섑솚.
 * @param date
 * @returns {Date}
 */
function w_getLastDate(date) {
	// 珥덇린��.
	date = date ? ($.type(date) === 'date' ? date : w_getDate(date)) : new Date();

	return new Date(date.getFullYear(), date.getMonth()+1, 0);
}

/**
 * 湲곕낯�щ㎎(YYYY-MM-DD)�쇰줈 �꾩옱 �좎쭨 異쒕젰.
 * @returns {String}
 */
function w_Today() {
	var today = new Date();
	return today.getFullYear() +"-"+ $.addZero(today.getMonth() + 1) +"-"+ $.addZero(today.getDate());
}