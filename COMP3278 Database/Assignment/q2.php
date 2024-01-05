<!DOCTYPE html>
<html>
<head>
	<title>assignment 2</title>
</head>
<body align=center>
	<h3>Q2 Answer</h3>
	<table border='1' align='center'>
		<thead>
			<tr>
				<th>order_ID</th>
				<th>address</th>
				<th>member_ID</th>
				<th>name</th>
			</tr>
		</thead>
		<tbody>
			<?php
			// Database connection
			include("connect.php");

			// SQL query
			$sql = "SELECT home.order_ID, home.address, M.member_ID, M.name
			FROM HomeDeliveryOrder home INNER JOIN GroupOrder G ON home.order_ID = G.order_ID
			INNER JOIN Member M ON G.member_ID = M.member_ID
			WHERE home.address LIKE '%Hong Kong Island%'
			ORDER BY ABS(home.order_ID) ASC";
			$result = mysqli_query($conn, $sql);

			// Check if data exists
			if (mysqli_num_rows($result) > 0) {
			  // Output data of each row
			  while($row = mysqli_fetch_assoc($result)) {
			    echo "
			    <tr>
				    <td>" . $row["order_ID"] . "</td>
				    <td>" . $row["address"] . "</td> 
				    <td>" . $row["member_ID"] . "</td> 
				    <td>" . $row["name"] . "</td>
			    </tr>";
			  }
			} else {
			  echo "0 results";
			}

			mysqli_close($conn);
			?>
		</tbody>
	</table>
	<br><a href='index.html'>back</a>
</body>
</html>
<!-- 
SELECT home.order_ID, home.address, M.member_ID, M.name
FROM HomeDeliveryOrder home INNER JOIN GroupOrder G ON home.order_ID = G.order_ID
INNER JOIN Member M ON G.member_ID = M.member_ID
WHERE home.address LIKE "%Hong Kong Island%"  
ORDER BY ABS(home.order_ID) ASC; -->