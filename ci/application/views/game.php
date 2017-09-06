<?php
defined('BASEPATH') OR exit('No direct script access allowed');
?><!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<title>Welcome to CodeIgniter</title>

	<style type="text/css">

	::selection { background-color: #E13300; color: white; }
	::-moz-selection { background-color: #E13300; color: white; }

	body {
		background-color: #fff;
		margin: 40px;
		font: 13px/20px normal Helvetica, Arial, sans-serif;
		color: #4F5155;
	}

	a {
		color: #003399;
		background-color: transparent;
		font-weight: normal;
	}

	h1 {
		color: #444;
		background-color: transparent;
		border-bottom: 1px solid #D0D0D0;
		font-size: 19px;
		font-weight: normal;
		margin: 0 0 14px 0;
		padding: 14px 15px 10px 15px;
	}

	code {
		font-family: Consolas, Monaco, Courier New, Courier, monospace;
		font-size: 12px;
		background-color: #f9f9f9;
		border: 1px solid #D0D0D0;
		color: #002166;
		display: block;
		margin: 14px 0 14px 0;
		padding: 12px 10px 12px 10px;
	}

	#body {
		margin: 0 15px 0 15px;
	}

	p.footer {
		text-align: right;
		font-size: 11px;
		border-top: 1px solid #D0D0D0;
		line-height: 32px;
		padding: 0 10px 0 10px;
		margin: 20px 0 0 0;
	}

	#container {
		margin: 10px;
		border: 1px solid #D0D0D0;
		box-shadow: 0 0 8px #D0D0D0;
	}
	</style>
</head>
<body>

<div id="container">
	<h1>applyForFight</h1>

	<form enctype='application/json' action="http://i.cs.hku.hk/~zqshi/ci/index.php/Game/applyForFight" method="post">
	  <p>UserID: <input type="text" name="UserID" /></p>
	  <input type="submit" value="applyForFight" />
	</form>
</div>

<div id="container">
	<h1>isRoomReady</h1>

	<form enctype='application/json' action="http://i.cs.hku.hk/~zqshi/ci/index.php/Game/isRoomReady" method="post">
	  <p>RoomID: <input type="text" name="RoomID" /></p>
	  <input type="submit" value="isRoomReady" />
	</form>
</div>

<div id="container">
	<h1>setCards</h1>

	<form enctype='application/json' action="http://i.cs.hku.hk/~zqshi/ci/index.php/Game/setCards" method="post">
	  <p>RoomID: <input type="text" name="RoomID" /></p>
	  <p>UserID: <input type="text" name="UserID" /></p>
	  <p>CardID1: <input type="text" name="CardID1" /></p>
	  <p>CardID2: <input type="text" name="CardID2" /></p>
	  <p>CardID3: <input type="text" name="CardID3" /></p>

	  <input type="submit" value="setCards" />
	</form>
</div>

<div id="container">
	<h1>isFightReady</h1>

	<form enctype='application/json' action="http://i.cs.hku.hk/~zqshi/ci/index.php/Game/isFightReady" method="post">
	  <p>RoomID: <input type="text" name="RoomID" /></p>
	  <input type="submit" value="isFightReady" />
	</form>
</div>
<div id="container">
	<h1>playCard</h1>
	<form enctype='application/json' action="http://i.cs.hku.hk/~zqshi/ci/index.php/Game/playCard" method="post">
	  <p>RoomID: <input type="text" name="RoomID" /></p>
	  <p>UserID: <input type="text" name="UserID" /></p>
	  <p>Player1CardID: <input type="text" name="Player1CardID" /></p>
	  <p>Player2CardID: <input type="text" name="Player2CardID" /></p>
	  <p>Player1CardNum: <input type="text" name="Player1CardNum" /></p>
	  <p>Player2CardNum: <input type="text" name="Player2CardNum" /></p>
	  <p>Player: <input type="text" name="Player" /></p>
	  <input type="submit" value="playCard" />
	</form>
</div>

<div id="container">
	<h1>myTurn</h1>
	<form enctype='application/json' action="http://i.cs.hku.hk/~zqshi/ci/index.php/Game/myTurn" method="post">
	  <p>RoomID: <input type="text" name="RoomID" /></p>
	  <p>UserID: <input type="text" name="UserID" /></p>
	  <input type="submit" value="myTurn" />
	</form>
</div>

<div id="container">
	<h1>getTime</h1>
	<form enctype='application/json' action="http://i.cs.hku.hk/~zqshi/ci/index.php/Game/getTime" method="post">
	  <input type="submit" value="getTime" />
	</form>
</div>
</body>
</html>