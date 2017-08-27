jQuery.fn.formatCurrency = function(){
		$(this).autoNumeric({
			aSep : '.',
			aDec : ',',
			vMax : '2147483647.99',
			aSign : 'R$ '
		});
}

jQuery.fn.formatNumber = function(){
	$(this).autoNumeric({
		aSep : '.',
		aDec : ',',
		vMax : '2147483647.99',
		aSign : ''
	});
}

jQuery.fn.formatNumberDecimal = function(){
	$(this).autoNumeric({
		aSep : '.',
		aDec : ',',
		vMax : '2147483647.999',
		aSign : ''
	});
}

jQuery.fn.formatCurrencyDolar = function(){
	$(this).autoNumeric({
		aSep : '.',
		aDec : ',',
		vMax : '2147483647.99',
		aSign : 'U$ '
	});
}