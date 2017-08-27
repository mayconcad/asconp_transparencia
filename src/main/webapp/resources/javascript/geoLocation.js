$.mask.definitions['h'] = "[NSns]";
$.mask.definitions['v'] = "[EWew]";

$(function() {
	$("body").on("keydown", ".masked-coord-input", function(ev) {
		var key = ev.which;
		if (key != 9 && (key < 37 || key > 40))
			$(".coord-address-input").val("");
	}).on("keypress", ".coord-address-input", function() {
		$(".masked-coord-input").val("");
	});
});

function codeAddress() {
	var input = arguments[0] || addressInput;
	var map = arguments[1];
	if (!map && typeof map1 != 'undefinded')
		map = map1;
	var marker = arguments[2];
	var command = arguments[3] || commandGeocoder;
	if (typeof updateAddressFromPoint == 'undefined' || !updateAddressFromPoint)
		window.updateAddressFromPoint = arguments[4];
	if (!marker && typeof marker1 != 'undefined')
		marker = marker1;
	var address = input.jq.val();
	window._lat = window._lng = null;
	if (typeof latInput != 'undefined' && typeof lngInput != 'undefined' && latInput && lngInput) {
		window._lat = latInput.jq.val();
		window._lng = lngInput.jq.val();
	}
	if ((address == null || address === undefined || address == '')
			&& (window._lat == null || window._lat == '' || window._lng == null || window._lng == ''))
		return;
	if (address) {
		var geocoder = new google.maps.Geocoder();
		geocoder.geocode({ 'address' : address }, function(results, status) {
			if (status != google.maps.GeocoderStatus.OK)
				return;
			var location = results[0].geometry.location;
			window._latLng = location;
			window._address = newAddressComponents(results);
			moveToLocation(window._latLng, address, map, marker, command);
		});
	} else {
		window._lat = dmsToDec(window._lat);
		window._lng = dmsToDec(window._lng);
		window._latLng = new google.maps.LatLng(window._lat, window._lng);
		moveToLocation(window._latLng, "", map, marker, command);
	}
}

function dmsToDec(value) {
	var pieces = value.toUpperCase().split(/[^\d\.EWNS]+/);
	var d = parseInt(pieces[0]);
	var m = parseInt(pieces[1]);
	var s = parseFloat(pieces[2]);
	var h = pieces[3].toUpperCase();

	var dec = Math.abs(d) + (60 * m + s) / 3600;
	if (h.match(/[WS]/))
		dec *= -1;
	return dec;
}

function moveToLocation(location, address, map, marker, command) {
	var lat = location.lat();
	var lng = location.lng();
	command([ { name : 'latitude', value : lat }, 
              { name : 'longitude', value : lng },
              { name : 'address', value : address } ]);
	if (marker != null)
		marker.setPosition(location);
	map.setCenter(location);
	map.setZoom(15);
	completeAddress();
}

function newAddressComponents(results) {
	var address = window._address || {};
	$.each(results[0].address_components, function(key, comp) {
		address[comp.types[0]] = (comp.types[0] == 'administrative_area_level_1') ? comp.short_name : comp.long_name;
	});
	if (address != {})
		address.formatted = results[0].formatted_address;
	return address;
}

function completeAddress() {
	if (typeof updateAddressFromPoint == 'undefined'
			|| typeof window._latLng == 'undefined' || window._latLng == null)
		return;

	var geocoder = new google.maps.Geocoder();
	geocoder.geocode({ 'latLng' : window._latLng }, function(results, status) {
		if (status != google.maps.GeocoderStatus.OK)
			return;
		var address = newAddressComponents(results);
		if (address != {}) {
			updateAddressFromPoint([ { name : 'formattedAddress', value : address.formatted }, 
			                         { name : 'latitude', value : window._latLng.lat() }, 
			                         { name : 'longitude', value : window._latLng.lng() }, 
			                         { name : 'city', value : address.locality }, 
			                         { name : 'state', value : address.administrative_area_level_1 }, 
			                         { name : 'street', value : address.route }, 
			                         { name : 'postal_code', value : address.postal_code },
			                         { name : 'street_number', value : address.street_number },
			                         { name : 'district', value : address.sublocality } ]);
		}
		window._address = {};
	});
}

function marker1DragHandler(event) {
	var latLng = event.latLng;
	window._lat = latLng.lat();
	window._lng = latLng.lng();
	window._latLng = latLng;
	completeAddress();
}