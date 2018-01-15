
var _emptyGameObject = '{"id": "" ,"x": [], "y": [], "center": {"x": "", "y": ""} , "untouchable" : false, "visible" : false, "immovable": false}';

var zoneWidth = $("#dragZone").width();
var zoneHeight = $("#dragZone").height();
var zoneBorderWidth = 3;
var offsetLeft = $("#dragZone").position().left;
var offsetTop = $("#dragZone").position().top;

var objIds = 0;


$("#dragZone").css({"border-style": "solid", "border-width": "3px"});


var convertToPerc = function(point, length){
	return ((point * 100) / length).toFixed(2);
}

var convertToPercY = function(point){
	return convertToPerc(point, zoneHeight);
}

var convertToPercX = function(point){
	return convertToPerc(point, zoneWidth);
}

var convertToPx = function (point, length){
		return (point / 100) * length;
}

var convertToPxY = function (point, length){
		return convertToPx(point, zoneHeight);
}

var convertToPxX = function (point, length){
		return convertToPx(point, zoneWidth);
}


$( "#addButton" ).click(function() {
	var width = $("#inputWidth").val();
	var height = $("#inputHeight").val();
	
	var untouchable = "";
	var visible = "";
	var immovable = "";
	
	if ($("#untouchable").prop( "checked" ))
		untouchable = "untouchable";
	
	if ($("#visible").prop( "checked" ))
		visible = "visible";
	
	if ($("#immovable").prop( "checked" ))
		immovable = "immovable";
	objIds++;
	$("#dragZone").prepend($("<div id = '" +objIds+ "' class='gameObject " + untouchable + " " + visible + " " + immovable + "' style = 'width: " +width + "px; height: " +height + "px;'>"
		+"<div id = 'wrapper" +objIds+ "' style ='position:relative;width: 100%; height: 100%;' > <div style= 'position:absolute; z-index: 10; left: 50%;' > " + objIds+ "</div> </div> </div>"));
	
	
	gameObjectInitialization(objIds);
	
});

$( "#exportData" ).click(function() {
	
	var exportJson = [];
	
	$( ".gameObject" ).each(function(i, ele) {
		var gameObject = JSON.parse(_emptyGameObject);
		
		gameObject.id = this.id;
		
		gameObject.center.x = convertToPercX(position.left - offsetLeft - zoneBorderWidth + ($(this).width() /2) ) ;
		gameObject.center.y = convertToPercY(position.top - offsetTop - zoneBorderWidth + ($(this).height() /2 ) ) ;
		
		gameObject.x.push(convertToPercX($(this).position().left - offsetLeft - zoneBorderWidth ));
		gameObject.x.push(convertToPercX($(this).position().left + $(this).width() - offsetLeft - zoneBorderWidth ));
		gameObject.x.push(convertToPercX($(this).position().left + $(this).width() - offsetLeft - zoneBorderWidth ));
		gameObject.x.push(convertToPercX($(this).position().left - offsetLeft - zoneBorderWidth ));
		gameObject.y.push(convertToPercY($(this).position().top - offsetTop - zoneBorderWidth ));
		gameObject.y.push(convertToPercY($(this).position().top - offsetTop - zoneBorderWidth ));
		gameObject.y.push(convertToPercY($(this).position().top + $(this).height() - offsetTop - zoneBorderWidth ));
		gameObject.y.push(convertToPercY($(this).position().top + $(this).height() - offsetTop - zoneBorderWidth ));
		
		if($(this).hasClass("untouchable"))
			gameObject.untouchable = true;
		else
			gameObject.untouchable = false;
		
		if ($(this).hasClass("visible"))
			gameObject.visible = true;
		else
			gameObject.visible = false;
	
		if ($(this).hasClass("immovable"))
			gameObject.immovable = true;
		else
			gameObject.immovable = false;
		
		exportJson.push(gameObject);
	});
	
	var filename = "levelBuild";
	var blob = new Blob([JSON.stringify(exportJson)], {type: "application/json;charset=utf-8"});
	saveAs(blob, filename+".json");
	
});


$("#importData").click(function(){
	
	var myFile = $('#fileinput').prop('files');
	var files =  $('#fileinput').prop('files');
    var reader = new FileReader();
	
    reader.onload = function () {
        var parsedJson =  JSON.parse (reader.result);
		
		$.each(parsedJson, function(i, ele) {
			
			var left = convertToPxX(ele.x[0]) + offsetLeft + zoneBorderWidth; 
			var top = convertToPxY(ele.y[0]) + offsetTop + zoneBorderWidth; 
			
			var width = convertToPxX(ele.x[1]) - convertToPxX(ele.x[0]); 
			
			var height = convertToPxY(ele.y[3]) - convertToPxY(ele.y[0]);
			
			
			var untouchable = "";
			var visible = "";
			var immovable = "";
			
			if (ele.untouchable)
				untouchable = "untouchable";
			
			if (ele.visible)
				visible = "visible";
			
			if (ele.immovable)
				immovable = "immovable";
			
			$("#dragZone").prepend($("<div id = '" +ele.id+ "' class='gameObject " + untouchable + " " + visible + " " + immovable + "' style = 'width: " +width + "px; height: " +height + "px;'>"
				+"<div id = 'wrapper" +ele.id+ "' style ='position:relative;width: 100%; height: 100%;' > <div style= 'position:absolute; z-index: 10; left: 50%;' > " + ele.id+ "</div> </div> </div>"));
			
			objIds = ele.id;
			
			/* $( "#" + ele.id ).position().left = left;
			$( "#" + ele.id ).position().top = top; */
			
			$( "#" + ele.id ).css({top: top, left: left, position:'absolute'});
			
			gameObjectInitialization(ele.id);
			
		});
		
    };
    reader.readAsText(files[0]);

});


var gameObjectInitialization = function(id){
	
	$( "#" + id ).draggable({
		containment: "parent",
		stop: function(event, ui) {
			position = ui.helper.position();
			
			$( "#pos" ).text( "left: " + (position.left - offsetLeft - zoneBorderWidth) 
				+ " , right: " + (position.left - offsetLeft - zoneBorderWidth + $(this).width() )
				+ ", top: " + (position.top - offsetTop - zoneBorderWidth) 
				+ " , bottom: " + (position.top - offsetTop - zoneBorderWidth + $(this).height()) 
				+ ", center: x " +  (position.left - offsetLeft - zoneBorderWidth + ($(this).width() /2) )  + " y " + (position.top - offsetTop - zoneBorderWidth + ($(this).height() /2 ) ) );
			
			$( "#posPerc" ).text( "left: " + convertToPercX( position.left - offsetLeft - zoneBorderWidth ) 
				+ " , right: " +  convertToPercX(position.left - offsetLeft - zoneBorderWidth + $(this).width() )
				+ ", top: " + convertToPercY( position.top - offsetTop - zoneBorderWidth ) 
				+ " , bottom: " + convertToPercY(position.top - offsetTop - zoneBorderWidth + $(this).height())
				 );
			
		},
		drag: function(event, ui){
			
			position = ui.helper.position();
			
			$( "#pos" ).text( "left: " + (position.left - offsetLeft - zoneBorderWidth) 
				+ " , right: " + (position.left - offsetLeft - zoneBorderWidth + $(this).width() ) 
				+ ", top: " + (position.top - offsetTop - zoneBorderWidth) 
				+ " , bottom: " + (position.top - offsetTop - zoneBorderWidth + $(this).height()) 
				+ ", center: x " +  (position.left - offsetLeft - zoneBorderWidth + ($(this).width() /2) )  + " y " + (position.top - offsetTop - zoneBorderWidth + ($(this).height() /2 ) ) );
			
			$( "#posPerc" ).text( "left: " + convertToPercX( position.left - offsetLeft - zoneBorderWidth ) 
				+ " , right: " +  convertToPercX(position.left - offsetLeft - zoneBorderWidth + $(this).width() )
				+ ", top: " + convertToPercY( position.top - offsetTop - zoneBorderWidth ) 
				+ " , bottom: " + convertToPercY(position.top - offsetTop - zoneBorderWidth + $(this).height())
				 );
				
		}
	});
   	
	$("#" + id).on("mouseover",function(){
		$(this).addClass("markedDeletable");
		$(document).bind("keydown",function(e) {
			var originator = e.keyCode || e.which;
			 if(e.keyCode == 81){
				 $(".markedDeletable").remove();
			 }
			 if(e.keyCode == 76){
				var src = $("#valueInputImage").val();
				var new_img = '<img style = "position: absolute;" src='+src+'>';
				$("#wrapper" + $(".markedDeletable")[0].id).append(new_img);
			 }
			 
		});
	}).on("mouseout",function(){
		$(document).unbind("keydown");
		$(this).removeClass("markedDeletable");
	});

}










  
  