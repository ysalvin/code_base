<!DOCTYPE html>
<html>
<head>
	<title>assignment 2</title>
</head>
<body align=center>
	<h3>Q1 Answer</h3>
	<table border='1' align='center'>
		<thead>
			<tr>
				<th>member_ID</th>
				<th>name</th>
				<th>email</th>
				<th>contact_number</th>
			</tr>
		</thead>
		<tbody>
			<?php
			// Database connection
			include("connect.php");

			// SQL query
			$sql = "SELECT member_ID, name, email, contact_number FROM Member WHERE LOWER(name) LIKE '%tom%' ORDER BY member_ID DESC";
			$result = mysqli_query($conn, $sql);

			// Check if data exists
			if (mysqli_num_rows($result) > 0) {
			  // Output data of each row
			  while($row = mysqli_fetch_assoc($result)) {
			    echo "
			    <tr>
				    <td>" . $row["member_ID"] . "</td>
				    <td>" . $row["name"] . "</td> 
				    <td>" . $row["email"] . "</td> 
				    <td>" . $row["contact_number"] . "</td>
			    </tr>";
			  }
			} else {
			  echo "0 results";
			}

			mysqli_close($conn);
			?>
		</tbody>
	</table>
</body>
</html>
