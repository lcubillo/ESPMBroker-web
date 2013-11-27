function lanzando(evt) {
	var button = getTarget(evt)
   var hidden = document.createElement("INPUT");
	hidden.type = "hidden";
	hidden.name = button.name;
	hidden.value = button.value;
	button.form.appendChild(hidden);
	var inputs = document.getElementsByTagName("input");

	for (var i = 0; i < inputs.length; i++) {
		var input = inputs[i];

		if (input.type == "submit" || input.type == "image" || input.type == "reset") {
			input.disabled = true;
		}
	}

	var buttons = document.getElementsByTagName("button");
	
	for (var i = 0; i < buttons.length; i++) {
		buttons[i].disabled = true;
	}


   var root = document.getElementById("wrap");
	var oDiv = document.createElement("DIV");
	root.appendChild(oDiv);
	oDiv.id = "reloj";
	oDiv.appendChild(document.createTextNode(" "));
   button.form.submit();
//   evt.form.submit();
   return true;

};

if (window.addEventListener) {
	function attach(element, event, handler) {
      element.addEventListener(event, handler, false);
	}
	
	function getTarget(evt) {
        return evt.target;
	}
} else {
	function attach(element, event, handler) {
		element.attachEvent("on" + event, handler); 
	}
	
	function getTarget(evt) {
        return window.event.srcElement;
	}
}

attach(window, "load", function () {
	var inputs = document.getElementsByTagName("input");
	
	for (var i = 0; i < inputs.length; i++) {
		var input = inputs[i];

		if (input.type == "submit" || input.type == "image") {
			attach(input, "click", lanzando);
		}
	}
} );

attach(window, "unload", function () {
   var reloj = document.getElementById("reloj");

	if (reloj) {
		reloj.parentNode.removeChild(reloj);
	}
} );

