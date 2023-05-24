<?php

include '../connect.php';
$response = array();

if (isset($_POST['name']) && isset($_POST['in_outlet'])){
    $name = $_POST['name'];
    $outlet = $_POST['in_outlet'];

    $query = "INSERT INTO category (name, in_outlet) VALUES ('".$name."', '".$outlet."')";

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