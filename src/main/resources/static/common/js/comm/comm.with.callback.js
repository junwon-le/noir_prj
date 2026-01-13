function defaultCallback(rtnData, options){
	options = options || {};
	var resultCode = parseInt(rtnData.resultCd, 10);
	if(resultCode === 00) return true;

	switch(resultCode) {
	case 11: // AuthException
		//cfn_errorBox({ code: resultCode, title:"Auth Exception", message: rtnData.resultMsg, traceInfo: rtnData.traceInfo, OK: options.error});
		break;
	case 21: // SessionException
		/*cfn_errorBox({ code: resultCode, title:"Session Exception", message: rtnData.resultMsg, traceInfo: rtnData.traceInfo,
			beforeOK: function(){
				cfn_logout();
				return false;
			}
		});*/
		break;
	case 31: // BizException
		//cfn_msgBox({ code: resultCode, title:"Biz Exception", message: rtnData.resultMsg, traceInfo: rtnData.traceInfo, OK: options.error});
		break;
	case 32: // BizException
		//cfn_msgBox({ code: resultCode, title:"Biz Exception", message: rtnData.resultMsg, traceInfo: rtnData.traceInfo, OK: options.error});
		break;
	case 41: // UserException
		//cfn_errorBox({ code: resultCode, title:"User Exception", message: rtnData.resultMsg, traceInfo: rtnData.traceInfo, OK: options.error});
		break;
	case 51: // DbException
		//cfn_errorBox({ code: resultCode, title:"DataBase Exception", message: rtnData.resultMsg, traceInfo: rtnData.traceInfo, OK: options.error});
		break;
	case 91: // OtherException
		//cfn_errorBox({ code: resultCode, title:"Other Exception", message: rtnData.resultMsg, traceInfo: rtnData.traceInfo, OK: options.error});
		break;
	default:
		//cfn_errorBox({ code: resultCode, message: rtnData.resultMsg, traceInfo: rtnData.traceInfo, OK: options.error});
		break;
	}
	
	return false;
}