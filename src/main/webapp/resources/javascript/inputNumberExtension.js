PrimeFacesExt.widget.InputNumber = PrimeFacesExt.widget.InputNumber.extend({
	init : function(cfg) {
		this._super(cfg);

		var _self = this;

		// workaround: verify value
		this.inputExternal.bind('blur', function(event) {
			cleanVal = _self.inputExternal.autoNumeric('get');
			if (cleanVal != "") {
				_self.inputInternal.attr('value', cleanVal);
			} else {
				_self.inputInternal.removeAttr('value');
			}
		});
	}
});