<?php
    if($_GET){
        $streetaddress = $_GET["streetaddress"];
        $city = $_GET["city"];
        $state = $_GET["state"];
        $degree = $_GET["degree"];
        $googleAPIKey = "AIzaSyA3bAQ5Nci8Z_DYogwSl5p6K7Aul3rCJ4I";
        $googleMap = "https://maps.google.com/maps/api/geocode/xml?address=".$_GET["streetaddress"].",".$_GET["city"].",".$_GET["state"]."&key=".$googleAPIKey;
        $xml = new SimpleXMLElement($googleMap, NULL, TRUE);
        if($xml->status == "ZERO_RESULTS"){
            echo '
                <script>alert("No Result. Insert correct location");</script>
            ';
        } else {
            $forecastAPIKey = 'd12efb56a06288bfaf6e66dfe0da5958';
            $latitude = $xml->result->geometry->location->lat;
            $longitude = $xml->result->geometry->location->lng;
            if($_GET["degree"] == 'fahrenheit'){
                $units = 'us';
            } else if ($_GET["degree"] == 'celsius'){
                $units = 'si';
            }
            $exclude = 'flags';
            $forecast = "https://api.forecast.io/forecast/".$forecastAPIKey."/".$latitude.",".$longitude."?units=".$units."&exclude=".$exclude;
            $json = file_get_contents($forecast);
            $obj = json_decode($json);
            echo $json;
        }
    }
?>