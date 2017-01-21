jQuery.fn.validateDate = function(){
  $(this).change(function(event){
    $value = $(this).val();
    if($value){
      $err = false;
      var expReg = /^(0[1-9]|1[012])\/(([1-2][0-9]\d{2})|([0-9]{0,}\_)*)$/;
      if(!$value.match(expReg)){
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