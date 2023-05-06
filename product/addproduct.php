<?php
include '../connect.php';

$target_path = dirname(__FILE__).'../image/';
	$_BASE_URL = 'http://192.168.43.8/pos/image/';

	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		//Mendapatkan Nilai Variable
		$name = $_POST['name'];
		$price = $_POST['price'];
		$cat_product = $_POST['cat_product'];
		$merk = $_POST['merk'];
		$description = $_POST['description'];
		$stock = $_POST['stock'];

		$target_path = $target_path . basename($_FILES['image']['name']);

		try{
			if(!move_uploaded_file($_FILES['image']['tmp_name'], $target_path)){
				echo json_encode(array('status'=>'KO', 'message'=> 'Image gagal diupload'));
			}else {
				$sql = "INSERT INTO product (name, price, merk, cat_product, stock, image, description) VALUES ('$name', '$price', '$merk', '$cat_product', '$stock', '".$_BASE_URL.basename($_FILES['image']['name']) ."', '$description',)";

				//Eksekusi Query database
				if (mysqli_query($con, $sql)) {
					echo json_encode(array('status' => 'OK', 'message' => 'Berhasil Menambahkan Pegawai'));
				} else {
					echo json_encode(array('status' => 'KO', 'message' => 'Gagal Menambahkan Pegawai'));
				}
				mysqli_close($con);
			}

		} catch(Exception $e){
			echo json_encode(array('status'=>'KO', 'message'=> $e->getMessage()));
		}
		
	}
?>