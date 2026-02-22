// document 생성 후 실행될 script --------------------------------------------------종료

// script function --------------------------------------------------시작
//	WCF_script(sanha web common function script)

/*	---------------------------------------------------------------------
//	함수명		:	wcf_setPaging(_parameter)
//	파라미터	:	_parameter
//	반환값		:	objext obj_rtnValue
//	설명		:	
//	히스토리	:	작성일자	작성자		작업 내용
//	---------------------------------------------------------------------
//					2013-01-28	김광일		최초 작성.
//	---------------------------------------------------------------------*/
function wcf_setPaging(_obj, _data, _enableTotRecord){		
	if(_data) {
		if (_data.length > 0){
			if (_enableTotRecord) {
				wcf_setPagingEx(_obj, _data[0].CUR_PG, _data[0].TOT_PG, _data[0].TOT_CNT);
			} else{
				wcf_setPagingEx(_obj, _data[0].CUR_PG, _data[0].TOT_PG);
			}	
		}else{
			wcf_setPagingEx(_obj, 1, 1, 1);
		}			
	}else{
		wcf_setPagingEx(_obj, 1, 1, 1);
	}
}

function wcf_setPagingEx(_obj, _nPage, _tPage, _tRecord){
	var _sPage, _ePage;
	var _strTag = '';
	
	if (_tRecord){
		$('.total').html('<strong>Total ' + _tRecord + '</strong>('+_nPage+'/'+_tPage+'page)')
	}

	// 초기화 작업.
	var _pBlock = 10;
	var _isBlockStep = true;
	_obj.html('');
	_sPage = parseInt( parseInt( (_nPage -1) / _pBlock, 10) * _pBlock +1, 10);
	_ePage = parseInt( _sPage + (_pBlock -1), 10);

	if(_ePage > _tPage) _ePage = _tPage;
	if(_tPage == 0) _ePage = 1;
	
	_nPage = parseInt(_nPage, 10);
	_tPage = parseInt(_tPage, 10);
	//_strTag+= '	<span class="list">';	
			
	/*if (_isBlockStep) { 
		_strTag += ' <a href="#" class="direction first goPage" ' +(_sPage > 1 ? ' data-pagenum="' + (_nPage-10) + '" ' : ' disabled')+ '><span>맨앞으로</span></a>';
	}	*/	
	_strTag += ' <a href="#" class="direction nv goPage" ' +(_nPage > 1 ? ' data-pagenum="' + (_nPage-1) + '" ' : ' disabled')+ '><img src="/Hotel28NewCMS_common/images/homepage/left.png" alt=""></a>';
	for (var i = _sPage; i <= _ePage; i++) {
		if(i == _nPage) 
			_strTag += ' <a class="thisPage" data-pagenum="' +i+ '" style="cursor:pointer;">' +i+ '</a>';
		else 
			_strTag += ' <a href="#" class="goPage" data-pagenum="' +i+ '" style="cursor:pointer;">' +i+ '</a>';
	}
	_strTag += ' <a href="#" class="direction nv goPage" ' +(_nPage < _tPage ? ' data-pagenum="' + (_nPage+1) + '" ' : ' disabled')+ '><img src="/Hotel28NewCMS_common/images/homepage/right.png" alt=""></a>';
	/*if (_isBlockStep) { 
		_strTag += ' <a href="#" class="direction last goPage" ' +(_ePage < _tPage ? ' data-pagenum="' + (_nPage+10) + '" ' : ' disabled')+ '><span>맨뒤로</span></a>'; 
	}	*/	
	//_strTag+= '	</span>';	
	_obj.append(_strTag);
	//$(_strTag).appendTo(_obj);
}
// script function --------------------------------------------------종료

//페이징
$(function(){
	$(document).on('click','.goPage', function(e){	
		if ($(this).data('pagenum')){
			gotoPage($(this).data('pagenum'));				
		}
		e.preventDefault();
	});

});
