function getVersion() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
		if (this.readyState == 4) {
			if (this.status == 200) {
        	 	document.getElementById("paragraphVersion").outerText = this.responseText;
         	} else {
        	 	alert('Error: ' + this.status + " " + this.responseText);
			}
         }
    };
    var url = location.protocol + '//' + location.host + "/version";
    xhttp.open("GET", url, true);
  	xhttp.send(null);
}