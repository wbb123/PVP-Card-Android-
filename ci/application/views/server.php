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
	<h1>Registration</h1>

	<form enctype='application/json' action="http://i.cs.hku.hk/~zqshi/ci/index.php/Server/register" method="post">
	  <p>UserName: <input type="text" name="UserName" /></p>
	  <p>Password: <input type="text" name="Password" /></p>
	  <p>Nickname: <input type="text" name="Nickname" /></p>
	  <p>Alliance: <input type="text" name="Alliance" /></p>
	  <input type="submit" value="Register" />
	</form>
</div>

<div id="container">
	<h1>Login</h1>


	<form enctype='application/json' action="http://i.cs.hku.hk/~zqshi/ci/index.php/Server/login" method="post">
	  <p>UserName: <input type="text" name="UserName" /></p>
	  <p>Password: <input type="text" name="Password" /></p>
	  <input type="submit" value="Login" />
	</form>


</div>

<div id="container">
	<h1>GetUserCards</h1>


	<form enctype='application/json' action="http://i.cs.hku.hk/~zqshi/ci/index.php/Server/getUserCards" method="post">
	  <p>UserID: <input type="text" name="UserID" /></p>
	  <input type="submit" value="GetUserCards" />
	</form>


</div>

<div id="container">
	<h1>updateUserStep</h1>


	<form enctype='application/json' action="http://i.cs.hku.hk/~zqshi/ci/index.php/Server/updateUserStep" method="post">
	  <p>UserID: <input type="text" name="UserID" /></p>
	  <p>WalkDistance: <input type="text" name="WalkDistance" /></p>
	  <input type="submit" value="updateUserStep" />
	</form>


</div>

<div id="container">
	<h1>updateTargetLocationID</h1>

	<form enctype='application/json' action="http://i.cs.hku.hk/~zqshi/ci/index.php/Server/updateTargetLocationID" method="post">
	  <p>UserID: <input type="text" name="UserID" /></p>
	  <p>TargetLocationID: <input type="text" name="TargetLocationID" /></p>
	  <input type="submit" value="updateTargetLocationID" />
	</form>


</div>

<div id="container">
	<h1>updateCurrentLocationID</h1>

	<form enctype='application/json' action="http://i.cs.hku.hk/~zqshi/ci/index.php/Server/updateCurrentLocationID" method="post">
	  <p>UserID: <input type="text" name="UserID" /></p>
	  <p>CurrentLocationID: <input type="text" name="CurrentLocationID" /></p>
	  <input type="submit" value="updateCurrentLocationID" />
	</form>

</div>

<div id="container">
	<h1>updateCurrentPosition</h1>

	<form enctype='application/json' action="http://i.cs.hku.hk/~zqshi/ci/index.php/Server/updateCurrentPosition" method="post">
	  <p>UserID: <input type="text" name="UserID" /></p>
	  <p>CurrentPositionX: <input type="text" name="CurrentPositionX" /></p>
		<p>CurrentPositionY: <input type="text" name="CurrentPositionY" /></p>
	  <input type="submit" value="updateCurrentPosition" />
	</form>

</div>

<div id="container">
	<h1>updateUserCardRelation</h1>

	<form enctype='application/json' action="http://i.cs.hku.hk/~zqshi/ci/index.php/Server/updateUserCardRelation" method="post">
	  <p>UserID: <input type="text" name="UserID" /></p>
	  <p>CardID: <input type="text" name="CardID" /></p>
	  <input type="submit" value="updateUserCardRelation" />
	</form>

</div>



</body>
</html>
