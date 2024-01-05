<!DOCTYPE html>
<html>
<head>
	<title>assignment 2</title>
</head>
<body align=center>
	<h3>Q7 Answer</h3>
	<table border='1' align='center'>
		<thead>
			<tr>
				<th>order_ID</th>
				<th>locker_name</th>
				<th>service_area_name</th>
			</tr>
		</thead>
		<tbody>
			<?php
			// Database connection
			include("connect.php");

			// SQL query
			$sql = "SELECT LockerPickupOrder.order_ID, Locker.name AS locker_name, ServiceArea.name AS service_area_name
			FROM LockerPickupOrder INNER JOIN Locker ON LockerPickupOrder.locker_ID = locker.locker_ID
			INNER JOIN ServiceArea ON ServiceArea.service_area_ID = Locker.service_area_ID
			WHERE Locker.service_area_ID IN(
					SELECT ServiceArea.service_area_ID
					FROM ServiceArea 
					WHERE ServiceArea.name LIKE 'Hong Kong Island'
					)
					OR
					ServiceArea.service_area_ID IN(
						SELECT ServiceArea.service_area_ID
						FROM ServiceArea
						WHERE ServiceArea.parent_area_ID IN
							(SELECT ServiceArea.service_area_ID
							FROM ServiceArea 
							WHERE ServiceArea.name LIKE 'Hong Kong Island')
					)
					OR
					ServiceArea.service_area_ID IN(
						SELECT ServiceArea.service_area_ID
						FROM ServiceArea
						WHERE ServiceArea.parent_area_ID IN(
							SELECT ServiceArea.service_area_ID
							FROM ServiceArea
							WHERE ServiceArea.parent_area_ID IN
								(SELECT ServiceArea.service_area_ID
								FROM ServiceArea 
								WHERE ServiceArea.name LIKE 'Hong Kong Island')
						)
			)
			ORDER BY ServiceArea.name ASC, Locker.name ASC, ABS(LockerPickupOrder.order_ID) ASC;";

			$result = mysqli_query($conn, $sql);

			// Check if data exists
			if (mysqli_num_rows($result) > 0) {
			  // Output data of each row
			  while($row = mysqli_fetch_assoc($result)) {
			    echo "
			    <tr>
				    <td>" . $row["order_ID"] . "</td>
				    <td>" . $row["locker_name"] . "</td> 
				    <td>" . $row["service_area_name"] . "</td> 
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


<!-- SELECT LockerPickupOrder.order_ID, Locker.name AS locker_name, ServiceArea.name AS service_area_name
FROM LockerPickupOrder INNER JOIN Locker ON LockerPickupOrder.locker_ID = locker.locker_ID
INNER JOIN ServiceArea ON ServiceArea.service_area_ID = Locker.service_area_ID
WHERE ServiceArea.parent_area_ID IN (
    SELECT ServiceArea.service_area_ID
    FROM ServiceArea 
    WHERE ServiceArea.parent_area_ID IN (
        SELECT ServiceArea.service_area_ID
    	FROM ServiceArea 
        WHERE ServiceArea.name LIKE "Hong Kong Island"
        )
    )
ORDER BY ServiceArea.name ASC, Locker.name ASC, ABS(LockerPickupOrder.order_ID) ASC; -->


<!-- 
SELECT LockerPickupOrder.order_ID, Locker.name AS locker_name, ServiceArea.name AS service_area_name
FROM LockerPickupOrder INNER JOIN Locker ON LockerPickupOrder.locker_ID = locker.locker_ID
INNER JOIN ServiceArea ON ServiceArea.service_area_ID = Locker.service_area_ID
WHERE Locker.service_area_ID IN(
        SELECT ServiceArea.service_area_ID
    	FROM ServiceArea 
        WHERE ServiceArea.name LIKE "Hong Kong Island"
        )
    	OR
    	ServiceArea.service_area_ID IN(
            SELECT ServiceArea.service_area_ID
            FROM ServiceArea
            WHERE ServiceArea.parent_area_ID IN
                (SELECT ServiceArea.service_area_ID
                FROM ServiceArea 
                WHERE ServiceArea.name LIKE "Hong Kong Island")
        )
        OR
        ServiceArea.service_area_ID IN(
        	SELECT ServiceArea.service_area_ID
    		FROM ServiceArea
            WHERE ServiceArea.parent_area_ID IN(
            	SELECT ServiceArea.service_area_ID
                FROM ServiceArea
                WHERE ServiceArea.parent_area_ID IN
                    (SELECT ServiceArea.service_area_ID
                    FROM ServiceArea 
                    WHERE ServiceArea.name LIKE "Hong Kong Island")
            )
)
ORDER BY ServiceArea.name ASC, Locker.name ASC, ABS(LockerPickupOrder.order_ID) ASC; -->