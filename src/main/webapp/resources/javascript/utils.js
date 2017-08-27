String.prototype.replaceAll = function(de, para) {
	var str = this;
	var pos = str.indexOf(de);
	while (pos > -1) {
		str = str.replace(de, para);
		pos = str.indexOf(de);
	}
	return (str);
};

function getNumber() {
	currentValue = valueInput.jq.val();
	launchingValor.jq.val(currentValue.replace("R$ ", "").replaceAll(".", "")
			.replace(",", "."));
}

function setCurrencyMask() {
	$(function() {
		$('.currency').maskMoney({
			symbol : "R$ ",
			showSymbol : true,
			symbolStay : true,
			decimal : ",",
			thousands : ".",
			defaultZero : false
		});

	});
}

function setFocus(index) {
	index.jq.focus();
}

function setMaskInInput() {
	jQuery(function($) {
		$('input[type="text"]').setMask();
	});
}

var regexNumeros = /^(0[1-9]|[1-9]|1[0-9]|2[0-9]|3[0|1])\/{1}(0[1-9]|[1-9]|1[0-2])\/{1}([1-2]\d{3})$/;
function validaDate(value) {
	if (!regexNumeros.test(value.input.val())) {
		value.input.val("");
	}
}

function renderer(){
	setTimeout(function() {

		$("div.ui-selectonemenu").each(function() {
			$(this).css("width", "");
		});

		$("label.ui-selectonemenu-label").each(function() {
			$(this).css("width", "97%");
		});

	});
}