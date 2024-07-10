function getLocation() {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(showPosition);
	} else {
		alert("Geolocation is not supported by this browser.");
	}
}

function showPosition(position) {
	document.querySelector('input[name="latitude"]').value = position.coords.latitude;
	document.querySelector('input[name="longitude"]').value = position.coords.longitude;
}

document.addEventListener("DOMContentLoaded", function() {
	getLocation();
});