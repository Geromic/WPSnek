function getBoard(callbackFunction){
	$.getJSON("GameController", callbackFunction);
}

function resetGame(callbackFunction){
	$.getJSON("GameController", {action: 'restart'}, callbackFunction);
}

function move(dir, callbackFunction){
	$.post("GameController", {direction : dir}, callbackFunction);
}

function getScore(callbackFunction) {
	$.getJSON("GameController", {action: 'score'}, callbackFunction);
}

