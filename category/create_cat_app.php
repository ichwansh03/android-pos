<?php

include '../connect.php';
$response = array();

if (isset($_POST['name'])){
    $name = $_POST['name'];

    $query = "INSERT INTO category (name) VALUES ('".$name."')";

    $result = mysqli_query($conn, $query);

    if ($result) {
        array_push($response, array(
            'status' => 'OK'
        ));
    } else {
        array_push($response, array(
            'status' => 'FAILED'
        ));
    }
} else {
    array_push($response, array(
        'status' => 'FAILED IN ASSET'
    ));
}

header('Content-type: application/json');
echo json_encode($response);
?>