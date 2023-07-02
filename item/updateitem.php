<?php
include '../connect.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    $id = $_POST['id'];
    $name = $_POST['name'];
    $price = $_POST['price'];
    $quantity = $_POST['quantity'];
  
    $sql = "UPDATE cart_item SET name = '".$name."', price = '".$price."', quantity = '".$quantity."' WHERE id = '$id';";

    if (mysqli_query($conn, $sql)) {
        echo json_encode(array('status' => 'OK', 'message' => 'Berhasil Update Data Produk'));
    } else {
        echo json_encode(array('status' => 'KO', 'message' => 'Gagal Update Data Produk'));
    }
    
    mysqli_close($conn);
}

?>
