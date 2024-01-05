<!DOCTYPE html>
<html>
<head>
	<title>assignment 2</title>
</head>
<body align=center>
	<h3>Q9 Answer</h3>
	<table border='1' align='center'>
		<thead>
			<tr>
				<th>locker_ID</th>
				<th>cell_number</th>
				<th>member_ID</th>
				<th>name</th>
                <th>contact_number</th>
				<th>package_count</th>
			</tr>
		</thead>
		<tbody>
			<?php
			// Database connection
			include("connect.php");

			// SQL query
			$sql = "SELECT  LockerPickupOrder.locker_ID, LockerPickupOrder.cell_number, Member.member_ID, Member.name, Member.contact_number, COUNT(Package.package_ID) AS package_count
			FROM LockerPickupOrder 
			INNER JOIN GroupOrder ON LockerPickupOrder.order_ID = GroupOrder.order_ID AND LockerPickupOrder.collect_datetime IS NULL AND LockerPickupOrder.locker_ID=".$_GET['locker_ID']." 
			INNER JOIN Member ON Member.member_ID = GroupOrder.member_ID
			INNER JOIN Package ON Package.order_ID = GroupOrder.order_ID 
			GROUP BY LockerPickupOrder.locker_ID
			HAVING COUNT(Package.package_ID)>0
			ORDER BY package_count DESC;";
			$result = mysqli_query($conn, $sql);

			// Check if data exists
			if (mysqli_num_rows($result) > 0) {
			  // Output data of each row
			  while($row = mysqli_fetch_assoc($result)) {
			    echo "
			    <tr>
				    <td>" . $row["locker_ID"] . "</td>
				    <td>" . $row["cell_number"] . "</td> 
				    <td>" . $row["member_ID"] . "</td> 
				    <td>" . $row["name"] . "</td>
                    <td>" . $row["contact_number"] . "</td>
				    <td>" . $row["package_count"] . "</td>
			    </tr>";
			  }
			} 

			mysqli_close($conn);
			?>
		</tbody>
	</table>
</body>
</html>


<!-- SELECT  LockerPickupOrder.locker_ID, LockerPickupOrder.cell_number, Member.member_ID, Member.name, Member.contact_number, COUNT(Package.package_ID) AS package_count
FROM LockerPickupOrder 
INNER JOIN GroupOrder ON LockerPickupOrder.order_ID = GroupOrder.order_ID AND LockerPickupOrder.collect_datetime IS NULL AND LockerPickupOrder.locker_ID=5
INNER JOIN Member ON Member.member_ID = GroupOrder.member_ID
INNER JOIN Package ON Package.order_ID = GroupOrder.order_ID 
GROUP BY LockerPickupOrder.locker_ID
HAVING COUNT(Package.package_ID)>0
ORDER BY package_count DESC;-->