<?php
include '../connect.php';

if ($conn) {
    $id = $_POST['id'];
    $nama = $_POST['name'];

    $getData = "SELECT * FROM category WHERE id = '$id'";

    if ($nama != "") {
        $result = mysqli_query($conn, $getData);
        $rows = mysqli_num_rows($result);
        $response = array();

        if ($rows > 0) {
            $update = "UPDATE category SET name = '$nama' WHERE id = '$id'";
            $exequery = mysqli_query($conn, $update);

            if ($exequery) {
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
                'status' => 'FAILED'
            ));
        }
    } else {
        array_push($response, array(
            'status' => 'FAILED'
        ));
    }
} else {
    array_push($response, array(
        'status' => 'FAILED'
    ));
}

echo json_encode(array("server_response" => $response));
mysqli_close($conn);
?>