<!DOCTYPE html>
<html>
<head>
	<title>assignment 2</title>
</head>
<body align=center>
	<h3>Q4 Answer</h3>
	<table border='1' align='center'>
		<thead>
			<tr>
				<th>member_ID</th>
				<th>name</th>
				<th>service_count</th>
			</tr>
		</thead>
		<tbody>
			<?php
			// Database connection
			include("connect.php");

			// SQL query
			$sql = "SELECT M.member_ID, M.name, COUNT(L.order_ID) AS service_count
			FROM Member M LEFT OUTER JOIN GroupOrder G ON M.member_ID = G.member_ID
			LEFT OUTER JOIN LockerPickupOrder L ON L.order_ID = G.order_ID
			GROUP By member_ID
			HAVING COUNT(L.order_ID) < 3  
			ORDER BY ABS(COUNT(L.order_ID)) DESC, ABS(M.member_ID) ASC;";
			$result = mysqli_query($conn, $sql);

			// Check if data exists
			if (mysqli_num_rows($result) > 0) {
			  // Output data of each row
			  while($row = mysqli_fetch_assoc($result)) {
			    echo "
			    <tr>
				    <td>" . $row["member_ID"] . "</td>
				    <td>" . $row["name"] . "</td> 
				    <td>" . $row["service_count"] . "</td> 
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



<!-- SELECT M.member_ID, M.name, COUNT(L.order_ID) AS service_count
FROM Member M LEFT OUTER JOIN GroupOrder G ON M.member_ID = G.member_ID
LEFT OUTER JOIN LockerPickupOrder L ON L.order_ID = G.order_ID
GROUP By member_ID
HAVING COUNT(L.order_ID) < 3  
ORDER BY ABS(COUNT(L.order_ID)) DESC, ABS(M.member_ID) ASC; -->