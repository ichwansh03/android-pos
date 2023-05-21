<?php
include '../connect.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    $id = $_POST['id'];
    $name = $_POST['name'];
    $price = $_POST['price'];
    $cat_product = $_POST['cat_product'];
    $merk = $_POST['merk'];
    $description = $_POST['description'];
    $stock = $_POST['stock'];

    $sql = "UPDATE product SET name = '".$name."', price = '".$price."', merk = '".$merk."', cat_product = '".$cat_product."', stock = '".$stock."', description = '".$description."' WHERE id = '".$id."';";

    if (mysqli_query($conn, $sql)) {
        echo json_encode(array('status' => 'OK', 'message' => 'Berhasil Update Data Produk'));
    } else {
        echo json_encode(array('status' => 'KO', 'message' => 'Gagal Update Data Produk'));
    }
    
    mysqli_close($conn);
}
?>