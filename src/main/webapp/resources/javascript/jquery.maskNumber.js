jQuery.fn.maskNumber = function(){
		$(this).autoNumeric({
			aSep : '.',
			aDec : null,
			vMin: '0',
			vMax : '999999',
		});
}