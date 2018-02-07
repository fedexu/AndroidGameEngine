
var _emptyGameObject = '{"id": "" ,"x": [], "y": [], "center": {"x": "", "y": ""} , "untouchable" : false, "visible" : false, "immovable": false}';

var zoneWidth = $("#dragZone").width();
var zoneHeight = $("#dragZone").height();
var zoneBorderWidth = 3;
var offsetLeft = $("#dragZone").offset().left;	//offset: current position relative to the document. Must be updated on change
var offsetTop = $("#dragZone").offset().top;

var objIds = 0;

var drawnElements = new drawnElementManager();


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
	drawnElements.insert();
	$("#dragZone").prepend($("<div id = '" +objIds+ "' class='gameObject " + untouchable + " " + visible + " " + immovable + "' style = 'width: " +width + "px; height: " +height + "px; z-index: " + objIds+" ;'>"
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
		containment: "#dragZone",
		stop: function(event, ui) {
			position = ui.helper.position();
			
			$( "#pos" ).html( " <br> " + "left: " + (position.left - offsetLeft).toFixed(2) 
				+ " right: " + (position.left - offsetLeft  + $(this).width() ).toFixed(2)
				+" <br> "
				+ "top: " + (position.top - offsetTop ).toFixed(2) 
				+ " , bottom: " + (position.top - offsetTop  + $(this).height()).toFixed(2) 
				+" <br> "
				+ "Center: x " +  (position.left - offsetLeft  + ($(this).width() /2) ).toFixed(2)  
				+ " y " + (position.top - offsetTop  + ($(this).height() /2 ) ).toFixed(2) );
			
			$( "#posPerc" ).html( " <br> " + "left: " + convertToPercX( position.left - offsetLeft  ) 
				+ " , right: " +  convertToPercX(position.left - offsetLeft  + $(this).width() )
				+" <br> "
				+ "top: " + convertToPercY( position.top - offsetTop  ) 
				+ " , bottom: " + convertToPercY(position.top - offsetTop  + $(this).height())
				 );
			
		},
		drag: function(event, ui){
			
			position = ui.helper.position();
			
			$( "#pos" ).html( " <br> " + "left: " + (position.left - offsetLeft ).toFixed(2) 
				+ " right: " + (position.left - offsetLeft  + $(this).width() ).toFixed(2) 
				+" <br> "
				+ "top: " + (position.top - offsetTop ).toFixed(2) 
				+ " , bottom: " + (position.top - offsetTop  + $(this).height()).toFixed(2) 
				+" <br> "
				+ "Center: x " +  (position.left - offsetLeft  + ($(this).width() /2) ).toFixed(2) 
				+ " y " + (position.top - offsetTop  + ($(this).height() /2 ) ).toFixed(2) );
			
			$( "#posPerc" ).html( " <br> " + "left: " + convertToPercX( position.left - offsetLeft  ) 
				+ " , right: " +  convertToPercX(position.left - offsetLeft  + $(this).width() )
				+" <br> "
				+ "top: " + convertToPercY( position.top - offsetTop  ) 
				+ " , bottom: " + convertToPercY(position.top - offsetTop  + $(this).height())
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
				
				var src = $('#valueInputImage').prop('files');
				var reader = new FileReader();

				reader.onload = function(e) {
					var new_img = '<img style = "position: absolute;" src='+e.target.result+'>';
					$("#wrapper" + $(".markedDeletable")[0].id).append(new_img);
				}

				reader.readAsDataURL(src[0]);
				
			 }
			 
		});
	}).on("mouseout",function(){
		$(document).unbind("keydown");
		$(this).removeClass("markedDeletable");
	});

}

function changeScale(x, srcMin, srcMax, trgMin, trgMax){
	var unitaryScale = (x - srcMin) / (srcMax - srcMin);
	var newRange = (trgMax - trgMin);

	return newRange * unitaryScale + trgMin;
}

$( "#rotate-image" ).click(function() {
	var prevContainerWidth = $("#dragZoneContainer").width();
	var prevContainerHeight = $("#dragZoneContainer").height();
	
	zoneHeight = prevContainerWidth;
	zoneWidth = prevContainerHeight;
	
	var oldOffsetLeft = offsetLeft;
	var oldOffsetTop = offsetTop;
	
	//necessary cause the resizing of the container may force an item on the border to be moved inside the new area
	var elemOldPos = [];
	for(let i=1; i <= objIds; i++){
		let element = $("#" + i);
		let pos = {
			x: (element.position().left - oldOffsetLeft),
			y: (element.position().top - oldOffsetTop)
		};
		elemOldPos.push(pos);
	}

	//swap size
	$("#dragZoneContainer").width(prevContainerHeight);
	$("#dragZoneContainer").height(prevContainerWidth);	

	offsetLeft = ($("#dragZone").offset().left);
	offsetTop = ($("#dragZone").offset().top);

	//update each object
	for(let i=1; i <= objIds; i++){
		let element = $("#" + i);			
		let oldElpos = elemOldPos[i-1];

		//change scale from old to new space
		let newPosX = changeScale(oldElpos.x, 0, prevContainerWidth, 0, prevContainerHeight);
		let newPosY = changeScale(oldElpos.y, 0, prevContainerHeight, 0, prevContainerWidth);
		
		console.log("OLD ", oldElpos.x, oldElpos.y);
		console.log("NEW ", newPosX, newPosY);
		//console.log("OFFSETS", oldOffsetLeft, oldOffsetTop);

		//update pos
		element.css({left: newPosX + offsetLeft, top: newPosY + offsetTop});
		
	}
	
});








  
  