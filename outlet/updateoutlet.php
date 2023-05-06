<?php
include '../connect.php';

$response = array();

if (isset($_POST['id']) && isset($_POST['name']) && isset($_POST['address'])) {

    $id = $_POST['id'];
    $name = $_POST['name'];
    $address = $_POST['address'];
    $category = $_POST['category'];

    // Check if new image file is uploaded
    if (isset($_FILES['image']) && $_FILES['image']['size'] > 0) {
        $image_tmp = $_FILES['image']['tmp_name'];
        $image = $_FILES['image']['name'];
        move_uploaded_file($image_tmp, "../image/".$image);
        $image_query = ", image = '".$image."'";
    } else {
        $image_query = "";
    }

    $query = "UPDATE outlet SET name='".$name."', address='".$address."'".$image_query." WHERE id='".$id."'";

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
        'status' => 'FAILED IN ISSET'
    ));
}

header('Content-type: application/json');
echo json_encode($response);
?>
