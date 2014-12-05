<?php


// API access key from Google API's Console
define( 'API_ACCESS_KEY', 'AIzaSyD8SSCKE2XsS8GiW_3YbDmptAl_yFExqSw' );


$registrationIds = array("APA91bG7rCwLtgLvVcUTk1pCdA7bXqraQkF-2jfcUOcyzEtoU3AJJjArZZuS6oflYVsfvdhCwdWoIA0-WSerZn4pbp0-2jIp_ZFqPx3NNuGUkeOUuZQ64ZrgXFSei-ONd28rzssqb4Zac816nCOIbDyfMCHB2qbgeg");

// prep the bundle
$msg = array
(
    'message'   => 'Se suspende la clase de mecanica',
    'message_id' => '6',
    'vibrate'   => 1,
    'sound'     => 1
);

$fields = array
(
    'registration_ids'  => $registrationIds,
    'data'              => $msg
);

$headers = array
(
    'Authorization: key=' . API_ACCESS_KEY,
    'Content-Type: application/json'
);

$ch = curl_init();
curl_setopt( $ch,CURLOPT_URL, 'https://android.googleapis.com/gcm/send' );
curl_setopt( $ch,CURLOPT_POST, true );
curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ) );
$result = curl_exec($ch );
curl_close( $ch );

echo $result;
?>