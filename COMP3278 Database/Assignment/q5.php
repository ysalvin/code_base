<!DOCTYPE html>
<html>
<head>
	<title>assignment 2</title>
</head>
<body align=center>
	<h3>Q5 Answer</h3>
	<table border='1' align='center'>
		<thead>
			<tr>
				<th>order_ID</th>
				<th>total_weight</th>
			</tr>
		</thead>
		<tbody>
			<?php
			// Database connection
			include("connect.php");

			// SQL query
			$sql = "SELECT G.order_ID, SUM(P.weight)AS total_weight
			FROM GroupOrder G INNER JOIN Package P ON G.order_ID = P.order_ID
			GROUP BY G.order_ID
			HAVING SUM(P.weight) > 10
			ORDER BY SUM(P.weight) DESC, G.order_ID ASC;";

			$result = mysqli_query($conn, $sql);

			// Check if data exists
			if (mysqli_num_rows($result) > 0) {
			  // Output data of each row
			  while($row = mysqli_fetch_assoc($result)) {
			    echo "
			    <tr>
				    <td>" . $row["order_ID"] . "</td>
				    <td>" . $row["total_weight"] . "</td> 
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

<!-- SELECT G.order_ID, SUM(P.weight)AS total_weight
FROM GroupOrder G INNER JOIN Package P ON G.order_ID = P.order_ID
GROUP BY G.order_ID
HAVING SUM(P.weight) > 10
ORDER BY SUM(P.weight) DESC, G.order_ID ASC; -->

