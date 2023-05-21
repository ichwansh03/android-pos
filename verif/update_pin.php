<?php
include '../connect.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    $email = $_POST['email'];
    $no_pin = $_POST['no_pin'];

    $sql = "UPDATE register SET no_pin = '".$no_pin."' WHERE email = '".$email."';";

    if (mysqli_query($conn, $sql)) {
        echo json_encode(array('status' => 'OK', 'message' => 'Berhasil Update Data Produk'));
    } else {
        echo json_encode(array('status' => 'KO', 'message' => 'Gagal Update Data Produk'));
    }
    
    mysqli_close($conn);
}
?>