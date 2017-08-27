jQuery.fn.integerFormatter = function(){
		$(this).autoNumeric({
			aSep : null,
			aDec : null,
			vMin: '0',
			vMax : '999999999'
		});
}