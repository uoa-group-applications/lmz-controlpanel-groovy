(function(LMZ, undefined) {

	LMZ.invokeScript = function (){
		console.log("Sending to: "+UOA.endpoints.admin.groovyexec);
		var body = $('#textgroovyinput').val();
		console.log(body);
		$.ajax({type: 'POST',
			dataType : "JSON",
			contentType: 'application/json; charset=UTF-8',
			url: UOA.endpoints.admin.groovyexec, //url,
			data: JSON.stringify({scriptbody: body })
		}).done(function(data) {
			if (typeof(data.message) !== "undefined"){
				var elOutput = $('#textgroovyoutput');
				var currentText = elOutput.text();
				if (currentText) currentText += "\n----------------------\n\n";
				elOutput.text(currentText+data.message);
			}
		});
	};

	LMZ.clearOutput = function (){
		$('#textgroovyoutput').text("");
	};

	$(document).ready(function(){
		$("#textgroovyinput").keypress(function(e){
			if (e.ctrlKey && (e.keyCode == 13 || e.keyCode == 10)) {
				LMZ.invokeScript();
			}else if (e.keyCode == 27){
				LMZ.clearOutput();
			}
		});

		$(document).keyup(function(e){
			if (e.keyCode == 27){
				LMZ.clearOutput();
			}
		});
	});

})(window.LMZ || (window.LMZ = {}));

