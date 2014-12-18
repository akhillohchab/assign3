<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <title>TwitMap_v2</title>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?libraries=visualization&sensor=true_or_false"></script> 
    <script src="./static/markerclusterer.js" type="text/javascript"></script>
	<script src="./static/twittmap.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href="./static/twittmap.css">
	
  </head>
 
	<body>
		<div id="panel">
			<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Choose a keyword:&nbsp;
				<form name="Form1" action='Twts' method='get' target="map">
					<select class="selectpicker" id="keyword">
						<option value="isis">Isis</option>
						<option value="NFL">NFL</option>
						<option value="ebola">Ebola</option>
						<option value="Interstellar">Interstellar</option>
						<option value="Thanksgiving">Thanksgiving</option>
						<option value="halloween">Halloween</option>
						<option value="winter">Winter</option>
						<option value="NYC">NYC</option>
						<option value="obama">Obama</option>
					</select>&nbsp;
					<button type="submit" class="btn" id="go" onclick="return TWonClick();">Plot!</button>
				</form>
			</p>
		</div>
		<div id="map"></div>
		<div id="sentiment"></div>
	</body>
</html>