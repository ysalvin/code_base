<!DOCTYPE html>
<html>
<head>
	<title>assignment 2</title>
</head>
<body align=center>
	<h3>Q6 Answer</h3>
	<table border='1' align='center'>
		<thead>
			<tr>
				<th>order_ID</th>
				<th>arrival_datetime</th>
				<th>collect_datetime</th>
				<th>extra_hours</th>
			</tr>
		</thead>
		<tbody>
			<?php
			// Database connection
			include("connect.php");

			// SQL query
			$sql = "SELECT L.order_ID, L.arrival_datetime, L.collect_datetime, (TIMESTAMPDIFF(HOUR, L.arrival_datetime, L.collect_datetime)-48) AS extra_hours
			FROM LockerPickupOrder L
			WHERE TIMESTAMPDIFF(HOUR, L.arrival_datetime, L.collect_datetime) > 48
			ORDER BY TIMESTAMPDIFF(HOUR, L.arrival_datetime, L.collect_datetime) DESC, L.order_ID ASC; ";

			$result = mysqli_query($conn, $sql);

			// Check if data exists
			if (mysqli_num_rows($result) > 0) {
			  // Output data of each row
			  while($row = mysqli_fetch_assoc($result)) {
			    echo "
			    <tr>
				    <td>" . $row["order_ID"] . "</td>
				    <td>" . $row["arrival_datetime"] . "</td> 
				    <td>" . $row["collect_datetime"] . "</td> 
				    <td>" . $row["extra_hours"] . "</td>
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

<!-- SELECT L.order_ID, L.arrival_datetime, L.collect_datetime, (TIMESTAMPDIFF(HOUR, L.arrival_datetime, L.collect_datetime)-48) AS extra_hours
FROM LockerPickupOrder L
WHERE TIMESTAMPDIFF(HOUR, L.arrival_datetime, L.collect_datetime) > 48
ORDER BY TIMESTAMPDIFF(HOUR, L.arrival_datetime, L.collect_datetime) DESC, L.order_ID ASC; -->