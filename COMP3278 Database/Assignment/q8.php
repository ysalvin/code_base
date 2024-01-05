<!DOCTYPE html>
<html>
<head>
	<title>assignment 2</title>
</head>
<body align=center>
	<h3>Q8_2 Answer</h3>
	<table border='1' align='center'>
		<thead>
			<tr>
				<th>locker_ID</th>
				<th>locker_name</th>
				<th>uncollected_order_count</th>
			</tr>
		</thead>
		<tbody>
			<?php
			// Database connection
			include("connect.php");

			// SQL query
			$sql = "SELECT Locker.locker_ID, Locker.name AS locker_name, COUNT(LockerPickupOrder.order_ID) AS uncollected_order_count
			FROM Locker
			LEFT OUTER JOIN LockerPickupOrder ON Locker.locker_ID = LockerPickupOrder.locker_ID AND LockerPickupOrder.collect_datetime IS NULL
			WHERE Locker.service_area_ID = ".$_GET['service_area_ID']." 
			GROUP BY Locker.locker_ID
			ORDER BY uncollected_order_count DESC;";
			$result = mysqli_query($conn, $sql);

			// Check if data exists
			if (mysqli_num_rows($result) > 0) {
			  // Output data of each row
			  while($row = mysqli_fetch_assoc($result)) {
			    echo "
			    <tr>
				    <td>" . $row["locker_ID"] . "</td>
				    <td> <a href='q9.php?locker_ID=".$row['locker_ID']."'>". $row["locker_name"] . "</a></td> 
				    <td>" . $row["uncollected_order_count"] . "</td> 
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

<!-- SELECT ServiceArea.name service_area_name, COUNT(Locker.locker_ID) AS locker_count
FROM ServiceArea INNER JOIN Locker ON ServiceArea.service_area_ID = locker.service_area_ID
WHERE ServiceArea.service_area_ID NOT IN(
    SELECT ServiceArea.parent_area_ID
    FROM ServiceArea
    WHERE ServiceArea.parent_area_ID IS NOT NULL
)
GROUP BY ServiceArea.service_area_ID
HAVING COUNT(Locker.locker_ID)>=1; -->


<!-- SELECT Locker.locker_ID, Locker.name AS locker_name, COUNT(LockerPickupOrder.order_ID) AS uncollected_order_count
FROM Locker
LEFT OUTER JOIN LockerPickupOrder ON Locker.locker_ID = LockerPickupOrder.locker_ID AND LockerPickupOrder.collect_datetime IS NULL
WHERE Locker.service_area_ID = 12
GROUP BY Locker.locker_ID
ORDER BY uncollected_order_count DESC; -->