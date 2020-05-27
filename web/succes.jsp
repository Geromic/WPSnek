<%@ page import="websnake.domain.User" %><%--
  Created by IntelliJ IDEA.
  User: forest
  Date: 16.12.2014
  Time: 10:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="css/board.css">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
    <style>
        .asset-name {
            background-color: cornflowerblue;
            border-right: solid 1px black;
        }
    </style>
    <script src="js/jquery-2.0.3.js"></script>
    <script src="js/ajax-utils.js"></script>
</head>
<body>
<%! User user; %>
<%  user = (User) session.getAttribute("user");
    if (user != null) {
        out.println("Welcome "+user.getUsername());
%>
        <br/>
        <section><div id="score"></div></section>
        <section><div id="time">Time: 00:00:00</div></section>
        <section><table class="snakeBoard" id="board"></table></section>
        <section><button id="restartBtn">Restart</button></section>
        <section><div id="msg"></div></section>

        <script>
            var start = null;
            var lastMove = null;

            function pad(num, size) {
                var s = num+"";
                while (s.length < size) s = "0" + s;
                return s;
            }

            $(document).ready(function(){
                function displayErr() {
                    start= null;
                    lastMove = null;
                    if(confirm("Game over!\nRestart?")){
                        resetGame(cells => populateMatrix(cells));
                        getScore(score => updateScore(score));
                        console.log("Game restarted");
                    }
                    else{
                        console.log("No reset");
                    }
                }

                $("#restartBtn").click(function () {
                    if(confirm("Are you sure?")){
                        resetGame(cells => populateMatrix(cells));
                        start = null;
                        lastMove = null;
                        getScore(score => updateScore(score));
                        console.log("Reset pressed");
                    }
                    else{
                        console.log("Make up ur mind");
                    }
                });

                function populateMatrix(cells){
                    const arr = jQuery.parseJSON(JSON.stringify(cells));

                    $("#board").html("");
                    var i;
                    for(i=0; i<10; i++){
                        var j;
                        var row = "<tr>";
                        for(j=0; j<20; j++){
                            var val = 0;
                            for(let k = 0; k < arr.length; k++){
                                if(arr[k].x === i && arr[k].y === j){
                                    val = arr[k].val;
                                }
                            }
                            switch(val){
                                case 1: row+="<td class='obstacle'>11</td>";break;
                                case 2: row+="<td class='food'>22</td>";break;
                                case 3: row+="<td class='snake'>33</td>";break;
                                case 0: row+="<td class='clear'>00</td>";break;
                            }
                        }
                        $("#board").append(row + "</tr>");
                    }
                }

                setInterval(function () {
                    if(lastMove == null)
                        return;
                    if(new Date() - lastMove >= 1000){
                        move("NONE", msg => displayErr());
                        getBoard(cells => populateMatrix(cells));
                        getScore(score => updateScore(score));
                    }
                }, 1000);

                setInterval(function(){
                    if(start === null){
                        $("#time").html("");
                        $("#time").append("Time: 00:00:00");
                        return;
                    }
                    else{
                        var elapsed = new Date() - start;
                        var sec = pad(Math.trunc(elapsed/1000)%60, 2);
                        var min = pad(Math.trunc(elapsed/(1000*60))%60, 2);
                        var hour = pad(Math.trunc(elapsed/(1000*60*60))%60, 2);
                        $("#time").html("");
                        $("#time").append("Time: " + hour + ":" + min + ":" + sec);
                    }
                }, 1000);

                function updateScore(score){
                    const scr = jQuery.parseJSON(JSON.stringify(score));
                    $("#score").html("");
                    $("#score").append("Score: " + scr.score);
                }

                getBoard(cells => populateMatrix(cells));
                getScore(score => updateScore(score));

                window.addEventListener("keydown", function (event) {
                    if (event.defaultPrevented) {
                        return;
                    }

                    if(start === null){
                        start = new Date();
                    }

                    console.log(start);
                    switch (event.key) {
                        case "ArrowDown": lastMove= new Date();move("DOWN", msg => displayErr());getBoard(cells => populateMatrix(cells));getScore(score => updateScore(score));break;
                        case "ArrowUp": lastMove= new Date();move("UP", msg => displayErr());getBoard(cells => populateMatrix(cells));getScore(score => updateScore(score));break;
                        case "ArrowLeft": lastMove= new Date();move("LEFT", msg => displayErr());getBoard(cells => populateMatrix(cells));getScore(score => updateScore(score));break;
                        case "ArrowRight": lastMove= new Date();move("RIGHT", msg => displayErr());getBoard(cells => populateMatrix(cells));getScore(score => updateScore(score));break;
                        default:return;
                    }
                    event.preventDefault();
                }, true);
            });
        </script>
<%
    }
%>

</body>
</html>