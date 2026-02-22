var EmailFunc = {
	instanceName : 'EmailFunc'
	, ids : {
		crsPopupEmail : '#pCRSPopEmailDiv'
	}
	, getPureId : function(_ids){
		return _ids.replace('#', '');
	}
	,ajaxLoadHtml : function(opts){
//			cfn_openLoading(null);
		//var crsFunc = window[this.instanceName];		// CRSFunc		
		if($(this.ids.crsPopupEmail).length == 0){
			$('<div />', {
				'id' : this.getPureId(this.ids.crsPopupEmail)
				,'style' : 'display:none'
			}).appendTo('body');
		}
		opts.url = CONTEXT + opts.url + (opts.params ? '?' + opts.params : '');
		//if(opts.params) opts.url = opts.url + '?' + opts.params;
		$(this.ids.crsPopupEmail)
			 .load(opts.url, function(response, status, xhr){
				if(status == 'success'){
					//$(this).html(response);																	
					//opts.callback && opts.callback.apply(this, arguments);
					opts.callback && opts.callback(response);
//						cfn_closeLoading();
				}
			});
	}	
};