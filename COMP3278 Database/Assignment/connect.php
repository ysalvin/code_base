<?php

$host = "localhost"; // In CS department, the database is located in a machine called sophia.
$username="root"; // Your CSID.
$password=""; // Your MySQL password.
$database="Assignment 2"; // In CS department, we create a database for you with name equal to your CSID.


$conn = mysqli_connect($host,$username,$password);

mysqli_select_db($conn, $database) or die( "Unable to select database: ".mysqli_error($conn));

?>
