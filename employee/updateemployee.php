<?php
include '../connect.php';

$response = array();

if (isset($_POST['name']) && isset($_POST['phone']) && isset($_POST['job']) && isset($_POST['email']) && isset($_POST['no_pin']) && isset($_POST['in_outlet']) && isset($_FILES['image'])) {

    $name = $_POST['name'];
	$phone = $_POST['phone'];
	$job = $_POST['job'];
	$email = $_POST['email'];
	$in_outlet = $_POST['in_outlet'];
	$no_pin = $_POST['no_pin'];
	$image_tmp = $_FILES['image']['tmp_name'];
	$image = $_FILES['image']['name'];

    // Check if new image file is uploaded
    if (isset($_FILES['image']) && $_FILES['image']['size'] > 0) {
        $image_tmp = $_FILES['image']['tmp_name'];
        $image = $_FILES['image']['name'];
        move_uploaded_file($image_tmp, "../image/".$image);
        $image_query = "image = '".$image."'";
    } else {
        $image_query = "";
    }

    $query = "UPDATE employee SET name='".$name."', phone= '".$phone."', job='".$job."', email='".$email."', no_pin='".$no_pin."', ".$image_query.", in_outlet='".$in_outlet."' WHERE id='".$id."'";

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
