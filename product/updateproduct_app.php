<?php
include '../connect.php';

$response = array();

if (isset($_POST['id']) && isset($_POST['name']) && isset($_POST['price']) && isset($_POST['cat_product']) && isset($_POST['merk']) && isset($_POST['description']) && isset($_POST['stock'])) {

    $id = $_POST['id'];
    $name = $_POST['name'];
    $price = $_POST['price'];
    $cat_product = $_POST['cat_product'];
    $merk = $_POST['merk'];
    $description = $_POST['description'];
    $stock = $_POST['stock'];

    // Check if new image file is uploaded
    if (isset($_FILES['image']) && $_FILES['image']['size'] > 0) {
        $image_tmp = $_FILES['image']['tmp_name'];
        $image = $_FILES['image']['name'];
        move_uploaded_file($image_tmp, "../image/".$image);
        $image_query = "image = '".$image."'";
    } else {
        $image_query = "";
    }

    $query = "UPDATE product SET name='".$name."', price='".$price."', merk='".$merk."', cat_product='".$cat_product."', stock='".$stock."', ".$image_query.", description='".$description."' WHERE id='".$id."'";

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
