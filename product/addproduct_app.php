<?php
	include '../connect.php';

	$response = array();

	if (isset($_POST['name']) && isset($_POST['price']) && isset($_POST['cat_product']) && isset($_POST['merk']) && isset($_POST['description']) && isset($_POST['stock']) && isset($_FILES['image'])) {

		$name = $_POST['name'];
		$price = $_POST['price'];
		$cat_product = $_POST['cat_product'];
		$merk = $_POST['merk'];
		$description = $_POST['description'];
		$stock = $_POST['stock'];
		$image_tmp = $_FILES['image']['tmp_name'];
		$image = $_FILES['image']['name'];

		$query = "INSERT INTO product (name, price, merk, cat_product, stock, image, description) 
		VALUES ('".$name."', '".$price."', '".$merk."', '".$cat_product."', '".$stock."', '".$image."', '".$description."')";

		move_uploaded_file($image_tmp, "../image/".$image);
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
