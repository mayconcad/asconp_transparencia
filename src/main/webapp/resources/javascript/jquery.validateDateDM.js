//validate Dia/Mês
jQuery.fn.validateDateDM = function(){
  $(this).change(function(event){
    $value = $(this).val();
    if($value){
      $err = false;
      var expReg = /(0[1-9]|1[0-9]|2[0-9]|3[01])\/(0[1-9]|1[012])/;
      if($value.match(expReg)){
        var $dd  = parseFloat($value.substring(0,2));
        var $mm  = parseFloat($value.substring(3,5));
        var $yy  = parseFloat($value.substring(6,10));

        if(($mm==4 && $dd>30) || ($mm==6 && $dd>30) || ($mm==9 && $dd>30) || ($mm==11 && $dd>30)){
          $err = true;
        }else{
          if($yy%4!=0 && $mm==2 && $dd>28){
            $err = true;
          }else{
            if($yy%4==0 && $mm==2 && $dd>29){
              $err = true;
            }
          }
        }
      }else{
        $err = true;
      }
      if($err){
        $(this).val('');
        setTimeout(function(){ $(this).focus();},50);                      
      }else{
        return $(this);
      }      
    }else{
      return $(this);
    }
  });
}


/**
 * TODO: Refatorar posteriormente.
 * Se o pattern for dd/mm/yyyy = total length = 10
 * a função valida dd/mm/yyyy caso o contrário
 * valida apenas mm/yyyy.
 */
jQuery.fn.validateDate = function(pattern){
	  $(this).change(function(event){
	    $value = $(this).val();
	    if($value){
	      $err = false;
	      var expReg = /^((0[1-9]|[12][0-9]|3[01])\/(0[1-9]|1[012])\/[1-2][0-9]\d{2})$/;
	      if($value.length < 10)
	    	  expReg = /^((0[1-9]|1[012])\/[1-2][0-9]\d{2})$/;
	      if($value.match(expReg)){
	    	  
	    	  	var	$dd  = 0;
		        var $mm  = 0;
		        var $yy  = 0;
	    	
	    	if(pattern.length === 10) {
	    		$dd  = parseFloat($value.substring(0,2));
		        $mm  = parseFloat($value.substring(3,5));
		        $yy  = parseFloat($value.substring(6,10));
	    	} else {
	    		$dd  = 2;
		        $mm  = parseFloat($value.substring(0,2));
		        $yy  = parseFloat($value.substring(3,6));
	    	}
	    	

	        if(($mm==4 && $dd>30) || ($mm==6 && $dd>30) || ($mm==9 && $dd>30) || ($mm==11 && $dd>30)){
	          $err = true;
	        }else{
	          if($yy%4!=0 && $mm==2 && $dd>28){
	            $err = true;
	          }else{
	            if($yy%4==0 && $mm==2 && $dd>29){
	              $err = true;
	            }
	          }
	        }
	      }else{
	        $err = true;
	      }
	      if($err){
	        $(this).val('');
	        setTimeout(function(){ $(this).focus();},50);                      
	      }else{
	        return $(this);
	      }      
	    }else{
	      return $(this);
	    }
	  });
 };