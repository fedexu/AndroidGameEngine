function drawnElementManager(){
    var elementList = [];    
    var fixedName = "builderElement_";

    this.insert = function(){
        var name = fixedName + elementList.length;
        elementList.push(new drawnElement(name));
    };

    this.retrieve = function(index){        
        return elementList.pop(index);
    };

    this.rotateAllObjects = function(){

    };
}